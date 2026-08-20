package com.example.sm_tubo_plast.genesys.fuerza_ventas.Dialog;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.BEAN.LugarEntrega;
import com.example.sm_tubo_plast.genesys.DAO.DAO_Cliente;
import com.example.sm_tubo_plast.genesys.datatypes.DB_DireccionClientes;
import com.example.sm_tubo_plast.genesys.datatypes.DB_RegistrosGenerales;
import com.example.sm_tubo_plast.genesys.datatypes.DBclasses;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.ClientesActivity;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.Google.service.GetDireccionByCoordenada;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.Google.util.UtilMarker;
import com.example.sm_tubo_plast.genesys.hardware.Permiso_Adroid;
import com.example.sm_tubo_plast.genesys.util.UtilView;
import com.example.sm_tubo_plast.genesys.util.VARIABLES;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class BottomSheetGeolocalizarCliente extends BottomSheetDialogFragment {
    private static final String TAG = "BottomSheetMapDialog";
    public static final int PERMISO_PARA_ACCEDER_A_LOCALIZACION = Permiso_Adroid.PERMISO_PARA_ACCEDER_A_LOCALIZACION;



    String codven, codcli, nomcli;
    int item_dircli;

    public static BottomSheetGeolocalizarCliente newInstance(
            String codven, String codcli, String nomcli, int item_dircli
    ) {
        BottomSheetGeolocalizarCliente fragment = new BottomSheetGeolocalizarCliente();
        Bundle args = new Bundle();
        args.putString("codven", codven);
        args.putString("codcli", codcli);
        args.putString("nomcli", nomcli);
        args.putInt("item_dircli", item_dircli);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new BottomSheetDialog(requireContext(), R.style.Theme_Dialog_Translucent);
    }



    TextView tv1;
    TextView tvActualGeolocalizacion, tv_localizacionNuevo;
    TextView btnVerMapsSinGeo;
    TextView tv_cliente;
    Spinner spn_direccion;
    Spinner spn_giro;
    TextView txt_valor_extra;
    TextView chckEditar;
    Button btnCancel, btnConfirm;

    GoogleMap mMap;
    Marker markerPost;

    LatLng coordenadaUbicacionCel=null;
    ArrayList<LugarEntrega> puntoEntregas=new ArrayList<>();
    DBclasses obj_dbclasses;


    private BottomSheetBehavior<View> bottomSheetBehavior;
    MyGeolocaliacionListener myListener;
    GetDireccionByCoordenada utilDireccionFromCoord=null;

    public void setMyGeolocaliacionListener(MyGeolocaliacionListener myListener) {
        this.myListener = myListener;
    }

    public interface MyGeolocaliacionListener {
        LatLng getLastUbicacion();
        void onCancel();
        void onChanged(int itemDireccion);
        void onEnvioServer();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View viewx = inflater.inflate(R.layout.dialog_geolocalizar, container, false);
        if (getArguments()!=null) {
            codven= getArguments().getString("codven", "");
            codcli= getArguments().getString("codcli", "");
            nomcli= getArguments().getString("nomcli", "");
            item_dircli= getArguments().getInt("item_dircli", -1);
        }

        //View alertLayout = inflater.inflate(R.layout.dialog_geolocalizar,null);
        tv1 = (TextView) viewx.findViewById(R.id.tv1);
        tvActualGeolocalizacion = (TextView) viewx.findViewById(R.id.tvActualGeolocalizacion);
        tv_localizacionNuevo = (TextView) viewx.findViewById(R.id.tv_localizacion);
        btnVerMapsSinGeo = (TextView) viewx.findViewById(R.id.btnVerMapsSinGeo);
        tv_cliente = (TextView) viewx.findViewById(R.id.tv_cliente);
        spn_direccion = (Spinner) viewx.findViewById(R.id.spn_direccion);
        spn_giro = (Spinner) viewx.findViewById(R.id.spn_giro);
        txt_valor_extra = (TextView) viewx.findViewById(R.id.txt_valor_extra);
        chckEditar = viewx.findViewById(R.id.chckEditar);
        btnCancel = (Button) viewx.findViewById(R.id.btnCancel);
        btnConfirm = (Button) viewx.findViewById(R.id.btnConfirm);
        desahabledBottomSheeetDraggable(viewx);
        initMap();

        return viewx;
    }

    private void desahabledBottomSheeetDraggable(final View view){
        view.post(new Runnable() {
            @Override
            public void run() {
                View parent = (View) view.getParent();
                if (parent != null) {
                    bottomSheetBehavior = BottomSheetBehavior.from(parent);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    bottomSheetBehavior.setDraggable(false);
                }
            }
        });
    }

    private void initMap(){
        SupportMapFragment[] mapFragment={null};
        mapFragment[0] =  getFragmentMapa();
        mapFragment[0].getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap _googleMap) {
                mMap = _googleMap;
                Toast.makeText(getActivity(), "Map iniciado", Toast.LENGTH_SHORT).show();
                setMyUbicacionEnabled();
                initData();
            }
        });
    }
    private SupportMapFragment getFragmentMapa(){
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager()
                        .findFragmentByTag("MAP_FRAGMENT");
        //SupportMapFragment mapFragment =  (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapGeo);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.mapGeo, mapFragment, "MAP_FRAGMENT")
                    .commit();
        }
        return mapFragment;
    }
    private void initData() {
        obj_dbclasses= new DBclasses(getActivity());
        DAO_Cliente daoCliente=new DAO_Cliente(getActivity());
        tv_cliente.setText(nomcli);
        coordenadaUbicacionCel = myListener.getLastUbicacion();
        setDireccionNombre(tv_localizacionNuevo, coordenadaUbicacionCel);
        moveCameraEnfoqueMapa(coordenadaUbicacionCel);

        puntoEntregas  = daoCliente.getPuntoEntrega(codcli, ""+item_dircli);
        List<String> direccionesList = new ArrayList<String>();
        for (int i=0;i<puntoEntregas.size();i++) {
            LugarEntrega db_DireccionClientes = puntoEntregas.get(i);
            direccionesList.add(db_DireccionClientes.getDireccion());
        }

        ArrayAdapter<String> direccionAdapter = new ArrayAdapter<String>(getActivity(),R.layout.spinner_item, direccionesList);
        spn_direccion.setAdapter(direccionAdapter);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myListener.onCancel();
                dismiss();
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(markerPost==null){
                    Toast.makeText(getActivity(), "Por favor, seleccione una ubicación en mapa.", Toast.LENGTH_SHORT).show();
                    return;
                }
                String itemPuntoEntrega="0";
                if (!puntoEntregas.isEmpty()) {
                    itemPuntoEntrega = puntoEntregas.get(spn_direccion.getSelectedItemPosition()).getCodigoLugar();
                }

                obj_dbclasses.updateGeolocalizacionClientePuntoEntrega(codcli,
                        String.valueOf(item_dircli),
                        itemPuntoEntrega,
                        markerPost.getPosition().latitude, markerPost.getPosition().longitude, 0.0);

                myListener.onEnvioServer();
                dismiss();
//                new ClientesActivity.asyncEnviarGeolocalizacionCliente().execute();

//                WS_DireccionCliente ws_direccionCliente=new WS_DireccionCliente(getActivity());
//                ws_direccionCliente.setEnvioDireccion(codcli, itemDireccion);
//
//                String finalItemDireccion = itemDireccion;
//                ws_direccionCliente.EnviarDirecciones(mensaje -> {
//                    Toast toast = Toast.makeText(getActivity(), mensaje,	Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
//                    myListener.onChanged(Integer.parseInt(finalItemDireccion));
//                    dismiss();
//                });


            }
        });

        btnVerMapsSinGeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                IntentTerceros intentTerceros =new IntentTerceros(getActivity());
//                intentTerceros.IntentGoogleMaps(coordenadaUbicacionCel, true);

            }
        });
        gestionarEnventoSpinnerDireccion();
        chckEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioLimite=Integer.parseInt(obj_dbclasses.getConfiguracionByName("metros_radio_dragg_posicion_manual", "500"));
                final BottomSheetMapDialog dialog=BottomSheetMapDialog.newInstance(radioLimite);
                dialog.setOnLocationSelectedListener(new BottomSheetMapDialog.OnLocationSelectedListener() {
                    @Override
                    public void onLocationSelected(LatLng latLng) {
                        moveCameraEnfoqueMapa(latLng);
                        markerPost = getMarkerCustom(markerPost, mMap, latLng);
                        setDireccionNombre(tv_localizacionNuevo, latLng);
                        setDireccionNombre(tvActualGeolocalizacion, latLng);

                    }

                    @Override
                    public void onCancel() {

                    }
                });
                dialog.show(getActivity().getSupportFragmentManager(), "BottomSheetMapDialog");
            }
        });


        //tv_localizacionActual.setText(""+coordenadaActual.latitude+", "+coordenadaActual.longitude);


    }
    private void gestionarEnventoSpinnerDireccion(){
        spn_direccion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    String estado = obj_dbclasses.getEstadoDireccionClientePuntoEntrega(codcli, String.valueOf(item_dircli), puntoEntregas.get(i).getCodigoLugar());

                    if (estado.equals("O")) {//Pendiente de localizar
                        estado="Atención esta dirreción está pendiente por localizar.";
                    }
                    else if (estado.equals("P")) {//Pendiente de localizar
                        estado="Atención esta dirreción está pendiente por enviar al servidor.";
                    }
                    tv1.setText(estado);
                    txt_valor_extra.setText("Fecha geolocalizado NO DATA INFO");

                    if(Double.parseDouble(puntoEntregas.get(i).getLatitud())==0.0){
                        tvActualGeolocalizacion.setText("Ubicación no geolocalizada");
                        markerPost = getMarkerCustom(markerPost, mMap, null);
                        tv_localizacionNuevo.setText("Ubicación no geolocalizada. \nPor favor seleccione una ubicación en el mapa.");
                        moveCameraEnfoqueMapa(coordenadaUbicacionCel);
                    }
                    else{
                        LatLng posi=new LatLng(Double.parseDouble(puntoEntregas.get(i).getLatitud()),Double.parseDouble(puntoEntregas.get(i).getLongitud()));
                        markerPost = getMarkerCustom(markerPost, mMap, posi);
                        setDireccionNombre(tvActualGeolocalizacion, posi);
                        tv_localizacionNuevo.setText(tvActualGeolocalizacion.getText().toString());
                        moveCameraEnfoqueMapa(posi);
                    }

                }catch (Exception e){
                    UtilView.MENSAJES(getActivity(),  "Error!",
                            "\n\n Detalle del error:\n"+e.getMessage(), 0,false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setMyUbicacionEnabled() {
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    private Marker getMarkerCustom(Marker marker, GoogleMap map, LatLng ubicacion){
        if(marker!=null){marker.remove(); marker=null;}
        if(ubicacion==null){
            return null;
        }

        MarkerOptions markerOptions = new MarkerOptions()
                .position(ubicacion)
                .zIndex(100)
                .icon(BitmapDescriptorFactory.fromBitmap(
                                new UtilMarker(getActivity())
                                        .getMarkerIconWithLabel(null)
                        )
                )
                .title("Ubicación seleccionada")
                .draggable(false);

        return map.addMarker(markerOptions);
    }

    private void setDireccionNombre(TextView textView, LatLng locationx){
        utilDireccionFromCoord=new GetDireccionByCoordenada(getActivity(), locationx, new GetDireccionByCoordenada.MyCallback() {
            @Override
            public void result(String direccionName) {
                textView.setText(direccionName);
            }
        });
        utilDireccionFromCoord.setShowLoading(false);
        utilDireccionFromCoord.execute();
    }
    private void moveCameraEnfoqueMapa(LatLng latLngx) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngx, 17));
    }

}
