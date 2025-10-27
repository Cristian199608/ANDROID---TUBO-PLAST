package com.example.sm_tubo_plast.genesys.fuerza_ventas;

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
import android.location.Location;
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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.BEAN.Almacen;
import com.example.sm_tubo_plast.genesys.BEAN.FormaPago;
import com.example.sm_tubo_plast.genesys.BEAN.ItemProducto;
import com.example.sm_tubo_plast.genesys.BEAN.LugarEntrega;
import com.example.sm_tubo_plast.genesys.BEAN.Nro_Letras;
import com.example.sm_tubo_plast.genesys.BEAN.Obra;
import com.example.sm_tubo_plast.genesys.BEAN.PedidoCabeceraRecalcular;
import com.example.sm_tubo_plast.genesys.BEAN.Pedido_detalle2;
import com.example.sm_tubo_plast.genesys.BEAN.PromocionDetalleProducto;
import com.example.sm_tubo_plast.genesys.BEAN.RegistroGeneralMovil;
import com.example.sm_tubo_plast.genesys.BEAN.ResumenVentaTipoProducto;
import com.example.sm_tubo_plast.genesys.BEAN.Sucursal;
import com.example.sm_tubo_plast.genesys.BEAN.Transporte;
import com.example.sm_tubo_plast.genesys.BEAN.Turno;
import com.example.sm_tubo_plast.genesys.BEAN.Model_bonificacion;
import com.example.sm_tubo_plast.genesys.DAO.DAO_Cliente;
import com.example.sm_tubo_plast.genesys.DAO.DAO_Pedido;
import com.example.sm_tubo_plast.genesys.DAO.DAO_Pedido_detalle2;
import com.example.sm_tubo_plast.genesys.DAO.DAO_PromocionDetalle;
import com.example.sm_tubo_plast.genesys.DAO.DAO_PromocionDetalleProducto;
import com.example.sm_tubo_plast.genesys.DAO.DAO_RegistroBonificaciones;
import com.example.sm_tubo_plast.genesys.DAO.DAO_RegistrosGeneralesMovil;
import com.example.sm_tubo_plast.genesys.adapters.Adapter_Bonificacion_Colores;
import com.example.sm_tubo_plast.genesys.adapters.Adapter_Detalle_Entrega;
import com.example.sm_tubo_plast.genesys.adapters.Adapter_itemPedidoProducto;
import com.example.sm_tubo_plast.genesys.datatypes.DBClientes;
import com.example.sm_tubo_plast.genesys.datatypes.DBMotivo_noventa;
import com.example.sm_tubo_plast.genesys.datatypes.DBPedido_Cabecera;
import com.example.sm_tubo_plast.genesys.datatypes.DBPedido_Detalle;
import com.example.sm_tubo_plast.genesys.datatypes.DBSync_soap_manager;
import com.example.sm_tubo_plast.genesys.datatypes.DBTb_Promocion_Detalle;
import com.example.sm_tubo_plast.genesys.datatypes.DB_Detalle_Entrega;
import com.example.sm_tubo_plast.genesys.datatypes.DB_Pedido_Detalle_Promocion;
import com.example.sm_tubo_plast.genesys.datatypes.DB_PromocionDetalle;
import com.example.sm_tubo_plast.genesys.datatypes.DB_RegistroBonificaciones;
import com.example.sm_tubo_plast.genesys.datatypes.DBclasses;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.Dialog.DialogFragment_bonificaciones;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.Reportes.ReportesPedidosCotizacionYVisitaActivity;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.compartidoUtil.UtilCalcularPrecioProducto;
import com.example.sm_tubo_plast.genesys.hardware.LocationApiGoogle;
import com.example.sm_tubo_plast.genesys.hardware.Permiso_Adroid;
import com.example.sm_tubo_plast.genesys.hardware.RequestPermisoUbicacion;
import com.example.sm_tubo_plast.genesys.hardware.TaskCheckUbicacion;
import com.example.sm_tubo_plast.genesys.service.ConnectionDetector;
import com.example.sm_tubo_plast.genesys.session.SessionManager;
import com.example.sm_tubo_plast.genesys.util.Dialog.AlertViewSimpleConEdittext;
import com.example.sm_tubo_plast.genesys.util.GlobalFunctions;
import com.example.sm_tubo_plast.genesys.util.SharePrefencia.PreferenciaConfiguracion;
import com.example.sm_tubo_plast.genesys.util.SnackBar.UtilViewSnackBar;
import com.example.sm_tubo_plast.genesys.util.UtilViewMensaje;
import com.example.sm_tubo_plast.genesys.util.VARIABLES;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationRequest;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.stream.Stream;

import me.piruin.quickaction.ActionItem;
import me.piruin.quickaction.QuickAction;

@SuppressLint("LongLogTag")
public class PedidosActivity extends AppCompatActivity implements View.OnClickListener, DialogFragment_bonificaciones.DialogListener {

    public static final String TAG = "PedidosActivity";
    public static final String TIPO_PEDIDO 		= "PEDIDO";
    public static final String TIPO_COTIZACION 	= "COTIZACION";
    public static final String TIPO_DEVOLUCION 	= "DEVOLUCION";

    public String TIPO_REGISTRO = "PEDIDO";
    ArrayList<DB_PromocionDetalle> al_PromocionDetalle = new ArrayList<DB_PromocionDetalle>();



    private static final int Menu_Agregar = 1;
    private static final int Menu_Guardar = 2;
    private static final int Menu_Eliminar = 3;
    private static final int Menu_Cancelar = 4;
    private static final int Menu_Modificar = 5;
    private static final int Menu_Evaluar = 6;


    LocationApiGoogle locationApiGoogle;
    TaskCheckUbicacion taskCheckUbicacion;
    public static final int PERMISO_PARA_ACCEDER_A_LOCALIZACION = 1000;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final long UPDATE_INTERVAL = 3000, FASTEST_INTERVAL = 3000; // = 3 seconds

    //private LocationListener Loclistener;

    private boolean GUARDAR_ACTIVO = false;
    private boolean PUEDE_AGREGAR = true;
    private static final int Fecha_Dialog_ID = 1;
    private AlertDialog.Builder dialogo1;
    private String   numOc;
    double lat=0, lng=0;
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

    int variablemodificar=0;
    String Oc_numero;
    int itemBonificacion = 0;
    double Sum_monto_total = 0.0;
    TabHost tabs;
    DBclasses dbclass;
    DAO_RegistroBonificaciones DAOBonificaciones;
    DAO_Pedido DAOPedidoDetalle;
    DAO_Pedido_detalle2 dao_pedido_detalle2;
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
    ArrayList<ItemProducto> productos = new ArrayList<ItemProducto>();

    Calendar calendar = Calendar.getInstance();
    Button btn_agregar, btn_guardar, btn_eliminar;
    //private TextView txtTotal;
    //private TextView txtTotal_peso;
    private  TextView tvDescuentoBonificacion, tvTotalPagarSinIgv, tvTotalPedidoPagarConIgv;
    private ListView lstProductos;

    //private TextView txtPercepcionTotal;
    //private TextView txtTotalPedido;

    // private EditText edtFechaEntrega, edtObservacion,edtLimiteCredito;
    // private Spinner spnDireccion, spnFormaPago;

    String cod_familiar = "";
    String nombre_familiar, nombre_cliente;
    String pedidoAnterior;//Pedido anterior o version anterior en caso de cotizacion

    /* Cabecera Pedido -------------- */
    private AutoCompleteTextView autocomplete;
    private EditText edt_nroPedido, edt_nroOrdenCompra, edt_limiteCredito,edt_direccionFiscal, edt_fechaPedido,edt_observacion1NombreContacto, edt_observacion1Telefono, 
            edt_observacion2NombreTrasporte,edt_observacion2DireccionTransporte, edt_observacion2Proyecto, edt_observacion3, edt_observacion4,edt_docAdicional;
    private LinearLayout linear_obra;
    private RadioButton rButton_boleta,rButton_factura;
    private RadioButton rButtonSoles,rButtonDolares;

    private RadioButton rButtonDescuentoSi,rButtonDescuentoNo;
    private RadioGroup rGroup_tipoDocumento,  rGroup_moneda,rGroup_aplicaDescuento;
    private Spinner spn_prioridad, spn_sucursal, spn_puntoEntrega,spn_tipoDespacho, spn_obra, spn_transportista, spn_almacenDespacho,spn_condicionVenta,spn_turno, spn_numeroletra;
    private Spinner spn_despacho;
    private TextView tv_moneda,tvTipoCambio, tv_cantidadItems;
    private TextView tv_subTotal,tv_total,tv_totalCompleto,tv_IGV,tv_percepcion;
    private TextView tv_descuento,tv_descuentoPorcentaje,tvPrecioPorKilo;
    //Cotizacion
    private EditText edt_diasVigencia;
    private LinearLayout linear_diasVigencia;
    private LinearLayout linearLayoutNumeroLetras	;
    private LinearLayout linearLayoutObservacion3;

    private CheckBox chkBox_embalaje, chkBox_pedidoAnticipo;
    private Button btn_fechaPedido;

    ArrayList<RegistroGeneralMovil> prioridades;
    //ArrayList<RegistroGeneralMovil> sucursales; de clientes
    ArrayList<LugarEntrega> puntoEntregas;
    ArrayList<RegistroGeneralMovil> tipoDespachos;
    ArrayList<Obra> Obras;

    DAO_RegistrosGeneralesMovil DAO_registrosGeneralesMovil;
    DAO_Cliente DAO_cliente;

    private int year, month, day, hour, minute;
    static final int DATE_DIALOG_ID = 999;
    private DatePicker datePicker;

    public static String MONEDA_SOLES_IN			= "1";//PEN
    public static String MONEDA_DOLARES_IN			= "2";//USD


    private static final String MONEDA_NACIONAL 	= "N";
    private static final String MONEDA_DOLARES 		= "E";
    private static final String MONEDA_AMBOS 		= "A";
    private static final String APLICA_DESCUENTO	= "S";
    private static final String NO_APLICA_DESCUENTO	= "N";
    private static final String FACTURA				= "01";
    private static final String BOLETA				= "02";
    private static final String DESPACHO_INTERNO	= "I";
    private static final String DESPACHO_EXTERNO	= "E";

    ArrayList<RegistroGeneralMovil> listaPrioridades;
    ArrayList<RegistroGeneralMovil> listaTipoDespacho;

    ArrayList<Turno> listaTurnos;
    ArrayList<Almacen> listaAlmacenes;
    ArrayList<FormaPago> listaFormaPago;
    ArrayList<Nro_Letras> listaNumeroLetra;
    ArrayList<Sucursal> listaSucursales;

    ArrayList<LugarEntrega> listaLugarEntrega;
    ArrayList<Obra> listaObras;
    ArrayList<Transporte> listaTransportes;

    String tipoDocumento;
    String nroPedido,nroOrdenCompra,limiteCredito,direccion;
    String codigoPrioridad, codigoSucursal, codigoLugarEntrega, codigoTipoDespacho, codigoObra="";
    String flagEmbalaje, flagPedidoAnticipo;
    String codigoTransportista, codigoAlmacenDespacho, codigoMoneda, codigoCondicionVenta,codigoTurno, codigoLetraCondicionVenta;
    String flagDescuento;
    String fechaEntrega, horaEntrega, fechaEntregaCompleta;
    String observacion,observacion2="",observacion3="", observacion4="";
    String observacionDescuento="", observacionTipoProducto="";

    String docAdicional="";
    String flagMsPack  ="";
    String flagMsPackAux  ="";
    boolean cabeceraRegistrada = false;
    float valorIGV;
    String codigoUbigeo;
    //float descuentoGeneralPedido;
    //Cotizacion
    String diasVigencia;
    //variables creadas para android version 7.0 de solucion con QuickAction
    String VERSION_ANDROID="";
    private static final String VERSION7_A_MAS="VERSION7_A_MAS";
    private static final String VERSIO6_A_MENOS="VERSION7_A_MAS";
    private static final int ELIMINAR_PRODUCTO = 1;
    private static final int ELIMINAR_ITEM_PROMO = 2;
    private static final int EDITAR_COLOR_BONI = 3;
    private static final int EDITAR_CANTIDAD = 4;
    private static final int EDITAR_PRODUCTO = 7;
    private static final int EDITAR_ENTREGA = 5;
    private static final int EDITAR_ITEM_PROMO = 6;

    String CODPRO="";
    int positionLisView=0;
    //Fin

    DecimalFormat formaterMoneda = new DecimalFormat("#,##0.00");


    SharedPreferences preferencias_configuracion;


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
        setContentView(R.layout.activity_pedidos);

        dbclass = new DBclasses(getApplicationContext());
        DAOBonificaciones = new DAO_RegistroBonificaciones(getApplicationContext());
        DAOPedidoDetalle = new DAO_Pedido(getApplicationContext());
        dao_pedido_detalle2 = new DAO_Pedido_detalle2(this);
        DAOPromocionDetalle = new DAO_PromocionDetalle(getApplicationContext());
        DAO_registrosGeneralesMovil = new DAO_RegistrosGeneralesMovil(getApplicationContext());
        DAO_cliente = new DAO_Cliente(getApplicationContext());

        Bundle bundle = getIntent().getExtras();
        origen = bundle.getString("origen");

        nomcli = bundle.getString("nombreCliente");
        codcli = bundle.getString("codcli");
        codven = "" + bundle.getString("codigoVendedor");

        if (bundle.getString("tipoRegistro")==null) {
            Log.e(TAG, "tipoRegistro es null . pedido");
            TIPO_REGISTRO = TIPO_PEDIDO;
        }else{
            Log.e(TAG, "tipoRegistro ."+bundle.getString("tipoRegistro"));
            TIPO_REGISTRO = bundle.getString("tipoRegistro");
        }

        preferencias_configuracion =  getSharedPreferences("preferencias_configuracion", Context.MODE_PRIVATE);
        valorIGV = preferencias_configuracion.getFloat("valorIGV", 0.0f);
        //descuentoGeneralPedido = preferencias_configuracion.getFloat("descuentoGeneralPedido", 0.0f);

        /*
         * Cabecera Pedido -----------------------------------------------------------------------
         */

        autocomplete 		= (AutoCompleteTextView) findViewById(R.id.autocomplete);
        edt_nroPedido 		= (EditText) findViewById(R.id.edt_nroPedido);
        rGroup_tipoDocumento= (RadioGroup) findViewById(R.id.rGroup_tipoDocumento);
        rButton_boleta		= (RadioButton) findViewById(R.id.rButton_boleta);
        rButton_factura		= (RadioButton) findViewById(R.id.rButton_factura);
        edt_nroOrdenCompra 	= (EditText) findViewById(R.id.edt_nroOrdenCompra);
        edt_limiteCredito 	= (EditText) findViewById(R.id.edt_limiteCredito);
        edt_direccionFiscal = (EditText) findViewById(R.id.edt_direccionFiscal);
        spn_prioridad 		= (Spinner) findViewById(R.id.spn_prioridad);
        spn_despacho 		= (Spinner) findViewById(R.id.spn_despacho);
        spn_turno 			= (Spinner) findViewById(R.id.spn_turno_entrega);
        spn_sucursal 		= (Spinner) findViewById(R.id.spn_sucursal);
        spn_puntoEntrega 	= (Spinner) findViewById(R.id.spn_puntoEntrega);
        spn_tipoDespacho 	= (Spinner) findViewById(R.id.spn_tipoDespacho);
        spn_obra 			= (Spinner) findViewById(R.id.spn_obra);
        chkBox_embalaje 	= (CheckBox) findViewById(R.id.chkBox_embalaje);
        chkBox_pedidoAnticipo = (CheckBox) findViewById(R.id.chkBox_pedidoAnticipo);
        spn_transportista 	= (Spinner) findViewById(R.id.spn_transportista);
        spn_almacenDespacho = (Spinner) findViewById(R.id.spn_almacenDespacho);
        rGroup_moneda 		= (RadioGroup) findViewById(R.id.rGroup_moneda);
        spn_condicionVenta 	= (Spinner) findViewById(R.id.spn_condicionVenta);
        spn_numeroletra		= (Spinner) findViewById(R.id.spn_numeroletra);
        linearLayoutNumeroLetras	= (LinearLayout)findViewById(R.id.linearLayoutNumeroLetras);
        linearLayoutObservacion3	= (LinearLayout)findViewById(R.id.linearLayoutObservacion3);
        rGroup_aplicaDescuento = (RadioGroup) findViewById(R.id.rGroup_aplicaDescuento);
        btn_fechaPedido 	= (Button) findViewById(R.id.btn_fechaPedido);
        //btn_horaPedido 		= (Button) findViewById(R.id.btn_horaPedido);
        edt_fechaPedido 	= (EditText) findViewById(R.id.edt_fechaPedido);
        //edt_horaPedido 		= (EditText) findViewById(R.id.edt_horaPedido);
        edt_observacion1NombreContacto 	= (EditText) findViewById(R.id.edt_observacion1NombreContacto);
        edt_observacion1Telefono 	= (EditText) findViewById(R.id.edt_observacion1Telefono);
        edt_observacion2NombreTrasporte 	= (EditText) findViewById(R.id.edt_observacion2NombreTrasporte);
        edt_observacion2DireccionTransporte 	= (EditText) findViewById(R.id.edt_observacion2DireccionTransporte);
        edt_observacion2Proyecto 	= (EditText) findViewById(R.id.edt_observacion2Proyecto);
        edt_observacion3 	= (EditText) findViewById(R.id.edt_observacion3);
        edt_observacion4 	= (EditText) findViewById(R.id.edt_observacion4);
        rButtonSoles 		= (RadioButton)findViewById(R.id.rButton_soles);
        rButtonDolares 		= (RadioButton)findViewById(R.id.rButton_dolares);
        rButtonDescuentoSi 	= (RadioButton)findViewById(R.id.rButton_descuentoSi);
        rButtonDescuentoNo 	= (RadioButton)findViewById(R.id.rButton_descuentoNo);
        linear_obra			= (LinearLayout)findViewById(R.id.linear_obra);
        edt_docAdicional	= (EditText)findViewById(R.id.edt_docAdicional);
        edt_diasVigencia	= (EditText)findViewById(R.id.edt_diasVigencia);
        linear_diasVigencia = (LinearLayout)findViewById(R.id.linear_diasVigencia);
        /*---------------------------------------------------------------------------------------*/
        /* Detalle Pedido -----------------------------------------------------------------------*/
        tv_moneda		= (TextView) findViewById(R.id.tv_moneda);
        tvTipoCambio			= (TextView) findViewById(R.id.tvTipoCambio);
        tv_cantidadItems= (TextView) findViewById(R.id.tv_cantidadItems);
        tv_subTotal		= (TextView) findViewById(R.id.tv_subtotal);
        tv_total		= (TextView) findViewById(R.id.tv_total);
        tv_totalCompleto= (TextView) findViewById(R.id.tv_totalCompleto);
        tv_IGV			= (TextView) findViewById(R.id.tv_IGV);
        tv_percepcion 	= (TextView) findViewById(R.id.tv_serie);

        tv_descuento	= (TextView) findViewById(R.id.tv_descuento);
        tv_descuentoPorcentaje = (TextView) findViewById(R.id.tv_descuentoPorcentaje);
        tvPrecioPorKilo= (TextView) findViewById(R.id.tvPrecioPorKilo);
        /*---------------------------------------------------------------------------------------*/




        tvDescuentoBonificacion = (TextView) findViewById(R.id.tvDescuentoBonificacion);
        tvTotalPagarSinIgv =  findViewById(R.id.tvTotalPagarSinIgv);
        tvTotalPedidoPagarConIgv =  findViewById(R.id.tvTotalPedidoPagarConIgv);
        lstProductos = (ListView) findViewById(R.id.lstProductos);
        //txtTotal = (TextView) findViewById(R.id.pedidolyt_txtTotal);
        //txtTotal_peso = (TextView) findViewById(R.id.pedidolyt_txtTotal_peso);
        btn_agregar = (Button) findViewById(R.id.pedido_lyt_btnAgregar);


        //txtPercepcionTotal = (TextView) findViewById(R.id.pedidolyt_txtPercepcionTotal);
        //txtTotalPedido = (TextView) findViewById(R.id.pedidolyt_txtTotal_pedido);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        inicializarFormulario();


        rGroup_tipoDocumento.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rButton_boleta:
                        tipoDocumento = BOLETA;
                        break;
                    case R.id.rButton_factura:
                        tipoDocumento = FACTURA;
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "TIPO DOCUMENTO INCORRECTO", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        btn_fechaPedido.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Process to get Current Date
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(PedidosActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
                                int monthOfYear_true = monthOfYear +1 ;
                                String pickerMonth=String.valueOf(monthOfYear_true),pickerDay=String.valueOf(dayOfMonth);
                                if (dayOfMonth<10) {
                                    pickerDay = "0"+dayOfMonth;
                                }
                                if (monthOfYear_true<10) {
                                    pickerMonth = "0"+(monthOfYear_true);
                                }
                                edt_fechaPedido.setText(pickerDay+"/"+pickerMonth+"/"+year);
                                fechaEntrega = pickerDay+"/"+pickerMonth+"/"+year;
                            }
                        }, year, month, day+1);
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
	            TimePickerDialog tpd = new TimePickerDialog(PedidosActivity.this,
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

        spn_condicionVenta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
                //String desc = spn_condicionVenta.getSelectedItem().toString();
                if (listaFormaPago!=null) {
                    if (listaFormaPago.size()>0) {
                        final String codigo = listaFormaPago.get(spn_condicionVenta.getSelectedItemPosition()).getCodigoFormaPago();
                        String descripcion = listaFormaPago.get(spn_condicionVenta.getSelectedItemPosition()).getDescripcionFormaPago();
                        Log.d("PedidoActivity", "Codigo de cliente: "+codcli);
                        Log.d("PedidoActivity", "onItemSelected:CodigoFormaPago: "+codigo);
                        Log.d("PedidoActivity", "onItemSelected:CodigoFormaPago: "+descripcion);

                        String forma_pago="";

                        try {forma_pago = descripcion.substring(0, 5);}
                        catch (Exception e) {	Log.e(TAG, "Error, no se puedo obtener los caracteres. "+e.getMessage());}

                        if(forma_pago.equalsIgnoreCase("LETRA")){
                            linearLayoutNumeroLetras.setVisibility(View.VISIBLE);
                            linearLayoutObservacion3.setVisibility(View.VISIBLE);

                            SharedPreferences prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);

                            preferencias_configuracion = getSharedPreferences("preferencias_configuracion", Context.MODE_PRIVATE);

                            codven = new SessionManager(PedidosActivity.this).getCodigoVendedor();


                            final String url = prefs.getString("url", "0");
                            final String catalog = prefs.getString("catalog", "0");
                            final String userid = prefs.getString("userid", "0");
                            final String contrasenaid = prefs.getString("contrasenaid", "0");
                            String servicio = prefs.getString("servicio", "0");
                            boolean chk_principal_estado = prefs.getBoolean("servicio_principal_activo",
                                    true);
                            String cond_ven="";

                            final DBSync_soap_manager soap_manager = new DBSync_soap_manager(getApplicationContext());

                            AsyncTask.execute(new Runnable() {
                                @Override
                                public void run() {

                                    ConnectionDetector cd = new ConnectionDetector(getApplicationContext());

                                    if (cd.hasActiveInternetConnection(getApplicationContext())) {
                                        Log.i(TAG, "entrando a la sincronizacion d letras");

                                        try {
                                            Log.w("mira el codcli ::", codcli);
                                            Log.w("mira el condicion_venta ::", codigo);
                                            soap_manager.Sync_tabla_Nro_letras(codcli, codigo);

                                            runOnUiThread(new Runnable() {

                                                @Override
                                                public void run() {
                                                    if (variablemodificar == 0){

                                                        listaNumeroLetra = DAO_registrosGeneralesMovil.getNroLetras();
                                                        if (listaNumeroLetra.size()>0){
                                                            ArrayList<CharSequence> nrletra = new ArrayList<>();
                                                            for (int i = 0; i < listaNumeroLetra.size(); i++) {
                                                                nrletra.add(listaNumeroLetra.get(i).getDescripcionNroLetras());
                                                            }
                                                            ArrayAdapter<CharSequence> spin_adapter = new ArrayAdapter<CharSequence>(getApplicationContext(), R.layout.spinner_item,nrletra);
                                                            spin_adapter.setDropDownViewResource(R.layout.spinner_item);
                                                            spn_numeroletra.setAdapter(spin_adapter);
                                                        }else
                                                            listaNumeroLetra=null;

                                                    }else{
                                                    }
                                                }

                                            });

                                        } catch (Exception e) {
                                            Log.e(TAG, "No se pudo sincronizar las letras "+e.getMessage());
                                        }

                                    }else{
                                        Log.e(TAG, "No hay conexion a internet");
                                    }
                                }
                            });


                        }else{
                            dbclass.eliminarAll_Nroletras();
                            listaNumeroLetra=null;
                            observacion4="";
                            codigoLetraCondicionVenta="0";
                            linearLayoutNumeroLetras.setVisibility(View.GONE);
                            linearLayoutObservacion3.setVisibility(View.GONE);
                        }

                        if (DAO_registrosGeneralesMovil.getHabilitaDescuentoCondicion(codigo) == true) {
                            if (!origen.equals("REPORTES-MODIFICAR") && !origen.equals("REPORTES-PEDIDO")) {
                                rButtonDescuentoSi.setEnabled(true);
                                rButtonDescuentoNo.setEnabled(true);
                            }

                            //if (codigo.equals("FCAS") || codigo.equals("CEDJ")){

                        }else{
                            rButtonDescuentoSi.setEnabled(false);
                            rButtonDescuentoNo.setEnabled(false);
                            rButtonDescuentoSi.setChecked(false);
                            rButtonDescuentoNo.setChecked(true);
                        }
                    }
                }

				/*if (desc.indexOf("CONTADO") != -1 || desc.indexOf("Contado") != -1 || desc.indexOf("contado") != -1) {//la palabra obra esta dentro del desc
					rButtonDescuentoSi.setEnabled(true);
					rButtonDescuentoNo.setEnabled(true);
					rButtonDescuentoSi.setChecked(false);
				}else{
					rButtonDescuentoSi.setEnabled(false);
					rButtonDescuentoNo.setEnabled(false);
					rButtonDescuentoSi.setChecked(false);
					rButtonDescuentoNo.setChecked(true);
				}*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        spn_numeroletra.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
                //String desc = spn_condicionVenta.getSelectedItem().toString();

                if (listaNumeroLetra!=null) {
                    if (listaNumeroLetra.size()>0) {
                        final String codigoLetra = listaNumeroLetra.get(spn_numeroletra.getSelectedItemPosition()).getNumeroLetras();
                        codigoLetraCondicionVenta=codigoLetra;
                        Log.d("PedidoActivity", "onItemSelected:Numero_letra: "+codigoLetra);
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        spn_tipoDespacho.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
                String desc = spn_tipoDespacho.getSelectedItem().toString();
                String palabra = "obra";
                if (desc.indexOf(palabra) != -1 || desc.indexOf("Obra") != -1 || desc.indexOf("OBRA") != -1) {//la palabra obra esta dentro del desc
                    //linear_obra.setVisibility(View.VISIBLE);
                    //Toast.makeText(getApplicationContext(), "OBRA !", Toast.LENGTH_SHORT).show();
                    flagMsPackAux = "0";
                }else{
                    flagMsPackAux = flagMsPack;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        spn_sucursal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,int position, long arg3) {
                if (!origen.equals("REPORTES-PEDIDO")) {
                    if (listaSucursales!=null) {
                        if (!listaSucursales.isEmpty()) {
                            docAdicional = listaSucursales.get(position).getDocAdicional();
                            edt_docAdicional.setText(""+docAdicional);

                            if (variablemodificar == 0){
                                listaLugarEntrega = DAO_cliente.getPuntoEntrega(codcli, listaSucursales.get(position).getItem());
                                ArrayList<CharSequence> lugaresEntrega = new ArrayList<>();
                                for (int i = 0; i < listaLugarEntrega.size(); i++) {
                                    lugaresEntrega.add(listaLugarEntrega.get(i).getDireccionEntrega());
                                }
                                ArrayAdapter<CharSequence> spinner_adapter = new ArrayAdapter<CharSequence>(getApplicationContext(), R.layout.spinner_item,lugaresEntrega);
                                spinner_adapter.setDropDownViewResource(R.layout.spinner_item);
                                spn_puntoEntrega.setAdapter(spinner_adapter);
                                //Log.w("PedidosActivity", "adapter punto entrega setted");
                            }else{
                            }
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {}
        });

        spn_puntoEntrega.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
                //String desc = spn_condicionVenta.getSelectedItem().toString();

                if (listaLugarEntrega!=null) {
                    if (listaLugarEntrega.size()>0) {
                        final String lugarentrega = listaLugarEntrega.get(spn_puntoEntrega.getSelectedItemPosition()).getCodigoLugar();
                        codigoLetraCondicionVenta=lugarentrega;
                        Log.d("PedidoActivity", "onItemSelected:Lugar_entrega: "+lugarentrega);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        cd = new ConnectionDetector(this);

        Pre_StartUbicacionApiGoogle();

        //

        //
        String DIALOGO="";
        String FORMA1="CONTEXT_MENU";
        String FORMA2="QUICKACTION_VERTICAL";
        final QuickAction mQuickAction1 = new QuickAction(this);
        final QuickAction mQuickAction2 = new QuickAction(this);
        if (Build.VERSION.SDK_INT >= 24){
            VERSION_ANDROID=VERSION7_A_MAS;
            DIALOGO=FORMA2;
            ActionItem infoItemEDIT_ENTRA = new ActionItem(EDITAR_CANTIDAD, "EDITAR \n CANTIDA",R.drawable.icon_ch_edit2_32);
            ActionItem infoItemEDIT_PRODUC = new ActionItem(EDITAR_PRODUCTO, "EDITAR \n PRODUCTO",R.drawable.icon_ch_edit2_32);
            ActionItem infoItemDELET_PRO = new ActionItem(ELIMINAR_PRODUCTO, "ELIMINAR \n PRODUCTO",R.drawable.icon_ch_delete2_32);
            ActionItem infoItem_EDIT_ENTREGA = new ActionItem(EDITAR_ENTREGA, "EDITAR \n ENTREGA",R.drawable.fecha_32);

            ActionItem infoItem_EDITAR_COLOR_BONI = new ActionItem(EDITAR_COLOR_BONI, "BONIFICACION COLORES",R.drawable.icon_man_24dp);
            ActionItem infoItem_EDITAR_ITEM_PROMO = new ActionItem(EDITAR_ITEM_PROMO, "EDITAR CANTIDAD",R.drawable.icon_man_24dp);
            ActionItem infoItem_ELIMINAR_ITEM_PROMO = new ActionItem(ELIMINAR_ITEM_PROMO, "ELIMINAR PROMOCION",R.drawable.ic_anular);

            mQuickAction1.addActionItem(infoItemEDIT_ENTRA);
            mQuickAction1.addActionItem(infoItemEDIT_PRODUC);
            mQuickAction1.addActionItem(infoItemDELET_PRO);
            mQuickAction1.addActionItem(infoItem_EDIT_ENTREGA);

            mQuickAction2.addActionItem(infoItem_EDITAR_COLOR_BONI);
            mQuickAction2.addActionItem(infoItem_EDITAR_ITEM_PROMO);
            mQuickAction2.addActionItem(infoItem_ELIMINAR_ITEM_PROMO);
        } else{
            VERSION_ANDROID=VERSIO6_A_MENOS;
            DIALOGO=FORMA1;
        }

        mQuickAction1.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {

            @Override
            public void onItemClick(ActionItem item) {
                accion_segunId(item.getActionId(), CODPRO);
            }


        });

        mQuickAction2.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {

            @Override
            public void onItemClick(ActionItem item) {
                accion_segunId(item.getActionId(), CODPRO);

            }
        });

        autocomplete.setText(nomcli);
        fecha_configuracion = dbclass.getCambio("Fecha");

        if (origen.equals("REPORTES-PEDIDO")) {//Ver detalle
            variablemodificar = 1;
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


        }else if (origen.equals("REPORTES-MODIFICAR")) {//Modificar Pedido
            variablemodificar = 1;
            autocomplete.setText(nomcli);
            Oc_numero = bundle.getString("OC_NUMERO");
            edt_nroPedido.setText(Oc_numero);
            DBPedido_Cabecera ped_cab = dbclass.getRegistroPedidoCabecera(Oc_numero);
            codcli = ped_cab.getCod_cli();

            mostrarDatosCliente(codcli);
            cargarDatosCliente(ped_cab);
            cargarDatosDocumento(ped_cab);

            cabeceraRegistrada = true;
            GUARDAR_ACTIVO = true;
            DeshabilitarCampos(false);
            HabilitarCamposEditables();
            mostrarListaProductos("");
            if(DIALOGO.equals(FORMA1))
                registerForContextMenu(lstProductos);
            else{
                lstProductos.setOnItemClickListener( new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                        lstProductos.setOnItemClickListener(this);
                        positionLisView=position;
                        CODPRO= productos.get(positionLisView).getCodprod();
                        if(productos.get(positionLisView).getTipo().equals("V"))
                            mQuickAction1.show(view);
                        else mQuickAction2.show(view);
                    }

                });
            }
        } else if (origen.equals("CLIENTES_MAPA")) {
            autocomplete.setText(nomcli);
            // numOc = dbclass.obtenerNumOC(codven);
            numOc = dbclass.obtenerMaxNumOc(codven);
            edt_nroPedido.setText(codven + calcularSecuencia(numOc,fecha_configuracion));
            colocarFechaActual();
            mostrarDatosCliente(codcli);
            if(DIALOGO.equals(FORMA1))
                registerForContextMenu(lstProductos);
            else{
                lstProductos.setOnItemClickListener( new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                        lstProductos.setOnItemClickListener(this);
                        positionLisView=position;
                        CODPRO= productos.get(positionLisView).getCodprod();
                        if(productos.get(positionLisView).getTipo().equals("V"))
                            mQuickAction1.show(view);
                        else mQuickAction2.show(view);
                    }
                });
            }
        } else if (origen.equals("CLIENTES")) {
            autocomplete.setText(nomcli);

            //numOc = dbclass.obtenerOcxCliente(codcli);
            //edt_nroPedido.setText(numOc);
            numOc = dbclass.obtenerMaxNumOc(codven);
            edt_nroPedido.setText(codven + calcularSecuencia(numOc,fecha_configuracion));
            mostrarDatosCliente(codcli);
            autocomplete.setEnabled(false);

            if (dbclass.VerificarCtasXCobrar(codcli).size() > 0) {
                Log.w("CLIENTE CON DEUDA", codcli+ "al venir de ActivityCliente");
                crearDialogo_cuentaXcobrar();
            }
            if(DIALOGO.equals(FORMA1))
                registerForContextMenu(lstProductos);
            else{
				/*
				if(productos.size()>0){
					View view = lstProductos.getAdapter().getView(0, null, null);
					lstProductos.performItemClick(view, 0, 0);
				}		*/

                lstProductos.setOnItemClickListener( new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                        lstProductos.setOnItemClickListener(this);
                        positionLisView=position;
                        CODPRO= productos.get(positionLisView).getCodprod();
                        if(productos.get(positionLisView).getTipo().equals("V"))
                            mQuickAction1.show(view);
                        else mQuickAction2.show(view);
                    }
                });
            }
        }else {
            numOc = dbclass.obtenerMaxNumOc(codven);
            edt_nroPedido.setText(codven + calcularSecuencia(numOc,fecha_configuracion));
            colocarFechaActual();

            mostrarDatosCliente(codcli);

            if (dbclass.VerificarCtasXCobrar(codcli).size() > 0) {
                crearDialogo_cuentaXcobrar();
            }
            if(DIALOGO.equals(FORMA1))
                registerForContextMenu(lstProductos);
            else{
                lstProductos.setOnItemClickListener( new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

                        lstProductos.setOnItemClickListener(this);
                        positionLisView=position;
                        CODPRO= productos.get(positionLisView).getCodprod();
                        if(productos.get(positionLisView).getTipo().equals("V"))
                            mQuickAction1.show(view);
                        else mQuickAction2.show(view);
                    }
                });
            }
        }
        Oc_numero = edt_nroPedido.getText().toString();

        //Si el tipo registro no es cotizacion no se muestra el campo dias de Vigencia
        if (!TIPO_REGISTRO.equals(TIPO_COTIZACION)) {
            linear_diasVigencia.setVisibility(View.GONE);
        }

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


        // Variables necesarias para el tema de percepcion

        clienteTienePercepcion = dbclass.obtenerPercepcionCliente(codcli);
        clienteTienePercepcionEspecial = dbclass.obtenerPercepcionEspecialCliente(codcli);
        clienteValorPercepcion = dbclass.obtenerValorPercepcionCliente(codcli);

        Log.i("PedidosActivity ::::","Variables Percepcion\n tienePercepcion= "+ clienteTienePercepcion + "; tienePercepcionEsp= "+ clienteTienePercepcionEspecial+ "; valorPercepcion= " + clienteValorPercepcion);

        // promociones=dbclass.obtenerPromociones(codcli);
        clientes = new String[dbclass.getAllClient(codven).length];
        clientes = dbclass.getAllClient(codven);

        // ArrayAdapter<String> adaptador=new
        // ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,clientes);
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this,R.layout.autocomplete_textview, clientes);
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
            public void onItemClick(AdapterView<?> adap, View view,int pos, long id) {
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
        spec.setIndicator(getTabIndicador(""+TIPO_REGISTRO));
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
private  void llenarSpinnerDespacho(String valor){
    ArrayList<CharSequence> lista=new ArrayList<>();
    lista.add("Cliente recoje");
    lista.add("Cronogrma");
    lista.add("Despacho normal");
    lista.add("Envio por agencia");

    ArrayAdapter<CharSequence> spinner_adapter = new ArrayAdapter<CharSequence>(getApplicationContext(), R.layout.spinner_item,lista);
    spinner_adapter.setDropDownViewResource(R.layout.spinner_item);
    spn_despacho.setAdapter(spinner_adapter);

    for (int i=0; i<lista.size();i++){
        if(lista.get(i).toString().equals(valor)){
            spn_despacho.setSelection(i);
        }
    }
}
    private void HabilitarCamposEditables() {		;
        rGroup_tipoDocumento.setEnabled(true);
        rButton_boleta.setEnabled(true);
        rButton_factura.setEnabled(true);
        btn_fechaPedido.setEnabled(true);
        //btn_horaPedido.setEnabled(true);
        edt_nroOrdenCompra.setEnabled(true);
        spn_prioridad.setEnabled(true);
        spn_turno.setEnabled(true);
        spn_puntoEntrega.setEnabled(true);
        spn_tipoDespacho.setEnabled(true);
        spn_obra.setEnabled(true);
        chkBox_embalaje.setEnabled(true);
        chkBox_pedidoAnticipo.setEnabled(true);
        spn_transportista.setEnabled(true);
        edt_observacion1NombreContacto.setEnabled(true);
        edt_observacion1Telefono.setEnabled(true);
        edt_observacion2NombreTrasporte.setEnabled(true);
        edt_observacion2DireccionTransporte.setEnabled(true);
        edt_observacion2Proyecto.setEnabled(true);
        edt_observacion3.setEnabled(true);
        edt_observacion4.setEnabled(true);
    }

    private void DeshabilitarCampos(boolean flag){
        //No habilitar campos que no deben
        autocomplete.setEnabled(flag);
        edt_nroPedido.setEnabled(flag);
        rGroup_tipoDocumento.setEnabled(flag);
        rButton_boleta.setEnabled(flag);
        rButton_factura.setEnabled(flag);
        rGroup_moneda.setEnabled(flag);
        rButtonSoles.setEnabled(flag);
        rButtonDolares.setEnabled(flag);
        edt_nroOrdenCompra.setEnabled(flag);
        edt_limiteCredito.setEnabled(flag);
        edt_direccionFiscal.setEnabled(flag);
        spn_prioridad.setEnabled(flag);
        spn_turno.setEnabled(flag);
        spn_sucursal.setEnabled(flag);
        spn_puntoEntrega.setEnabled(flag);

        if(TIPO_REGISTRO.equals(TIPO_COTIZACION) && !flag) {//no deshabilitar cuando es cotizacion
            spn_tipoDespacho.setEnabled(true);
            spn_despacho.setEnabled(true);
            spn_obra.setEnabled(true);
            spn_condicionVenta.setEnabled(true);
        }

        chkBox_embalaje.setEnabled(flag);
        chkBox_pedidoAnticipo.setEnabled(flag);
        spn_transportista.setEnabled(flag);
        spn_almacenDespacho.setEnabled(flag);
        spn_numeroletra.setEnabled(flag);
        rGroup_aplicaDescuento.setEnabled(flag);
        btn_fechaPedido.setEnabled(flag);
        //btn_horaPedido.setEnabled(flag);
        edt_fechaPedido.setEnabled(flag);
        //edt_horaPedido.setEnabled(flag);
        edt_observacion1NombreContacto.setEnabled(flag);
        edt_observacion1Telefono.setEnabled(flag);
        edt_observacion2NombreTrasporte.setEnabled(flag);
        edt_observacion2DireccionTransporte.setEnabled(flag);
        edt_observacion2Proyecto.setEnabled(flag);
        edt_observacion3.setEnabled(flag);
        rButtonDescuentoSi.setEnabled(flag);
        rButtonDescuentoNo.setEnabled(flag);
        linear_obra.setEnabled(flag);
        edt_docAdicional.setEnabled(flag);
        edt_diasVigencia.setEnabled(flag);
        edt_observacion4.setEnabled(flag);
    }

    private void inicializarFormulario() {
        rButtonDescuentoSi.setEnabled(false);
        rButtonDescuentoNo.setEnabled(false);

        listaPrioridades = DAO_registrosGeneralesMovil.getPrioridades();
        ArrayList<CharSequence> prioridades = new ArrayList<CharSequence>();
        for (int i = 0; i < listaPrioridades.size(); i++) {
            prioridades.add(listaPrioridades.get(i).getValor());
        }
        ArrayAdapter<CharSequence> spinner_adapter = new ArrayAdapter<CharSequence>(getApplicationContext(), R.layout.spinner_item,prioridades);
        spinner_adapter.setDropDownViewResource(R.layout.spinner_item);
        spn_prioridad.setAdapter(spinner_adapter);


        listaTipoDespacho = DAO_registrosGeneralesMovil.getTipoDespacho();
        ArrayList<CharSequence> tiposDespacho = new ArrayList<CharSequence>();
        for (int i = 0; i < listaTipoDespacho.size(); i++) {
            tiposDespacho.add(listaTipoDespacho.get(i).getValor());
        }
        spinner_adapter = new ArrayAdapter<CharSequence>(getApplicationContext(), R.layout.spinner_item,tiposDespacho);
        spinner_adapter.setDropDownViewResource(R.layout.spinner_item);
        spn_tipoDespacho.setAdapter(spinner_adapter);

        //LISTA TURNOO
        listaTurnos = DAO_registrosGeneralesMovil.getTurnos();
        ArrayList<CharSequence> codTurno = new ArrayList<CharSequence>();
        for (int i = 0; i < listaTurnos.size(); i++) {
            codTurno.add(listaTurnos.get(i).getTurno());
        }
        spinner_adapter = new ArrayAdapter<CharSequence>(getApplicationContext(), R.layout.spinner_item,codTurno);
        spinner_adapter.setDropDownViewResource(R.layout.spinner_item);
        spn_turno.setAdapter(spinner_adapter);
        //FIN

        listaAlmacenes = DAO_registrosGeneralesMovil.getAlmacenes();
        ArrayList<CharSequence> almacenes = new ArrayList<>();
        for (int i = 0; i < listaAlmacenes.size(); i++) {
            almacenes.add(listaAlmacenes.get(i).getDescripcion());
        }
        spinner_adapter = new ArrayAdapter<CharSequence>(getApplicationContext(), R.layout.spinner_item,almacenes);
        spinner_adapter.setDropDownViewResource(R.layout.spinner_item);
        spn_almacenDespacho.setAdapter(spinner_adapter);
        for (int i = 0; i < listaAlmacenes.size(); i++) {
            String descripcion = listaAlmacenes.get(i).getDescripcion();
            if (descripcion.indexOf("HUACHIPA")!=-1 || descripcion.indexOf("Huachipa")!=-1) {
                spn_almacenDespacho.setSelection(i);
            }
        }

//        try {
//            ArrayList<String> monedas = DAO_registrosGeneralesMovil.getMoneda();
//            MONEDA_SOLES_IN = monedas.get(0);
//            MONEDA_DOLARES_IN = monedas.get(1);
//        } catch (Exception e) {
//            GlobalFunctions.showCustomToast(PedidosActivity.this, "Sincronice monedas(Vendedores) correctamente", GlobalFunctions.TOAST_ERROR,GlobalFunctions.POSICION_BOTTOM);
//            e.printStackTrace();
//        }
    }

    public void mostrarDatosCliente(String codigoCliente) {
        Log.d("PedidosActivity", "mostrarDatosCliente");
        if (!codigoCliente.equals("") || codigoCliente.length() != 0) {

            tipoDocumento = DAO_cliente.getComprobante(codigoCliente);

            if (tipoDocumento.equals(FACTURA)) {
                rButton_factura.setChecked(true);
            }else{
                rButton_boleta.setChecked(true);
            }

            String direccionFiscal = DAO_cliente.getDireccionFiscal(codigoCliente);
            edt_direccionFiscal.setText(direccionFiscal);

            String limiteCredito = DAO_cliente.getLimiteCredito(codigoCliente);
            edt_limiteCredito.setText(formaterMoneda.format(Double.parseDouble(limiteCredito)));

            listaSucursales = DAO_cliente.getSucursales(codigoCliente);
            ArrayList<CharSequence> sucursales = new ArrayList<>();
            for (int i = 0; i < listaSucursales.size(); i++) {
                sucursales.add(listaSucursales.get(i).getItem() + " - " + listaSucursales.get(i).getDescripcionCorta());
            }
            ArrayAdapter<CharSequence> spinner_adapter = new ArrayAdapter<CharSequence>(getApplicationContext(), R.layout.spinner_item,sucursales);
            spinner_adapter.setDropDownViewResource(R.layout.spinner_item);
            spn_sucursal.setAdapter(spinner_adapter);

            llenarSpinnerDespacho("");

            Log.e(TAG,"listaSucursales size:"+ listaSucursales.size());
            Log.e(TAG, "spn_sucursal size:"+spn_sucursal.getCount());

            if (!listaSucursales.isEmpty()) {
                listaLugarEntrega = DAO_cliente.getPuntoEntrega(codigoCliente, listaSucursales.get(spn_sucursal.getSelectedItemPosition()).getItem());
                ArrayList<CharSequence> lugaresEntrega = new ArrayList<>();
                for (int i = 0; i < listaLugarEntrega.size(); i++) {
                    lugaresEntrega.add(listaLugarEntrega.get(i).getDireccionEntrega());
                }
                spinner_adapter = new ArrayAdapter<CharSequence>(getApplicationContext(), R.layout.spinner_item,lugaresEntrega);
                spinner_adapter.setDropDownViewResource(R.layout.spinner_item);
                spn_puntoEntrega.setAdapter(spinner_adapter);

				/*for (int i = 0; i < listaLugarEntrega.size(); i++) {
					String desentrega = listaLugarEntrega.get(i).getDireccionEntrega();
					if (desentrega.indexOf("CONTADO")!=-1 || desentrega.indexOf("Contado")!=-1) {
						spn_puntoEntrega.setSelection(i);
					}
				}*/
            }else{
                GlobalFunctions.showCustomToast(PedidosActivity.this, "Sin sucursales", GlobalFunctions.TOAST_WARNING);
            }

            listaObras = DAO_cliente.getObras(codigoCliente);
            ArrayList<CharSequence> obras = new ArrayList<>();
            for (int i = 0; i < listaObras.size(); i++) {
                obras.add(listaObras.get(i).getObra());
            }
            spinner_adapter = new ArrayAdapter<CharSequence>(getApplicationContext(), R.layout.spinner_item,obras);
            spinner_adapter.setDropDownViewResource(R.layout.spinner_item);
            spn_obra.setAdapter(spinner_adapter);
            //spn_obra.setVisibility(View.GONE);

            listaTransportes = DAO_cliente.getTransportes(codigoCliente);
            ArrayList<CharSequence> transportes = new ArrayList<>();
            for (int i = 0; i < listaTransportes.size(); i++) {
                transportes.add(listaTransportes.get(i).getDescripcion());
            }
            spinner_adapter = new ArrayAdapter<CharSequence>(getApplicationContext(), R.layout.spinner_item,transportes);
            spinner_adapter.setDropDownViewResource(R.layout.spinner_item);
            spn_transportista.setAdapter(spinner_adapter);


            listaFormaPago = DAO_registrosGeneralesMovil.getCondicionVenta(codigoCliente);
            ArrayList<CharSequence> formasPago = new ArrayList<>();
            for (int i = 0; i < listaFormaPago.size(); i++) {
                formasPago.add(listaFormaPago.get(i).getDescripcionFormaPago());
            }
            spinner_adapter = new ArrayAdapter<CharSequence>(getApplicationContext(), R.layout.spinner_item,formasPago);
            spinner_adapter.setDropDownViewResource(R.layout.spinner_item);
            spn_condicionVenta.setAdapter(spinner_adapter);

            for (int i = 0; i < listaFormaPago.size(); i++) {
                String descripcion = listaFormaPago.get(i).getDescripcionFormaPago();
                if (descripcion.indexOf("CONTADO")!=-1 || descripcion.indexOf("Contado")!=-1) {
                    spn_condicionVenta.setSelection(i);
                }
            }

            listaNumeroLetra = DAO_registrosGeneralesMovil.getNroLetras();
            ArrayList<CharSequence> numeroletra = new ArrayList<>();
            for (int i = 0; i < listaNumeroLetra.size(); i++) {
                numeroletra.add(listaNumeroLetra.get(i).getDescripcionNroLetras());
            }
            spinner_adapter = new ArrayAdapter<CharSequence>(getApplicationContext(),R.layout.spinner_item,numeroletra);
            spinner_adapter.setDropDownViewResource(R.layout.spinner_item);
            spn_numeroletra.setAdapter(spinner_adapter);

			/*for (int i = 0; i < listaNumeroLetra.size(); i++) {
				String desnrletra = listaNumeroLetra.get(i).getDescripcionNroLetras();
				if (desnrletra.indexOf("1 letra")!=-1 || desnrletra.indexOf("1 LETRA")!=-1) {
					spn_numeroletra.setSelection(i);
				}
			}*/

            //ArrayAdapter<CharSequence> spin_adapter = new ArrayAdapter<CharSequence>(getApplicationContext(), R.layout.spinner_item,nrletra);
            //spin_adapter.setDropDownViewResource(R.layout.spinner_item);
            //spn_numeroletra.setAdapter(spin_adapter);


            String monedaDocumento = DAO_cliente.getMonedaDocumento(codigoCliente);

            //rButtonSoles.setClickable(false);
            rButtonSoles.setEnabled(false);
            rButtonDolares.setEnabled(false);

            if (monedaDocumento.equals(MONEDA_AMBOS)) {
                rButtonSoles.setEnabled(true);
                rButtonDolares.setEnabled(true);

                rGroup_moneda.check(R.id.rButton_dolares);
            }else if (monedaDocumento.equals(MONEDA_NACIONAL)) {
                rGroup_moneda.check(R.id.rButton_soles);
            }else if (monedaDocumento.equals(MONEDA_DOLARES)){
                rGroup_moneda.check(R.id.rButton_dolares);
            }else{
                Toast.makeText(getApplicationContext(), "Moneda documento no especificada", Toast.LENGTH_SHORT).show();
            }

            //El flag de masterpack en el cliente ya no será usado, por lo tanto el flag siempre estara activo
            //flagMsPack = DAO_cliente.getFlagMsPack(codigoCliente);
            flagMsPack = "1";
        }

    }
    public void cargarDatosDocumento(DBPedido_Cabecera cabecera){
        if (cabecera.getTipoRegistro().equals(TIPO_COTIZACION)) {
            setTitle("Nueva versión de cotización");
            diasVigencia = cabecera.getDiasVigencia();
            edt_diasVigencia.setText(diasVigencia);
            pedidoAnterior = cabecera.getOc_numero();
        }else if (cabecera.getTipoRegistro().equals(TIPO_DEVOLUCION)) {

        }else{

        }
    }

    public void cargarDatosCliente(DBPedido_Cabecera item){
        String tipoDoc = item.getTipoDocumento();
        if (tipoDoc.equals(BOLETA)) {
            rButton_boleta.setChecked(true);
        }else{
            rButton_factura.setChecked(true);
        }

        Log.d("PedidosActivity", "cargarDatosCliente");
        edt_nroOrdenCompra.setText(item.getNumeroOrdenCompra());

        String codigoPrio = item.getCodigoPrioridad();
        for (int i = 0; i < listaPrioridades.size(); i++) {
            if (listaPrioridades.get(i).getCodValor().equals(codigoPrio)) {
                spn_prioridad.setSelection(i);
            }
        }

        String codTurno = item.getCodTurno();
        for (int i = 0; i < listaTurnos.size(); i++) {
            if (listaTurnos.get(i).getCodTurno().equals(codTurno)) {
                spn_turno.setSelection(i);
            }
        }

        String codigoSucur = item.getCodigoSucursal();
        for (int i = 0; i < listaSucursales.size(); i++) {
            if (listaSucursales.get(i).getItem().equals(codigoSucur)) {
                spn_sucursal.setSelection(i);
            }
        }

        String codigoPuntoEn = item.getCodigoPuntoEntrega();
        Log.w("PedidoAcivity", "codigoPuntoEn:"+codigoPuntoEn);
        for (int p = 0; p < listaLugarEntrega.size(); p++) {
            if (listaLugarEntrega.get(p).getCodigoLugar().equals(codigoPuntoEn)) {
                spn_puntoEntrega.setSelection(p);
                Log.w("PedidoAcivity", "codigoPuntoEn 2:"+codigoPuntoEn);
            }
        }

        String codigoTipoDesp = item.getCodigoTipoDespacho();
        for (int i = 0; i < listaTipoDespacho.size(); i++) {
            if (listaTipoDespacho.get(i).getCodValor().equals(codigoTipoDesp)) {
                spn_tipoDespacho.setSelection(i);
            }
        }

        String codigoObr = item.getCodigoObra();
        for (int i = 0; i < listaObras.size(); i++) {
            if (listaObras.get(i).getCodigoObra().equals(codigoObr)) {
                spn_obra.setSelection(i);
            }
        }

        String flagDespacho = item.getFlagDespacho();
        llenarSpinnerDespacho(flagDespacho);

        String flagEmbal = item.getFlagEmbalaje();
        if (flagEmbal.equals("1")) {
            chkBox_embalaje.setChecked(true);
        }else{
            chkBox_embalaje.setChecked(false);
        }


        String flagPedidoAnti = item.getFlagPedido_Anticipo();
        if (flagPedidoAnti.equals("1")) {
            chkBox_pedidoAnticipo.setChecked(true);
        }else{
            chkBox_pedidoAnticipo.setChecked(false);
        }

        String codigoTranspor = item.getCodigoTransportista();
        for (int i = 0; i < listaTransportes.size(); i++) {
            if (listaTransportes.get(i).getCodigoTransporte().equals(codigoTranspor)) {
                spn_transportista.setSelection(i);
            }
        }

        String codigoAlmacenD = item.getCodigoAlmacen();
        Log.d("ALMACEN RECUPERADOOO", ""+codigoAlmacenD);
        for (int i = 0; i < listaAlmacenes.size(); i++) {
            if (listaAlmacenes.get(i).getCodigoAlmacen().equals(codigoAlmacenD)) {
                spn_almacenDespacho.setSelection(i);
            }
        }

        String moneda = item.getMoneda();
        if (moneda.equals(MONEDA_DOLARES_IN)) {
            rButtonDolares.setChecked(true);
        }else{
            rButtonSoles.setChecked(true);
        }

        String codigoCondicionV = item.getCond_pago();
        for (int i = 0; i < listaFormaPago.size(); i++) {
            if (listaFormaPago.get(i).getCodigoFormaPago().equals(codigoCondicionV)) {
                spn_condicionVenta.setSelection(i);
            }
        }

        String codigo_letra = item.getNroletra();
        Log.i(TAG, "valor codigo_letra "+codigo_letra);
        for (int i = 0; i < listaNumeroLetra.size(); i++) {
            Log.i(TAG, "valor codigo_letra "+codigo_letra+"::: "+listaNumeroLetra.get(i).getNumeroLetras().toString());
            if (listaNumeroLetra.get(i).getNumeroLetras().equals(codigo_letra)) {
                spn_numeroletra.setSelection(i);
            }
        }

        String flagDescuent = item.getFlagDescuento();
        if (flagDescuent.equals(APLICA_DESCUENTO)) {
            rGroup_aplicaDescuento.check(R.id.rButton_descuentoSi);
        }else{
            rGroup_aplicaDescuento.check(R.id.rButton_descuentoNo);
        }

        String fechaEnterg = item.getFecha_mxe();
        edt_fechaPedido.setText(""+fechaEnterg.substring(0, 10));
        //edt_horaPedido.setText(""+fechaEnterg.substring(11, 16));

        ArrayList<String> observacion1=VARIABLES.GetListString(item.getObservacion(), 2);
        edt_observacion1NombreContacto.setText(observacion1.get(0));
        edt_observacion1Telefono.setText(observacion1.get(1));

        ArrayList<String> observacion2=VARIABLES. GetListString(item.getObservacion2(), 3);
        edt_observacion2NombreTrasporte.setText(observacion2.get(0));
        edt_observacion2DireccionTransporte.setText(observacion2.get(1));
        edt_observacion2Proyecto.setText(observacion2.get(2));

        edt_observacion3.setText(item.getObservacion3());
        edt_observacion4.setText(item.getObservacion4());

        if (item.getTipoRegistro().equals(TIPO_COTIZACION)) {
            diasVigencia = item.getDiasVigencia();
            edt_diasVigencia.setText(diasVigencia);
        }
    }


    // Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if (origen.equals("REPORTES")) {
            menu.add(Menu.NONE, Menu_Agregar, Menu.NONE, "MenuAgregar").setIcon(R.drawable.ic_carrito_white_24).setTitle("+Agregar").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menu.add(Menu.NONE, Menu_Modificar, Menu.NONE, "Modificar").setIcon(R.drawable.icon_ch_edit2_32).setTitle("Modificar");
        }else if (origen.equals("REPORTES-MODIFICAR")) {
            menu.add(Menu.NONE, Menu_Agregar, Menu.NONE, "MenuAgregar").setIcon(R.drawable.ic_carrito_white_24).setTitle("+Agregar").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menu.add(Menu.NONE, Menu_Modificar, Menu.NONE, "Modificar").setIcon(R.drawable.icon_ch_edit2_32).setTitle("Guardar Cambios");
            menu.add(Menu.NONE, Menu_Evaluar, Menu.NONE, "MenuEvaluar").setIcon(R.drawable.icon_ch_check2_32).setTitle("Evaluar");
        } else {
            menu.add(Menu.NONE, Menu_Agregar, Menu.NONE, "MenuAgregar").setIcon(R.drawable.ic_carrito_white_24).setTitle("+Agregar").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
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
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;

            case Menu_Cancelar:

                if (origen.equals("REPORTES")) {
                    dbclass.actualizarEstadoCabeceraPedido(Oc_numero, "G");
                    finish();
                } else {
                    // dbclass.cambiarEstadoEliminados(Oc_numero);
                    PedidosActivity.this.finish();
                    Intent intent2 = new Intent(PedidosActivity.this,  ReportesPedidosCotizacionYVisitaActivity.class);
                    intent2.putExtra("TIPO_VISTA", ReportesPedidosCotizacionYVisitaActivity.DATOS_PREVENTA);
                    intent2.putExtra("ORIGEN", "PEDIDOS");
                    startActivity(intent2);
                    Log.w("Menu Cancelar", "se cancelo");
                }

                return true;

            case Menu_Modificar:
                variablemodificar=0;
                GuardarFormularioCabecera();
                guardarCabeceraPedido();
                recalcularItemPedidoDetalle();
                if (TIPO_REGISTRO.equals(TIPO_COTIZACION)) {
                    crear_dialogo_guardar_modificar("Se guardará el pedido como una nueva versión");
                }else{
                    crear_dialogo_guardar_modificar("Se modificará el pedido");
                }
                Log.w("Menu Modificar", "se modificó");

                return true;

            case Menu_Agregar:
                GuardarFormularioCabecera();
                agregarProducto();
                Log.w("Agregar","Producto");
                return true;

            case Menu_Guardar:
                GuardarFormularioCabecera();
                guardarCabeceraPedido();
                dbclass.eliminar_item_promo_marcados(Oc_numero);
                recalcularItemPedidoDetalle();
                crear_dialogo_guardar_modificar("Se guardarán todos los datos");

                return true;
            case Menu_Evaluar:
                /*El descuento por familia puede darse por varias familias a la vez? */
                double valorPercepcionx = 0.0;
                if (clienteValorPercepcion != null) {
                    try {
                        valorPercepcionx = Double.parseDouble(clienteValorPercepcion);
                    } catch (Exception ex) {
                        Log.e("Pedidosctivity ::getDouble(CLIENTE_VALOR_PERCEPCION)::", ex.toString());
                        valorPercepcionx = 0.0;
                    }
                } else {
                    valorPercepcionx = 0.0;
                }

                ArrayList<DB_PromocionDetalle> promocionesPeso = DAOPromocionDetalle.getPromociones_xPeso(codcli, codven, "");
                try{
                    for (DB_PromocionDetalle promocionPeso : promocionesPeso) {
                        double peso = 0.0d;
                        if (productos != null) {
                            for (int i = 0; i < productos.size(); i++) {
                                if (!promocionPeso.getGrupo().equals("")) {
                                    if (promocionPeso.getGrupo().equals(productos.get(i).getGrupo())) {
                                        peso += productos.get(i).getPeso();
                                        Log.d("PedidosActivity", "Suma de Pesos por grupo: "+promocionPeso.getGrupo()+" ->"+peso);
                                    }
                                }else if (!promocionPeso.getFamilia().equals("")) {
                                    if (promocionPeso.getFamilia().equals(productos.get(i).getFamilia())) {
                                        peso += productos.get(i).getPeso();
                                        Log.d("PedidosActivity", "Suma de Pesos por familia: "+promocionPeso.getFamilia()+" ->"+peso);
                                    }
                                }else if (!promocionPeso.getSubfamilia().equals("")) {
                                    if (promocionPeso.getSubfamilia().equals(productos.get(i).getSubfamilia())) {
                                        peso += productos.get(i).getPeso();
                                        Log.d("PedidosActivity", "Suma de Pesos por subfamilia: "+promocionPeso.getSubfamilia()+" ->"+peso);
                                    }
                                }
                            }

                            if (peso >= promocionPeso.getCant_condicion()) {
                                Double descuentox,precioUnidadx,subtotalx,precioPercepcion;
                                for (int i = 0; i < productos.size(); i++) {
                                    if (!promocionPeso.getGrupo().equals("")) {
                                        if (promocionPeso.getGrupo().equals(productos.get(i).getGrupo())) {
                                            descuentox 		= Double.parseDouble(promocionPeso.getDescuento());
                                            precioUnidadx 	= GlobalFunctions.redondear_toDouble(productos.get(i).getPrecioLista() * (1-(descuentox / 100)));
                                            subtotalx 		= precioUnidadx * productos.get(i).getCantidad();
                                            productos.get(i).getAfecto();
                                            precioPercepcion= precioUnidadx * (1+valorIGV) * valorPercepcionx * productos.get(i).getCantidad();

                                            productos.get(i).setDescuento(GlobalFunctions.redondear_toDouble(descuentox));
                                            productos.get(i).setPrecio(precioUnidadx);
                                            productos.get(i).setSubtotal(GlobalFunctions.redondear_toDouble(subtotalx));
                                            productos.get(i).setPercepcion(GlobalFunctions.redondear_toDouble(precioPercepcion));
                                            dbclass.updatePedidoDetalle(Oc_numero,productos.get(i));
                                            Log.d("PedidosActivity", productos.get(i).getCodprod()+" Precio:"+precioUnidadx+" decuento:"+descuentox+" subtotal:"+subtotalx);
                                        }
                                    }else if (!promocionPeso.getFamilia().equals("")) {
                                        if (promocionPeso.getFamilia().equals(productos.get(i).getFamilia())) {
                                            descuentox = Double.parseDouble(promocionPeso.getDescuento());
                                            precioUnidadx = GlobalFunctions.redondear_toDouble(productos.get(i).getPrecioLista() * (1-(descuentox / 100)));
                                            subtotalx = precioUnidadx * productos.get(i).getCantidad();
                                            precioPercepcion= precioUnidadx * (1+valorIGV) * valorPercepcionx * productos.get(i).getCantidad();

                                            productos.get(i).setDescuento(GlobalFunctions.redondear_toDouble(descuentox));
                                            productos.get(i).setPrecio(precioUnidadx);
                                            productos.get(i).setSubtotal(GlobalFunctions.redondear_toDouble(subtotalx));
                                            productos.get(i).setPercepcion(GlobalFunctions.redondear_toDouble(precioPercepcion));
                                            dbclass.updatePedidoDetalle(Oc_numero,productos.get(i));
                                            Log.d("PedidosActivity", productos.get(i).getCodprod()+" Precio:"+precioUnidadx+" decuento:"+descuentox+" subtotal:"+subtotalx);
                                        }
                                    }else if (!promocionPeso.getSubfamilia().equals("")) {
                                        if (promocionPeso.getSubfamilia().equals(productos.get(i).getSubfamilia())) {
                                            descuentox = Double.parseDouble(promocionPeso.getDescuento());
                                            precioUnidadx = GlobalFunctions.redondear_toDouble(productos.get(i).getPrecioLista() * (1-(descuentox / 100)));
                                            subtotalx = precioUnidadx * productos.get(i).getCantidad();
                                            precioPercepcion= precioUnidadx * (1+valorIGV) * valorPercepcionx * productos.get(i).getCantidad();
                                            Log.d("PedidosActivity", "valorIGV:"+valorIGV+ " valorPercepcionx:" +valorPercepcionx);

                                            productos.get(i).setDescuento(GlobalFunctions.redondear_toDouble(descuentox));
                                            productos.get(i).setPrecio(precioUnidadx);
                                            productos.get(i).setSubtotal(GlobalFunctions.redondear_toDouble(subtotalx));
                                            productos.get(i).setPercepcion(GlobalFunctions.redondear_toDouble(precioPercepcion));
                                            dbclass.updatePedidoDetalle(Oc_numero,productos.get(i));
                                            Log.d("PedidosActivity", productos.get(i).getCodprod()+" Precio:"+precioUnidadx+" decuento:"+descuentox+" subtotal:"+subtotalx+" percepcion:"+precioPercepcion);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Gson gson = new Gson();
                    Log.w("PedidosActivity",""+gson.toJson(productos));
                    mostrarListaProductos("");
                    GlobalFunctions.showCustomToast(PedidosActivity.this, "Verificacion Completa", GlobalFunctions.TOAST_DONE, GlobalFunctions.POSICION_BOTTOM);
                }catch (Exception e) {
                    e.printStackTrace();
                }
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

    private  void recalcularItemPedidoDetalle(){
        //no recalcular. pues estan amarrado a promociones
        //dbclass.recalcularItemPedidoDetalle(Oc_numero);
    }
    public boolean camposValidos(){

        nroPedido 		= edt_nroPedido.getText().toString().trim();
        nroOrdenCompra 	= edt_nroOrdenCompra.getText().toString().trim();
        limiteCredito 	= edt_limiteCredito.getText().toString().trim();
        direccion 		= edt_direccionFiscal.getText().toString().trim();
        fechaEntrega	= edt_fechaPedido.getText().toString().trim();
        //horaEntrega		= edt_horaPedido.getText().toString().trim();
        diasVigencia	= edt_diasVigencia.getText().toString().trim();

        if (nroPedido.equals("") || nroPedido.length()==0) {
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
        if (limiteCredito.equals("") || limiteCredito.length()==0) {
            tabs.setCurrentTab(0);
            edt_limiteCredito.setError(Html.fromHtml("<font color='#424242'>Campo necesario</font>"));
            edt_limiteCredito.requestFocus();

            return false;
        }
        if (direccion.equals("") || direccion.length()==0) {
            tabs.setCurrentTab(0);
            edt_direccionFiscal.setError(Html.fromHtml("<font color='#424242'>Campo necesario</font>"));
            edt_direccionFiscal.requestFocus();
            return false;
        }
        if (fechaEntrega.equals("") || fechaEntrega.length()==0){
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

        //Validacion para Cotizacion
        if (TIPO_REGISTRO.equals(TIPO_COTIZACION)) {
            if (diasVigencia.equals("") || diasVigencia.length()==0) {
                edt_diasVigencia.setError(Html.fromHtml("<font color='#424242'>Campo necesario</font>"));
                edt_diasVigencia.requestFocus();
                return false;
            }
        }
        return true;
    }

    public void agregarProducto(){
        String rquest_accion=ProductoActivity.REQUEST_ACCION_AGREGAR_PRODUCTO;
        agregarOrModificarProducto(rquest_accion, new ItemProducto.DataEdit());
    }
    public void modificarProducto(ItemProducto __itemProd){
        ItemProducto.DataEdit itemProd= ItemProducto.DataEdit.getInstance(__itemProd);
        String rquest_accion=ProductoActivity.REQUEST_ACCION_MODIFICAR_PRODUCTO;
        agregarOrModificarProducto(rquest_accion,itemProd);
    }
    public void agregarOrModificarProducto(String request_accion_producto, ItemProducto.DataEdit productoModificar){
        if (autocomplete.getText().equals("") || codcli.equals("")) {
            autocomplete.setError(Html.fromHtml("<font color='#424242'>Cliente no Valido</font>"));
        }else{
            autocomplete.setEnabled(false);
            autocomplete.setFocusable(false);

            if (camposValidos()) {
                try {
                    if (cabeceraRegistrada == false) {
                        GuardarFormularioCabecera();
                        guardarCabeceraPedido();
                    }

                    if (rButtonSoles.isChecked()) {
                        codigoMoneda = MONEDA_SOLES_IN;
                    }else{
                        codigoMoneda = MONEDA_DOLARES_IN;
                    }
                    fechaEntregaCompleta = edt_fechaPedido.getText().toString();
                    Intent intent = new Intent(PedidosActivity.this,ProductoActivity.class);
                    intent.putExtra(ProductoActivity.REQUEST_ACCION_PRODUCTO_KEY,request_accion_producto);
                    intent.putExtra(ProductoActivity.REQUEST_DATA_PRODUCTO_KEY, productoModificar);
                    intent.putExtra("codcli", codcli);
                    intent.putExtra("codven", codven);
                    intent.putExtra("origen", pedido);
                    intent.putExtra("oc_numero", Oc_numero);

                    // Datos necesarios para el tema de percepcion
                    intent.putExtra("clienteTienePercepcion",clienteTienePercepcion);
                    intent.putExtra("clienteTienePercepcionEspecial",clienteTienePercepcionEspecial);
                    intent.putExtra("clienteValorPercepcion",clienteValorPercepcion);

                    intent.putExtra("codigoMoneda", codigoMoneda);
                    intent.putExtra("codigoCondicionVenta", codigoCondicionVenta);
                    intent.putExtra("codigoLetraCondicionVenta", codigoLetraCondicionVenta);
                    intent.putExtra("fechaEntrega", fechaEntregaCompleta);
                    intent.putExtra("flagDescuento", flagDescuento);
                    intent.putExtra("codigoSucursal", codigoSucursal);
                    intent.putExtra("codigoLugarEntrega", codigoLugarEntrega);
                    intent.putExtra("codigoTipoDespacho", codigoTipoDespacho);
                    intent.putExtra("codigoAlmacenDespacho", codigoAlmacenDespacho);
                    intent.putExtra("flagMsPack", flagMsPackAux);

                    Log.w("INTENT","cond venta "+codigoCondicionVenta+" flag dscto "+flagDescuento+" cod suc "+codigoSucursal+" codlugar "+codigoLugarEntrega+ " codAlm "+codigoAlmacenDespacho);
                    startActivityForResult(intent, 1);

                } catch (Exception e) {
                    Log.e(TAG, "Error "+e.getMessage());
                    GlobalFunctions.showCustomToast(this, "Verificar que esten cargados los datos y no haya campos vacios", GlobalFunctions.TOAST_WARNING,GlobalFunctions.POSICION_BOTTOM);
                }
            }
        }
    }



private void EnvalularMoneda(){
    rGroup_moneda.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId == rButtonDolares.getId()) {
                codigoMoneda = MONEDA_DOLARES_IN;
            }else{
                codigoMoneda = MONEDA_SOLES_IN;
            }
            Log.i(TAG, "rGroup_moneda.setOnCheckedChangeListener:: codigoMoneda: "+codigoMoneda);
        }
    });
    if (rButtonDolares.isChecked()) {
        codigoMoneda = MONEDA_DOLARES_IN;
    }else{
        codigoMoneda = MONEDA_SOLES_IN;
    }
}
    private void GuardarFormularioCabecera(){
        nroOrdenCompra 			= edt_nroOrdenCompra.getText().toString();
        codigoPrioridad 		= listaPrioridades.get(spn_prioridad.getSelectedItemPosition()).getCodValor();
        codigoSucursal 			= listaSucursales.get(spn_sucursal.getSelectedItemPosition()).getItem();
        if(listaLugarEntrega.size()>0){
            codigoLugarEntrega		= listaLugarEntrega.get(spn_puntoEntrega.getSelectedItemPosition()).getCodigoLugar();
            Log.w("PedidoActivity", "puntoEntrega::"+codigoLugarEntrega);
        }
        else {
            codigoLugarEntrega="00";
            Log.w("PedidoActivity", "puntoEntrega::"+codigoLugarEntrega);

        }
        //Toast.makeText(getApplicationContext(), "codigoLugarEntrega es "+codigoLugarEntrega, Toast.LENGTH_SHORT).show();
        codigoUbigeo			= dbclass.getCodigoUbigeo(codcli,codigoSucursal,codigoLugarEntrega);
        codigoTipoDespacho		= listaTipoDespacho.get(spn_tipoDespacho.getSelectedItemPosition()).getCodValor();

        codigoTransportista		= listaTransportes.get(spn_transportista.getSelectedItemPosition()).getCodigoTransporte();
        codigoAlmacenDespacho	= listaAlmacenes.get(spn_almacenDespacho.getSelectedItemPosition()).getCodigoAlmacen();
        codigoTurno				= listaTurnos.get(spn_turno.getSelectedItemPosition()).getCodTurno();
        observacion				= edt_observacion1NombreContacto.getText().toString()+VARIABLES.SEPARADOR_OBSERVACION+edt_observacion1Telefono.getText().toString();
        observacion2			= edt_observacion2NombreTrasporte.getText().toString()
                +VARIABLES.SEPARADOR_OBSERVACION+edt_observacion2DireccionTransporte.getText().toString()
                +VARIABLES.SEPARADOR_OBSERVACION+edt_observacion2Proyecto.getText().toString();
        observacion3			= edt_observacion3.getText().toString();

        Log.d("GuardarFormularioCabecera", "observacion2:"+observacion2);
        Log.d("GuardarFormularioCabecera", "observacion3:"+observacion3);


        if (listaObras != null) {
            if (!listaObras.isEmpty()) {
                if (linear_obra.getVisibility() == View.VISIBLE) {
                    codigoObra	= listaObras.get(spn_obra.getSelectedItemPosition()).getCodigoObra();
                }else{
                    codigoObra	= "";
                    Toast.makeText(getApplicationContext(), "Guardando obra vacia !", Toast.LENGTH_SHORT).show();
                }
            }
        }

        if (chkBox_embalaje.isChecked()) {
            flagEmbalaje = "1";
        }else{
            flagEmbalaje = "0";
        }

        if (chkBox_pedidoAnticipo.isChecked()) {
            flagPedidoAnticipo = "1";
        }else{
            flagPedidoAnticipo = "0";
        }

        EnvalularMoneda();

        if(rButtonDescuentoSi.isChecked()){
            flagDescuento = APLICA_DESCUENTO;
        }else{
            flagDescuento = NO_APLICA_DESCUENTO;
        }

        //fechaEntrega + " " + horaEntrega;
        fechaEntregaCompleta	= edt_fechaPedido.getText().toString();


        codigoCondicionVenta 	= listaFormaPago.get(spn_condicionVenta.getSelectedItemPosition()).getCodigoFormaPago();

        if(listaNumeroLetra==null){
            Log.i(TAG, "listaNumeroLetra es:: "+listaNumeroLetra);
            codigoLetraCondicionVenta 	= "0";
            observacion4			= "";
            Log.i("GuardarFormularioCabecera", "observacion4: "+observacion4+", Tamaño de lista NRO_LETRA es 0 ");
        }
        else {
            Log.i(TAG, "lista de numero letras no son vacios");
            codigoLetraCondicionVenta 	=listaNumeroLetra.get(spn_numeroletra.getSelectedItemPosition()).getNumeroLetras();
            observacion4			= edt_observacion4.getText().toString();
            Log.d("GuardarFormularioCabecera", "observacion4: "+observacion4);
            Log.i(TAG, "listaNumeroLetra "+listaNumeroLetra+", valor seleccionado es "+codigoLetraCondicionVenta+", Tamaño de lista NRO_LETRA "+listaNumeroLetra.size());
        }

        observacionDescuento	= "";
        observacionTipoProducto	= "";

        //Cotizacion
        diasVigencia	= edt_diasVigencia.getText().toString();

        Log.d(TAG, "GuardarFormularioCabecera:tipoDocumento -> "+tipoDocumento);
        Log.d(TAG, "GuardarFormularioCabecera:fechaEntrega  -> "+fechaEntregaCompleta);
        Log.d(TAG, "GuardarFormularioCabecera:CodigoTurno  -> "+codigoTurno);
        Log.d(TAG, "GuardarFormularioCabecera:nroOrdenCompra-> "+nroOrdenCompra);
        Log.d(TAG, "GuardarFormularioCabecera:codigoPrioridad-> "+codigoPrioridad);
        Log.d(TAG, "GuardarFormularioCabecera:codigoSucursal-> "+codigoSucursal);
        Log.d(TAG, "GuardarFormularioCabecera:puntoEntrega -> "+codigoLugarEntrega);
        Log.d(TAG, "GuardarFormularioCabecera:tipoDespacho -> "+codigoTipoDespacho);
        Log.d(TAG, "GuardarFormularioCabecera:codigoDespacho-> "+spn_despacho.getSelectedItem().toString());
        Log.d(TAG, "GuardarFormularioCabecera:flagEmbalaje-> "+flagEmbalaje);
        Log.d(TAG, "GuardarFormularioCabecera:flagAnticipo-> "+flagPedidoAnticipo);
        Log.d(TAG, "GuardarFormularioCabecera:codigoTranspor-> "+codigoTransportista);
        Log.d(TAG, "GuardarFormularioCabecera:codigoNroLetra-> "+codigoLetraCondicionVenta);
        Log.d(TAG, "GuardarFormularioCabecera:diasVigencia-> "+diasVigencia);
    }
    // Menu contextual listview
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

        ItemProducto prod = (ItemProducto) lstProductos.getAdapter().getItem(info.position);

        if (prod.getTipo().equals("V")) {
            menu.setHeaderTitle(prod.getDescripcion());
            inflater.inflate(R.menu.context_menu_pedido, menu);

        } else {
            menu.setHeaderTitle(prod.getDescripcion());
            inflater.inflate(R.menu.context_menu_pedido2, menu);
            String sec=DAOBonificaciones.getSecuenciaporEntrada(prod.getCodprod(), Oc_numero);
            if(!DAOBonificaciones.validarColorBono(sec))
                menu.removeItem(R.id.CntxMenu_Editar_color_bono);
        }
    }

//    @Override
//    public boolean onContextItemSelected(final MenuItem item) {
//
//        switch (item.getItemId()) {
//            case R.id.CntxMenu_Eliminar:
//                Log.d("PedidosActivity :onContextItemSelected:","CntxMenu_Eliminar");
//                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//                Log.v("", "info position: " + info.position);
//                ItemProducto prod = (ItemProducto) lstProductos.getAdapter().getItem(info.position);
//
//                /* Lista de bonificaciones afectadas por el producto eliminado */
//                ArrayList<DB_RegistroBonificaciones> bonificaciones_xEntrada = DAOBonificaciones.getRegistroBonificaciones_xEntrada(Oc_numero,prod.getCodprod(), prod.getItem());
//
//                for (DB_RegistroBonificaciones boni : bonificaciones_xEntrada) {
//                    if (DAOBonificaciones.esAcumulado(boni.getSecuenciaPromocion())) {
//                        int saldoAnterior1 = DAOBonificaciones.getSaldoAnterior(Oc_numero,boni.getSalida());
//                        String codigoRegistroAnterior = DAOBonificaciones.getCodigoRegistroAnterior(Oc_numero, boni.getSalida());
//
//                        int cantidadBonificadaAntes = DAOBonificaciones.getCantidadSalidaSecuencia(Oc_numero, boni.getSecuenciaPromocion(),boni.getItem(), boni.getAgrupado());
//                        ArrayList<String> registroAnterior = DAOBonificaciones.getRegistroAnterior(Oc_numero, boni.getSalida());
//                        DAOBonificaciones.Eliminar_RegistroBonificacion(Oc_numero,boni.getSecuenciaPromocion(), boni.getItem(),boni.getEntrada(), boni.getAgrupado(), prod.getItem());
//
//                        int cantidadEntradas = DAOBonificaciones.Recalcular_cantidadEntrada(Oc_numero,boni.getSecuenciaPromocion(),boni.getAcumulado());
//                        double montoEntradas = DAOBonificaciones.Recalcular_montoEntrada(Oc_numero,boni.getSecuenciaPromocion(),boni.getAcumulado());
//
//                        DB_PromocionDetalle dbpromo = DAOBonificaciones.getPromocionDetalle(boni.getSecuenciaPromocion(),boni.getEntrada(), boni.getAgrupado());
//                        Log.v("PedidosActivity :itemEliminar:","detalle promocion del item a eliminar");
//                        Log.v("PedidosActivity :itemEliminar:", "secuencia: "+ dbpromo.getSecuencia());
//                        Log.v("PedidosActivity :itemEliminar:", "entrada: "+ dbpromo.getEntrada());
//                        Log.v("PedidosActivity :itemEliminar:", "agrupado: "+ dbpromo.getAgrupado());
//                        Log.v("PedidosActivity :itemEliminar:", "salida: "+ dbpromo.getSalida());
//                        Log.v("PedidosActivity :itemEliminar:", "acumulado: "+ dbpromo.getAcumulado());
//
//                        ArrayList<DB_RegistroBonificaciones> regBonificaciones = DAOBonificaciones.getRegistroBonificaciones_xSecuencia(
//                                Oc_numero,
//                                boni.getSecuenciaPromocion(),
//                                boni.getItem(),
//                                boni.getAcumulado());
//
//                        listaEntradasCompuestas = new ArrayList<String[]>();
//                        cantidadesUsadas = new ArrayList<Integer>();
//                        montosUsados = new ArrayList<Double>();
//
//                        /*
//                         * --------------------- Obtener las cantidades y montos
//                         * utilizados ---------------------
//                         */
//                        for (DB_RegistroBonificaciones itemBoni : regBonificaciones) {
//                            int cantidadUnidadesMin = Convertir_toUnidadesMinimas(
//                                    itemBoni.getEntrada(),
//                                    itemBoni.getUnimedEntrada(),
//                                    itemBoni.getCantidadEntrada());
//
//                            entradasCompuestas = new String[9];
//                            entradasCompuestas[1] = String.valueOf(cantidadUnidadesMin);
//                            entradasCompuestas[2] = String.valueOf(itemBoni.getMontoEntrada());
//                            Log.e("", "cantidad entrada entradasCompuestas[1]:   "+ cantidadUnidadesMin);
//                            Log.e("", "   Monto entrada entradasCompuestas[2]:   "+ itemBoni.getMontoEntrada());
//                            listaEntradasCompuestas.add(entradasCompuestas);
//                        }
//
//                        int cantidadSalida = 0;
//                        Log.d("PedidosActivity:", "dbpromo.getTipo() -> "+dbpromo.getTipo());
//                        /** Se generan las cantidades y montos usados **/
//                        if (dbpromo.getTipo().equals("C")) {
//                            cantidadSalida = verificarTipoCondicionAcumulados_xCantidad(dbpromo, String.valueOf(montoEntradas),cantidadEntradas);
//                            Log.d("Cantidad","cantidad bonificacion obtenido por Acumulados xCantidad-> "+ cantidadSalida);
//                        } else {
//                            cantidadSalida = verificarTipoCondicionAcumulados_xMonto(dbpromo, String.valueOf(montoEntradas),cantidadEntradas);
//                            Log.d("Cantidad","cantidad bonificacion obtenido por Acumulados xMonto-> "+ cantidadSalida);
//                        }
//
//                        /*
//                         * --------------------- Actualizar las cantidades y montos
//                         * disponibles ---------------------
//                         */
//                        int ind = 0;
//                        for (DB_RegistroBonificaciones itemBoni : regBonificaciones) {
//                            /*
//                             * Si es el ultimo registro se le coloca la cantidad
//                             * Bonificada
//                             */
//                            String[] entradaCompuesta = listaEntradasCompuestas.get(ind);
//                            int cantidadDisponible = Integer.parseInt(entradaCompuesta[1]);
//                            double montoDisponible = Double.parseDouble(entradaCompuesta[2]);
//                            cantidadDisponible = cantidadDisponible- cantidadesUsadas.get(ind);
//                            montoDisponible = montoDisponible- montosUsados.get(ind);
//
//                            if ((ind + 1) == regBonificaciones.size()) {
//                                DAOBonificaciones.ActualizarRegistrosAcumulado(
//                                        Oc_numero,
//                                        itemBoni.getSecuenciaPromocion(),
//                                        itemBoni.getItem(), itemBoni.getEntrada(),
//                                        itemBoni.getAgrupado(), cantidadSalida,
//                                        cantidadDisponible, montoDisponible);
//                            } else {
//                                DAOBonificaciones.ActualizarRegistrosAcumulado(
//                                        Oc_numero,
//                                        itemBoni.getSecuenciaPromocion(),
//                                        itemBoni.getItem(), itemBoni.getEntrada(),
//                                        itemBoni.getAgrupado(), 0,
//                                        cantidadDisponible, montoDisponible);
//                            }
//                            ind++;
//                        }
//
//                        int cantidadBonificadaDespues = DAOBonificaciones.getCantidadSalidaSecuencia(Oc_numero, boni.getSecuenciaPromocion(),boni.getItem(), boni.getAgrupado());
//
//                        int cantidad = DAOBonificaciones.obtenerCantidadBonificacion(Oc_numero,"B"+dbpromo.getSalida());
//
//                        if (registroAnterior.size() > 0 && registroAnterior.get(1) != null) {
//                            //Si habia un registro completo con pendientes
//                            //obtener el ultimo registro, si no existe bajo las condiciones quiere decir q se ha eliminado por lo cual se debe designar al ulimo registrado como el nuevo ultimo registro
//
//                            String codigoRegistroActual = "";//Sera el codigo registro a modificar, si no se obtiene el ultimo completo se obtiene el ultimo bonificado para ser el nuevo completo
//
//                            ArrayList<String> ultimoRegistroCompleto = DAOBonificaciones.getRegistroAnterior(Oc_numero, boni.getSalida());//Si no se encuentra un ultimoRegistroCompleto quiere decir que ha sido borrado
//                            Log.e("PedidosActivity", "ultimoRegistroCompleto: " +ultimoRegistroCompleto);
//
//                            if (ultimoRegistroCompleto.size() > 0) {
//                                if (ultimoRegistroCompleto.get(1) != null) {
//                                    codigoRegistroActual = ultimoRegistroCompleto.get(0);
//                                }else{
//                                    codigoRegistroActual = DAOBonificaciones.getUltimoRegistroBonificacion(Oc_numero,boni.getSalida());
//                                }
//                            }else{
//                                codigoRegistroActual = DAOBonificaciones.getUltimoRegistroBonificacion(Oc_numero,boni.getSalida());
//                            }
//
//                            //int cantidadTotal = DAOBonificaciones.obtenerCantidadBonificacion(oc_numero,"B"+promocionDetalle.getSalida());
//                            int cantidadEntregada = Integer.parseInt(registroAnterior.get(2)) - (cantidadBonificadaAntes - cantidadBonificadaDespues);
//                            if (cantidadEntregada <= 0) {
//                                cantidadEntregada = cantidad;
//                            }
//
//                            Log.e("PedidosActivity", "cantidadEntregada = cantEntregadaAnterior("+Integer.parseInt(registroAnterior.get(2))+") - ( "+cantidadBonificadaAntes+" - "+cantidadBonificadaDespues+" )");
//                            int saldoAnterior = Integer.parseInt(registroAnterior.get(1));
//                            DAOBonificaciones.Actualizar_RegistroBonificacion(registroAnterior.get(0), 0, 0, 0, 0, "",codven,codcli);//Limpiar registro anterior
//                            //Se pasan los campos al nuevo regitro y se calculan los montos pendientes
//                            DAOBonificaciones.Actualizar_RegistroBonificacion(codigoRegistroActual, cantidad,saldoAnterior, cantidadEntregada, (cantidad+saldoAnterior-cantidadEntregada), registroAnterior.get(3),codven,codcli);
//
//                            if (codigoRegistroActual.equals("") || codigoRegistroActual.equals("null") || codigoRegistroActual == null) {
//                                //Toast.makeText(getApplicationContext(), "El saldo se debe devolver al anterior", Toast.LENGTH_SHORT).show();
//                                Log.d("PedidosActivity", "El saldo se debe devolver al anterior");
//                            }else{
//                                if (cantidadEntregada > 0)  {
//                                    cantidad = cantidadEntregada;
//                                }
//                            }
//
//
//                        }
//
//                        if (cantidad == 0) {
//                            // El producto no tiene cantidad, no debe ser mostrado
//                            DAOBonificaciones.Actualizar_saldoBonificacion(codigoRegistroAnterior, saldoAnterior1);
//
//                            DAOPedidoDetalle.Eliminar_itemPedidoBonificacion( boni.getSalida(), Oc_numero, DAOBonificaciones,  prod, boni.getSecuenciaPromocion() );
//                        } else {
//                            DAOPedidoDetalle.Modificar_CantidadItemPedido(Oc_numero, "B"+dbpromo.getSalida(), cantidad);
//                        }
//
//                        mostrarListaProductos(boni.getSalida());
//
//                    } else {
//                        /*
//                         * Las dependencias son cuando una promocion es tipo &, se
//                         * necesita tener todos las entradas, si no estan todas no
//                         * se registra ninguno, por lo tanto al borrarse uno se
//                         * deben borrar todos.
//                         */
//
//                        //Obtener el ultimo registro completo
//                        int saldoAnterior1 = DAOBonificaciones.getSaldoAnterior(Oc_numero,boni.getSalida());
//                        String codigoRegistroAnterior = DAOBonificaciones.getCodigoRegistroAnterior(Oc_numero, boni.getSalida());
//                        ArrayList<String> registroAnterior = DAOBonificaciones.getRegistroAnterior(Oc_numero, boni.getSalida());
//                        int cantidadBonificadaAntes = DAOBonificaciones.getCantidadSalidaSecuencia(Oc_numero, boni.getSecuenciaPromocion(),boni.getItem(), boni.getAgrupado());
//                        DAOBonificaciones.Eliminar_RegistroBonificacion_Dependencias(
//                                Oc_numero, boni.getSecuenciaPromocion(),
//                                boni.getItem(), boni.getAgrupado());
//                        int cantidadBonificadaDespues = DAOBonificaciones.getCantidadSalidaSecuencia(Oc_numero, boni.getSecuenciaPromocion(),boni.getItem(), boni.getAgrupado());
//
//                        int cantidad = DAOBonificaciones.obtenerCantidadBonificacion(Oc_numero,boni.getSalida());
//
//                        if (registroAnterior.size() > 0 && registroAnterior.get(1) != null) {
//                            //Si habia un registro completo con pendientes
//                            //obtener el ultimo registro, si no existe bajo las condiciones quiere decir q se ha eliminado por lo cual se debe designar al ulimo registrado como el nuevo ultimo registro
//
//                            String codigoRegistroActual = "";//Sera el codigo registro a modificar, si no se obtiene el ultimo completo se obtiene el ultimo bonificado para ser el nuevo completo
//
//                            ArrayList<String> ultimoRegistroCompleto = DAOBonificaciones.getRegistroAnterior(Oc_numero, boni.getSalida());//Si no se encuentra un ultimoRegistroCompleto quiere decir que ha sido borrado
//                            Log.e("PedidosActivity", "ultimoRegistroCompleto: " +ultimoRegistroCompleto);
//
//                            if (ultimoRegistroCompleto.size() > 0) {
//                                if (ultimoRegistroCompleto.get(1) != null) {
//                                    codigoRegistroActual = ultimoRegistroCompleto.get(0);
//                                }else{
//                                    codigoRegistroActual = DAOBonificaciones.getUltimoRegistroBonificacion(Oc_numero,boni.getSalida());
//                                }
//                            }else{
//                                codigoRegistroActual = DAOBonificaciones.getUltimoRegistroBonificacion(Oc_numero,boni.getSalida());
//                            }
//
//                            //int cantidadTotal = DAOBonificaciones.obtenerCantidadBonificacion(oc_numero,"B"+promocionDetalle.getSalida());
//                            int cantidadEntregada = Integer.parseInt(registroAnterior.get(2)) - (cantidadBonificadaAntes - cantidadBonificadaDespues);
//                            if (cantidadEntregada <= 0) {
//                                cantidadEntregada = cantidad;
//                            }
//
//                            Log.e("PedidosActivity", "cantidadEntregada = cantEntregadaAnterior("+Integer.parseInt(registroAnterior.get(2))+") - ( "+cantidadBonificadaAntes+" - "+cantidadBonificadaDespues+" )");
//                            int saldoAnterior = Integer.parseInt(registroAnterior.get(1));
//                            DAOBonificaciones.Actualizar_RegistroBonificacion(registroAnterior.get(0), 0, 0, 0, 0, "",codven,codcli);//Limpiar registro anterior
//                            //Se pasan los campos al nuevo regitro y se calculan los montos pendientes
//                            DAOBonificaciones.Actualizar_RegistroBonificacion(codigoRegistroActual, cantidad,saldoAnterior, cantidadEntregada, (cantidad+saldoAnterior-cantidadEntregada), registroAnterior.get(3),codven,codcli);
//
//                            if (codigoRegistroActual.equals("") || codigoRegistroActual.equals("null") || codigoRegistroActual == null) {
//                                //Toast.makeText(getApplicationContext(), "El saldo se debe devolver al anterior", Toast.LENGTH_SHORT).show();
//                                Log.d("PedidosActivity", "El saldo se debe devolver al anterior");
//                            }
//
//                        }
//
//                        int cantidadEntregadaTotal = DAOBonificaciones.getCantidadEntregada(Oc_numero,boni.getSalida());
//
//                        if (cantidadEntregadaTotal > 0)  {
//                            cantidad = cantidadEntregadaTotal;
//                        }
//
//
//                        if (cantidad == 0) {
//                            DAOBonificaciones.Actualizar_saldoBonificacion(codigoRegistroAnterior, saldoAnterior1);
//
//                            // El producto no tiene cantidad, no debe ser mostrado
//                            DAOPedidoDetalle.Eliminar_itemPedidoBonificacion(boni.getSalida(), Oc_numero, DAOBonificaciones, prod, boni.getSecuenciaPromocion());
//                        } else {
//                            DAOPedidoDetalle.Modificar_CantidadItemPedido(Oc_numero, boni.getSalida(), cantidad);
//                        }
//
//                        mostrarListaProductos(boni.getSalida());
//                    }
//
//
//                }
//
//                dbclass.EliminarItemPedido(prod.getCodprod(), prod.getItem(), Oc_numero);
//
//                Log.e("", "---------------------------------");
//                Gson gson = new Gson();
//                Log.d("listaRegistroBonificacaciones", gson
//                        .toJson(DAOBonificaciones.ObtenerRegistroBonificaciones()));
//                Log.d("listaPedidoDetalle",
//                        gson.toJson(dbclass.getPedidosDetallexOc_numero(Oc_numero)));
//
//                mostrarListaProductos("");
//
//                dbclass.eliminar_detalle_entrega(Oc_numero, prod.getCodprod());
//                DAOBonificaciones.eliminar_detalle_promocion(Oc_numero, prod.getCodprod());
//
//                return true;
//
//            case R.id.CntxMenu_Eliminar_item_promo:
//                Log.d("PedidosActivity :onContextItemSelected:","CntxMenu_Eliminar_item_promo");
//                AdapterView.AdapterContextMenuInfo info3 = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//
//                ItemProducto prod3 = (ItemProducto) lstProductos.getAdapter().getItem(info3.position);
//                int saldoAnterior = DAOBonificaciones.getSaldoAnterior(Oc_numero,prod3.getCodprod());
//                String codigoRegistroAnterior = DAOBonificaciones.getCodigoRegistroAnterior(Oc_numero, prod3.getCodprod());
//                DAOBonificaciones.Actualizar_saldoBonificacion(codigoRegistroAnterior, saldoAnterior);
//
//                //Si hay un pendiente nulo agregado este debe devolver el saldo usado antes de ser eliminado
//                DB_RegistroBonificaciones nuloAgregado = DAOBonificaciones.getPendienteAgregado(Oc_numero, prod3.getCodprod());
//                if (nuloAgregado.getOc_numero() != null) {//Si existe un nuloAgregado
//                    DB_RegistroBonificaciones registroUsado = DAOBonificaciones.getRegistroBonificacion(nuloAgregado.getCodigoAnterior());
//                    DAOBonificaciones.Actualizar_saldoBonificacion(registroUsado.getCodigoRegistro(), (registroUsado.getSaldo()+nuloAgregado.getCantidadEntregada()));
//                    Log.d("PedidosActivity", "Saldo devuelto por "+nuloAgregado.getSalida()+" al codigoRegistro:"+registroUsado.getCodigoRegistro()+" saldo total:"+(registroUsado.getSaldo()+nuloAgregado.getCantidadEntregada()));
//                }
//
//                DAOBonificaciones.Eliminar_RegistroBonificacion_xSalida(Oc_numero,prod3.getCodprod());
//                DAOPedidoDetalle.Eliminar_itemPedidoBonificacion(prod3.getCodprod(), Oc_numero, DAOBonificaciones, prod3, 0 );
//
//                Log.e("", "---------------------------------");
//                Gson gson2 = new Gson();
//                Log.d("listaRegistroBonificacaciones", gson2.toJson(DAOBonificaciones.ObtenerRegistroBonificaciones()));
//                Log.d("listaPedidoDetalle", gson2.toJson(dbclass.getPedidosDetallexOc_numero(Oc_numero)));
//
//                mostrarListaProductos("");//--------------------------------------------------------------------------------------- ANALIZAR
//                DAOBonificaciones.eliminar_detalle_promocion(Oc_numero, prod3.getCodprod());
//                return true;
//
//            //COLORES BONIFICACION
//
//
//            case R.id.CntxMenu_Editar_color_bono:
//
//                Log.d("PedidosActivity :onContextItemSelected:","CntxMenu_editar_color_bono");
//                AdapterView.AdapterContextMenuInfo info5 = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//                final ItemProducto prod5 = (ItemProducto) lstProductos.getAdapter().getItem(info5.position);
//
//                String sec=DAOBonificaciones.getSecuenciaporEntrada(prod5.getCodprod(), Oc_numero);
//
//                //OBTENIENDO COLORES SEGUN SECUENCIA E ITEM
//                ArrayList<DB_Pedido_Detalle_Promocion> listaColores= new ArrayList<DB_Pedido_Detalle_Promocion>();
//                listaColores=dbclass.obtenerListadoDetallePromocion(Oc_numero, prod5.getCodprod(),sec);
//
//                final ArrayList<DB_Pedido_Detalle_Promocion> detpromo= new ArrayList<DB_Pedido_Detalle_Promocion>();
//                Adapter_Bonificacion_Colores adpcolores;
//
//                adpcolores= new Adapter_Bonificacion_Colores(PedidosActivity.this,
//                        listaColores,Oc_numero,prod5.getCodprod(),sec,prod5.getCantidad(),detpromo);
//
//
//
//                LayoutInflater ly = LayoutInflater.from(PedidosActivity.this);
//                View v = ly.inflate(R.layout.dialog_bonificacion_colores, null);
//                ListView lv_bonificacionColores= (ListView)v.findViewById(R.id.lv_bonificacionColores);
//
//                lv_bonificacionColores.setAdapter(adpcolores);
//
//
//                AlertDialog.Builder dialogBono = new AlertDialog.Builder(PedidosActivity.this);
//                dialogBono.setView(v);
//
//                dialogBono
//
//                        .setPositiveButton("Guardar",null)
//                        .setNeutralButton("Borrar Datos", null)
//                        .setNegativeButton("Cancelar",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog,int id) {
//
//                                    }
//                                });
//
//                final AlertDialog alert= dialogBono.create();
//                alert.show();
//                alert.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
//                alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
//                //alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//
//                //BOTON GUARDAR
//
//                Button bt = alert.getButton(AlertDialog.BUTTON_POSITIVE);
//                bt.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        // TODO Auto-generated method stub
//                        Gson json= new Gson();
//                        for(int i=0;i<detpromo.size();i++){
//                            DB_Pedido_Detalle_Promocion filapromo= new DB_Pedido_Detalle_Promocion();
//                            filapromo.setOc_numero(detpromo.get(i).getOc_numero());
//                            filapromo.setCip(detpromo.get(i).getCip());
//                            filapromo.setCc_artic(detpromo.get(i).getCc_artic());
//                            filapromo.setSecuencia(detpromo.get(i).getSecuencia());
//                            filapromo.setCantidad(detpromo.get(i).getCantidad());
//                            filapromo.setDescripcion(detpromo.get(i).getDescripcion());
//                            if(detpromo.get(i).getCantidad()>0){
//                                DAOBonificaciones.AgregarPedidoDetallePromo(filapromo, Oc_numero, prod5.getCodprod(), detpromo.get(i).getCc_artic());
//                                Log.w("JSON BOTON GUARDAR","INSERTANDO: FILA "+(i+1)+" DATOS: "+json.toJson(filapromo));
//                            }
//                        }
//                        GlobalFunctions.showCustomToastShort(PedidosActivity.this, "Se guardaron los datos", GlobalFunctions.TOAST_DONE, GlobalFunctions.POSICION_BOTTOM);
//                        alert.dismiss();
//                    }
//                });
//
//                Button btEliminar=alert.getButton(AlertDialog.BUTTON_NEUTRAL);
//                btEliminar.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        DAOBonificaciones.eliminar_detalle_promocion(Oc_numero, prod5.getCodprod());
//                        GlobalFunctions.showCustomToastShort(PedidosActivity.this, "Se eliminaron los datos", GlobalFunctions.TOAST_DONE, GlobalFunctions.POSICION_BOTTOM);
//                        alert.dismiss();
//                    }
//                });
//
//                return true;
//            //FIN COLORES
//
//            case R.id.CntxMenu_Editar:
//                /*
//                 * COMENTADO TEMPORALMANTE HASTA REALIZAR LA FUNCIONALIDAD MODIFICAR
//                 * EN REGISTRO BONIFICACIONES
//                 */
//                /*
//                 * AdapterView.AdapterContextMenuInfo info2 =
//                 * (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//                 *
//                 * final ItemProducto prod2 = (ItemProducto)
//                 * lstProductos.getAdapter().getItem(info2.position); Intent intent
//                 * = new Intent(PedidosActivity.this, ProductoActivity.class);
//                     * intent.putExtra("codcli", codcli); intent.putExtra("codven",
//                 * codven); intent.putExtra("origen", pedido);
//                 * intent.putExtra("origen", "PEDIDO_MODIFICAR");
//                 * intent.putExtra("OCNUMERO", Oc_numero); intent.putExtra("CODPRO",
//                 * prod2.getCodprod());
//                 *
//                 * // Datos necesarios para el tema de percepcion
//                 * intent.putExtra(ProductoActivity.CLIENTE_TIENE_PERCEPCION,
//                 * clienteTienePercepcion);
//                 * intent.putExtra(ProductoActivity.CLIENTE_TIENE_PERCEPCION_ESPECIAL
//                 * , clienteTienePercepcionEspecial);
//                 * intent.putExtra(ProductoActivity.CLIENTE_VALOR_PERCEPCION,
//                 * clienteValorPercepcion);
//                 *
//                 * Log.d("PedidosActivity ::item Editar::",
//                 * "Envio a ProductoActivity :PEDIDO_MODIFICAR:");
//                 *
//                 * startActivityForResult(intent, 1);
//                 */
//                GlobalFunctions.showCustomToast(PedidosActivity.this, "Elimine y vuelva a ingresar el producto", GlobalFunctions.TOAST_WARNING, GlobalFunctions.POSICION_BOTTOM);
//                return true;
//
//
//            //------- EDITAR ENTREGAS ---------
//            case R.id.CntxMenu_Editar_Ent:
//
//                AdapterView.AdapterContextMenuInfo info2 = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//                final ItemProducto producto = (ItemProducto) lstProductos.getAdapter().getItem(info2.position);
//
//                final int filas_detalle_entrega=dbclass.getCantidad_Detalle_Entrega(edt_nroPedido.getText().toString(),producto.getCodprod());
//                final ArrayList<DB_Detalle_Entrega> filas= new ArrayList<DB_Detalle_Entrega>();
//
//                //Si no hay datos previos, mostrar spinner para ingresar la cantidad de entregas
//                if(filas_detalle_entrega<1)	{
//
//                    // llenando spinner con numeros hasta el 15
//                    Integer[] canti= new Integer[15];
//                    for(int i=0;i<15;i++){
//                        canti[i]=i+1;
//                    }
//
//                    ArrayAdapter<Integer> adp= new ArrayAdapter<>(PedidosActivity.this, android.R.layout.simple_list_item_1,canti);
//                    LayoutInflater li = LayoutInflater.from(PedidosActivity.this);
//                    View promptsView = li.inflate(R.layout.dialog_cant_entregas, null);
//                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PedidosActivity.this);
//                    alertDialogBuilder.setView(promptsView);
//
//                    final Spinner spn_cant_ent = (Spinner) promptsView.findViewById(R.id.item_spn_cant_ent);
//                    spn_cant_ent.setAdapter(adp);
//
//                    alertDialogBuilder
//                            .setCancelable(false)
//                            .setPositiveButton("OK",
//                                    new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog,int id) {
//
//                                            //obteniendo numero de entregas
//                                            int nro_ent;
//                                            nro_ent=Integer.parseInt(spn_cant_ent.getSelectedItem().toString());
//
//                                            //llenando arraylist con la cantidad de filas obtenidas
//                                            for(int i=0;i<nro_ent;i++){
//                                                DB_Detalle_Entrega det= new DB_Detalle_Entrega();
//                                                det.setNroEntrega(i+1);
//                                                filas.add(det);
//                                            }
//                                            //enviando array vacio, solo con la cantidad de filas (Nro de Entrega)
//                                            verDialogEntregas(filas,item, VERSIO6_A_MENOS);
//                                        }
//                                    })
//                            .setNegativeButton("Cancel",
//                                    new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog,int id) {
//                                            dialog.cancel();
//                                        }
//                                    });
//
//                    AlertDialog alertDialog = alertDialogBuilder.create();
//                    alertDialog.show();
//
//                }
//
//                // si hay datos, enviar la cantidad de filas
//                else {
//                    verDialogEntregas(filas,item, VERSIO6_A_MENOS);
//                }
//
//                return true;
//
//            case R.id.CntxMenu_Editar_item_promo:
//
//                AdapterView.AdapterContextMenuInfo info4 = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//
//                final ItemProducto bonificacion = (ItemProducto) lstProductos.getAdapter().getItem(info4.position);
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//                final int cantidadesPendiente = DAOBonificaciones.getSaldoPendiente(codcli,bonificacion.getCodprod());
//                final int cantidadMaximaB = bonificacion.getCantidad() + cantidadesPendiente;
//
//                final EditText input = new EditText(getApplicationContext());
//                input.setInputType(InputType.TYPE_CLASS_NUMBER);
//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT,.5f);
//                params.setMargins(10, 2, 2, 10);
//                input.setLayoutParams(params);
//
//                builder.setTitle("Ingrese cantidad");
//                builder.setMessage("Cantidad maxima "+cantidadMaximaB);
//                builder.setView(input);
//
//                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (input.length()>0) {
//                            int cantidad = Integer.parseInt(input.getText().toString());
//                            if (cantidad > 0 && cantidad <= cantidadMaximaB) {
//                                actualizarBonificacion(bonificacion,cantidad,cantidadesPendiente);
//
//                            }else{
//                                input.setError("Cantidad no valida");
//                            }
//                        }
//                    }
//                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener(){
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//
//                });
//                builder.create().show();
//
//                return true;
//
//            default:
//                return super.onContextItemSelected(item);
//        }
//    }

    private void eliminarOrUpdateProducto(ItemProducto prod, boolean isEliminar){
        String codPro= prod.getCodprod();
        Log.i(TAG," accion_segunId ELIMINAR PRODUCTO, copPro "+codPro);

        /* Lista de bonificaciones afectadas por el producto eliminado */
        ArrayList<DB_RegistroBonificaciones> bonificaciones_xEntrada = DAOBonificaciones.getRegistroBonificaciones_xEntrada(Oc_numero,codPro,  prod.getItem());

        for (DB_RegistroBonificaciones boni : bonificaciones_xEntrada) {
            if (DAOBonificaciones.esAcumulado(boni.getSecuenciaPromocion())) {
                int saldoAnterior1 = DAOBonificaciones.getSaldoAnterior(Oc_numero,boni.getSalida());
                String codigoRegistroAnterior = DAOBonificaciones.getCodigoRegistroAnterior(Oc_numero, boni.getSalida());

                int cantidadBonificadaAntes = DAOBonificaciones.getCantidadSalidaSecuencia(Oc_numero, boni.getSecuenciaPromocion(),boni.getItem(), boni.getAgrupado(), boni.getSalida_item());
                ArrayList<String> registroAnterior = DAOBonificaciones.getRegistroAnterior(Oc_numero, boni.getSalida());
                DAOBonificaciones.Eliminar_RegistroBonificacion(Oc_numero,boni.getSecuenciaPromocion(), boni.getItem(),boni.getEntrada(), boni.getAgrupado(), boni.getEntrada_item(), boni.getSalida_item());

                int cantidadEntradas = 0;
                double montoEntradas = 0;
                if(isEliminar){
                    cantidadEntradas = DAOBonificaciones.Recalcular_cantidadEntrada(Oc_numero,boni.getSecuenciaPromocion(),boni.getAcumulado(), boni.getSalida_item());
                    montoEntradas = DAOBonificaciones.Recalcular_montoEntrada(Oc_numero,boni.getSecuenciaPromocion(),boni.getAcumulado(), boni.getSalida_item());
                }

                DB_PromocionDetalle dbpromo = DAOBonificaciones.getPromocionDetalle(boni.getSecuenciaPromocion(),boni.getEntrada(), boni.getAgrupado());
                Log.v("PedidosActivity :itemEliminar:","detalle promocion del item a eliminar");
                Log.v("PedidosActivity :itemEliminar:", "secuencia: "+ dbpromo.getSecuencia());
                Log.v("PedidosActivity :itemEliminar:", "entrada: "+ dbpromo.getEntrada());
                Log.v("PedidosActivity :itemEliminar:", "agrupado: "+ dbpromo.getAgrupado());
                Log.v("PedidosActivity :itemEliminar:", "salida: "+ dbpromo.getSalida());
                Log.v("PedidosActivity :itemEliminar:", "acumulado: "+ dbpromo.getAcumulado());

                ArrayList<DB_RegistroBonificaciones> regBonificaciones = DAOBonificaciones.getRegistroBonificaciones_xSecuencia(
                        Oc_numero,
                        boni.getSecuenciaPromocion(),
                        boni.getItem(),
                        boni.getAcumulado(),
                        boni.getSalida_item());

                listaEntradasCompuestas = new ArrayList<String[]>();
                cantidadesUsadas = new ArrayList<Integer>();
                montosUsados = new ArrayList<Double>();

                /*
                 * --------------------- Obtener las cantidades y montos
                 * utilizados ---------------------
                 */
                for (DB_RegistroBonificaciones itemBoni : regBonificaciones) {
                    int cantidadUnidadesMin = Convertir_toUnidadesMinimas(
                            itemBoni.getEntrada(),
                            itemBoni.getUnimedEntrada(),
                            itemBoni.getCantidadEntrada());

                    entradasCompuestas = new String[9];
                    entradasCompuestas[1] = String.valueOf(cantidadUnidadesMin);
                    entradasCompuestas[2] = String.valueOf(itemBoni.getMontoEntrada());
                    Log.e("", "cantidad entrada entradasCompuestas[1]:   "+ cantidadUnidadesMin);
                    Log.e("", "   Monto entrada entradasCompuestas[2]:   "+ itemBoni.getMontoEntrada());
                    listaEntradasCompuestas.add(entradasCompuestas);
                }

                int cantidadSalida = 0;
                Log.d("PedidosActivity:", "dbpromo.getTipo() -> "+dbpromo.getTipo());
                /** Se generan las cantidades y montos usados **/
                if (dbpromo.getTipo().equals("C")) {
                    cantidadSalida = verificarTipoCondicionAcumulados_xCantidad(dbpromo, String.valueOf(montoEntradas),cantidadEntradas);
                    Log.d("Cantidad","cantidad bonificacion obtenido por Acumulados xCantidad-> "+ cantidadSalida);
                } else {
                    cantidadSalida = verificarTipoCondicionAcumulados_xMonto(dbpromo, String.valueOf(montoEntradas),cantidadEntradas);
                    Log.d("Cantidad","cantidad bonificacion obtenido por Acumulados xMonto-> "+ cantidadSalida);
                }

                /*
                 * --------------------- Actualizar las cantidades y montos
                 * disponibles ---------------------
                 */
                int ind = 0;
                for (DB_RegistroBonificaciones itemBoni : regBonificaciones) {
                    /*
                     * Si es el ultimo registro se le coloca la cantidad
                     * Bonificada
                     */
                    String[] entradaCompuesta = listaEntradasCompuestas.get(ind);
                    int cantidadDisponible = Integer.parseInt(entradaCompuesta[1]);
                    double montoDisponible = Double.parseDouble(entradaCompuesta[2]);
                    cantidadDisponible = cantidadDisponible- cantidadesUsadas.get(ind);
                    montoDisponible = montoDisponible- montosUsados.get(ind);

                    if ((ind + 1) == regBonificaciones.size()) {
                        DAOBonificaciones.ActualizarRegistrosAcumulado(
                                Oc_numero,
                                itemBoni.getSecuenciaPromocion(),
                                itemBoni.getItem(), itemBoni.getEntrada(),
                                itemBoni.getAgrupado(), cantidadSalida,
                                cantidadDisponible, montoDisponible,
                                itemBoni.getEntrada_item()
                                );
                    } else {
                        DAOBonificaciones.ActualizarRegistrosAcumulado(
                                Oc_numero,
                                itemBoni.getSecuenciaPromocion(),
                                itemBoni.getItem(), itemBoni.getEntrada(),
                                itemBoni.getAgrupado(), 0,
                                cantidadDisponible, montoDisponible,
                                itemBoni.getEntrada_item());
                    }
                    ind++;
                }

                int cantidadBonificadaDespues = DAOBonificaciones.getCantidadSalidaSecuencia(Oc_numero, boni.getSecuenciaPromocion(),boni.getItem(), boni.getAgrupado(), boni.getSalida_item());

                int cantidad = DAOBonificaciones.obtenerCantidadBonificacion(Oc_numero,"B"+dbpromo.getSalida(), boni.getSalida_item());

                if (registroAnterior.size() > 0 && registroAnterior.get(1) != null) {
                    //Si habia un registro completo con pendientes
                    //obtener el ultimo registro, si no existe bajo las condiciones quiere decir q se ha eliminado por lo cual se debe designar al ulimo registrado como el nuevo ultimo registro

                    String codigoRegistroActual = "";//Sera el codigo registro a modificar, si no se obtiene el ultimo completo se obtiene el ultimo bonificado para ser el nuevo completo

                    ArrayList<String> ultimoRegistroCompleto = DAOBonificaciones.getRegistroAnterior(Oc_numero, boni.getSalida());//Si no se encuentra un ultimoRegistroCompleto quiere decir que ha sido borrado
                    Log.e("PedidosActivity", "ultimoRegistroCompleto: " +ultimoRegistroCompleto);

                    if (ultimoRegistroCompleto.size() > 0) {
                        if (ultimoRegistroCompleto.get(1) != null) {
                            codigoRegistroActual = ultimoRegistroCompleto.get(0);
                        }else{
                            codigoRegistroActual = DAOBonificaciones.getUltimoRegistroBonificacion(Oc_numero,boni.getSalida());
                        }
                    }else{
                        codigoRegistroActual = DAOBonificaciones.getUltimoRegistroBonificacion(Oc_numero,boni.getSalida());
                    }

                    //int cantidadTotal = DAOBonificaciones.obtenerCantidadBonificacion(oc_numero,"B"+promocionDetalle.getSalida());
                    int cantidadEntregada = Integer.parseInt(registroAnterior.get(2)) - (cantidadBonificadaAntes - cantidadBonificadaDespues);
                    if (cantidadEntregada <= 0) {
                        cantidadEntregada = cantidad;
                    }

                    Log.e("PedidosActivity", "cantidadEntregada = cantEntregadaAnterior("+Integer.parseInt(registroAnterior.get(2))+") - ( "+cantidadBonificadaAntes+" - "+cantidadBonificadaDespues+" )");
                    int saldoAnterior = Integer.parseInt(registroAnterior.get(1));
                    DAOBonificaciones.Actualizar_RegistroBonificacion(registroAnterior.get(0), 0, 0, 0, 0, "",codven,codcli);//Limpiar registro anterior
                    //Se pasan los campos al nuevo regitro y se calculan los montos pendientes
                    DAOBonificaciones.Actualizar_RegistroBonificacion(codigoRegistroActual, cantidad,saldoAnterior, cantidadEntregada, (cantidad+saldoAnterior-cantidadEntregada), registroAnterior.get(3),codven,codcli);

                    if (codigoRegistroActual.equals("") || codigoRegistroActual.equals("null") || codigoRegistroActual == null) {
                        //Toast.makeText(getApplicationContext(), "El saldo se debe devolver al anterior", Toast.LENGTH_SHORT).show();
                        Log.d("PedidosActivity", "El saldo se debe devolver al anterior");
                    }else{
                        if (cantidadEntregada > 0)  {
                            cantidad = cantidadEntregada;
                        }
                    }


                }

                if (cantidad == 0) {
                    // El producto no tiene cantidad, no debe ser mostrado
                    DAOBonificaciones.Actualizar_saldoBonificacion(codigoRegistroAnterior, saldoAnterior1);
                    DAOPedidoDetalle.Eliminar_itemPedidoBonificacion(boni.getSalida(), Oc_numero, DAOBonificaciones, prod, boni.getSecuenciaPromocion(), boni.getSalida_item());
                    DeletePromocionComboOrColorsIfExist(dbpromo.getSecuencia(), boni.getSalida_item());
                } else {
                    DAOPedidoDetalle.Modificar_CantidadItemPedido(Oc_numero, "B"+dbpromo.getSalida(), cantidad);
                    modifyPromotionColorsOrXComboIfExists(dbpromo.getSecuencia(), dbpromo.getTipo_promocion(), (int)cantidad, boni.getSalida_item());
                }

                mostrarListaProductos(boni.getSalida());

            } else {
                /*
                 * Las dependencias son cuando una promocion es tipo &, se
                 * necesita tener todos las entradas, si no estan todas no
                 * se registra ninguno, por lo tanto al borrarse uno se
                 * deben borrar todos.
                 */

                //Obtener el ultimo registro completo
                int saldoAnterior1 = DAOBonificaciones.getSaldoAnterior(Oc_numero,boni.getSalida());
                String codigoRegistroAnterior = DAOBonificaciones.getCodigoRegistroAnterior(Oc_numero, boni.getSalida());
                ArrayList<String> registroAnterior = DAOBonificaciones.getRegistroAnterior(Oc_numero, boni.getSalida());
                int cantidadBonificadaAntes = DAOBonificaciones.getCantidadSalidaSecuencia(Oc_numero, boni.getSecuenciaPromocion(),boni.getItem(), boni.getAgrupado(), boni.getSalida_item());

                DAOBonificaciones.Eliminar_RegistroBonificacion_Dependencias(
                        Oc_numero, boni.getSecuenciaPromocion(),
                        boni.getItem(), boni.getAgrupado(), boni.getSalida_item());

                int cantidadBonificadaDespues = DAOBonificaciones.getCantidadSalidaSecuencia(Oc_numero, boni.getSecuenciaPromocion(),boni.getItem(), boni.getAgrupado(), boni.getSalida_item());

                int cantidad = DAOBonificaciones.obtenerCantidadBonificacion(Oc_numero,boni.getSalida(), boni.getSalida_item());

                if (registroAnterior.size() > 0 && registroAnterior.get(1) != null) {
                    //Si habia un registro completo con pendientes
                    //obtener el ultimo registro, si no existe bajo las condiciones quiere decir q se ha eliminado por lo cual se debe designar al ulimo registrado como el nuevo ultimo registro

                    String codigoRegistroActual = "";//Sera el codigo registro a modificar, si no se obtiene el ultimo completo se obtiene el ultimo bonificado para ser el nuevo completo

                    ArrayList<String> ultimoRegistroCompleto = DAOBonificaciones.getRegistroAnterior(Oc_numero, boni.getSalida());//Si no se encuentra un ultimoRegistroCompleto quiere decir que ha sido borrado
                    Log.e("PedidosActivity", "ultimoRegistroCompleto: " +ultimoRegistroCompleto);

                    if (ultimoRegistroCompleto.size() > 0) {
                        if (ultimoRegistroCompleto.get(1) != null) {
                            codigoRegistroActual = ultimoRegistroCompleto.get(0);
                        }else{
                            codigoRegistroActual = DAOBonificaciones.getUltimoRegistroBonificacion(Oc_numero,boni.getSalida());
                        }
                    }else{
                        codigoRegistroActual = DAOBonificaciones.getUltimoRegistroBonificacion(Oc_numero,boni.getSalida());
                    }

                    //int cantidadTotal = DAOBonificaciones.obtenerCantidadBonificacion(oc_numero,"B"+promocionDetalle.getSalida());
                    int cantidadEntregada = Integer.parseInt(registroAnterior.get(2)) - (cantidadBonificadaAntes - cantidadBonificadaDespues);
                    if (cantidadEntregada <= 0) {
                        cantidadEntregada = cantidad;
                    }

                    Log.e("PedidosActivity", "cantidadEntregada = cantEntregadaAnterior("+Integer.parseInt(registroAnterior.get(2))+") - ( "+cantidadBonificadaAntes+" - "+cantidadBonificadaDespues+" )");
                    int saldoAnterior = Integer.parseInt(registroAnterior.get(1));
                    DAOBonificaciones.Actualizar_RegistroBonificacion(registroAnterior.get(0), 0, 0, 0, 0, "",codven,codcli);//Limpiar registro anterior
                    //Se pasan los campos al nuevo regitro y se calculan los montos pendientes
                    DAOBonificaciones.Actualizar_RegistroBonificacion(codigoRegistroActual, cantidad,saldoAnterior, cantidadEntregada, (cantidad+saldoAnterior-cantidadEntregada), registroAnterior.get(3),codven,codcli);

                    if (codigoRegistroActual.equals("") || codigoRegistroActual.equals("null") || codigoRegistroActual == null) {
                        //Toast.makeText(getApplicationContext(), "El saldo se debe devolver al anterior", Toast.LENGTH_SHORT).show();
                        Log.d("PedidosActivity", "El saldo se debe devolver al anterior");
                    }

                }

                int cantidadEntregadaTotal = DAOBonificaciones.getCantidadEntregada(Oc_numero,boni.getSalida(), boni.getSalida_item());

                if (cantidadEntregadaTotal > 0)  {
                    cantidad = cantidadEntregadaTotal;
                }


                if (cantidad == 0) {
                    DAOBonificaciones.Actualizar_saldoBonificacion(codigoRegistroAnterior, saldoAnterior1);
                    // El producto no tiene cantidad, no debe ser mostrado
                    DAOPedidoDetalle.Eliminar_itemPedidoBonificacion(boni.getSalida(), Oc_numero, DAOBonificaciones, prod, boni.getSecuenciaPromocion(), boni.getSalida_item());
                    DeletePromocionComboOrColorsIfExist(boni.getSecuenciaPromocion(), boni.getSalida_item());
                } else {
                    DAOPedidoDetalle.Modificar_CantidadItemPedido(Oc_numero, boni.getSalida(), cantidad);
                    DB_PromocionDetalle dbpromo = DAOBonificaciones.getPromocionDetalle(boni.getSecuenciaPromocion(),boni.getEntrada(), boni.getAgrupado());
                    modifyPromotionColorsOrXComboIfExists(dbpromo.getSecuencia(), dbpromo.getTipo_promocion(), boni.getSalida_item (),(int)cantidad);
                }

                mostrarListaProductos(boni.getSalida());
            }


        }

        if (isEliminar) {
            if(prod.getItem()==0){
                UtilViewMensaje.MENSAJE_simple(this, "Eliminación fallído","Error de eliminacion de producto codigo "+prod.getCodprod());
            }
            else{
                dbclass.EliminarItemPedido(prod.getCodprod(), prod.getItem(), Oc_numero);
                Log.e("", "---------------------------------");
                Gson gson = new Gson();
                Log.d("listaRegistroBonificacaciones", gson.toJson(DAOBonificaciones.ObtenerRegistroBonificaciones()));
                Log.d("listaPedidoDetalle",gson.toJson(dbclass.getPedidosDetallexOc_numero(Oc_numero)));

                dbclass.eliminar_detalle_entrega(Oc_numero, codPro);
                DAOBonificaciones.eliminar_detalle_promocion(Oc_numero, codPro);
            }
        }
        mostrarListaProductos("");
    }
    public void accion_segunId(int acciones, String codPro){
        final ItemProducto prod = (ItemProducto) lstProductos.getAdapter().getItem(positionLisView);
        switch (acciones) {

            case ELIMINAR_PRODUCTO:

                boolean isEliminar=true;
                eliminarOrUpdateProducto(prod, isEliminar);

                break;

            case ELIMINAR_ITEM_PROMO:
                eliminarItemPromocion(prod);
                break;

            case EDITAR_COLOR_BONI:

                String sec=DAOBonificaciones.getSecuenciaporEntrada(productos.get(positionLisView).getCodprod(), Oc_numero);
                int cantidad=productos.get(positionLisView).getCantidad();
                final String final_codPro=codPro;
                final int final_cantidad=cantidad;
                //OBTENIENDO COLORES SEGUN SECUENCIA E ITEM
                ArrayList<DB_Pedido_Detalle_Promocion> listaColores= new ArrayList<DB_Pedido_Detalle_Promocion>();
                listaColores=dbclass.obtenerListadoDetallePromocion(Oc_numero, codPro,sec);

                final ArrayList<DB_Pedido_Detalle_Promocion> detpromo= new ArrayList<DB_Pedido_Detalle_Promocion>();
                Adapter_Bonificacion_Colores adpcolores;

                adpcolores= new Adapter_Bonificacion_Colores(PedidosActivity.this,listaColores,Oc_numero,codPro,sec,cantidad,detpromo);

                LayoutInflater ly = LayoutInflater.from(PedidosActivity.this);
                View v = ly.inflate(R.layout.dialog_bonificacion_colores, null);
                TextView txt_cantidad=v.findViewById(R.id.txt_cantidad_colores);
                ListView lv_bonificacionColores= (ListView)v.findViewById(R.id.lv_bonificacionColores);

                txt_cantidad.setVisibility(View.VISIBLE);
                txt_cantidad.setText("Cantidad bonificada ("+cantidad+"), catalogo ("+listaColores.size()+")");
                lv_bonificacionColores.setAdapter(adpcolores);


                AlertDialog.Builder dialogBono = new AlertDialog.Builder(PedidosActivity.this);
                dialogBono.setView(v);

                dialogBono

                        .setPositiveButton("Guardar",null)
                        .setNeutralButton("Borrar Datos", null)
                        .setNegativeButton("Cancelar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {

                                    }
                                });

                final AlertDialog alert= dialogBono.create();
                alert.show();
                alert.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                //alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

                //BOTON GUARDAR

                Button bt = alert.getButton(AlertDialog.BUTTON_POSITIVE);
                bt.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Gson json= new Gson();
                        for(int i=0;i<detpromo.size();i++){
                            DB_Pedido_Detalle_Promocion filapromo= new DB_Pedido_Detalle_Promocion();
                            filapromo.setOc_numero(detpromo.get(i).getOc_numero());
                            filapromo.setCip(detpromo.get(i).getCip());
                            filapromo.setCc_artic(detpromo.get(i).getCc_artic());
                            filapromo.setSecuencia(detpromo.get(i).getSecuencia());
                            filapromo.setCantidad(detpromo.get(i).getCantidad());
                            filapromo.setDescripcion(detpromo.get(i).getDescripcion());
                            if(detpromo.get(i).getCantidad()>0){
                                DAOBonificaciones.AgregarPedidoDetallePromo(filapromo, Oc_numero, final_codPro, detpromo.get(i).getCc_artic());
                                Log.w("JSON BOTON GUARDAR","INSERTANDO: FILA "+(i+1)+" DATOS: "+json.toJson(filapromo));
                            }
                        }
                        GlobalFunctions.showCustomToastShort(PedidosActivity.this, "Se guardaron los datos", GlobalFunctions.TOAST_DONE, GlobalFunctions.POSICION_BOTTOM);
                        alert.dismiss();
                    }
                });

                Button btEliminar=alert.getButton(AlertDialog.BUTTON_NEUTRAL);
                btEliminar.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        DAOBonificaciones.eliminar_detalle_promocion(Oc_numero, final_codPro);
                        GlobalFunctions.showCustomToastShort(PedidosActivity.this, "Se eliminaron los datos", GlobalFunctions.TOAST_DONE, GlobalFunctions.POSICION_BOTTOM);
                        alert.dismiss();
                    }
                });

                //FIN COLORES
                break;

            case EDITAR_CANTIDAD:
//                Log.i(TAG," accion_segunId EDITAR CANTIDAD, copPro "+codPro);
//                GlobalFunctions.showCustomToast(PedidosActivity.this, "Elimine y vuelva a ingresar el producto",
//                        GlobalFunctions.TOAST_WARNING, GlobalFunctions.POSICION_BOTTOM);

                AlertViewSimpleConEdittext alertEditCantidad=new AlertViewSimpleConEdittext(this);
                alertEditCantidad.titulo="Editar cantidad";
                alertEditCantidad.mensaje="Editar cantidad para "+prod.getCodprod()+"-"+prod.getDescripcion();
                alertEditCantidad.hint="Ingrese numero mayor a 0";
                alertEditCantidad.texto_cargado=""+prod.getCantidad();
                alertEditCantidad.min_caracteres=1;
                alertEditCantidad.type_number=true;
                alertEditCantidad.cancelable=true;
                alertEditCantidad.start(new AlertViewSimpleConEdittext.Listener() {
                    @Override
                    public String resultOK(String newCantida) {
                        if (newCantida==null)
                            return null;
                        boolean isUpdated=dbclass.modificarCantidadItemPedido(Integer.parseInt(newCantida),
                                prod.getItem(),
                                prod.getCodprod(),
                                Oc_numero
                                );
                        if (!isUpdated) {
                            UtilViewMensaje.MENSAJE_simple(PedidosActivity.this, "Error", "No se ha podido actualizar la cantidad");
                        }else{
                            UtilViewSnackBar.SnackBarSuccess(PedidosActivity.this, lstProductos,"Actualización correcta");
                            boolean isEliminar=false;//solo update
                            eliminarOrUpdateProducto(prod, isEliminar);
                            //recalcular promocion, si hay, agregar de forma automatica, pero seraá mejor alertar al usuario para q seleccione
                            DBPedido_Detalle producto_a_verificar = dbclass.getPedidosDetalleEntityWithItem(Oc_numero,prod.getCodprod(), prod.getItem());
                            getProductoSalida(producto_a_verificar, prod.getDescripcion());

                        }

                        return null;
                    }

                    @Override
                    public String resultBucle(String s) {
                        return null;
                    }
                });

                break;

            case EDITAR_PRODUCTO:
                modificarProducto(prod);
                break;

            case EDITAR_ENTREGA:

                final int filas_detalle_entrega=dbclass.getCantidad_Detalle_Entrega(edt_nroPedido.getText().toString(),codPro);
                final ArrayList<DB_Detalle_Entrega> filas= new ArrayList<DB_Detalle_Entrega>();

                //Si no hay datos previos, mostrar spinner para ingresar la cantidad de entregas
                if(filas_detalle_entrega<1)	{

                    // llenando spinner con numeros hasta el 15
                    Integer[] canti= new Integer[15];
                    for(int i=0;i<15;i++){
                        canti[i]=i+1;
                    }

                    ArrayAdapter<Integer> adp= new ArrayAdapter<>(PedidosActivity.this, android.R.layout.simple_list_item_1,canti);
                    LayoutInflater li = LayoutInflater.from(PedidosActivity.this);
                    View promptsView = li.inflate(R.layout.dialog_cant_entregas, null);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PedidosActivity.this);
                    alertDialogBuilder.setView(promptsView);

                    final Spinner spn_cant_ent = (Spinner) promptsView.findViewById(R.id.item_spn_cant_ent);
                    spn_cant_ent.setAdapter(adp);

                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {

                                            //obteniendo numero de entregas
                                            int nro_ent;
                                            nro_ent=Integer.parseInt(spn_cant_ent.getSelectedItem().toString());

                                            //llenando arraylist con la cantidad de filas obtenidas
                                            for(int i=0;i<nro_ent;i++){
                                                DB_Detalle_Entrega det= new DB_Detalle_Entrega();
                                                det.setNroEntrega(i+1);
                                                filas.add(det);
                                            }
                                            //enviando array vacio, solo con la cantidad de filas (Nro de Entrega)
                                            verDialogEntregas(filas,null, VERSION7_A_MAS);
                                        }
                                    })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            dialog.cancel();
                                        }
                                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                }

                // si hay datos, enviar la cantidad de filas
                else {
                    verDialogEntregas(filas,null, VERSION7_A_MAS);
                }

                break;
            case EDITAR_ITEM_PROMO:

                //final ItemProducto bonificacion = (ItemProducto) lstProductos.getAdapter().getItem(info4.position);
                ArrayList<DB_RegistroBonificaciones>  listaRegB = DAOBonificaciones.getRegistroBonificaciones(Oc_numero);
                Stream<DB_RegistroBonificaciones> filter= listaRegB.stream().filter(regB -> regB.getSalida_item()==(prod.getItem()));
                if (filter.count()>0) {
                    GlobalFunctions.showCustomToast(PedidosActivity.this, "No disponible para promociones programadas", GlobalFunctions.TOAST_WARNING);
                    return;
                }
                String codProBoni=codPro;
                int cantidadBoni=productos.get(positionLisView).getCantidad();


                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                final int cantidadesPendiente = DAOBonificaciones.getSaldoPendiente(codcli,codProBoni);
                final int cantidadMaximaB = cantidadBoni + cantidadesPendiente;

                final EditText input = new EditText(getApplicationContext());
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT,.5f);
                params.setMargins(10, 2, 2, 10);
                input.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                input.setTextSize(30);
                input.setLayoutParams(params);

                builder.setTitle("Ingrese cantidad");
                builder.setMessage("Cantidad maxima "+cantidadMaximaB);
                builder.setView(input);

                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (input.length()>0) {
                            int cantidad = Integer.parseInt(input.getText().toString());
                            boolean isUpdated=dbclass.modificarCantidadItemPedido(Integer.parseInt(cantidad+""),
                                    prod.getItem(),
                                    prod.getCodprod(),
                                    Oc_numero
                            );
                            if (!isUpdated) {
                                UtilViewMensaje.MENSAJE_simple(PedidosActivity.this, "Error", "No se ha podido actualizar la cantidad");
                            }else{
                                UtilViewSnackBar.SnackBarSuccess(PedidosActivity.this, lstProductos,"Actualización correcta");
                                mostrarListaProductos("");
                            }
//                            if (cantidad > 0 && cantidad <= cantidadMaximaB) {
//                                actualizarBonificacion(bonificacion,cantidad,cantidadesPendiente);
//
//                            }else{
//                                input.setError("Cantidad no valida");
//                                GlobalFunctions.showCustomToast(PedidosActivity.this, "Cantidad no válida", GlobalFunctions.TOAST_ERROR);
//                            }
                        }
                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }

                });
                builder.create().show();

                break;

            default:
                break;
        }

    }

    private void eliminarItemPromocion(ItemProducto prod){
        int _secPromo=prod.getSec_promo().trim().length()>0?Integer.parseInt(prod.getSec_promo()):0;
        int _secPromoPrioridad=prod.getSec_promo_prioridad();
        if(_secPromo<=0 && _secPromoPrioridad<=0){//Producto bonificacion manual
            dbclass.EliminarItemPedido(prod.getCodprod(), prod.getItem(), Oc_numero);
            mostrarListaProductos("");
            return;
        }

        String codPro=prod.getCodprod();
        int nroItemSalida=prod.getItem();
        int saldoAnterior = DAOBonificaciones.getSaldoAnterior(Oc_numero,codPro);

        String codigoRegistroAnterior = DAOBonificaciones.getCodigoRegistroAnterior(Oc_numero, codPro);
        DAOBonificaciones.Actualizar_saldoBonificacion(codigoRegistroAnterior, saldoAnterior);

        //Si hay un pendiente nulo agregado este debe devolver el saldo usado antes de ser eliminado
        DB_RegistroBonificaciones nuloAgregado = DAOBonificaciones.getPendienteAgregado(Oc_numero, codPro);
        if (nuloAgregado.getOc_numero() != null) {//Si existe un nuloAgregado
            DB_RegistroBonificaciones registroUsado = DAOBonificaciones.getRegistroBonificacion(nuloAgregado.getCodigoAnterior());
            DAOBonificaciones.Actualizar_saldoBonificacion(registroUsado.getCodigoRegistro(), (registroUsado.getSaldo()+nuloAgregado.getCantidadEntregada()));
            Log.d("PedidosActivity", "Saldo devuelto por "+nuloAgregado.getSalida()+" al codigoRegistro:"+registroUsado.getCodigoRegistro()+" saldo total:"+(registroUsado.getSaldo()+nuloAgregado.getCantidadEntregada()));
        }

        //obtenemos todos las secuencia promocion e item que dependa del producto salida
        ArrayList<DB_RegistroBonificaciones> bonificaciones_xSalida = DAOBonificaciones.getRegistroBonificaciones_xSalida(Oc_numero, codPro, nroItemSalida);
        for (int i = 0; i < bonificaciones_xSalida.size(); i++) {
            DB_RegistroBonificaciones dbRegBonif = bonificaciones_xSalida.get(i);
            //▄ ▄ ▄ ▄ ▄ ▄Actualizamos los precios con descuento a 0 a todos los productos q dependen ▄ ▄ ▄ ▄
            int secuencia_promocion=dbRegBonif.getSecuenciaPromocion();
            int item_promocion  =dbRegBonif.getItem();
            String productoSalida  =dbRegBonif.getSalida();
            int itemSalida  =dbRegBonif.getSalida_item();

            /**--Verificamos si hay otro prod de bonfiicacion con el mismo secuencia de promocion ---*/
            DAOPedidoDetalle.Eliminar_itemPedidoBonificacion(productoSalida, Oc_numero, DAOBonificaciones, prod, secuencia_promocion, nroItemSalida);
            DAOBonificaciones.Eliminar_RegistroBonificacion_xSalida(Oc_numero, productoSalida, itemSalida);
            DAOBonificaciones.eliminarBonificacionXSecuenciaConCero(Oc_numero, secuencia_promocion);

            DeletePromocionComboOrColorsIfExist(secuencia_promocion, itemSalida);
        }
        Log.e("", "---------------------------------");
        Gson gson2 = new Gson();
        Log.d("listaRegistroBonificacaciones", gson2.toJson(DAOBonificaciones.ObtenerRegistroBonificaciones()));
        Log.d("listaPedidoDetalle", gson2.toJson(dbclass.getPedidosDetallexOc_numero(Oc_numero)));

        dbclass.EliminarItemPedido(prod.getCodprod(), prod.getItem(), Oc_numero);
        DAOBonificaciones.eliminar_detalle_promocion(Oc_numero, codPro);

        mostrarListaProductos("");//--------------------------------------------------------------------------------------- ANALIZAR
    }


    private void MostrarResumenByTipoProducto(double totalDescuentoBonificacion){
        LinearLayout layoutResumentByTipoProducto=findViewById(R.id.layoutResumentByTipoProducto);
        layoutResumentByTipoProducto.removeAllViews();
        double valorIgv=new PreferenciaConfiguracion(this).getValorIgv();
        double tipoCambio=codigoMoneda.equals(MONEDA_SOLES_IN)?Double.parseDouble(tvTipoCambio.getText().toString()):1;
        Log.i(TAG, "MostrarResumenByTipoProducto:: codigoMoneda: "+codigoMoneda+" tipoCambio: "+tipoCambio);
        ArrayList<ResumenVentaTipoProducto> lista=dbclass.getPedidoResumenByTipoProducto(Oc_numero, valorIgv, tipoCambio);

        double sumPesoTotal=0;
        double sumSubTotal=0;
        double sumPrecioKiTotal=0;
        double sumIgvTotal=0;
        for (ResumenVentaTipoProducto itemRes : lista) {
            sumPesoTotal+=itemRes.getPesoTotal();
            sumSubTotal+=itemRes.getSutTotal();
            sumPrecioKiTotal+=itemRes.getPkDolar();
            sumIgvTotal+=itemRes.getIgvTotal();
            layoutResumentByTipoProducto.addView(GetViewResumenByTipoProducto(itemRes, R.color.grey_800, false));
        }

        ResumenVentaTipoProducto itemRes=new ResumenVentaTipoProducto(
                "Total", sumPesoTotal,sumSubTotal, sumPrecioKiTotal, sumIgvTotal);
        double totalDetalle= itemRes.getSutTotal();
        layoutResumentByTipoProducto.addView(GetViewResumenByTipoProducto(itemRes, R.color.grey_900, true));

        double totalSinIgvPagar=GlobalFunctions.redondear_toDouble(totalDetalle-totalDescuentoBonificacion);
        double totalConIgvPagar=totalSinIgvPagar*(1+valorIgv);
        tvDescuentoBonificacion.setText("Dscto Bonif: "+VARIABLES.formater_thow_decimal.format(totalDescuentoBonificacion));
        tvTotalPagarSinIgv.setText("Sub Total: "+VARIABLES.formater_thow_decimal.format(totalSinIgvPagar));
        tvTotalPedidoPagarConIgv.setText("Total: "+VARIABLES.formater_thow_decimal.format(totalConIgvPagar));

    }
    private View  GetViewResumenByTipoProducto(ResumenVentaTipoProducto itemRes, int resColor, boolean isCabecera){
        LayoutInflater inflater = LayoutInflater.from(this);
        final View laViewInflada = inflater.inflate(R.layout.item_resumen_by_tipo_producto, null, true);
        TextView tvTipoProducto =laViewInflada.findViewById(R.id.tvTipoProducto);
        TextView tvPesoTotal =laViewInflada.findViewById(R.id.tvPesoTotal);
        TextView tvSubTotal =laViewInflada.findViewById(R.id.tvSubTotal);
        TextView tvPrecioKilo =laViewInflada.findViewById(R.id.tvPrecioKilo);
        TextView tvIgvTotal =laViewInflada.findViewById(R.id.tvIgvTotal);
        TextView tvTotal =laViewInflada.findViewById(R.id.tvTotal);

        tvTipoProducto.setText(itemRes.getTipoProducto());

        DecimalFormat formaterText = new DecimalFormat("###0."+(isCabecera?"00":"000"));
        formaterText.setRoundingMode(RoundingMode.HALF_UP);
        DecimalFormat formatDouble = new DecimalFormat("0."+(isCabecera?"00":"000"));
        formatDouble.setRoundingMode(RoundingMode.HALF_UP);

        tvPesoTotal.setText(""+ VARIABLES.formater_thow_decimal.format(itemRes.getPesoTotal()));
        tvSubTotal.setText(""+formaterText.format(itemRes.getSutTotal()));
        tvPrecioKilo.setText(""+formaterText.format(itemRes.getPkDolar()));
        tvIgvTotal.setText(""+formaterText.format(itemRes.getIgvTotal()));
        String totalText= formaterText.format(Double.parseDouble(formatDouble.format(itemRes.getSutTotal())) + Double.parseDouble(formatDouble.format(itemRes.getIgvTotal())));
        tvTotal.setText(""+totalText);

        tvTipoProducto.setBackgroundColor(getResources().getColor(resColor));
        tvPesoTotal.setBackgroundColor(getResources().getColor(resColor));
        tvSubTotal.setBackgroundColor(getResources().getColor(resColor));
        tvPrecioKilo.setBackgroundColor(getResources().getColor(resColor));
        tvIgvTotal.setBackgroundColor(getResources().getColor(resColor));
        tvTotal.setBackgroundColor(getResources().getColor(resColor));
        return laViewInflada;
    }

    public void verDialogEntregas(ArrayList<DB_Detalle_Entrega> filas,final MenuItem itemenu, String versionAndroid){

        String codPro="";
        int cantidadTotal=0;
        if(versionAndroid.equals(VERSION7_A_MAS)){
            codPro= productos.get(positionLisView).getCodprod();
            cantidadTotal=productos.get(positionLisView).getCantidad();
        }else{
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) itemenu.getMenuInfo();
            final ItemProducto prod = (ItemProducto) lstProductos.getAdapter().getItem(info.position);
            cantidadTotal=prod.getCantidad();
            codPro=prod.getCodprod();
        }

        Adapter_Detalle_Entrega adp;
        final ArrayList<DB_Detalle_Entrega> arregloEntrega;

        Gson jsonn = new Gson();

        LayoutInflater ly = LayoutInflater.from(PedidosActivity.this);
        View v = ly.inflate(R.layout.dialog_bonificacion_colores, null);
        ListView lv_bonificacionColores= (ListView)v.findViewById(R.id.lv_bonificacionColores);

        //si no hay datos previos, enviar el arreglo "vacio" al adaptador
        if(dbclass.getCantidad_Detalle_Entrega(Oc_numero, codPro)<1){
            arregloEntrega=filas;
        }
        //si hay datos previos, enviar el arreglo recuperado al adaptador
        else {
            arregloEntrega= dbclass.obtenerListadoDetalleEntrega(Oc_numero, codPro);
        }
        adp=new Adapter_Detalle_Entrega(arregloEntrega, PedidosActivity.this,cantidadTotal,Oc_numero,codPro);
        final String codProd=codPro;
        lv_bonificacionColores.setAdapter(adp);
        Log.w("ARREGLO RECUPERADO",jsonn.toJson(arregloEntrega));
        AlertDialog.Builder dialogEnt = new AlertDialog.Builder(PedidosActivity.this);
        dialogEnt.setView(v);

        dialogEnt
                .setPositiveButton("Guardar",null)
                .setNeutralButton("Cancelar", null)
                .setNegativeButton("Borrar Datos",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dbclass.eliminar_detalle_entrega(Oc_numero, codProd);
                                dialog.dismiss();
                            }
                        });

        final int cantidad=cantidadTotal;
        final AlertDialog alert= dialogEnt.create();
        alert.show();
        alert.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        Button bt = alert.getButton(AlertDialog.BUTTON_POSITIVE);
        bt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Gson json= new Gson();
                //arreglo recuperado DEL ADAPTADOR
                Log.w("ARREGLO LLENO 2",json.toJson(arregloEntrega));
                //Log.w("cant ing rec ",""+cantIng);
                int cantIn=0;
                for(int i=0;i<arregloEntrega.size();i++){
                    cantIn=arregloEntrega.get(i).getCantidad()+cantIn;
                }
                Log.w("CANT ING2",""+cantIn);

                if(cantIn!=cantidad){
                    GlobalFunctions.showCustomToastShort(PedidosActivity.this, "La Cantidad Ingresada no coincide",
                            GlobalFunctions.TOAST_ERROR,GlobalFunctions.POSICION_BOTTOM);

                }
                else{
                    //obteniendo todas filas del arreglo recuperado siempre y cuando se haya presionado el boton "OK"
                    for(int i=0;i<arregloEntrega.size();i++){

                        if(!arregloEntrega.get(i).getOc_numero().equals("")){
                            DB_Detalle_Entrega fila= new DB_Detalle_Entrega();

                            fila.setOc_numero(Oc_numero);
                            fila.setCip(codProd);
                            fila.setNroEntrega(arregloEntrega.get(i).getNroEntrega());
                            fila.setDt_fechaEntrega(arregloEntrega.get(i).getDt_fechaEntrega());
                            fila.setCantidad(arregloEntrega.get(i).getCantidad());
                            if(arregloEntrega.get(i).getCodTurno().equals("Mañana")){
                                fila.setCodTurno("01");
                            }
                            else {
                                fila.setCodTurno("02");
                            }
                            fila.setEstado(arregloEntrega.get(i).getEstado());
                            //Agregando las filas recuperadas a la tabla pedido_detalle_entrega
                            dbclass.AgregarDetalleEntrega(fila, Oc_numero, codProd,arregloEntrega.get(i).getNroEntrega()+"");
                        }
                    }

                    alert.dismiss();
                }

            }
        });

        Button btCancelar = alert.getButton(AlertDialog.BUTTON_NEUTRAL);
        btCancelar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                alert.dismiss();

            }
        });
    }

    //---FIN DIALOG ENTREGAS---------

    public void verDialogEntregas_copiaSeguridad(ArrayList<DB_Detalle_Entrega> filas,final MenuItem itemenu, String versionAndroid){

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) itemenu.getMenuInfo();
        final ItemProducto prod = (ItemProducto) lstProductos.getAdapter().getItem(info.position);
        int cantidadTotal=prod.getCantidad();

        Adapter_Detalle_Entrega adp;

        final ArrayList<DB_Detalle_Entrega> arregloEntrega;

        Gson jsonn = new Gson();

        LayoutInflater ly = LayoutInflater.from(PedidosActivity.this);
        View v = ly.inflate(R.layout.dialog_bonificacion_colores, null);
        ListView lv_bonificacionColores= (ListView)v.findViewById(R.id.lv_bonificacionColores);

        //si no hay datos previos, enviar el arreglo "vacio" al adaptador
        if(dbclass.getCantidad_Detalle_Entrega(Oc_numero, prod.getCodprod())<1){
            arregloEntrega=filas;
        }
        //si hay datos previos, enviar el arreglo recuperado al adaptador
        else {
            arregloEntrega= dbclass.obtenerListadoDetalleEntrega(Oc_numero, prod.getCodprod());
        }
        adp=new Adapter_Detalle_Entrega(arregloEntrega, PedidosActivity.this,cantidadTotal,Oc_numero,prod.getCodprod());

        lv_bonificacionColores.setAdapter(adp);
        Log.w("ARREGLO RECUPERADO",jsonn.toJson(arregloEntrega));
        AlertDialog.Builder dialogEnt = new AlertDialog.Builder(PedidosActivity.this);
        dialogEnt.setView(v);

        dialogEnt
                .setPositiveButton("Guardar",null)
                .setNeutralButton("Cancelar", null)
                .setNegativeButton("Borrar Datos",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dbclass.eliminar_detalle_entrega(Oc_numero, prod.getCodprod());
                                dialog.dismiss();
                            }
                        });

        final AlertDialog alert= dialogEnt.create();
        alert.show();
        alert.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        Button bt = alert.getButton(AlertDialog.BUTTON_POSITIVE);
        bt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Gson json= new Gson();
                //arreglo recuperado DEL ADAPTADOR
                Log.w("ARREGLO LLENO 2",json.toJson(arregloEntrega));
                //Log.w("cant ing rec ",""+cantIng);
                int cantIn=0;
                for(int i=0;i<arregloEntrega.size();i++){
                    cantIn=arregloEntrega.get(i).getCantidad()+cantIn;
                }
                Log.w("CANT ING2",""+cantIn);

                if(cantIn!=prod.getCantidad()){
                    GlobalFunctions.showCustomToastShort(PedidosActivity.this, "La Cantidad Ingresada no coincide",
                            GlobalFunctions.TOAST_ERROR,GlobalFunctions.POSICION_BOTTOM);

                }
                else{
                    //obteniendo todas filas del arreglo recuperado siempre y cuando se haya presionado el boton "OK"
                    for(int i=0;i<arregloEntrega.size();i++){

                        if(!arregloEntrega.get(i).getOc_numero().equals("")){
                            DB_Detalle_Entrega fila= new DB_Detalle_Entrega();

                            fila.setOc_numero(Oc_numero);
                            fila.setCip(prod.getCodprod());
                            fila.setNroEntrega(arregloEntrega.get(i).getNroEntrega());
                            fila.setDt_fechaEntrega(arregloEntrega.get(i).getDt_fechaEntrega());
                            fila.setCantidad(arregloEntrega.get(i).getCantidad());
                            if(arregloEntrega.get(i).getCodTurno().equals("Mañana")){
                                fila.setCodTurno("01");
                            }
                            else {
                                fila.setCodTurno("02");
                            }
                            fila.setEstado(arregloEntrega.get(i).getEstado());
                            //Agregando las filas recuperadas a la tabla pedido_detalle_entrega
                            dbclass.AgregarDetalleEntrega(fila, Oc_numero, prod.getCodprod(),arregloEntrega.get(i).getNroEntrega()+"");
                        }
                    }

                    alert.dismiss();
                }

            }
        });

        Button btCancelar = alert.getButton(AlertDialog.BUTTON_NEUTRAL);
        btCancelar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                alert.dismiss();

            }
        });
    }


    public void actualizarBonificacion(ItemProducto bonificacion, int cantidadEntregada,int saldoAnterior){
        String codigoAnteriorPendiente = DAOBonificaciones.getRegistroAnteriorPendiente(codcli, bonificacion.getCodprod());
        DB_RegistroBonificaciones registroAnteriorPendiente = DAOBonificaciones.getRegistroBonificacion(codigoAnteriorPendiente);//Ultimo registro con todos los datos (con saldo pendiente)

        String codigoUltimoRegistro = DAOBonificaciones.getUltimoRegistroBonificacion(Oc_numero,bonificacion.getCodprod());
        DB_RegistroBonificaciones registroUltimoPedido = DAOBonificaciones.getRegistroBonificacion(codigoUltimoRegistro);//Ultimo registro del pedido actual tenga o no saldo pendiente

        int cantidadTotal = DAOBonificaciones.obtenerCantidadBonificacion(Oc_numero,bonificacion.getCodprod(), Integer.MIN_VALUE);
        if (saldoAnterior > 0) {
            //Si el saldo es mayor a 0 el registro anterior pendiente existe
            if (registroAnteriorPendiente.getOc_numero().equals(registroUltimoPedido.getOc_numero())) {
                //Si ambos son del mismo pedido los datos se mantien y no se aumenta el saldo anterior, eso solo sucede cuando se jala un saldo pendiente de un pedido anterior
                DAOBonificaciones.Actualizar_RegistroBonificacion(codigoUltimoRegistro,cantidadTotal,registroUltimoPedido.getSaldoAnterior(),cantidadEntregada,(cantidadTotal+registroUltimoPedido.getSaldoAnterior()-cantidadEntregada),registroUltimoPedido.getCodigoAnterior(),codven,codcli);
            }else{
                DAOBonificaciones.Actualizar_saldoBonificacion(registroAnteriorPendiente.getCodigoRegistro(), 0);
                DAOBonificaciones.Actualizar_RegistroBonificacion(codigoUltimoRegistro,cantidadTotal,saldoAnterior,cantidadEntregada,(cantidadTotal+saldoAnterior-cantidadEntregada),codigoAnteriorPendiente,codven,codcli);
            }
        }else{
            //Si el saldo es 0 no existe un registro anterior pendiente y se debe mantener los datos del ultimo registro pedido
            DAOBonificaciones.Actualizar_RegistroBonificacion(codigoUltimoRegistro,cantidadTotal,registroUltimoPedido.getSaldoAnterior(),cantidadEntregada,(cantidadTotal+registroUltimoPedido.getSaldoAnterior()-cantidadEntregada),registroUltimoPedido.getCodigoAnterior(),codven,codcli);
        }

        dbclass.actualizarItem_promo(
                Oc_numero,
                registroUltimoPedido.getSalida(),
                dbclass.obtener_codunimedXtipo_unimed_salida(registroUltimoPedido.getTipo_unimedSalida(),registroUltimoPedido.getSalida()),
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
                DAOBonificaciones.Actualizar_RegistroBonificacion(codigoAnteriorPendiente,(saldoAnterior+agregadoNulo.getCantidadEntregada()),saldoCompleto,agregadoNulo.getCodigoAnterior());
            }else{
                //Se mantiene el codigoAnterior
                DAOBonificaciones.Actualizar_RegistroBonificacion(codigoAnteriorPendiente,(saldoAnterior+agregadoNulo.getCantidadEntregada()),saldoCompleto);
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

    public static String calcularSecuencia(String oc, String fecha_configuracionx) {
        String cero = "0";
        String orden = "";

        // obtengo la fecha de la tabla configuracion
        // String fecha_configuracion = dbclass.getCambio("Fecha");

        // String mes_actual=(calendar.get(Calendar.MONTH)+1)+"";
        // String dia_actual=calendar.get(Calendar.DAY_OF_MONTH)+"";
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
        if (dia_actual.length() < 2) {
            dia_actual = cero + dia_actual;
        }
        if (secactual < 10) {
            //orden = mes_actual + dia_actual + cero + secactual;
            orden = anio_actual + mes_actual + dia_actual + cero + secactual;
        } else {
            //orden = mes_actual + dia_actual + secactual;
            orden = anio_actual + mes_actual + dia_actual + secactual;
        }

        return orden;
    }

    public static String calcularSecuenciaX(String oc,String fecha_configuracionx) {
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

                String busqueda = data.getStringExtra("busqueda");
                if (busqueda.equals("BONIFICACION")) {
                    String codigoRegistro 	= data.getStringExtra("codigoRegistro");
                    String codigoSalida 	= data.getStringExtra("codigoSalida");
                    int saldoUsado 			= data.getIntExtra("saldoUsado",0);
                    int saldo 				= data.getIntExtra("saldoPendiente",0);
                    Log.d("PedidosActivity", "codigoRegistro: "+codigoRegistro);
                    Log.d("PedidosActivity", "saldoUsado: "+saldoUsado);
                    itemBonificacion ++;

                    DB_RegistroBonificaciones agregadoNulo = DAOBonificaciones.getPendienteAgregado(Oc_numero, codigoSalida);
                    if(agregadoNulo.getOc_numero() != null ){

                        DAOBonificaciones.ActualizarRegistroBonificacionPendiente(Oc_numero, codigoSalida, (agregadoNulo.getCantidadEntregada() + saldoUsado));
                        DAOBonificaciones.Actualizar_saldoBonificacion(codigoRegistro, (saldo-saldoUsado));
                        DAOPedidoDetalle.Modificar_CantidadItemPedido(Oc_numero, codigoSalida,(agregadoNulo.getCantidadEntregada() + saldoUsado) );
                        mostrarListaProductos("");
                    }else{
                        DAOBonificaciones.AgregarRegistroBonificacionPendiente(Oc_numero,(Oc_numero+itemBonificacion),codigoSalida, codigoRegistro, saldoUsado, codven, codcli);
                        DAOBonificaciones.Actualizar_saldoBonificacion(codigoRegistro, (saldo-saldoUsado));
                        mostrarListaProductos(codigoSalida);
                    }

                    //Al mostrar la lista de productos se debe actualizar o agregar el producto pendiente agregado, de otra forma podra ser tomado en cuenta al momento de recalcular bonificaciones

                }else{
                    String requestAccionProducto = data.getStringExtra(ProductoActivity.REQUEST_ACCION_PRODUCTO_KEY);
                    String descripcion 		= data.getStringExtra("descripcion");
                    final String codprod 	= data.getStringExtra("codigoProducto");
                    final int nro_item 	= data.getIntExtra("item", 0);
                    String desunimed 		= data.getStringExtra("desunimed");
                    String unidad_medida 	= dbclass.obtener_codXdesunimed(codprod,desunimed);
                    double peso 			= data.getDoubleExtra("peso", 0.0);
                    int cantidad 			= data.getIntExtra("Cantidad", 0);
                    final double percepcion = data.getDoubleExtra("percepcion", 0.0);
                    double precioPercepcion = data.getDoubleExtra("precioPercepcion", 0.0);
                    int fact_conv 			= data.getIntExtra("fact_conv", 0);
                    final double precio 	= data.getDoubleExtra("precioUnidad", 0.0);
                    final String precioLista= data.getStringExtra("precioLista");
                    final double porcentaje_desc= data.getDoubleExtra("porcentaje_desc", 0);
                    final double porcentaje_desc_extra= data.getDoubleExtra("porcentaje_desc_extra", 0);
                    final boolean agregarComoBonificacion= data.getBooleanExtra("agregarComoBonificacion", false);


                    //String sec_politica = data.getStringExtra("sec_politica");

                    double precioNetoLista= VARIABLES.getDoubleFormaterThreeDecimal(Double.parseDouble(precioLista) * cantidad);
                    String subtotal 		= ""+VARIABLES.getDoubleFormaterThreeDecimal(precio*cantidad);
                    String subtotal_peso 	= ""+VARIABLES.getDoubleFormaterThreeDecimal(peso*cantidad);
                    String percepcionxCantidad = ""+VARIABLES.getDoubleFormaterThreeDecimal(precioPercepcion * cantidad);

                    String descuento = ""+VARIABLES.getDoubleFormaterThreeDecimal(precioNetoLista - Double.parseDouble(subtotal));

                    Log.d("onActivityResult", "org: "+requestAccionProducto);
                    Log.d("onActivityResult", "descripcion: "+descripcion);
                    Log.d("onActivityResult", "codprod: "+codprod);
                    Log.d("onActivityResult", "desunimed"+desunimed);
                    Log.d("onActivityResult", "unidad_medida: "+unidad_medida);
                    Log.d("onActivityResult", "peso: "+peso);
                    Log.d("onActivityResult", "cantidad: "+cantidad);
                    Log.d("onActivityResult", "percepcion: "+percepcion);
                    Log.d("onActivityResult", "precioPercepcion: "+precioPercepcion);
                    Log.d("onActivityResult", "fact_conv: "+fact_conv);
                    Log.d("onActivityResult", "precio: "+precio);
                    Log.d("onActivityResult", "precioLista: "+precioLista);
                    Log.d("onActivityResult", "descuento: "+descuento);
                    Log.d("onActivityResult", "subtotal: "+subtotal);
                    Log.d("onActivityResult", "subtotal_peso: "+subtotal_peso);
                    Log.d("onActivityResult", "percepcionxCantidad: "+percepcionxCantidad);

                    String w_codpro_inser= codprod;
                    String tipoProducto = "V";
                    if(agregarComoBonificacion){
                        //Si es bonificacion se debe agregar con el codigo de producto de bonificacion (BONI+CODPROD
                        w_codpro_inser = DBPedido_Detalle.PREFIX_PRODUCTO_BONIFICACION_MANUAL+codprod;
                        tipoProducto= "C";
                    }
                    if (requestAccionProducto.equals(ProductoActivity.REQUEST_ACCION_MODIFICAR_PRODUCTO)) {
                        //ELIMAMOS EL ITEM PRODUCTO PARA QUE LUEGO SE CREAR UNO NUEVO
                        ItemProducto itemProd=dbclass.obtenerListadoProductos_pedidoBY(Oc_numero, w_codpro_inser, nro_item);
                        boolean isEliminar=true;
                        eliminarOrUpdateProducto(itemProd, isEliminar);
                    }

                    PUEDE_AGREGAR=dbclass.isNotRegistradoProductoEItem(Oc_numero,w_codpro_inser, nro_item);

                    if (!PUEDE_AGREGAR) {
                        Toast.makeText(getApplicationContext(),"producto ya registrado",Toast.LENGTH_LONG).show();
                    }else PUEDE_AGREGAR=true;

                    if (PUEDE_AGREGAR) {
                        //-------------------------------Eliminamos si ya tiene promocion (solo promocion)---------------------
                        boolean isEliminar=false;
                        ItemProducto itemProducto=new ItemProducto();
                        itemProducto.setCodprod(w_codpro_inser);
                        eliminarOrUpdateProducto(itemProducto, isEliminar);
                        //-------------------------------FIN Eliminamos si ya tiene promocion---------------------

                        DBPedido_Detalle itemDetalle = new DBPedido_Detalle();
                        itemDetalle.setOc_numero(edt_nroPedido.getText().toString());
                        itemDetalle.setCip(w_codpro_inser);
                        itemDetalle.setEan_item("");
                        itemDetalle.setPrecio_bruto(precio + "");
                        itemDetalle.setPercepcion(percepcionxCantidad);

                        itemDetalle.setPrecio_neto(subtotal);
                        itemDetalle.setCantidad(cantidad);
                        itemDetalle.setTipo_producto(tipoProducto);
                        itemDetalle.setUnidad_medida(unidad_medida);
                        itemDetalle.setPeso_bruto(subtotal_peso);
                        itemDetalle.setFlag("N");
                        itemDetalle.setPrecioLista(""+precioLista);
                        itemDetalle.setDescuento(""+descuento);
                        itemDetalle.setPorcentaje_desc(porcentaje_desc);
                        itemDetalle.setPorcentaje_desc_extra(porcentaje_desc_extra);
                        //itemDetalle.setCod_politica(sec_politica);
                        //Campos usados para devoluciones
                        itemDetalle.setLote("");
                        itemDetalle.setMotivoDevolucion("");
                        itemDetalle.setExpectativa("");
                        itemDetalle.setEnvase("");
                        itemDetalle.setContenido("");
                        itemDetalle.setProceso("");
                        itemDetalle.setObservacionDevolucion("");
                        itemDetalle.setTipoDocumento("");
                        itemDetalle.setTipoDocumento("");
                        itemDetalle.setSerieDevolucion("");
                        itemDetalle.setNumeroDevolucion("");
                        itemDetalle.setSec_promo("");
                        itemDetalle.setItem_promo(0);
                        itemDetalle.setSec_promo_prioridad(0);
                        itemDetalle.setItem_promo_prioridad(0);

                        dbclass.AgregarPedidoDetallePrincipal(itemDetalle, nro_item);
                    }
                    ObtenerBonificaciones(w_codpro_inser, nro_item, descripcion);
                    mostrarListaProductos("");
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
        Log.i(TAG, "Verificar codigoLetraCondicionVenta:  "+codigoLetraCondicionVenta);

        Log.d("fechaEntregaCompleta", fechaEntregaCompleta);
        itemCabecera.setOc_numero(edt_nroPedido.getText().toString());
        itemCabecera.setSitio_enfa("0");
        itemCabecera.setMonto_total("");
        itemCabecera.setPercepcion_total("");
        itemCabecera.setValor_igv("0.0");
        itemCabecera.setMoneda(codigoMoneda);

        itemCabecera.setFecha_oc(GlobalFunctions.getFechaActual());
        Log.d("fechaActual", itemCabecera.getFecha_oc());
        itemCabecera.setFecha_mxe(fechaEntregaCompleta); //fechaEntrega
        itemCabecera.setCond_pago(codigoCondicionVenta);
        itemCabecera.setCod_cli(codcli);
        itemCabecera.setCod_emp(codven);
        itemCabecera.setEstado("G");
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
        itemCabecera.setNumeroOrdenCompra(nroOrdenCompra);
        itemCabecera.setCodigoPrioridad(codigoPrioridad);
        itemCabecera.setCodigoSucursal(codigoSucursal);
        itemCabecera.setCodigoPuntoEntrega(codigoLugarEntrega);
        itemCabecera.setCodigoTipoDespacho(codigoTipoDespacho);
        itemCabecera.setFlagEmbalaje(flagEmbalaje);
        itemCabecera.setFlagPedido_Anticipo(flagPedidoAnticipo);
        itemCabecera.setCodigoTransportista(codigoTransportista);
        itemCabecera.setCodigoAlmacen(codigoAlmacenDespacho);
        itemCabecera.setObservacion2(observacion2);
        itemCabecera.setObservacion3(observacion3);
        itemCabecera.setObservacionDescuento(observacionDescuento);
        itemCabecera.setObservacionTipoProducto(observacionTipoProducto);
        itemCabecera.setFlagDescuento(flagDescuento);
        itemCabecera.setCodigoObra(codigoObra);//No importa si esta vacio
        itemCabecera.setFlagDespacho(spn_despacho.getSelectedItem().toString());
        itemCabecera.setDocAdicional(docAdicional);
        Log.d("Tipo Documento", ""+tipoDocumento);
        Log.d("setObservacion2", "observacion2:"+observacion2.length());
        Log.d("setObservacion3", "observacion3:"+observacion3.length());
        Log.d("setObservacion4", "observacion4:"+observacion4.length());
        itemCabecera.setTipoDocumento(tipoDocumento);
        itemCabecera.setTipoRegistro(TIPO_REGISTRO);
        itemCabecera.setDiasVigencia(diasVigencia);//Solo para el caso Cotizacion
        itemCabecera.setCodTurno(codigoTurno);
        itemCabecera.setNroletra(codigoLetraCondicionVenta);
        itemCabecera.setObservacion4(observacion4);

        if (origen.equals("CLIENTESAC")) {
            dbclass.Actualizar_pedido_cabecera(itemCabecera);
        } else if (origen.equals("REPORTES-MODIFICAR")) {
            DAOPedidoDetalle.ActualizarPedidoCabecera(itemCabecera);
        } else{
            if (cabeceraRegistrada) {
                DAOPedidoDetalle.ActualizarPedidoCabecera(itemCabecera);
            }else{
                dbclass.AgregarPedidoCabecera(itemCabecera);
            }
        }
        // dbclass.actualizarEstadoCliente(codcli, "P");
        cabeceraRegistrada = true;

    }

//    public void cargarClientesXRuta() {
//        ArrayList<DBClientes> lista_clientes_ruta = new ArrayList<DBClientes>();
//        int i = 0;
//        lista_clientes_ruta = dbclass.getClientesXRuta();
//        String[] lclientes = new String[lista_clientes_ruta.size()];
//        Iterator<DBClientes> it = lista_clientes_ruta.iterator();
//
//        while (it.hasNext()) {
//            Object objeto = it.next();
//            DBClientes cta = (DBClientes) objeto;
//
//            lclientes[i] = cta.getNomcli();
//
//            i++;
//
//        }
//
//        if (lclientes.length > 0) {
//
//            autocomplete.setText(lclientes[0]);
//            codcli = obtenerCodigoCliente(lclientes[0]);
//
//            mostrarDatosCliente(lclientes[0]);
//        }
//
//        else {
//            autocomplete.setHint("Ingrese cliente");
//        }
//
//    }

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

        final Dialog dialogo = new Dialog(this,android.R.style.Theme_Light);

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

                    dbclass.Anular_pedido(edt_nroPedido.getText().toString(),item.getCod_noventa());

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

    void showDialogSalir(){
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Pedido")
                .setMessage("Se perderan todos los datos")
                .setPositiveButton("Confirmar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,int which) {
                                //Se restaura los saldos pendientes de las bonificaciones pasadas usadas
                                RestaurarSaldosPendientes();
                                dbclass.eliminar_pedido(Oc_numero);
                                DAOBonificaciones.Eliminar_RegistrosBonificacion(Oc_numero);
                                dao_pedido_detalle2.LimpiarTabla(Oc_numero);
                                //dbclass.actualizarEstadoCliente(codcli, "S");
                                finish();

                            }

                        }).setNegativeButton("Cancelar", null).show();
    }

    @Override
    public void onBackPressed() {
        if (origen.equals("REPORTES-PEDIDO")) {
            finish();
        }else if (origen.equals("REPORTES-MODIFICAR")) {
            if (TIPO_REGISTRO.equals(TIPO_COTIZACION)) {
                showDialogSalir();
            }else{
                GlobalFunctions.showCustomToast(
                        PedidosActivity.this, "Debes guardar los cambios",
                        GlobalFunctions.TOAST_ERROR);
                //finish(); Si es un Pedido obligar a guardar cambios
            }
        } else {
            showDialogSalir();
        }
    }

    void RestaurarSaldosPendientes(){
        ArrayList<DB_RegistroBonificaciones> lista = DAOBonificaciones.getRegistrosUsanPendientes(Oc_numero);
        for (int i = 0; i < lista.size(); i++) {
            DAOBonificaciones.Actualizar_saldoBonificacion(lista.get(i).getCodigoAnterior(), lista.get(i).getSaldoAnterior());
        }

        //Devolver las cantidadesEntregadas por los nulosAgregados
        ArrayList<DB_RegistroBonificaciones> listaNulos = DAOBonificaciones.getRegistrosUsanPendientesNulos(Oc_numero);
        for (int i = 0; i < listaNulos.size(); i++) {
            DB_RegistroBonificaciones registroUsado = DAOBonificaciones.getRegistroBonificacion(listaNulos.get(i).getCodigoAnterior());
            DAOBonificaciones.Actualizar_saldoBonificacion(registroUsado.getCodigoRegistro(), (registroUsado.getSaldo()+listaNulos.get(i).getCantidadEntregada()));
            Log.d("PedidosActivity", "Saldo devuelto por "+listaNulos.get(i).getSalida()+" al codigoRegistro:"+registroUsado.getCodigoRegistro()+" saldo total:"+(registroUsado.getSaldo()+listaNulos.get(i).getCantidadEntregada()));
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

            pDialog = new ProgressDialog(PedidosActivity.this);
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


            if (1==1/*cd.hasActiveInternetConnection(getApplicationContext())*/) {

                try {
                    valor = soap_manager.actualizarObjPedido_directo(Oc_numero);

                } catch (JsonParseException ex) {

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

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
    }

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

    /* Remove the locationlistener updates when Activity is paused */

    protected void onPause() {
        super.onPause();

        if (taskCheckUbicacion!=null){
            taskCheckUbicacion.RemoveLocation();
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

        ArrayList<DBPedido_Detalle> lista_detallePedido = dbclass.getPedidosDetallexOc_numero(Oc_numero);
        ArrayList<DB_PromocionDetalle> lista_acumulados = dbclass.obtenerListaAcumulados(itemPromocion.getItem(),itemPromocion.getSecuencia());

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
                    boolean addCompuestoTotal=false, addCompuestoParcial=false;
                    int cantidadDisponible = DAOBonificaciones.getCantidadDisponible(Oc_numero,dbPedido_Detalle.getCip(), db_PromocionDetalle, dbPedido_Detalle.getItem());
                    double montoDisponible = DAOBonificaciones.getMontoDisponible(Oc_numero,dbPedido_Detalle.getCip(),db_PromocionDetalle, dbPedido_Detalle.getItem());
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
                        addCompuestoTotal=true;

                    } else if (cantidadDisponible > 0 || montoDisponible > 0.0) {
                        // EL PRODUCTO TIENE DISPONIBILIDAD (ESTA REGISTRADO)
                        addCompuestoParcial= true;
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

                    if (addCompuestoTotal || addCompuestoParcial) {
                        entradasCompuestas = new String[9];
                        entradasCompuestas[0] = db_PromocionDetalle.getEntrada();
                        entradasCompuestas[1] = String.valueOf(cantidadDisponible);
                        entradasCompuestas[2] = String.valueOf(montoDisponible);
                        entradasCompuestas[3] = String.valueOf(tipo_unidad_medida);
                        entradasCompuestas[4] = String.valueOf(dbPedido_Detalle.getUnidad_medida());
                        entradasCompuestas[5] = String.valueOf(db_PromocionDetalle.getAgrupado());
                        entradasCompuestas[6] = String.valueOf(dbPedido_Detalle.getCantidad());
                        entradasCompuestas[7] = String.valueOf(dbPedido_Detalle.getPrecio_neto());
                        entradasCompuestas[8] = String.valueOf(dbPedido_Detalle.getItem());
                        Log.e("", "cantidad entrada entradasCompuestas[1]:   " + cantidadUnidadesMin);
                        listaEntradasCompuestas.add(entradasCompuestas);
                    }

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

        ArrayList<DBPedido_Detalle> lista_detallePedido = dbclass.getPedidosDetallexOc_numero(Oc_numero);
        ArrayList<Integer> cantidadesIndividual = new ArrayList<Integer>();

        int contador = 0;

        /*
         *  ******** PROMOCION ACUMULADO MIXTO
         * ***********************************
         * ***********************************
         * ************************************************************
         */

        ArrayList<DB_PromocionDetalle> lista_acumuladosAND = DAOPromocionDetalle .getAcumuladosAND(itemPromocion);
        ArrayList<DB_PromocionDetalle> lista_acumuladosOR = DAOPromocionDetalle.getAcumuladosOR(itemPromocion);

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
                    int cantidadDisponible = DAOBonificaciones.getCantidadDisponible(Oc_numero,pedidoDetalle.getCip(), promocionAND, pedidoDetalle.getItem());
                    double montoDisponible = DAOBonificaciones.getMontoDisponible(Oc_numero,pedidoDetalle.getCip(), promocionAND, pedidoDetalle.getItem());
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

                        entradasCompuestas = new String[9];
                        entradasCompuestas[0] = promocionAND.getEntrada();
                        entradasCompuestas[1] = String.valueOf(cantidadDisponible);
                        entradasCompuestas[2] = String.valueOf(montoDisponible);
                        entradasCompuestas[3] = String.valueOf(tipo_unidad_medida);
                        entradasCompuestas[4] = String.valueOf(pedidoDetalle.getUnidad_medida());
                        entradasCompuestas[5] = String.valueOf(promocionAND.getAgrupado());
                        entradasCompuestas[6] = String.valueOf(pedidoDetalle.getCantidad());
                        entradasCompuestas[7] = String.valueOf(pedidoDetalle.getPrecio_neto());
                        entradasCompuestas[8] = String.valueOf(pedidoDetalle.getItem());
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
                                .getCantidadDisponible(Oc_numero,pedidoDetalle2.getCip(), promocionOR,pedidoDetalle2.getItem() );
                        double montoDisponible = DAOBonificaciones
                                .getMontoDisponible(Oc_numero,
                                        pedidoDetalle2.getCip(), promocionOR, pedidoDetalle2.getItem());
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

                        entradasCompuestas = new String[9];
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
                        entradasCompuestas[7] = String.valueOf(pedidoDetalle2.getPrecio_neto());
                        entradasCompuestas[8] = String.valueOf(pedidoDetalle2.getItem());
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
                                        AuxItemDetalle.getCip(), AuxItemPromo, AuxItemDetalle.getItem());
                        double montoDisponible = DAOBonificaciones
                                .getMontoDisponible(Oc_numero,
                                        AuxItemDetalle.getCip(), AuxItemPromo, AuxItemDetalle.getItem());
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

                        int cantidadDisponible = DAOBonificaciones.getCantidadDisponible(Oc_numero,AuxItemDetalle.getCip(), AuxItemPromo, AuxItemDetalle.getItem());
                        double montoDisponible = DAOBonificaciones.getMontoDisponible(Oc_numero,AuxItemDetalle.getCip(),AuxItemPromo, AuxItemDetalle.getItem());
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

                            entradasCompuestas = new String[9];
                            entradasCompuestas[0] = AuxItemPromo.getEntrada();
                            entradasCompuestas[1] = String.valueOf(cantidadDisponible);
                            entradasCompuestas[2] = String.valueOf(montoDisponible);
                            entradasCompuestas[3] = String.valueOf(tipo_unidad_medida);
                            entradasCompuestas[4] = String.valueOf(AuxItemDetalle.getUnidad_medida());
                            entradasCompuestas[5] = String.valueOf(AuxItemPromo.getAgrupado());
                            entradasCompuestas[6] = String.valueOf(AuxItemDetalle.getCantidad());
                            entradasCompuestas[7] = String.valueOf(AuxItemDetalle.getPrecio_neto());
                            entradasCompuestas[8] = String.valueOf(AuxItemDetalle.getItem());
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

                            int cantidadDisponible = DAOBonificaciones.getCantidadDisponible(Oc_numero,
                                            AuxItemDetalle.getCip(), AuxItemPromo, AuxItemDetalle.getItem());
                            double montoDisponible = DAOBonificaciones.getMontoDisponible(Oc_numero,
                                            AuxItemDetalle.getCip(), AuxItemPromo, AuxItemDetalle.getItem());
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
                    Oc_numero, itemDetalle.getCip(), itemPromocion, itemDetalle.getItem());
            double montoDisponible = DAOBonificaciones.getMontoDisponible(
                    Oc_numero, itemDetalle.getCip(), itemPromocion, itemDetalle.getItem());

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

    public void ObtenerBonificaciones(String codprod, int nro_item, String descripcion) {
        productos.clear();
        ItemProducto[] producto;
        DBPedido_Detalle producto_a_verificar;
        Oc_numero = edt_nroPedido.getText().toString();
        producto = dbclass.obtenerListadoProductos_pedido(Oc_numero);

        if (producto.length > 0) {
            GUARDAR_ACTIVO = true;
        } else {
            GUARDAR_ACTIVO = false;
        }

        accionGlobal="agregar";
        producto_a_verificar = dbclass.getPedidosDetalleEntityWithItem(Oc_numero,codprod, nro_item);
        getProductoSalida(producto_a_verificar, descripcion);

    }

    public void mostrarListaProductos(String codigoSalida) {
        Log.d("PedidosActivity", "MOSTRANDO LISTA DE PRODUCTOS");
        //Si el parametro esta vacio significa el metodo fue invocado solo para visualizacion

        //Si el parametro tiene el codigoSalida significa que se ha realizado un movimiento
        //(se recalculó la salida sin tomar en cuenta la bonificacion pendiene agregada) con esa salida y se debe sumar la boniicacion pendiente agregada

        if (!codigoSalida.equals("")) {
            ActualizarBonificacionesPendientes(codigoSalida);
        }

        productos.clear();
        ItemProducto[] producto;

        Oc_numero = edt_nroPedido.getText().toString();

        double peso_total = 0.0d;
        double subtotal = 0.0d;

        double total = 0.0d;
        double percepcion = 0.0d;
        double totalCompleto = 0.0d;
        double totalSujetoPercepcion = 0.0d;
        double descuento = 0.0d;
        double montoTotalBonif = 0;
        double descuentoPercent = 0.0d;

        productos.clear();

        producto = dbclass.obtenerListadoProductos_pedido(Oc_numero);

        for (int i = 0; i < producto.length; i++) {
            productos.add(producto[i]);
            peso_total 	+= producto[i].getPeso();
            if(producto[i].getTipo().equals("C")){
                montoTotalBonif += producto[i].getSubtotal();
            }
            else{
                subtotal	+= producto[i].getSubtotal(); //Sin IGV
                percepcion 	+= producto[i].getPercepcionPedido();
                descuento	+= (producto[i].getPrecioLista() - producto[i].getPrecio())*producto[i].getCantidad();
                Log.d("DESCUENTO", producto[i].getPrecioLista() + " - " + producto[i].getPrecio()+ " * " + producto[i].getCantidad());
                descuentoPercent += 0;
                if (producto[i].getPercepcionPedido() != 0) {
                    totalSujetoPercepcion = totalSujetoPercepcion+ (producto[i].getSubtotal()*(1+valorIGV));
                }
            }
            dbclass.close();
        }
        if (dbclass.obtenerCantidadPedidoDetalle(Oc_numero) > 0) {
            rButtonSoles.setEnabled(false);
            rButtonDolares.setEnabled(false);
            spn_sucursal.setEnabled(false);
            spn_almacenDespacho.setEnabled(false);
            rButtonDescuentoSi.setEnabled(false);
            rButtonDescuentoNo.setEnabled(false);

        }else{
            rButtonSoles.setEnabled(true);
            rButtonDolares.setEnabled(true);
            spn_sucursal.setEnabled(true);
            spn_almacenDespacho.setEnabled(true);
            rButtonDescuentoSi.setEnabled(true);
            rButtonDescuentoNo.setEnabled(true);
        }

        peso_total 		= GlobalFunctions.redondear_toDouble(peso_total);
        ArrayList<Pedido_detalle2> listaDet2 = dao_pedido_detalle2.getDataByOcnumero(Oc_numero);
        double desctoBonifDet2 = 0;
        for (Pedido_detalle2 pedido_detalle2 : listaDet2) {
            desctoBonifDet2 += pedido_detalle2.getPrecio_neto();
        }
        double dsctoBonifi = GlobalFunctions.redondear_toDouble(montoTotalBonif + desctoBonifDet2);
        subtotal		= GlobalFunctions.redondear_toDouble(subtotal);
        double IGV		= GlobalFunctions.redondear_toDouble(subtotal*valorIGV);
        total			= GlobalFunctions.redondear_toDouble(subtotal + IGV);
        percepcion		= GlobalFunctions.redondear_toDouble(percepcion);
        totalCompleto	= GlobalFunctions.redondear_toDouble(total + percepcion);
        descuento		= GlobalFunctions.redondear_toDouble(descuento);

        //dbclass.GuardarMontoPeso_Pedido(total, peso_total, Oc_numero);
        //COLOREES
        if (codigoMoneda==null){
            EnvalularMoneda();
        }
        double valor_cambio=this.getTipoCambioPedido();
        Adapter_itemPedidoProducto adaptador = new Adapter_itemPedidoProducto(this, productos,Oc_numero, valor_cambio);
        lstProductos.setAdapter(adaptador);

        if (rButtonDolares.isChecked()) {
            tv_moneda.setText(rButtonDolares.getText());
        }else{
            tv_moneda.setText(rButtonSoles.getText());
        }
        String tipoCambio=dbclass.getConfiguracionByName("Tipo_cambio", "0");
        tvTipoCambio.setText(formaterMoneda.format(Double.parseDouble(tipoCambio)));
        tv_cantidadItems.setText(""+productos.size());
        tv_subTotal.setText(formaterMoneda.format(subtotal));
        tv_total.setText(formaterMoneda.format(total));
        tv_totalCompleto.setText(formaterMoneda.format(totalCompleto));
        tv_IGV.setText(formaterMoneda.format(IGV));
        tv_percepcion.setText(formaterMoneda.format(percepcion));
        tv_descuento.setText(formaterMoneda.format(descuento));
        tv_descuentoPorcentaje.setText(""+formaterMoneda.format(descuentoPercent)+"");

        double stotalReal=subtotal/valor_cambio;
        double precioXkilo=stotalReal /(peso_total>0.0?peso_total:stotalReal);
        tvPrecioPorKilo.setText(formaterMoneda.format(precioXkilo));

        //dbclass.GuardarMontoPesoPercepcion_Pedido(total, percepcion,peso_total, totalSujetoPercepcion, Oc_numero);
        PedidoCabeceraRecalcular dataRecalculo=new PedidoCabeceraRecalcular(
                Oc_numero,
                peso_total,
                subtotal,
                IGV,
                total,
                percepcion,
                totalSujetoPercepcion,
                descuento,
                descuentoPercent,
                dsctoBonifi
        );
        dbclass.guardarPedidoTotales(dataRecalculo);
        MostrarResumenByTipoProducto(dsctoBonifi);
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

                    UtilViewMensaje.MENSAJE_simple(this, "Bonificacion?","Intentas agregar una bonificación pendiente? No esta disponible por ahora eso ");
                    //                    agregarProductoxPromocion(
//                            lista.get(i).getSalida(),
//                            ""+0,
//                            lista.get(i).getCantidadEntregada(),
//                            );
                }
            }
        }
    }

    public void getProductoSalida(DBPedido_Detalle itemDetalle_ok,String descripcion) {
        // la cantidadCalculada es la transformada con el factor de conversion.

        int cantidadCalculada = VerificarSiTienePromocion(itemDetalle_ok.getCip(),itemDetalle_ok.getUnidad_medida(), itemDetalle_ok.getCod_politica(),itemDetalle_ok.getCantidad(),codigoUbigeo);

        if (cantidadCalculada > 0) {
            Iterator<DB_PromocionDetalle> it_itemDetallePromocion = al_PromocionDetalle.iterator(); // -> lista de promocion detalle (promociones con esa entrada)
            Log.d("PedidosActivity ::getProductoSalida2::","EL PRODUCTO: " + itemDetalle_ok.getCip() + " tiene "+ al_PromocionDetalle.size() + " promocion(es)");

            ArrayList<Integer> listaCantidades = new ArrayList<Integer>();
            ArrayList<DB_PromocionDetalle> listaPromociones = new ArrayList<DB_PromocionDetalle>();
            listaCantidadesUsadas = new ArrayList<ArrayList<Integer>>();
            listaMontosUsados = new ArrayList<ArrayList<Double>>();
            listaPromocionCompuesta = new ArrayList<ArrayList<String[]>>();

            while (it_itemDetallePromocion.hasNext()) {
                Object objeto = it_itemDetallePromocion.next();
                DB_PromocionDetalle itemPromocion = (DB_PromocionDetalle) objeto;
                DBPedido_Detalle itemDetalle= itemDetalle_ok.getNuevaInstancia(itemDetalle_ok);
                /*
                 * Acumulado 0 --> No
                 * Acumulado 1 --> Si
                 * Acumulado 2 --> Escalable
                 * Acumulado 3 --> Acumulado y Escalable
                 */

                int cantidadBonificacion = 0;
                listaEntradasCompuestas = new ArrayList<String[]>();

                if (itemPromocion.acumulado == 1) { // Si permite acumulado
                    if (DAOPromocionDetalle.isPromocionAcumuladoPuro(itemPromocion)) {
                        cantidadBonificacion = obtenerCantidadBonificacionAcumulado_Puro(itemPromocion, itemDetalle, Oc_numero);
                    } else {
                        cantidadBonificacion = obtenerCantidadBonificacionAcumulado_Combinado(itemPromocion, itemDetalle, Oc_numero);
                    }
                } else { // Si no se permite acumulado
                    Log.d("", "LA PROMOCION ES DE TIPO AGRUPADO O SIMPLE");
                    /*
                     * 1.- Verificar agrupado "Si tienen el mismo codigo"->& "Si tienen codigos distintos"-> ó 2.- Verificar tipo
                     * [C,M] ->Cantidad,Monto 3.- Verificar por condición
                     * [1,2,3] -> mayor o igual, menor o igual, por cada
                     */
                    cantidadBonificacion = obtenerCantidadBonificacion(itemPromocion, itemDetalle_ok, Oc_numero);
                }

                if (cantidadBonificacion > 0) {
                    /*
                     * Si la promocion es acumulado y de condicion 1 (mayor o
                     * igual que), la bonificacion solo se da una vez, al tener
                     * candidades sobrantes, insertar un producto de la
                     * promocion y cumplir la condicion vuelve a mostrar la
                     * bonificacion para elegir y sumar; esto se soluciona :
                     */

                    /*
                     * Quitar las promociones que sean acumulado y de condicion
                     * 1 que ya esten registrados (no agregarlas a la lista)
                     */
                    if (!DAOBonificaciones
                            .estaRegistradoAcumulado_1(itemPromocion)) {
                        /* Se guardan las promociones que cumplen condicion */
                        Log.d("", "agregando a la lista de promociones "
                                + itemPromocion.getEntrada() + "  salida: "
                                + itemPromocion.getSalida() + "  cantidad: "
                                + cantidadBonificacion);
                        listaPromociones.add(itemPromocion);
                        listaCantidades.add(cantidadBonificacion);
                        listaPromocionCompuesta.add(listaEntradasCompuestas);
                        listaCantidadesUsadas.add(cantidadesUsadas);
                        listaMontosUsados.add(montosUsados);
                        Log.e("",
                                "MONTOS USADOS PARA "
                                        + itemPromocion.getEntrada() + "  "
                                        + montosUsados.size());
                    }

                } else {
                    /*
                     * Antes de eliminar se debe verificar si es de tipo
                     * acumulado, donde los otros registros no se deben ver
                     * afectadpos
                     */
                    if (DAOBonificaciones
                            .estaRegistradoAcumulado_3(itemPromocion)) {

                    } else {

                        listaEntradasCompuestas = null;
                        /*
                         * La cantidad de bonificacion es cero, no se agrega o
                         * actualiza el producto bonificado Si la salida es cero
                         * entonces se deben elminar los registros bonificacion
                         * incluyendo las dependencias de la promocion ya que si
                         * se obtiene cero en cantidad no deben estar
                         * registrados en las bonificaciones
                         */

                        DAOBonificaciones
                                .Eliminar_RegistroBonificacion_Dependencias(
                                        Oc_numero,
                                        itemPromocion.getSecuencia(),
                                        itemPromocion.getItem(),
                                        itemPromocion.getAgrupado(),
                                        0);
                        int cantidad = DAOBonificaciones.obtenerCantidadBonificacion(Oc_numero,
                                        itemPromocion.getSalida(), Integer.MIN_VALUE);

                        if (cantidad == 0) {
                            // El producto no tiene cantidad, no debe ser
                            // mostrado
                            DAOPedidoDetalle.Eliminar_itemPedidoBonificacion(
                                    itemPromocion.getSalida(), Oc_numero, DAOBonificaciones, null, itemPromocion.getSecuencia(), 0);
                        } else {
                            DAOPedidoDetalle.Modificar_CantidadItemPedido(
                                    Oc_numero, itemPromocion.getSalida(),
                                    cantidad);
                        }
                        Log.w("PedidosActivity :getSalida:",
                                "no se agrego a la lista, es null");
                    }
                }
            }

            /*
             *  *****************************************************************
             * VERIFICACIÓN DE LISTA DE PROMOCIONES OBTENIDAS
             * *******************
             * *************************************************************
             */
            Log.d("", "empezando a verificar lista promociones ");
            Gson gson = new Gson();
            Log.d("-->","JSON "+ gson.toJson(listaPromociones) );

            if (listaPromociones.size() > 0) {

                if (listaPromociones.size() > 1) {
                    ArrayList<Model_bonificacion> listaBonif = new ArrayList<Model_bonificacion>();
                    ArrayList<Integer> items_tipoAgrupado = new ArrayList<Integer>();
                    ArrayList<Integer> listaEscalasBajas = new ArrayList<Integer>();
                    ArrayList<DB_PromocionDetalle> listaEscalasRemover = new ArrayList<DB_PromocionDetalle>();

                    /*
                     * AL TENER COINCIDENCIAS DE PROMOCIONES SE DEBE MOSTRAR UN
                     * DIALOGO PARA ELEGIR UNA DE ELLAS
                     */

                    /*
                     * ALGORITMO PARA OBTENER LOS INDICES DE LAS BONIFICACIONES
                     * ESCALABLES QUE SERAN QUITADAS DEL DIALOGO for (int i = 0;
                     * i < listaPromociones.size(); i++) { DB_PromocionDetalle
                     * promocionDetalle = listaPromociones.get(i);
                     * ArrayList<String []> listaEntradasCompuestas =
                     * listaPromocionCompuesta.get(i);
                     *
                     * if (promocionDetalle.getAcumulado() == 2 ||
                     * promocionDetalle.getAcumulado() == 3) { /*Si es escalable
                     * o acumulado escalable
                     *
                     * for(int n = (i+1); n < listaPromociones.size();n++){
                     * DB_PromocionDetalle promocionDetalle2 =
                     * listaPromociones.get(n);
                     *
                     * if(promocionDetalle2.getAcumulado() == 2 ||
                     * promocionDetalle2.getAcumulado() == 3){
                     *
                     * if((promocionDetalle.getSecuencia() ==
                     * promocionDetalle2.getSecuencia()) &&
                     * promocionDetalle.getItem() != promocionDetalle2.getItem()
                     * ){ ArrayList<String []> listaEntradasCompuestas2 =
                     * listaPromocionCompuesta.get(n);
                     *
                     * if(listaEntradasCompuestas.size() ==
                     * listaEntradasCompuestas2.size()){ int escala = 0; for(int
                     * x=0;x<listaEntradasCompuestas.size();x++){ String []
                     * entradaCompuesta = listaEntradasCompuestas.get(x);
                     * for(int xx=0;xx<listaEntradasCompuestas2.size();xx++){
                     * String [] entradaCompuesta2 =
                     * listaEntradasCompuestas.get(xx); if(entradaCompuesta[0]
                     * == entradaCompuesta2[0]){ escala++; } } } if (escala ==
                     * listaEntradasCompuestas.size()) { /*Guardamos el indice
                     * de la promocion escalable a quitar if
                     * (promocionDetalle.getCant_condicion() >
                     * promocionDetalle2.getCant_condicion()){
                     * listaEscalasBajas.add(n);
                     * listaEscalasRemover.add(promocionDetalle2); }else{
                     * listaEscalasBajas.add(i);
                     * listaEscalasRemover.add(promocionDetalle); } } } } } } }
                     * } /* ALGORITMO PARA OBTENER LOS INDICES DE LAS
                     * BONIFICACIONES ESCALABLES QUE SERAN QUITADAS DEL DIALOGO
                     */

                    /*
                     * for(int i=0;i<listaEscalasBajas.size();i++){ for (int j =
                     * 0; j < listaPromociones.size(); j++) {
                     * if(listaEscalasBajas
                     * .get(i).equals(listaPromociones.get(j)) ){
                     * Log.d("PedidosActivity :Quitar escalables bajos:"
                     * ,"lista Escala: "+i+"   listaPromociones: "+j);
                     * listaPromociones.remove(j); listaCantidades.remove(j);
                     * listaPromocionCompuesta.remove(j);
                     * listaCantidadesUsadas.remove(j);
                     * listaMontosUsados.remove(j); } } }
                     */

                    for (int i = 0; i < listaPromociones.size(); i++) {

                        DB_PromocionDetalle promocionDetalle = listaPromociones.get(i);
                        boolean esAgrupado = DAOBonificaciones.esAgrupado_acumulado(promocionDetalle);
                        if (esAgrupado == true) {
                            items_tipoAgrupado.add(i);
                        }
                        Model_bonificacion bonificacion = DAOBonificaciones
                                .getModelBonificacion(listaCantidades.get(i),
                                        listaPromociones.get(i).getSalida(),
                                        listaPromociones.get(i).getEntrada());
                        if(bonificacion==null){
                            Log.e(TAG, "El producto "+listaPromociones.get(i).getSalida()+ " de promocion no se ha encontrado en catalogo");
                            //Elinamos de la posicion el item
                            listaBonif.remove(i);
                            listaCantidades.remove(i);
                            listaPromociones.remove(i);
                            listaPromocionCompuesta.remove(i);
                            listaCantidadesUsadas.remove(i);
                            listaMontosUsados.remove(i);
                            i--;
                        }
                        else {
                            bonificacion.setPrioridad(listaPromociones.get(i).getPrioridad());
                            listaBonif.add(bonificacion);
                        }
                    }

                    //verificamos si hay mas de un prioridad
                    double cantCondicion=-1;
                    double MontoCondicion=-1;//1991, problema 1990
                    int posOldPrioridad=-1;//1991, problema 1990
                    for (int i=0; i<listaBonif.size();i++){

                        if (listaPromociones.get(i).getPrioridad()==1){
                            if (cantCondicion>-1 || MontoCondicion>-1){
                                int locamlRemove=i;
                                if (cantCondicion<listaPromociones.get(i).getCant_condicion()  || MontoCondicion <Double.parseDouble(listaPromociones.get(i).getMonto()) ){
                                    locamlRemove=posOldPrioridad;
                                    cantCondicion=listaPromociones.get(i).getCant_condicion();
                                    MontoCondicion=Double.parseDouble(listaPromociones.get(i).getMonto());

                                    posOldPrioridad=i-1;
                                }
                                //Elinamos de la posicion el item
                                Log.e(TAG, " removiendo prioridad de listaBonif  el producto bonif "+new Gson().toJson(listaBonif.get(posOldPrioridad)));
                                listaBonif.remove(locamlRemove);
                                listaCantidades.remove(locamlRemove);
                                listaPromociones.remove(locamlRemove);
                                listaPromocionCompuesta.remove(locamlRemove);
                                //items_tipoAgrupado.remove(i); YA no se usa
                                listaCantidadesUsadas.remove(locamlRemove);
                                listaMontosUsados.remove(locamlRemove);
                                i--;

                            }else {
                                posOldPrioridad=i;
                                cantCondicion=listaPromociones.get(posOldPrioridad).getCant_condicion();
                                MontoCondicion=Double.parseDouble(listaPromociones.get(posOldPrioridad).getMonto());
                            }
                        }
                    }

                    int positionOldPrioridad=posOldPrioridad;

                    itemDetalleGlobal = itemDetalle_ok;
                    Log.w("-->","JSON MULTIPLE "+ gson.toJson(listaBonif) );

                    DialogFragment_bonificaciones dialog_bonificaciones = new DialogFragment_bonificaciones(
                            listaBonif, descripcion, listaPromociones,
                            listaCantidades, listaPromocionCompuesta,
                            items_tipoAgrupado, listaCantidadesUsadas,
                            listaMontosUsados,positionOldPrioridad);
                    dialog_bonificaciones.show(getSupportFragmentManager(),
                            "dialogBonificaciones");
                    dialog_bonificaciones.setCancelable(false);

                } else {
                    /* LA BONIFICACION SE DEBE MOSTRAR COMO UNA OPCION A ELEGIR */
                    Log.w("", "la lista tiene un item");
                    itemDetalleGlobal = itemDetalle_ok;

                    ArrayList<Integer> items_tipoAgrupado = new ArrayList<Integer>();
                    ArrayList<Model_bonificacion> listaBonif = new ArrayList<Model_bonificacion>();

                    for (int i = 0; i < listaPromociones.size(); i++) {
                        Model_bonificacion bonificacion = DAOBonificaciones.getModelBonificacion(listaCantidades.get(i),
                                listaPromociones.get(i).getSalida(),
                                listaPromociones.get(i).getEntrada());

                        if(bonificacion==null){
                            Log.e(TAG, "El producto "+listaPromociones.get(i).getSalida()+ " de promocion no se ha encontrado en catalogo");
                            //Elinamos de la posicion el item
                            listaBonif.remove(i);
                            listaCantidades.remove(i);
                            listaPromociones.remove(i);
                            listaPromocionCompuesta.remove(i);
                            listaCantidadesUsadas.remove(i);
                            listaMontosUsados.remove(i);
                            i--;
                        }
                        else{
                            bonificacion.setPrioridad(listaPromociones.get(i).getPrioridad());
                            listaBonif.add(bonificacion);
                        }
                    }

                    Log.w("-->","JSON SOLO "+ gson.toJson(listaBonif) );
                    int positionOldPrioridad=listaBonif.get(0).getPrioridad()==1?0:-1;

                    DialogFragment_bonificaciones dialog_bonificaciones = new DialogFragment_bonificaciones(
                            listaBonif, descripcion, listaPromociones,
                            listaCantidades, listaPromocionCompuesta,
                            items_tipoAgrupado, listaCantidadesUsadas,
                            listaMontosUsados, positionOldPrioridad);
                    dialog_bonificaciones.show(getSupportFragmentManager(),
                            "dialogBonificaciones");
                    dialog_bonificaciones.setCancelable(false);


                }


                Log.e("", "---------------------------------");
                Log.d("",
                        "lista Promociones:\n" + gson.toJson(listaPromociones));
                Log.e("", "---------------------------------");
                Log.d("", "lista Cantidades:\n" + gson.toJson(listaCantidades));
                Log.e("", "---------------------------------");
                Log.d("",
                        "lista PromocionCompuesta:\n"
                                + gson.toJson(listaPromocionCompuesta));
                Log.e("", "---------------------------------");
                Log.d("",
                        "lista CantidadesUsadas:\n"
                                + gson.toJson(listaCantidadesUsadas));
                Log.e("", "---------------------------------");
                Log.d("",
                        "lista MontosUsados:\n"
                                + gson.toJson(listaMontosUsados));
                Log.e("", "---------------------------------");

            }
            /*
             *  *****************************************************************
             * VERIFICANDO LISTA DE PROMOCIONES OBTENIDAS
             * ***********************
             * *********************************************************
             */
        }
    }

    private int RegistroBonificacion(String oc_numero,
                                     DB_PromocionDetalle promocionDetalle,
                                     DBPedido_Detalle itemDetallePedido, int cantidadBonificada,
                                     ArrayList<String[]> listaEntradasCompuestasAux,
                                     ArrayList<Integer> cantidadesUsadas, ArrayList<Double> montosUsados,
                                     int nroItemDetalleSalida) {
        if (listaEntradasCompuestas == null) {
            Log.v("", "listaEntradasCompuestas es null");
        } else {
            Log.v("", "listaEntradasCompuestas no es null "+ listaEntradasCompuestas.size());
        }
        int cantidadDisponible = 0;
        double montoDisponible = 0.0;
        int cantidadDisponibleRegistro = 0;
        double montoDisponibleRegistro = 0.0;
        int cantidadEntrada=0;
        double montoEntrada=0.0;
        if (listaEntradasCompuestasAux.size() > 0) {

            //ordenamos listaEntradasCompuestasAux para que seal al ultimo
            for (int i = 0; i < listaEntradasCompuestasAux.size(); i++) {
                String[] entradasCompuestasAux = listaEntradasCompuestasAux.get(i);
                int cantidadUsada = cantidadesUsadas.get(i);
                double montoUsado = montosUsados.get(i);
                if (entradasCompuestasAux[0].equals(itemDetallePedido.getCip())
                    && Integer.parseInt(entradasCompuestasAux[8])==itemDetallePedido.getItem()
                ) {
                    listaEntradasCompuestasAux.remove(i);
                    listaEntradasCompuestasAux.add(entradasCompuestasAux);

                    cantidadesUsadas.remove(i);
                    cantidadesUsadas.add(cantidadUsada);

                    montosUsados.remove(i);
                    montosUsados.add(montoUsado);
                    break;
                }
            }
            //fin al ordenar

            /*
             * 1.Eliminar los registros que tengan alguna de las entradas compuestas
             * 2.Insertar todas las entradas compuestas con cantidadSalida 0 excepto la entrada q se acaba de registrar o modificar
             */
            Log.d("", "Existen entradas Compuestas:  "+ listaPromocionCompuesta.size());

            int cantidadbonificacionAnterior = 0;
            //cantidadbonificacionAnterior = DAOBonificaciones.getcantidadSalidaPromocion(Oc_numero,promocionDetalle.getSecuencia(),promocionDetalle.getItem());
            int cantidadBonificacionSalida = cantidadBonificada;
            double cantidadbonificacionTotal=0;
            DB_RegistroBonificaciones registroBonificacionAnterior=null;

            ArrayList<String> registroAnterior = DAOBonificaciones.getRegistroAnterior(oc_numero,"B"+promocionDetalle.getSalida());

            for (int i = 0; i < listaEntradasCompuestasAux.size(); i++) {
                String[] entradasCompuestasAux = listaEntradasCompuestasAux.get(i);
                int cantidadUsada = cantidadesUsadas.get(i);
                double montoUsado = montosUsados.get(i);
                // entradasCompuestasAux[0] -> entrada
                // entradasCompuestasAux[1] -> cantidad
                // entradasCompuestasAux[2] -> monto
                // entradasCompuestasAux[3] -> tipoUnimed
                // entradasCompuestasAux[4] -> unimed
                // entradasCompuestasAux[5] -> agrupado de la promocion

                /*
                 * 1-Se obtiene la cantidad disponible 2-Se obtiene la cantidad
                 * bonificada
                 */

                /*
                 * cantidadDisponibleRegistro =
                 * DAOBonificaciones.getCantidadDisponible(Oc_numero,
                 * entradasCompuestasAux[0]); if(cantidadDisponibleRegistro ==
                 * -1){ //Si la cantidad no esta registrada se obtiene la
                 * cantidad disponible que se acaba de registrar
                 * cantidadDisponibleRegistro =
                 * DAOPedidoDetalle.getCantidadItemPedido(Oc_numero,
                 * entradasCompuestas[0]); }
                 */
                cantidadDisponibleRegistro = Integer.parseInt(entradasCompuestasAux[1]);
                montoDisponibleRegistro = Double.parseDouble(entradasCompuestasAux[2]);

                cantidadDisponible = cantidadDisponibleRegistro - cantidadUsada;
                montoDisponible = montoDisponibleRegistro - montoUsado;
                registroBonificacionAnterior=null;

                registroBonificacionAnterior= DAOBonificaciones.Eliminar_RegistroBonificacion(oc_numero,
                        promocionDetalle.getSecuencia(),
                        promocionDetalle.getItem(), entradasCompuestasAux[0],
                        Integer.parseInt(entradasCompuestasAux[5]),
                        Integer.parseInt(entradasCompuestasAux[8]),
                        -999
                        );

                //-----------------------------------------------------------------------------------------------
                if(registroBonificacionAnterior!=null
                        && registroBonificacionAnterior.getSecuenciaPromocion()==promocionDetalle.getSecuencia()
                        && registroBonificacionAnterior.getItem()==promocionDetalle.getItem()
                        && registroBonificacionAnterior.getEntrada().equals(entradasCompuestasAux[0])
                        && registroBonificacionAnterior.getEntrada_item()== Integer.parseInt(entradasCompuestasAux[8])
                ){//si es el mismo registro entonces debemos recuperar la cantidad y monto de entrada
                    cantidadEntrada = registroBonificacionAnterior.getCantidadEntrada();
                    montoEntrada    =registroBonificacionAnterior.getMontoEntrada();
                }else{
                    cantidadEntrada = Integer.parseInt(entradasCompuestasAux[1]); // cantidadEntrada
                    montoEntrada    =Double.parseDouble(entradasCompuestasAux[2]); // montoEntrada
                }
                //-----------------------------------------------------------------------------------------------

                Log.d("", "entradasCompuestasAux[0]: " + entradasCompuestasAux[0]+"    getEntrada:"+promocionDetalle.getEntrada());
                Log.e("PedidosActivity", "cantidadbonificacionAnterior: "+cantidadbonificacionAnterior);
                Log.e("PedidosActivity", "cantidadBonificada: "+cantidadBonificada);


                if (entradasCompuestasAux[0].equals(promocionDetalle.getEntrada())
                && entradasCompuestasAux[8].equals(String.valueOf(itemDetallePedido.getItem()))
                ) {
                    cantidadBonificacionSalida = cantidadBonificada;
                    Log.d("", "entrada actual ");

                    if (promocionDetalle.getAcumulado() == 1 ) {
                        cantidadBonificacionSalida = cantidadBonificada + cantidadbonificacionAnterior;
                        Log.d("", "La promocion es acumulable, se acumula la bonificacion: "+cantidadBonificacionSalida);
                    }
                    itemBonificacion ++;
                    //REGISTRAR NUEVO ITEM REGISTRO BONIFICACION
                    registrarItemRegistroBonificacion(oc_numero,
                            registroBonificacionAnterior,
                            entradasCompuestasAux,
                            promocionDetalle,
                            itemBonificacion,
                            cantidadBonificacionSalida,
                            cantidadDisponible,
                            montoDisponible,
                            registroBonificacionAnterior!=null?registroBonificacionAnterior.getSalida_item(): nroItemDetalleSalida
                    );
                    if(registroBonificacionAnterior!=null) {
                        itemBonificacion++;
                        registrarItemRegistroBonificacion(oc_numero,
                                registroBonificacionAnterior,
                                entradasCompuestasAux,
                                promocionDetalle,
                                itemBonificacion,
                                cantidadBonificacionSalida,
                                cantidadDisponible,
                                montoDisponible,
                                nroItemDetalleSalida
                        );
                    }

                    Log.d("", "AGREGANDO REGISTRO BONIFICACION");
                    Log.v("", "oc_numero: " + oc_numero);
                    Log.v("", "secuencia: " + promocionDetalle.getSecuencia());
                    Log.v("", "item: " + promocionDetalle.getItem());
                    Log.v("", "agrupado: " + entradasCompuestasAux[5]);
                    Log.v("", "entrada: " + entradasCompuestasAux[0]);
                    Log.v("", "tipoUnimed: " + entradasCompuestasAux[3]);
                    // Log.v("","unimed: "+entradasCompuestasAux[4]);
                    Log.v("", "cantidad: " + entradasCompuestasAux[6]);
                    Log.v("", "monto: S/" + entradasCompuestasAux[7]);
                    Log.v("", "salida: " + "B"+promocionDetalle.getSalida());
                    // Log.v("","tipoUnimedSalida: "+Integer.parseInt(promocionDetalle.getTipo_unimed_salida()));
                    Log.v("", "cantidadBonificada: " + cantidadBonificacionSalida);
                    Log.v("", "acumulado: " + promocionDetalle.getAcumulado());
                    Log.v("", "cantidadDisponible: " + cantidadDisponible);
                    Log.v("", "montoDisponible: S/" + montoDisponible);
                } else {
                    itemBonificacion ++;
                    int cantidad_boni=registroBonificacionAnterior!=null?registroBonificacionAnterior.getCantidadSalida():0;
                    registrarItemRegistroBonificacion(oc_numero,
                            registroBonificacionAnterior,
                            entradasCompuestasAux,
                            promocionDetalle,
                            itemBonificacion,
                            0+cantidad_boni,
                            cantidadDisponible,
                            montoDisponible,
                            registroBonificacionAnterior!=null?registroBonificacionAnterior.getSalida_item(): nroItemDetalleSalida
                    );
                    if(registroBonificacionAnterior!=null){
                        itemBonificacion++;
                        registrarItemRegistroBonificacion(oc_numero,
                                registroBonificacionAnterior,
                                entradasCompuestasAux,
                                promocionDetalle,
                                itemBonificacion,
                                0+cantidad_boni,
                                cantidadDisponible,
                                montoDisponible,
                                nroItemDetalleSalida
                        );
                    }

//                    DAOBonificaciones.AgregarRegistroBonificacion(
//                            oc_numero,
//                            itemBonificacion,
//                            promocionDetalle.getItem(),
//                            promocionDetalle.getSecuencia(),
//                            Integer.parseInt(entradasCompuestasAux[5]),// Agrupado
//                            entradasCompuestasAux[0], // entrada
//                            Integer.parseInt(entradasCompuestasAux[3]), // tipoUnimedEntrada
//                            entradasCompuestasAux[4], // unimedEntrada
//                            Integer.parseInt(entradasCompuestasAux[6]), // cantidadEntrada ------------------------------>CAMBIADO POR LA CANTIDAD ENTRADA INICIAL
//                            Double.parseDouble(entradasCompuestasAux[7]), // montoEntrada ------------------------------->CAMBIADO POR EL PRECIO NETO DE LA ENTRADA INICIAL
//                            "B"+promocionDetalle.getSalida(),
//                            Integer.parseInt(promocionDetalle.getTipo_unimed_salida()),
//                            0+cantidad_boni,
//                            promocionDetalle.getAcumulado(),
//                            cantidadDisponible,
//                            montoDisponible,
//                            codven,
//                            promocionDetalle.getPrioridad()
//                            ,Integer.parseInt(entradasCompuestasAux[8])// item de pedido que genero la bonificacion
//                            ,registroBonificacionAnterior!=null?registroBonificacionAnterior.getSalida_item(): nroItemDetalleSalida
//                    );

                    Log.d("", "AGREGANDO REGISTRO BONIFICACION");
                    Log.v("", "oc_numero: " + oc_numero);
                    Log.v("", "secuencia: " + promocionDetalle.getSecuencia());
                    Log.v("", "item: " + promocionDetalle.getItem());
                    Log.v("", "agrupado: " + entradasCompuestasAux[5]);
                    Log.v("", "entrada: " + entradasCompuestasAux[0]);
                    Log.v("", "tipoUnimed: " + entradasCompuestasAux[3]);
                    // Log.v("","unimed: "+entradasCompuestasAux[4]);
                    Log.v("", "cantidad: " + entradasCompuestasAux[6]);
                    Log.v("", "monto: S/" + entradasCompuestasAux[7]);
                    Log.v("", "salida: " + "B"+promocionDetalle.getSalida());
                    // Log.v("","tipoUnimedSalida: "+Integer.parseInt(promocionDetalle.getTipo_unimed_salida()));
                    Log.v("", "cantidadBonificada: " + "0");
                    Log.v("", "acumulado: " + promocionDetalle.getAcumulado());
                    Log.v("", "cantidadDisponible: " + cantidadDisponible);
                    Log.v("", "montoDisponible: S/" + montoDisponible);
                }
            }

            if (registroAnterior.size() > 0 ) {
                if (registroAnterior.get(1) != null) {
                    int cantidadTotal = DAOBonificaciones.obtenerCantidadBonificacion(oc_numero,"B"+promocionDetalle.getSalida(), nroItemDetalleSalida);
                    int cantidadEntregada = 0;
                    if (promocionDetalle.getAcumulado() == 1 ) {
                        cantidadEntregada = Integer.parseInt(registroAnterior.get(2)) + cantidadBonificada;
                    }else{
                        cantidadEntregada = Integer.parseInt(registroAnterior.get(2)) + cantidadBonificacionSalida;
                    }

                    int saldoAnterior = Integer.parseInt(registroAnterior.get(1));
                    DAOBonificaciones.Actualizar_RegistroBonificacion(registroAnterior.get(0), 0, 0, 0, 0, "",codven,codcli);//Limpiar registro anterior
                    //Se pasan los campos al nuevo regitro y se calculan los montos pendientes
                    DAOBonificaciones.Actualizar_RegistroBonificacion((oc_numero+itemBonificacion), cantidadTotal,saldoAnterior, cantidadEntregada, (cantidadTotal+saldoAnterior-cantidadEntregada), registroAnterior.get(3),codven,codcli);
                }
            }else{

            }
            // Si no, la bonificacion es simple tipo Ó
        } else {

            boolean estaRegistrado = DAOBonificaciones
                    .VerificarRegistroBonificacion(oc_numero,
                            promocionDetalle.getSecuencia(),
                            promocionDetalle.getItem(),
                            promocionDetalle.getEntrada(),
                            promocionDetalle.getAgrupado(),
                            itemDetallePedido.getItem()
                    );

            cantidadDisponibleRegistro = DAOBonificaciones.getCantidadDisponible(Oc_numero,itemDetalleGlobal.getCip(), promocionDetalle, itemDetalleGlobal.getItem());
            montoDisponibleRegistro = DAOBonificaciones.getMontoDisponible(Oc_numero, itemDetalleGlobal.getCip(), promocionDetalle, itemDetalleGlobal.getItem());

            if (cantidadDisponibleRegistro == -1) {
                int cantidadCalculada = Convertir_toUnidadesMinimas(
                        promocionDetalle.getEntrada(),
                        itemDetalleGlobal.getUnidad_medida(),
                        itemDetalleGlobal.getCantidad());
                cantidadDisponibleRegistro = cantidadCalculada;
                montoDisponibleRegistro = Double.parseDouble(itemDetalleGlobal
                        .getPrecio_neto());
            } else if (cantidadDisponibleRegistro > 0) {
                // EL PRODUCTO TIENE DISPONIBILIDAD (ESTA REGISTRADO)
            } else {
                Log.e("", "Producto: " + itemDetalleGlobal.getCip()
                        + " no tiene cantidad disponible para la bonificacion");
                cantidadDisponibleRegistro = 0;
                montoDisponibleRegistro = 0;
            }
            Log.d("PedidosActivity :RegistroBonificacion:",
                    "cantidadDisponibleRegistro: " + cantidadDisponibleRegistro);
            Log.d("PedidosActivity :RegistroBonificacion:", "cantidadUsada: "
                    + cantidadesUsadas.get(0));

            Log.d("PedidosActivity :RegistroBonificacion:",
                    "montoDisponibleRegistro: " + montoDisponibleRegistro);
            Log.d("PedidosActivity :RegistroBonificacion:", "montoUsado: "
                    + montosUsados.get(0));

            cantidadDisponible = cantidadDisponibleRegistro- cantidadesUsadas.get(0);
            montoDisponible = montoDisponibleRegistro - montosUsados.get(0);

            int tipo_unidad_medida = dbclass.isCodunimed_Almacen(
                    itemDetallePedido.getCip(),
                    itemDetallePedido.getUnidad_medida());
            if (estaRegistrado) {
                Log.e("PedidosActivity", "El registro bonificacion ya esta registrado, actualizando...");
                DAOBonificaciones.Actualizar_RegistroBonificacion(oc_numero,
                        promocionDetalle.getSecuencia(),
                        promocionDetalle.getItem(),
                        promocionDetalle.getAgrupado(),
                        promocionDetalle.getEntrada(),
                        tipo_unidad_medida,
                        itemDetallePedido.getUnidad_medida(),
                        // cantidadDisponibleRegistro,
                        itemDetallePedido.getCantidad(),
                        Double.parseDouble(itemDetallePedido.getPrecio_neto()),
                        "B"+promocionDetalle.getSalida(),
                        Integer.parseInt(promocionDetalle.getTipo_unimed_salida()),
                        cantidadBonificada, promocionDetalle.getAcumulado(),
                        cantidadDisponible, montoDisponible);

            } else {
                //Antes de agregar verificar si la salida se ha modificado o usa pendientes obteniendo la cantidad entregada
                ArrayList<String> registroAnterior = DAOBonificaciones.getRegistroAnterior(oc_numero,"B"+promocionDetalle.getSalida());

                itemBonificacion ++;
                DAOBonificaciones.AgregarRegistroBonificacion(oc_numero,itemBonificacion,
                        promocionDetalle.getItem(),
                        promocionDetalle.getSecuencia(),
                        promocionDetalle.getAgrupado(),
                        promocionDetalle.getEntrada(),
                        tipo_unidad_medida,
                        itemDetallePedido.getUnidad_medida(),
                        // cantidadDisponibleRegistro,
                        itemDetallePedido.getCantidad(),
                        Double.parseDouble(itemDetallePedido.getPrecio_neto()),
                        "B"+promocionDetalle.getSalida(), Integer
                                .parseInt(promocionDetalle
                                        .getTipo_unimed_salida()),
                        cantidadBonificada, promocionDetalle.getAcumulado(),
                        cantidadDisponible, montoDisponible, codven,
                        promocionDetalle.getPrioridad()
                        , itemDetallePedido.getItem(),
                        nroItemDetalleSalida
                );

                Log.d("", "AGREGANDO REGISTRO BONIFICACION");
                Log.v("", "oc_numero: " + oc_numero);
                Log.v("", "itemBonificacion: " + itemBonificacion);
                Log.v("", "secuencia: " + promocionDetalle.getSecuencia());
                Log.v("", "item: " + promocionDetalle.getItem());
                Log.v("", "agrupado: " + promocionDetalle.getAgrupado());
                Log.v("", "entrada: " + promocionDetalle.getEntrada());
                Log.v("", "tipoUnimed: " + tipo_unidad_medida);
                // Log.v("","unimed: "+entradasCompuestasAux[4]);
                Log.v("", "cantidad: " + itemDetallePedido.getCantidad());
                Log.v("", "monto: S/" + itemDetallePedido.getPrecio_neto());
                Log.v("", "salida: " + "B"+promocionDetalle.getSalida());
                // Log.v("","tipoUnimedSalida: "+Integer.parseInt(promocionDetalle.getTipo_unimed_salida()));
                Log.v("", "cantidadBonificada: " + cantidadBonificada);
                Log.v("", "acumulado: " + promocionDetalle.getAcumulado());
                Log.v("", "cantidadDisponible: " + cantidadDisponible);
                Log.v("", "montoDisponible: S/" + montoDisponible);

                if (registroAnterior.size() > 0 && registroAnterior.get(1) != null) {
                    int cantidadTotal = DAOBonificaciones.obtenerCantidadBonificacion(oc_numero,"B"+promocionDetalle.getSalida(), nroItemDetalleSalida);
                    int cantidadEntregada = Integer.parseInt(registroAnterior.get(2)) + cantidadBonificada;
                    int saldoAnterior = Integer.parseInt(registroAnterior.get(1));
                    DAOBonificaciones.Actualizar_RegistroBonificacion(registroAnterior.get(0), 0, 0, 0, 0, "",codven,codcli);//Limpiar registro anterior
                    //Se pasan los campos al nuevo regitro y se calculan los montos pendientes
                    DAOBonificaciones.Actualizar_RegistroBonificacion((oc_numero+itemBonificacion), cantidadTotal,saldoAnterior, cantidadEntregada, (cantidadTotal+saldoAnterior-cantidadEntregada), registroAnterior.get(3),codven,codcli);
                }else{

                }
            }
        }
        int cantidadEntregada = DAOBonificaciones.getCantidadEntregada(oc_numero,"B"+promocionDetalle.getSalida(), nroItemDetalleSalida);
        int cantidad = DAOBonificaciones.obtenerCantidadBonificacion(oc_numero,"B"+promocionDetalle.getSalida(), nroItemDetalleSalida);

        if (cantidadEntregada > 0)  {
            cantidad = cantidadEntregada;
        }

        return cantidad;
    }

    private void registrarItemRegistroBonificacion(String oc_numero
                                                    ,DB_RegistroBonificaciones registroBonificacionAnterior
                                                   , String[] entradasCompuestasAux
                                                   ,DB_PromocionDetalle promocionDetalle
                                                    ,int itemBonificacion
                                                    ,int cantidadBonificacionSalida
                                                   ,int cantidadDisponible
                                                   ,double montoDisponible
                                                   ,int nroItemDetalleSalida
                                                   ){
        DAOBonificaciones.AgregarRegistroBonificacion(
                oc_numero,
                itemBonificacion,
                promocionDetalle.getItem(),
                promocionDetalle.getSecuencia(),
                Integer.parseInt(entradasCompuestasAux[5]),// Agrupado
                entradasCompuestasAux[0], // entrada
                Integer.parseInt(entradasCompuestasAux[3]), // tipoUnimedEntrada
                entradasCompuestasAux[4], // unimedEntrada
                Integer.parseInt(entradasCompuestasAux[6]), // cantidadEntrada------------------------------>CAMBIADO POR LA CANTIDAD ENTRADA INICIAL
                Double.parseDouble(entradasCompuestasAux[7]), // montoEntrada ------------------------------->CAMBIADO POR EL PRECIO NETO DE LA ENTRADA INICIAL
                "B"+promocionDetalle.getSalida(),
                Integer.parseInt(promocionDetalle.getTipo_unimed_salida()),
                cantidadBonificacionSalida,
                promocionDetalle.getAcumulado(),
                cantidadDisponible,
                montoDisponible,
                codven,
                promocionDetalle.getPrioridad(),
                Integer.parseInt(entradasCompuestasAux[8])// item de pedido que genero la bonificacion
                , nroItemDetalleSalida
        );
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
    private double getTipoCambioPedido(){
        if (codigoMoneda.equals(PedidosActivity.MONEDA_SOLES_IN)) {
            String tipo_de_cambio  = dbclass.getCambio("Tipo_cambio");
            return Double.parseDouble(tipo_de_cambio);
        }
        return 1;
    }

    private void agregarProductoxPromocion(String cipSalida,
                                           String tipo_unimed_salida,
                                           int cantidadProducto,
                                           int secuenciaPromo,
                                             int itemPromo,
                                             int prioridad,
                                           int nroItemDetalle,
                                           String codproEntrada, int itemEntrada
                                           ) {
        DBPedido_Detalle detalleOrigen = dbclass.getPedidosDetalleEntityWithItem(edt_nroPedido.getText().toString(),
                codproEntrada,
                itemEntrada);
        UtilCalcularPrecioProducto utilCaclularPrecioProductoUnit= new UtilCalcularPrecioProducto(
                dbclass, codcli, codigoMoneda
        );
        UtilCalcularPrecioProducto.ResultPrecios resulPrecio =utilCaclularPrecioProductoUnit.consultarPrecios(cipSalida, detalleOrigen.getPorcentaje_desc(), detalleOrigen.getPorcentaje_desc_extra());
        if(resulPrecio.errorMensaje!=null) {
            GlobalFunctions.showCustomToast(this,  "Producto promoción no se agregó motivo a: \n\n"+resulPrecio.errorMensaje, GlobalFunctions.TOAST_ERROR);
        }

        String salida= "B"+cipSalida;
        double precioLista= Double.parseDouble(resulPrecio.precioLista.replace(",", ""));
        double precioVentaSinIgv= Double.parseDouble(resulPrecio.precioVentaPreSinIGV.replace(",", ""));
        double precioVentaConIgv= Double.parseDouble(resulPrecio.precioVentaPreConIGV.replace(",", ""));
        String precioSutotal= VARIABLES.getStringFormaterThreeDecimal(cantidadProducto*precioVentaSinIgv).replace(",","");
        double pesoProducto = dbclass.getPesoProducto(codproEntrada);

        DBPedido_Detalle itemDetalle = new DBPedido_Detalle();
        itemDetalle.setOc_numero(edt_nroPedido.getText().toString());
        itemDetalle.setCip(salida);
        itemDetalle.setEan_item("");
        itemDetalle.setPrecioLista(String.valueOf(precioLista));
        itemDetalle.setPercepcion("0");
        itemDetalle.setPorcentaje_desc(precioVentaSinIgv==0?100:detalleOrigen.getPorcentaje_desc());
        itemDetalle.setPorcentaje_desc_extra(precioVentaSinIgv==0?100:detalleOrigen.getPorcentaje_desc_extra());
        itemDetalle.setDescuento(String.valueOf(precioVentaConIgv));
        itemDetalle.setPrecio_bruto(String.valueOf(precioVentaSinIgv));
        itemDetalle.setPrecio_neto(precioSutotal);
        itemDetalle.setCantidad(cantidadProducto);
        itemDetalle.setTipo_producto("C");
        itemDetalle.setUnidad_medida((dbclass.obtener_codunimedXtipo_unimed_salida(Integer.parseInt(tipo_unimed_salida), salida)));
        itemDetalle.setFlag("N");
        itemDetalle.setPeso_bruto(""+VARIABLES.getDoubleFormaterThreeDecimal(pesoProducto*cantidadProducto));
        if (prioridad==1){
            itemDetalle.setSec_promo_prioridad(secuenciaPromo);
            itemDetalle.setItem_promo_prioridad(itemPromo);
        }else {
            itemDetalle.setSec_promo(""+secuenciaPromo);
            itemDetalle.setItem_promo(itemPromo);
        }
        itemDetalle.setItem(nroItemDetalle);
        dbclass.AgregarPedidoDetallePromocion(itemDetalle);

    }

    private int VerificarSiTienePromocion(String codpro, String und_medida_prod,String cod_politica, int cantidad,String codigoUbigeo) {
        // TODO Auto-generated method stub
        int cantidadCalculada = 0;
        int tipo_unidad_medida = dbclass.isCodunimed_Almacen(codpro,
                und_medida_prod);

        // verifica si hubo concidencia entre la unidad de medida del
        // itemDetalle y las unidades de media del producto

        if (tipo_unidad_medida != -1) {
            // Se obtiene la promocion del producto en promocion_detalle

            if (tipo_unidad_medida == 0) {// es la unidad minima, no se aplica factor de conversion
                Log.d("", "Unidad minima misma cantidad");

                al_PromocionDetalle = dbclass.getPromocionesXProducto2(codpro,tipo_unidad_medida, codcli, codven, cod_politica,codigoUbigeo);
                cantidadCalculada = cantidad;
            } else {// Debe transformarse a la unidad minima

                int factor_conversion = dbclass.getFactorConversion(codpro);
                cantidadCalculada = cantidad * factor_conversion;
                Log.d("", "calculando cantidad:  " + cantidad + " * "+ factor_conversion);
                Log.d("", "nueva cantidad calculada por factor conversion: "						+ cantidadCalculada);
                al_PromocionDetalle = dbclass.getPromocionesXProducto2(codpro,0, codcli, codven, cod_politica,codigoUbigeo);
            }

            if (al_PromocionDetalle.size() == 0) {
                Log.d("VerificarSiTienePromocion", "retorna false");
                return 0;
            } else
                return cantidadCalculada;
        } else {

            Log.v("VerificarSiTienepromocion",
                    "valor devuelto = -1, no hay concidencia }"
                            + "entre unidad_medida y tipo_unida_medida_entrada ");

            return 0;
        }

        // SI TIENE PROMOCION RETORNA LA CANTIDAD DE PRODUCTOS PEDIDOS (FACTOR
        // DE CONVERSION)
        // SI NO TIENE PROMOCION RETORNA 0

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

        AlertDialog.Builder dialogo2 = new AlertDialog.Builder(PedidosActivity.this);
        dialogo2.setTitle(titulo);
        dialogo2.setMessage(mensaje);
        dialogo2.setIcon(icon);
        dialogo2.setCancelable(false);
        dialogo2.setPositiveButton("Aceptar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {

                        if (origen.equals("REPORTES")) {

                            finish();
                            Intent intent2 = new Intent(PedidosActivity.this, ReportesPedidosCotizacionYVisitaActivity.class);
                            intent2.putExtra("TIPO_VISTA", ReportesPedidosCotizacionYVisitaActivity.DATOS_PREVENTA);
                            intent2.putExtra("ORIGEN", "PEDIDOS");
                            startActivity(intent2);

                        } else {
                            //crear_dialogo_otro_pedido();
                            finish();
                            Intent intent2 = new Intent(PedidosActivity.this, ReportesPedidosCotizacionYVisitaActivity.class);
                            intent2.putExtra("TIPO_VISTA", ReportesPedidosCotizacionYVisitaActivity.DATOS_PREVENTA);
                            intent2.putExtra("ORIGEN", "PEDIDOS");
                            startActivity(intent2);
                        }
                    }
                });

        dialogo2.show();

    }

    private void crear_dialogo_otro_pedido() {

        AlertDialog.Builder alerta = new AlertDialog.Builder(
                PedidosActivity.this);
        alerta.setCancelable(false);
        alerta.setMessage("Desea realizar otro pedido para este cliente?");

        alerta.setPositiveButton("Si", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

                finish();

                Intent i = new Intent(PedidosActivity.this,
                        PedidosActivity.class);
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

                Intent intent2 = new Intent(PedidosActivity.this, ReportesPedidosCotizacionYVisitaActivity.class);
                intent2.putExtra("TIPO_VISTA", ReportesPedidosCotizacionYVisitaActivity.DATOS_PREVENTA);
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
                            Intent intent2 = new Intent(PedidosActivity.this, ReportesPedidosCotizacionYVisitaActivity.class);
                            intent2.putExtra("TIPO_VISTA", ReportesPedidosCotizacionYVisitaActivity.DATOS_PREVENTA);
                            intent2.putExtra("ORIGEN", "PEDIDOS");
                            startActivity(intent2);

                        } else {
                            finish();
                            Intent intent2 = new Intent(PedidosActivity.this, ReportesPedidosCotizacionYVisitaActivity.class);
                            intent2.putExtra("TIPO_VISTA", ReportesPedidosCotizacionYVisitaActivity.DATOS_PREVENTA);
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

        LinearLayout customToast = (LinearLayout) view
                .findViewById(R.id.toast_personalizado);
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

    // Metodo del DialogFragment_bonificaciones
    @Override
    public void onItemClick(int posicion,
                            boolean boniAutomatico,
                            ArrayList<Model_bonificacion> listaBonif,
                            ArrayList<DB_PromocionDetalle> listaPromociones,
                            ArrayList<Integer> listaCantidades,
                            ArrayList<ArrayList<String[]>> listaPromocionesCompuestas,
                            ArrayList<Integer> items_tipoAgrupado,
                            ArrayList<ArrayList<Integer>> listaCantidadesUsadas,
                            ArrayList<ArrayList<Double>> listaMontosUsados) {
        // TODO Auto-generated method stub



        if (!boniAutomatico)
            AddBonificacionAutomatico(posicion, null, listaBonif, listaPromociones, listaCantidades,  listaPromocionesCompuestas, items_tipoAgrupado, listaCantidadesUsadas, listaMontosUsados);
        if (listaPromociones.get(posicion).getPrioridad()!=1 || boniAutomatico){
            int posPrioridad=0;
            for (DB_PromocionDetalle item: listaPromociones){
                if (item.getPrioridad()==1){
                    AddBonificacionAutomatico(posPrioridad, "Bonificación Automática", listaBonif, listaPromociones, listaCantidades,  listaPromocionesCompuestas, items_tipoAgrupado, listaCantidadesUsadas, listaMontosUsados);
                    break;
                }
                posPrioridad++;
            }
        }
        //else //prioridad ya fue seleccionado
        mostrarListaProductos("");


    }
    public void AddBonificacionAutomatico(int posicion,
                                          String descripcionBoni,
                                          ArrayList<Model_bonificacion> listaBonif,
                            ArrayList<DB_PromocionDetalle> listaPromociones,
                            ArrayList<Integer> listaCantidades,
                            ArrayList<ArrayList<String[]>> listaPromocionesCompuestas,
                            ArrayList<Integer> items_tipoAgrupado,
                            ArrayList<ArrayList<Integer>> listaCantidadesUsadas,
                            ArrayList<ArrayList<Double>> listaMontosUsados) {

        Log.e("",
                "=====================================================================");
        Log.w("", "ACTION GLOBAL -> " + accionGlobal);
        Model_bonificacion modelBonificacion = listaBonif.get(posicion);

        int cantidadActualizada = 0;
        if (accionGlobal!=null && accionGlobal.equals("editar")) {
            // Si se esta editando un producto, se debe eliminar las promociones
            // de ese producto en detallePedido y registroBonificaciones.
            // ya que se volverá a analizar y registrar nuevamente.

            ArrayList<DB_RegistroBonificaciones> bonificaciones_xEntrada = DAOBonificaciones
                    .getRegistroBonificaciones_xEntrada(Oc_numero,
                            listaPromociones.get(0).getEntrada(), itemDetalleGlobal.getItem() );
            Log.v("", "Inicio del FOR --> PARA LIMPIAR BONIF");
            /*-----------------------------------------------------------------------------------------------------------------------------------------*/
            for (DB_RegistroBonificaciones boni : bonificaciones_xEntrada) {
                // Se eliminan las bonificaciones que tienen relacion a la
                // entrada a analizar
                DAOBonificaciones.Eliminar_RegistroBonificacion_Dependencias(
                        Oc_numero, boni.getSecuenciaPromocion(),
                        boni.getItem(), boni.getAgrupado(), boni.getSalida_item());

                // Se eliminan los item de pedido con las salidas que han sido
                // eliminadas en las bonificaciones
                dbclass.EliminarItemPedido(boni.getSalida(), -1, Oc_numero);
            }
            /*-----------------------------------------------------------------------------------------------------------------------------------------*/
            Log.v("", "Fin del FOR --> PARA LIMPIAR BONIF");
        }
        /*
         * La lista de items_tipoAgrupado indicaba si habian varias promociones
         * y alguna de ellas era agrupada
         */

        DB_PromocionDetalle promocionDetalle = listaPromociones.get(posicion);
        int cantidadBonificada = listaCantidades.get(posicion);
        ArrayList<String[]> listaEntradasCompuestas = listaPromocionesCompuestas.get(posicion);
        ArrayList<Integer> cantidadesUsadas = listaCantidadesUsadas.get(posicion);
        ArrayList<Double> montosUsados = listaMontosUsados.get(posicion);

        Log.d("PedidosActivity :onItemClick:","REGISTRANDO ITEM ELEGIDO DEL DIALOG:");
        Log.d("PedidosActivity :onItemClick:", "secuencia >> "+ promocionDetalle.getSecuencia());
        Log.d("PedidosActivity :onItemClick:", "agrupado >> "+ promocionDetalle.getAgrupado());
        Log.d("PedidosActivity :onItemClick:", "entrada >> "+ itemDetalleGlobal.getCip());
        Log.d("PedidosActivity :onItemClick:","salida >> " + promocionDetalle.getSalida());

        DBPedido_Detalle dbdetalle_temporal = null;
        /**COMENTADO, XK AHORA CADA VEZ QUE HAY SALIDA DE PROMOCION. VIENE CON SU PROPIO ITEM DE REGALO,
        PESE SER MISMO PRODUCTO O MISMA SECUENCIA DE PROMOCION.
        dbdetalle_temporal = dbclass.existePedidoDetalle2(
                Oc_numero,
                "B"+promocionDetalle.getSalida(),
                dbclass.obtener_codunimedXtipo_unimed_salida(Integer.parseInt(promocionDetalle.getTipo_unimed_salida()),
                promocionDetalle.getSalida()));
         */

        int nroItemDetalleSalida = dbclass.getNextNroItemPedido(Oc_numero);

        cantidadActualizada = RegistroBonificacion(Oc_numero, promocionDetalle,
                itemDetalleGlobal, cantidadBonificada, listaEntradasCompuestas,
                cantidadesUsadas, montosUsados, nroItemDetalleSalida);


        if (dbdetalle_temporal != null) {
            Log.d("PedidosActivity ","el producto de regalo(bonificacion) ya esta registrado.... actualizando");
            if (!dbdetalle_temporal.getCod_politica().equals("ELIM")) {

                dbclass.actualizarItem_promo(
                        Oc_numero,
                        "B"+promocionDetalle.getSalida(),
                        dbclass.obtener_codunimedXtipo_unimed_salida(Integer.parseInt(promocionDetalle.getTipo_unimed_salida()),"B"+promocionDetalle.getSalida()),
                        cantidadActualizada);

            }
        } else {
            Log.d("PedidosActivity ","el producto de regalo(bonificacion) no esta registrado.... agregando cantidad: "+ cantidadActualizada);

            String codproEntrada= "";
            int itemEtrada= 0;
            if(listaEntradasCompuestas.size()>0){
                String[] listEntradaCompuesto= listaEntradasCompuestas.get(0);
                codproEntrada= listEntradaCompuesto[0];
                itemEtrada= Integer.parseInt(listEntradaCompuesto[8]);
            }else{
                codproEntrada= itemDetalleGlobal.getCip();
                itemEtrada= itemDetalleGlobal.getItem();
            }

            agregarProductoxPromocion(
                    promocionDetalle.getSalida(),
                    promocionDetalle.getTipo_unimed_salida(),
                    cantidadActualizada,
                    promocionDetalle.getSecuencia(),
                    promocionDetalle.getItem(),
                    promocionDetalle.getPrioridad(),
                    nroItemDetalleSalida,
                    codproEntrada,
                    itemEtrada
                    );
        }

        if(cantidadActualizada>0){
            if (promocionDetalle.getTipo_promocion().equals(DB_PromocionDetalle.salidaBonificacionXCOMBO)) {
                registrarXcomboDetalle(promocionDetalle.getSecuencia(), cantidadActualizada, nroItemDetalleSalida );
            }
        }

        dbclass.updatePedidoDetalle_SecuenciaPromocion(Oc_numero,  ""+promocionDetalle.getSecuencia(), promocionDetalle.getItem(), promocionDetalle.getPrioridad(), nroItemDetalleSalida );
        Log.e("","=======================================================================");
        Gson gson = new Gson();
        Log.d("", "Oc_numero--> " + Oc_numero);
        Log.d("", "promocionDetalle--> " + gson.toJson(promocionDetalle));
        Log.d("", "itemDetalleGlobal--> " + gson.toJson(itemDetalleGlobal));
        Log.d("", "cantidadBonificada--> " + cantidadBonificada);
        Log.d("", "entradasCompuestas--> " + gson.toJson(entradasCompuestas));

        Log.d("listaRegistroBonificacaciones",
                gson.toJson(DAOBonificaciones.ObtenerRegistroBonificaciones()));
        Log.d("listaPedidoDetalle",
                gson.toJson(dbclass.getPedidosDetallexOc_numero(Oc_numero)));
//        mostrarListaProductos("B"+promocionDetalle.getSalida());
    }

    private void registrarXcomboDetalle(int secuenciaPromocion, int cantidadActualizada, int nroItemDetalleSalida) {
        dao_pedido_detalle2= dao_pedido_detalle2!=null? dao_pedido_detalle2: new DAO_Pedido_detalle2(this);

        UtilCalcularPrecioProducto utilCaclularPrecioProductoUnit= new UtilCalcularPrecioProducto(
                dbclass, codcli, codigoMoneda
        );

        DAO_PromocionDetalleProducto dao_promoP = new DAO_PromocionDetalleProducto(this);
        ArrayList<PromocionDetalleProducto> listaBon = dao_promoP.getDataByID(secuenciaPromocion);
        ArrayList<Pedido_detalle2 > listaPedidoDeta2=new ArrayList<>();
        for (PromocionDetalleProducto promDetCombo : listaBon) {
            UtilCalcularPrecioProducto.ResultPrecios resulPrecio = utilCaclularPrecioProductoUnit.consultarPrecios(promDetCombo.getCodpro_bonificacion(),    itemDetalleGlobal.getPorcentaje_desc(), itemDetalleGlobal.getPorcentaje_desc_extra());
            if (resulPrecio.errorMensaje!=null) {
                GlobalFunctions.showCustomToast(this,  "Producto promociónXcombo detalle no se agregó motivo a: \n\n"+resulPrecio.errorMensaje, GlobalFunctions.TOAST_ERROR);
                listaPedidoDeta2.clear();
                break;
            }
            double pesoProducto= dbclass.getPesoProducto(promDetCombo.getCodpro_bonificacion());
            int cantidadBonifXcombo= (promDetCombo.getCantidad() * cantidadActualizada);
            double precioLista = Double.parseDouble(resulPrecio.precioLista.replace(",",""));
            double precioListaTotaL =VARIABLES.getDoubleFormaterThreeDecimal(precioLista*cantidadBonifXcombo);
            double precioVentaSinIgv =Double.parseDouble(resulPrecio.precioVentaPreSinIGV.replace(",",""));
            double precioUnitTotal =VARIABLES.getDoubleFormaterThreeDecimal(precioVentaSinIgv*cantidadBonifXcombo);
            double descuentoTotal =VARIABLES.getDoubleFormaterThreeDecimal(precioListaTotaL-precioUnitTotal);
            double pesoTotal =VARIABLES.getDoubleFormaterThreeDecimal(pesoProducto*cantidadBonifXcombo);
            double desc1= precioLista==0?100:itemDetalleGlobal.getPorcentaje_desc();
            double descExtra=precioLista==0?100:itemDetalleGlobal.getPorcentaje_desc_extra();

            Pedido_detalle2 pedDeta2 = new Pedido_detalle2(
                    Oc_numero,
                    secuenciaPromocion,
                    nroItemDetalleSalida,
                    promDetCombo.getCodpro_bonificacion(),
                    cantidadBonifXcombo,
                    Double.parseDouble(resulPrecio.precioLista),
                    desc1,
                    descExtra,
                    Double.parseDouble(resulPrecio.precioVentaPreSinIGV),
                    precioUnitTotal,
                    descuentoTotal,
                    pesoTotal
            );
            listaPedidoDeta2.add(pedDeta2);
        }


        dao_pedido_detalle2.DeleteItemByPromocion(Oc_numero, secuenciaPromocion, nroItemDetalleSalida);
        for (int i = 0; i < listaPedidoDeta2.size(); i++) {
            dao_pedido_detalle2.InsertItem(listaPedidoDeta2.get(i), null);
        }

    }

    @Override
    public void onDialogClickPositivo() {
        // TODO Auto-generated method stub

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

    private void modifyPromotionColorsOrXComboIfExists(int secPromocion, String tipoPromocion, int salidaItem, int cantidadCombo ) {
        if (tipoPromocion.equals(DB_PromocionDetalle.salidaBonificacionXCOLORES)){
//            dbclass.updateFlagPedidoCabecera(Oc_numero, DBPedido_Cabecera.FLAG_O, "","");
//            PromocionColoresFragment promoColores=new PromocionColoresFragment(Oc_numero,secPromocion);
//            promoColores.show(getSupportFragmentManager(),"promoColores");
//            promoColores.setCallback(() -> mostrarListaProductos());
        }
        else if (tipoPromocion.equals(DB_PromocionDetalle.salidaBonificacionXCOMBO)){
            //dao_pedido_detalle2.InsertPromotionXCombo(Oc_numero,  secPromocion, salidaItem, cantidadCombo);
            registrarXcomboDetalle(secPromocion, cantidadCombo, salidaItem);
        }
    }

    private void DeletePromocionComboOrColorsIfExist(int secuenciaPromo, int salidaItem){
        dao_pedido_detalle2.DeleteItemByPromocion(Oc_numero, secuenciaPromo, salidaItem);
    }


    @Override
    public void onRequestPermissionsResult( int requestCode, String permissions[], int[] grantResults) {
        boolean permitido=false;
        if (grantResults.length > 0) {
            permitido=true;
            for (int a=0;a<grantResults.length;a++){
                if (grantResults[a] != PackageManager.PERMISSION_GRANTED) {
                    permitido=false;
                    break;
                }
            }
            if (permitido){
                Pre_StartUbicacionApiGoogle();
            }
        }
        if (!permitido){
            UtilViewMensaje.MENSAJE_simple(PedidosActivity.this, "Ubicación", "Permiso de ubicación sin conceder. El pedido se guardará como pendiente");
        }

    }

    private void ApiLocationGoogleConectar(){
        if (locationApiGoogle.googleApiClient != null) {
            locationApiGoogle.googleApiClient.connect();
        }
    }

    private void Pre_StartUbicacionApiGoogle(){
        new RequestPermisoUbicacion(this, PERMISO_PARA_ACCEDER_A_LOCALIZACION, new RequestPermisoUbicacion.MyListener() {
            @Override
            public void Result(int isConcedido) {
                if (Permiso_Adroid.IS_PERMISO_DENEGADO==isConcedido){
                    UtilViewMensaje.MENSAJE_simple(PedidosActivity.this, "Permiso denegado", "No podras acceder a la ubicación");
                }
                else if (Permiso_Adroid.IS_PERMISO_CONCEDIDO==isConcedido){
                    Log.i(TAG, "permiso concedido");
                    StartUbicacionApiGoogle();
                    ApiLocationGoogleConectar();
                }
            }
        });
    }

    private void StartUbicacionApiGoogle(){
        locationApiGoogle=new LocationApiGoogle(this, new LocationApiGoogle.Listener() {
            @Override
            public void onConnected(Bundle bundle) {

                taskCheckUbicacion= new TaskCheckUbicacion(PedidosActivity.this, new TaskCheckUbicacion.MyListener() {
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
                    lat =(location.getLatitude());
                    lng =location.getLongitude();

                    Log.i(TAG, "StartUbicacionApiGoogle:: LastLocation:: Latitude : " + location.getLatitude() + "Longitude : " + location.getLongitude());
                }
            }
        });

    }


}

