package com.example.sm_tubo_plast.genesys.fuerza_ventas.Google;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.BEAN.Cliente;
import com.example.sm_tubo_plast.genesys.BEAN.MyItem;
import com.example.sm_tubo_plast.genesys.DAO.DAO_Cliente;
import com.example.sm_tubo_plast.genesys.adapters.AdapterCLientesMap;
import com.example.sm_tubo_plast.genesys.datatypes.DB_DireccionClientes;
import com.example.sm_tubo_plast.genesys.datatypes.DBclasses;
import com.example.sm_tubo_plast.genesys.hardware.LocationApiGoogle;
import com.example.sm_tubo_plast.genesys.hardware.Permiso_Adroid;
import com.example.sm_tubo_plast.genesys.hardware.RequestPermisoUbicacion;
import com.example.sm_tubo_plast.genesys.hardware.TaskCheckUbicacion;
import com.example.sm_tubo_plast.genesys.util.UtilView;
import com.example.sm_tubo_plast.genesys.util.UtilViewMensaje;
import com.example.sm_tubo_plast.genesys.util.VARIABLES;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;

public class MapsClientesActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "MapsClientesActivity";

    public static final int PERMISO_PARA_ACCEDER_A_LOCALIZACION = Permiso_Adroid.PERMISO_PARA_ACCEDER_A_LOCALIZACION;
    private GoogleMap mMap;
    ClusterManager<MyItem> mClusterManager;
    Location myUbicacionActual;
    LocationApiGoogle locationApiGoogle;
    TaskCheckUbicacion taskCheckUbicacion;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = Permiso_Adroid.PLAY_SERVICES_RESOLUTION_REQUEST;
    private static final long UPDATE_INTERVAL = LocationApiGoogle.UPDATE_INTERVAL_3_segundos, FASTEST_INTERVAL = LocationApiGoogle.FASTEST_INTERVAL_3_segundos; // = 3 seconds


    boolean isShowedMyUbicacion=false;

    DBclasses roomRepositorio;
    AdapterCLientesMap adapterCLientesMap;
    ArrayList<MyItem> ListaMarcadores_ok=new ArrayList<>();
    ArrayList<MyItem>  ListaMarcadores=new ArrayList<>();




    View my_fragment_map;
    NavigationView nav_view_map_cliente;
    FloatingActionButton myfab;
    ImageView img_close_nav_view;
    RecyclerView recyclerClienteMap;
    EditText edit_radios_show,editBuscarClientes;
    TextView txtLeyendaCabecera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps_clientes);
        Star_Check_Permiso_Ubicacion();

//         Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.my_fragment_map);
        mapFragment.getMapAsync(this);

    }

    private void GestionBotonMYFAB(){
        if (nav_view_map_cliente.getVisibility()==View.GONE){
            nav_view_map_cliente.setVisibility(View.VISIBLE);
            myfab.setVisibility(View.GONE);
            nav_view_map_cliente.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_left_in));
            my_fragment_map.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_left_in));
        }
    }
    public void StartActivity(){
        txtLeyendaCabecera=findViewById(R.id.txtLeyendaCabecera);
        nav_view_map_cliente=findViewById(R.id.nav_view_map_cliente);
        my_fragment_map=findViewById(R.id.my_fragment_map);
        myfab=findViewById(R.id.myfab);
        img_close_nav_view=findViewById(R.id.img_close_nav_view);
        recyclerClienteMap=findViewById(R.id.recyclerClienteMap);
        edit_radios_show=findViewById(R.id.edit_radios_show);
        editBuscarClientes=findViewById(R.id.editBuscarClientes);
        ImageView img_buscar_by_radio=findViewById(R.id.img_buscar_by_radio);
        ImageView img_buscar=findViewById(R.id.img_buscar);

        myfab.setVisibility(View.GONE);
        myfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GestionBotonMYFAB();
            }
        });
        img_close_nav_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myfab.setVisibility(View.VISIBLE);
                nav_view_map_cliente.setVisibility(View.GONE);
                nav_view_map_cliente.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_right_out));
                my_fragment_map.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_right_in));

                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(edit_radios_show.getWindowToken(), 0);
            }
        });

        roomRepositorio=new DBclasses(getApplicationContext()) ;


        UtilView.Efectos(getApplicationContext(), img_buscar_by_radio, R.color.yellow_500);
        img_buscar_by_radio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuscarClienteByRadio();
            }
        });
        UtilView.Efectos(getApplicationContext(), img_buscar, R.color.yellow_500);
        img_buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuscarClientes();
            }
        });
        TriggerClick_img_close_nav_view(500);
    }
    private void TriggerClick_img_close_nav_view(final  long tiempo){
        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    Thread.sleep(tiempo);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                img_close_nav_view.performClick();
            }
        }.execute();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
    @Override
    protected void onPause() {
        super.onPause();
        if (taskCheckUbicacion!=null){
            taskCheckUbicacion.RemoveLocation();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!LocationApiGoogle.checkPlayServices(this, PLAY_SERVICES_RESOLUTION_REQUEST)) {
            UtilViewMensaje.MENSAJE_simple(this, "Google Play", "Necesitas instalar Google Play Services para usar las ubicaciones de los clientes ");
        }else {
            if (taskCheckUbicacion!=null){
                taskCheckUbicacion.RequestLocationUpdates();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationApiGoogle!=null){
            if (locationApiGoogle.fusedLocationClient!=null && locationApiGoogle.locationCallback!=null){
                locationApiGoogle.fusedLocationClient.removeLocationUpdates(locationApiGoogle.locationCallback);
            }
        }
        if (taskCheckUbicacion!=null){
            taskCheckUbicacion.RemoveLocation();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
        starMapa( null, null);
        StartActivity();


        InicializarMapCluester();
        ListaMarcadores=getMarcadorClienteDireccionAll();
        editBuscarClientes.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    BuscarClientes();
                    return true;
                } else {
                    return false;
                }
            }
        });

        InicializarAdaptador();

        edit_radios_show.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    BuscarClienteByRadio();
                    return true;
                } else {
                    return false;
                }
            }
        });

    }

    private void Star_Check_Permiso_Ubicacion(){
        new RequestPermisoUbicacion(this, PERMISO_PARA_ACCEDER_A_LOCALIZACION, new RequestPermisoUbicacion.MyListener() {
            @Override
            public void Result(int isConcedido) {
                if (Permiso_Adroid.IS_PERMISO_DENEGADO==isConcedido){
                    UtilViewMensaje.MENSAJE_simple(MapsClientesActivity.this, "Permiso denegado", "No podras acceder a la ubicación");
                }
                else if (Permiso_Adroid.IS_PERMISO_CONCEDIDO==isConcedido){
                    StartUbicacionApiGoogle();

                }
            }
        });
    }

    private void StartUbicacionApiGoogle(){
        locationApiGoogle=new LocationApiGoogle(this, new LocationApiGoogle.Listener() {
            @Override
            public void onConnected(Bundle bundle) {

                taskCheckUbicacion= new TaskCheckUbicacion(MapsClientesActivity.this, new TaskCheckUbicacion.MyListener() {
                    @Override
                    public void result(boolean isOk) {
                        if (isOk) {
                            locationApiGoogle.ForzarUltimaUbicacion();
                            locationApiGoogle.StartLocationCallback(UPDATE_INTERVAL, FASTEST_INTERVAL, LocationRequest.PRIORITY_HIGH_ACCURACY);

                        }else{
                            locationApiGoogle.checkGPSActivate();
                        }
                    }
                });

            }

            @Override
            public void onConnectionSuspended(int i) {

            }

            @Override
            public void onConnectionFailed(ConnectionResult location) {

            }

            @Override
            public void LastLocation(Location location) {
                if (location!=null){
                    if (myUbicacionActual==null){
                        myUbicacionActual=location;
                        starMapa(location, "");
                        BuscarClientes();
                    }else{
                        myUbicacionActual=location;
                        if(adapterCLientesMap!=null)adapterCLientesMap.notifyDataSetChanged();
                    }

                    Log.i(TAG, "StartUbicacionApiGoogle:: LastLocation:: Latitude : " + location.getLatitude() + "Longitude : " + location.getLongitude());
                }
            }
        });

        locationApiGoogle.ApiLocationGoogleConectar();
    }

    private void BuscarClientes(){
        try {
            InicializarMapCluester();
            ListaMarcadores.clear();
            ListaMarcadores_ok.clear();
            ListaMarcadores=getMarcadorClienteDireccionAll();
            adapterCLientesMap.notifyDataSetChanged();
            InicializarAdaptador();

            if (edit_radios_show.getText().toString().length()>0){
                MostrarMarcadoresByRadio(edit_radios_show.getText().toString());
            }

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(MapsClientesActivity.this, "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void BuscarClienteByRadio(){
        try {
            MostrarMarcadoresByRadio(edit_radios_show.getText().toString());

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(MapsClientesActivity.this, "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void InicializarAdaptador(){
        ListaMarcadores_ok.addAll(ListaMarcadores);
        adapterCLientesMap=new AdapterCLientesMap(this, ListaMarcadores, new AdapterCLientesMap.MyListener() {
            @Override
            public int DistanciaEntreDosPuntos(double latitud, double logitud) {
                Location location=new Location("");
                location.setLatitude(latitud);
                location.setLongitude(logitud);
                if(myUbicacionActual!=null){
                    return (int)myUbicacionActual.distanceTo(location);
                }
                else return 0;

            }
        });
        recyclerClienteMap.setLayoutManager(new LinearLayoutManager(this));
        recyclerClienteMap.setAdapter(adapterCLientesMap);

        adapterCLientesMap.setOnClickListenerClientes(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyItem mcliente=ListaMarcadores.get(v.getId());

                LatLng sydney = new LatLng(mcliente.getPosition().latitude, mcliente.getPosition().longitude);

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 17.0f));

            }
        });



    }
    private void InicializarMapCluester(){
        mMap.clear();
        mClusterManager = new ClusterManager<MyItem>(this, mMap);
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        ListaMarcadores.clear();
        mClusterManager.setRenderer( new MarkerClusterRenderer(this, mMap, mClusterManager));
    }
    private void MostrarMarcadoresByRadio(String radio){

        InicializarMapCluester();
        int radioSelected=0;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        if (radio.length()>0){

            radioSelected=Integer.parseInt(radio);
            mClusterManager.setRenderer(new MarkerClusterRenderer(this, mMap, mClusterManager));
            CircleOptions circleOptions = new CircleOptions()
                    .center(new LatLng(myUbicacionActual.getLatitude(), myUbicacionActual.getLongitude()))
                    .radius(radioSelected)
                    .strokeColor(getResources().getColor(R.color.red_400))
                    .strokeWidth(4)
                    .fillColor(Color.argb(32, 33, 150, 243));
            Circle circle = mMap.addCircle(circleOptions);

            mClusterManager.clearItems();

            for (int i=0;i<ListaMarcadores_ok.size(); i++){
                MyItem marker=ListaMarcadores_ok.get(i);
                double distance2= getDistance(marker.getPosition().latitude, marker.getPosition().longitude, circle.getCenter().latitude, circle.getCenter().longitude);
                if( distance2 > circle.getRadius() ){

                }else{
                    Cliente cliente=(Cliente)marker.getTag();
                    MyItem offsetItem = new MyItem(marker.getPosition().latitude, marker.getPosition().longitude,
                            cliente.getNombre(), cliente.getDb_direccionClientes().getDireccion(),  cliente);
                    ListaMarcadores.add(offsetItem);
                    builder.include(offsetItem.getPosition());
                    mClusterManager.addItem(offsetItem);
                }
            }

        }else{
            ListaMarcadores.addAll(ListaMarcadores_ok);
            mClusterManager.addItems(ListaMarcadores);
            for (int i=0;i<ListaMarcadores_ok.size(); i++){
                MyItem marker=ListaMarcadores_ok.get(i);
                builder.include( new LatLng(marker.getPosition().latitude,marker.getPosition().latitude));
            }
        }

        if (ListaMarcadores.size()>0){
            GestionEnfoqueCamara(builder);
            TriggerClick_img_close_nav_view(700);
        }
        MostrarLeyendaForMaps(ListaMarcadores.size(),radioSelected);
        adapterCLientesMap.notifyDataSetChanged();
    }
    private void GestionEnfoqueCamara(LatLngBounds.Builder builder ){
        //**Gestion de enfoque*/
        final int zoomWidth = getResources().getDisplayMetrics().widthPixels;
        final int zoomHeight = getResources().getDisplayMetrics().heightPixels;
        final int zoomPadding = (int) (zoomWidth * 0.20); // offset from


        if (myUbicacionActual!=null){
            builder.include(new LatLng(myUbicacionActual.getLatitude(), myUbicacionActual.getLongitude()));
        }

        final LatLngBounds bounds = builder.build();
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds,zoomWidth,  zoomHeight,zoomPadding));
        //Fin gestion de enfdoque;
    }
    double getDistance(double LAT1, double LONG1, double LAT2, double LONG2) {
        double distance = 2 * 6371000 * Math.asin(Math.sqrt(Math.pow((Math.sin((LAT2 * (3.14159 / 180) - LAT1 * (3.14159 / 180)) / 2)), 2) + Math.cos(LAT2 * (3.14159 / 180)) * Math.cos(LAT1 * (3.14159 / 180)) * Math.sin(Math.pow(((LONG2 * (3.14159 / 180) - LONG1 * (3.14159 / 180)) / 2), 2))));
        return distance;
    }






//    @Override
//    public boolean onMarkerClick(Marker xmarker) {
//        if (xmarker.isInfoWindowShown()) {
//            xmarker.showInfoWindow();
//        }else{
//            xmarker.hideInfoWindow();
//        }
//        return true;
//    }
//
//    @Override
//    public void onInfoWindowClick(Marker marker) {
//        if (marker.isInfoWindowShown()) {
//            marker.showInfoWindow();
//        }else{
//            marker.hideInfoWindow();
//        }
//    }

    public void starMapa(Location ubicacion, String direccion){
        if (ubicacion!=null){
            if (!isShowedMyUbicacion){
                isShowedMyUbicacion=true;
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(ubicacion.getLatitude(), ubicacion.getLongitude()), 10));
            }
            mMap.setMyLocationEnabled(true);
        }
    }
//    public Marker AddMarker(TB_Cliente cliente, float icon){
//        LatLng sydney = new LatLng(cliente.getDircliente().getLatitud(), cliente.getDircliente().getLongitud());
//        Marker marker=mMap.addMarker(GetCustomMarker(""+cliente.getNomcli(), cliente.getDircliente().getDireccion(), sydney, icon));
    //  mMap.setOnInfoWindowClickListener(this);
//        mMap.setInfoWindowAdapter(new CustomMarkerInfoWindowView(this));
//        marker.setTag(cliente);
//        //marker.showInfoWindow();
//        return marker;
//    }
//
//    public MarkerOptions GetCustomMarker(String titli, String direccion,  LatLng position, float icon ){
//        MarkerOptions marker=new MarkerOptions();
//        marker.position(position);
//        marker.title(titli);
//        marker.snippet(direccion);
//        //marker.icon(BitmapDescriptorFactory.fromBitmap(icon));
//        marker.icon(BitmapDescriptorFactory.defaultMarker(icon));
//        return  marker;
//    }




//    @Override
//    public void onPolygonClick(Polygon polygon) {
//
//    }
//
//    @Override
//    public void onPolylineClick(Polyline polyline) {
//// Flip from solid stroke to dotted stroke pattern.
//        if ((polyline.getPattern() == null) || (!polyline.getPattern().contains(DOT))) {
//            polyline.setPattern(PATTERN_POLYLINE_DOTTED);
//        } else {
//            // The default pattern is a solid stroke.
//            polyline.setPattern(null);
//        }
//
//        Toast.makeText(this, "Route type " + polyline.getTag().toString(),
//                Toast.LENGTH_SHORT).show();
//    }





    public ArrayList<MyItem> getMarcadorClienteDireccionAll(){
        String texto_buscar="%"+(editBuscarClientes.getText().toString().replace(" ", "%"))+"%";

        DAO_Cliente dao_Cliente = new DAO_Cliente(getApplicationContext());
        Cursor cursor=dao_Cliente.getClienteDirrectionAll(texto_buscar);

        ArrayList<MyItem> arrayList=new ArrayList<>();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        while (cursor.moveToNext()){
            Cliente cliente=new Cliente();
            cliente.setCodigoCliente(cursor.getString(cursor.getColumnIndex("codcli")));
            cliente.setNombre(cursor.getString(cursor.getColumnIndex("nomcli")));
            DB_DireccionClientes dicli=new DB_DireccionClientes();
            dicli.setCodcli(cliente.getCodigoCliente());
            dicli.setTelefono(cursor.getString(cursor.getColumnIndex("telefono")));
            dicli.setLatitud(cursor.getString(cursor.getColumnIndex("latitud")));
            dicli.setLongitud(cursor.getString(cursor.getColumnIndex("longitud")));
            dicli.setDireccion(cursor.getString(cursor.getColumnIndex("direccion")));
            dicli.setEstado(cursor.getString(cursor.getColumnIndex("estado")));

            cliente.setDb_direccionClientes(dicli);
            MyItem offsetItem = new MyItem(Double.parseDouble(dicli.getLatitud()), Double.parseDouble(dicli.getLongitud()), cliente.getNombre(), dicli.getDireccion(),  cliente);
            arrayList.add(offsetItem);
            builder.include(offsetItem.getPosition());
        }
        cursor.close();
        mClusterManager.addItems(arrayList);
        MostrarLeyendaForMaps(ListaMarcadores_ok.size(), 0);
        if (arrayList.size()>0){
            GestionEnfoqueCamara(builder);
        }
        return arrayList;
    }
    private void MostrarLeyendaForMaps(int cantidad_filtado, int radioFilter){
        DAO_Cliente dao_Cliente = new DAO_Cliente(getApplicationContext());
        int cantidad_total=dao_Cliente.getClienteDirrectionAll();
        Cursor cursor1=dao_Cliente.getClienteDirrectionAll("%");
        int cantidadLocalizados=cursor1.getCount();
        String texto_buscar="%"+(editBuscarClientes.getText().toString().replace(" ", ""))+"%";
        Cursor cursor=dao_Cliente.getClienteDirrectionAll(""+texto_buscar);
        int cantidadLocalizadosFilter=cursor.getCount();
        cursor.close();
        if (TextUtils.isEmpty(editBuscarClientes.getText().toString()) && radioFilter==0){
            txtLeyendaCabecera.setText("Clientes Localizados "+cantidadLocalizados+" de "+cantidad_total);
        }else if(radioFilter==0){
            txtLeyendaCabecera.setText("Clientes Localizados "+cantidadLocalizados+" de "+cantidad_total+" \nCliente Filtrado "+cantidadLocalizadosFilter+" de "+cantidadLocalizados);
        }
        else if(radioFilter>0){
            String uniddad_medida= VARIABLES.ConvertirKmToString(radioFilter);
            txtLeyendaCabecera.setText("Clientes Localizados "+cantidadLocalizados+" de "+cantidad_total+"\nCliente Filtrado radio "+uniddad_medida+" "+cantidad_filtado+" de "+cantidadLocalizadosFilter);
        }
    }

    @Override
    public void onRequestPermissionsResult( int requestCode, String permissions[], int[] grantResults) {
        if (requestCode==PERMISO_PARA_ACCEDER_A_LOCALIZACION){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i("Permiso","Permiso concedido");
                Toast.makeText(this, "Permiso aceptado", Toast.LENGTH_SHORT).show();
                Star_Check_Permiso_Ubicacion();
            } else {

            }
        }
    }

}

