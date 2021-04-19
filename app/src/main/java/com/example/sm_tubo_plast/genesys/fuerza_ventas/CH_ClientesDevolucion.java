package com.example.sm_tubo_plast.genesys.fuerza_ventas;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.datatypes.DBClientes;
import com.example.sm_tubo_plast.genesys.datatypes.DBMotivo_noventa;
import com.example.sm_tubo_plast.genesys.datatypes.DBPedido_Cabecera;
import com.example.sm_tubo_plast.genesys.datatypes.DBclasses;
import com.example.sm_tubo_plast.genesys.service.ConnectionDetector;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.HashMap;

import me.piruin.quickaction.ActionItem;
import me.piruin.quickaction.QuickAction;

@SuppressLint("LongLogTag")
public class CH_ClientesDevolucion extends AppCompatActivity {

    static final String KEY_CODCLI = "codcli";
    static final String KEY_CLIENTE = "cliente";
    static final String KEY_RUC = "ruc";
    String ruc;
    double lat;
    double lng;
    String numOc;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> lista_clientes = new ArrayList();
    ListView list;
    Clientes_LazyAdapter cli_adapter;
    DBclasses obj_dbclasses;
    DBClientes db_clientes;
    String nomcli;
    String codven;
    private LocationManager locationManager;
    private String provider = null;
    int selectedPosition1 = 0;
    int dia;
    String Fecha;
    private static final int ID_INFO = 4;
    private static final int ID_DEVOLUCION = 7;
    private LocationListener Loclistener;
    Boolean isInternetPresent = false;
    private int mSelectedRow = 0;
    ConnectionDetector cd;
    private ImageView mMoreIv = null;
    EditText inputSearch;
    Dialog dialogo;
    ProgressDialog pDialog;
    DBMotivo_noventa item;
    Clientes_LazyAdapter adapter;
    HashMap<String, Object> temp;
    ArrayList<HashMap<String, Object>> searchResults;
    ArrayList<HashMap<String, Object>> originalValues;
    DBPedido_Cabecera itemCabecera;
    Builder dialogo1;
    Builder dialogo2;
    String codcli;
    int arti = 0;
    int item_direccion = 0;
    ImageButton btn_scan;
    private EditText alert_edt;
    private boolean isAlert = false;
    SharedPreferences prefs;
    String _usuario;
    String _pass;

    public CH_ClientesDevolucion() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_h__clientes_devolucion);




        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        originalValues = new ArrayList<HashMap<String, Object>>();
        obj_dbclasses = new DBclasses(getApplicationContext());
        list = (ListView) findViewById(R.id.lv_clientes);
        inputSearch = (EditText) findViewById(R.id.inputSearch);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Bundle bundle = getIntent().getExtras();
        codven = "" + bundle.getString("codven");

        btn_scan = (ImageButton) findViewById(R.id.main_clienteslyt_btnScan);

        dia = obj_dbclasses.getDiaConfiguracion();
        Fecha = obj_dbclasses.getFecha2();

        prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        _usuario = prefs.getString("usuario", "0");
        _pass = prefs.getString("pass", "0");

        btn_scan.setVisibility(View.GONE);
        btn_scan.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // instantiate ZXing integration class
                IntentIntegrator scanIntegrator = new IntentIntegrator(
                        CH_ClientesDevolucion.this);
                // start scanning
                scanIntegrator.initiateScan();
            }
        });
        // new cargarClientes().execute("todos");

        new cargarClientes().execute("visita");

        /* ***************ENVIAR MENSAJE DE SINCRONIZACION************** */
        SharedPreferences preferencias_configuracion;
        preferencias_configuracion = getSharedPreferences("preferencias_configuracion", Context.MODE_PRIVATE);
        boolean sincronizacionCorrecta = preferencias_configuracion.getBoolean("preferencias_sincronizacionCorrecta", false);
        if (sincronizacionCorrecta == false) {
            Builder alerta = new Builder(CH_ClientesDevolucion.this);
            alerta.setTitle("Sincronizacion incompleta");
            alerta.setMessage("Los datos estan incompletos, sincronice correctamente");
            alerta.setIcon(R.drawable.icon_warning);
            alerta.setCancelable(false);
            alerta.setPositiveButton("OK", null);
            alerta.show();
        }
        /****************************************************************/

        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the location provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location = null;

        if (provider != null) {
            Log.w("Provider Pedido Activity", "Provider: " + provider
                    + " has been selected.");

            if (locationManager.isProviderEnabled(provider)) {
                Log.w("Provider Pedido Activity", "provider: " + provider
                        + " Habilitado");
                // location = locationManager.getLastKnownLocation(provider);
            } else {
                Log.w("Provider Pedido Activity", "provider: " + provider
                        + " deshabilitado");
                // Intent myIntent = new
                // Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                // startActivity(myIntent);
            }
        } else {
            Log.w("Provider Pedido Activity", "Provider: " + provider
                    + " has been selected.");
        }

        //
        Loclistener = new LocationListener() {

            public void onLocationChanged(Location location) {
                Log.w("Localizacion cambio", "");
                obtenerLocalizacion(location);
                // Toast.makeText(getApplicationContext(),"localizacion cambio Lat:"+lat+", Lng:"+lng+"",
                // Toast.LENGTH_LONG).show();
            }

            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {
                // TODO Auto-generated method stub

            }

            public void onProviderEnabled(String provider) {
                Log.w("Provider", "Provider Habilitado");
            }

            public void onProviderDisabled(String provider) {
                Log.w("Provider", "Provider Deshabilitado");
            }

        };
        //

        obtenerLocalizacion(location);

        ActionItem infoItem = new ActionItem(ID_INFO, "Informacion",R.drawable.icon_man_24dp);
        ActionItem devolucionItem = new ActionItem(ID_DEVOLUCION, "Devolución",R.drawable.icon_check_24dp);

        final QuickAction mQuickAction = new QuickAction(this);
        final QuickAction mQuickAction2 = new QuickAction(this);

        mQuickAction.addActionItem(infoItem);
        mQuickAction.addActionItem(devolucionItem);

        mQuickAction2.addActionItem(infoItem);
        mQuickAction2.addActionItem(devolucionItem);

        // ///////////////////Acciones para mQuickAction///////////////////////
        // setup the action item click listener
        mQuickAction
                .setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
                    @Override
                    public void onItemClick(ActionItem item) {
                        accion_segunId(item.getActionId());
                    }

                });



        // ///////////////////////////////////////////////////////////////////////

        // ///////////////Acciones para mQuickAction2////////////////////////
        // setup the action item click listener
        mQuickAction2
                .setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
                    @Override
                    public void onItemClick(ActionItem item) {
                        accion_segunId(item.getActionId());
                    }
                });


        // ////////////////////////////////////////////////////////////////

        list.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                mSelectedRow = position; // set the selected row

//                mMoreIv = (ImageView) view.findViewById(R.id.i_more);
                ruc = ((TextView) view.findViewById(R.id.tv_ruc)).getText().toString();
                String nomcli2 = ((TextView) view.findViewById(R.id.tv_cliente)).getText().toString();
                String dir = ((TextView) view.findViewById(R.id.list_item_direccion)).getText().toString();
                nomcli = nomcli2;

                item_direccion = Integer.parseInt(dir);

                codcli = obj_dbclasses.obtenerCodigoCliente(nomcli);

                if (obj_dbclasses.existePedidoCabeceraXcodcli_item(codcli,item_direccion)) {
                    mQuickAction.show(view);
                } else {
                    mQuickAction2.show(view);
                }

                // mMoreIv.setImageResource(R.drawable.ic_list_more_selected);
            }
        });

        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    searchResults.clear();

                    if (TextUtils.isDigitsOnly(charSequence)) {
                        for (int x = 0; x < originalValues.size(); x++) {
                            String ruc = originalValues.get(x).get("ruc").toString();
                            if (ruc.indexOf(""+charSequence) != -1) {
                                searchResults.add(originalValues.get(x));
                            }
                        }
                    } else {
                        for (int x = 0; x < originalValues.size(); x++) {
                            String nombreCliente = originalValues.get(x).get("name").toString();
                            if (nombreCliente.indexOf(charSequence.toString().toUpperCase()) != -1) {
                                searchResults.add(originalValues.get(x));
                            }
                        }
                    }
                    Log.d("ClientesActivity", "texto cambiado tamaño de la lista: " + searchResults.size());
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public class Clientes_LazyAdapter extends
            ArrayAdapter<HashMap<String, Object>> {
        Activity context;
        private ArrayList<HashMap<String, Object>> data;

        private ArrayList<HashMap<String, String>> mOriginalValues;

        public Clientes_LazyAdapter(Context context, int textViewResourceId,
                                    ArrayList<HashMap<String, Object>> Strings) {

            // let android do the initializing :)
            super(context, textViewResourceId, Strings);
        }

        private class ViewHolder {

            TextView name, ruc, observacion, fecha, monto, direccion;
            ImageView foto;

        }

        ViewHolder viewHolder;

        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {

                convertView = inflater
                        .inflate(R.layout.list_item_cliente, null);
                viewHolder = new ViewHolder();

                // cache the views

                viewHolder.name = (TextView) convertView
                        .findViewById(R.id.tv_cliente);
                viewHolder.ruc = (TextView) convertView
                        .findViewById(R.id.tv_ruc);
                viewHolder.foto = (ImageView) convertView
                        .findViewById(R.id.item_cliente_iv_foto);
                viewHolder.observacion = (TextView) convertView
                        .findViewById(R.id.list_item_cliente_observacion);
                viewHolder.fecha = (TextView) convertView
                        .findViewById(R.id.list_item_cliente_fecha);
                viewHolder.monto = (TextView) convertView
                        .findViewById(R.id.list_item_cliente_monto);
                viewHolder.direccion = (TextView) convertView
                        .findViewById(R.id.list_item_direccion);
                // link the cached views to the convertview
                convertView.setTag(viewHolder);

            } else
                viewHolder = (ViewHolder) convertView.getTag();
            try {

                int i = obj_dbclasses.obtenerPedidosXCodcli(
                        searchResults.get(position).get("codigo").toString(),
                        searchResults.get(position).get("item_direccion")
                                .toString());

                Log.w("SITIO_ENFA", i + "obtenerPedidosXCODCLI");
                if (i == 1) {
                    // convertView.setBackgroundColor(getResources().getColor(R.color.color_pedidos_realizados));
                    viewHolder.foto.setImageDrawable(getResources()
                            .getDrawable(R.drawable.cliente_verde));

                } else if (i == 2) {
                    // convertView.setBackgroundResource(R.drawable.list_selector);
                    viewHolder.foto.setImageDrawable(getResources()
                            .getDrawable(R.drawable.cliente_rojo));
                } else if (i == 0) {
                    viewHolder.foto.setImageDrawable(getResources()
                            .getDrawable(R.drawable.cliente_gris));
                }

                // set the data to be displayed
                // viewHolder.photo.setImageDrawable(photoId));
                if (searchResults.get(position).get("n_dia").toString()
                        .equals(Integer.toString(dia))) {
                    viewHolder.name.setTextColor(Color.parseColor("#4682B4"));
                    viewHolder.name.setText(searchResults.get(position)
                            .get("name").toString());
                } else {
                    viewHolder.name.setTextColor(Color.parseColor("#000000"));
                    viewHolder.name.setText(searchResults.get(position)
                            .get("name").toString());
                }
                viewHolder.ruc.setText(searchResults.get(position).get("ruc")
                        .toString());

                String obs = "";

                try {
                    obs = obj_dbclasses.getObservacionCliente(searchResults
                            .get(position).get("codigo").toString());
                } catch (Exception e) {
                    Log.w("LOG CLIENTES ACTIVITY ADAPTER", e);
                }

                // viewHolder.observacion.setText(searchResults.get(position).get("observacion").toString());
                viewHolder.observacion.setText(obs);

                viewHolder.monto.setText(searchResults.get(position)
                        .get("monto").toString());
                viewHolder.fecha.setText(searchResults.get(position)
                        .get("fecha").toString());
                viewHolder.direccion.setText(searchResults.get(position)
                        .get("item_direccion").toString());

            } catch (Exception e) {
                Log.w("LOG CLIENTES ACTIVITY", e);
            }
            // return the view to be displayed
            return convertView;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class cargarClientes extends
            AsyncTask<String, Integer, ArrayList<HashMap<String, Object>>> {
        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();

        protected void onPreExecute() {
            // para el progress dialog
            pDialog = new ProgressDialog(CH_ClientesDevolucion.this);
            pDialog.setMessage("Cargando Clientes...");
            pDialog.setIndeterminate(false);
            pDialog.show();
        }

        @Override
        protected ArrayList<HashMap<String, Object>> doInBackground(
                String... params) {
            String buscar = params[0];

            try {

                originalValues = obj_dbclasses.getProgramacionxDia2("", 0, 50000, 50);
                originalValues.addAll(obj_dbclasses.getDemasClientes2());

            }

            catch (Exception e) {
                Log.e("log_tag", "Error parsing data " + e.toString());
            }
            return originalValues;
        }

        @Override
        protected void onPostExecute(ArrayList<HashMap<String, Object>> result) {
            pDialog.dismiss();// ocultamos progress dialog.
            Log.e("onPostExecute= DEPOSITOs", "" + result);

            searchResults = new ArrayList<HashMap<String, Object>>(
                    originalValues);

            adapter = new Clientes_LazyAdapter(CH_ClientesDevolucion.this,
                    R.layout.main_clientes, searchResults);

            LayoutInflater inflater = LayoutInflater
                    .from(CH_ClientesDevolucion.this);
            View emptyView = inflater
                    .inflate(R.layout.list_empty_clients, null);
            emptyView.setVisibility(View.GONE);
            ((ViewGroup) list.getParent()).addView(emptyView);
            list.setEmptyView(emptyView);

            // cli_adapter=new Clientes_LazyAdapter(this, searchResults);
            list.setAdapter(adapter);

            btn_scan.setEnabled(true);

        }
    }

    protected void onResume() {
        super.onResume();
        if (provider != null && locationManager.isProviderEnabled(provider)) {
            locationManager.requestLocationUpdates(provider, 400, 1,
                    Loclistener);
        }
    }

    /* Remove the locationlistener updates when Activity is paused */

    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(Loclistener);
    }

    public void obtenerLocalizacion(Location location) {
        if (location != null) {
            lat = (double) (location.getLatitude());
            lng = (double) (location.getLongitude());
        } else {

            /*
             * String[] localizacion = new String[2];
             *
             * int itm_dir = spnDireccion.getSelectedItemPosition();
             * localizacion = dbclass.Obtener_localizacion(codcli, itm_dir);
             *
             * lat = Double.parseDouble(localizacion[0]);// lng =
             * Double.parseDouble(localizacion[1]);//
             */

            // if(lat == 0.0 || lng == 0.0){
            if (provider != null) {
                Location loc = locationManager.getLastKnownLocation(provider);
                if (loc != null) {
                    lat = (double) (loc.getLatitude());
                    lng = (double) (loc.getLongitude());
                }
            }
            // }

        }
        Log.w("Obtener Localizacion", "Lat:" + lat + ", Lng:" + lng + "");
    }

    private void accion_segunId(int actionId) {

        if (actionId == ID_INFO) {
            Intent i = new Intent(getApplicationContext(),
                    ClienteDetalleActivity.class);

            i.putExtra("CODCLI", codcli);

            startActivity(i);

        }else if (actionId == ID_DEVOLUCION) {
            final Intent i = new Intent(getApplicationContext(),CH_DevolucionesActivity.class);
            i.putExtra("origen", "CLIENTES");
            i.putExtra("nombreCliente", nomcli);
            i.putExtra("codigoVendedor", codven);
            startActivity(i);
            finish();
        }else {

        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // retrieve result of scanning - instantiate ZXing object
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(
                requestCode, resultCode, intent);
        // check we have a valid result
        if (scanningResult != null) {
            // get content from Intent Result
            String scanContent = scanningResult.getContents();
            // get format name of data scanned
            String scanFormat = scanningResult.getFormatName();

            // output to UI
            // formatTxt.setText("FORMAT: "+scanFormat);
            // contentTxt.setText("CONTENT: "+scanContent);

            if (scanContent != null && scanFormat != null) {

                if (scanFormat.equals("QR_CODE")) {
                    //

                    if (!isAlert) {
                        searchResults.clear();

                        String nombreCliente = "";
                        int textLength = scanContent.length();

                        for (int i = 0; i < originalValues.size(); i++) {

                            String playerName = originalValues.get(i).get("codigo").toString();
                            Log.d("playerName: ",playerName);

                            nombreCliente = originalValues.get(i).get("name").toString();
                            Log.d("nombreCliente: ",nombreCliente);

                            if (textLength <= playerName.length()) {
                                // compare the String in EditText with Names in
                                // the ArrayList
                                Log.d("if (textLength <= playerName.length()) ","true");
                                if (scanContent.equalsIgnoreCase(playerName.substring(0, textLength))) {
                                    searchResults.add(originalValues.get(i));
                                    Log.d("searchResults.add", originalValues.get(i).toString());
                                    break;
                                }

                            }

                        }

                        // inputSearch.setText(nombreCliente);
                        adapter.notifyDataSetChanged();
                        //

                        if (!searchResults.isEmpty()) {
                            Log.d("searchResults no vacio","");
                            View view = list.getAdapter().getView(0, null, null);

                            list.performItemClick(view, 0, 0);
                            inputSearch.setText(nombreCliente);
                            Log.d("inputSearch.setText",nombreCliente);
                        }
                    } else {
                        alert_edt.setText(scanContent);
                    }

                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "FORMATO INCORRECTO", Toast.LENGTH_SHORT);
                    toast.show();
                }

            } else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Ningun dato recibido", Toast.LENGTH_SHORT);
                toast.show();
            }

        } else {
            // invalid scan data or scan canceled
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Ningun dato recibido", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

}