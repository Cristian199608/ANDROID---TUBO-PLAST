package com.example.fuerzaventaschema.genesys.fuerza_ventas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fuerzaventaschema.R;
import com.example.fuerzaventaschema.genesys.BEAN.San_Visitas;
import com.example.fuerzaventaschema.genesys.DAO.DAO_San_Visitas;
import com.example.fuerzaventaschema.genesys.datatypes.DBClientes;
import com.example.fuerzaventaschema.genesys.datatypes.DBMotivo_noventa;
import com.example.fuerzaventaschema.genesys.datatypes.DBPedido_Cabecera;
import com.example.fuerzaventaschema.genesys.datatypes.DBSync_soap_manager;
import com.example.fuerzaventaschema.genesys.datatypes.DB_DireccionClientes;
import com.example.fuerzaventaschema.genesys.datatypes.DBclasses;
import com.example.fuerzaventaschema.genesys.fuerza_ventas.Google.MapsClientesActivity;
import com.example.fuerzaventaschema.genesys.fuerza_ventas.cliente.CH_InformacionCliente;
import com.example.fuerzaventaschema.genesys.fuerza_ventas.cliente.CH_RegistroClientesNuevos;
import com.example.fuerzaventaschema.genesys.service.ConnectionDetector;
import com.example.fuerzaventaschema.genesys.util.GlobalFunctions;
import com.example.fuerzaventaschema.genesys.util.GlobalVar;
import com.example.fuerzaventaschema.genesys.util.UtilView;
import com.example.fuerzaventaschema.genesys.util.VARIABLES;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.piruin.quickaction.ActionItem;
import me.piruin.quickaction.QuickAction;

@SuppressLint("LongLogTag")
public class ClientesActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    public static final String TAG= "ClientesActivity";
    static final String KEY_CODCLI = "codcli"; // parent node
    static final String KEY_CLIENTE = "cliente";
    static final String KEY_RUC = "ruc";
    String ruc;String flagTipoEnvio = "P";
    double lat, lng;
    String numOc;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> lista_clientes = new ArrayList<HashMap<String, String>>();
    // PedidosActivity ob= new PedidosActivity();
    ListView list;
    Clientes_LazyAdapter cli_adapter;
    DBclasses obj_dbclasses;
    DBClientes db_clientes;
    String nomcli, codven;
    private LocationManager locationManager;
    private String provider = null;
    int selectedPosition1 = 0;
    int dia;
    String Fecha;
    private static final int ID_PEDIDO = 1;
    private static final int ID_COBRANZA = 2;
    private static final int ID_NO_VENTA = 3;
    private static final int ID_INFO = 4;
    private static final int ID_OBSERVACION = 5;
    private static final int ID_INFOWEB = 6;
    private static final int ID_DEVOLUCION = 7;
    private static final int ID_COTIZACION = 8;
    private static final int ID_VISITA_CLIENTE = 9;
    private static final int ID_LOCALIZACION = 10;

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

    AlertDialog.Builder dialogo1, dialogo2;
    String codcli, codSucursal, estadoLocalizacion;
    int arti = 0, item_direccion = 0;

    FloatingActionButton myFAB;



    private EditText alert_edt;
    private boolean isAlert = false;

    SharedPreferences prefs;
    String _usuario;
    String _pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);


        // para que no salga el teclado al iniciar
        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Clientes");
        setSupportActionBar(toolbar);
        originalValues = new ArrayList<HashMap<String, Object>>();

        obj_dbclasses = new DBclasses(getApplicationContext());
        list = (ListView) findViewById(R.id.lv_clientes);
        inputSearch = (EditText) findViewById(R.id.inputSearch);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Bundle bundle = getIntent().getExtras();
        codven = "" + bundle.getString("codven");


        dia = obj_dbclasses.getDiaConfiguracion();
        Fecha = obj_dbclasses.getFecha2();

        prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        _usuario = prefs.getString("usuario", "0");
        _pass = prefs.getString("pass", "0");



        myFAB = (FloatingActionButton) findViewById(R.id.myFAB);
        myFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                starWebCreateCliente(false);

            }
        });
        myFAB.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                starWebCreateCliente(true);
                return false;
            }
        });


        // El espinner "(xvisita,todos)" esta invisible, asi que
        // itemSelectedListener no funciona
        // cargo los clientes directamente
        new cargarClientes().execute("visita");

        /* ***************ENVIAR MENSAJE DE SINCRONIZACION************** */
        SharedPreferences preferencias_configuracion;
        preferencias_configuracion = getSharedPreferences("preferencias_configuracion", Context.MODE_PRIVATE);
        boolean sincronizacionCorrecta = preferencias_configuracion.getBoolean("preferencias_sincronizacionCorrecta", false);
        if (sincronizacionCorrecta == false) {
            AlertDialog.Builder alerta = new AlertDialog.Builder(ClientesActivity.this);
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


        ActionItem infoItem = new ActionItem(ID_INFO, "Informacion", R.drawable.icon_man_24dp);
        //ActionItem infoWebItem = new ActionItem(ID_INFOWEB,"Informacion Online", getResources().getDrawable(R.drawable.infoweb));
        ActionItem addItem = new ActionItem(ID_PEDIDO, "Orden Compra",R.drawable.pedidopn);
        ActionItem acceptItem = new ActionItem(ID_COBRANZA, "Cobranza", R.drawable.icon_coins_24dp);
        ActionItem uploadItem = new ActionItem(ID_NO_VENTA, "Motivo no venta", R.drawable.icon_stop_24dp);
        ActionItem gestionVisita = new ActionItem(ID_VISITA_CLIENTE, "Gestión visita", R.drawable.icon_man_24dp);
        ActionItem observacion = new ActionItem(ID_OBSERVACION, "Observacion", R.drawable.alert);
        ActionItem devolucionItem = new ActionItem(ID_DEVOLUCION, "Devolución", R.drawable.icon_check_24dp);
        ActionItem cotizacionItem = new ActionItem(ID_COTIZACION, "Cotizacion", R.drawable.icon_survey_24dp);
        ActionItem geolocalizacion = new ActionItem(ID_LOCALIZACION, "Localización", R.drawable.ic_ubicacion_grey);

        final QuickAction mQuickAction = new QuickAction(this);
        final QuickAction mQuickAction2 = new QuickAction(this);

        mQuickAction.addActionItem(infoItem);
        mQuickAction.addActionItem(geolocalizacion);
        //mQuickAction.addActionItem(infoWebItem);
        mQuickAction.addActionItem(addItem);
        mQuickAction.addActionItem(acceptItem);
        mQuickAction.addActionItem(uploadItem);
        mQuickAction.addActionItem(gestionVisita);
        mQuickAction.addActionItem(observacion);
        mQuickAction.addActionItem(devolucionItem);
        mQuickAction.addActionItem(cotizacionItem);

        mQuickAction2.addActionItem(infoItem);
        mQuickAction2.addActionItem(geolocalizacion);
        //mQuickAction2.addActionItem(infoWebItem);
        mQuickAction2.addActionItem(addItem);
        mQuickAction2.addActionItem(acceptItem);
        mQuickAction2.addActionItem(uploadItem);
        mQuickAction2.addActionItem(gestionVisita);
        mQuickAction2.addActionItem(observacion);
        mQuickAction2.addActionItem(devolucionItem);
        mQuickAction2.addActionItem(cotizacionItem);


        // ///////////////////Acciones para mQuickAction///////////////////////
        // setup the action item click listener
        mQuickAction
                .setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
                    @Override
                    public void onItemClick(ActionItem item) {

                        int actionId = item.getActionId();
                        accion_segunId(actionId);
                    }

                });


        // ///////////////////////////////////////////////////////////////////////

        // ///////////////Acciones para mQuickAction2////////////////////////
        // setup the action item click listener
        mQuickAction2
                .setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
                    @Override
                    public void onItemClick(ActionItem item) {
                        int actionId = item.getActionId();
                        accion_segunId(actionId);
                    }


                });


        // ////////////////////////////////////////////////////////////////

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                inputMethodManager.hideSoftInputFromWindow(inputSearch.getWindowToken(), 0);

                //Toast.makeText(getApplicationContext(), "Ocultando teclado", Toast.LENGTH_LONG).show();

                mSelectedRow = position; // set the selected row

               // mMoreIv = (ImageView) view.findViewById(R.id.i_more);
                ruc = ((TextView) view.findViewById(R.id.tv_ruc)).getText().toString();
                String nomcli2 = ((TextView) view.findViewById(R.id.tv_cliente)).getText().toString();
                String dir = ((TextView) view.findViewById(R.id.list_item_direccion)).getText().toString();

                nomcli = nomcli2;

                item_direccion = Integer.parseInt(dir);

                codcli = obj_dbclasses.obtenerCodigoCliente(nomcli);
                codSucursal = obj_dbclasses.obtenerCodigoSucursalCliente(codcli, codven);
                estadoLocalizacion = obj_dbclasses.obtenerEstadoSucursalCliente(codcli, codSucursal);

                if (obj_dbclasses.existePedidoCabeceraXcodcli_item(codcli, item_direccion)) {
                    mQuickAction.show(view);
                } else {
                    mQuickAction2.show(view);
                }

                // mMoreIv.setImageResource(R.drawable.ic_list_more_selected);
            }
        });

        inputSearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                inputSearch.setInputType(InputType.TYPE_CLASS_TEXT);

            }
        });

		/*
		inputSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				// When user changed the Text
				String searchString = inputSearch.getText().toString();
				int textLength = inputSearch.length();
				try {
					searchResults.clear();

					for (int i = 0; i < originalValues.size(); i++) {
						String playerName = originalValues.get(i).get("name").toString();

						if (textLength <= playerName.length()) {
							if (searchString.equalsIgnoreCase(playerName.substring(0, textLength)))
								searchResults.add(originalValues.get(i));
						}
					}
					adapter.notifyDataSetChanged();
				} catch (Exception e) {

				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
			}

		});
		*/


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
                            if (ruc.indexOf("" + charSequence) != -1) {
                                searchResults.add(originalValues.get(x));
                            }
                        }
                    } else {
                        for (int x = 0; x < originalValues.size(); x++) {
                            String nombreCliente = originalValues.get(x).get("name").toString();
                            String direccion = originalValues.get(x).get("direccion").toString();
                            if (nombreCliente.indexOf(charSequence.toString().toUpperCase()) != -1 || direccion.contains(charSequence.toString().toUpperCase())) {
                                searchResults.add(originalValues.get(x));
                            }

                        }
                    }
                    Log.d("ClientesActivity", "texto cambiado tama�o de la lista: " + searchResults.size());
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

    private void starWebCreateCliente(boolean forzar) {
        if ((lat!=0.0 && lng!=0.0) || forzar){
            Log.i(TAG, "Valor de COD_VEN ES " + codven+" coordenadas "+lat+", "+lng);
            Intent intReportes = new Intent(getApplicationContext(), CreacionNuevoCliente2Activity.class);
            intReportes.putExtra("COD_VEND", codven);
            intReportes.putExtra("LATITUD", String.valueOf(lat));
            intReportes.putExtra("LONGITUD", String.valueOf(lng));
            startActivity(intReportes);
        }else {
            UtilView.MENSAJE_simple(this, "Sin coordenadas",  "No se ha obtenido las coordenadas. \nVerifique que tengas activado el GPS, ó mantén presionado para registrar sin geolocalizar. " +
                    "Vuelva intentar.");
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        inputSearch.setText(newText);
        return false;
    }

    public class Clientes_LazyAdapter extends
            ArrayAdapter<HashMap<String, Object>> {
        Activity context;
        private ArrayList<HashMap<String, Object>> data;

        private ArrayList<HashMap<String, String>> mOriginalValues;
        String fecha_yyyy_mm_dd="";
        public Clientes_LazyAdapter(Context context, int textViewResourceId,
                                    ArrayList<HashMap<String, Object>> Strings) {

            // let android do the initializing :)
            super(context, textViewResourceId, Strings);
            fecha_yyyy_mm_dd= VARIABLES.GetFechaStringFrom_dd_mm_yyyy_TO_yyyy_mm_dd(obj_dbclasses.getFecha2());
        }

        private class ViewHolder {

            TextView txt_flag_pedido,name, ruc, observacion, fecha, monto, direccion,item_fecha_visitado, item_fecha_programada;
            ImageView foto;

        }

        ViewHolder viewHolder;

        @SuppressLint("LongLogTag")
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {

                convertView = inflater  .inflate(R.layout.list_item_cliente, null);
                viewHolder = new ViewHolder();

                // cache the views

                viewHolder.name = (TextView) convertView .findViewById(R.id.tv_cliente);
                viewHolder.txt_flag_pedido = (TextView) convertView .findViewById(R.id.txt_flag_pedido);
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
                viewHolder.direccion = (TextView) convertView.findViewById(R.id.list_item_direccion);
                viewHolder.item_fecha_visitado = (TextView) convertView.findViewById(R.id.item_fecha_visitado);
                viewHolder.item_fecha_programada = (TextView) convertView.findViewById(R.id.item_fecha_programada);
                // link the cached views to the convertview
                convertView.setTag(viewHolder);

            } else
                viewHolder = (ViewHolder) convertView.getTag();
            try {

                int i = obj_dbclasses.obtenerPedidosXCodcli(
                        searchResults.get(position).get("codigo").toString(),
                        searchResults.get(position).get("item_direccion").toString());




                Log.w("SITIO_ENFA", i + "obtenerPedidosXCODCLI");
                if (i == 1) {
                    // convertView.setBackgroundColor(getResources().getColor(R.color.color_pedidos_realizados));
                    viewHolder.foto.setImageDrawable(getResources()
                            .getDrawable(R.drawable.icon_cliente_verde));

                } else if (i == 2) {
                    // convertView.setBackgroundResource(R.drawable.list_selector);
                    viewHolder.foto.setImageDrawable(getResources()
                            .getDrawable(R.drawable.icon_cliente_rojo));
                } else if (i == 3) {
                    // convertView.setBackgroundResource(R.drawable.list_selector);
                    viewHolder.foto.setImageDrawable(getResources()
                            .getDrawable(R.drawable.icon_cliente_orange));
                }
                else if (i == 0) {
                    viewHolder.foto.setImageDrawable(getResources()
                            .getDrawable(R.drawable.icon_cliente_gris));
                }

                // set the data to be displayed
                // viewHolder.photo.setImageDrawable(getResources().getDrawable(photoId));
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

                    String estado = obj_dbclasses.getEstadoDireccionCliente(searchResults.get(position).get("codigo").toString(), searchResults.get(position).get("item_direccion").toString());

                    //String estado = searchResults.get(position).get("estado").toString();
                    if (estado.equals("L")) {//Localizado
                        viewHolder.txt_flag_pedido.setBackgroundColor(getResources().getColor(R.color.yellow_600));
                    } else if (estado.equals("V")) {//Localizado y Validado
                        viewHolder.txt_flag_pedido.setBackgroundColor(getResources().getColor(R.color.green_500));
                    } else if (estado.equals("P")) {//Pendiente de localizar
                        viewHolder.txt_flag_pedido.setBackgroundColor(getResources().getColor(R.color.grey_400));
                    } else {
                        viewHolder.txt_flag_pedido.setBackgroundColor(getResources().getColor(R.color.grey_400));
                    }

                    obs = obj_dbclasses.getObservacionCliente(searchResults
                            .get(position).get("codigo").toString());
                } catch (Exception e) {
                    Log.w("LOG CLIENTES ACTIVITY ADAPTER", e);
                }

                // viewHolder.observacion.setText(searchResults.get(position).get("observacion").toString());
                viewHolder.observacion.setText(obs);

                viewHolder.monto.setText(searchResults.get(position)
                        .get("monto").toString());
                viewHolder.fecha.setText(searchResults.get(position).get("direccion").toString()+"\n" +
                                 searchResults.get(position).get("fecha").toString());
                viewHolder.direccion.setText(searchResults.get(position)
                        .get("item_direccion").toString());


                San_Visitas visitas = DAO_San_Visitas.getSan_VisitasByFecha(
                        obj_dbclasses.getReadableDatabase(),
                        searchResults.get(position).get("codigo").toString(),
                        searchResults.get(position).get("item_direccion").toString()
                );

                if (visitas!=null){
                    String fecha_visitado=visitas.getFecha_Ejecutada();
                    String fecha_next_visita=visitas.getFecha_proxima_visita();
                    viewHolder.item_fecha_visitado.setTextColor(getResources().getColor(
                            (fecha_visitado.contains(fecha_yyyy_mm_dd)?R.color.teal_700:R.color.grey_700)
                            )
                    );
                    viewHolder.item_fecha_programada.setTextColor(getResources().getColor(
                            (fecha_next_visita.contains(fecha_yyyy_mm_dd)?R.color.blue_500:R.color.grey_700)
                            )
                    );

                    viewHolder.item_fecha_visitado.setText(fecha_visitado.length()>0?"Visitado: "+fecha_visitado:"");
                    viewHolder.item_fecha_programada.setText(fecha_next_visita.length()>0?"P.Visitar: "+fecha_next_visita:"");

                }else{
                    viewHolder.item_fecha_visitado.setText("");
                    viewHolder.item_fecha_programada.setText("");
                }

            } catch (Exception e) {
                Log.w("LOG CLIENTES ACTIVITY", e);
            }
            // return the view to be displayed
            return convertView;
        }

    }

    public void crearDialogo_noventa() {

        dialogo = new Dialog(this);
        numOc = obj_dbclasses.obtenerNumOC(codven);
        dialogo.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialogo.setContentView(R.layout.dialog_motivo_noventa);
        dialogo.setCancelable(false);

        final ListView lstNoventa_motivo = (ListView) dialogo
                .findViewById(R.id.dialog_motivo_noventa_lstNo_venta);
        Button btnAceptar = (Button) dialogo
                .findViewById(R.id.dialog_motivo_noventa_btnAceptar);
        Button btnCancelar = (Button) dialogo
                .findViewById(R.id.dialog_motivo_noventa_btnCancelar);
        lstNoventa_motivo.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        ArrayList<DBMotivo_noventa> motivos = new ArrayList<DBMotivo_noventa>();
        motivos = obj_dbclasses.obtenerMotivo_noventa();
        no_ventaAdapter adaptador = new no_ventaAdapter(this, motivos);
        lstNoventa_motivo.setAdapter(adaptador);

        lstNoventa_motivo
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> adapter, View arg1,
                                            int position, long id) {

                        selectedPosition1 = position;

                    }
                });

        btnAceptar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                item = (DBMotivo_noventa) lstNoventa_motivo.getAdapter()
                        .getItem(selectedPosition1);

                dialogo.dismiss();

                final CheckBox checkBox=UtilView.GetCheckBoxEnvioSistema(ClientesActivity.this);

                AlertDialog.Builder builder = new AlertDialog.Builder(ClientesActivity.this);
                builder.setTitle("Importante");
                builder.setView(checkBox);
                builder.setMessage("Se guardaran todos los datos");
                builder.setCancelable(false);
                builder.setIcon(R.drawable.ic_alert);
                builder.setPositiveButton("Guardar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {
                                // Se guarda el motivo de no_venta, y recupero el oc_numero que
                                // se haya generado
                                final String orden_compra = guardarCabeceraPedido(item.getCod_noventa(), "");

                                if (checkBox.isChecked()){
                                    new asyncGuardarMotivo().execute(orden_compra);

                                }else{
                                    ((Clientes_LazyAdapter) list.getAdapter()).notifyDataSetChanged();
                                    dialogo.dismiss();
                                }
                            }

                        });
                builder.setNegativeButton("Cancelar",null);

                builder.show();

            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialogo.dismiss();
            }
        });

        dialogo.show();

    }

    public class no_ventaAdapter extends ArrayAdapter<DBMotivo_noventa> {

        Activity context;
        ArrayList<DBMotivo_noventa> datos;

        public no_ventaAdapter(Activity context,
                               ArrayList<DBMotivo_noventa> datos) {

            super(context, android.R.layout.simple_list_item_single_choice,
                    datos);
            this.context = context;
            this.datos = datos;
        }

        public View getView(int position, View convertview, ViewGroup parent) {

            View item = convertview;
            ViewHolder holder;

            if (item == null) {
                LayoutInflater inflater = context.getLayoutInflater();
                item = inflater.inflate(
                        android.R.layout.simple_list_item_single_choice, null);

                holder = new ViewHolder();

                holder.des_noventa = (TextView) item
                        .findViewById(android.R.id.text1);

                item.setTag(holder);

            } else {

                holder = (ViewHolder) item.getTag();

            }

            holder.des_noventa.setText(datos.get(position).getDes_noventa());

            return item;
        }

        public class ViewHolder {
            TextView des_noventa;
        }
    }

    public String guardarCabeceraPedido(int cod_noventa, String observcion) {

        itemCabecera = new DBPedido_Cabecera();

        String fecha_configuracion = obj_dbclasses.getCambio("Fecha");
        itemCabecera.setOc_numero(codven + calcularSecuencia(numOc, fecha_configuracion));
        // SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        // Date date = new Date();
        // String codcli= obj_dbclasses.obtenerCodigoCliente(nomcli);
        // itemCabecera.setFecha_oc( Fecha+" "+dateFormat.format(date));
        itemCabecera.setFecha_oc(GlobalFunctions.getFecha_configuracion(getApplicationContext()) + " " + GlobalFunctions.getHoraActual());
        itemCabecera.setFecha_mxe("");
        itemCabecera.setMoneda("");
        itemCabecera.setSubTotal("0");
        itemCabecera.setFlagPedido_Anticipo("0");
        itemCabecera.setEstado("A");
        itemCabecera.setValor_igv("0");
        itemCabecera.setUsername(obj_dbclasses.getNombreUsuarioXcodvend(codven));
        itemCabecera.setRuta("ruta");
        itemCabecera.setCond_pago("");
        itemCabecera.setCod_cli(codcli);
        itemCabecera.setCod_emp(codven);
        itemCabecera.setObservacion(observcion);
        itemCabecera.setCod_noventa(cod_noventa);
        itemCabecera.setPeso_total("0.0");
        itemCabecera.setFlag("P");
        itemCabecera.setLatitud(lat + "");
        itemCabecera.setLongitud(lng + "");
        itemCabecera.setSitio_enfa("" + item_direccion);
        itemCabecera.setCodigo_familiar("");
        itemCabecera.setTipoRegistro("");

        obj_dbclasses.AgregarPedidoCabecera(itemCabecera);

        return itemCabecera.getOc_numero();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Alternativa 1
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_clientes, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView =  (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this);



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clientes_menu_mapa:
                Intent intent=new Intent(this, MapsClientesActivity.class);
                startActivity(intent);
                return true;
//            case R.id.clientes_menu_registrados:
//                Intent intentr = new Intent(ClientesActivity.this, CH_RegistroClientesNuevos.class);
//                startActivity(intentr);
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class asyncGuardarMotivo extends AsyncTask<String, String, String> {

        String user, pass;

        protected void onPreExecute() {
            // para el progress dialog
            pDialog = new ProgressDialog(ClientesActivity.this);
            pDialog.setMessage("Guardando motivo de no venta....");
            pDialog.setIndeterminate(false);

            pDialog.show();
        }

        protected String doInBackground(String... params) {
            // obtnemos usr y pass

            String oc_numero = params[0];
            String valor = "ok";

            DBSync_soap_manager soap_manager = new DBSync_soap_manager(
                    getApplicationContext());

            ConnectionDetector cd = new ConnectionDetector(
                    getApplicationContext());

            if (cd.hasActiveInternetConnection(getApplicationContext())) {

                try {
                    soap_manager.actualizarObjPedido_directo(oc_numero);
                } catch (JsonParseException ex) {
                    // exception al parsear json
                    valor = "error_2";
                } catch (Exception ex) {
                    // cualquier otra exception al enviar los datos
                    valor = "error_3";
                }

            } else {
                // Sin conexion al servidor
                valor = "error_1";
            }

            return valor;
        }


        protected void onPostExecute(String result) {

            pDialog.dismiss();// ocultamos progess dialog.

            Log.e("onPostExecute= DEPOSITOs", "" + result);

            String mensaje = "";

            if (result.equals("ok")) {
                mensaje = "Envio Correcto";
            } else if (result.equals("error_1")) {
                mensaje = "Sin conexion al Servidor";
            } else if (result.equals("error_2")) {
                mensaje = "Error en el envio";
            } else if (result.equals("error_3")) {
                mensaje = "No se pudo verificar";
            }

            Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_LONG).show();
            ((Clientes_LazyAdapter) list.getAdapter()).notifyDataSetChanged();

        }

    }

    class cargarClientes extends
            AsyncTask<String, Integer, ArrayList<HashMap<String, Object>>> {
        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();

        protected void onPreExecute() {
            // para el progress dialog
            pDialog = new ProgressDialog(ClientesActivity.this);
            pDialog.setMessage("Cargando Clientes...");
            pDialog.setIndeterminate(false);
            pDialog.show();
        }

        @Override
        protected ArrayList<HashMap<String, Object>> doInBackground(
                String... params) {
            String buscar = params[0];

            try {

                originalValues = obj_dbclasses.getProgramacionxDia2();
                originalValues.addAll(obj_dbclasses.getDemasClientes2());

            } catch (Exception e) {
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

            adapter = new Clientes_LazyAdapter(ClientesActivity.this,
                    R.layout.main_clientes, searchResults);

            LayoutInflater inflater = LayoutInflater
                    .from(ClientesActivity.this);
            View emptyView = inflater
                    .inflate(R.layout.list_empty_clients, null);
            emptyView.setVisibility(View.GONE);
            ((ViewGroup) list.getParent()).addView(emptyView);
            list.setEmptyView(emptyView);

            // cli_adapter=new Clientes_LazyAdapter(this, searchResults);
            list.setAdapter(adapter);


        }
    }

    protected void onResume() {
        super.onResume();
        if (provider != null && locationManager.isProviderEnabled(provider)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
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

    public static String calcularSecuencia(String oc,String fecha_configuracionx) {
        String cero = "0";
        String orden = "";

        // obtengo la fecha de la tabla configuracion
        // String fecha_configuracion = dbclass.getCambio("Fecha");

        // String mes_actual=(calendar.get(Calendar.MONTH)+1)+"";
        // String dia_actual=calendar.get(Calendar.DAY_OF_MONTH)+"";

        String mes_actual = fecha_configuracionx.substring(3, 5);
        String dia_actual = fecha_configuracionx.substring(0, 2);

        int secactual = 0;

        Log.d("calculando secuencia...", oc+"");

        if (oc.length() < 6) {
            Log.d("calculando secuencia...", oc+" < 6");
            secactual = 1;
            if (mes_actual.length() < 2) {
                mes_actual = cero + mes_actual;
            }
            if (dia_actual.length() < 2) {
                dia_actual = cero + dia_actual;
            }
            Log.d("calculando secuencia...","orden = "+mes_actual +"+"+ dia_actual +"+"+ cero +"+"+ secactual);

            orden = mes_actual + dia_actual + cero + secactual;
            return orden;
        } else {
            Log.d("calculando secuencia...", oc+" > 6");
            String cadenaFecha = oc.substring(oc.length()-6,oc.length());

            int mest = Integer.parseInt(cadenaFecha.substring(0, 2));
            int diat = Integer.parseInt(cadenaFecha.substring(2, 4));
            int sectem = Integer.parseInt(cadenaFecha.substring(4, 6));

            Log.d("calculando secuencia...","diat:"+diat);
            Log.d("calculando secuencia...","mest:"+mest);
            Log.d("calculando secuencia...","sectem:"+sectem);

            if (Integer.parseInt(mes_actual) <= mest) {
                Log.d("calculando secuencia...","mes_actual < mest");
                if (Integer.parseInt(dia_actual) > diat) {
                    secactual = 1;
                    Log.d("calculando secuencia...","secactual = 1;  ->"+secactual);
                } else
                    secactual = sectem + 1;
                Log.d("calculando secuencia...","secactual = sectem + 1; ->"+secactual);

            } else {
                secactual = 1;
                Log.d("calculando secuencia...","secactual = 1;");
            }
        }

        if (mes_actual.length() < 2) {
            mes_actual = cero + mes_actual;
        }
        if (dia_actual.length() < 2) {
            dia_actual = cero + dia_actual;
        }
        if (secactual < 10) {
            Log.d("calculando secuencia...","secactual < 10");
            orden = mes_actual + dia_actual + cero + secactual;
            Log.d("calculando secuencia...","orden = mes_actual + dia_actual + cero + secactual;");
            Log.d("calculando secuencia...","orden = "+mes_actual +"+"+ dia_actual +"+"+ cero +"+"+ secactual);
        } else {
            Log.d("calculando secuencia...","secactual > 10");
            Log.d("calculando secuencia...","orden = mes_actual + dia_actual + secactual");
            orden = mes_actual + dia_actual + secactual;
            Log.d("calculando secuencia...","orden = "+mes_actual +"+"+ dia_actual +"+"+ secactual);
        }

        return orden;
    }

    private void accion_segunId(final int actionId) {

        if (actionId == ID_PEDIDO) {
            finish();

            String vistaSeleccionado;
            boolean stockAuto;

            /* Obtenemos las preferencias guardadas anteriormente */
            SharedPreferences preferencias_configuracion = getSharedPreferences("preferencias_configuracion", Context.MODE_PRIVATE);
            String preferenciasJSON = preferencias_configuracion.getString("preferenciasJSON", "no_preferencias");
            Log.d("preferenciasJSON", preferenciasJSON);

            if (preferenciasJSON.equals("no_preferencias")) {
                vistaSeleccionado = "Vista 2";
                stockAuto = false;
            } else {
                Gson gson = new Gson();
                Type listType = new TypeToken<Map<String, Object>>() {}.getType();

                Map<String, Object> mapObject2 = gson.fromJson(preferenciasJSON, listType);
                vistaSeleccionado = (String) mapObject2.get("preferencias_vista");
                stockAuto = (Boolean) mapObject2.get("preferencias_stock");
                List<String> almacenAsociado = (List<String>) mapObject2.get("preferencias_almacen");
                Log.d("DATOS:", "\nvista_Seleccionado: " + vistaSeleccionado+ "\nstock: " + stockAuto + "\nalmacen_seleccionado: "+ almacenAsociado);
            }

            if (vistaSeleccionado.equals("Vista 1")) {
                final Intent i = new Intent(getApplicationContext(),PedidosActivityVentana2.class);
                i.putExtra("codven", codven);
                i.putExtra("NOMCLI", nomcli);
                i.putExtra("ORIGEN", "clientesac");
                i.putExtra("DIRECCION", item_direccion);
                startActivity(i);

            } else if (vistaSeleccionado.equals("Vista 2")) {
                final Intent i = new Intent(getApplicationContext(),PedidosActivity.class);
                i.putExtra("origen", "CLIENTES");
                i.putExtra("nombreCliente", nomcli);
                i.putExtra("codigoVendedor", codven);
                i.putExtra("tipoRegistro", PedidosActivity.TIPO_PEDIDO);
                startActivity(i);
            } else {
                Toast.makeText(getApplicationContext(),	"Puede configurar el tipo de visualizacion en Preferencias",Toast.LENGTH_SHORT).show();
            }

        } else if (actionId == ID_OBSERVACION) {
            AlertDialog.Builder editalert = new AlertDialog.Builder(
                    ClientesActivity.this);

            editalert.setTitle("Observación");
            editalert.setMessage("Ingrese observación");
            /*
             * final EditText input = new EditText(ClientesActivity.this);
             *
             * LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
             * LinearLayout.LayoutParams.MATCH_PARENT,
             * LinearLayout.LayoutParams.MATCH_PARENT);
             * input.setLayoutParams(lp);
             */

            View view = inflater.inflate(R.layout.cliente_alert_observacion,
                    null);

            editalert.setView(view);

            isAlert = true;

            final EditText input = (EditText) view
                    .findViewById(R.id.cliente_alert_observacion_edt);
            final Button boton = (Button) view
                    .findViewById(R.id.cliente_alert_observacion_btn);

            alert_edt = input;

            boton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    // instantiate ZXing integration class
                    IntentIntegrator scanIntegrator = new IntentIntegrator(
                            ClientesActivity.this);
                    // start scanning
                    scanIntegrator.initiateScan();

                }
            });

            editalert.setPositiveButton("guardar",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {

                            obj_dbclasses.guardarObservacion2(input.getText()
                                    .toString(), codcli);

                            ((Clientes_LazyAdapter) list.getAdapter())
                                    .notifyDataSetChanged();

                            isAlert = false;

                        }
                    });
            editalert.setNegativeButton("cancelar",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {

                            isAlert = false;

                        }
                    });

            editalert.show();

        } else if (actionId == ID_NO_VENTA) {

            crearDialogo_noventa();

        }
        else if (actionId == ID_VISITA_CLIENTE) {
            Intent intent=new Intent(this, GestionVisita3Activity.class);
            intent.putExtra("ID_RRHH", codcli);
            intent.putExtra("CODIGO_CRM", "");
            intent.putExtra("NOMBRE_INSTI", "Sin nombre");
            intent.putExtra("OC_NUMERO", "");
            intent.putExtra("COD_VEND", codven);
            intent.putExtra("ORIGEN", TAG);
            startActivity(intent);

            /*AlertDialog.Builder editalert = new AlertDialog.Builder(                    ClientesActivity.this);
            editalert.setTitle("Control Visita");
            editalert.setMessage("Ingrese una descripción o una observción para la visita");

            final EditText input = new EditText(ClientesActivity.this);
            final LinearLayout layout = new LinearLayout(ClientesActivity.this);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.addView(input);

            final CheckBox checkBox=UtilView.GetCheckBoxEnvioSistema(ClientesActivity.this);
            layout.addView(checkBox);
            editalert.setView(layout);
            editalert.setPositiveButton("guardar",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int whichButton) {
                            numOc = obj_dbclasses.obtenerNumOC(codven);
                            if (input.getText().toString().trim().length()>0) {

                                final String orden_compra = guardarCabeceraPedido(GlobalVar.CODIGO_VISITA_CLIENTE, input.getText().toString());
                                if (checkBox.isChecked()){
                                    new asyncGuardarMotivo().execute(orden_compra);
                                }else{
                                    ((Clientes_LazyAdapter) list.getAdapter()).notifyDataSetChanged();
                                    dialogo.dismiss();
                                }
                            }else{
                                Toast.makeText(ClientesActivity.this, "Ingrese una observación", Toast.LENGTH_SHORT).show();
                                accion_segunId(actionId);
                            }
                        }
                    });
            editalert.setNegativeButton("cancelar",null);

            editalert.show();*/


        }else if (actionId == ID_INFO) {
            Intent intent = new Intent(ClientesActivity.this, CH_InformacionCliente.class);
            intent.putExtra("codigoCliente", codcli);
            startActivity(intent);
        } else if (actionId == ID_INFOWEB) {
            Intent intent = new Intent(ClientesActivity.this,CH_InformacionCliente.class);
            intent.putExtra("codigoCliente", codcli);
            startActivity(intent);
        }else if (actionId == ID_DEVOLUCION) {
            final Intent i = new Intent(getApplicationContext(),CH_DevolucionesActivity.class);
            i.putExtra("origen", "CLIENTES");
            i.putExtra("nombreCliente", nomcli);
            i.putExtra("codigoVendedor", codven);
            startActivity(i);
            finish();
        }else if (actionId == ID_COTIZACION) {
            final Intent i = new Intent(getApplicationContext(),PedidosActivity.class);
            i.putExtra("origen", "CLIENTES");
            i.putExtra("nombreCliente", nomcli);
            i.putExtra("codigoVendedor", codven);
            i.putExtra("tipoRegistro", PedidosActivity.TIPO_COTIZACION);
            startActivity(i);
            finish();
        }  else if (actionId == ID_LOCALIZACION) { //**Nuevo Localizacion

            if(estadoLocalizacion.equals("V")){
                Toast toast2 =  Toast.makeText(getApplicationContext(),  "La geolocalización de este cliente esta validada y no se podra modificar.", Toast.LENGTH_SHORT);
                toast2.show();
            }else{
                Log.d("La posición actual es :", lat + "-" + lng + "-" + codcli + "-" + codSucursal);
                flagTipoEnvio= "S";

                View alertLayout = inflater.inflate(R.layout.dialog_geolocalizar, null);
                final TextView tv1 = (TextView) alertLayout.findViewById(R.id.tv1);
                final TextView tv_localizacionActual = (TextView) alertLayout.findViewById(R.id.tv_localizacionActual);
                final TextView tv_localizacion = (TextView) alertLayout.findViewById(R.id.tv_localizacion);
                final TextView btnVerMapsGeo = (TextView) alertLayout.findViewById(R.id.btnVerMapsGeo);
                final TextView btnVerMapsSinGeo = (TextView) alertLayout.findViewById(R.id.btnVerMapsSinGeo);
                final TextView tv_cliente = (TextView) alertLayout.findViewById(R.id.tv_cliente);
                final Spinner spn_direccion = (Spinner) alertLayout.findViewById(R.id.spn_direccion);


                tv_localizacion.setText(lat+" , "+lng);
                tv_cliente.setText(nomcli);

                final ArrayList<DB_DireccionClientes> direcciones = obj_dbclasses.obtenerDirecciones_cliente2(codcli);
                List<String> direccionesList = new ArrayList<String>();
                int posicionDireccionHoy = 0;
                for (int i=0;i<direcciones.size();i++) {
                    DB_DireccionClientes db_DireccionClientes = direcciones.get(i);

                    direccionesList.add(db_DireccionClientes.getDireccion());
                    if (db_DireccionClientes.getItem().equals(codSucursal)) {
                        posicionDireccionHoy = i;
                    }
                }

                ArrayAdapter<String> direccionAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item, direccionesList);
                spn_direccion.setAdapter(direccionAdapter);
                //Seleccionar la direccion cliente de la visita actual (programcion zonificacion)
                spn_direccion.setSelection(posicionDireccionHoy);




                AlertDialog.Builder builder = new AlertDialog.Builder(ClientesActivity.this,AlertDialog.THEME_HOLO_LIGHT);

                ///builder.setTitle("Importante");
                builder.setIcon(R.drawable.warning);
                builder.setView(alertLayout);
                builder.setCancelable(true)
                        .setPositiveButton("Enviar al servidor", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String itemDireccion="";
                                int secuenciaGiro=0;
                                if (!direcciones.isEmpty()) {
                                    itemDireccion = direcciones.get(spn_direccion.getSelectedItemPosition()).getItem();
                                }



                                obj_dbclasses.updateGeolocalizacionCliente(codcli,itemDireccion,lat,lng);

                                new asyncEnviarGeolocalizacionCliente().execute();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Local", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String itemDireccion="";
                                int secuenciaGiro=0;
                                if (!direcciones.isEmpty()) {
                                    itemDireccion = direcciones.get(spn_direccion.getSelectedItemPosition()).getItem();
                                }


                                obj_dbclasses.updateGeolocalizacionCliente(codcli,itemDireccion,lat,lng);


                                Log.d(TAG, "itemDireccion"+itemDireccion+"\nsecuenciaGiro"+secuenciaGiro+"\nSucursal:"+codSucursal);
                                ((Clientes_LazyAdapter) list.getAdapter()) .notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        });

                btnVerMapsSinGeo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LatLng coordenadaLast=new LatLng (lat,lng);
                        lanzarGooleMaps(coordenadaLast);
                    }
                });

                //tv_localizacionActual.setText(""+coordenadaActual.latitude+", "+coordenadaActual.longitude);

                spn_direccion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        try {
                            String estado = obj_dbclasses.getEstadoDireccionCliente(codcli, direcciones.get(i).getItem());

                            if (estado.equals("P")) {//Pendiente de localizar
                                estado="Atención esta dirreción está pendiente por localizar.\nSe guardarán los siguientes datos:";
                            }else estado="Se guardarán los siguientes datos:";
                            tv1.setText(estado);

                            tv_localizacionActual.setText(Double.parseDouble(direcciones.get(i).getLatitud())+", "+Double.parseDouble(direcciones.get(i).getLongitud()));
                        }catch (Exception e){
                            UtilView.MENSAJES(ClientesActivity.this,  "Error!",
                                    "\n\n Detalle del error:\n"+e.getMessage(), 0,false);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                btnVerMapsGeo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int posDir=spn_direccion.getSelectedItemPosition();
                        try {
                            LatLng coordenadaLast=new LatLng (Double.parseDouble(direcciones.get(posDir).getLatitud()),Double.parseDouble(direcciones.get(posDir).getLongitud()));
                            lanzarGooleMaps(coordenadaLast);
                        }catch (Exception e){
                            UtilView.MENSAJES(ClientesActivity.this,  "Error!",
                                    "Error al intentar abrir GOOGLE MAPS\n\n Detalle del error:\n"+e.getMessage(), 0,false);
                        }
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }



        }
        else {

            if (obj_dbclasses.VerificarCtasXCobrar(codcli).size() > 0) {
                Intent i = new Intent(getApplicationContext(),
                        CuentasXCobrarActivity2.class);
                i.putExtra("CODCLI", codcli);
                i.putExtra("NOMCLI", nomcli);
                i.putExtra("ORIGEN", "clienteac");

                startActivity(i);

            } else {
                Toast.makeText(getApplicationContext(), "No tiene deudas",
                        Toast.LENGTH_SHORT).show();
            }

        }

    }

    public void lanzarGooleMaps(LatLng latLng) {
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + latLng.latitude + "," + latLng.longitude + "(Ubicación \n" + "Seleccionada" + ")");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(ClientesActivity.this);
            dialog.setCancelable(true);
            dialog.setMessage("No tienes instalado Google Maps.\n Debes instalar para continuar.");
            dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            dialog.create().show();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // retrieve result of scanning - instantiate ZXing object
        super.onActivityResult(requestCode, resultCode, intent);
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
                            Log.d("playerName: ", playerName);

                            nombreCliente = originalValues.get(i).get("name").toString();
                            Log.d("nombreCliente: ", nombreCliente);

                            if (textLength <= playerName.length()) {
                                // compare the String in EditText with Names in
                                // the ArrayList
                                Log.d("if (textLength <= playerName.length()) ", "true");
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
                            Log.d("searchResults no vacio", "");
                            View view = list.getAdapter().getView(0, null, null);

                            list.performItemClick(view, 0, 0);
                            inputSearch.setText(nombreCliente);
                            Log.d("inputSearch.setText", nombreCliente);
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

    class asyncEnviarGeolocalizacionCliente extends AsyncTask<String, String, String> {

        String user, pass;
        ProgressDialog pDialog;

        protected void onPreExecute() {
            // para el progress dialog
            pDialog = new ProgressDialog(ClientesActivity.this);
            pDialog.setMessage("Enviando pendientes...\n\n"	+ GlobalVar.urlService);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... params) {

            //verificar flagTipoEnvio S/P
            String mensaje = "";
            DBSync_soap_manager soap_manager = new DBSync_soap_manager(getApplicationContext());

            ConnectionDetector connection = new ConnectionDetector(getApplicationContext());

            if (connection.hasActiveInternetConnection(getApplicationContext())) {

                try {
                    String flag="0";
                    if(flagTipoEnvio.equals("P")){//Envio de todos las direcciones cliente
                        flag=soap_manager.actualizarDireccionCliente("ninguno","");
                    }else{
                        flag=soap_manager.actualizarDireccionCliente(codcli,codSucursal);
                    }
                    if (flag.equalsIgnoreCase("1")){
                        mensaje = "Envio completo";
                    }else{
                        mensaje = "Error, no se ha podido actualizar la localización, sin embargo se guardó como pendiente.";
                    }

                } catch (JsonParseException e) {
                    e.printStackTrace();
                    mensaje = "No se pudo verificar";
                } catch (Exception e) {
                    e.printStackTrace();
                    mensaje = "Error al enviar";
                }
            } else {
                mensaje = "Sin conexion al servidor";
            }

            return mensaje;
        }

        protected void onPostExecute(String mensaje) {
            Toast toast = Toast.makeText(getApplicationContext(), mensaje,	Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

            pDialog.dismiss();// ocultamos progess dialog.
            Log.e("onPostExecute= Enviado", "" + mensaje);
            ((Clientes_LazyAdapter) list.getAdapter()).notifyDataSetChanged();
        }

    }
}