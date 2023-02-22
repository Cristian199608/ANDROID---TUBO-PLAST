package com.example.sm_tubo_plast.genesys.fuerza_ventas;

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
import android.location.Location;
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
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.AccesosPerfil.AccesosOpciones;
import com.example.sm_tubo_plast.genesys.AccesosPerfil.Model.OptionPantallaClientes;
import com.example.sm_tubo_plast.genesys.BEAN.Cliente_estado;
import com.example.sm_tubo_plast.genesys.BEAN.San_Visitas;
import com.example.sm_tubo_plast.genesys.DAO.DAO_Cliente;
import com.example.sm_tubo_plast.genesys.DAO.DAO_ClienteEstado;
import com.example.sm_tubo_plast.genesys.DAO.DAO_San_Visitas;
import com.example.sm_tubo_plast.genesys.service.WS_Cliente_Contacto;
import com.example.sm_tubo_plast.genesys.datatypes.DBClientes;
import com.example.sm_tubo_plast.genesys.datatypes.DBMotivo_noventa;
import com.example.sm_tubo_plast.genesys.datatypes.DBPedido_Cabecera;
import com.example.sm_tubo_plast.genesys.datatypes.DBSync_soap_manager;
import com.example.sm_tubo_plast.genesys.datatypes.DB_DireccionClientes;
import com.example.sm_tubo_plast.genesys.datatypes.DBclasses;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.Google.MapsClientesActivity;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.cliente.CH_InformacionCliente;
import com.example.sm_tubo_plast.genesys.hardware.LocationApiGoogle;
import com.example.sm_tubo_plast.genesys.hardware.Permiso_Adroid;
import com.example.sm_tubo_plast.genesys.hardware.RequestPermisoUbicacion;
import com.example.sm_tubo_plast.genesys.hardware.TaskCheckUbicacion;
import com.example.sm_tubo_plast.genesys.service.ConnectionDetector;
import com.example.sm_tubo_plast.genesys.util.CustomDateTimePicker;
import com.example.sm_tubo_plast.genesys.util.GlobalFunctions;
import com.example.sm_tubo_plast.genesys.util.GlobalVar;
import com.example.sm_tubo_plast.genesys.util.UtilView;
import com.example.sm_tubo_plast.genesys.util.UtilViewMensaje;
import com.example.sm_tubo_plast.genesys.util.VARIABLES;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
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

    String numOc;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> lista_clientes = new ArrayList<HashMap<String, String>>();
    // PedidosActivity ob= new PedidosActivity();
    ListView list;

    DBclasses obj_dbclasses;
    DBClientes db_clientes;
    String nomcli, codven, ORIGEN;
    /*

     */
    LocationApiGoogle locationApiGoogle;
    TaskCheckUbicacion taskCheckUbicacion;
    Location Location_Actual;




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
    private static final int ID_PROGRAMACION_VISITA_CLIENTE = 10;
    private static final int ID_LOCALIZACION = 11;
    private static final int ID_BAJA_OR_ALTA_ClIENTE = 12;


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


    SwipeRefreshLayout swipeRefreshLayout;
    Thread asynCliente3=null;

    SearchView searchView;

    private EditText alert_edt;
    private boolean isAlert = false;

    TextView tv_cantidadGeneral, tv_cantidad_filtrado, tv_fecha_filtrado_de, tvClientesAnulados;
    CheckBox check_programada,check_visitado;
    Button txtAsignacionCliente;
    Dialog customDialog = null;

    SharedPreferences prefs;
    String _usuario;
    String _pass;
    AccesosOpciones.PantallaClientes accesosOpciones=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);


        // para que no salga el teclado al iniciar
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

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
        ORIGEN = "" + bundle.getString("ORIGEN");
        myFAB = (FloatingActionButton) findViewById(R.id.myFAB);



        dia = obj_dbclasses.getDiaConfiguracion();
        Fecha = obj_dbclasses.getFecha2();

        prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        _usuario = prefs.getString("usuario", "0");
        _pass = prefs.getString("pass", "0");


        tv_cantidadGeneral = (TextView) findViewById(R.id.tv_cantidadClientesGeneral);
        tv_cantidad_filtrado = (TextView) findViewById(R.id.tv_cantidad_filtrado);
        tv_fecha_filtrado_de = (TextView) findViewById(R.id.tv_fecha_filtrado_de);
        tvClientesAnulados = (TextView) findViewById(R.id.tvClientesAnulados);
        check_programada =  findViewById(R.id.check_programada);
        check_visitado =  findViewById(R.id.check_visitado);
        txtAsignacionCliente =  findViewById(R.id.txtAsignacionCliente);

        check_programada.setVisibility(View.GONE);
        check_visitado.setVisibility(View.GONE);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(UtilView.getColorsSwipe());
        swipeRefreshLayout.setEnabled(false);
        tv_fecha_filtrado_de.setVisibility(View.GONE);
        tvClientesAnulados.setVisibility(View.GONE);

        if (ORIGEN.equals(SeguimientoPedidoActivity.TAG)) {
            myFAB.setVisibility(View.GONE);
            txtAsignacionCliente.setVisibility(View.GONE);
        }

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

        tv_fecha_filtrado_de.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_fecha_filtrado_de.setVisibility(View.GONE);
                tv_fecha_filtrado_de.setText("");
                tv_fecha_filtrado_de.setHint("");
                GestionCargarCliente(0, "");
            }
        });
        tvClientesAnulados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvClientesAnulados.setVisibility(View.GONE);
                GestionCargarCliente(0, "");
            }
        });
        check_programada.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                GestionCargarCliente(0, tv_fecha_filtrado_de.getHint().toString());
            }
        });

        check_visitado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                GestionCargarCliente(0, tv_fecha_filtrado_de.getHint().toString());
            }
        });

        // El espinner "(xvisita,todos)" esta invisible, asi que
        // itemSelectedListener no funciona
        // cargo los clientes directamente

        GestionCargarCliente(0, "");

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
            alerta.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            alerta.show();
        }
        /****************************************************************/

        //




        ActionItem infoItem = new ActionItem(ID_INFO, "Informacion", R.drawable.icon_man_24dp);
        //ActionItem infoWebItem = new ActionItem(ID_INFOWEB,"Informacion Online", getResources().getDrawable(R.drawable.infoweb));
        ActionItem itemPedido = new ActionItem(ID_PEDIDO, "Orden Compra",R.drawable.pedidopn);
        ActionItem itemCobranza = new ActionItem(ID_COBRANZA, "Estado de Cuenta", R.drawable.icon_coins_24dp);
        ActionItem uploadItem = new ActionItem(ID_NO_VENTA, "Motivo no venta", R.drawable.icon_stop_24dp);
        ActionItem gestionVisita = new ActionItem(ID_VISITA_CLIENTE, "Gestión visita", R.drawable.icon_man_24dp);
        ActionItem programar_visita = new ActionItem(ID_PROGRAMACION_VISITA_CLIENTE, "Programar Visita", R.drawable.icon_man_24dp);
        ActionItem observacion = new ActionItem(ID_OBSERVACION, "Observacion", R.drawable.alert);
        //ActionItem devolucionItem = new ActionItem(ID_DEVOLUCION, "Devolución", R.drawable.icon_check_24dp);
        ActionItem cotizacionItem = new ActionItem(ID_COTIZACION, "Cotizacion", R.drawable.icon_survey_24dp);
        ActionItem geolocalizacion = new ActionItem(ID_LOCALIZACION, "Geolocalización", R.drawable.ic_ubicacion_grey);
        ActionItem motivoBajaOrAlta = new ActionItem(ID_BAJA_OR_ALTA_ClIENTE, "Baja/Alta de Cliente", R.drawable.icon_man_24dp);

        final QuickAction mQuickAction = new QuickAction(this);
        final QuickAction mQuickAction2 = new QuickAction(this);

        mQuickAction.addActionItem(infoItem);
        mQuickAction.addActionItem(geolocalizacion);
        mQuickAction.addActionItem(cotizacionItem);
        //mQuickAction.addActionItem(infoWebItem);
        mQuickAction.addActionItem(itemPedido);

        mQuickAction.addActionItem(itemCobranza);
        mQuickAction.addActionItem(gestionVisita);
        mQuickAction.addActionItem(programar_visita);

        mQuickAction.addActionItem(uploadItem);
        mQuickAction.addActionItem(observacion);
        //mQuickAction.addActionItem(devolucionItem);
        mQuickAction.addActionItem(motivoBajaOrAlta);

        mQuickAction2.addActionItem(infoItem);
        mQuickAction2.addActionItem(geolocalizacion);
        mQuickAction2.addActionItem(cotizacionItem);
        //mQuickAction2.addActionItem(infoWebItem);
        mQuickAction2.addActionItem(itemPedido);
        mQuickAction2.addActionItem(itemCobranza);
        mQuickAction2.addActionItem(gestionVisita);
        mQuickAction2.addActionItem(programar_visita);

        mQuickAction2.addActionItem(uploadItem);
        mQuickAction2.addActionItem(observacion);
        //mQuickAction2.addActionItem(devolucionItem);
        mQuickAction2.addActionItem(motivoBajaOrAlta);

        AdministrarAccesos(mQuickAction, mQuickAction2);


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
                codcli = ""+searchResults.get(position).get("codcli");
                codSucursal = obj_dbclasses.obtenerCodigoSucursalCliente(codcli, codven);
                estadoLocalizacion = obj_dbclasses.obtenerEstadoSucursalCliente(codcli, codSucursal);

                if (ORIGEN.equals(SeguimientoPedidoActivity.TAG)) {
                    Intent returnIntent=new Intent();
                    returnIntent.putExtra("codcli", ""+codcli);
                    returnIntent.putExtra("nomcli", ""+nomcli);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }else{
                    if (obj_dbclasses.existePedidoCabeceraXcodcli_item(codcli, item_direccion)) {
                        mQuickAction.show(view);
                    } else {
                        mQuickAction2.show(view);
                    }
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

        txtAsignacionCliente.setOnClickListener(v -> {
            startActivity(new Intent(ClientesActivity.this, AsignacionClienteXVendedorWebActivity.class));
        });


    }

    private void AdministrarAccesos(QuickAction quikAction1, QuickAction quikAction2) {
        accesosOpciones= new AccesosOpciones.PantallaClientes(this);
        OptionPantallaClientes opciones=accesosOpciones.option();
        try {

            if (!opciones.getVerInformacionCliente()){
                if (quikAction1.getActionItemById(ID_INFO)!=null)  quikAction1.remove(ID_INFO);
                if (quikAction2.getActionItemById(ID_INFO)!=null)   quikAction2.remove(ID_INFO);
            }
            if (!opciones.getOdenPedido()){
                if (quikAction1.getActionItemById(ID_PEDIDO)!=null) quikAction1.remove(ID_PEDIDO);
                if (quikAction2.getActionItemById(ID_PEDIDO)!=null) quikAction2.remove(ID_PEDIDO);
            }
            if (!opciones.getEstadoDeCuenta()){
                if (quikAction1.getActionItemById(ID_COBRANZA)!=null) quikAction1.remove(ID_COBRANZA);
                if (quikAction2.getActionItemById(ID_COBRANZA)!=null) quikAction2.remove(ID_COBRANZA);
            }
            if (!opciones.getMotivoNoVenta()){
                if (quikAction1.getActionItemById(ID_NO_VENTA)!=null) quikAction1.remove(ID_NO_VENTA);
                if (quikAction2.getActionItemById(ID_NO_VENTA)!=null) quikAction2.remove(ID_NO_VENTA);
            }
            if (!opciones.getGestionarVisita()){
                if (quikAction1.getActionItemById(ID_VISITA_CLIENTE)!=null) quikAction1.remove(ID_VISITA_CLIENTE);
                if (quikAction2.getActionItemById(ID_VISITA_CLIENTE)!=null) quikAction2.remove(ID_VISITA_CLIENTE);
            }
            if (!opciones.getProgramarVisita()){
                if (quikAction1.getActionItemById(ID_PROGRAMACION_VISITA_CLIENTE)!=null) quikAction1.remove(ID_PROGRAMACION_VISITA_CLIENTE);
                if (quikAction2.getActionItemById(ID_PROGRAMACION_VISITA_CLIENTE)!=null) quikAction2.remove(ID_PROGRAMACION_VISITA_CLIENTE);
            }
            if (!opciones.getObservacion()){
                if (quikAction1.getActionItemById(ID_OBSERVACION)!=null) quikAction1.remove(ID_OBSERVACION);
                if (quikAction2.getActionItemById(ID_OBSERVACION)!=null) quikAction2.remove(ID_OBSERVACION);
            }
            if (!opciones.getCotizacion()){
                if (quikAction1.getActionItemById(ID_COTIZACION)!=null) quikAction1.remove(ID_COTIZACION);
                if (quikAction2.getActionItemById(ID_COTIZACION)!=null) quikAction2.remove(ID_COTIZACION);
            }
            if (!opciones.getGeolocalizarCliente()){
                if (quikAction1.getActionItemById(ID_LOCALIZACION)!=null) quikAction1.remove(ID_LOCALIZACION);
                if (quikAction2.getActionItemById(ID_LOCALIZACION)!=null) quikAction2.remove(ID_LOCALIZACION);
            }
            if (!opciones.getBajaDeCliente()){
                if (quikAction1.getActionItemById(ID_BAJA_OR_ALTA_ClIENTE)!=null) quikAction1.remove(ID_BAJA_OR_ALTA_ClIENTE);
                if (quikAction2.getActionItemById(ID_BAJA_OR_ALTA_ClIENTE)!=null) quikAction2.remove(ID_BAJA_OR_ALTA_ClIENTE);
            }
            if (!opciones.getAutoAsignarClienteCartera()) {
                txtAsignacionCliente.setVisibility(View.GONE);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void starWebCreateCliente(boolean forzar) {

        double lat=0.0,lng=0.0, altitud=0.0;
        if (Location_Actual!=null){
            lat=Location_Actual.getLatitude();
            lng=Location_Actual.getLongitude();
            altitud=Location_Actual.getAltitude();
        }

        if ((lat!=0.0 && lng!=0.0) || forzar){
            Log.i(TAG, "Valor de COD_VEN ES " + codven+" coordenadas "+lat+", "+lng);
            Intent intReportes = new Intent(getApplicationContext(), CreacionNuevoCliente2Activity.class);
            intReportes.putExtra("COD_VEND", codven);
            intReportes.putExtra("LATITUD", String.valueOf(lat));
            intReportes.putExtra("LONGITUD", String.valueOf(lng));
            intReportes.putExtra("ALTITUD",altitud);
            intReportes.putExtra("CODVEN", codven);
            startActivity(intReportes);
        }else {
            UtilView.MENSAJE_simple(this, "Sin coordenadas",  "No se ha obtenido las coordenadas. \nVerifique que tengas activado el GPS, ó mantén presionado para registrar sin geolocalizar. " +
                    "Vuelva intentar.");
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        //searchView.setEnabled(false);
        inputSearch.setText(query);
        StartCargaCliente();

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

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

            TextView txt_flag_pedido,name, ruc, observacion,itemMotivoBajaCliente, fecha, monto, direccion,item_fecha_visitado, item_fecha_programada;
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
                viewHolder.observacion = (TextView) convertView .findViewById(R.id.list_item_cliente_observacion);
                viewHolder.itemMotivoBajaCliente = (TextView) convertView .findViewById(R.id.itemMotivoBajaCliente);
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


                int i=Integer.parseInt(searchResults.get(position).get("estado_pedido").toString());
                int estadoCliente=Integer.parseInt(searchResults.get(position).get("estado_cli").toString());
                String motivoBajaCliente=searchResults.get(position).get("motivoBajaCliente").toString();
                /*int i = "" obj_dbclasses.obtenerPedidosXCodcli(
                        searchResults.get(position).get("codigo").toString(),
                        searchResults.get(position).get("item_direccion").toString());
                 */




                Log.w("SITIO_ENFA", i + "obtenerPedidosXCODCLI");
                viewHolder.itemMotivoBajaCliente.setText("");
                viewHolder.itemMotivoBajaCliente.setVisibility(View.GONE);
               if (i == 2 || estadoCliente==0) {
                    // convertView.setBackgroundResource(R.drawable.list_selector);
                    viewHolder.foto.setImageDrawable(getResources().getDrawable(R.drawable.icon_cliente_rojo));
                    if (estadoCliente==0)  {
                        viewHolder.itemMotivoBajaCliente.setVisibility(View.VISIBLE);
                        viewHolder.itemMotivoBajaCliente.setText("Motivo baja: "+motivoBajaCliente);
                    }
               }
               else if (i == 1) {
                    // convertView.setBackgroundColor(getResources().getColor(R.color.color_pedidos_realizados));
                    viewHolder.foto.setImageDrawable(getResources()
                            .getDrawable(R.drawable.icon_cliente_verde));

                }
                else if (i == 3) {
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

                    String estado = searchResults.get(position).get("estado_localizacion").toString();
                    //String estado = obj_dbclasses.getEstadoDireccionCliente(searchResults.get(position).get("codigo").toString(), searchResults.get(position).get("item_direccion").toString());

                    //String estado = searchResults.get(position).get("estado").toString();
                    if (estado.equals("L")) {//Localizado
                        viewHolder.txt_flag_pedido.setBackgroundColor(getResources().getColor(R.color.yellow_600));
                    } else if (estado.equals("V")) {//Localizado y Validado
                        viewHolder.txt_flag_pedido.setBackgroundColor(getResources().getColor(R.color.green_500));
                    } else if (estado.equals("P")) {//Pendiente de localizar
                        viewHolder.txt_flag_pedido.setBackgroundColor(getResources().getColor(R.color.yellow_600));
                    } else {
                        viewHolder.txt_flag_pedido.setBackgroundColor(getResources().getColor(R.color.grey_400));
                    }

                    obs = obj_dbclasses.getObservacionCliente(searchResults .get(position).get("codigo").toString());
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
        numOc = obj_dbclasses.obtenerMaxNumOc(codven);
        dialogo.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialogo.setContentView(R.layout.dialog_motivo_noventa);
        dialogo.setCancelable(false);

        final ListView lstNoventa_motivo = (ListView) dialogo.findViewById(R.id.dialog_motivo_noventa_lstNo_venta);
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
                                // se haya generados
                                final String orden_compra = guardarCabeceraPedido(item.getCod_noventa(), "");

                                if (checkBox.isChecked()){
                                    new asyncGuardarMotivo().execute(orden_compra);

                                }else{
                                    adapter.notifyDataSetChanged();
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

        double lat=0.0,lng=0.0;
        if (Location_Actual!=null){
            lat=Location_Actual.getLatitude();
            lng=Location_Actual.getLongitude();
        }


        itemCabecera = new DBPedido_Cabecera();

        itemCabecera.setOc_numero(codven + calcularSecuencia(numOc));
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
    protected void onDestroy() {
        super.onDestroy();


        asynCliente3.interrupt();
        asynCliente3=null;

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
        public boolean onCreateOptionsMenu(Menu menu) {
        // Alternativa 1
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_clientes, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView =  (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this);

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                if (searchView.getQuery().toString().length()==0 && inputSearch.getText().toString().length()>0) {
                    inputSearch.setText("");
                    StartCargaCliente();
                }
                return false;
            }
        });


        MenuItem menu_calendario_visita=menu.findItem(R.id.menu_calendario_visita);
       /* DAO_Cliente_Contacto dao_cliente_contacto=new DAO_Cliente_Contacto();
        ArrayList<Cliente_Contacto> lista =dao_cliente_contacto.getClientesPendientesAll(obj_dbclasses);
        menuitem2.setTitle(""+menuitem2.getTitle()+"("+lista.size()+")");
*/



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clientes_menu_mapa:
                Intent intent=new Intent(this, MapsClientesActivity.class);
                startActivity(intent);
                return true;
            case R.id.clientes_enviar_localizacion_pendiente:
                flagTipoEnvio = "P";
                new asyncEnviarGeolocalizacionCliente().execute();
                return true;

            case android.R.id.home:
                finish();
            return true;
            case R.id.cliente_contacto_pendientes:
                WS_Cliente_Contacto ws_cliente_contacto=new WS_Cliente_Contacto(ClientesActivity.this, obj_dbclasses);
                ws_cliente_contacto.EnviarCliente_Contacto();

            return true;
            case R.id.menu_ver_clientes_inactivos:
                tvClientesAnulados.setVisibility(View.VISIBLE);
                tvClientesAnulados.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_top_in));
                GestionCargarCliente(0, "");

                return true;
            case R.id.menu_calendario_visita:
                final String CERO = "0";
                final Calendar c = Calendar.getInstance();
                final int hora = c.get(Calendar.HOUR_OF_DAY);
                final int minuto = c.get(Calendar.MINUTE);

                CustomDateTimePicker recogerHora=new CustomDateTimePicker(this, new CustomDateTimePicker.OnTimeSetListener() {
                    @Override
                    public String onDateTimeSet(Calendar myCalendar, String fecha_formateado) {
                        if (fecha_formateado!=null){
                            tv_fecha_filtrado_de.setText(""+fecha_formateado);
                            tv_fecha_filtrado_de.setVisibility(View.VISIBLE);
                            tv_fecha_filtrado_de.setHint(fecha_formateado);
                            tv_fecha_filtrado_de.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_top_in));
                            GestionCargarCliente(0, "");
                        }
                        return null;
                    }
                },hora, minuto, true, false, false);
                recogerHora.setFormatFecha("yyyy-MM-dd");
                //recogerHora.sethabliltar_rango_fechas(true);
                recogerHora.Show();

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
            adapter.notifyDataSetChanged();

        }

    }

    private void StartCargaCliente(){
//        if (searchView.isEnabled()) {

            GestionCargarCliente(0,  "");
//        }else{
//            Toast.makeText(this, "Espere un momento...", Toast.LENGTH_SHORT).show();
//        }
    }

    private void GestionCargarCliente(int startt,  String fecha_formateado) {


        ProgressBar pbar =new ProgressBar(ClientesActivity.this);
        pbar.setIndeterminate(true);
        pbar.setVisibility(View.VISIBLE);
        pbar.setPadding(0,0,0,50);
        final int indexStart = startt;
        int nro_paginacion=15;

        if (startt==0 ){
            if (originalValues!=null)originalValues.clear();
            if (searchResults!=null)searchResults.clear();

            swipeRefreshLayout.setRefreshing(true);
            if (searchView!=null)searchView.setEnabled(false);


            if (adapter==null){
                searchResults=new ArrayList<>();
                originalValues=new ArrayList<>();
                adapter = new Clientes_LazyAdapter(ClientesActivity.this,R.layout.main_clientes, searchResults);
                //LinearPie.setVisibility(View.VISIBLE);
                LayoutInflater inflater = LayoutInflater.from(ClientesActivity.this);
                View emptyView = inflater.inflate(R.layout.list_empty_clients, null);
                emptyView.setVisibility(View.GONE);
                ((ViewGroup) list.getParent()).addView(emptyView);
                list.setEmptyView(emptyView);
                list.setAdapter(adapter);
            }else{
                adapter.notifyDataSetChanged();
            }
            //ocultar_vista_vacio_lista(View.GONE);

        }




        long beforecall = System.currentTimeMillis();
        Log.i("CLientesAtivity",
                "1ro proceso carga en EN: "  + (System.currentTimeMillis() - beforecall) + "miliseg");

        list.setOnScrollListener(new AbsListView.OnScrollListener(){
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }
            @Override
            public void onScroll(AbsListView absListView, int i, int cant_visible, int b) {
                Log.i("CLientesAtivity",
                        "2do proceso carga en EN: "  + (System.currentTimeMillis() - beforecall) + "miliseg");
                if ((i+cant_visible==b && b>=indexStart+searchResults.size() && searchResults.size()==b) ) {
                    list.addFooterView(pbar);
                    AsynTask2(pbar, indexStart+searchResults.size(), nro_paginacion);
                }

//                if (i+cant_visible==b) {
//                    LinearPie.setVisibility(View.GONE);
//                }else LinearPie.setVisibility(View.VISIBLE);

            }
        });

        Star_Check_Permiso_Ubicacion();
    }


    private void AsynTask2(ProgressBar p, int start, int nro_paginacion) {


        if (asynCliente3!=null){
            asynCliente3.interrupt();
            asynCliente3=null;
        }
        asynCliente3 = new Thread(new Runnable() {
            @Override
            protected void finalize() throws Throwable {
                Log.i(TAG, "AsynTask2:: finalizado");
                super.finalize();
            }

            @Override
            public void run() {

                try{
                    Log.i(TAG, "AsynTask2:: corriendo HILO inicio");


                    boolean checkProgramada=check_programada.isChecked();
                    boolean checkVisitado=check_visitado.isChecked();
                    boolean verClientesAnulados = tvClientesAnulados.getVisibility()==View.VISIBLE;

                    String fecha_formateada="";
                    if (tv_fecha_filtrado_de.getHint()!=null) {
                        fecha_formateada=tv_fecha_filtrado_de.getHint().toString();

                    }
                    String newBusqueda=inputSearch.getText().toString().replace(" ", "%");
                    ArrayList<HashMap<String, Object>>  data = obj_dbclasses.getProgramacionxDia2(
                            newBusqueda, start, nro_paginacion, 50, fecha_formateada, verClientesAnulados);

                    DAO_Cliente dao_Cliente = new DAO_Cliente(getApplicationContext());
                    int cantidad_total=dao_Cliente.getClienteDirrectionAll();


                    Log.i(TAG, "AsynTask2:: corriendo HILO fin");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_cantidadGeneral.setText("Cantidad General: "+cantidad_total);
                            cargar_data_lista(p, data, nro_paginacion);
                        }
                    });


                }catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(false);
                            if (searchView!=null)searchView.setEnabled(true);
                            RemoveFoorter(p);
                            //Toast.makeText(ClientesActivity.this, "Hubo un error al buscar, Intentalo nuevamente", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        asynCliente3.start();
    }
    private void RemoveFoorter(ProgressBar progressBar){
        if (progressBar!=null){
            list.removeFooterView(progressBar);
        }
    }

    public void cargar_data_lista(ProgressBar progressBar, ArrayList<HashMap<String, Object>> result, int  paginaacion) {

        swipeRefreshLayout.setRefreshing(false);
        if (searchView!=null)searchView.setEnabled(true);

        searchResults.addAll(result);

        adapter.notifyDataSetChanged();

        if (progressBar!=null){
            RemoveFoorter(progressBar);
        }



        if (result.size()== 0 || result.size()<paginaacion ){
//            ocultar_vista_vacio_lista(View.VISIBLE);

            list.setOnScrollListener(new AbsListView.OnScrollListener(){
                @Override
                public void onScrollStateChanged(AbsListView absListView, int i) {
                }
                @Override
                public void onScroll(AbsListView absListView, int i, int cant_visible, int b) {
                    String dd;
                }
            });
        }else{
//            ocultar_vista_vacio_lista(View.GONE);
        }

    }


//    class cargarClientes extends
//            AsyncTask<String, Integer, ArrayList<HashMap<String, Object>>> {
//        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
//
//        protected void onPreExecute() {
//            // para el progress dialog
//            pDialog = new ProgressDialog(ClientesActivity.this);
//            pDialog.setMessage("Cargando Clientes...");
//            pDialog.setIndeterminate(false);
//            pDialog.show();
//        }
//
//        @Override
//        protected ArrayList<HashMap<String, Object>> doInBackground(
//                String... params) {
//            String buscar = params[0];
//
//            try {
//
//                //originalValues = obj_dbclasses.getProgramacionxDia2();
//                originalValues.addAll(obj_dbclasses.getDemasClientes2());
//
//            } catch (Exception e) {
//                Log.e("log_tag", "Error parsing data " + e.toString());
//            }
//            return originalValues;
//        }
//
//        @Override
//        protected void onPostExecute(ArrayList<HashMap<String, Object>> result) {
//            pDialog.dismiss();// ocultamos progress dialog.
//            Log.e("onPostExecute= DEPOSITOs", "" + result);
//
//            searchResults = new ArrayList<HashMap<String, Object>>(
//                    originalValues);
//
//            adapter = new Clientes_LazyAdapter(ClientesActivity.this,
//                    R.layout.main_clientes, searchResults);
//
//            LayoutInflater inflater = LayoutInflater
//                    .from(ClientesActivity.this);
//            View emptyView = inflater
//                    .inflate(R.layout.list_empty_clients, null);
//            emptyView.setVisibility(View.GONE);
//            ((ViewGroup) list.getParent()).addView(emptyView);
//            list.setEmptyView(emptyView);
//
//            // cli_adapter=new Clientes_LazyAdapter(this, searchResults);
//            list.setAdapter(adapter);
//
//
//        }
//    }

    protected void onResume() {
        super.onResume();
        if (!LocationApiGoogle.checkPlayServices(this, Permiso_Adroid.PLAY_SERVICES_RESOLUTION_REQUEST)) {
            UtilViewMensaje.MENSAJE_simple(this, "Google Play", "Necesitas instalar Google Play Services para usar las ubicaciones de los clientes ");
        }else {
            if (taskCheckUbicacion!=null){
                taskCheckUbicacion.RequestLocationUpdates();
            }
        }

    }

    /* Remove the locationlistener updates when Activity is paused */

    protected void onPause() {
        super.onPause();
        if (locationApiGoogle!=null){
            if (locationApiGoogle.fusedLocationClient!=null && locationApiGoogle.locationCallback!=null){
                locationApiGoogle.fusedLocationClient.removeLocationUpdates(locationApiGoogle.locationCallback);
            }
        }
        if (taskCheckUbicacion!=null){
            taskCheckUbicacion.RemoveLocation();
        }
    }



    public  String calcularSecuencia(String oc) {
        String cero = "0";
        String orden = "";


        String fecha_configuracionx = obj_dbclasses.getCambio("Fecha");


        String anio_actual = fecha_configuracionx.substring(8, 10);
        String mes_actual = fecha_configuracionx.substring(3, 5);
        String dia_actual = fecha_configuracionx.substring(0, 2);

        int secactual = 0;

        if (oc.length() < 6) {
            secactual = 1;
            if (mes_actual.length() < 2) {
                mes_actual = cero + mes_actual;
            }
            if (dia_actual.length() < 2) {
                dia_actual = cero + dia_actual;
            }
            //orden = mes_actual + dia_actual + cero + secactual;
            orden = anio_actual + mes_actual + dia_actual + cero + secactual;
            return orden;
        } else {
            //String cadenaFecha = oc.substring(oc.length()-6,oc.length());
            Log.i(TAG, "VER oc numero es "+oc+", tamaño "+oc.length());
            String cadenaFecha = oc.substring(oc.length() - 8, oc.length());

            int aniot = Integer.parseInt(cadenaFecha.substring(0, 2));
            int mest = Integer.parseInt(cadenaFecha.substring(2, 4));
            int diat = Integer.parseInt(cadenaFecha.substring(4, 6));
            int sectem = Integer.parseInt(cadenaFecha.substring(6, 8));

            //Verificar por año
            if (Integer.parseInt(mes_actual) <= mest) {
                if (Integer.parseInt(dia_actual) > diat) {
                    secactual = 1;
                } else
                    secactual = sectem + 1;

            } else {
                secactual = 1;
            }
        }

        if (mes_actual.length() < 2) {
            mes_actual = cero + mes_actual;
        }
        if (dia_actual.length() < 2)
            dia_actual = cero + dia_actual;
        if (secactual < 10) {
            //orden = mes_actual + dia_actual + cero + secactual;
            orden = anio_actual + mes_actual + dia_actual + cero + secactual;
        } else {
            //orden = mes_actual + dia_actual + secactual;
            orden = anio_actual + mes_actual + dia_actual + secactual;
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
                i.putExtra("codcli", codcli);
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

                            adapter.notifyDataSetChanged();

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
            intent.putExtra("TIPO_GESTION", GestionVisita3Activity.GESTIONAR_VISITA);
            intent.putExtra("ORIGEN", TAG);
            startActivity(intent);

        }
        else if (actionId == ID_PROGRAMACION_VISITA_CLIENTE) {
            Intent intent=new Intent(this, GestionVisita3Activity.class);
            intent.putExtra("ID_RRHH", codcli);
            intent.putExtra("CODIGO_CRM", "");
            intent.putExtra("NOMBRE_INSTI", "Sin nombre");
            intent.putExtra("OC_NUMERO", "");
            intent.putExtra("COD_VEND", codven);
            intent.putExtra("TIPO_GESTION", GestionVisita3Activity.PROGRAMACION_VISITA);
            intent.putExtra("ORIGEN", TAG);
            startActivity(intent);

        }
        else if (actionId == ID_INFO) {
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
        } else if (actionId == ID_BAJA_OR_ALTA_ClIENTE) {
            String mensaje="";
            String estado_cliente="0";
            if (searchResults.get(mSelectedRow).get("estado_cli").toString().equals("1")) {
                mensaje="¿Estas seguro de solicitar la baja de este cliente?";
                estado_cliente="0";
                if (obj_dbclasses.VerificarCtasXCobrar(codcli).size()>0){
                    UtilViewMensaje.MENSAJE_simple(this, "DEUDA", "No se puede dar de baja al cliente, ya que mantiene deuda");
                    return;
                }
            }
            else{
                mensaje="¿Estas seguro de solicitar la alta de este cliente?";
                estado_cliente="1";

            }
            AlertDialog.Builder dialogo = new AlertDialog.Builder(ClientesActivity.this);
            dialogo.setMessage(mensaje);
            String finalEstado_cliente = estado_cliente;
            dialogo.setPositiveButton("SI", (dialog, which) -> mostrarDialog_motivo_BAJA_ALTA(finalEstado_cliente));
            dialogo.setNegativeButton("No", (dialogInterface, i) -> {});
            dialogo.create();
            dialogo.show();

        }else if (actionId == ID_COTIZACION) {
            final Intent i = new Intent(getApplicationContext(),PedidosActivity.class);
            i.putExtra("origen", "CLIENTES");
            i.putExtra("nombreCliente", nomcli);
            i.putExtra("codcli", codcli);
            i.putExtra("codigoVendedor", codven);
            i.putExtra("tipoRegistro", PedidosActivity.TIPO_COTIZACION);
            startActivity(i);
            finish();
        }  else if (actionId == ID_LOCALIZACION) { //**Nuevo Localizacion


            double __lat=0.0,__lng=0.0, __altitud=-1;
            if (Location_Actual!=null){
                __lat=Location_Actual.getLatitude();
                __lng=Location_Actual.getLongitude();
                __altitud=Location_Actual.getAltitude();
            }
            double lat=__lat,lng=__lng, altitud=__altitud;


                flagTipoEnvio= "S";

                View alertLayout = inflater.inflate(R.layout.dialog_geolocalizar, null);
                final TextView tv1 = (TextView) alertLayout.findViewById(R.id.tv1);
                final TextView tv_localizacionActual = (TextView) alertLayout.findViewById(R.id.tv_localizacionActual);
                final TextView tv_altitud_actual = (TextView) alertLayout.findViewById(R.id.tv_altitud_actual);
                final TextView tv_localizacion = (TextView) alertLayout.findViewById(R.id.tv_localizacion);
                final TextView tv_altitud_nueva = (TextView) alertLayout.findViewById(R.id.tv_altitud_nueva);
                final TextView btnVerMapsGeo = (TextView) alertLayout.findViewById(R.id.btnVerMapsGeo);
                final TextView btnVerMapsSinGeo = (TextView) alertLayout.findViewById(R.id.btnVerMapsSinGeo);
                final TextView tv_cliente = (TextView) alertLayout.findViewById(R.id.tv_cliente);
                final Spinner spn_direccion = (Spinner) alertLayout.findViewById(R.id.spn_direccion);


                tv_localizacion.setText(lat+" , "+lng);
                tv_altitud_nueva.setText(VARIABLES.formater_thow_decimal.format(altitud)+" m.s.n.m");
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
                builder.setCancelable(true);
                builder.setPositiveButton("Enviar al servidor", null);
                builder.setNegativeButton("Local", null);
                builder.setNeutralButton("cancelar", null);

            AlertDialog dialog = builder.create();
            dialog.show();
            Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                    positiveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                                String itemDireccion="";
                                int secuenciaGiro=0;
                                if (!direcciones.isEmpty()) {
                                    itemDireccion = direcciones.get(spn_direccion.getSelectedItemPosition()).getItem();
                                }

                                obj_dbclasses.updateGeolocalizacionCliente(codcli,itemDireccion,lat,lng, altitud);

                                new asyncEnviarGeolocalizacionCliente().execute();
                                //dialog.dismiss();
                            }
                        });
            negativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                                String itemDireccion="";
                                int secuenciaGiro=0;
                                if (!direcciones.isEmpty()) {
                                    itemDireccion = direcciones.get(spn_direccion.getSelectedItemPosition()).getItem();
                                }


                                obj_dbclasses.updateGeolocalizacionCliente(codcli,itemDireccion,lat,lng, altitud);


                                Log.d(TAG, "itemDireccion"+itemDireccion+"\nsecuenciaGiro"+secuenciaGiro+"\nSucursal:"+codSucursal);
                                adapter.notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        });

                btnVerMapsSinGeo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LatLng coordenadaLast=new LatLng(lat,lng);
                        lanzarGooleMaps(coordenadaLast);
                    }
                });

                //tv_localizacionActual.setText(""+coordenadaActual.latitude+", "+coordenadaActual.longitude);

                spn_direccion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        try {

                            String estado = obj_dbclasses.getEstadoDireccionCliente(codcli, direcciones.get(i).getItem());
                            double altitud_actual = direcciones.get(i).getAltitud();

                            if(estado.equals("V")) {
                                estado= "La geolocalización de este cliente esta validada y no se podra modificar.";
                                positiveButton.setEnabled(false);
                                negativeButton.setEnabled(false);
                            }else {
                                positiveButton.setEnabled(true);
                                negativeButton.setEnabled(true);
                                if (estado.equals("P")) {//Pendiente de localizar
                                    estado="Atención esta dirreción está pendiente por localizar.\nSe guardarán los siguientes datos:";
                                }else estado="Se guardarán los siguientes datos:";
                            }

                            tv1.setText(estado);

                            tv_localizacionActual.setText(Double.parseDouble(direcciones.get(i).getLatitud())+", "+Double.parseDouble(direcciones.get(i).getLongitud()));
                            tv_altitud_actual.setText(VARIABLES.formater_thow_decimal.format(altitud_actual)+" m.s.n.m");
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
                            LatLng coordenadaLast=new LatLng(Double.parseDouble(direcciones.get(posDir).getLatitud()),Double.parseDouble(direcciones.get(posDir).getLongitud()));
                            lanzarGooleMaps(coordenadaLast);
                        }catch (Exception e){
                            UtilView.MENSAJES(ClientesActivity.this,  "Error!",
                                    "Error al intentar abrir GOOGLE MAPS\n\n Detalle del error:\n"+e.getMessage(), 0,false);
                        }
                    }
                });

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

    private void mostrarDialog_motivo_BAJA_ALTA(String estado_cliente) {


            String mensaje="";
            String titulo="";
            if (estado_cliente.equals("0")){
                mensaje="Se procederá a enviar la solicitud de baja. ¿Confirmar?";
                titulo="INDIQUE EL MOTIVO DE LA BAJA";
            }
            else if(estado_cliente.equals("1")){
                mensaje="Se procederá a enviar la solicitud de alta. ¿Confirmar?";
                titulo="INDIQUE EL MOTIVO DE LA ALTA";
            }else return;

            customDialog = new Dialog(this);
            customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            customDialog.setCancelable(true);

            customDialog.setContentView(R.layout.dialog__indique_motivo_baja_o_alta);

            final ProgressBar ProgBarAltaBaja;
            final TextInputLayout et_bodyMotivoBaja_alta=(TextInputLayout)customDialog.findViewById(R.id.et_bodyMotivoBaja_alta);
            TextView tv_motivo=(TextView)customDialog.findViewById(R.id.tv_motivo);
            TextView btn_cancelar=(TextView)customDialog.findViewById(R.id.btn_cancelar);
            final TextView btnAceptar=(TextView)customDialog.findViewById(R.id.btnAceptar);
            ProgBarAltaBaja=(ProgressBar)customDialog.findViewById(R.id.ProgBarAltaBaja);
            ProgBarAltaBaja.setVisibility(View.GONE);

            tv_motivo.setText(titulo);
            et_bodyMotivoBaja_alta.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if(editable.toString().length()>0){
                        et_bodyMotivoBaja_alta.setError(null);
                        et_bodyMotivoBaja_alta.setErrorEnabled(false);
                    }
                }
            });
            btn_cancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String contenido=et_bodyMotivoBaja_alta.getEditText().getText().toString();
                    Log.i(TAG, "cantidad de letras son: "+contenido.length());
                    if (!TextUtils.isEmpty(contenido)){
                        AlertDialog.Builder alert=new AlertDialog.Builder(ClientesActivity.this);
                        alert.setMessage("Se perderan los datos.\n¿Estas seguro que quieres salir?");
                        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //No se ciiera el dialogo
                            }
                        });
                        alert.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                et_bodyMotivoBaja_alta.setError(null);
                                et_bodyMotivoBaja_alta.setErrorEnabled(false);
                                customDialog.dismiss();
                            }
                        });
                        alert.create().show();
                    }else customDialog.dismiss();

                }
            });
            final String finalMensaje = mensaje;
            btnAceptar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    String contenido=et_bodyMotivoBaja_alta.getEditText().getText().toString();
                    if (!TextUtils.isEmpty(contenido)){
                        AlertDialog.Builder alert=new AlertDialog.Builder(ClientesActivity.this);
                        alert.setMessage(finalMensaje);
                        alert.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ProgBarAltaBaja.setVisibility(View.VISIBLE);
                                view.setVisibility(View.GONE);
                                new AsyncTask<Void, Void, Void>() {
                                    int STD_ENVIO= VARIABLES.ID_ENVIO_FALLIDA;
                                    boolean peticion=false;
                                    boolean internet=false;
                                    String errorExepcion="";

                                    Cliente_estado cliente_estado=null;

                                    @Override
                                    protected void onPreExecute() {
                                        boolean peticion=false;
                                        super.onPreExecute();
                                        cliente_estado=new Cliente_estado();
                                        cliente_estado.setCodcli(codcli);
                                        cliente_estado.setEstado(estado_cliente);
                                        cliente_estado.setCodven(codven);
                                        cliente_estado.setFec_server(obj_dbclasses.getFecha2());
                                        cliente_estado.setMotivo(et_bodyMotivoBaja_alta.getEditText().getText().toString());

                                    }

                                    @Override
                                    protected Void doInBackground(Void... voids) {
                                        try {

                                            ConnectionDetector cd=new ConnectionDetector(getApplicationContext());
                                            if (cd.isConnectingToInternet()) {
                                                internet=true;
                                                DBSync_soap_manager DBsyn=new DBSync_soap_manager(getApplicationContext());

                                                STD_ENVIO=DBsyn.realizar_altas_bajas_clientes(cliente_estado);
                                                if (STD_ENVIO==VARIABLES.ID_ENVIO_EXITOSA) {
                                                    DAO_ClienteEstado dao_clienteEstado=new DAO_ClienteEstado(getApplicationContext());
                                                    peticion= dao_clienteEstado.InsertOrReplaceItem (cliente_estado);
                                                }
                                            }
                                            Thread.sleep(500);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }catch (Exception e){
                                            errorExepcion="No se ha podido enviar su petición al sistema. Intenta nuevamente .\n" +
                                                    "\nDetalle de error:\n" +
                                                    ""+e.getMessage();
                                            e.printStackTrace();
                                        }
                                        return null;
                                    }

                                    @Override
                                    protected void onPostExecute(Void aVoid) {
                                        ProgBarAltaBaja.setVisibility(View.GONE);
                                        view.setVisibility(View.VISIBLE);
                                        if (internet) {
                                            if (errorExepcion.length()>0){
                                                MENSAJES(""+errorExepcion, 0, false, 0);
                                            }else{
                                                if (STD_ENVIO==VARIABLES.ID_ENVIO_EXITOSA) {
                                                    if (peticion){
                                                        customDialog.dismiss();
                                                        MENSAJES( "Su petición ha sido completado correctamente", 0, true, 0);
//                                                new cargarClientes().execute("");
                                                        searchResults.remove(mSelectedRow);
                                                        adapter.notifyDataSetChanged();
                                                    }else MENSAJES( "Su petición ha sido enviada sin embargo, no se ha recuperado la data.\nDebe sincronizar para recuperar.", 0, false, 0);
                                                }else  MENSAJES( "Lo sentimos, su petición no se ha completado", 0, false, 0);
                                            }
                                        }else MENSAJES("", 1, false, 0);
                                        super.onPostExecute(aVoid);
                                    }
                                }.execute();

                            }
                        });
                        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        alert.create().show();
                        et_bodyMotivoBaja_alta.setError(null);
                        et_bodyMotivoBaja_alta.setErrorEnabled(false);
                        Log.i(TAG, "Valor de motivo de la baja es "+et_bodyMotivoBaja_alta.getEditText().getText());

                    }
                    else {
                        et_bodyMotivoBaja_alta.setErrorEnabled(true);
                        et_bodyMotivoBaja_alta.setError("Es necesario que escribas el motivo");
                        et_bodyMotivoBaja_alta.getEditText().setBackgroundResource(R.drawable.edittext_rounded_corners5);
                    }

                }
            });
            customDialog.show();
    }

    public  void MENSAJES(String mensaje, int error, boolean calcelable, final int ID_ACCION){
        if (error==1) mensaje="Verifica tu conexión a internet y vuelva a intentar de nuevo.";
        else if (error==2) mensaje="No hay conexión al servidor.\nVuelva a intentar mas tarde.";
        android.app.AlertDialog.Builder dialog=new android.app.AlertDialog.Builder(ClientesActivity.this);
        dialog.setCancelable(calcelable);
        dialog.setMessage(mensaje);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.setNegativeButton("Cancelar", null);
        dialog.create();
        dialog.show();
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
            adapter.notifyDataSetChanged();
        }

    }

    private void Star_Check_Permiso_Ubicacion(){
        new RequestPermisoUbicacion(this, Permiso_Adroid.PERMISO_PARA_ACCEDER_A_LOCALIZACION, new RequestPermisoUbicacion.MyListener() {
            @Override
            public void Result(int isConcedido) {
                if (Permiso_Adroid.IS_PERMISO_DENEGADO==isConcedido){
                    UtilViewMensaje.MENSAJE_simple(ClientesActivity.this, "Permiso denegado", "No podras acceder a la ubicación");
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

                taskCheckUbicacion= new TaskCheckUbicacion(ClientesActivity.this, new TaskCheckUbicacion.MyListener() {
                    @Override
                    public void result(boolean isOk) {
                        if (isOk) {
                            locationApiGoogle.ForzarUltimaUbicacion();
                            locationApiGoogle.StartLocationCallback(LocationApiGoogle.UPDATE_INTERVAL_3_segundos, LocationApiGoogle.FASTEST_INTERVAL_3_segundos, LocationRequest.PRIORITY_HIGH_ACCURACY);

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
                Location_Actual=location;
                if (location!=null){
                    Log.i(TAG, "StartUbicacionApiGoogle:: LastLocation:: Latitude : " + location.getLatitude() + "Longitude : " + location.getLongitude());
                }else{
                    Toast.makeText(ClientesActivity.this, "Error, no se pudo obtener la ubicación", Toast.LENGTH_SHORT).show();
                }
            }
        });
        locationApiGoogle.ApiLocationGoogleConectar();
    }

    @Override
    public void onRequestPermissionsResult( int requestCode, String permissions[], int[] grantResults) {
        if (requestCode==Permiso_Adroid.PERMISO_PARA_ACCEDER_A_LOCALIZACION){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i("Permiso","Permiso concedido");
                Toast.makeText(this, "Permiso aceptado", Toast.LENGTH_SHORT).show();
                Star_Check_Permiso_Ubicacion();
            } else {

            }
        }
    }
}