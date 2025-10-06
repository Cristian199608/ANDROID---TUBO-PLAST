package com.example.sm_tubo_plast.genesys.fuerza_ventas;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Display;
import android.view.Gravity;
import android.view.InflateException;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.BEAN.ItemProducto;
import com.example.sm_tubo_plast.genesys.BEAN.LugarEntrega;
import com.example.sm_tubo_plast.genesys.BEAN.PedidoCabeceraRecalcular;
import com.example.sm_tubo_plast.genesys.BEAN.RegistroGeneralMovil;
import com.example.sm_tubo_plast.genesys.BEAN.Sucursal;
import com.example.sm_tubo_plast.genesys.BEAN.Transporte;
import com.example.sm_tubo_plast.genesys.BEAN.Turno;
import com.example.sm_tubo_plast.genesys.BEAN.Model_bonificacion;
import com.example.sm_tubo_plast.genesys.DAO.DAO_Cliente;
import com.example.sm_tubo_plast.genesys.DAO.DAO_Pedido;
import com.example.sm_tubo_plast.genesys.DAO.DAO_PromocionDetalle;
import com.example.sm_tubo_plast.genesys.DAO.DAO_RegistroBonificaciones;
import com.example.sm_tubo_plast.genesys.DAO.DAO_RegistrosGeneralesMovil;
import com.example.sm_tubo_plast.genesys.adapters.CH_Adapter_itemDevolucionProducto;
import com.example.sm_tubo_plast.genesys.adapters.ModelDevolucionProducto;
import com.example.sm_tubo_plast.genesys.datatypes.DBClientes;
import com.example.sm_tubo_plast.genesys.datatypes.DBMotivo_noventa;
import com.example.sm_tubo_plast.genesys.datatypes.DBPedido_Cabecera;
import com.example.sm_tubo_plast.genesys.datatypes.DBPedido_Detalle;
import com.example.sm_tubo_plast.genesys.datatypes.DBSync_soap_manager;
import com.example.sm_tubo_plast.genesys.datatypes.DBTb_Promocion_Detalle;
import com.example.sm_tubo_plast.genesys.datatypes.DB_PromocionDetalle;
import com.example.sm_tubo_plast.genesys.datatypes.DB_RegistroBonificaciones;
import com.example.sm_tubo_plast.genesys.datatypes.DBclasses;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.Reportes.ReportesActivity;
import com.example.sm_tubo_plast.genesys.service.ConnectionDetector;
import com.example.sm_tubo_plast.genesys.util.GlobalFunctions;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;

@SuppressLint("LongLogTag")
public class CH_DevolucionesActivity extends AppCompatActivity {

    public static final String TAG = "CH_DevolucionesActivity";

    ArrayList<DB_PromocionDetalle> al_PromocionDetalle = new ArrayList<DB_PromocionDetalle>();
    public static final String TIPO_REGISTRO = "DEVOLUCION";
    public static final String MONEDA_PEN = "PEN";
    public static final String MONEDA_USD = "USD";

    private static final int Menu_Agregar = 1;
    private static final int Menu_Guardar = 2;
    private static final int Menu_Eliminar = 3;
    private static final int Menu_Cancelar = 4;
    private static final int Menu_Modificar = 5;
    private static final int Menu_Evaluar = 6;
    private LocationListener Loclistener;

    private boolean GUARDAR_ACTIVO = false;
    private boolean PUEDE_AGREGAR = true;
    private static final int Fecha_Dialog_ID = 1;
    private AlertDialog.Builder dialogo1;
    private LocationManager locationManager;
    private String provider = null, numOc;
    double lat, lng;
    int item_direccion = 0;
    int index = 0;
    // Array para traer todas las promociones
    ArrayList<DBTb_Promocion_Detalle> promociones = new ArrayList<DBTb_Promocion_Detalle>();

    // Array para comparar los productos del pedido con el Array promociones
    ArrayList<DB_PromocionDetalle> item_promo = new ArrayList<DB_PromocionDetalle>();

    // Array para guardar los productos a los que ya se le han asignado una
    // promocion
    ArrayList<DBTb_Promocion_Detalle> producto_registrado = new ArrayList<DBTb_Promocion_Detalle>();

    // array que guarda temporalmente los productos que se agregan por promocion
    DBPedido_Detalle itemDetalleGlobal;
    ArrayList<DBPedido_Detalle> productos_promocion = new ArrayList<DBPedido_Detalle>();
    ArrayList<DB_PromocionDetalle> promocionDetalle;

    /*
     * Listas de las cantidades y montos usados de los productos para la
     * bonificación, las cuales restaran las cantidades y montos disponibles de
     * los productos
     */
    ArrayList<ArrayList<Integer>> listaCantidadesUsadas;
    ArrayList<ArrayList<Double>> listaMontosUsados;
    ArrayList<Turno> listaTurnos;
    ArrayList<Integer> cantidadesUsadas;
    ArrayList<Double> montosUsados;

    String[] entradasCompuestas;
    ArrayList<String[]> listaEntradasCompuestas;
    // entrada -> codigoProducto
    // cantidad
    // monto
    // tipoUnimed
    // unimed

    ArrayList<ArrayList<String[]>> listaPromocionCompuesta;

    String accionGlobal;

    // fecha de la tabla configuracion
    String fecha_configuracion;

    // Fecha Actual
    int año_act;
    int mes_act;
    int dia_act;

    int pos;

    String Oc_numero;
    int itemBonificacion = 0;
    double Sum_monto_total = 0.0;
    TabHost tabs;
    DBclasses dbclass;
    DAO_RegistroBonificaciones DAOBonificaciones;
    DAO_Pedido DAOPedidoDetalle;
    DAO_PromocionDetalle DAOPromocionDetalle;
    int dia, mes, año;
    String cliente;
    String secuenciacli;
    String codven;
    String codcli;
    String origen;
    String pedido = "pedido";
    int selectedPosition1 = -1;
    int selectedPosition2 = -1;
    String[] clientes;
    String[] direcciones;
    String[] nomcli_mapa;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    // Button btn_codFamilia;
    DBClientes clienteEntity;
    ProgressDialog pDialog;

    int clienteTienePercepcion;
    int clienteTienePercepcionEspecial;
    String clienteValorPercepcion;

    String nomcli;
    ArrayList<ModelDevolucionProducto> listaProductoDevolucion = new ArrayList<>();

    Calendar calendar = Calendar.getInstance();
    Button btn_agregar, btn_guardar, btn_eliminar;
    //private TextView txtTotal;
    //private TextView txtTotal_peso;
    private ListView lstProductos;

    //private TextView txtPercepcionTotal;
    //private TextView txtTotalPedido;

    // private EditText edtFechaEntrega, edtObservacion,edtLimiteCredito;
    // private Spinner spnDireccion, spnFormaPago;

    String cod_familiar = "";
    String nombre_familiar, nombre_cliente;

    /* Cabecera Pedido -------------- */
    private AutoCompleteTextView autocomplete;
    private EditText edt_nroPedido, edt_direccionFiscal, edt_fechaPedido, edt_horaPedido, edt_observacion1;
    private Spinner spn_prioridad, spn_sucursal, spn_puntoEntrega, spn_transportista, spn_turno;
    private TextView tv_peso, tv_cantidadItems;

    //campos para devoluciones
    private Spinner spn_generarCambio;
    private EditText edt_numeroGuiaCliente;
    private RadioButton rbtn_Interno;
    private RadioButton rbtn_Externo;
    private RadioButton rbtn_Vendedor;

    private Button btn_fechaPedido, btn_horaPedido;

    DAO_RegistrosGeneralesMovil DAO_registrosGeneralesMovil;
    DAO_Cliente DAO_cliente;

    private int year, month, day, hour, minute;
    static final int DATE_DIALOG_ID = 999;
    private DatePicker datePicker;
    //Devolucion
    String recojoInterno = "";
    String recojoExterno = "";
    String recojoVendedor = "";


    ArrayList<Sucursal> listaSucursales;
    ArrayList<LugarEntrega> puntoEntregas;
    ArrayList<Transporte> listaTransportes;
    //Devolucion
    ArrayList<RegistroGeneralMovil> listaGeneraCambio;
    ArrayList<RegistroGeneralMovil> listaPrioridadDSC;
    ArrayList<RegistroGeneralMovil> listaRecojo;

    ArrayList<LugarEntrega> listaLugarEntrega;

    String nroPedido, direccion;
    String codigoPrioridad, codigoSucursal, codigoLugarEntrega, codigoTurno;
    String codigoTransportista;
    String fechaEntrega, horaEntrega, fechaEntregaCompleta;
    String observacion;
    String flagMsPack = "";
    String flagMsPackAux = "";
    String codigoGeneraCambio;
    boolean cabeceraRegistrada = false;
    float valorIGV;
    String codigoUbigeo;

    String generarCambio, nroGuiaCliente, codigoRecojo;
    String CURRENT_SERIE_FAC = "";
    String CURRENT_NUMERO_FAC = "";
    String CURRENT_TIPO_FAC = "";
    String VALIDAR_MISMA_FACTURA = "0";
    //float descuentoGeneralPedido;

    DecimalFormat formaterMoneda = new DecimalFormat("#,###.00");
    SharedPreferences preferencias_configuracion;


//    public View getTabIndicador(String indicador) {
////        View r = getLayoutInflater().inflate(R.layout.tab_ped_act,
////                (TabHost) findViewById(android.R.id.tabhost), false);
//        TextView txtview = new TextView(this);
//        txtview.setText(indicador);
//        return txtview;
//    }

    public View getTabIndicador(String indicador) {
        View r = getLayoutInflater().inflate(R.layout.tab_ped_act,
                (TabHost) findViewById(android.R.id.tabhost), false);
        TextView txtview = (TextView) r
                .findViewById(R.id.tab_ped_act_txt_indicador);
        txtview.setText(indicador);
        return r;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_h__devoluciones);


        dbclass = new DBclasses(getApplicationContext());
        DAOBonificaciones = new DAO_RegistroBonificaciones(getApplicationContext());
        DAOPedidoDetalle = new DAO_Pedido(getApplicationContext());
        DAOPromocionDetalle = new DAO_PromocionDetalle(getApplicationContext());
        DAO_registrosGeneralesMovil = new DAO_RegistrosGeneralesMovil(getApplicationContext());
        DAO_cliente = new DAO_Cliente(getApplicationContext());

        Bundle bundle = getIntent().getExtras();
        origen = bundle.getString("origen");
        nomcli = bundle.getString("nombreCliente");
        codven = "" + bundle.getString("codigoVendedor");

        preferencias_configuracion = getSharedPreferences("preferencias_configuracion", Context.MODE_PRIVATE);
        valorIGV = preferencias_configuracion.getFloat("valorIGV", 0.0f);
        VALIDAR_MISMA_FACTURA = dbclass.getCambio("validarFacturaDevolucion");
        //descuentoGeneralPedido = preferencias_configuracion.getFloat("descuentoGeneralPedido", 0.0f);

        /*
         * Cabecera Pedido -----------------------------------------------------------------------
         */

        autocomplete = (AutoCompleteTextView) findViewById(R.id.autocomplete);
        edt_nroPedido = (EditText) findViewById(R.id.edt_nroPedido);
        edt_direccionFiscal = (EditText) findViewById(R.id.edt_direccionFiscal);
        spn_prioridad = (Spinner) findViewById(R.id.spn_prioridad);
        spn_sucursal = (Spinner) findViewById(R.id.spn_sucursal);
        spn_puntoEntrega = (Spinner) findViewById(R.id.spn_puntoEntrega);
        spn_turno = (Spinner) findViewById(R.id.spn_turno_entregaDev);
        rbtn_Interno = (RadioButton) findViewById(R.id.rbtn_interno);
        rbtn_Externo = (RadioButton) findViewById(R.id.rbtn_externo);
        rbtn_Vendedor = (RadioButton) findViewById(R.id.rbtn_vendedor);

        spn_transportista = (Spinner) findViewById(R.id.spn_transportista);
        spn_generarCambio = (Spinner) findViewById(R.id.spn_generarCambio);
        edt_numeroGuiaCliente = (EditText) findViewById(R.id.edt_numeroGuiaCliente);

        btn_fechaPedido = (Button) findViewById(R.id.btn_fechaPedido);
        //btn_horaPedido 		= (Button) findViewById(R.id.btn_horaPedido);
        edt_fechaPedido = (EditText) findViewById(R.id.edt_fechaPedido);
        //edt_horaPedido 		= (EditText) findViewById(R.id.edt_horaPedido);
        edt_observacion1 = (EditText) findViewById(R.id.edt_observacion1);
        /*---------------------------------------------------------------------------------------*/
        /* Detalle Pedido -----------------------------------------------------------------------*/
        tv_peso = (TextView) findViewById(R.id.tv_peso);
        tv_cantidadItems = (TextView) findViewById(R.id.tv_cantidadItems);
        /*---------------------------------------------------------------------------------------*/

        lstProductos = (ListView) findViewById(R.id.lstProductos);
        //txtTotal = (TextView) findViewById(R.id.pedidolyt_txtTotal);
        //txtTotal_peso = (TextView) findViewById(R.id.pedidolyt_txtTotal_peso);
        btn_agregar = (Button) findViewById(R.id.pedido_lyt_btnAgregar);
        //txtPercepcionTotal = (TextView) findViewById(R.id.pedidolyt_txtPercepcionTotal);
        //txtTotalPedido = (TextView) findViewById(R.id.pedidolyt_txtTotal_pedido);


        inicializarFormulario();


        btn_fechaPedido.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Process to get Current Date
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(CH_DevolucionesActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                int monthOfYear_true = monthOfYear + 1;
                                String pickerMonth = String.valueOf(monthOfYear_true), pickerDay = String.valueOf(dayOfMonth);
                                if (dayOfMonth < 10) {
                                    pickerDay = "0" + dayOfMonth;
                                }
                                if (monthOfYear_true < 10) {
                                    pickerMonth = "0" + (monthOfYear_true);
                                }
                                edt_fechaPedido.setText(pickerDay + "/" + pickerMonth + "/" + year);
                                fechaEntrega = pickerDay + "/" + pickerMonth + "/" + year;
                            }
                        }, year, month, day + 1);
                dpd.show();
            }
        });

		/*btn_horaPedido.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final Calendar c = Calendar.getInstance();
	            hour = c.get(Calendar.HOUR_OF_DAY);
	            minute = c.get(Calendar.MINUTE);

	            // Launch Time Picker Dialog
	            TimePickerDialog tpd = new TimePickerDialog(CH_DevolucionesActivity.this,
	                    new TimePickerDialog.OnTimeSetListener() {
							@Override
							public void onTimeSet(TimePicker view,int hourOfDay, int minute) {
								String pickerHour=String.valueOf(hourOfDay),pickerMinute = String.valueOf(minute);
								if (hourOfDay<10) {
									pickerHour = "0"+hourOfDay;
								}
								if (minute<10) {
									pickerMinute = "0"+minute;
								}
								edt_horaPedido.setText(pickerHour+":"+pickerMinute);
								horaEntrega = pickerHour+":"+pickerMinute;
							}
	                    }, hour, minute, false);
	            tpd.show();
			}
		});*/


        spn_sucursal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                if (!origen.equals("REPORTES-PEDIDO")) {
                    if (listaSucursales != null) {
                        if (!listaSucursales.isEmpty()) {
                            listaLugarEntrega = DAO_cliente.getPuntoEntrega(codcli, listaSucursales.get(position).getItem());
                            ArrayList<CharSequence> lugaresEntrega = new ArrayList<>();
                            for (int i = 0; i < listaLugarEntrega.size(); i++) {
                                lugaresEntrega.add(listaLugarEntrega.get(i).getDireccionEntrega());
                            }
                            ArrayAdapter<CharSequence> spinner_adapter = new ArrayAdapter<CharSequence>(getApplicationContext(), R.layout.spinner_item, lugaresEntrega);
                            spinner_adapter.setDropDownViewResource(R.layout.spinner_item);
                            spn_puntoEntrega.setAdapter(spinner_adapter);
                            Log.d("PedidosActivity", "adapter punto entrega setted");
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        cd = new ConnectionDetector(this);

        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use default
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
        autocomplete.setText(nomcli);
        fecha_configuracion = dbclass.getCambio("Fecha");

        if (origen.equals("REPORTES-PEDIDO")) {
            autocomplete.setText(nomcli);
            Oc_numero = bundle.getString("OC_NUMERO");
            edt_nroPedido.setText(Oc_numero);
            DBPedido_Cabecera ped_cab = dbclass.getRegistroPedidoCabecera(Oc_numero);
            codcli = ped_cab.getCod_cli();
            mostrarDatosCliente(codcli);
            cargarDatosCliente(ped_cab);
            DeshabilitarCampos(false);
            mostrarListaProductos("");
            edt_nroPedido.setText(Oc_numero);
        } else if (origen.equals("REPORTES-MODIFICAR")) {
            autocomplete.setText(nomcli);
            Oc_numero = bundle.getString("OC_NUMERO");
            edt_nroPedido.setText(Oc_numero);
            DBPedido_Cabecera ped_cab = dbclass.getRegistroPedidoCabecera(Oc_numero);
            codcli = ped_cab.getCod_cli();
            mostrarDatosCliente(codcli);
            cargarDatosCliente(ped_cab);
            cabeceraRegistrada = true;
            GUARDAR_ACTIVO = true;
            //DeshabilitarCampos(false);
            mostrarListaProductos("");
        } else if (origen.equals("CLIENTES_MAPA")) {
            Log.d(TAG, "cliente mapa");
            autocomplete.setText(nomcli);
            // numOc = dbclass.obtenerNumOC(codven);
            numOc = dbclass.obtenerMaxNumOc(codven);
            edt_nroPedido.setText(codven + calcularSecuencia(numOc));
            colocarFechaActual();
            mostrarDatosCliente(codcli);
            registerForContextMenu(lstProductos);
        } else if (origen.equals("CLIENTES")) {
            Log.d(TAG, "cliente");
            autocomplete.setText(nomcli);
            codcli = obtenerCodigoCliente(autocomplete.getText().toString());
            //numOc = dbclass.obtenerOcxCliente(codcli);
            //edt_nroPedido.setText(numOc);
            numOc = dbclass.obtenerMaxNumOc(codven);
            edt_nroPedido.setText(codven + calcularSecuencia(numOc));
            mostrarDatosCliente(codcli);
            autocomplete.setEnabled(false);

            if (dbclass.VerificarCtasXCobrar(codcli).size() > 0) {
                Log.w("CLIENTE CON DEUDA", codcli + "al venir de ActivityCliente");
                crearDialogo_cuentaXcobrar();
            }
            registerForContextMenu(lstProductos);
        } else {
            Log.d(TAG, "default origen");
            numOc = dbclass.obtenerMaxNumOc(codven);
            edt_nroPedido.setText(codven + calcularSecuencia(numOc));
            colocarFechaActual();
            //codcli = obtenerCodigoCliente(codcli);
            codcli = obtenerCodigoCliente(autocomplete.getText().toString());
            mostrarDatosCliente(codcli);

            if (dbclass.VerificarCtasXCobrar(codcli).size() > 0) {
                crearDialogo_cuentaXcobrar();
            }
            registerForContextMenu(lstProductos);
        }
        Oc_numero = edt_nroPedido.getText().toString();

        /* ENVIAR MENSAJE DE SINCRONIZACION */
		/*
		SharedPreferences preferencias_configuracion;
		preferencias_configuracion = getSharedPreferences(
				"preferencias_configuracion", Context.MODE_PRIVATE);
		boolean sincronizacionCorrecta = preferencias_configuracion.getBoolean(
				"preferencias_sincronizacionCorrecta", false);
		if (sincronizacionCorrecta == false) {
			AlertDialog.Builder alerta = new AlertDialog.Builder(
					PedidosActivity.this);
			alerta.setTitle("Sincronizacion incompleta");
			alerta.setMessage("Los datos estan incompletos, sincronice correctamente");
			alerta.setIcon(R.drawable.icon_warning);
			alerta.setCancelable(false);
			alerta.setPositiveButton("OK", null);
			alerta.show();
		}
		*/
        /***********************************/


        nomcli = autocomplete.getText().toString();
        codcli = dbclass.obtenerCodigoCliente(nomcli);

        // Variables necesarias para el tema de percepcion

        clienteTienePercepcion = dbclass.obtenerPercepcionCliente(codcli);
        clienteTienePercepcionEspecial = dbclass.obtenerPercepcionEspecialCliente(codcli);
        clienteValorPercepcion = dbclass.obtenerValorPercepcionCliente(codcli);

        Log.i("PedidosActivity ::::", "Variables Percepcion\n tienePercepcion= " + clienteTienePercepcion + "; tienePercepcionEsp= " + clienteTienePercepcionEspecial + "; valorPercepcion= " + clienteValorPercepcion);

        // promociones=dbclass.obtenerPromociones(codcli);
        clientes = new String[dbclass.getAllClient(codven).length];
        clientes = dbclass.getAllClient(codven);

        // ArrayAdapter<String> adaptador=new
        // ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,clientes);
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, R.layout.autocomplete_textview, clientes);
        autocomplete.setAdapter(adaptador);

        autocomplete.setThreshold(1);
        autocomplete.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.d("PedidosActivity", "onEditorAction");
                codcli = obtenerCodigoCliente(autocomplete.getText().toString());
                cliente = autocomplete.getText().toString();

                if (actionId != 5) {
                    if (dbclass.VerificarCtasXCobrar(codcli).size() > 0) {
                        Log.w("CLIENTE CON DEUDA", codcli + "autocompleteEvent");
                        crearDialogo_cuentaXcobrar();
                    }
                    mostrarDatosCliente(codcli);
                    clienteTienePercepcion = dbclass
                            .obtenerPercepcionCliente(codcli);
                    clienteTienePercepcionEspecial = dbclass
                            .obtenerPercepcionEspecialCliente(codcli);
                    clienteValorPercepcion = dbclass
                            .obtenerValorPercepcionCliente(codcli);
                    Log.d("->clienteTienePercepcion", ""
                            + clienteTienePercepcion);
                    Log.d("->clienteTienePercepcionEspecial", ""
                            + clienteTienePercepcionEspecial);
                    Log.d("->clienteValorPercepcion", ""
                            + clienteValorPercepcion);

                    return true;
                }
                return false;
            }
        });
        Log.v("PedidosActivity", "--------------------------");
        // Evento al clickear sobre el nombre del autocomplete
        autocomplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> adap, View view, int pos, long id) {
                Log.d("PedidosActivity", "autocomplete:onItemClick");
                String nombre = (String) adap.getItemAtPosition(pos);
                codcli = obtenerCodigoCliente(nombre);

                if (dbclass.VerificarCtasXCobrar(codcli).size() > 0) {
                    Log.w("CLIENTE CON DEUDA", codcli
                            + "autocomplete al hacer click");
                    crearDialogo_cuentaXcobrar();
                }

                mostrarDatosCliente(codcli);
                clienteTienePercepcion = dbclass
                        .obtenerPercepcionCliente(codcli);
                clienteTienePercepcionEspecial = dbclass
                        .obtenerPercepcionEspecialCliente(codcli);
                clienteValorPercepcion = dbclass
                        .obtenerValorPercepcionCliente(codcli);
                Log.d("->>clienteTienePercepcion", ""
                        + clienteTienePercepcion);
                Log.d("->>clienteTienePercepcionEspecial", ""
                        + clienteTienePercepcionEspecial);
                Log.d("->>clienteValorPercepcion", ""
                        + clienteValorPercepcion);

            }
        });

        // mostrar los tabs
        tabs = (TabHost) findViewById(android.R.id.tabhost);
        tabs.setup();

        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();

        TabHost.TabSpec spec = tabs.newTabSpec("mitab1");
        spec.setContent(R.id.tab1);
        // spec.setIndicator("Pedido",getResources().getDrawable(android.R.drawable.ic_btn_speak_now));
        // spec.setIndicator("Pedido");
        spec.setIndicator(getTabIndicador("PEDIDO"));
        tabs.addTab(spec);

        spec = tabs.newTabSpec("mitab2");
        spec.setContent(R.id.tab2);
        // spec.setIndicator("Productos",getResources().getDrawable(android.R.drawable.ic_dialog_map));
        // spec.setIndicator("Producto");
        spec.setIndicator(getTabIndicador("PRODUCTO"));
        tabs.addTab(spec);

        tabs.setCurrentTab(0);

        tabs.getTabWidget()
                .getChildAt(0)
                .setLayoutParams(
                        new LinearLayout.LayoutParams((width / 2),
                                LinearLayout.LayoutParams.WRAP_CONTENT));

        tabs.getTabWidget()
                .getChildAt(1)
                .setLayoutParams(
                        new LinearLayout.LayoutParams((width / 2),
                                LinearLayout.LayoutParams.WRAP_CONTENT));

        btn_agregar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                agregarProducto();
            }
        });
        Log.v("Pedidos", "-------------------------");
    }

    private void DeshabilitarCampos(boolean flag) {
        //No habilitar campos que no deben
        autocomplete.setEnabled(flag);
        edt_nroPedido.setEnabled(flag);
        edt_direccionFiscal.setEnabled(flag);
        spn_prioridad.setEnabled(flag);
        spn_sucursal.setEnabled(flag);
        spn_turno.setEnabled(flag);
        spn_puntoEntrega.setEnabled(flag);
        spn_transportista.setEnabled(flag);
        btn_fechaPedido.setEnabled(flag);
        //btn_horaPedido.setEnabled(flag);
        edt_fechaPedido.setEnabled(flag);
        //edt_horaPedido.setEnabled(flag);
        edt_observacion1.setEnabled(flag);
        spn_generarCambio.setEnabled(flag);
    }

    private void inicializarFormulario() {
		/*
		listaPrioridades = DAO_registrosGeneralesMovil.getPrioridades();
		ArrayList<CharSequence> prioridades = new ArrayList<CharSequence>();
		for (int i = 0; i < listaPrioridades.size(); i++) {
			prioridades.add(listaPrioridades.get(i).getValor());
		}
		ArrayAdapter<CharSequence> spinner_adapter = new ArrayAdapter<CharSequence>(getApplicationContext(), R.layout.spinner_item,prioridades);
		spinner_adapter.setDropDownViewResource(R.layout.spinner_item);
		spn_prioridad.setAdapter(spinner_adapter);
		*/

        listaPrioridadDSC = DAO_registrosGeneralesMovil.getPrioridadDSC();
        ArrayList<CharSequence> prioridades = new ArrayList<CharSequence>();
        for (int i = 0; i < listaPrioridadDSC.size(); i++) {
            prioridades.add(listaPrioridadDSC.get(i).getValor());
        }
        ArrayAdapter<CharSequence> spinner_adapter = new ArrayAdapter<CharSequence>(getApplicationContext(), R.layout.spinner_item, prioridades);
        spinner_adapter.setDropDownViewResource(R.layout.spinner_item);
        spn_prioridad.setAdapter(spinner_adapter);

        //LISTA TURNOO
        listaTurnos = DAO_registrosGeneralesMovil.getTurnos();
        ArrayList<CharSequence> codTurno = new ArrayList<CharSequence>();
        for (int i = 0; i < listaTurnos.size(); i++) {
            codTurno.add(listaTurnos.get(i).getTurno());
        }
        spinner_adapter = new ArrayAdapter<CharSequence>(getApplicationContext(), R.layout.spinner_item, codTurno);
        spinner_adapter.setDropDownViewResource(R.layout.spinner_item);
        spn_turno.setAdapter(spinner_adapter);
        //FIN

        //---------
        listaGeneraCambio = DAO_registrosGeneralesMovil.getGeneraCambio();
        ArrayList<CharSequence> generaCambios = new ArrayList<CharSequence>();
        for (int i = 0; i < listaGeneraCambio.size(); i++) {
            generaCambios.add(listaGeneraCambio.get(i).getValor());
        }
        spinner_adapter = new ArrayAdapter<CharSequence>(getApplicationContext(), R.layout.spinner_item, generaCambios);
        spinner_adapter.setDropDownViewResource(R.layout.spinner_item);
        spn_generarCambio.setAdapter(spinner_adapter);

        listaRecojo = DAO_registrosGeneralesMovil.getRecojo();
        for (int i = 0; i < listaRecojo.size(); i++) {
            if (listaRecojo.get(i).getValor().equals("Interno")) {
                recojoInterno = listaRecojo.get(i).getCodValor();
            } else if (listaRecojo.get(i).getValor().equals("Vendedor")) {
                recojoVendedor = listaRecojo.get(i).getCodValor();
            } else {
                recojoExterno = listaRecojo.get(i).getCodValor();
            }
        }


    }

    public void mostrarDatosCliente(String codigoCliente) {
        Log.d("PedidosActivity", "mostrarDatosCliente");
        if (!codigoCliente.equals("") || codigoCliente.length() != 0) {

            String direccionFiscal = DAO_cliente.getDireccionFiscal(codigoCliente);
            edt_direccionFiscal.setText(direccionFiscal);


            listaSucursales = DAO_cliente.getSucursales(codigoCliente);
            ArrayList<CharSequence> sucursales = new ArrayList<>();
            for (int i = 0; i < listaSucursales.size(); i++) {
                sucursales.add(listaSucursales.get(i).getItem() + " - " + listaSucursales.get(i).getDescripcionCorta());
            }
            ArrayAdapter<CharSequence> spinner_adapter = new ArrayAdapter<CharSequence>(getApplicationContext(), R.layout.spinner_item, sucursales);
            spinner_adapter.setDropDownViewResource(R.layout.spinner_item);
            spn_sucursal.setAdapter(spinner_adapter);

            listaLugarEntrega = DAO_cliente.getPuntoEntrega(codigoCliente, listaSucursales.get(spn_sucursal.getSelectedItemPosition()).getItem());
            ArrayList<CharSequence> lugaresEntrega = new ArrayList<>();
            for (int i = 0; i < listaLugarEntrega.size(); i++) {
                lugaresEntrega.add(listaLugarEntrega.get(i).getDireccionEntrega());
            }
            spinner_adapter = new ArrayAdapter<CharSequence>(getApplicationContext(), R.layout.spinner_item, lugaresEntrega);
            spinner_adapter.setDropDownViewResource(R.layout.spinner_item);
            spn_puntoEntrega.setAdapter(spinner_adapter);

            listaTransportes = DAO_cliente.getTransportes(codigoCliente);
            ArrayList<CharSequence> transportes = new ArrayList<>();
            for (int i = 0; i < listaTransportes.size(); i++) {
                transportes.add(listaTransportes.get(i).getDescripcion());
            }
            spinner_adapter = new ArrayAdapter<CharSequence>(getApplicationContext(), R.layout.spinner_item, transportes);
            spinner_adapter.setDropDownViewResource(R.layout.spinner_item);
            spn_transportista.setAdapter(spinner_adapter);

            flagMsPack = DAO_cliente.getFlagMsPack(codigoCliente);
        }

    }

    public void cargarDatosCliente(DBPedido_Cabecera item) {
        Log.d("PedidosActivity", "cargarDatosCliente");

        String codigoPrio = item.getCodigoPrioridad();
		/*
		for (int i = 0; i < listaPrioridades.size(); i++) {
			if (listaPrioridades.get(i).getCodValor().equals(codigoPrio)) {
				spn_prioridad.setSelection(i);
			}
		}
		*/

        String codTurno = item.getCodTurno();
        for (int i = 0; i < listaTurnos.size(); i++) {
            if (listaTurnos.get(i).getCodTurno().equals(codTurno)) {
                spn_turno.setSelection(i);
            }
        }

        for (int i = 0; i < listaPrioridadDSC.size(); i++) {
            if (listaPrioridadDSC.get(i).getCodValor().equals(codigoPrio)) {
                spn_prioridad.setSelection(i);
            }
        }


        String codigoSucur = item.getCodigoSucursal();
        for (int i = 0; i < listaSucursales.size(); i++) {
            if (listaSucursales.get(i).getItem().equals(codigoSucur)) {
                spn_sucursal.setSelection(i);
            }
        }

        String codigoPuntoEn = item.getCodigoPuntoEntrega();
        Log.d("PedidoAcivity", "codigoPuntoEn:" + codigoPuntoEn);
        for (int p = 0; p < listaLugarEntrega.size(); p++) {
            if (listaLugarEntrega.get(p).getCodigoLugar().equals(codigoPuntoEn)) {
                spn_puntoEntrega.setSelection(p);
                Log.d("PedidoAcivity", "listaLugarEntrega:" + listaLugarEntrega.get(p).getCodigoLugar() + "  " + listaLugarEntrega.get(p).getDireccionEntrega() + "  " + spn_puntoEntrega.getItemAtPosition(p).toString());
            }
        }

        String codigoRecojoX = item.getObservacion2();//recojo se guarda en observacion2
        //Los codigos de recojo ya se han cargado en InicializarFormulario()
        if (codigoRecojoX.equals(recojoExterno)) {
            //this.codigoRecojo = codigoRecojoX;
            rbtn_Externo.setChecked(true);
        } else if (codigoRecojoX.equals(recojoInterno)) {
            rbtn_Interno.setChecked(true);
        } else {
            rbtn_Vendedor.setChecked(true);
        }

        String codigoTranspor = item.getCodigoTransportista();
        for (int i = 0; i < listaTransportes.size(); i++) {
            if (listaTransportes.get(i).getCodigoTransporte().equals(codigoTranspor)) {
                spn_transportista.setSelection(i);
            }
        }

        String codigoGeneraCambio = item.getObservacion3();//genera cambio se guarda en observacion3
        for (int i = 0; i < listaGeneraCambio.size(); i++) {
            if (listaGeneraCambio.get(i).getCodValor().equals(codigoGeneraCambio)) {
                spn_generarCambio.setSelection(i);
            }
        }

        edt_numeroGuiaCliente.setText(item.getNumeroOrdenCompra());

        String fechaEnterg = item.getFecha_mxe();
        edt_fechaPedido.setText("" + fechaEnterg.substring(0, 10));
        //edt_horaPedido.setText(""+fechaEnterg.substring(11, 16));

        Log.d(TAG, "Indicaciones: " + item.getObservacion());
        edt_observacion1.setText(item.getObservacion());

    }

    // Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if (origen.equals("REPORTES")) {
            menu.add(Menu.NONE, Menu_Agregar, Menu.NONE, "MenuAgregar").setIcon(R.drawable.icon_ch_add2_32).setTitle("Agregar");
            menu.add(Menu.NONE, Menu_Modificar, Menu.NONE, "Modificar").setIcon(R.drawable.icon_ch_edit2_32).setTitle("Modificar");
        } else if (origen.equals("REPORTES-MODIFICAR")) {
            menu.add(Menu.NONE, Menu_Agregar, Menu.NONE, "MenuAgregar").setIcon(R.drawable.icon_ch_add2_32).setTitle("Agregar");
            menu.add(Menu.NONE, Menu_Modificar, Menu.NONE, "Modificar").setIcon(R.drawable.icon_ch_edit2_32).setTitle("Guardar Cambios");
            menu.add(Menu.NONE, Menu_Evaluar, Menu.NONE, "MenuEvaluar").setIcon(R.drawable.icon_ch_check2_32).setTitle("Evaluar");
        } else {
            menu.add(Menu.NONE, Menu_Agregar, Menu.NONE, "MenuAgregar").setIcon(R.drawable.icon_ch_add2_32).setTitle("Agregar");
            menu.add(Menu.NONE, Menu_Guardar, Menu.NONE, "MenuGuardar").setIcon(R.drawable.icon_ch_save2_32).setTitle("Guardar");
            menu.add(Menu.NONE, Menu_Eliminar, Menu.NONE, "MenuEliminar").setIcon(R.drawable.icon_ch_delete2_32).setTitle("Anular");
            menu.add(Menu.NONE, Menu_Evaluar, Menu.NONE, "MenuEvaluar").setIcon(R.drawable.icon_ch_check2_32).setTitle("Evaluar");
        }

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (origen.equals("REPORTES-PEDIDO")) {
            menu.getItem(0).setEnabled(false);
            menu.getItem(1).setEnabled(false);
            menu.getItem(2).setEnabled(false);
            menu.getItem(3).setEnabled(false);
        } else {
            if (GUARDAR_ACTIVO) {
                menu.getItem(1).setEnabled(true);
            } else {
                menu.getItem(1).setEnabled(false);
                //menu.getItem(3).setEnabled(false);
            }
            menu.getItem(3).setEnabled(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case Menu_Cancelar:

                if (origen.equals("REPORTES")) {
                    dbclass.actualizarEstadoCabeceraPedido(Oc_numero, "G");
                    finish();
                } else {
                    // dbclass.cambiarEstadoEliminados(Oc_numero);
                    CH_DevolucionesActivity.this.finish();
                    Intent intent2 = new Intent(CH_DevolucionesActivity.this,
                            ReportesActivity.class);
                    intent2.putExtra("ORIGEN", "PEDIDOS");
                    startActivity(intent2);
                    Log.w("Menu Cancelar", "se cancelo");
                }

                return true;

            case Menu_Modificar:
                GuardarFormularioCabecera();
                guardarCabeceraPedido();
                crear_dialogo_guardar_modificar("Se modificara el pedido");

                Log.w("Menu Modificar", "se modificó");

                return true;

            case Menu_Agregar:
                agregarProducto();
                return true;

            case Menu_Guardar:
                dbclass.eliminar_item_promo_marcados(Oc_numero);
                crear_dialogo_guardar_modificar("Se guardaran todos los datos");

                return true;
            case Menu_Evaluar:
                return true;
            case Menu_Eliminar:

                if (dbclass.getPedidosCabeceraxCliente(codcli).size() > 0) {
                    // Toast.makeText(getApplicationContext(),
                    // "El cliente seleccionado ya tiene un pedido o motivo de no venta",
                    // Toast.LENGTH_SHORT).show();
                    // return true;
                }

                AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
                builder3.setTitle("Importante");
                builder3.setMessage("Esta seguro que desea Anular este pedido?");
                builder3.setCancelable(false);

                builder3.setPositiveButton("Confirmar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {

                                crearDialogo_noventa();

                            }
                        });
                builder3.setNegativeButton("Cancelar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {

                            }
                        });

                builder3.create().show();

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public boolean camposValidos() {

        nroPedido = edt_nroPedido.getText().toString().trim();
        direccion = edt_direccionFiscal.getText().toString().trim();
        fechaEntrega = edt_fechaPedido.getText().toString().trim();
        //horaEntrega		= edt_horaPedido.getText().toString().trim();

        if (nroPedido.equals("") || nroPedido.length() == 0) {
            tabs.setCurrentTab(0);
            edt_nroPedido.setError(Html.fromHtml("<font color='#424242'>Campo necesario</font>"));
            edt_nroPedido.requestFocus();
            return false;
        }
		/*
		if (nroOrdenCompra.equals("") || nroOrdenCompra.length()==0) {
			edt_nroOrdenCompra.setError("Campo necesario");
			edt_nroOrdenCompra.requestFocus();
			validado = false;
		}
		*/

        if (direccion.equals("") || direccion.length() == 0) {
            tabs.setCurrentTab(0);
            edt_direccionFiscal.setError(Html.fromHtml("<font color='#424242'>Campo necesario</font>"));
            edt_direccionFiscal.requestFocus();
            return false;
        }
        if (fechaEntrega.equals("") || fechaEntrega.length() == 0) {
            tabs.setCurrentTab(0);
            edt_fechaPedido.setError(Html.fromHtml("<font color='#424242'>Campo necesario</font>"));
            edt_fechaPedido.requestFocus();
            return false;
        }
		/*if (horaEntrega.equals("") || horaEntrega.length()==0){
			tabs.setCurrentTab(0);
			edt_horaPedido.setError(Html.fromHtml("<font color='#424242'>Campo necesario</font>"));
			edt_horaPedido.requestFocus();
			return false;
		}*/
        return true;

    }

    public void agregarProducto() {
        if (autocomplete.getText().equals("") || codcli.equals("")) {
            autocomplete.setError(Html.fromHtml("<font color='#424242'>Cliente no Valido</font>"));
        } else {
            autocomplete.setEnabled(false);
            autocomplete.setFocusable(false);

            if (camposValidos()) {
                try {
                    if (cabeceraRegistrada == false) {
                        GuardarFormularioCabecera();
                        guardarCabeceraPedido();
                    }

                    //fechaEntregaCompleta = edt_fechaPedido.getText().toString() + " " + edt_horaPedido.getText().toString();
                    fechaEntregaCompleta = edt_fechaPedido.getText().toString();

                    Intent intent = new Intent(CH_DevolucionesActivity.this, CH_BuscarProductoDevolucion.class);
                    intent.putExtra("codigoCliente", codcli);
                    intent.putExtra("codigoVendedor", codven);
                    intent.putExtra("origen", pedido);
                    startActivityForResult(intent, 1);

                } catch (Exception e) {
                    e.printStackTrace();
                    GlobalFunctions.showCustomToast(this, "Verificar que esten cargados los datos y no haya campos vacios", GlobalFunctions.TOAST_WARNING, GlobalFunctions.POSICION_BOTTOM);
                }
            }
        }
    }

    private void GuardarFormularioCabecera() {
        //codigoPrioridad 		= listaPrioridades.get(spn_prioridad.getSelectedItemPosition()).getCodValor();
        codigoTurno = listaTurnos.get(spn_turno.getSelectedItemPosition()).getCodTurno();
        codigoPrioridad = listaPrioridadDSC.get(spn_prioridad.getSelectedItemPosition()).getCodValor();
        codigoSucursal = listaSucursales.get(spn_sucursal.getSelectedItemPosition()).getItem();
        codigoLugarEntrega = listaLugarEntrega.get(spn_puntoEntrega.getSelectedItemPosition()).getCodigoLugar();

        codigoTransportista = listaTransportes.get(spn_transportista.getSelectedItemPosition()).getCodigoTransporte();
        observacion = edt_observacion1.getText().toString();
        codigoGeneraCambio = listaGeneraCambio.get(spn_generarCambio.getSelectedItemPosition()).getCodValor();

        //fechaEntrega + " " + horaEntrega;
        //fechaEntregaCompleta	= edt_fechaPedido.getText().toString() + " " + edt_horaPedido.getText().toString();
        fechaEntregaCompleta = edt_fechaPedido.getText().toString();

        nroGuiaCliente = edt_numeroGuiaCliente.getText().toString();
        Log.d("PedidosActivity", "GuardarFormularioCabecera:fechaEntregaCompleta -> " + fechaEntregaCompleta);
        Log.d(TAG, "DevolucionesActivity --> GuardarFormularioCabecera:CodigoTurno  -> " + codigoTurno);
        if (rbtn_Interno.isChecked()) {
            codigoRecojo = recojoInterno;
        } else if (rbtn_Vendedor.isChecked()) {
            codigoRecojo = recojoVendedor;
        } else {
            codigoRecojo = recojoExterno;
        }
    }

    // Menu contextual listview
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

        ModelDevolucionProducto prod = (ModelDevolucionProducto) lstProductos.getAdapter().getItem(info.position);

        if (prod.getTipo().equals("V")) {
            menu.setHeaderTitle(prod.getDescripcionProducto());
            inflater.inflate(R.menu.context_menu_pedido, menu);

        } else {
            menu.setHeaderTitle(prod.getDescripcionProducto());
            inflater.inflate(R.menu.context_menu_pedido2, menu);
        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.CntxMenu_Eliminar:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                ModelDevolucionProducto prod = (ModelDevolucionProducto) lstProductos.getAdapter().getItem(info.position);

                dbclass.EliminarItemPedido(prod.getCodigoProducto(), -1, Oc_numero);
                mostrarListaProductos("");
                return true;

            case R.id.CntxMenu_Eliminar_item_promo:
                Log.d("PedidosActivity :onContextItemSelected:", "CntxMenu_Eliminar_item_promo");
                AdapterView.AdapterContextMenuInfo info3 = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

                ItemProducto prod3 = (ItemProducto) lstProductos.getAdapter().getItem(info3.position);
                int saldoAnterior = DAOBonificaciones.getSaldoAnterior(Oc_numero, prod3.getCodprod());
                String codigoRegistroAnterior = DAOBonificaciones.getCodigoRegistroAnterior(Oc_numero, prod3.getCodprod());
                DAOBonificaciones.Actualizar_saldoBonificacion(codigoRegistroAnterior, saldoAnterior);

                //Si hay un pendiente nulo agregado este debe devolver el saldo usado antes de ser eliminado
                DB_RegistroBonificaciones nuloAgregado = DAOBonificaciones.getPendienteAgregado(Oc_numero, prod3.getCodprod());
                if (nuloAgregado.getOc_numero() != null) {//Si existe un nuloAgregado
                    DB_RegistroBonificaciones registroUsado = DAOBonificaciones.getRegistroBonificacion(nuloAgregado.getCodigoAnterior());
                    DAOBonificaciones.Actualizar_saldoBonificacion(registroUsado.getCodigoRegistro(), (registroUsado.getSaldo() + nuloAgregado.getCantidadEntregada()));
                    Log.d("PedidosActivity", "Saldo devuelto por " + nuloAgregado.getSalida() + " al codigoRegistro:" + registroUsado.getCodigoRegistro() + " saldo total:" + (registroUsado.getSaldo() + nuloAgregado.getCantidadEntregada()));
                }

                DAOBonificaciones.Eliminar_RegistroBonificacion_xSalida(Oc_numero, prod3.getCodprod(), 0);
                DAOPedidoDetalle.Eliminar_itemPedidoBonificacion(prod3.getCodprod(), Oc_numero, null, null, 0, 0);

                Log.e("", "---------------------------------");
                Gson gson2 = new Gson();
                Log.d("listaRegistroBonificacaciones", gson2.toJson(DAOBonificaciones.ObtenerRegistroBonificaciones()));
                Log.d("listaPedidoDetalle", gson2.toJson(dbclass.getPedidosDetallexOc_numero(Oc_numero)));

                mostrarListaProductos("");//--------------------------------------------------------------------------------------- ANALIZAR
                return true;

            case R.id.CntxMenu_Editar:
                /*
                 * COMENTADO TEMPORALMANTE HASTA REALIZAR LA FUNCIONALIDAD MODIFICAR
                 * EN REGISTRO BONIFICACIONES
                 */
                /*
                 * AdapterView.AdapterContextMenuInfo info2 =
                 * (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                 *
                 * final ItemProducto prod2 = (ItemProducto)
                 * lstProductos.getAdapter().getItem(info2.position); Intent intent
                 * = new Intent(PedidosActivity.this, ProductoActivity.class);
                 * intent.putExtra("codcli", codcli); intent.putExtra("codven",
                 * codven); intent.putExtra("origen", pedido);
                 * intent.putExtra("origen", "PEDIDO_MODIFICAR");
                 * intent.putExtra("OCNUMERO", Oc_numero); intent.putExtra("CODPRO",
                 * prod2.getCodprod());
                 *
                 * // Datos necesarios para el tema de percepcion
                 * intent.putExtra(ProductoActivity.CLIENTE_TIENE_PERCEPCION,
                 * clienteTienePercepcion);
                 * intent.putExtra(ProductoActivity.CLIENTE_TIENE_PERCEPCION_ESPECIAL
                 * , clienteTienePercepcionEspecial);
                 * intent.putExtra(ProductoActivity.CLIENTE_VALOR_PERCEPCION,
                 * clienteValorPercepcion);
                 *
                 * Log.d("PedidosActivity ::item Editar::",
                 * "Envio a ProductoActivity :PEDIDO_MODIFICAR:");
                 *
                 * startActivityForResult(intent, 1);
                 */
                GlobalFunctions.showCustomToast(CH_DevolucionesActivity.this, "Elimine y vuelva a ingresar el producto", GlobalFunctions.TOAST_WARNING, GlobalFunctions.POSICION_BOTTOM);
                return true;
            case R.id.CntxMenu_Editar_item_promo:

                AdapterView.AdapterContextMenuInfo info4 = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

                final ItemProducto bonificacion = (ItemProducto) lstProductos.getAdapter().getItem(info4.position);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                final int cantidadesPendiente = DAOBonificaciones.getSaldoPendiente(codcli, bonificacion.getCodprod());
                final int cantidadMaximaB = bonificacion.getCantidad() + cantidadesPendiente;

                final EditText input = new EditText(getApplicationContext());
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, .5f);
                params.setMargins(10, 2, 2, 10);
                input.setLayoutParams(params);

                builder.setTitle("Ingrese cantidad");
                builder.setMessage("Cantidad maxima " + cantidadMaximaB);
                builder.setView(input);

                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (input.length() > 0) {
                            int cantidad = Integer.parseInt(input.getText().toString());
                            if (cantidad > 0 && cantidad <= cantidadMaximaB) {
                                actualizarBonificacion(bonificacion, cantidad, cantidadesPendiente);

                            } else {
                                input.setError("Cantidad no valida");
                            }
                        }
                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }

                });
                builder.create().show();

                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    public void actualizarBonificacion(ItemProducto bonificacion, int cantidadEntregada, int saldoAnterior) {
        String codigoAnteriorPendiente = DAOBonificaciones.getRegistroAnteriorPendiente(codcli, bonificacion.getCodprod());
        DB_RegistroBonificaciones registroAnteriorPendiente = DAOBonificaciones.getRegistroBonificacion(codigoAnteriorPendiente);//Ultimo registro con todos los datos (con saldo pendiente)

        String codigoUltimoRegistro = DAOBonificaciones.getUltimoRegistroBonificacion(Oc_numero, bonificacion.getCodprod());
        DB_RegistroBonificaciones registroUltimoPedido = DAOBonificaciones.getRegistroBonificacion(codigoUltimoRegistro);//Ultimo registro del pedido actual tenga o no saldo pendiente

        int cantidadTotal = DAOBonificaciones.obtenerCantidadBonificacion(Oc_numero, bonificacion.getCodprod(), Integer.MIN_VALUE);
        if (saldoAnterior > 0) {
            //Si el saldo es mayor a 0 el registro anterior pendiente existe
            if (registroAnteriorPendiente.getOc_numero().equals(registroUltimoPedido.getOc_numero())) {
                //Si ambos son del mismo pedido los datos se mantien y no se aumenta el saldo anterior, eso solo sucede cuando se jala un saldo pendiente de un pedido anterior
                DAOBonificaciones.Actualizar_RegistroBonificacion(codigoUltimoRegistro, cantidadTotal, registroUltimoPedido.getSaldoAnterior(), cantidadEntregada, (cantidadTotal + registroUltimoPedido.getSaldoAnterior() - cantidadEntregada), registroUltimoPedido.getCodigoAnterior(), codven, codcli);
            } else {
                DAOBonificaciones.Actualizar_saldoBonificacion(registroAnteriorPendiente.getCodigoRegistro(), 0);
                DAOBonificaciones.Actualizar_RegistroBonificacion(codigoUltimoRegistro, cantidadTotal, saldoAnterior, cantidadEntregada, (cantidadTotal + saldoAnterior - cantidadEntregada), codigoAnteriorPendiente, codven, codcli);
            }
        } else {
            //Si el saldo es 0 no existe un registro anterior pendiente y se debe mantener los datos del ultimo registro pedido
            DAOBonificaciones.Actualizar_RegistroBonificacion(codigoUltimoRegistro, cantidadTotal, registroUltimoPedido.getSaldoAnterior(), cantidadEntregada, (cantidadTotal + registroUltimoPedido.getSaldoAnterior() - cantidadEntregada), registroUltimoPedido.getCodigoAnterior(), codven, codcli);
        }

        dbclass.actualizarItem_promo(
                Oc_numero,
                registroUltimoPedido.getSalida(),
                dbclass.obtener_codunimedXtipo_unimed_salida(registroUltimoPedido.getTipo_unimedSalida(), registroUltimoPedido.getSalida()),
                cantidadEntregada);


        //Verificar que el registroAnterior Pendiente sea el que se ha actualizado y no el pasado de quien se obtuvo el saldo pendiente inicialmente

        codigoAnteriorPendiente = DAOBonificaciones.getRegistroAnteriorPendiente(codcli, bonificacion.getCodprod());
        registroAnteriorPendiente = DAOBonificaciones.getRegistroBonificacion(codigoAnteriorPendiente);//Ultimo registro con todos los datos (con saldo pendiente)

        DB_RegistroBonificaciones agregadoNulo = DAOBonificaciones.getPendienteAgregado(Oc_numero, bonificacion.getCodprod());
        if (agregadoNulo.getOc_numero() != null) {
            int saldoAnteriorCompleto = saldoAnterior + agregadoNulo.getCantidadEntregada();
            int saldoCompleto = (saldoAnteriorCompleto + registroAnteriorPendiente.getCantidadTotal()) - cantidadEntregada;
            if (registroAnteriorPendiente.getCodigoAnterior().equals("")) {
                //Se debe actualizar el codigoAnterior con el del agregadoNulo ya que este tiene el codigoAnterior
                DAOBonificaciones.Actualizar_RegistroBonificacion(codigoAnteriorPendiente, (saldoAnterior + agregadoNulo.getCantidadEntregada()), saldoCompleto, agregadoNulo.getCodigoAnterior());
            } else {
                //Se mantiene el codigoAnterior
                DAOBonificaciones.Actualizar_RegistroBonificacion(codigoAnteriorPendiente, (saldoAnterior + agregadoNulo.getCantidadEntregada()), saldoCompleto);
            }

            DAOBonificaciones.Eliminar_PendienteAgregado(Oc_numero, bonificacion.getCodprod());
        }

        mostrarListaProductos(registroUltimoPedido.getSalida());
    }

    public void aceptar() {
        cliente = dbclass.obtenerNomcliXCodigo(codcli);
        Intent i = new Intent(getApplicationContext(),
                CuentasXCobrarActivity2.class);
        i.putExtra("CODCLI", codcli);
        i.putExtra("NOMCLI", cliente);
        i.putExtra("ORIGEN", "clienteac");
        startActivity(i);
    }

    public void cancelar() {
        Toast t = Toast.makeText(this, "Realizar Pedido.", Toast.LENGTH_SHORT);
        t.show();
    }

    // Picker Dialog funcion del boton en layout
    public void Mostrar_date(View view) {

        dia = calendar.get(Calendar.DAY_OF_MONTH);
        mes = calendar.get(Calendar.MONTH);
        año = calendar.get(Calendar.YEAR);

        showDialog(Fecha_Dialog_ID);
    }

    public void ColocarFecha(int day, int month, int year) {

        String _dia, _mes;

        // toma la fecha de la tabla configuracion
        // String fecha_configuracion = dbclass.getCambio("Fecha");

        // se cambia las variables globales(dia,mes) que traen la fecha actual
        // del dispositivo
        // por unas que contengan la fecha de la tabla configuracion (dia1,mes1)

        // int mes1 = Integer.parseInt(fecha_configuracion.substring(3, 5));
        // int dia1 = Integer.parseInt(fecha_configuracion.substring(0, 2));

        if (day > 0 && day < 10) {
            _dia = "0" + day;
        } else {
            _dia = "" + day;
        }

        if ((month + 1) < 10) {
            _mes = "0" + (month + 1);
        } else {
            _mes = "" + (month + 1);
        }

        // <<<<<<<<<<<<<<<<<<<<<<<edtFechaEntrega.setText(new
        // StringBuilder().append(_dia).append("/")
        // .append(_mes).append("/").append(año));

    }

    public String calcularSecuencia(String oc) {
        String cero = "0";
        String orden = "";

        // obtengo la fecha de la tabla configuracion
        // String fecha_configuracion = dbclass.getCambio("Fecha");

        // String mes_actual=(calendar.get(Calendar.MONTH)+1)+"";
        // String dia_actual=calendar.get(Calendar.DAY_OF_MONTH)+"";

        String mes_actual = fecha_configuracion.substring(3, 5);
        String dia_actual = fecha_configuracion.substring(0, 2);

        int secactual = 0;

        Log.d("calculando secuencia...", oc + "");

        if (oc.length() < 6) {
            Log.d("calculando secuencia...", oc + " < 6");
            secactual = 1;
            if (mes_actual.length() < 2) {
                mes_actual = cero + mes_actual;
            }
            if (dia_actual.length() < 2) {
                dia_actual = cero + dia_actual;
            }
            Log.d("calculando secuencia...", "orden = " + mes_actual + "+" + dia_actual + "+" + cero + "+" + secactual);

            orden = mes_actual + dia_actual + cero + secactual;
            return orden;
        } else {
            Log.d("calculando secuencia...", oc + " > 6");
            String cadenaFecha = oc.substring(oc.length() - 6, oc.length());

            int mest = Integer.parseInt(cadenaFecha.substring(0, 2));
            int diat = Integer.parseInt(cadenaFecha.substring(2, 4));
            int sectem = Integer.parseInt(cadenaFecha.substring(4, 6));

            Log.d("calculando secuencia...", "diat:" + diat);
            Log.d("calculando secuencia...", "mest:" + mest);
            Log.d("calculando secuencia...", "sectem:" + sectem);

            if (Integer.parseInt(mes_actual) <= mest) {
                Log.d("calculando secuencia...", "mes_actual < mest");
                if (Integer.parseInt(dia_actual) > diat) {
                    secactual = 1;
                    Log.d("calculando secuencia...", "secactual = 1;  ->" + secactual);
                } else
                    secactual = sectem + 1;
                Log.d("calculando secuencia...", "secactual = sectem + 1; ->" + secactual);

            } else {
                secactual = 1;
                Log.d("calculando secuencia...", "secactual = 1;");
            }
        }

        if (mes_actual.length() < 2) {
            mes_actual = cero + mes_actual;
        }
        if (dia_actual.length() < 2) {
            dia_actual = cero + dia_actual;
        }
        if (secactual < 10) {
            Log.d("calculando secuencia...", "secactual < 10");
            orden = mes_actual + dia_actual + cero + secactual;
            Log.d("calculando secuencia...", "orden = mes_actual + dia_actual + cero + secactual;");
            Log.d("calculando secuencia...", "orden = " + mes_actual + "+" + dia_actual + "+" + cero + "+" + secactual);
        } else {
            Log.d("calculando secuencia...", "secactual > 10");
            Log.d("calculando secuencia...", "orden = mes_actual + dia_actual + secactual");
            orden = mes_actual + dia_actual + secactual;
            Log.d("calculando secuencia...", "orden = " + mes_actual + "+" + dia_actual + "+" + secactual);
        }

        return orden;
    }

    public String obtenerCodigoCliente(String nomcli) {

        String codcli = dbclass.obtenerCodigoCliente(nomcli);
        return codcli;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                tabs.setCurrentTab(1);
                String tipo = data.getStringExtra("tipo");
                String codigoProducto = data.getStringExtra("codigoProducto");
                String descripcion = data.getStringExtra("descripcion");
                String desunimed = data.getStringExtra("desunimed");
                String unidad_medida = dbclass.obtener_codXdesunimed(codigoProducto, desunimed);
                int cantidad = data.getIntExtra("cantidad", 0);
                double peso = data.getDoubleExtra("peso", 0.0);
                String lote = data.getStringExtra("lote");
                String tipoDocumento = data.getStringExtra("tipoDocumento");
                String serie = data.getStringExtra("serie");
                String numero = data.getStringExtra("numero");
                String fechaEmision = data.getStringExtra("fechaEmision");
                String codigoMotivo = data.getStringExtra("codigoMotivo");
                String codigoExpectativa = data.getStringExtra("codigoExpectativa");
                String envase = data.getStringExtra("envase");
                String contenido = data.getStringExtra("contenido");
                String observaciones = data.getStringExtra("observaciones");

                String subtotal_peso = GlobalFunctions.redondear_toString(peso * cantidad);

                Log.d("onActivityResult", "tipo: " + tipo);
                Log.d("onActivityResult", "codigoProducto: " + codigoProducto);
                Log.d("onActivityResult", "descripcion: " + descripcion);
                Log.d("onActivityResult", "cantidad" + desunimed);
                Log.d("onActivityResult", "unidad_medida: " + unidad_medida);
                Log.d("onActivityResult", "cantidad: " + cantidad);
                Log.d("onActivityResult", "peso: " + peso);
                Log.d("onActivityResult", "lote: " + lote);
                Log.d("onActivityResult", "tipoDocumento: " + tipoDocumento);
                Log.d("onActivityResult", "serie: " + serie);
                Log.d("onActivityResult", "numero: " + numero);
                Log.d("onActivityResult", "fechaEmision: " + fechaEmision);
                Log.d("onActivityResult", "codigoMotivo: " + codigoMotivo);
                Log.d("onActivityResult", "codigoExpectativa: " + codigoExpectativa);
                Log.d("onActivityResult", "envase: " + envase);
                Log.d("onActivityResult", "contenido: " + contenido);
                Log.d("onActivityResult", "observaciones: " + observaciones);
                Log.d("onActivityResult", "subtotal_peso: " + subtotal_peso);

                boolean facturaValidada = false;

                if (VALIDAR_MISMA_FACTURA.equals("1")) {// PARAMETRO QUE VIENE LA LA TABLA configuracion (SAEMOVILES)
                    if ((CURRENT_TIPO_FAC.equals("") && CURRENT_SERIE_FAC.equals("") && CURRENT_NUMERO_FAC.equals("")) || (CURRENT_TIPO_FAC.equals(tipoDocumento) && CURRENT_SERIE_FAC.equals(serie) && CURRENT_NUMERO_FAC.equals(numero))) {
                        facturaValidada = true;
                    } else {
                        facturaValidada = false;
                    }
                } else {//SI EL PARAMETRO ES "0" NO SE REQUIERE VALIDACION
                    facturaValidada = true;
                }


                /* Validacion para solo agregar productos de la misma factura */
                if (facturaValidada == true) {

                    if (tipo.equals("AGREGAR-DEVOLUCION")) {
                        if (lstProductos.getAdapter() != null) {
                            PUEDE_AGREGAR = true;

                            for (int i = 0; i < lstProductos.getAdapter().getCount(); i++) {
                                ModelDevolucionProducto item = (ModelDevolucionProducto) lstProductos.getAdapter().getItem(i);
                                if (codigoProducto.equals(item.getCodigoProducto())) {
                                    PUEDE_AGREGAR = false;
                                    Toast.makeText(getApplicationContext(), "producto ya registrado", Toast.LENGTH_LONG).show();
                                }
                            }
                        }


                        if (PUEDE_AGREGAR) {

                            DBPedido_Detalle itemDetalle = new DBPedido_Detalle();
                            itemDetalle.setOc_numero(edt_nroPedido.getText().toString());
                            itemDetalle.setEan_item("");
                            itemDetalle.setCip(codigoProducto);
                            itemDetalle.setPrecio_bruto("0.0");
                            itemDetalle.setPrecio_neto("0.0");
                            itemDetalle.setPercepcion("0.0");
                            itemDetalle.setCantidad(cantidad);
                            itemDetalle.setTipo_producto("V");
                            itemDetalle.setUnidad_medida(unidad_medida);
                            itemDetalle.setPeso_bruto(subtotal_peso);
                            itemDetalle.setFlag("N");
                            itemDetalle.setCod_politica("");
                            itemDetalle.setSec_promo("");
                            itemDetalle.setItem_promo(0);
                            itemDetalle.setAgrup_promo(0);

                            itemDetalle.setPrecioLista("0.0");
                            itemDetalle.setDescuento("0");
                            if (lote == null) {
                                itemDetalle.setLote("");
                            } else {
                                itemDetalle.setLote("" + lote);
                            }
                            itemDetalle.setMotivoDevolucion(codigoMotivo);
                            itemDetalle.setExpectativa(codigoExpectativa);
                            itemDetalle.setEnvase(envase);
                            itemDetalle.setContenido(contenido);
                            itemDetalle.setProceso("");
                            itemDetalle.setObservacionDevolucion(observaciones);
                            itemDetalle.setTipoDocumento(tipoDocumento);
                            itemDetalle.setSerieDevolucion(serie);
                            itemDetalle.setNumeroDevolucion(numero);

                            dbclass.AgregarPedidoDetalle(itemDetalle);
                        }
                        mostrarListaProductos("");

                    } else {
                        mostrarListaProductos("");
                    }
                } else {
                    GlobalFunctions.showCustomToast(CH_DevolucionesActivity.this, "EL producto no pertenece a la misma factura", GlobalFunctions.TOAST_WARNING, GlobalFunctions.POSICION_BOTTOM);
                }

            }
        }
    }

    private void verificarPromocionXMonto() {
        // TODO Auto-generated methosd stub

    }

    public void guardarCabeceraPedido() {
        DBPedido_Cabecera itemCabecera = new DBPedido_Cabecera();
        //new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date(System.currentTimeMillis()));
        String fechaActual = dbclass.getFecha2();
        Log.d("fechaActual", fechaActual);
        Log.d("fechaEntregaCompleta", fechaEntregaCompleta);
        itemCabecera.setOc_numero(edt_nroPedido.getText().toString());
        itemCabecera.setSitio_enfa("");
        itemCabecera.setMonto_total("");
        itemCabecera.setPercepcion_total("");
        itemCabecera.setValor_igv("0.0");
        itemCabecera.setMoneda("");
        itemCabecera.setFecha_oc(fechaActual);
        itemCabecera.setFecha_mxe(fechaEntregaCompleta);//----Para devoluciones es la fecha de recojo
        itemCabecera.setCond_pago("");
        itemCabecera.setCod_cli(codcli);
        itemCabecera.setCod_emp(codven);
        itemCabecera.setEstado("I");
        itemCabecera.setUsername(dbclass.getNombreUsuarioXcodvend(codven));
        itemCabecera.setRuta("");
        itemCabecera.setObservacion(observacion);
        itemCabecera.setCod_noventa(0);
        itemCabecera.setPeso_total("0.0");
        itemCabecera.setFlag("P");
        itemCabecera.setLatitud(lat + "");
        itemCabecera.setLongitud(lng + "");
        itemCabecera.setCodigo_familiar(cod_familiar);
        //itemCabecera.setDT_PEDI_FECHASERVIDOR("");
        //itemCabecera.setTotalSujetopercepcion("");
        //itemCabecera.setTipoVista("");
        itemCabecera.setNumeroOrdenCompra(nroGuiaCliente);//----Para devoluciones es el nroGuia del Cliente
        itemCabecera.setCodigoPrioridad(codigoPrioridad);//----Para devoluciones es la prioridad
        itemCabecera.setCodigoSucursal(codigoSucursal);
        itemCabecera.setCodigoPuntoEntrega(codigoLugarEntrega);
        itemCabecera.setCodigoTipoDespacho("");
        itemCabecera.setFlagEmbalaje("");
        itemCabecera.setFlagPedido_Anticipo("");
        itemCabecera.setCodigoTransportista(codigoTransportista);
        itemCabecera.setCodigoAlmacen("");
        itemCabecera.setObservacion2("");
        itemCabecera.setObservacion3("");
        itemCabecera.setObservacionDescuento("");
        itemCabecera.setObservacionTipoProducto("");
        itemCabecera.setFlagDescuento("");
        itemCabecera.setCodigoObra("");
        itemCabecera.setFlagDespacho("");
        itemCabecera.setDocAdicional("");
        itemCabecera.setTipoDocumento("");
        itemCabecera.setTipoRegistro(TIPO_REGISTRO);
        itemCabecera.setDiasVigencia("");//Solo para el caso Cotizacion

        //Devolucion
        itemCabecera.setCodigoGeneraCambio(codigoGeneraCambio);
        itemCabecera.setCodigoRecojo(codigoRecojo);
        Log.d(TAG, "itemCabecera.setCodigoRecojo" + itemCabecera.getCodigoRecojo());

        if (origen.equals("CLIENTESAC")) {
            dbclass.Actualizar_pedido_cabecera(itemCabecera);
        } else if (origen.equals("REPORTES-MODIFICAR")) {
            DAOPedidoDetalle.ActualizarPedidoCabecera(itemCabecera);
        } else
            dbclass.AgregarPedidoCabecera(itemCabecera);
        // dbclass.actualizarEstadoCliente(codcli, "P");

        cabeceraRegistrada = true;
    }

    public void cargarClientesXRuta() {
        ArrayList<DBClientes> lista_clientes_ruta = new ArrayList<DBClientes>();
        int i = 0;
        lista_clientes_ruta = dbclass.getClientesXRuta();
        String[] lclientes = new String[lista_clientes_ruta.size()];
        Iterator<DBClientes> it = lista_clientes_ruta.iterator();

        while (it.hasNext()) {
            Object objeto = it.next();
            DBClientes cta = (DBClientes) objeto;

            lclientes[i] = cta.getNomcli();

            i++;

        }

        if (lclientes.length > 0) {

            autocomplete.setText(lclientes[0]);
            codcli = obtenerCodigoCliente(lclientes[0]);

            mostrarDatosCliente(lclientes[0]);
        } else {
            autocomplete.setHint("Ingrese cliente");
        }

    }

    public void colocarFechaActual() {

        // int mes_actual=(calendar.get(Calendar.MONTH)+1);

        // int dia_propuesto=(calendar.get(Calendar.DAY_OF_MONTH)+1);

        // int año_actual=calendar.get(Calendar.YEAR);

        int mes_actual = Integer.parseInt(fecha_configuracion.substring(3, 5));
        int dia_propuesto = Integer.parseInt(fecha_configuracion
                .substring(0, 2)) + 1;
        int año_actual = Integer.parseInt(fecha_configuracion.substring(6, 10));

        Calendar mycal = new GregorianCalendar(año_actual, mes_actual - 1, 1);
        int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);

        if (dia_propuesto > daysInMonth) {
            dia_propuesto = 1;

            if ((mes_actual - 1) == Calendar.DECEMBER) {
                mes_actual = Calendar.JANUARY + 1;
                año_actual = año_actual + 1;
            } else {
                mes_actual = mes_actual + 1;
            }

        }

        String _dia_propuesto, _mes_actual;

        if (dia_propuesto > 0 && dia_propuesto < 10) {
            _dia_propuesto = "0" + dia_propuesto;
        } else {
            _dia_propuesto = "" + dia_propuesto;
        }

        if (mes_actual < 10) {
            _mes_actual = "0" + (mes_actual);
        } else {
            _mes_actual = "" + (mes_actual);
        }

        // edtFechaEntrega.setText(new
        // StringBuilder().append(_dia_propuesto).append("/").append(_mes_actual).append("/").append(año_actual));
    }

    public void mostrarCondPago() {
        codcli = obtenerCodigoCliente(autocomplete.getText().toString());
        String condPago = dbclass.obtener_condPagoXcliente(codcli);

        Log.i("CONDPAGO", "" + condPago);
        Log.i("CODCLI", "" + codcli);

        int position = 0;
        if (condPago.equals("01")) {
            position = 0;
        } else if (condPago.equals("2")) {
            position = 1;
        } else if (condPago.equals("07")) {
            position = 2;
        }
        // <<<<<<<<spnFormaPago.setSelection(position);
    }

    public String getHoraActual() {
        Calendar calendar1 = Calendar.getInstance();

        String hora = "" + calendar1.get(Calendar.HOUR_OF_DAY);
        String min = "" + calendar1.get(Calendar.MINUTE);
        String seg = "" + calendar1.get(Calendar.SECOND);

        if (hora.length() < 2) {
            hora = "0" + hora;
        }

        if (min.length() < 2) {
            min = "0" + min;
        }

        if (seg.length() < 2) {
            seg = "0" + seg;
        }

        return hora + ":" + min + ":" + seg;
    }

    public void crearDialogo_cuentaXcobrar() {

        // Dialogo Deudas
        dialogo1 = new AlertDialog.Builder(this);
        dialogo1.setTitle("Importante");
        dialogo1.setMessage("El cliente seleccionado mantiene una deuda ¿Desea realizar el cobro?");
        dialogo1.setCancelable(false);

        dialogo1.setPositiveButton("Confirmar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        aceptar();
                    }
                });
        dialogo1.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        cancelar();
                    }
                });

        dialogo1.show();
    }

    public void crearDialogo_noventa() {

        final Dialog dialogo = new Dialog(this, android.R.style.Theme_Light);

//		dialogo.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        //dialogo.setTitle("Motivo de no Venta");
        dialogo.setContentView(R.layout.dialog_motivo_noventa);
        dialogo.setCancelable(false);

        final ListView lstNoventa_motivo = (ListView) dialogo.findViewById(R.id.dialog_motivo_noventa_lstNo_venta);
        Button btnAceptar = (Button) dialogo.findViewById(R.id.dialog_motivo_noventa_btnAceptar);
        Button btnCancelar = (Button) dialogo.findViewById(R.id.dialog_motivo_noventa_btnCancelar);
        lstNoventa_motivo.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        ArrayList<DBMotivo_noventa> motivos = new ArrayList<DBMotivo_noventa>();
        motivos = dbclass.obtenerMotivo_noventa();
        dbclass.close();
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

                if (selectedPosition1 != -1) {

                    DBMotivo_noventa item = (DBMotivo_noventa) lstNoventa_motivo
                            .getAdapter().getItem(selectedPosition1);

                    if (GUARDAR_ACTIVO == true) {

                        guardarCabeceraPedido();
                        // dbclass.actualizarEstadoCliente(cliente, "M");
                    }

                    dbclass.Anular_pedido(edt_nroPedido.getText().toString(), item.getCod_noventa());

                    dialogo.dismiss();

                    new async_envio_pedido().execute();

                } else {
                    Toast.makeText(getApplicationContext(), "Seleccione uno",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialogo.cancel();
            }
        });

        dialogo.show();

    }

    @Override
    public void onBackPressed() {
        if (origen.equals("REPORTES-PEDIDO")) {
            finish();
        } else if (origen.equals("REPORTES-MODIFICAR")) {
            finish();
        } else {

            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Pedido")
                    .setMessage("Se perderan todos los datos")
                    .setPositiveButton("Confirmar",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //Se restaura los saldos pendientes de las bonificaciones pasadas usadas
                                    RestaurarSaldosPendientes();

                                    dbclass.eliminar_pedido(Oc_numero);
                                    DAOBonificaciones.Eliminar_RegistrosBonificacion(Oc_numero);
                                    //dbclass.actualizarEstadoCliente(codcli, "S");
                                    finish();

                                }

                            }).setNegativeButton("Cancelar", null).show();

        }
    }

    void RestaurarSaldosPendientes() {
        ArrayList<DB_RegistroBonificaciones> lista = DAOBonificaciones.getRegistrosUsanPendientes(Oc_numero);
        for (int i = 0; i < lista.size(); i++) {
            DAOBonificaciones.Actualizar_saldoBonificacion(lista.get(i).getCodigoAnterior(), lista.get(i).getSaldoAnterior());
        }

        //Devolver las cantidadesEntregadas por los nulosAgregados
        ArrayList<DB_RegistroBonificaciones> listaNulos = DAOBonificaciones.getRegistrosUsanPendientesNulos(Oc_numero);
        for (int i = 0; i < listaNulos.size(); i++) {
            DB_RegistroBonificaciones registroUsado = DAOBonificaciones.getRegistroBonificacion(listaNulos.get(i).getCodigoAnterior());
            DAOBonificaciones.Actualizar_saldoBonificacion(registroUsado.getCodigoRegistro(), (registroUsado.getSaldo() + listaNulos.get(i).getCantidadEntregada()));
            Log.d("PedidosActivity", "Saldo devuelto por " + listaNulos.get(i).getSalida() + " al codigoRegistro:" + registroUsado.getCodigoRegistro() + " saldo total:" + (registroUsado.getSaldo() + listaNulos.get(i).getCantidadEntregada()));
        }
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

    class async_envio_pedido extends AsyncTask<Void, Integer, String> {
        protected void onPreExecute() {
            // para el progress dialog

            // int datos1 = dbclass.getCantidadDatos_pedido_cabecera(Oc_numero);
            // int datos2 = dbclass.getCantidadDatos_pedido_detalle(Oc_numero);

            pDialog = new ProgressDialog(CH_DevolucionesActivity.this);
            pDialog.setIcon(R.drawable.ic_send);
            pDialog.setMessage("Enviando....");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected String doInBackground(Void... params) {

            DBSync_soap_manager soap_manager = new DBSync_soap_manager(getApplicationContext());

            String valor = "";

            if (cd.hasActiveInternetConnection(getApplicationContext())) {

                try {
                    valor = soap_manager.actualizarObjPedido_directo(Oc_numero);
                } catch (JsonParseException ex) {
                    // exception al parsear json
                    valor = "error_2";
                } catch (Exception ex) {
                    // cualquier otra exception al enviar los datos

                    valor = "error_3";
                    ex.printStackTrace();
                }

            } else {
                // Sin conexion al servidor
                valor = "error_1";
            }

            return valor;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int progreso = values[0].intValue();

            pDialog.setProgress(progreso);
        }

        /*
         * Una vez terminado doInBackground segun lo que halla ocurrido pasamos
         * a la sig. activity o mostramos error
         */
        @Override
        protected void onPostExecute(String result) {

            pDialog.dismiss();// ocultamos progess dialog.
            // AlertDialog alertDialog = new
            // AlertDialog.Builder(PedidosActivity.this).create();
            // alertDialog.setCancelable(false);

            if (result.equals("E")) {

                crear_dialogo_post_envio("ENVIO CORRECTO",
                        "El pedido fue ingresado al Servidor", R.drawable.check);

            } else if (result.equals("I")) {

                crear_dialogo_post_envio("ATENCION",
                        "No se pudieron guardar todos los datos",
                        R.drawable.alert);

            } else if (result.equals("P")) {

                crear_dialogo_post_envio("ATENCION",
                        "El servidor no pudo ingresar este pedido",
                        R.drawable.ic_alert);

            } else if (result.equals("T")) {

                crear_dialogo_post_envio(
                        "ATENCION",
                        "Este pedido ya se encuentra en proceso de facturacion \nComuniquese con el administrador \nLos cambios se guardaran localmente",
                        R.drawable.ic_alert);

            } else if (result.equals("error_1")) {

                crear_dialogo_post_envio(
                        "SIN CONEXION",
                        "Es probable que no tenga acceso a la red \nEl pedido se guardo localmente",
                        R.drawable.ic_alert);

            } else if (result.equals("error_2")) {

                crear_dialogo_post_envio(
                        "ATENCION",
                        "El pedido fue enviado pero no se pudo verificar \nVerifique manualmente",
                        R.drawable.alert);

            } else if (result.equals("error_3")) {

                crear_dialogo_post_envio("ERROR AL ENVIAR",
                        "No se pudo Enviar este pedido \nSe guardo localmente",
                        R.drawable.ic_alert);

            }

            Log.e("onPostExecute=", "" + result);

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
            locationManager.requestLocationUpdates(provider, 1000, 0,
                    Loclistener);
            Log.w("Location",
                    "Pedido de actualizacion de ubicacion a provider: "
                            + provider);
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
        Log.w("Localizacion", "Lat:" + lat + ", Lng:" + lng + "");
    }

    protected void setMenuBackground() {
        // Log.d(TAG, "Enterting setMenuBackGround");
        getLayoutInflater().setFactory(new LayoutInflater.Factory() {
            public View onCreateView(String name, Context context,
                                     AttributeSet attrs) {
                if (name.equalsIgnoreCase("com.android.internal.view.menu.IconMenuItemView")) {
                    try { // Ask our inflater to create the view
                        LayoutInflater f = getLayoutInflater();
                        final View view = f.createView(name, null, attrs);
                        /*
                         * The background gets refreshed each time a new item is
                         * added the options menu. So each time Android applies
                         * the default background we need to set our own
                         * background. This is done using a thread giving the
                         * background change as runnable object
                         */
                        new Handler().post(new Runnable() {
                            public void run() {
                                // sets the background color
                                view.setBackgroundResource(R.color.color_white);
                                // sets the text color
                                ((TextView) view).setTextColor(Color.BLACK);
                                // sets the text size
                                ((TextView) view).setTextSize(18);
                            }
                        });
                        return view;
                    } catch (InflateException e) {
                    } catch (ClassNotFoundException e) {
                    }
                }
                return null;
            }
        });
    }

    public int verificarTipoCondicion(DB_PromocionDetalle itemPromocion,double precioNeto, int cantidadX) {
        int cantidad = 0;
        Log.w("",
                "---------------------- verificarTipoCondicion ----------------------");
        if (itemPromocion.getTipo().equals("C")) {
            Log.d("PedidosActivity ::verificarTipoCondicion::","itemPromocion.getTipo() -> C");
            Log.d("PedidosActivity ::verificarTipoCondicion::","itemDetalle.getCantidad()-> " + cantidadX);
            Log.d("PedidosActivity ::verificarTipoCondicion::","itemPromocion.getCant_condicion()-> "+ itemPromocion.getCant_condicion());

            if (cantidadX >= itemPromocion.getCant_condicion()) {
                if (itemPromocion.getCondicion().equals("1")) {
                    cantidad = itemPromocion.getCant_promocion();
                } else if (itemPromocion.getCondicion().equals("3")) {
                    cantidad = (cantidadX / itemPromocion.getCant_condicion())
                            * itemPromocion.getCant_promocion();
                } else {
                    Log.d("PedidosActivity ::obtenerCantidadBonificacion::","la condicion de promocion no esta establecido"									+ itemPromocion.getCondicion());
                }

            } else {
                // Cuando no se supera la condicion, es menor que..
                if (itemPromocion.getCondicion().equals("2")) {
                    Log.d("PedidosActivity ::obtenerCantidadBonificacion::",
                            "getCondicion 2");
                    cantidad = itemPromocion.getCant_promocion();
                }
            }
        } else if (itemPromocion.getTipo().equals("M")) {

            Log.d("PedidosActivity ::obtenerCantidadBonificacion::",
                    "promoEntity.getTipo() -> M");
            Log.d("PedidosActivity ::verificarTipoCondicion::",
                    "itemDetalle.getPrecioNeto-> " + precioNeto);
            Log.d("PedidosActivity ::verificarTipoCondicion::",
                    "montoCondicion-> " + itemPromocion.getMonto());
            if (precioNeto >= Double.parseDouble(itemPromocion.getMonto())) {

                if (itemPromocion.getCondicion().equals("1")) {
                    cantidad = itemPromocion.getCant_promocion();
                } else if (itemPromocion.getCondicion().equals("3")) {
                    int aux = (int) (precioNeto / Double
                            .parseDouble(itemPromocion.getMonto()));
                    cantidad = aux * itemPromocion.getCant_promocion();
                } else {
                    Log.d("PedidosActivity ::obtenerCantidadBonificacion::",
                            "la condicion de promocion no esta establecido"
                                    + itemPromocion.getCondicion());
                }

            } else {
                if (itemPromocion.getCondicion().equals("2")) {
                    cantidad = itemPromocion.getCant_promocion();
                }
            }
        }
        Log.w("", "--------------------------  -------------------------------");
        return cantidad;
    }

    public int verificarTipoCondicionAcumulados_xCantidad(
            DB_PromocionDetalle itemPromocion, String precioNeto, int cantidadX) {
        int cantidad = 0;
        int cantidadTotalUsada = 0;

        int cantidadAux, cantidadUsadaAux = 0;
        double montoAux = 0.0;

        Log.w("",
                "---------------------- verificarTipoCondicion Acumulado----------------------");
        if (itemPromocion.getTipo().equals("C")) {

            if (cantidadX >= itemPromocion.getCant_condicion()) {
                // Cuando se supera la condicion, es mayor que..
                if (itemPromocion.getCondicion().equals("1")) {
                    cantidad = itemPromocion.getCant_promocion();
                    cantidadTotalUsada = itemPromocion.getCant_condicion();
                    // montoTotalUsado = No se puede determinar, se calcula al
                    // final por cada entrada;

                } else if (itemPromocion.getCondicion().equals("3")) {
                    cantidad = (cantidadX / itemPromocion.getCant_condicion())
                            * itemPromocion.getCant_promocion();
                    Log.d("", "Cantidad =  ( " + cantidadX + " / "
                            + itemPromocion.getCant_condicion() + " )  * "
                            + itemPromocion.getCant_promocion() + " ==== "
                            + cantidad);
                    cantidadTotalUsada = (cantidadX / itemPromocion
                            .getCant_condicion())
                            * itemPromocion.getCant_condicion();
                    Log.d("", "Cantidad total Usada =  ( " + cantidadX + " / "
                            + itemPromocion.getCant_condicion() + " )  * "
                            + itemPromocion.getCant_promocion() + " ==== "
                            + cantidadTotalUsada);
                    // montoTotalUsado = No se puede determinar, se calcula al
                    // final por cada entrada;
                } else {
                    Log.d("PedidosActivity ::obtenerCantidadBonificacion::",
                            "getCondicion NO ESPECIFICADA"
                                    + itemPromocion.getCondicion());
                }

            } else {
                // Cuando no se supera la condicion, es menor que..
                if (itemPromocion.getCondicion().equals("2")) {
                    Log.d("PedidosActivity ::obtenerCantidadBonificacion::",
                            "getCondicion 2");
                    cantidad = itemPromocion.getCant_promocion();
                    cantidadTotalUsada = cantidadX;
                    // montoTotalUsado = No se puede determinar, se calcula al
                    // final por cada entrada;
                }
            }

            /*
             * ------------------------------------------------------------------
             * --
             * ----------------------------------------------------------------
             * ----------------------------------- CÁLCULO DE LOS MONTOS Y
             * CANTIDADES USADOS POR CADA ENTRADA POR CANTIDADES
             */

            for (int it = 0; it < listaEntradasCompuestas.size(); it++) {
                // Se calcula la cantidad y monto usado por cada entrada hasta
                // que cumplan la cantidad condicion de la promocion
                if (cantidadUsadaAux < cantidadTotalUsada) {

                    String[] entradaCompuesta = listaEntradasCompuestas.get(it);
                    cantidadAux = Integer.parseInt(entradaCompuesta[1]); // Cantidad
                    // de
                    // la
                    // entrada
                    montoAux = Double.parseDouble(entradaCompuesta[2]); // Monto
                    // de la
                    // entrada
                    cantidadUsadaAux = cantidadUsadaAux + cantidadAux;

                    // Si la cantidad sumada es mayor a la cantidad de la
                    // condicion, se debe calcular la cantidad real y el monto
                    // real usado por esa entrada
                    if (cantidadUsadaAux > cantidadTotalUsada) {
                        int cantidadAux_real = cantidadAux
                                - (cantidadUsadaAux - cantidadTotalUsada);
                        double montoAux_real = (montoAux * cantidadAux_real)
                                / cantidadAux;

                        cantidadesUsadas.add(cantidadAux_real);
                        montosUsados.add(montoAux_real);
                        Log.d("", "agregando cantidad usada: "
                                + cantidadAux_real);
                    } else {
                        // Si la cantidad sumada no es mayor se guarda la
                        // cantidad y monto completa de esa entrada
                        cantidadesUsadas.add(cantidadAux);
                        montosUsados.add(montoAux);
                        Log.d("", "agregando cantidad usada: " + cantidadAux);
                    }

                } else {
                    // Si la cantidad se supera, las demas cantidades del
                    // acumulado no se toman en cuenta
                    cantidadesUsadas.add(0);
                    montosUsados.add(0.0);
                    Log.d("", "agregando cantidad usada: 0");
                }
            }

            /*
             * ------------------------------------------------------------------
             * --
             * ----------------------------------------------------------------
             * -----------------------------------
             */

        }
        Log.w("", "--------------------------  -------------------------------");
        return cantidad;
    }

    public int verificarTipoCondicionAcumulados_xMonto(
            DB_PromocionDetalle itemPromocion, String precioNeto, int cantidadX) {
        int cantidad = 0;
        double montoTotalUsado = 0.0;

        int cantidadAux = 0;
        double montoAux, montoUsadoAux = 0.0;

        Log.w("",
                "-------------------- verificarTipoCondicion --------------------");
        if (itemPromocion.getTipo().equals("M")) {
            Log.d("PedidosActivity ::obtenerCantidadBonificacion::",
                    "promoEntity.getTipo() -> M");
            Log.d("PedidosActivity ::verificarTipoCondicion::",
                    "itemDetalle.getPrecioNeto-> " + precioNeto);
            Log.d("PedidosActivity ::verificarTipoCondicion::",
                    "montoCondicion-> " + itemPromocion.getMonto());

            if (Double.parseDouble(precioNeto) >= Double
                    .parseDouble(itemPromocion.getMonto())) {

                if (itemPromocion.getCondicion().equals("1")) {
                    cantidad = itemPromocion.getCant_promocion();
                    montoTotalUsado = Double.parseDouble(itemPromocion
                            .getMonto());
                    // cantidadUsada = No se puede determinar, se calcula al
                    // final por cada entrada;

                } else if (itemPromocion.getCondicion().equals("3")) {
                    int aux = (int) (Double.parseDouble(precioNeto) / Double
                            .parseDouble(itemPromocion.getMonto()));
                    cantidad = aux * itemPromocion.getCant_promocion();
                    montoTotalUsado = aux
                            * Double.parseDouble(itemPromocion.getMonto());
                    // cantidadUsada = No se puede determinar, se calcula al
                    // final por cada entrada;

                } else {
                    Log.d("PedidosActivity ::obtenerCantidadBonificacion::",
                            "getCondicion NO ESPECIFICADA"
                                    + itemPromocion.getCondicion());
                }

            } else {
                if (itemPromocion.getCondicion().equals("2")) {
                    cantidad = itemPromocion.getCant_promocion();
                    montoTotalUsado = Double.parseDouble(precioNeto);
                }
            }

            /*
             * ------------------------------------------------------------------
             * --
             * ----------------------------------------------------------------
             * ----------------------------------- CÁLCULO DE LOS MONTOS Y
             * CANTIDADES USADOS POR CADA ENTRADA POR MONTOS
             */

            for (int it = 0; it < listaEntradasCompuestas.size(); it++) {
                // Se calcula la cantidad y monto usado por cada entrada hasta
                // que cumplan la cantidad condicion de la promocion
                if (montoUsadoAux < montoTotalUsado) {

                    String[] entradaCompuesta = listaEntradasCompuestas.get(it);
                    cantidadAux = Integer.parseInt(entradaCompuesta[1]); // Cantidad
                    // de
                    // la
                    // entrada
                    montoAux = Double.parseDouble(entradaCompuesta[2]); // Monto
                    // de la
                    // entrada
                    montoUsadoAux = montoUsadoAux + montoAux;

                    // Si el monto sumada es mayor al monto de la condicion, se
                    // debe calcular la cantidad real y el monto real usado por
                    // esa entrada
                    if (montoUsadoAux > montoTotalUsado) {
                        double montoAux_real = montoAux
                                - (montoUsadoAux - montoTotalUsado);
                        double cantAux = ((cantidadAux * montoAux_real) / montoAux);

                        int cantidadAux_real = (int) Math.ceil(cantAux);

                        cantidadesUsadas.add(cantidadAux_real);
                        montosUsados.add(montoAux_real);
                        Log.d("", "agregando cantidad usada: "
                                + cantidadAux_real);
                        Log.d("", "agregando monto usado: " + montoAux_real);
                    } else {
                        // Si la cantidad sumada no es mayor se guarda la
                        // cantidad y monto completa de esa entrada
                        cantidadesUsadas.add(cantidadAux);
                        montosUsados.add(montoAux);
                    }

                } else {
                    // Si la cantidad se supera, las demas cantidades del
                    // acumulado no se toman en cuenta
                    cantidadesUsadas.add(0);
                    montosUsados.add(0.0);
                }
            }
            /*
             * ------------------------------------------------------------------
             * --
             * ----------------------------------------------------------------
             * -----------------------------------
             */
        }
        Log.w("", "--------------------------  -------------------------------");
        return cantidad;
    }

    public int obtenerCantidadBonificacionAcumulado_Puro(
            DB_PromocionDetalle itemPromocion, DBPedido_Detalle itemDetalle,
            String Oc_numero) {
        int cantidad = 0;
        Log.d("PedidosActivity ::obtenerCantidadBonificacion::",
                "obteniendo lista de detalle acumulado pedido del oc_numero->"
                        + Oc_numero);
        Log.d("PedidosActivity ::obtenerCantidadBonificacion::",
                "▬▬▬▬▬▬▬▬ Analizis del detalle ▬▬▬▬▬▬▬");

        ArrayList<DBPedido_Detalle> lista_detallePedido = dbclass
                .getPedidosDetallexOc_numero(Oc_numero);
        ArrayList<DB_PromocionDetalle> lista_acumulados = dbclass
                .obtenerListaAcumulados(itemPromocion.getItem(),
                        itemPromocion.getSecuencia());

        int cantidadAcumulada = 0;
        double montoAcumulado = 0.0;

        /*
         *  ******** PROMOCION ACUMULADO NORMAL
         * **********************************
         * ************************************
         * ************************************************************
         */

        for (DBPedido_Detalle dbPedido_Detalle : lista_detallePedido) {
            for (DB_PromocionDetalle db_PromocionDetalle : lista_acumulados) {
                if (dbPedido_Detalle.getCip().equals(
                        db_PromocionDetalle.getEntrada())) {

                    /*
                     * VERIFICAR LA DISPONIBILIDAD DEL PRODUCTO 1. SI EL
                     * PRODUCTO NO TIENE DISPONIBILIDAD (NO ESTA REGISTRADO) -se
                     * obtienen las cantidades del detalle directamente -las
                     * cantidades ingresadas se deben convertir a unidades
                     * minimas para calcular la bonificacion -se usan todas las
                     * cantidades posibles y se registra la cantidad disponible
                     * de los productos - 2. SI EL PRODUCTO TIENE DISPONIBILIDAD
                     * (ESTA REGISTRADO) -se obtienen las cantidades del
                     * registro bonificaciones campo(cantidadDisponible)
                     */
                    int cantidadDisponible = DAOBonificaciones.getCantidadDisponible(Oc_numero,dbPedido_Detalle.getCip(),null, 0);
                    double montoDisponible = DAOBonificaciones.getMontoDisponible(Oc_numero,dbPedido_Detalle.getCip(),null, 0);
                    int cantidadUnidadesMin = Convertir_toUnidadesMinimas(dbPedido_Detalle.getCip(),dbPedido_Detalle.getUnidad_medida(),dbPedido_Detalle.getCantidad());
                    // Si hay cantidad disponible tambien hay monto disponible

                    if (cantidadDisponible == -1) {
                        // EL PRODUCTO NO TIENE DISPONIBILIDAD (NO ESTA
                        // REGISTRADO)
                        // Se deben convertir a unidades minimas

                        Log.d("", "acumulando cantidad: " + cantidadUnidadesMin);
                        Log.d("",
                                "acumulando monto: "
                                        + dbPedido_Detalle.getPrecio_neto());

                        cantidadDisponible = cantidadUnidadesMin;
                        montoDisponible = Double.parseDouble(dbPedido_Detalle
                                .getPrecio_neto());

                    } else if (cantidadDisponible > 0 || montoDisponible > 0.0) {
                        // EL PRODUCTO TIENE DISPONIBILIDAD (ESTA REGISTRADO)

                    } else {
                        // EL PRODUCTO TIENE DISPONIBILIDAD DE 0
                        // No puede usarse para la bonificacion
                        Log.e("",
                                "Producto: "
                                        + dbPedido_Detalle.getCip()
                                        + " no tiene cantidad disponible para la bonificacion");
                        cantidadDisponible = 0;
                        montoDisponible = 0;
                    }
                    cantidadAcumulada = cantidadAcumulada + cantidadDisponible;
                    montoAcumulado = montoAcumulado + montoDisponible;

                    int tipo_unidad_medida = dbclass.isCodunimed_Almacen(
                            dbPedido_Detalle.getCip(),
                            dbPedido_Detalle.getUnidad_medida());
                    Log.e("", "tipo unimed >>> " + tipo_unidad_medida);
                    Log.d("obtenerCantidadBonificacionAcumulado",
                            db_PromocionDetalle.getEntrada());
                    Log.d("obtenerCantidadBonificacionAcumulado",
                            dbPedido_Detalle.getPrecio_neto());

                    entradasCompuestas = new String[8];
                    entradasCompuestas[0] = db_PromocionDetalle.getEntrada();
                    entradasCompuestas[1] = String.valueOf(cantidadDisponible);
                    entradasCompuestas[2] = String.valueOf(montoDisponible);
                    entradasCompuestas[3] = String.valueOf(tipo_unidad_medida);
                    entradasCompuestas[4] = String.valueOf(dbPedido_Detalle
                            .getUnidad_medida());
                    entradasCompuestas[5] = String.valueOf(db_PromocionDetalle
                            .getAgrupado());
                    entradasCompuestas[6] = String.valueOf(dbPedido_Detalle
                            .getCantidad());
                    entradasCompuestas[7] = String.valueOf(dbPedido_Detalle
                            .getPrecio_neto());
                    Log.e("", "cantidad entrada entradasCompuestas[1]:   "
                            + cantidadUnidadesMin);
                    listaEntradasCompuestas.add(entradasCompuestas);

                    /*
                     * GUARDAR TAMBIEN LA CANTIDAD CALCULADA EN UNIDADES MINIMAS
                     * JUNTO A SU UNIDAD MEDIDA Y FACTOR DE CONVERSION
                     */
                    // Log.e("", "tipo unimed >>>>> " + entradasCompuestas[3]);

                    /**
                     * AL REGISTRAR POR CANTIDAD Y MONTO SE DEBE CALCULAR LA
                     * CANTIDAD O MONTO DISPONIBLE DE CADA ENTRADA QUE COMPONE
                     * LA PROMOCIÓN
                     */
                }
            }
        }

        Log.d("PedidosActivity ::obtenerCantidadBonificacion::",
                "▬▬▬▬▬▬▬▬ ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬ ▬▬▬▬▬▬▬");
        // Cambiamos la cantidad del pedido, acumulandolos
        Log.d("Cantidad", "cantidad acumulada por Acumulados-> "+ cantidadAcumulada);
        itemDetalle.setCantidad(cantidadAcumulada);
        itemDetalle.setPrecio_neto(montoAcumulado + "");

        cantidadesUsadas = new ArrayList<Integer>();
        montosUsados = new ArrayList<Double>();

        if (itemPromocion.getTipo().equals("C")) {
            cantidad = verificarTipoCondicionAcumulados_xCantidad(
                    itemPromocion, itemDetalle.getPrecio_neto(),
                    itemDetalle.getCantidad());
        } else {
            cantidad = verificarTipoCondicionAcumulados_xMonto(itemPromocion,
                    itemDetalle.getPrecio_neto(), itemDetalle.getCantidad());
        }

        Log.d("Cantidad", "cantidad bonificacion obtenido por Acumulados-> "+ cantidad);

        return cantidad;
    }

    public int obtenerCantidadBonificacionAcumulado_Combinado(
            DB_PromocionDetalle itemPromocion, DBPedido_Detalle itemDetalle,
            String Oc_numero) {
        int cantidadBonificacion_AND = 0;
        int cantidadBonificacion_OR = 0;
        int bonificacionMenor = 0;
        Log.d("PedidosActivity ::obtenerCantidadBonificacionAcumulado_Combinado::",
                "obteniendo lista de detalle acumulado pedido del oc_numero->"
                        + Oc_numero);
        Log.d("PedidosActivity ::obtenerCantidadBonificacionAcumulado_Combinado::",
                "▬▬▬▬▬▬▬▬ Analisis del detalle ▬▬▬▬▬▬▬");

        ArrayList<DBPedido_Detalle> lista_detallePedido = dbclass
                .getPedidosDetallexOc_numero(Oc_numero);
        ArrayList<Integer> cantidadesIndividual = new ArrayList<Integer>();

        int contador = 0;

        /*
         *  ******** PROMOCION ACUMULADO MIXTO
         * ***********************************
         * ***********************************
         * ************************************************************
         */

        ArrayList<DB_PromocionDetalle> lista_acumuladosAND = DAOPromocionDetalle
                .getAcumuladosAND(itemPromocion);
        ArrayList<DB_PromocionDetalle> lista_acumuladosOR = DAOPromocionDetalle
                .getAcumuladosOR(itemPromocion);

        ArrayList<String[]> listaEntradasCompuestas_auxAND = new ArrayList<String[]>();
        Log.v("PedidosActivity :obtenerCantidadBonificacionAcumulado_Combinado:",
                "••••••••••••••••••••Analisis lista AND•••••••••••••••••••••");
        for (DB_PromocionDetalle promocionAND : lista_acumuladosAND) {
            for (DBPedido_Detalle pedidoDetalle : lista_detallePedido) {
                if (promocionAND.getEntrada().equals(pedidoDetalle.getCip())) {
                    /*
                     * VERIFICAR LA DISPONIBILIDAD DEL PRODUCTO 1. SI EL
                     * PRODUCTO NO TIENE DISPONIBILIDAD (NO ESTA REGISTRADO) -se
                     * obtienen las cantidades del detalle directamente -las
                     * cantidades ingresadas se deben convertir a unidades
                     * minimas para calcular la bonificacion -se usan todas las
                     * cantidades posibles y se registra la cantidad disponible
                     * de los productos - 2. SI EL PRODUCTO TIENE DISPONIBILIDAD
                     * (ESTA REGISTRADO) -se obtienen las cantidades del
                     * registro bonificaciones campo(cantidadDisponible)
                     */
                    int cantidadDisponible = DAOBonificaciones.getCantidadDisponible(Oc_numero,pedidoDetalle.getCip(),null,0);
                    double montoDisponible = DAOBonificaciones.getMontoDisponible(Oc_numero,pedidoDetalle.getCip(),null,0);
                    int cantidadUnidadesMin = Convertir_toUnidadesMinimas(pedidoDetalle.getCip(),pedidoDetalle.getUnidad_medida(),pedidoDetalle.getCantidad());
                    // Si hay cantidad disponible tambien hay monto disponible

                    if (cantidadDisponible == -1) {
                        /*
                         * El producto no esta en alguna otra bonificacion, se
                         * toma la cantidad ingresada en el pedido
                         */
                        cantidadDisponible = cantidadUnidadesMin;
                        montoDisponible = Double.parseDouble(pedidoDetalle.getPrecio_neto());
                    } else if (cantidadDisponible > 0 || montoDisponible > 0.0) {
                        // EL PRODUCTO TIENE DISPONIBILIDAD (ESTA REGISTRADO)

                    } else {
                        // EL PRODUCTO TIENE DISPONIBILIDAD DE 0
                        // No puede usarse para la bonificacion
                        Log.e("","Producto: "+ pedidoDetalle.getCip()+ " no tiene cantidad disponible para la bonificacion");
                        cantidadDisponible = 0;
                        montoDisponible = 0;
                    }

                    int auxCantidadBonif = verificarTipoCondicion(promocionAND,montoDisponible, cantidadDisponible);
                    int tipo_unidad_medida = dbclass.isCodunimed_Almacen(pedidoDetalle.getCip(),pedidoDetalle.getUnidad_medida());
                    Log.e("obtenerCantidadBonificacion","cantidad bonificacion obtenido por item "+ pedidoDetalle.getCip() + " -> "+ auxCantidadBonif);

                    cantidadesIndividual.add(auxCantidadBonif);
                    // Todos los items de una promocion agrupada tienen y deben
                    // tener la misma cantidad de bonificacion salida
                    /*
                     * Se guarda la cantidad obtenida para luego comparar todas
                     * y elegir la idonea
                     */

                    if (auxCantidadBonif > 0) {
                        contador++;

                        entradasCompuestas = new String[8];
                        entradasCompuestas[0] = promocionAND.getEntrada();
                        entradasCompuestas[1] = String.valueOf(cantidadDisponible);
                        entradasCompuestas[2] = String.valueOf(montoDisponible);
                        entradasCompuestas[3] = String.valueOf(tipo_unidad_medida);
                        entradasCompuestas[4] = String.valueOf(pedidoDetalle.getUnidad_medida());
                        entradasCompuestas[5] = String.valueOf(promocionAND.getAgrupado());
                        entradasCompuestas[6] = String.valueOf(pedidoDetalle.getCantidad());
                        entradasCompuestas[7] = String.valueOf(pedidoDetalle.getPrecio_neto());
                        Log.e("", "cantidad entrada entradasCompuestas[1]:   "+ cantidadDisponible);
                        listaEntradasCompuestas_auxAND.add(entradasCompuestas);

                        /*
                         * Si la promocion cumplio con su condicion se guarda en
                         * la lista, al final si la lista es igual a la cantidad
                         * de agrupados se aplica el descuento de cantidades si
                         * no, no pasa nada y la bonificacion tampoco se aplica
                         */
                    }
                }
            }
        }
        Log.v("PedidosActivity :obtenerCantidadBonificacionAcumulado_Combinado:",
                "••••••••••••••••••••••••••••••••••••••••••••••••••••••••••");
        /* Se obtiene la cantidad bonificada menor para la lista AND */
        if (lista_acumuladosAND.size() == contador) {
            /*
             * Si todas las cantidades obtenidas son iguales elegir cualquiera,
             * Si no son iguales escoger la cantidad minima
             */

            int cantidadBonifMenor = cantidadesIndividual.get(0);
            int cantidadBonifTem = cantidadesIndividual.get(0);

            for (int x = 0; x < cantidadesIndividual.size(); x++) {
                if (cantidadBonifTem != cantidadesIndividual.get(x)) {
                    /* Si no son iguales */
                    if (cantidadesIndividual.get(x) < cantidadBonifMenor) {
                        /*
                         * Si la cantidad es la menor hasta ahora se guarda como
                         * tal
                         */
                        cantidadBonifMenor = cantidadesIndividual.get(x);
                        // Es la cantidad menor bonificado mas no utilizado
                    }
                }
            }
            cantidadBonificacion_AND = cantidadBonifMenor;
            Log.v("",
                    "las promociones cumplieron con su condicion, cantidad bonificacion obtenido por Acumulados AND -> "
                            + cantidadBonificacion_AND);

            /****************************************************** ANALISIS DE LA LISTA OR *******************************************************/
            int cantidadAcumulada = 0;
            double montoAcumulado = 0.0;
            ArrayList<String[]> listaEntradasCompuestas_auxOR = new ArrayList<String[]>();

            Log.v("PedidosActivity :obtenerCantidadBonificacionAcumulado_Combinado:",
                    "••••••••••••••••••••Analisis lista OR•••••••••••••••••••••");
            /*
             * Para analizar el tipo condicion de la promocion se necesita
             * guardar referencia de un item de la lista OR (cantidad
             * condicion,cantidad promocion)
             */
            DB_PromocionDetalle referenciaOR = new DB_PromocionDetalle();
            for (DB_PromocionDetalle promocionOR : lista_acumuladosOR) {
                for (DBPedido_Detalle pedidoDetalle2 : lista_detallePedido) {
                    if (promocionOR.getEntrada()
                            .equals(pedidoDetalle2.getCip())) {

                        int cantidadDisponible = DAOBonificaciones
                                .getCantidadDisponible(Oc_numero,
                                        pedidoDetalle2.getCip(),null, 0);
                        double montoDisponible = DAOBonificaciones
                                .getMontoDisponible(Oc_numero,
                                        pedidoDetalle2.getCip(),null, 0);
                        int cantidadUnidadesMin = Convertir_toUnidadesMinimas(
                                pedidoDetalle2.getCip(),
                                pedidoDetalle2.getUnidad_medida(),
                                pedidoDetalle2.getCantidad());

                        if (cantidadDisponible == -1) {
                            // EL PRODUCTO NO TIENE DISPONIBILIDAD (NO ESTA
                            // REGISTRADO)
                            Log.d("", "acumulando cantidad: "
                                    + cantidadUnidadesMin);
                            Log.d("",
                                    "acumulando monto: "
                                            + pedidoDetalle2.getPrecio_neto());

                            cantidadDisponible = cantidadUnidadesMin;
                            montoDisponible = Double.parseDouble(pedidoDetalle2
                                    .getPrecio_neto());

                        } else if (cantidadDisponible > 0
                                || montoDisponible > 0.0) {
                            // EL PRODUCTO TIENE DISPONIBILIDAD (ESTA
                            // REGISTRADO)
                        } else {
                            // EL PRODUCTO TIENE DISPONIBILIDAD DE 0 NO PUEDE
                            // USARSE
                            Log.e("",
                                    "Producto: "
                                            + pedidoDetalle2.getCip()
                                            + " no tiene cantidad disponible para la bonificacion");
                            cantidadDisponible = 0;
                            montoDisponible = 0;
                        }

                        cantidadAcumulada = cantidadAcumulada
                                + cantidadDisponible;
                        montoAcumulado = montoAcumulado + montoDisponible;

                        int tipo_unidad_medida = dbclass.isCodunimed_Almacen(
                                pedidoDetalle2.getCip(),
                                pedidoDetalle2.getUnidad_medida());
                        Log.e("", "tipo unimed >>> " + tipo_unidad_medida);
                        Log.d("obtenerCantidadBonificacionAcumulado",
                                promocionOR.getEntrada());
                        Log.d("obtenerCantidadBonificacionAcumulado",
                                pedidoDetalle2.getPrecio_neto());

                        entradasCompuestas = new String[8];
                        entradasCompuestas[0] = promocionOR.getEntrada();
                        entradasCompuestas[1] = String
                                .valueOf(cantidadDisponible);
                        entradasCompuestas[2] = String.valueOf(montoDisponible);
                        entradasCompuestas[3] = String
                                .valueOf(tipo_unidad_medida);
                        entradasCompuestas[4] = String.valueOf(pedidoDetalle2
                                .getUnidad_medida());
                        entradasCompuestas[5] = String.valueOf(promocionOR
                                .getAgrupado());
                        entradasCompuestas[6] = String.valueOf(pedidoDetalle2
                                .getCantidad());
                        entradasCompuestas[7] = String.valueOf(pedidoDetalle2
                                .getPrecio_neto());
                        Log.e("", "cantidad entrada entradasCompuestas[1]:   "
                                + cantidadUnidadesMin);
                        listaEntradasCompuestas_auxOR.add(entradasCompuestas);
                    }
                }
                referenciaOR = promocionOR;
            }
            // Cambiamos la cantidad del pedido, acumulandolos
            // itemDetalle.setCantidad(cantidadAcumulada);
            // itemDetalle.setPrecio_neto(montoAcumulado + "");
            if (cantidadAcumulada > 0) {
                cantidadBonificacion_OR = verificarTipoCondicion(referenciaOR,
                        montoAcumulado, cantidadAcumulada);
            } else {
                /*
                 * SI NO SE ACUMULAN CANTIDADES, NO SE ESTA CUMPLIENDO CON LA
                 * CONDICION
                 */
                Log.d("Cantidad",
                        "cantidad bonificacion obtenido por Acumulados OR-> "
                                + cantidadBonificacion_OR);
                Log.v("PedidosActivity :obtenerCantidadBonificacionAcumulado_Combinado:",
                        "••••••••••••••••••••Analisis lista OR•••••••••••••••••••••");
                return 0;
            }

            Log.d("Cantidad",
                    "cantidad bonificacion obtenido por Acumulados OR-> "
                            + cantidadBonificacion_OR);
            Log.v("PedidosActivity :obtenerCantidadBonificacionAcumulado_Combinado:",
                    "••••••••••••••••••••Analisis lista OR•••••••••••••••••••••");
            /*
             * Se debe obtener las cantidad minima bonificada entre la lista AND
             * y OR
             */

            if (cantidadBonificacion_OR <= cantidadBonificacion_AND) {
                bonificacionMenor = cantidadBonificacion_OR;
            } else {
                bonificacionMenor = cantidadBonificacion_AND;
            }

            /*
             * Se debe obtener el tamaño de la listaEntradasCompuestas_auxAND
             * para recorrer la listaEntradasCompuestas general a partir de ese
             * punto, obteniendo solo la listaEntradasCompuestas_auxOR y
             * calculando las cantidades usadas
             */
            int indiceListaOR = listaEntradasCompuestas_auxAND.size();

            /* ENTRADAS COMPUESTAS DE AMBAS LISTAS AND y OR */
            for (int i = 0; i < listaEntradasCompuestas_auxAND.size(); i++) {
                listaEntradasCompuestas.add(listaEntradasCompuestas_auxAND
                        .get(i));
            }
            for (int i = 0; i < listaEntradasCompuestas_auxOR.size(); i++) {
                listaEntradasCompuestas.add(listaEntradasCompuestas_auxOR
                        .get(i));
            }

            /* CALCULOS DE LOS MONTOS USADOS AND */

            cantidadesUsadas = new ArrayList<Integer>();
            montosUsados = new ArrayList<Double>();

            for (int la = 0; la < lista_acumuladosAND.size(); la++) {
                DB_PromocionDetalle AuxItemPromo = new DB_PromocionDetalle();
                AuxItemPromo = lista_acumuladosAND.get(la);

                for (int lp = 0; lp < lista_detallePedido.size(); lp++) {
                    DBPedido_Detalle AuxItemDetalle = new DBPedido_Detalle();
                    AuxItemDetalle = lista_detallePedido.get(lp);
                    if (AuxItemPromo.getEntrada().equals(
                            AuxItemDetalle.getCip())) {

                        int cantidadDisponible = DAOBonificaciones
                                .getCantidadDisponible(Oc_numero,
                                        AuxItemDetalle.getCip(),null, 0);
                        double montoDisponible = DAOBonificaciones
                                .getMontoDisponible(Oc_numero,
                                        AuxItemDetalle.getCip(),null, 0);
                        if (cantidadDisponible == -1) {
                            int cantidadCalculada = Convertir_toUnidadesMinimas(
                                    AuxItemDetalle.getCip(),
                                    AuxItemDetalle.getUnidad_medida(),
                                    AuxItemDetalle.getCantidad());
                            cantidadDisponible = cantidadCalculada;
                            montoDisponible = Double.parseDouble(AuxItemDetalle
                                    .getPrecio_neto());
                        } else if (cantidadDisponible > 0) {
                            // EL PRODUCTO TIENE DISPONIBILIDAD (ESTA
                            // REGISTRADO)
                        } else {
                            Log.e("",
                                    "Producto: "
                                            + AuxItemDetalle.getCip()
                                            + " no tiene cantidad disponible para la bonificacion");
                            cantidadDisponible = 0;
                            montoDisponible = 0;
                        }

                        if (AuxItemPromo.getTipo().equals("C")) {
                            Log.d("", cantidadDisponible + " "+ montoDisponible + " " + bonificacionMenor);
                            CalcularProductosUsados_xCantidad(AuxItemPromo,montoDisponible, cantidadDisponible,bonificacionMenor);
                        } else if (AuxItemPromo.getTipo().equals("M")) {
                            CalcularProductosUsados_xMonto(AuxItemPromo,montoDisponible, cantidadDisponible,bonificacionMenor);
                        }
                    }
                }
            }

            /* CALCULOS DE LOS MONTOS USADOS OR */

            Log.d("PedidosActivity ::obtenerCantidadBonificacion::",
                    "▬▬▬▬▬▬▬▬ ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬ ▬▬▬▬▬▬▬");

            if (referenciaOR.getTipo().equals("C")) {
                CalcularProductosUsados_xCantidadOR(referenciaOR,cantidadAcumulada, indiceListaOR, bonificacionMenor);
            } else if (referenciaOR.getTipo().equals("M")) {
                CalcularProductosUsados_xMontoOR(referenciaOR, montoAcumulado,indiceListaOR, bonificacionMenor);
            }

            Log.v("PedidosActivity :obtenerCantidadBonificacionAcumulado_Combinado:",
                    "••••••••••••••••••••••••••••••••••••••••••••••••••••••••••");
            /****************************************************** FIN ANALISIS LA LISTA OR *******************************************************/

        } else {
            Log.e("",
                    "No se cumplieron todas las condiciones lista_agrupados AND: "
                            + lista_acumuladosAND.size() + "  contador: "
                            + contador);
        }

        Log.d("PedidosActivity :obtenerCantidadBonificacionAcumulado_Combinado:",
                "listaEntradasCompuestas tamaño: "
                        + listaEntradasCompuestas.size());

        return bonificacionMenor;
    }

    public int obtenerCantidadBonificacion(DB_PromocionDetalle itemPromocion,DBPedido_Detalle itemDetalle, String Oc_numero) {

        /*
         * 1.- Verificar campo agrupado
         * -Si tienen el mismo codigo"-> &
         * -Si tienen codigos distintos"-> ó
         */

        ArrayList<DB_PromocionDetalle> lista_agrupados = new ArrayList<DB_PromocionDetalle>();
        lista_agrupados = dbclass.obtenerListaAgrupados(itemPromocion.getSecuencia(), itemPromocion.getItem(),itemPromocion.getAgrupado());

        /*
         * 2.- Verificar tipo [C,M] = Cantidad,Monto,Peso
         * 3.- Verificar por condición [1,2,3] = mayor o igual, menor o igual, por cada
         */
        int cantidad = 0;
        cantidadesUsadas = new ArrayList<Integer>();
        montosUsados = new ArrayList<Double>();

        if (lista_agrupados.size() > 1) {// De tipo &
            Log.d("", "Es de tipo &");
            Log.d("PedidosActivity ::obtenerCantidadBonificacion::","========= Analizis del detalle =========");

            ArrayList<DBPedido_Detalle> lista_TododetallePedido = dbclass.getPedidosDetallexOc_numero(Oc_numero);
            int contador = 0;

            /* Array de las cantidades obtenidas por los item agrupado individualmente */
            ArrayList<Integer> cantidadesIndividual = new ArrayList<Integer>();

            /*
             * Verificar si cada registro con el mismo agrupado cumple con su
             * condicion
             */
            for (int i = 0; i < lista_agrupados.size(); i++) {
                DB_PromocionDetalle AuxItemPromo = new DB_PromocionDetalle();
                AuxItemPromo = lista_agrupados.get(i);

                Log.v("","lista_agrupados(" + i + ")  "+ AuxItemPromo.getEntrada());

                for (int p = 0; p < lista_TododetallePedido.size(); p++) {
                    DBPedido_Detalle AuxItemDetalle = new DBPedido_Detalle();
                    AuxItemDetalle = lista_TododetallePedido.get(p);
                    /*
                     * Verifica si el item promocion esta en el detalle y si
                     * cumple con la condicion
                     */

                    if (AuxItemPromo.getEntrada().equals(AuxItemDetalle.getCip())) {

                        int cantidadDisponible = DAOBonificaciones.getCantidadDisponible(Oc_numero,AuxItemDetalle.getCip(),null, 0);
                        double montoDisponible = DAOBonificaciones.getMontoDisponible(Oc_numero,AuxItemDetalle.getCip(),null,0);
                        int cantidadUnidadesMin = Convertir_toUnidadesMinimas(AuxItemDetalle.getCip(),AuxItemDetalle.getUnidad_medida(),AuxItemDetalle.getCantidad());
                        // Si hay cantidad disponible tambien hay monto disponible

                        if (cantidadDisponible == -1) {
                            // EL PRODUCTO NO TIENE DISPONIBILIDAD (NO ESTA REGISTRADO) Se deben convertir a unidades minimas

                            cantidadDisponible = cantidadUnidadesMin;
                            montoDisponible = Double.parseDouble(AuxItemDetalle.getPrecio_neto());

                            Log.d("", AuxItemDetalle.getCip()+ " Cantidad disponible: "+ cantidadUnidadesMin);
                            Log.d("",AuxItemDetalle.getCip()+ " Monto disponnible: "+ AuxItemDetalle.getPrecio_neto());

                        } else if (cantidadDisponible > 0) {
                            // EL PRODUCTO TIENE DISPONIBILIDAD (ESTA REGISTRADO)

                        } else {
                            // EL PRODUCTO TIENE DISPONIBILIDAD DE 0 No puede usarse para la bonificacion
                            Log.e("","Producto: "+ AuxItemDetalle.getCip()+ " no tiene cantidad disponible para la bonificacion");
                            cantidadDisponible = 0;
                            montoDisponible = 0;
                        }

                        int auxCantidadBonif = verificarTipoCondicion(AuxItemPromo, montoDisponible,cantidadDisponible);
                        int tipo_unidad_medida = dbclass.isCodunimed_Almacen(AuxItemDetalle.getCip(),
                                AuxItemDetalle.getUnidad_medida());
                        Log.e("obtenerCantidadBonificacion","cantidad bonificacion obtenido por item "+ AuxItemDetalle.getCip() + " -> "+ auxCantidadBonif);

                        cantidadesIndividual.add(auxCantidadBonif);
                        // Todos los items de una promocion agrupada tienen y
                        // deben tener la misma cantidad de bonificacion salida
                        /*
                         * Se guarda la cantidad obtenida para luego comparar
                         * todas y elegir la idonea
                         */

                        if (auxCantidadBonif > 0) {
                            contador++;

                            // entrada -> codigoProducto
                            // cantidad
                            // monto
                            // tipoUnimed -> del producto entrada
                            // unimedEntrada ->
                            // agrupado

                            entradasCompuestas = new String[8];
                            entradasCompuestas[0] = AuxItemPromo.getEntrada();
                            entradasCompuestas[1] = String.valueOf(cantidadDisponible);
                            entradasCompuestas[2] = String.valueOf(montoDisponible);
                            entradasCompuestas[3] = String.valueOf(tipo_unidad_medida);
                            entradasCompuestas[4] = String.valueOf(AuxItemDetalle.getUnidad_medida());
                            entradasCompuestas[5] = String.valueOf(AuxItemPromo.getAgrupado());
                            entradasCompuestas[6] = String.valueOf(AuxItemDetalle.getCantidad());
                            entradasCompuestas[7] = String.valueOf(AuxItemDetalle.getPrecio_neto());
                            Log.e("","cantidad entrada entradasCompuestas[1]:   "+ cantidadDisponible);
                            listaEntradasCompuestas.add(entradasCompuestas);

                            /*
                             * Si la promocion cumplio con su condicion se
                             * guarda en la lista, al final si la lista es igual
                             * a la cantidad de agrupados se aplica el descuento
                             * de cantidades si no, no pasa nada y la
                             * bonificacion tampoco se aplica
                             */

                        }
                    } else {
                        Log.i("PedidosActivity","ENTRADA:" + AuxItemPromo.getEntrada()+ " NO IGUALA A CIP:"+ AuxItemDetalle.getCip());
                    }
                    AuxItemDetalle = null;
                }
            }

            /*
             * Si todos las promociones cumplieron su condicion, se procede a
             * obtener la bonificacion
             */
            if (lista_agrupados.size() == contador) {
                /*
                 * Si todas las cantidades obtenidas son iguales elegir
                 * cualquiera Si no son iguales escoger la cantidad minima
                 */

                int cantidadBonifMenor = cantidadesIndividual.get(0);
                int cantidadBonifTem = cantidadesIndividual.get(0);

                for (int x = 0; x < cantidadesIndividual.size(); x++) {
                    if (cantidadBonifTem != cantidadesIndividual.get(x)) {
                        /* Si no son iguales */
                        if (cantidadesIndividual.get(x) < cantidadBonifMenor) {
                            /*
                             * Si la cantidad es la menor hasta ahora se guarda
                             * como tal
                             */
                            cantidadBonifMenor = cantidadesIndividual.get(x);
                            // Es la cantidad menor bonificado mas no utilizado
                        }
                    }
                }
                cantidad = cantidadBonifMenor;
                Log.v("",
                        "Todos las promociones cumplieron con su condicion, cantidad bonificacion: "
                                + cantidad);

                for (int la = 0; la < lista_agrupados.size(); la++) {
                    DB_PromocionDetalle AuxItemPromo = new DB_PromocionDetalle();
                    AuxItemPromo = lista_agrupados.get(la);

                    for (int lp = 0; lp < lista_TododetallePedido.size(); lp++) {
                        DBPedido_Detalle AuxItemDetalle = new DBPedido_Detalle();
                        AuxItemDetalle = lista_TododetallePedido.get(lp);
                        if (AuxItemPromo.getEntrada().equals(
                                AuxItemDetalle.getCip())) {

                            int cantidadDisponible = DAOBonificaciones
                                    .getCantidadDisponible(Oc_numero,
                                            AuxItemDetalle.getCip(),null,0);
                            double montoDisponible = DAOBonificaciones
                                    .getMontoDisponible(Oc_numero,
                                            AuxItemDetalle.getCip(),null,0);
                            if (cantidadDisponible == -1) {
                                int cantidadCalculada = Convertir_toUnidadesMinimas(
                                        AuxItemDetalle.getCip(),
                                        AuxItemDetalle.getUnidad_medida(),
                                        AuxItemDetalle.getCantidad());
                                cantidadDisponible = cantidadCalculada;
                                montoDisponible = Double
                                        .parseDouble(AuxItemDetalle
                                                .getPrecio_neto());
                            } else if (cantidadDisponible > 0) {
                                // EL PRODUCTO TIENE DISPONIBILIDAD (ESTA
                                // REGISTRADO)
                            } else {
                                Log.e("",
                                        "Producto: "
                                                + AuxItemDetalle.getCip()
                                                + " no tiene cantidad disponible para la bonificacion");
                                cantidadDisponible = 0;
                                montoDisponible = 0;
                            }

                            if (AuxItemPromo.getTipo().equals("C")) {
                                Log.d("", cantidadDisponible + " "
                                        + montoDisponible + " " + cantidad);
                                CalcularProductosUsados_xCantidad(AuxItemPromo,
                                        montoDisponible, cantidadDisponible,
                                        cantidad);
                            } else {
                                CalcularProductosUsados_xMonto(AuxItemPromo,
                                        montoDisponible, cantidadDisponible,
                                        cantidadBonifMenor);
                            }
                        }
                    }
                }
                // ------

            } else {
                Log.e("",
                        "No se cumplieron todas las condiciones lista_agrupados: "
                                + lista_agrupados.size() + "  contador: "
                                + contador);
            }
            Log.d("PedidosActivity ::obtenerCantidadBonificacion::",
                    "==========================================");

            return cantidad;

        } else {// De tipo ó
            Log.d("", "Es de tipo Ó");

            int cantidadDisponible = DAOBonificaciones.getCantidadDisponible(
                    Oc_numero, itemDetalle.getCip(),null, 0);
            double montoDisponible = DAOBonificaciones.getMontoDisponible(
                    Oc_numero, itemDetalle.getCip(),null, 0);

            // Si hay cantidad disponible tambien hay monto disponible

            if (cantidadDisponible == -1) {
                // EL PRODUCTO NO TIENE DISPONIBILIDAD (NO ESTA REGISTRADO)
                // Se deben convertir a unidades minimas
                int cantidadUnimin = Convertir_toUnidadesMinimas(
                        itemDetalle.getCip(), itemDetalle.getUnidad_medida(),
                        itemDetalle.getCantidad());
                cantidadDisponible = cantidadUnimin;
                montoDisponible = Double.parseDouble(itemDetalle
                        .getPrecio_neto());
            } else if (cantidadDisponible > 0) {
                // EL PRODUCTO TIENE DISPONIBILIDAD (ESTA REGISTRADO)

            } else {
                // EL PRODUCTO TIENE DISPONIBILIDAD DE 0
                // No puede usarse para la bonificacion
                Log.e("", "Producto: " + itemDetalle.getCip()
                        + " no tiene cantidad disponible para la bonificacion");
                cantidadDisponible = 0;
                montoDisponible = 0;
            }
            Log.d("", itemDetalle.getCip() + " Cantidad disponible: "
                    + cantidadDisponible);
            Log.d("", itemDetalle.getCip() + " Monto disponnible: "
                    + montoDisponible);
            cantidad = verificarTipoCondicion(itemPromocion, montoDisponible,
                    cantidadDisponible);

            if (itemPromocion.getTipo().equals("C")) {
                CalcularProductosUsados_xCantidad(itemPromocion,
                        montoDisponible, cantidadDisponible, 0);
            } else if (itemPromocion.getTipo().equals("M")) {
                CalcularProductosUsados_xMonto(itemPromocion, montoDisponible,
                        cantidadDisponible, 0);
            }

            Log.d("PedidosActivity", "promocion del tipo O , cantidad-> "
                    + cantidad);
            return cantidad;
        }

    }

    public void CalcularProductosUsados_xCantidad(
            DB_PromocionDetalle itemPromocion, double precioNeto,
            int cantidadX, int cantidadBonificada) {

        int cantidadRealUsada = 0;
        double montoRealUsado = 0.0;

        Log.w("", "-------------- calculando cantidades usadas -------------");
        if (itemPromocion.getTipo().equals("C")) {
            if (cantidadX >= itemPromocion.getCant_condicion()) {
                if (itemPromocion.getCondicion().equals("1")) {
                    cantidadRealUsada = itemPromocion.getCant_condicion();
                    montoRealUsado = (cantidadRealUsada * precioNeto)
                            / cantidadX;
                } else if (itemPromocion.getCondicion().equals("3")) {

                    /*
                     * Si la cantidad bonificada es mayor que 0, ya se sabe
                     * cuanto se va a bonificar, esto viene analizado de los
                     * productos promocion agrupados las cantidades usadas van a
                     * ser dependiendo del minimo obtenido y dependiendo de la
                     * cantidad condicion de cada producto de la agrupación
                     * multiplicando la cantidad bonificada
                     */
                    if (cantidadBonificada > 0) {
                        /*
                         * Veces que se ha dado la promoción =
                         * cantidadBonificada/cantidadPromocion
                         * cantidadRealusada = cantidadCondicion * veces que se
                         * ha dado la promoción
                         */
                        cantidadRealUsada = itemPromocion.getCant_condicion()
                                * (cantidadBonificada / itemPromocion
                                .getCant_promocion());
                        montoRealUsado = (cantidadRealUsada * precioNeto)
                                / cantidadX;
                    } else {
                        cantidadRealUsada = (cantidadX / itemPromocion
                                .getCant_condicion())
                                * itemPromocion.getCant_condicion();
                        montoRealUsado = (cantidadRealUsada * precioNeto)
                                / cantidadX;
                    }
                } else {
                    Log.d("PedidosActivity ::obtenerCantidadBonificacion::",
                            "la condicion de promocion no esta establecido"
                                    + itemPromocion.getCondicion());
                }

            } else {
                // Cuando no se supera la condicion, es menor que..
                if (itemPromocion.getCondicion().equals("2")) {
                    cantidadRealUsada = cantidadX;
                    montoRealUsado = precioNeto;
                }
            }
            Log.e("PedidosActivity ::CalcularProductosUsados_xCantidad::",
                    itemPromocion.getEntrada() + " cantidad real usada: "
                            + cantidadRealUsada);
            Log.e("PedidosActivity ::CalcularProductosUsados_xCantidad::",
                    itemPromocion.getEntrada() + " monto real usada: "
                            + montoRealUsado);

            cantidadesUsadas.add(cantidadRealUsada);
            montosUsados.add(montoRealUsado);
        }
        Log.w("", "---------------------------------------------------------");
    }

    public void CalcularProductosUsados_xMonto(
            DB_PromocionDetalle itemPromocion, double precioNeto,
            int cantidadX, int cantidadBonificada) {

        int cantidadRealUsada = 0;
        double montoRealUsado = 0.0;

        /*
         * VERFIFICAR SU USO PARA EL FINAL DEL PEDIDO !!!!!!!!
         */
        Log.w("",
                "-------------- verificando Cantidades y montos usados -------------");
        if (itemPromocion.getTipo().equals("M")) {

            if (precioNeto >= Double.parseDouble(itemPromocion.getMonto())) {

                if (itemPromocion.getCondicion().equals("1")) {
                    montoRealUsado = Double.parseDouble(itemPromocion
                            .getMonto());
                    double cantAux = ((cantidadX * montoRealUsado) / precioNeto);
                    cantidadRealUsada = (int) Math.ceil(cantAux);// Redondear al
                    // maximo
                    // cercano
                    Log.e("CalcularProductosUsados_xMonto",
                            "Cantidad real usada " + itemPromocion.getEntrada()
                                    + ": " + cantidadRealUsada);

                } else if (itemPromocion.getCondicion().equals("3")) {

                    /*
                     * Si la cantidad bonificada es mayor que 0, ya se sabe
                     * cuanto se va a bonificar, esto viene analizado de los
                     * productos promocion agrupados las cantidades usadas van a
                     * ser dependiendo del minimo obtenido y dependiendo de la
                     * cantidad condicion de cada producto de la agrupación
                     * multiplicando la cantidad bonificada
                     */
                    if (cantidadBonificada > 0) {
                        /*
                         * Veces que se ha dado la promoción =
                         * cantidadBonificada/cantidadPromocion
                         * cantidadRealusada = cantidadCondicion * veces que se
                         * ha dado la promoción
                         */
                        montoRealUsado = Double.parseDouble(itemPromocion
                                .getMonto()) * cantidadBonificada;
                        double cantAux = (cantidadX * montoRealUsado)
                                / precioNeto;
                        cantidadRealUsada = (int) Math.ceil(cantAux);
                    } else {
                        int aux = (int) (precioNeto / Double
                                .parseDouble(itemPromocion.getMonto()));
                        montoRealUsado = aux
                                * Double.parseDouble(itemPromocion.getMonto());
                        double cantAux = (cantidadX * montoRealUsado)
                                / precioNeto;
                        cantidadRealUsada = (int) Math.ceil(cantAux);
                    }

                } else {
                    Log.d("PedidosActivity ::obtenerCantidadBonificacion::",
                            "la condicion de promocion no esta establecido"
                                    + itemPromocion.getCondicion());
                }

            } else {
                if (itemPromocion.getCondicion().equals("2")) {
                    cantidadRealUsada = cantidadX;
                    montoRealUsado = precioNeto;
                }
            }
            Log.e("PedidosActivity ::CalcularProductosUsados_xCantidad::",
                    itemPromocion.getEntrada() + " cantidad real usada: "
                            + cantidadRealUsada);
            Log.e("PedidosActivity ::CalcularProductosUsados_xCantidad::",
                    itemPromocion.getEntrada() + " monto real usada: "
                            + montoRealUsado);

            cantidadesUsadas.add(cantidadRealUsada);
            montosUsados.add(montoRealUsado);
        }
        Log.w("", "---------------------------------------------------------");
    }

    public void CalcularProductosUsados_xCantidadOR(
            DB_PromocionDetalle itemPromocion, int cantidadAcumulada,
            int indiceListaOR, int cantidadBonificada) {
        int cantidadTotalUsada = 0;

        int cantidadAux, cantidadUsadaAux = 0;
        double montoAux = 0.0;

        Log.w("",
                "---------------------- Calculando Productos Usados OR ----------------------");
        if (itemPromocion.getTipo().equals("C")) {
            if (cantidadAcumulada >= itemPromocion.getCant_condicion()) {
                // Cuando se supera la condicion, es mayor que..
                if (itemPromocion.getCondicion().equals("1")) {
                    cantidadTotalUsada = itemPromocion.getCant_condicion();
                } else if (itemPromocion.getCondicion().equals("3")) {
                    cantidadTotalUsada = itemPromocion.getCant_condicion()
                            * (cantidadBonificada / itemPromocion.cant_promocion);
                } else {
                    Log.d("PedidosActivity ::CalcularProductosUsados_xCantidadOR::",
                            "getCondicion NO ESPECIFICADA"
                                    + itemPromocion.getCondicion());
                }
            } else {
                // Cuando no se supera la condicion, es menor que..
                if (itemPromocion.getCondicion().equals("2")) {
                    cantidadTotalUsada = cantidadAcumulada;
                }
            }

            /*
             * ------------------------------------------------------------------
             * --
             * ----------------------------------------------------------------
             * ----------------------------------- CÁLCULO DE LOS MONTOS Y
             * CANTIDADES USADOS POR CADA ENTRADA POR CANTIDADES
             */
            /*
             * Luego de unir las listas AND y OR en una sola, indiceListaOR
             * indica desde donde se va a recorrer la listaEntradasCompuestas
             * (los items que perenezcan a la lista de entradasCompuesas_OR)
             */
            Log.d("PedidosActivity :CalcularProductosUsados_xCantidadOR:",
                    "cantidadTotalUsada: " + cantidadTotalUsada);
            for (int it = indiceListaOR; it < listaEntradasCompuestas.size(); it++) {
                // Se calcula la cantidad y monto usado por cada entrada hasta
                // que cumplan la cantidad condicion de la promocion
                if (cantidadUsadaAux < cantidadTotalUsada) {

                    String[] entradaCompuesta = listaEntradasCompuestas.get(it);
                    cantidadAux = Integer.parseInt(entradaCompuesta[1]); // Cantidad
                    // de
                    // la
                    // entrada
                    montoAux = Double.parseDouble(entradaCompuesta[2]); // Monto
                    // de la
                    // entrada
                    cantidadUsadaAux = cantidadUsadaAux + cantidadAux;

                    // Si la cantidad sumada es mayor a la cantidad de la
                    // condicion, se debe calcular la cantidad real y el monto
                    // real usado por esa entrada
                    if (cantidadUsadaAux > cantidadTotalUsada) {
                        int cantidadAux_real = cantidadAux
                                - (cantidadUsadaAux - cantidadTotalUsada);
                        double montoAux_real = (montoAux * cantidadAux_real)
                                / cantidadAux;

                        cantidadesUsadas.add(cantidadAux_real);
                        montosUsados.add(montoAux_real);
                        Log.d("", "agregando cantidad usada (exceso): "
                                + cantidadAux_real);
                    } else {
                        // Si la cantidad sumada no es mayor se guarda la
                        // cantidad y monto completa de esa entrada
                        cantidadesUsadas.add(cantidadAux);
                        montosUsados.add(montoAux);
                        Log.d("", "agregando cantidad usada: " + cantidadAux);
                    }

                } else {
                    // Si la cantidad se supera, las demas cantidades del
                    // acumulado no se toman en cuenta
                    cantidadesUsadas.add(0);
                    montosUsados.add(0.0);
                    Log.d("", "agregando cantidad usada: 0");
                }
            }

            /*
             * ------------------------------------------------------------------
             * --
             * ----------------------------------------------------------------
             * -----------------------------------
             */
        }
        Log.w("", "--------------------------  -------------------------------");
    }

    public void CalcularProductosUsados_xMontoOR(
            DB_PromocionDetalle itemPromocion, double montoAcumulado,
            int indiceListaOR, int cantidadBonificada) {
        double montoTotalUsado = 0.0;

        int cantidadAux = 0;
        double montoAux, montoUsadoAux = 0.0;

        Log.w("",
                "---------------------- Calculando Productos Usados OR ----------------------");
        if (itemPromocion.getTipo().equals("M")) {
            if (montoAcumulado >= Double.parseDouble(itemPromocion.getMonto())) {
                if (itemPromocion.getCondicion().equals("1")) {
                    montoTotalUsado = Double.parseDouble(itemPromocion
                            .getMonto());
                } else if (itemPromocion.getCondicion().equals("3")) {
                    int aux = (int) (montoAcumulado / Double
                            .parseDouble(itemPromocion.getMonto()));
                    montoTotalUsado = aux
                            * Double.parseDouble(itemPromocion.getMonto());
                } else {
                    Log.d("PedidosActivity ::CalcularProductosUsados_xMontoOR::",
                            "getCondicion NO ESPECIFICADA"
                                    + itemPromocion.getCondicion());
                }
            } else {
                if (itemPromocion.getCondicion().equals("2")) {
                    montoTotalUsado = montoAcumulado;
                }
            }

            /*
             * ------------------------------------------------------------------
             * --
             * ----------------------------------------------------------------
             * ----------------------------------- CÁLCULO DE LOS MONTOS Y
             * CANTIDADES USADOS POR CADA ENTRADA POR MONTOS
             */

            for (int it = indiceListaOR; it < listaEntradasCompuestas.size(); it++) {
                // Se calcula la cantidad y monto usado por cada entrada hasta
                // que cumplan la cantidad condicion de la promocion
                if (montoUsadoAux < montoTotalUsado) {

                    String[] entradaCompuesta = listaEntradasCompuestas.get(it);
                    cantidadAux = Integer.parseInt(entradaCompuesta[1]); // Cantidad
                    // de
                    // la
                    // entrada
                    montoAux = Double.parseDouble(entradaCompuesta[2]); // Monto
                    // de la
                    // entrada
                    montoUsadoAux = montoUsadoAux + montoAux;

                    // Si el monto sumada es mayor al monto de la condicion, se
                    // debe calcular la cantidad real y el monto real usado por
                    // esa entrada
                    if (montoUsadoAux > montoTotalUsado) {
                        double montoAux_real = montoAux
                                - (montoUsadoAux - montoTotalUsado);
                        double cantAux = ((cantidadAux * montoAux_real) / montoAux);

                        int cantidadAux_real = (int) Math.ceil(cantAux);

                        cantidadesUsadas.add(cantidadAux_real);
                        montosUsados.add(montoAux_real);
                        Log.d("", "agregando cantidad usada: "
                                + cantidadAux_real);
                        Log.d("", "agregando monto usado: " + montoAux_real);
                    } else {
                        // Si la cantidad sumada no es mayor se guarda la
                        // cantidad y monto completa de esa entrada
                        cantidadesUsadas.add(cantidadAux);
                        montosUsados.add(montoAux);
                    }

                } else {
                    // Si la cantidad se supera, las demas cantidades del
                    // acumulado no se toman en cuenta
                    cantidadesUsadas.add(0);
                    montosUsados.add(0.0);
                }
            }
            /*
             * ------------------------------------------------------------------
             * --
             * ----------------------------------------------------------------
             * -----------------------------------
             */
        }
        Log.w("", "--------------------------  -------------------------------");
    }

    public void mostrarListaProductos(String codigoSalida) {
        Log.d("PedidosActivity", "MOSTRANDO LISTA DE PRODUCTOS");
        //Si el parametro esta vacio significa el metodo fue invocado solo para visualizacion

        //Si el parametro tiene el codigoSalida significa que se ha realizado un movimiento
        //(se recalculó la salida sin tomar en cuenta la bonificacion pendiene agregada) con esa salida y se debe sumar la boniicacion pendiente agregada

        listaProductoDevolucion.clear();
        Oc_numero = edt_nroPedido.getText().toString();

        double peso_total = 0.0d;
        listaProductoDevolucion = dbclass.getListadoProductos_devolucion(Oc_numero);

        for (int i = 0; i < listaProductoDevolucion.size(); i++) {
            peso_total 	+= Double.parseDouble(listaProductoDevolucion.get(i).getPeso());
            dbclass.close();
        }
        if (listaProductoDevolucion.size()>0) {
            spn_sucursal.setEnabled(false);
        }else{
            spn_sucursal.setEnabled(true);
        }

        peso_total 		= GlobalFunctions.redondear_toDouble(peso_total);

        CH_Adapter_itemDevolucionProducto adapter = new CH_Adapter_itemDevolucionProducto(this, listaProductoDevolucion);
        lstProductos.setAdapter(adapter);

        tv_peso.setText(formaterMoneda.format(peso_total));
        tv_cantidadItems.setText(""+listaProductoDevolucion.size());
        dbclass.guardarPedidoTotales(new PedidoCabeceraRecalcular(
                Oc_numero,
                peso_total,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0.
        ));

        if (listaProductoDevolucion.size()>0) {
            GUARDAR_ACTIVO = true;
            //Tomar el numero de factura
            CURRENT_TIPO_FAC 	= listaProductoDevolucion.get(0).getTipoDocumento();
            CURRENT_SERIE_FAC	= listaProductoDevolucion.get(0).getSerie();
            CURRENT_NUMERO_FAC	= listaProductoDevolucion.get(0).getNumero();
        }else{
            GUARDAR_ACTIVO = false;
            CURRENT_TIPO_FAC 	= "";
            CURRENT_SERIE_FAC	= "";
            CURRENT_NUMERO_FAC	= "";
        }


    }

    private void ActualizarBonificacionesPendientes(String codigoSalida) {
        ArrayList<DB_RegistroBonificaciones> lista = DAOBonificaciones.getPendientesAgregados(Oc_numero);
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getSalida().equals(codigoSalida)) {
                DBPedido_Detalle dbdetalle_temporal = dbclass.existePedidoDetalle2(
                        Oc_numero,
                        lista.get(i).getSalida(),
                        dbclass.obtener_codunimedXtipo_unimed_salida(0,lista.get(i).getSalida().substring(1, lista.get(i).getSalida().length()))
                );

                if (dbdetalle_temporal != null) {
                    int cantidadActual = dbdetalle_temporal.getCantidad() + lista.get(i).getCantidadEntregada();
                    Log.d("PedidosActivity ","la bonificacion pendiente ya esta registrada con "+dbdetalle_temporal.getCantidad()+"... actualizando a "+cantidadActual);

                    if (!dbdetalle_temporal.getCod_politica().equals("ELIM")) {
                        dbclass.actualizarItem_promo(
                                Oc_numero,
                                lista.get(i).getSalida(),
                                dbclass.obtener_codunimedXtipo_unimed_salida(0,lista.get(i).getSalida()),
                                cantidadActual);
                    }
                } else {
                    Log.d("PedidosActivity ","la bonificacion pendiente no esta registrado.... agregando cantidad: "+ lista.get(i).getCantidadEntregada());
                    agregarProductoxPromocion(
                            lista.get(i).getSalida(),
                            ""+0,
                            lista.get(i).getCantidadEntregada());
                }
            }
        }
    }

    private int verificarSiExisteProducto(String entrada, int tipo_und_med,ArrayList<ItemProducto> productos) {
        for (int i = 0; i < productos.size(); i++) {
            String cod = productos.get(i).getCodprod();
            Log.d("Verificando si existe el producto",
                    "comparando codigoItemDetalle" + cod
                            + " -- codigoItemPromocion" + entrada);
            if (cod.equals(entrada)) {
                Log.d("Verificando si existe el producto", "SI");
                String codigoProducto = productos.get(i).getCodprod();
                String descripcionUnidadMed = productos.get(i).getDesunimed();

                String codunimed = dbclass.obtener_codXdesunimed(
                        codigoProducto, descripcionUnidadMed);
                int tipo_unidad_medida = dbclass.isCodunimed_Almacen(cod,codunimed);

                if (tipo_unidad_medida == tipo_und_med) {
                    Log.d("Verificando si existe el producto",
                            "tipos de unidad medida iguales, retornando indice de la lista de productos "
                                    + i);
                    return i;
                }

            }

        }
        return -1;
    }

    private void agregarProductoxPromocion(String salida,
                                           String tipo_unimed_salida, int cantidadProducto) {
        // TODO Auto-generated method stub

        DBPedido_Detalle itemDetalle = new DBPedido_Detalle();
        itemDetalle.setOc_numero(edt_nroPedido.getText().toString());
        itemDetalle.setCip(salida);
        itemDetalle.setEan_item("");
        itemDetalle.setPrecio_bruto("0");
        itemDetalle.setPercepcion("0");
        itemDetalle.setPrecio_neto("0");
        itemDetalle.setCantidad(cantidadProducto);
        itemDetalle.setTipo_producto("C");
        itemDetalle.setUnidad_medida((dbclass.obtener_codunimedXtipo_unimed_salida(Integer.parseInt(tipo_unimed_salida), salida)));
        itemDetalle.setPeso_bruto("0");
        itemDetalle.setFlag("N");
        itemDetalle.setPrecioLista("0");
        itemDetalle.setDescuento("0");

        dbclass.AgregarPedidoDetalle(itemDetalle);
    }

    private int Convertir_toUnidadesMinimas(String codigoProducto,
                                            String unidadMedidaProd, int cantidad) {
        int tipo_unidad_medida = dbclass.isCodunimed_Almacen(codigoProducto,unidadMedidaProd);
        int Cantidad_UnidadMin = 0;

        if (tipo_unidad_medida != -1) {
            // Se obtiene la promocion del producto en promocion_detalle

            if (tipo_unidad_medida == 0) {// es la unidad minima, no se aplica
                // factor de conversion
                Log.d("", "Unidad minima misma cantidad");
                Cantidad_UnidadMin = cantidad;
            } else {// Debe transformarse a la unidad minima

                int factor_conversion = dbclass
                        .getFactorConversion(codigoProducto);
                Cantidad_UnidadMin = cantidad * factor_conversion;
                Log.d("", "calculando cantidad:  " + cantidad + " * "
                        + factor_conversion);
                Log.d("", "nueva cantidad calculada por factor conversion: "
                        + Cantidad_UnidadMin);

            }
            return Cantidad_UnidadMin;
        } else {
            Log.e("Convertir_UnidadesMinimas",
                    "valor devuelto = -1, no hay concidencia entre unidad_medida y tipo_unida_medida_entrada !!!!!!!!!!!!!!!!!!");
            return cantidad;
        }

    }

    Dialog dialogo_codFamiliar;

    public void crearDialogo_CodigoFamiliar() {

        dialogo_codFamiliar = new Dialog(this);

        dialogo_codFamiliar.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialogo_codFamiliar.setContentView(R.layout.dialog_codigofamiliar);
        dialogo_codFamiliar.setCancelable(false);

        final ListView lstClientes = (ListView) dialogo_codFamiliar
                .findViewById(R.id.dialog_codigofamiliar_lvClientes);
        Button btnAceptar = (Button) dialogo_codFamiliar
                .findViewById(R.id.dialog_codigofamiliar_btnAceptar);
        Button btnCancelar = (Button) dialogo_codFamiliar
                .findViewById(R.id.dialog_codigofamiliar_btnCancelar);
        lstClientes.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        ArrayList<DBClientes> al_clientes = dbclass.getFamiliasxCliente(codcli);

        AdapterDialogCodFamiliar adaptador = new AdapterDialogCodFamiliar(this,
                al_clientes);
        lstClientes.setAdapter(adaptador);

        lstClientes
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> adapter, View arg1,
                                            int position, long id) {

                        selectedPosition2 = position;
                        Log.w("ListClientesONclick", "position"
                                + selectedPosition2);
                    }
                });

        btnAceptar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                clienteEntity = (DBClientes) lstClientes.getAdapter().getItem(
                        selectedPosition2);

                final Handler handle = new Handler();
                handle.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        // new asyncGuardarMotivo().execute("","");
                        guardarCodFamiliar();
                        guardarCodcli();
                        asignarNombreCabecera();
                        dialogo_codFamiliar.dismiss();
                    }

                    private void asignarNombreCabecera() {
                        // TODO Auto-generated method stub
                        autocomplete.setText(nombre_familiar);
                    }

                    private void guardarCodcli() {
                        // TODO Auto-generated method stub
                        nombre_cliente = autocomplete.getText().toString();
                    }

                    private void guardarCodFamiliar() {
                        // TODO Auto-generated method stub
                        nombre_familiar = clienteEntity.getNomcli().toString();
                        cod_familiar = dbclass
                                .obtenerCodigoCliente(nombre_familiar);
                    }
                }, 1000);

            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialogo_codFamiliar.dismiss();
            }
        });

        dialogo_codFamiliar.show();

    }

    public class AdapterDialogCodFamiliar extends ArrayAdapter<DBClientes> {

        Activity context;
        ArrayList<DBClientes> datos;

        public AdapterDialogCodFamiliar(Activity context,
                                        ArrayList<DBClientes> al_clientes) {

            super(context, android.R.layout.simple_list_item_single_choice);
            this.context = context;
            this.datos = al_clientes;
        }

        @Override
        public int getCount() {
            // getCount() represents how many items are in the list
            return datos.size();
        }

        @Override
        public DBClientes getItem(int position) {
            return datos.get(position);
        }

        @Override
        public View getView(int position, View convertview, ViewGroup parent) {

            View item = convertview;
            ViewHolder holder;

            if (item == null) {
                LayoutInflater inflater = context.getLayoutInflater();
                item = inflater.inflate(
                        android.R.layout.simple_list_item_single_choice, null);

                holder = new ViewHolder();

                holder.des_cliente = (TextView) item
                        .findViewById(android.R.id.text1);

                item.setTag(holder);

            } else {

                holder = (ViewHolder) item.getTag();

            }

            holder.des_cliente.setText(datos.get(position).getNomcli());

            return item;
        }

        public class ViewHolder {
            TextView des_cliente;
        }
    }

    private void crear_dialogo_post_envio(String titulo, String mensaje,
                                          int icon) {

        AlertDialog.Builder dialogo2 = new AlertDialog.Builder(CH_DevolucionesActivity.this);
        dialogo2.setTitle(titulo);
        dialogo2.setMessage(mensaje);
        dialogo2.setIcon(icon);
        dialogo2.setCancelable(false);
        dialogo2.setPositiveButton("Aceptar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {

                        if (origen.equals("REPORTES")) {

                            finish();
                            Intent intent2 = new Intent(CH_DevolucionesActivity.this,ReportesActivity.class);
                            intent2.putExtra("ORIGEN", "PEDIDOS");
                            startActivity(intent2);

                        } else {
                            //crear_dialogo_otro_pedido();
                            finish();
                            Intent intent2 = new Intent(CH_DevolucionesActivity.this,ReportesActivity.class);
                            intent2.putExtra("ORIGEN", "PEDIDOS");
                            startActivity(intent2);
                        }
                    }
                });

        dialogo2.show();

    }

    private void crear_dialogo_otro_pedido() {

        AlertDialog.Builder alerta = new AlertDialog.Builder(
                CH_DevolucionesActivity.this);
        alerta.setCancelable(false);
        alerta.setMessage("Desea realizar otro pedido para este cliente?");

        alerta.setPositiveButton("Si", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

                finish();

                Intent i = new Intent(CH_DevolucionesActivity.this,
                        CH_DevolucionesActivity.class);
                i.putExtra("codven", codven);
                i.putExtra("NOMCLI", nomcli);
                i.putExtra("ORIGEN", "clientesac");
                i.putExtra("DIRECCION", item_direccion);
                startActivity(i);

            }
        });

        alerta.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                finish();

                Intent intent2 = new Intent(CH_DevolucionesActivity.this,ReportesActivity.class);
                intent2.putExtra("ORIGEN", "PEDIDOS");
                startActivity(intent2);

            }
        });

        alerta.show();

    }

    private void crear_dialogo_guardar_modificar(String mensaje) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Importante");
        builder.setMessage(mensaje);
        builder.setIcon(R.drawable.ic_alert);
        builder.setCancelable(true);

        builder.setPositiveButton("Enviar al servidor",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        new async_envio_pedido().execute();
                        //crear_dialogo_otro_pedido();
                    }
                });

        builder.setNegativeButton("Guardar Localmente",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {

                        if (origen.equals("REPORTES")) {

                            finish();
                            Intent intent2 = new Intent(CH_DevolucionesActivity.this,ReportesActivity.class);
                            intent2.putExtra("ORIGEN", "PEDIDOS");
                            startActivity(intent2);

                        } else {
                            finish();
                            Intent intent2 = new Intent(CH_DevolucionesActivity.this,ReportesActivity.class);
                            intent2.putExtra("ORIGEN", "PEDIDOS");
                            startActivity(intent2);
                            //crear_dialogo_otro_pedido();
                        }

                    }
                });
        builder.create().show();

    }

    public class ProductoAdapter extends ArrayAdapter<ItemProducto> {

        ItemProducto[] productos;
        String undMedidas[];
        Activity context;

        public ProductoAdapter(Activity context, ItemProducto[] productos) {
            // super(context, R.layout.prodact_list_item_prod, productos);
            super(context, R.layout.prodact_list_item_prod, productos);
            this.context = context;
            this.productos = productos;
        }

        public View getView(int position, View convertview, ViewGroup parent) {

            View item = convertview;
            ViewHolder holder;

            if (item == null) {
                LayoutInflater inflater = context.getLayoutInflater();
                item = inflater.inflate(R.layout.prodact_list_item_prod, null);

                holder = new ViewHolder();

                holder.descripcion = (TextView) item
                        .findViewById(R.id.prd_act_txtDescrp);
                holder.stock = (TextView) item
                        .findViewById(R.id.prd_act_txtStock);
                holder.stock2 = (TextView) item
                        .findViewById(R.id.producto_item_tvUnidad);
                holder.des1 = (TextView) item
                        .findViewById(R.id.producto_item_tvUnidad2);
                holder.des2 = (TextView) item
                        .findViewById(R.id.producto_item_tvUnidad1);
                holder.precio = (TextView) item
                        .findViewById(R.id.prd_act_txtPrecio);
                holder.codigo = (TextView) item
                        .findViewById(R.id.prd_act_txtCodigo);
                holder.codigoProveedor = (TextView) item
                        .findViewById(R.id.prd_act_txtCodigoProveedor);

                item.setTag(holder);

            } else {

                holder = (ViewHolder) item.getTag();

            }
            undMedidas = dbclass
                    .getUndmedidas(productos[position].getCodprod());
            holder.precio
                    .setText(""
                            + Math.round(productos[position].getPrecio() * 100)
                            / 100.0);
            holder.codigo.setText(productos[position].getCodprod());
            holder.descripcion.setText(productos[position].getDescripcion());
            holder.codigoProveedor.setText(productos[position]
                    .getCodProveedor());

            // double stock =
            // productos[position].getStock()/productos[position].getFact_conv();
            double stock = productos[position].getStock();

            if (stock == 0) {
                holder.stock.setTextColor(Color.parseColor("#FF6347"));
                // holder.stock.setText(Integer.toString(productos[position].getStock()));
                holder.stock.setText(stock + "");
                holder.stock2.setVisibility(View.INVISIBLE);
                holder.des1.setText(undMedidas[0]);
                holder.des2.setVisibility(View.INVISIBLE);
                item.setBackgroundResource(R.drawable.list_selector_no_stock);
            } else {

                holder.stock.setTextColor(Color.parseColor("#343434"));
                // holder.stock.setText(Integer.toString(productos[position].getStock()));
                if (undMedidas.length == 1) {
                    holder.stock.setText(stock + "");
                    holder.stock2.setVisibility(View.INVISIBLE);
                    holder.des1.setText(undMedidas[0]);
                    holder.des2.setVisibility(View.INVISIBLE);
                } else {
                    holder.stock.setText(""
                            + (redondear((stock)
                            / productos[position].getFact_conv())));
                    holder.stock2.setText(""
                            + ((stock) % productos[position].getFact_conv()));
                    holder.des1.setText(undMedidas[0]);
                    holder.des2.setText(undMedidas[1]);
                }
                item.setBackgroundResource(R.drawable.list_selector);
            }

            return item;
        }

        public class ViewHolder {
            TextView descripcion;
            TextView precio;
            TextView codigo, des1, des2;
            TextView stock, codigoProveedor, stock2;
        }

    }

    public ArrayList<Model_bonificacion> CastModelBonificacion(
            ItemProducto[] productos) {
        ArrayList<Model_bonificacion> listaBonif = new ArrayList<Model_bonificacion>();

        for (int i = 0; i < productos.length; i++) {
            Model_bonificacion bonificacion = new Model_bonificacion();
            bonificacion.setCodigo(productos[i].getCodprod());
            bonificacion.setDescripcion((productos[i].getDescripcion()));
            // bonificacion.setEntrada(entrada);

            bonificacion.setStock((int) productos[i].getStock());
            bonificacion.setPrecio(""
                    + Math.round(productos[i].getPrecio() * 100) / 100.0);
            bonificacion.setFactorConversion(productos[i].getFact_conv());
            listaBonif.add(bonificacion);
        }
        return listaBonif;
    }

    public BigDecimal redondear(double val) {
        String r = val + "";
        BigDecimal big = new BigDecimal(r);
        big = big.setScale(0, RoundingMode.HALF_DOWN);
        return big;
    }
    public void showCustomToast(String mensaje, String tipo) {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.toast_personalizado,
                (ViewGroup) findViewById(R.id.toast_personalizado));

        LinearLayout customToast = (LinearLayout) view.findViewById(R.id.toast_personalizado);
        TextView text = (TextView) view.findViewById(R.id.toast_text);

        text.setText(mensaje);

        switch (tipo) {
            case "done":
                customToast.setBackgroundResource(R.drawable.toast_done_container);

                break;
            case "warning":
                customToast
                        .setBackgroundResource(R.drawable.toast_warning_container);
                break;
            case "wrong":
                customToast.setBackgroundResource(R.drawable.toast_wrong_container);
                break;
            default:
                break;
        }
        text.setTextColor(getResources().getColor(R.color.white));

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 15);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public boolean isNumber(String cadena){
        if (cadena == null || cadena.isEmpty()) {
            return false;
        }
        int i = 0;
		/*
		if (cadena.charAt(0) == '-') {
			if (cadena.length() > 1) {
				i++;
			}else{
				return false;
			}
		}
		*/
        for (; i < cadena.length(); i++) {
            if (!Character.isDigit(cadena.charAt(i))) {
                return false;
            }
        }
        return true;
    }


}
