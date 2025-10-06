package com.example.sm_tubo_plast.genesys.fuerza_ventas;

import android.R.layout;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.BEAN.ItemProducto;
import com.example.sm_tubo_plast.genesys.DAO.DAO_Producto;
import com.example.sm_tubo_plast.genesys.DAO.DAO_RegistroBonificaciones;
import com.example.sm_tubo_plast.genesys.adapters.CH_Adapter_bonificacionesPendientes;
import com.example.sm_tubo_plast.genesys.adapters.ModelBonificacionPendiente;
import com.example.sm_tubo_plast.genesys.datatypes.DBPedido_Detalle;
import com.example.sm_tubo_plast.genesys.datatypes.DBPolitica_Precio2;
import com.example.sm_tubo_plast.genesys.datatypes.DBSync_soap_manager;
import com.example.sm_tubo_plast.genesys.datatypes.DBclasses;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.compartidoUtil.UtilCalcularPrecioProducto;
import com.example.sm_tubo_plast.genesys.util.Dialog.AlertViewSimpleConEdittext;
import com.example.sm_tubo_plast.genesys.util.EditTex.ACG_EditText;
import com.example.sm_tubo_plast.genesys.util.GlobalFunctions;
import com.example.sm_tubo_plast.genesys.util.SnackBar.UtilViewSnackBar;
import com.example.sm_tubo_plast.genesys.util.VARIABLES;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;


@SuppressLint("LongLogTag")
public class ProductoActivity extends AppCompatActivity implements OnClickListener {


    private static final String TAG = "ProductoActivity";

    private static final String DISCONTINUO = "1";

    public final static String BUSQUEDA_DESCRIPCION = "descripcion";
    public final static String BUSQUEDA_PROVEEDOR = "proveedor";
    public final static String BUSQUEDA_DESCRIPCION_COMERCIAL = "descripcion_comercial";
    public final static String BUSQUEDA_CODIGO = "codigo";

    public final static String REQUEST_ACCION_PRODUCTO_KEY="REQUEST_ACCION_PRODUCTO_KEY";
    private String REQUEST_ACCION_PRODUCTO_VALUE =null;
    public final static String REQUEST_ACCION_AGREGAR_PRODUCTO ="REQUEST_ACCION_AGREGAR_PRODUCTO";
    public final static String REQUEST_ACCION_MODIFICAR_PRODUCTO="REQUEST_ACCION_MODIFICAR_PRODUCTO";
    public final static String REQUEST_DATA_PRODUCTO_KEY="REQUEST_PRODUCTO_MODIFICAR_KEY";
    private ItemProducto.DataEdit REQUEST_DATA_PRODUCTO_VALUE=null;

    private int clienteTienePercepcion;
    private int clienteTienePercepcionEspecial;
    private double clienteValorPercepcion;

    boolean PROMOCION = true;
    DBclasses obj_dbclasses;
    TextView tv_nombre_producto;
    ListView lstProductos;
    EditText txtProducto, edtBusqueda, edtPrecioUnt, edtCantidad, edt_descuento, edtDescuentoExtra;
    ProgressDialog pDialog;
    TableLayout tabla_datos;
    Button btnAgregar, btnCancelar, btnPromociones,btn_consultarProducto;
    ImageButton btnBuscar;
    ImageButton btnBonificaciones;
    Spinner spnUndMedidas;
    LinearLayout lnPrecUnt, lnPolPrec;
    TextView tv_monedaPrecio;

    String codcli = "", codven = "", codprod = "", origen = "", oc_numero = "";
    String codigoMoneda,codigoCondicionVenta,codigoLetraCondicionVenta,fechaEntrega,descuento,codigoSucursal,codigoLugarEntrega,codigoTipoDespacho,flagDescuento,codigoAlmacenDespacho,estado;
    ItemProducto[] productos;

    ItemProducto[] pol_prec, politica_cliente;
    ItemProducto auxiliar;
    String sec_politica = "";
    int fact_conv;
    String codigo_act;
    double prec_act, precUnd_act, subtotal, precio, peso, stock;
    Dialog dialog_promociones;
    private RadioGroup rb_filtro;
    boolean hayPrecio = false;
    String _precioUnitario;
    public int flag = 0;
    int art = 0;

    //
    ImageButton btn_scan;
    SharedPreferences preferencias_configuracion;
    SharedPreferences.Editor editor_preferencias;

    TextView tvGetPctDescuentoPorPrecio, tv_precioSinIGV,tv_precioIncIGV, tv_fechaUltimaVenta, tv_precioUltimaVenta,tv_descuentoPVP, tvTotalVenta;
    TextView tv_totalStockConfirmar, tv_totalStockDisponible;
    ListView lv_consultaStock,lv_consultaPrecios;
    //Switch swt_afecto;
    ToggleButton swt_afecto;
    CheckBox check_precio;
    Switch swAgregarComoNuevoProducto, swAgregarComoBonificacion;

    private double descuentoMin = 0.0;
    private double descuentoMax = 0.0;
    private double valorIGV;
    private double finalvalorIGV;

    private double totalStockConfirmar = 0.0d;
    private double totalStockDisponible = 0.0d;
    private String flagMsPack;
    int tamanioItem;
    boolean isBonificacion = false;
    int saldo = 0;
    String codigoRegistro = "";
    String codigoSalida = "";

    DBclasses database;
    ArrayList<ModelBonificacionPendiente> listaBonificacionesPendientes;
    DAO_RegistroBonificaciones DAOBonificaciones;
    DAO_Producto DAOProducto;

    String descuentoAplicado = "0";
    String PRECIO_BASE = "0";
    String PRECIO_LISTA = "0";
    String afecto_igv="0";
    UtilCalcularPrecioProducto utilCaclularPrecioProductoUnit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);

        Bundle bundle = getIntent().getExtras();
        REQUEST_ACCION_PRODUCTO_VALUE = bundle.getString(REQUEST_ACCION_PRODUCTO_KEY, null);
        REQUEST_DATA_PRODUCTO_VALUE=(ItemProducto.DataEdit) bundle.getSerializable(REQUEST_DATA_PRODUCTO_KEY);
        codcli = "" + bundle.getString("codcli");
        codven = "" + bundle.getString("codven");
        origen = "" + bundle.getString("origen");
        oc_numero = "" + bundle.getString("oc_numero");
        codigoMoneda 		= "" + bundle.getString("codigoMoneda");
        codigoCondicionVenta= "" + bundle.getString("codigoCondicionVenta");
        codigoLetraCondicionVenta= "" + bundle.getString("codigoLetraCondicionVenta");
        fechaEntrega 		= "" + bundle.getString("fechaEntrega");
        codigoSucursal 		= "" + bundle.getString("codigoSucursal");
        codigoLugarEntrega	= "" + bundle.getString("codigoLugarEntrega");
        codigoTipoDespacho 	= "" + bundle.getString("codigoTipoDespacho");
        codigoAlmacenDespacho = "" + bundle.getString("codigoAlmacenDespacho");
        flagDescuento		= "" + bundle.getString("flagDescuento");
        flagMsPack			= "" + bundle.getString("flagMsPack");

        clienteTienePercepcion			= bundle.getInt("clienteTienePercepcion");
        clienteTienePercepcionEspecial	= bundle.getInt("clienteTienePercepcionEspecial");

        Log.d("codcli",bundle.getString("codcli"));
        Log.d("codven",bundle.getString("codven"));
        Log.d("origen",bundle.getString("origen"));
        Log.d("fechaEntrega",bundle.getString("fechaEntrega"));
        // variables importante para calcular percepcion

        if(REQUEST_ACCION_PRODUCTO_VALUE==null || REQUEST_DATA_PRODUCTO_VALUE==null){
            Toast.makeText(this, "Uno o mas parametros no recibidos", Toast.LENGTH_SHORT).show();
            finish();
        }

        database = new DBclasses(getApplicationContext());
        DAOBonificaciones = new DAO_RegistroBonificaciones(getApplicationContext());
        DAOProducto		  = new DAO_Producto(getApplicationContext());

        preferencias_configuracion = getSharedPreferences("preferencias_configuracion", Context.MODE_PRIVATE);
        obj_dbclasses = new DBclasses(getApplicationContext());
        valorIGV = preferencias_configuracion.getFloat("valorIGV", 0.0f);
        descuentoMax = preferencias_configuracion.getFloat("limiteDescuento", 2.0f);

        if (bundle.getString("clienteValorPercepcion") != null) {
            try {
                clienteValorPercepcion = Double.parseDouble(bundle.getString("clienteValorPercepcion"));
            } catch (Exception ex) {
                Log.e("ProductoActivity ::bundle.getDouble(CLIENTE_VALOR_PERCEPCION)::", ex.toString());
                clienteValorPercepcion = 0.0;
            }
        } else {
            clienteValorPercepcion = 0.0;
        }

        Log.d("ProductoActivity ::datos de percepcion cliente::","clienteTienePercepcion "+clienteTienePercepcion);
        Log.d("ProductoActivity ::datos de percepcion cliente::","clienteTienePercepcionEspecial "+clienteTienePercepcionEspecial);
        Log.d("ProductoActivity ::datos de percepcion cliente::","clienteValorPercepcion "+clienteValorPercepcion);

        rb_filtro = (RadioGroup) findViewById(R.id.rgroup_busqueda);
        edtBusqueda = (EditText) findViewById(R.id.edt_busqueda);
        btnBuscar = (ImageButton) findViewById(R.id.btn_buscar);
        edtPrecioUnt = (EditText) findViewById(R.id.productolyt_edtPreciount);
        edtCantidad = (EditText) findViewById(R.id.productolyt_edtCantidad);
        spnUndMedidas = (Spinner) findViewById(R.id.productolyt_spnUndmed);
        edt_descuento = (EditText) findViewById(R.id.edt_descuento);
        edtDescuentoExtra = (EditText) findViewById(R.id.edtDescuentoExtra);
        btn_consultarProducto = (Button) findViewById(R.id.btn_consultarProducto);
        btnBonificaciones = (ImageButton) findViewById(R.id.productolyt_btnBonificacion);

        btnCancelar = (Button) findViewById(R.id.productolyt_btnCancelar);
        //swt_afecto = (Switch)findViewById(id.swt_afecto);
        swt_afecto = (ToggleButton)findViewById(R.id.swt_afecto);
        tv_precioSinIGV = (TextView) findViewById(R.id.tv_precioSinIGV);
        tvGetPctDescuentoPorPrecio = (TextView) findViewById(R.id.tvGetPctDescuentoPorPrecio);
        tv_precioIncIGV = (TextView) findViewById(R.id.tv_precioIncIGV);
        btnAgregar = (Button) findViewById(R.id.productolyt_btnAgregar);
        lv_consultaStock = (ListView) findViewById(R.id.lv_consultaStock);
        lv_consultaPrecios = (ListView) findViewById(R.id.lv_consultaPrecios);
        tv_descuentoPVP=(TextView)findViewById(R.id.tv_descuentoPVP);
        tvTotalVenta=(TextView)findViewById(R.id.tvTotalVenta);

        tv_fechaUltimaVenta = (TextView) findViewById(R.id.tv_fechaUltimaVenta);
        tv_precioUltimaVenta = (TextView) findViewById(R.id.tv_precioUltimaVenta);
        tv_totalStockConfirmar = (TextView) findViewById(R.id.tv_totalStockConfirmar);
        tv_totalStockDisponible = (TextView) findViewById(R.id.tv_totalStockDisponible);
        tv_monedaPrecio = (TextView)findViewById(R.id.tv_monedaPrecio);

        check_precio = (CheckBox) findViewById(R.id.check_precio);
        swAgregarComoBonificacion = (Switch) findViewById(R.id.swAgregarComoBonificacion);
        swAgregarComoNuevoProducto = (Switch) findViewById(R.id.swAgregarComoNuevoProducto);
        check_precio.setChecked(true);
        check_precio.setClickable(false);
        swAgregarComoNuevoProducto.setVisibility(View.GONE);

        if (codigoMoneda.equals(PedidosActivity.MONEDA_SOLES_IN)) {
            tv_monedaPrecio.setText("S/.");
        }else{
            tv_monedaPrecio.setText("$.");
        }
        gestionarTituloYSubtitulo();
		/*
		@SuppressWarnings("deprecation")
		final GestureDetector gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
		    public boolean onDoubleTap(MotionEvent e) {
		        GlobalFunctions.showCustomToast(ProductoActivity.this, "double !", GlobalFunctions.TOAST_DONE);
		        return true;
		    }
		});

		edtPrecioUnt.setOnTouchListener(new OnTouchListener() {
		    public boolean onTouch(View v, MotionEvent event) {
		        return gestureDetector.onTouchEvent(event);
		    }
		});
		*/

        check_precio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                final String precioProducto = edtPrecioUnt.getText().toString().trim().length()==0?"0":edtPrecioUnt.getText().toString();
                if (!precioProducto.equals("") || precioProducto.length() != 0) {
                    if (isChecked) {
                        if(Double.parseDouble(precioProducto)>Double.parseDouble(PRECIO_BASE) ){
                            PRECIO_BASE=precioProducto;
                            tv_precioSinIGV.setText(GlobalFunctions.redondear(String.valueOf(precioProducto)));
                            tv_precioSinIGV.setTextColor(getResources().getColor(R.color.green_800));
                            double precioIncIGV = Double.parseDouble(precioProducto) * (1 + finalvalorIGV);
                            tv_precioIncIGV.setText(GlobalFunctions.redondear(String.valueOf(precioIncIGV)));
                            tv_precioIncIGV.setTextColor(getResources().getColor(R.color.green_800));

                            edt_descuento.setText("");
                            edt_descuento.setEnabled(false);
                            edtDescuentoExtra.setText("");
                            edtPrecioUnt.setEnabled(false);
                        }else{
                            edtPrecioUnt.setText(PRECIO_BASE);
                            tv_precioSinIGV.setText(GlobalFunctions.redondear(String.valueOf(PRECIO_BASE)));
                            tv_precioSinIGV.setTextColor(getResources().getColor(R.color.green_500));
                            double precioIncIGV = Double.parseDouble(PRECIO_BASE) * (1 + finalvalorIGV);
                            tv_precioIncIGV.setText(GlobalFunctions.redondear(String.valueOf(precioIncIGV)));
                            tv_precioIncIGV.setTextColor(getResources().getColor(R.color.green_500));

                            edt_descuento.setText("");
                            edt_descuento.setEnabled(true);
                            edtDescuentoExtra.setText("");
                            edtPrecioUnt.setEnabled(false);
                        }
                    }else{
                        edtPrecioUnt.setText(PRECIO_BASE);
                        //quitar el precio sin IGV con IGV
                        edt_descuento.setText("");
                        edtDescuentoExtra.setText("");
                        tv_precioSinIGV.setText("");
                        tv_precioIncIGV.setText("");
                        edtPrecioUnt.setEnabled(true);
                    }
                }
            }
        });


        new ACG_EditText(this, edt_descuento).OnListen(texto ->{
            edt_descuento.setError(null);
            edtDescuentoExtra.setEnabled(false);
            if (edt_descuento.getText().toString().trim().length()>0) {
                try {
                    double porcentaje=Double.parseDouble(edt_descuento.getText().toString());
                    if (porcentaje>=0 && porcentaje<=100){
                        edtDescuentoExtra.setEnabled(true);
                        ConsultarProductoPrecios();
                    }else{
                        edt_descuento.setText("");
                        edt_descuento.setError("Ingrese un número de 1 a 100");
                        edtDescuentoExtra.setText("");
                    }
                }catch (Exception e){
                    edt_descuento.setText("");
                    edt_descuento.setError("Ingrese un número válido");
                    edtDescuentoExtra.setText("");
                    e.printStackTrace();
                }
            }else{
                edtDescuentoExtra.setText("");
                ConsultarProductoPrecios();
            }
        });

        new ACG_EditText(this, edtDescuentoExtra).OnListen(texto ->{

            if (texto.length()>0) {
                try {
                    double porcentaje=Double.parseDouble(texto);
                    if (porcentaje>=0 && porcentaje<=100){
                        ConsultarProductoPrecios();
                    }else{
                        edtDescuentoExtra.setText("");
                        edtDescuentoExtra.setError("Ingrese un número de 1 a 100");
                    }
                }catch (Exception e){
                    edtDescuentoExtra.setText("");
                    edtDescuentoExtra.setError("Ingrese un número válido");
                    e.printStackTrace();
                }
            }else{
                ConsultarProductoPrecios();
            }
        });

        new ACG_EditText(this, edtCantidad).OnListen(texto ->{
            if (texto.length()>0){
                if (VARIABLES.IsDouble(texto)){
                    calcularPrecioTotalVenta();
                }
            }
            else calcularPrecioTotalVenta();
        }
        );



        edtBusqueda.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                edtBusqueda.setText("");
                edtCantidad.setText("");
                edtPrecioUnt.setText("");
                edt_descuento.setText("");
                tv_precioSinIGV.setText("");
                tv_precioIncIGV.setText("");
                tv_descuentoPVP.setText("");
                tv_fechaUltimaVenta.setText("");
                tv_precioUltimaVenta.setText("");
                totalStockConfirmar = 0.0;
                totalStockDisponible = 0.0;

                codprod = "";
                swt_afecto.setChecked(false);
                btn_consultarProducto.setEnabled(true);

                edtPrecioUnt.setEnabled(false);
                check_precio.setChecked(true);
                check_precio.setClickable(false);
            }
        });

        edtBusqueda.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    preValidarBuscarProducto();
                    return true;
                } else {
                    return false;
                }
            }
        });


        btnBuscar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                preValidarBuscarProducto();
            }
        });

        btnBonificaciones.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new async_busquedaBonificaciones().execute();
            }
        });

        btn_consultarProducto.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //ConsultarProducto();//solo chema
                return true;
            }
        });
        btn_consultarProducto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ConsultarProductoPrecios();//solo demo
            }


        });


        btnCancelar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductoActivity.this.finish();
            }
        });

        btnAgregar.setOnClickListener(this);

        rb_filtro.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                switch (arg1) {
                    case R.id.rbtn_descripcion:
                        edtBusqueda.setText("");
                        edtCantidad.setText("");
                        edtPrecioUnt.setText("");
                        edt_descuento.setText("");
                        tv_precioSinIGV.setText("");
                        tv_precioIncIGV.setText("");
                        tv_descuentoPVP.setText("");
                        tv_fechaUltimaVenta.setText("");
                        tv_precioUltimaVenta.setText("");
                        codprod = "";
                        totalStockConfirmar = 0.0;
                        totalStockDisponible = 0.0;

                        edtBusqueda.setHint("Descripcion");
                        edtBusqueda.setInputType(InputType.TYPE_CLASS_TEXT);
                        btn_consultarProducto.setEnabled(true);
                        flag = 0;

                        editor_preferencias = preferencias_configuracion.edit();
                        editor_preferencias.putString("preferencias_busquedaProducto",BUSQUEDA_DESCRIPCION);
                        editor_preferencias.commit();
                        break;
                    case R.id.rbtn_descripcion_comercial:
                        edtBusqueda.setText("");
                        edtCantidad.setText("");
                        edtPrecioUnt.setText("");
                        edt_descuento.setText("");
                        tv_precioSinIGV.setText("");
                        tv_precioIncIGV.setText("");
                        tv_descuentoPVP.setText("");
                        tv_fechaUltimaVenta.setText("");
                        tv_precioUltimaVenta.setText("");
                        codprod = "";
                        totalStockConfirmar = 0.0;
                        totalStockDisponible = 0.0;

                        edtBusqueda.setHint("Descr comercial");
                        edtBusqueda.setInputType(InputType.TYPE_CLASS_TEXT);
                        btn_consultarProducto.setEnabled(true);
                        flag = 1;

                        editor_preferencias = preferencias_configuracion.edit();
                        editor_preferencias.putString("preferencias_busquedaProducto",BUSQUEDA_DESCRIPCION);
                        editor_preferencias.commit();
                        break;
                    case R.id.rbtn_codigoProducto:
                        edtBusqueda.setText("");
                        edtCantidad.setText("");
                        edtPrecioUnt.setText("");
                        edt_descuento.setText("");
                        tv_precioSinIGV.setText("");
                        tv_precioIncIGV.setText("");
                        tv_descuentoPVP.setText("");
                        tv_fechaUltimaVenta.setText("");
                        tv_precioUltimaVenta.setText("");
                        codprod = "";
                        totalStockConfirmar = 0.0;
                        totalStockDisponible = 0.0;

                        edtBusqueda.setHint("Codigo");
                        edtBusqueda.setInputType(InputType.TYPE_CLASS_TEXT);
                        btn_consultarProducto.setEnabled(true);
                        flag = 2;

                        editor_preferencias = preferencias_configuracion.edit();
                        editor_preferencias.putString("preferencias_busquedaProducto",BUSQUEDA_CODIGO);
                        editor_preferencias.commit();
                        break;
                    case R.id.rbtn_proveedor:
                        edtBusqueda.setText("");
                        edtCantidad.setText("");
                        edtPrecioUnt.setText("");
                        edt_descuento.setText("");
                        tv_precioSinIGV.setText("");
                        tv_precioIncIGV.setText("");
                        tv_descuentoPVP.setText("");
                        tv_fechaUltimaVenta.setText("");
                        tv_precioUltimaVenta.setText("");
                        codprod = "";
                        totalStockConfirmar = 0.0;
                        totalStockDisponible = 0.0;

                        edtBusqueda.setHint("Codigo Proveedor");
                        edtBusqueda.setInputType(InputType.TYPE_CLASS_NUMBER);
                        btn_consultarProducto.setEnabled(true);
                        flag = 3;

                        editor_preferencias = preferencias_configuracion.edit();
                        editor_preferencias.putString("preferencias_busquedaProducto",BUSQUEDA_PROVEEDOR);
                        editor_preferencias.commit();
                        break;
                    default:
                        break;
                }
            }
        });

        gestionarTipoFiltro();
        gestionarEventoSWProductoComoBonificacion();
        gestionarTvGetPctDescuentoPorPrecio();
        if(isRequestModifyProducto()){
            dishabilitarControlesByModificar();
            asyncBuscarProducto();
        }
        ///editText.setSelection(formattedValue.length());
    }
    private boolean isRequestModifyProducto(){
        return REQUEST_ACCION_PRODUCTO_VALUE.equals(REQUEST_ACCION_MODIFICAR_PRODUCTO);
    }
    private void dishabilitarControlesByModificar(){
        rb_filtro.setEnabled(false);
        rb_filtro.setVisibility(View.GONE);
        edtBusqueda.setEnabled(false);
        btn_consultarProducto.setEnabled(false);
        swAgregarComoBonificacion.setEnabled(false);
        swAgregarComoBonificacion.setVisibility(View.GONE);
        swAgregarComoNuevoProducto.setEnabled(false);
        swAgregarComoNuevoProducto.setVisibility(View.GONE);
    }
    private void gestionarTipoFiltro(){
        String preferencia_busquedaProducto = preferencias_configuracion.getString("preferencias_busquedaProducto", BUSQUEDA_DESCRIPCION);
        if(isRequestModifyProducto()){
            preferencia_busquedaProducto=BUSQUEDA_CODIGO;
        }

        switch (preferencia_busquedaProducto) {
            case BUSQUEDA_DESCRIPCION:
                ((RadioButton)rb_filtro.getChildAt(0)).setChecked(true);
                break;
            case BUSQUEDA_PROVEEDOR:
                ((RadioButton)rb_filtro.getChildAt(1)).setChecked(true);
                break;
            case BUSQUEDA_DESCRIPCION_COMERCIAL:
                ((RadioButton)rb_filtro.getChildAt(2)).setChecked(true);
                break;
            case BUSQUEDA_CODIGO:
                ((RadioButton)rb_filtro.getChildAt(3)).setChecked(true);
                break;
            default:
                break;
        }
    }

    private void gestionarTituloYSubtitulo() {
        Objects.requireNonNull(getSupportActionBar()).setSubtitle("Formulario producto");
        if(!isRequestModifyProducto()){
            btnAgregar.setText("AGREGAR");
        }
        else if(isRequestModifyProducto()){
                btnAgregar.setText("MODIFICAR");
        }
        else btnAgregar.setText("??");
    }

    private void gestionarEventoSWProductoComoBonificacion(){
        swAgregarComoBonificacion.setOnCheckedChangeListener((vd, isChecked)->{
            setEstadoInputDescuento();
        });
        setEstadoInputDescuento();
    }
    private void setEstadoInputDescuento(){
        edt_descuento.setText("");
        edtDescuentoExtra.setText("");
        if(swAgregarComoBonificacion.isChecked()){
            edt_descuento.setEnabled(false);
            edtDescuentoExtra.setEnabled(false);
        }else{
            edt_descuento.setEnabled(true);
            edtDescuentoExtra.setEnabled(true);
        }
    }
    private void gestionarTvGetPctDescuentoPorPrecio() {
        AtomicReference<AlertViewSimpleConEdittext> dialg=new AtomicReference<>();

        tvGetPctDescuentoPorPrecio.setOnClickListener(v -> {
            double precioSinIgv=Double.parseDouble(tv_precioSinIGV.getText().toString().replace(",", ""));
            dialg.set(new AlertViewSimpleConEdittext(this));
            dialg.get().cancelable = false;
            dialg.get().type_numberDecimal = true;
            dialg.get().titulo="Ingrese nuevo precio, Máximo "+tv_monedaPrecio.getText().toString()+" "+PRECIO_LISTA;
            dialg.get().texto_cargado = String.valueOf(precioSinIgv);
            dialg.get().start(new AlertViewSimpleConEdittext.Listener() {
                @Override
                public String resultOK(String s) {
                    if(s!=null){
                        Log.i(TAG, "gestionarTvGetPctDescuentoPorPrecio:: nuevo precio request "+s);
                        calcularDescuentoEnBaseANuevoPrecio(Double.parseDouble(s));
                    }
                    return null;
                }

                @Override
                public String resultBucle(String s) {
                    tvGetPctDescuentoPorPrecio.performClick();
                    return null;
                }
            });
        });

    }
    private void calcularDescuentoEnBaseANuevoPrecio(double newPrecioSinIgv){
        //si PRECIO_LISTA= (100)%
        //entonces newPrecioSinIgv = x
        //aplicando la regla de 3
        double maxDescuento=100.0;
        double precioLista=Double.parseDouble(PRECIO_LISTA);
        double pctPrecio=(newPrecioSinIgv*maxDescuento/precioLista);
        double pctDescuento=GlobalFunctions.redondear_toDouble(maxDescuento-pctPrecio);
        Log.i(TAG, "calcularDescuentoEnBaseANuevoPrecio:: descuento "+pctDescuento);
        edt_descuento.setText(String.valueOf(pctDescuento));
        edtDescuentoExtra.setText("0");
        //suficiente con setear el descuento, ya que el evento edt_descuento se encarga de calcular el precio
    }
    private void calcularPrecioTotalVenta() {
        int cantidad=Integer.parseInt(edtCantidad.getText().toString().trim().length()>0?edtCantidad.getText().toString():"0");
        double precioFinalSinIgv=0;
        double precioFinalConIgv=0;
        if(VARIABLES.IsDouble(tv_precioSinIGV.getText().toString().replace(",", "'"))){
            precioFinalSinIgv=Double.parseDouble(tv_precioSinIGV.getText().toString());
        }
        if(VARIABLES.IsDouble(tv_precioIncIGV.getText().toString().replace(",", "'"))){
            precioFinalConIgv=Double.parseDouble(tv_precioIncIGV.getText().toString());
        }
        tvTotalVenta.setText("SUB TOTAL "+tv_monedaPrecio.getText().toString()+" "+VARIABLES.formater_thow_decimal.format(precioFinalSinIgv*cantidad)+"     TOTAL: "+tv_monedaPrecio.getText().toString()+" "+VARIABLES.formater_thow_decimal.format(precioFinalConIgv*cantidad));
    }

    public void ConsultarProducto(){
        if(VARIABLES.isProduccion_prueba)return;
        final String precioGeneral = edtPrecioUnt.getText().toString();
        final String cantidad = edtCantidad.getText().toString();
        descuento = edt_descuento.getText().toString();
        if(descuento.equals("")) descuento="0.0";

        if (finalvalorIGV==0.0) {
            GlobalFunctions.showCustomToast(ProductoActivity.this, "valor IGV es 0.0 !", GlobalFunctions.TOAST_ERROR,GlobalFunctions.POSICION_BOTTOM);
        }else{
            if (!codprod.equals("") && !edtBusqueda.getText().equals("")) { //validacion del producto

                        if (precioGeneral.equals("") || precioGeneral.length() == 0) {	//validacion del precio (Cuando aun no se tiene precio)

                            descuento = "0.0";
                            descuentoAplicado = "0";


                            new AsyncTask<Void, Void, Void>() {
                                String respuestaPrecio,respuestaStock,respuestaListadoP;

                                @Override
                                protected void onPreExecute() {
                                    pDialog = new ProgressDialog(ProductoActivity.this);
                                    pDialog.setMessage("Consultando....");
                                    pDialog.setIndeterminate(false);
                                    pDialog.setCancelable(false);
                                    pDialog.show();
                                }

                                @Override
                                protected Void doInBackground(Void... params) {
                                    try {
                                        Log.d(TAG+"::", "codven: " + codven);
                                        Log.d(TAG+"::", "codprod: " + codprod);
                                        Log.d(TAG+"::", "precioGeneral: " + precioGeneral);
                                        Log.d(TAG+"::", "cantidad: " + cantidad);
                                        Log.d(TAG+"::", "flagDescuento: " + flagDescuento);
                                        Log.d(TAG+"::", "descuento: " + descuento);

                                        DBSync_soap_manager soap_manager = new DBSync_soap_manager(getApplicationContext());
                                        respuestaPrecio = soap_manager.sincro_obtenerPrecioProductoPVP_json(
                                                codcli,codprod,codigoMoneda,codigoCondicionVenta,cantidad,fechaEntrega,
                                                flagDescuento,descuento,codigoSucursal,codigoLugarEntrega,codigoAlmacenDespacho);

                                        Log.w("DATOS ENVIADOS PVP 2",codcli+","+codprod+","+codigoMoneda+","+codigoCondicionVenta+","+cantidad+","+fechaEntrega+","+
                                                ","+flagDescuento+","+descuento+","+codigoSucursal+","+codigoLugarEntrega+","+codigoAlmacenDespacho);

                                        respuestaStock = soap_manager.sincro_obtenerStockProducto_json(codprod);
                                        //NUEVO
                                       /* respuestaListadoP=soap_manager.sincro_obtenerListadoPrecios_json(
                                                codcli,codprod,codigoMoneda,codigoCondicionVenta,
                                                flagDescuento,codigoSucursal,codigoLugarEntrega,codigoAlmacenDespacho);*/

                                        Log.w("RESPUESTA",respuestaPrecio.toString());
                                        Log.w("RESPUESTA LISTADO",respuestaListadoP.toString());
                                    } catch (Exception e) {
                                        //Toast.makeText(getApplicationContext(), "Error al sincronizar", Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                    }
                                    return null;
                                }

                                @Override
                                protected void onPostExecute(Void result) {
                                    pDialog.dismiss();
                                    try {
                                        respuestConsultarProducto(respuestaPrecio,respuestaStock,respuestaListadoP);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }.execute();

                        }else{




                                    descuentoAplicado = descuento;
                                    new AsyncTask<Void, Void, Void>() {
                                        String respuestaPrecio,respuestaStock,respuestaListadoP;

                                        @Override
                                        protected void onPreExecute() {
                                            pDialog = new ProgressDialog(ProductoActivity.this);
                                            pDialog.setMessage("Consultando....");
                                            pDialog.setIndeterminate(false);
                                            pDialog.setCancelable(false);
                                            pDialog.show();
                                        }

                                        @Override
                                        protected Void doInBackground(Void... params) {
                                            try {
                                                Log.d(TAG+"::", "codven: " + codven);
                                                Log.d(TAG+"::", "codprod: " + codprod);
                                                Log.d(TAG+"::", "precioGeneral: " + precioGeneral);
                                                Log.d(TAG+"::", "cantidad: " + cantidad);
                                                Log.d(TAG+"::", "flagDescuento: " + flagDescuento);
                                                Log.d(TAG+"::", "descuento: " + descuento);

                                                DBSync_soap_manager soap_manager = new DBSync_soap_manager(getApplicationContext());
                                                respuestaPrecio = soap_manager.sincro_obtenerPrecioProductoPVP_json(
                                                        codcli,codprod,codigoMoneda,codigoCondicionVenta,cantidad,fechaEntrega,
                                                        flagDescuento,descuento,codigoSucursal,codigoLugarEntrega,codigoAlmacenDespacho);

                                                Log.w("DATOS ENVIADOS PVP 1",codcli+","+codprod+","+codigoMoneda+","+codigoCondicionVenta+","+cantidad+","+fechaEntrega+","+
                                                        ","+flagDescuento+","+descuento+","+codigoSucursal+","+codigoLugarEntrega+","+codigoAlmacenDespacho);


                                                respuestaStock = soap_manager.sincro_obtenerStockProducto_json(codprod);
                                                Log.w("RESPUESTA2",respuestaPrecio.toString());

                                                //NUEVO
                                                /*respuestaListadoP=soap_manager.sincro_obtenerListadoPrecios_json(
                                                        codcli,codprod,codigoMoneda,codigoCondicionVenta,
                                                        flagDescuento,codigoSucursal,codigoLugarEntrega,codigoAlmacenDespacho);*/




                                            } catch (Exception e) {
                                                new Runnable() {

                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(getApplicationContext(), "Error al sincronizar", Toast.LENGTH_SHORT).show();

                                                    }
                                                };
                                                e.printStackTrace();
                                            }
                                            return null;
                                        }

                                        @Override
                                        protected void onPostExecute(Void result) {
                                            pDialog.dismiss();
                                            try {
                                                totalStockConfirmar = 0.0;
                                                totalStockDisponible = 0.0;
                                                tv_totalStockConfirmar.setText(""+totalStockConfirmar);
                                                tv_totalStockDisponible.setText(""+totalStockDisponible);
                                                respuestConsultarProducto(respuestaPrecio,respuestaStock,respuestaListadoP);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                    }.execute();

                        }

            }else{
                edtBusqueda.setError(Html.fromHtml("<font color='#424242'>Producto no valido</font>"));
                edtBusqueda.requestFocus();
            }
        }
    }


    private void ConsultarProductoPrecios() {

        if (codprod.length()==0) return;

        edtPrecioUnt.setText("0.0");
        tv_precioIncIGV.setText("0.0");
        if(utilCaclularPrecioProductoUnit==null){
            utilCaclularPrecioProductoUnit= new UtilCalcularPrecioProducto(
                    obj_dbclasses, codcli, codigoMoneda
            );
        }
        double porcentajeDescuentoManual=edt_descuento.getText().toString().trim().length()>0?Double.parseDouble(edt_descuento.getText().toString()):0;
        double porcentajeDescuentoExtra= edtDescuentoExtra.getText().toString().trim().length()>0?Double.parseDouble(edtDescuentoExtra.getText().toString()):0;
        UtilCalcularPrecioProducto.ResultPrecios resulPrecio=utilCaclularPrecioProductoUnit.consultarPrecios(codprod, porcentajeDescuentoManual, porcentajeDescuentoExtra);
        if(resulPrecio.errorMensaje!=null){
            Toast.makeText(ProductoActivity.this, resulPrecio.errorMensaje, Toast.LENGTH_SHORT).show();
            return;
        }

        PRECIO_LISTA=resulPrecio.precioLista;
        btn_consultarProducto.setText(resulPrecio.smstxtPrecio);
        edtPrecioUnt.setText(resulPrecio.precioOriginal);

        descuentoAplicado= resulPrecio.descuentoSinIgvTotal;

        tv_precioSinIGV.setText(resulPrecio.precioVentaPreSinIGV); //precio sin IGV con descuento
        tv_precioIncIGV.setText(resulPrecio.precioVentaPreConIGV);//precio con IGV con descuento
        String mensjDescuento=(porcentajeDescuentoManual>0.0?
                "1 ("+porcentajeDescuentoManual+"%)":"")+" "+resulPrecio.descuentoSinIgv;
        if(porcentajeDescuentoExtra>0){
            String textDescuentExtra= "2 (" + porcentajeDescuentoExtra + "%)"
                    + " " + resulPrecio.descuentoSinIgvExtra;
            mensjDescuento+="\n"+textDescuentExtra;
        }
        tv_descuentoPVP.setText(mensjDescuento);
        calcularPrecioTotalVenta();

//        DecimalFormat formaterPrecioourDecimal = new DecimalFormat("#,##0.000");
//        formaterPrecioourDecimal.setRoundingMode(RoundingMode.HALF_UP);
//
//        edtPrecioUnt.setText("0.0");
//        tv_precioIncIGV.setText("0.0");
//
//
//        double valor_cambio=1;
//        if (codigoMoneda.equals(PedidosActivity.MONEDA_SOLES_IN)) {
//            String tipo_de_cambio  = obj_dbclasses.getCambio("Tipo_cambio");
//            valor_cambio  = Double.parseDouble(tipo_de_cambio);
//        }
//
//        DBPolitica_Precio2 politica_precio2=obj_dbclasses.GetPoliticaPrecio2ByCliente(codcli, codprod, valor_cambio);
//        if(politica_precio2!=null){
//            double porcentajeDescuentoManual=edt_descuento.getText().toString().trim().length()>0?Double.parseDouble(edt_descuento.getText().toString()):0;
//            double porcentajeDescuentoExtra= edtDescuentoExtra.getText().toString().trim().length()>0?Double.parseDouble(edtDescuentoExtra.getText().toString()):0;
//
//            double precioOriginal=(politica_precio2.getPrecio_base());
//            double precioListaConIgv=(politica_precio2.getPrepo_unidad()*1.18);
//            double precioListaSinIgv=(politica_precio2.getPrepo_unidad());
//            double porcentajeDescuentoSitema= Double.parseDouble(VARIABLES.ParseDecimalTwo(((precioOriginal-precioListaSinIgv)*100/precioOriginal)));
//
//            String sms="Precio Lista a "+formaterPrecioourDecimal.format(precioListaSinIgv);
//            if (porcentajeDescuentoSitema>0.0){
//                sms="Precio Lista con \n"+(porcentajeDescuentoSitema+porcentajeDescuentoManual)+"% dsc a "+formaterPrecioourDecimal.format(precioListaSinIgv);
//            }
//            PRECIO_LISTA=VARIABLES.ParseDecimalThree(precioListaSinIgv);
//
//
//            double descuentoConIgv=precioListaConIgv*(porcentajeDescuentoManual/100);
//            double descuentoSinIgv=politica_precio2.getPrepo_unidad()*(porcentajeDescuentoManual/100);
//
//            double precioVentaPreSinIGV=precioListaSinIgv-descuentoSinIgv;
//            double precioVentaPreIncIGV=precioListaConIgv-descuentoConIgv;
//
//            double descuentoSinIgvExtra=precioVentaPreSinIGV*(porcentajeDescuentoExtra/100);
//            double descuentoConIgvExtra=precioVentaPreIncIGV*(porcentajeDescuentoExtra/100);
//
//            double precioVentaFinalSinIGV=precioVentaPreSinIGV-descuentoSinIgvExtra;
//            double precioVentaFinalIncIGV=precioVentaPreIncIGV-descuentoConIgvExtra;
//
//            descuentoAplicado= VARIABLES.ParseDecimalThree(descuentoSinIgv+descuentoSinIgvExtra);
//
//            btn_consultarProducto.setText(sms);
//            edtPrecioUnt.setText(""+formaterPrecioourDecimal.format(precioOriginal));//precio original
//            tv_precioSinIGV.setText(""+formaterPrecioourDecimal.format(precioVentaPreSinIGV)); //precio sin IGV con descuento
//            tv_precioIncIGV.setText(""+formaterPrecioourDecimal.format(precioVentaPreIncIGV));//precio con IGV con descuento
//            tv_descuentoPVP.setText((porcentajeDescuentoManual>0.0?"1 ("+porcentajeDescuentoManual+"%)":"")+" "+formaterPrecioourDecimal.format(descuentoSinIgv));
//            if(porcentajeDescuentoExtra>0){
//                String textDescuentExtra= "2 (" + porcentajeDescuentoExtra + "%)" + " " + formaterPrecioourDecimal.format(descuentoSinIgvExtra);
//                tv_descuentoPVP.setText(tv_descuentoPVP.getText().toString()+"\n"+textDescuentExtra);
//                tv_precioSinIGV.setText("\n"+formaterPrecioourDecimal.format(precioVentaFinalSinIGV));
//                tv_precioIncIGV.setText("\n"+formaterPrecioourDecimal.format(precioVentaFinalIncIGV));
//            }
//            calcularPrecioTotalVenta();
//        }else{
//
//            Toast.makeText(ProductoActivity.this, "No se ha obtenido los precios para el producto", Toast.LENGTH_SHORT).show();
//        }
    }
    private void ResetearConsultaStock(){
        totalStockConfirmar =0;
        totalStockDisponible=0;

        tv_totalStockConfirmar.setText(""+totalStockConfirmar);
        tv_totalStockDisponible.setText(""+totalStockDisponible);

        final Adapter_consultaStock adapter = new Adapter_consultaStock(ProductoActivity.this, new JSONArray());
        lv_consultaStock.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void ResetearDatosPrecionOnLine(){
        tv_descuentoPVP.setText("");
        tv_precioSinIGV.setText("");
        tv_precioIncIGV.setText("");

        tv_descuentoPVP.setError(null);
        tv_precioSinIGV.setError(null);
        tv_precioIncIGV.setError(null);
    }
    private void respuestConsultarProducto(String respuestaPrecio, String respuestaStock,String respuestaListadoP) throws JSONException {

        if (!respuestaStock.equals("")) {
            final JSONArray listMap2 = new JSONArray(respuestaStock.toString());
            if (listMap2 != null) {
                final Adapter_consultaStock adapter = new Adapter_consultaStock(ProductoActivity.this, listMap2);
                lv_consultaStock.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                totalStockConfirmar = 0.0;
                totalStockDisponible = 0.0;

                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        if (lv_consultaStock.getCount()>0){
                            View vc=lv_consultaStock.getChildAt(0);
                            int tamanio_por_item=vc.getHeight()+5;
                            LayoutParams params = (LayoutParams) lv_consultaStock.getLayoutParams();
                            params.height = (lv_consultaStock.getCount() * tamanio_por_item);
                            params.width = LayoutParams.MATCH_PARENT;
                            lv_consultaStock.setLayoutParams(params);
                        }
                    }
                });


                for (int i=0;i<listMap2.length();i++){
                    try {

                        JSONObject jsonData  = listMap2.getJSONObject(i);
                        totalStockConfirmar += jsonData.getDouble("stock_x_confirmar");
                        totalStockDisponible += jsonData.getDouble("stock_disponible");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                tv_totalStockConfirmar.setText(""+totalStockConfirmar);
                tv_totalStockDisponible.setText(""+totalStockDisponible);

            }else{
                Toast.makeText(getApplicationContext(), "Sin lista de stock", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "No se pudo consultar stock, vuelva a intentarlo", Toast.LENGTH_SHORT).show();
        }

    }



    public void preValidarBuscarProducto(){
        try {
            if (flag == 0 || flag == 1) {
                if (edtBusqueda.getText().toString().length() < 3) {
                    edtBusqueda.setError(Html.fromHtml("<font color='#424242'>Ingrese al menos 3 caracteres</font>"));
                }
                else {
                    asyncBuscarProducto();
                }
            }
            else {
                //if (Integer.parseInt(edtBusqueda.getText().toString()) / 1 > 0) {
                    asyncBuscarProducto();
                //}
            }
        }
        catch (Exception e) {
            edtBusqueda.setError(Html.fromHtml("<font color='#424242'>Ingrese solo numeros</font>"));
        }
    }
    private void asyncBuscarProducto(){
        new async_busqueda().execute();
    }

    public void _buscarDatosProductoDB() {
        if (flag == 0 || flag == 1) {
            String text="%"+edtBusqueda.getText().toString().replace(" ", "%")+"%";
            if (flag == 0)productos = obj_dbclasses.getProductosXcliente(codcli,text);
            else productos = obj_dbclasses.getProductosXclienteYdescrip_comercial(codcli,text);
        }else if (flag == 3) {
            productos = obj_dbclasses.getProductosXProveedor(codcli,edtBusqueda.getText().toString());
        } else {
            if(isRequestModifyProducto()){
                productos = obj_dbclasses.getProductosXcliente_codpro(codcli,REQUEST_DATA_PRODUCTO_VALUE.getCodprod());
            }
            else productos = obj_dbclasses.getProductosXcliente_codpro(codcli,edtBusqueda.getText().toString());

        }

    }

    public void crear_vistaDialogo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Productos");

        ListView lista = new ListView(this);
///ok**********
        builder.setView(lista).setAdapter(
                new ProductoAdapter(ProductoActivity.this, productos),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        onItemClickListaProducto(position);
                    }
                });
        builder.create().show();
    }

    private void onItemClickListaProducto(int position) {
        afecto_igv = productos[position].getAfecto();
        edtBusqueda.setText(productos[position].getDescripcion());

        String undMedidas[] = obj_dbclasses.getUndmedidas(productos[position].getCodprod());
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(getApplicationContext(),R.layout.spinner_item,undMedidas);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spnUndMedidas.setAdapter(adapter);


        fact_conv 	= productos[position].getFact_conv();
        peso 		= VARIABLES.getDoubleFormaterFourDecimal(productos[position].getPeso());
        prec_act 	= Math.round(productos[position].getPrecio() * 100) / 100.0;
        precUnd_act = Math.round(productos[position].getPrecioUnidad() * 100) / 100.0;
        codigo_act 	= productos[position].getCodprod();
        codprod 	= productos[position].getCodprod();
        stock 		= productos[position].getStock();
        sec_politica= productos[position].getSec_politica();
        estado 		= productos[position].getEstado();//Discontinuo


        if (estado.equals(DISCONTINUO)) {
            GlobalFunctions.showCustomToast(ProductoActivity.this, "Producto discontinuo", GlobalFunctions.TOAST_WARNING,GlobalFunctions.POSICION_MIDDLE);
        }

        swt_afecto.setFocusable(false);




        if (afecto_igv.equals("0") || afecto_igv.equals("")) {
            swt_afecto.setChecked(false);
            finalvalorIGV=0;
        }else{
            swt_afecto.setChecked(true);
            finalvalorIGV=valorIGV;
        }

        if(!isRequestModifyProducto()){
            //verficamos si el producto ya esta registrado
            if (obj_dbclasses.isRegistradoProducto(oc_numero,codprod)
                    || obj_dbclasses.isRegistradoProducto(oc_numero,DBPedido_Detalle.PREFIX_PRODUCTO_BONIFICACION_MANUAL+codprod)) {
                swAgregarComoNuevoProducto.setVisibility(View.VISIBLE);
            }else {
                swAgregarComoNuevoProducto.setChecked(false);
                swAgregarComoNuevoProducto.setVisibility(View.GONE);
            }
        }



        edtCantidad.requestFocus();
        btn_consultarProducto.performClick();
        ConsultarProducto();//Consulta el stcok en linea
    }

    private void setDataProductoSiIsModificar(){
        if(isRequestModifyProducto()){
            edtCantidad.setText(""+REQUEST_DATA_PRODUCTO_VALUE.getCantidad());
            edt_descuento.setText(""+REQUEST_DATA_PRODUCTO_VALUE.getPorcentaje_desc());
            edtDescuentoExtra.setText(""+REQUEST_DATA_PRODUCTO_VALUE.getPorcentaje_desc_extra());
        }
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.productolyt_btnAgregar:

                String w_codpro_inser= codprod;
                int nro_item=0;
                if(swAgregarComoBonificacion.isChecked()){
                    w_codpro_inser = DBPedido_Detalle.PREFIX_PRODUCTO_BONIFICACION_MANUAL+codprod;
                }
                if(isRequestModifyProducto()){//Si es modificacion
                    nro_item=  REQUEST_DATA_PRODUCTO_VALUE.getItem();
                }else {//es nuevo producto
                    if(obj_dbclasses.isRegistradoProducto(oc_numero, w_codpro_inser)  ){
                        if(!swAgregarComoNuevoProducto.isChecked()){
                            UtilViewSnackBar.SnackBarDanger(this, swAgregarComoNuevoProducto, "Producto ya fue agregado anteriormente");
                            break;
                        }
                    }
                    nro_item=  obj_dbclasses.getNextNroItemPedido(oc_numero);
                }

                if(nro_item<=0) {
                    UtilViewSnackBar.SnackBarDanger(this, swAgregarComoBonificacion, "Error al obtener numero de item");
                    break;
                }

                int postion = spnUndMedidas.getSelectedItemPosition();
                if(postion<0){
                    UtilViewSnackBar.SnackBarDanger(this, swAgregarComoBonificacion, "Seleccione unidad de medida");
                    break;
                }
                String nombrex = spnUndMedidas.getSelectedItem().toString();
                if(nombrex.trim().isEmpty()){
                    UtilViewSnackBar.SnackBarDanger(this, swAgregarComoBonificacion, "Seleccione unidad de medida");
                    break;
                }

                if (isBonificacion) {
                    if (edtCantidad.getText().toString().matches("")|| (Integer.parseInt(edtCantidad.getText().toString())) == 0) {
                        edtCantidad.setError(Html.fromHtml("<font color='#424242'>Cantidad</font>"));
                        break;
                    }

                    int saldoUsado = Integer.parseInt(edtCantidad.getText().toString());
                    if (saldoUsado > saldo) {
                        edtCantidad.setError(Html.fromHtml("<font color='#424242'>No hay saldo suficiente("+saldo+")</font>"));
                        break;
                    }
                    if (saldoUsado < 0) {
                        edtCantidad.setError(Html.fromHtml("<font color='#424242'>Cantidad no valida</font>"));
                        break;
                    }


                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("busqueda", "BONIFICACION");
                    returnIntent.putExtra("codigoRegistro", codigoRegistro);
                    returnIntent.putExtra("codigoSalida", codigoSalida);
                    returnIntent.putExtra("saldoUsado", saldoUsado);
                    returnIntent.putExtra("saldoPendiente", saldo);

                    returnIntent.putExtra("codigoProducto", codprod);
                    returnIntent.putExtra("item", nro_item);
                    returnIntent.putExtra(REQUEST_ACCION_PRODUCTO_KEY, REQUEST_ACCION_PRODUCTO_VALUE);
                    setResult(RESULT_OK, returnIntent);
                    finish();


                }else{
                    if (edtBusqueda.getText().toString().matches("")|| obj_dbclasses.getProductoXNombre(edtBusqueda.getText().toString()) == 0) {
                        edtBusqueda.setError(Html.fromHtml("<font color='#424242'>Nombre incorrecto</font>"));
                        break;
                    }
                    if ((edtPrecioUnt.getText().toString()).equals("0.0")|| (edtPrecioUnt.getText().toString()).equals("0")) {
                        boolean preferencia_precioCero = preferencias_configuracion.getBoolean("preferencias_precioCero", false);

                        if (preferencia_precioCero) {
                            Toast.makeText(getApplicationContext(), "producto sin precio", Toast.LENGTH_SHORT).show();
                        }else{
                            edtPrecioUnt.setError(Html.fromHtml("<font color='#424242'>No tiene precio</font>"));
                            break;
                        }
                    }
                    if (edtCantidad.getText().toString().matches("")|| (Integer.parseInt(edtCantidad.getText().toString())) == 0) {
                        edtCantidad.setError(Html.fromHtml("<font color='#424242'>Cantidad</font>"));
                        break;
                    }

                    if (tv_precioSinIGV.getText().toString().matches("") || tv_precioSinIGV.length()==0) {
                        tv_precioSinIGV.setError(Html.fromHtml("<font color='#424242'>Precio requerido</font>"));
                        break;
                    }

                    if (tv_precioSinIGV.getText().toString().equals("0.00") || tv_precioSinIGV.getText().toString().equals("0") || tv_precioSinIGV.getText().toString().equals("0.0")) {
                        tv_precioSinIGV.setError(Html.fromHtml("<font color='#424242'>Precio requerido</font>"));
                        break;
                    }

                    try{
                        //if (estado.equals(DISCONTINUO)) {
//                            if (Double.parseDouble(tv_totalStockDisponible.getText().toString())< Integer.parseInt(edtCantidad.getText().toString())) {
//                                edtCantidad.setError(Html.fromHtml("<font color='#424242'>No hay stock suficiente</font>"));
//                                GlobalFunctions.showCustomToast(ProductoActivity.this, "Producto discontinuo o producto sin stock", GlobalFunctions.TOAST_WARNING,GlobalFunctions.POSICION_BOTTOM);
//                                break;
//                            }
                      //  }
                    }catch (Exception e) {
                        e.printStackTrace();
                        edtCantidad.setError(Html.fromHtml("<font color='#424242'>No hay stock suficiente</font>"));
                        GlobalFunctions.showCustomToast(ProductoActivity.this, "Stock insuficiente", GlobalFunctions.TOAST_WARNING,GlobalFunctions.POSICION_BOTTOM);
                        break;
                    }


                    if (edtCantidad.getText().toString().length() != 0 || edtBusqueda.getText().toString().length() != 0) {
                        // if(Double.parseDouble(edtCantidad.getText().toString())<=stock){
                        int cant = Integer.parseInt(edtCantidad.getText().toString());
                        //double precio = Double.parseDouble(edtPrecioUnt.getText().toString());
                        double precio = Double.parseDouble(tv_precioSinIGV.getText().toString().replace(",", ""));
                        boolean afectoPercepcion = swt_afecto.isChecked();
                        // precio=Double.parseDouble(edtPrecioUnt.getText().toString());
                        // prueba codUnimed
                        String desunimed 	= spnUndMedidas.getSelectedItem().toString();
                        String descripcion 	= edtBusqueda.getText().toString().trim();
                        if(Double.parseDouble(PRECIO_LISTA)<=0){
                            UtilViewSnackBar.SnackBarDanger(this, edtBusqueda, "Producto sin precio");
                            break;
                        }
                        String precioLista 	=PRECIO_LISTA;
                        double porcentaje_desc 	=edt_descuento.getText().toString().length()>0?Double.parseDouble(edt_descuento.getText().toString()):0;
                        double porcentaje_desc_extra 	= edtDescuentoExtra.getText().toString().length()>0?Double.parseDouble(edtDescuentoExtra.getText().toString()):0;
                        //String descuento 	= edt_descuento.getText().toString().trim();

                        descuento = descuentoAplicado;
                        Log.d(TAG, "descuentoAplicado:"+descuento);

                        if (descuento.equals("") || descuento.length()==0) {
                            descuento = "0";
                        }

                        /** ----------------------------------- VERIFICAR PERCEPCIONES ------------------------------------------ **/
                        double precioPercepcion = 0.0d;
                        Log.d("ProductoActivity ::datos de percepcion cliente::","clienteTienePercepcion "+clienteTienePercepcion);
                        Log.d("ProductoActivity ::datos de percepcion cliente::","clienteTienePercepcionEspecial "+clienteTienePercepcionEspecial);
                        Log.d("ProductoActivity ::datos de percepcion cliente::","clienteValorPercepcion "+clienteValorPercepcion);
                        Log.d("ProductoActivity ::datos de percepcion producto::","Precio "+precio);

                        if (afectoPercepcion == true && clienteTienePercepcion == 1) {
                            precioPercepcion = Double.parseDouble(tv_precioIncIGV.getText().toString().replace(",", "")) * clienteValorPercepcion;
                        }

                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("busqueda", "PRODUCTO");
                        returnIntent.putExtra("descripcion", descripcion);
                        returnIntent.putExtra("desunimed", desunimed);
                        returnIntent.putExtra("Cantidad", cant);
                        returnIntent.putExtra("peso", peso);
                        returnIntent.putExtra("fact_conv", fact_conv);
                        returnIntent.putExtra("precioUnidad",precio);
                        returnIntent.putExtra("precioLista",precioLista);
                        returnIntent.putExtra("descuento",	descuento);
                        returnIntent.putExtra("porcentaje_desc",	porcentaje_desc);
                        returnIntent.putExtra("porcentaje_desc_extra",	porcentaje_desc_extra);
                        returnIntent.putExtra("precioPercepcion", precioPercepcion);
                        returnIntent.putExtra("agregarComoBonificacion", swAgregarComoBonificacion.isChecked());
                        returnIntent.putExtra("codigoProducto", codprod);
                        returnIntent.putExtra("item", nro_item);
                        returnIntent.putExtra(REQUEST_ACCION_PRODUCTO_KEY, REQUEST_ACCION_PRODUCTO_VALUE);
                        Log.d("ProductoActivity ::fin return intent::","PrecioPercepcion-> "+precioPercepcion);
                        setResult(RESULT_OK, returnIntent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Complete los campos",
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
        }
    }

    class async_busquedaBonificaciones extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ProductoActivity.this);
            pDialog.setMessage("Buscando....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            listaBonificacionesPendientes = DAOBonificaciones.getListaBonificacionesPendientes(oc_numero,codcli);
            return null;
        }

        protected void onPostExecute(Void result) {
            pDialog.dismiss();// ocultamos progess dialog.
            if (listaBonificacionesPendientes.size()>0) {
                MostrarBonificacionesPendientes();
            }else{
                GlobalFunctions.showCustomToast(ProductoActivity.this, "No se encontraron bonificaciones", GlobalFunctions.TOAST_DONE, GlobalFunctions.POSICION_BOTTOM);
            }

        }
    }
    private void MostrarBonificacionesPendientes() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Bonificaciones Pendientes");

        ListView lista = new ListView(this);

        builder.setView(lista).setAdapter(
                new CH_Adapter_bonificacionesPendientes(this, listaBonificacionesPendientes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        isBonificacion = true;

                        String afecto = "";
                        edtBusqueda.setText(listaBonificacionesPendientes.get(position).getDescripcionSalida());

                        String undMedidas[] = obj_dbclasses.getUndmedidas(listaBonificacionesPendientes.get(position).getCodigoSalida().substring(1, listaBonificacionesPendientes.get(position).getCodigoSalida().length()));
                        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(getApplicationContext(),R.layout.spinner_item,undMedidas);
                        adapter.setDropDownViewResource(R.layout.spinner_item);
                        spnUndMedidas.setAdapter(adapter);


                        fact_conv 	= 1;
                        peso 		= 0.00;
                        prec_act 	= 0.00;
                        precUnd_act = 0.00;
                        codigo_act 	= listaBonificacionesPendientes.get(position).getCodigoSalida();
                        codprod 	= listaBonificacionesPendientes.get(position).getCodigoSalida();
                        stock 		= 0;
                        sec_politica= "0";
                        estado 		= "0";

                        codigoRegistro 	= listaBonificacionesPendientes.get(position).getCodigoRegistro();
                        saldo			= listaBonificacionesPendientes.get(position).getSaldo();
                        codigoSalida	= listaBonificacionesPendientes.get(position).getCodigoSalida();

                        if (estado.equals(DISCONTINUO)) {
                            GlobalFunctions.showCustomToast(ProductoActivity.this, "Producto discontinuo", GlobalFunctions.TOAST_WARNING,GlobalFunctions.POSICION_MIDDLE);
                        }

                        swt_afecto.setFocusable(false);

                        if (afecto.equals("0") || afecto.equals("")) {
                            swt_afecto.setChecked(false);
                        }else{
                            swt_afecto.setChecked(true);
                        }
                        edtCantidad.setText(""+saldo);
                        edtCantidad.requestFocus();
                        btn_consultarProducto.setEnabled(false);
                    }
                });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();

    }

    class async_busqueda extends AsyncTask<Void, String, String> {

        protected void onPreExecute() {
            pDialog = new ProgressDialog(ProductoActivity.this);
            pDialog.setMessage("Buscando....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub

            _buscarDatosProductoDB();

            return "ok";
        }

        protected void onPostExecute(String result) {

            pDialog.dismiss();// ocultamos progess dialog.

            if (productos.length != 0) {
                if(isRequestModifyProducto()){
                    onItemClickListaProducto(0);
                    setDataProductoSiIsModificar();
                }
                else crear_vistaDialogo();
            } else {
                Toast.makeText(getApplicationContext(),
                        "No se encontro Producto", Toast.LENGTH_SHORT).show();
            }

            Log.e("onPostExecute=", "" + result);

        }
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

                holder.descripcion = (TextView) item.findViewById(R.id.prd_act_txtDescrp);
                holder.prod_descripcion_comercial = (TextView) item.findViewById(R.id.prod_descripcion_comercial);
                holder.stock = (TextView) item.findViewById(R.id.prd_act_txtStock);
                holder.stock2 = (TextView) item.findViewById(R.id.producto_item_tvUnidad);
                holder.des1 = (TextView) item.findViewById(R.id.producto_item_tvUnidad2);
                holder.des2 = (TextView) item.findViewById(R.id.producto_item_tvUnidad1);
                holder.precio = (TextView) item.findViewById(R.id.prd_act_txtPrecio);
                holder.codigo = (TextView) item.findViewById(R.id.prd_act_txtCodigo);
                holder.codigoProveedor = (TextView) item.findViewById(R.id.prd_act_txtCodigoProveedor);

                item.setTag(holder);

            } else {

                holder = (ViewHolder) item.getTag();

            }
            undMedidas = obj_dbclasses.getUndmedidas(productos[position].getCodprod());
            holder.precio.setText(""+ Math.round(productos[position].getPrecio() * 100)/ 100.0);
            holder.codigo.setText(productos[position].getCodprod());
            holder.descripcion.setText(productos[position].getDescripcion());
            holder.prod_descripcion_comercial.setVisibility(productos[position].getDesc_comercial().length()>0?View.VISIBLE:View.GONE);
            holder.prod_descripcion_comercial.setText("Desc. Comercial: "+productos[position].getDesc_comercial());
            holder.codigoProveedor.setText(productos[position].getCodProveedor());

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
                //item.setBackgroundResource(R.drawable.list_selector_no_stock);
            } else {

                holder.stock.setTextColor(Color.parseColor("#343434"));
                // holder.stock.setText(Integer.toString(productos[position].getStock()));
                if (undMedidas.length == 1) {
                    holder.stock.setText(stock + "");
                    holder.stock2.setVisibility(View.INVISIBLE);
                    holder.des1.setText(undMedidas[0]);
                    holder.des2.setVisibility(View.INVISIBLE);
                } else {
                    holder.stock.setText(""+ (GlobalFunctions.redondear((stock)/ productos[position].getFact_conv())));
                    holder.stock2.setText(""+ ((stock) % productos[position].getFact_conv()));
                    holder.des1.setText(undMedidas[0]);
                    holder.des2.setText(undMedidas[1]);
                }
                item.setBackgroundResource(R.drawable.list_selector);
            }

            return item;
        }

        public class ViewHolder {
            TextView descripcion, prod_descripcion_comercial;
            TextView precio;
            TextView codigo, des1, des2;
            TextView stock, codigoProveedor, stock2;
        }

    }

    public class Adapter_consultaStock extends BaseAdapter {

        protected Activity activity;
        protected JSONArray listaArray;
        protected View view_item=null;
        public Adapter_consultaStock(Activity activity, JSONArray listaArray){
            this.activity = activity;
            this.listaArray = listaArray;
        }

        @Override
        public int getCount() {
            return listaArray.length();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View item = convertView;
            ViewHolder holder;

            //if (item == null) {
                LayoutInflater inflater = activity.getLayoutInflater();
                item = inflater.inflate(R.layout.item_consulta_stock, null);
                holder = new ViewHolder();

                holder.tv_almacen = (TextView) item.findViewById(R.id.tv_almacen);
            holder.tv_stock_actual = (TextView) item.findViewById(R.id.tv_stock_actual);
            holder.tv_stock_separado = (TextView) item.findViewById(R.id.tv_stock_separado);
            holder.tv_stock_xConfirmar = (TextView) item.findViewById(R.id.tv_stock_xConfirmar);
            holder.tv_stockDisponible = (TextView) item.findViewById(R.id.tv_stockDisponible);

                item.setTag(holder);
            /*} else {
                holder = (ViewHolder) item.getTag();
            }*/

            JSONObject jsonData = null;
            try {
                view_item=item;
                jsonData = listaArray.getJSONObject(position);
                String nombre=database.getAlmacenDescripcionResumen((String) jsonData.get("codigoAlmacen"));
                holder.tv_almacen.setText( (nombre.length()>0?nombre:jsonData.get("codigoAlmacen"))+"" );
                holder.tv_stock_actual.setText((String)jsonData.get("stock_actual"));
                holder.tv_stock_separado.setText((String)jsonData.get("stock_separado"));
                holder.tv_stock_xConfirmar.setText((String)jsonData.get("stock_x_confirmar"));
                holder.tv_stockDisponible.setText((String)jsonData.get("stock_disponible"));




            } catch (JSONException e) {
                e.printStackTrace();
            }

            return item;
        }
        public View getItemView(){
             return this.view_item;
        }

        public class ViewHolder {
            TextView tv_almacen,tv_stock_actual, tv_stock_separado, tv_stock_xConfirmar, tv_stockDisponible;
        }

    }

    //NUEVO ADAPTADOR LISTA DE PRECIOS

    public class Adapter_consultaPrecios extends BaseAdapter {

        protected Activity activity;
        protected ArrayList<HashMap<String, Object>> lista;

        public Adapter_consultaPrecios(Activity activity, ArrayList<HashMap<String, Object>> lista){
            this.activity = activity;
            this.lista = lista;
        }

        @Override
        public int getCount() {
            return lista.size();
        }

        @Override
        public Object getItem(int position) {
            return lista.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View item = convertView;
            ViewHolder holder;

            if (item == null) {
                LayoutInflater inflater = activity.getLayoutInflater();
                item = inflater.inflate(R.layout.item_consulta_lista_precios, null);
                holder = new ViewHolder();

                holder.tv_fqinicial = (TextView) item.findViewById(R.id.tv_fqinicial);
                holder.tv_fqfinal = (TextView) item.findViewById(R.id.tv_fqfinal);
                holder.tv_precioUni = (TextView) item.findViewById(R.id.tv_precioUni);

                item.setTag(holder);
            } else {
                holder = (ViewHolder) item.getTag();
            }

            HashMap<String, Object> map = lista.get(position);

            holder.tv_fqinicial.setText((String)map.get("fq_inicial"));
            holder.tv_fqfinal.setText((String)map.get("fq_final"));
            holder.tv_precioUni.setText((String)map.get("PrecioUni"));

			/*try {
				tv_fqinicial += Double.parseDouble(holder.tv_fqfinal.getText().toString());
				totalStockDisponible += Double.parseDouble(holder.tv_precioUni.getText().toString());
				//Log.d(TAG, "totalStockDisponible:"+totalStockDisponible+" + "+holder.tv_stockDisponible.getText() );
				tv_totalStockConfirmar.setText(""+totalStockConfirmar);
			    tv_totalStockDisponible.setText(""+totalStockDisponible);
			} catch (Exception e) {
				e.printStackTrace();
			}*/
            return item;
        }

        public class ViewHolder {
            TextView tv_fqinicial,tv_fqfinal, tv_precioUni;
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
                async_busqueda_scan task = new async_busqueda_scan();
                task.execute(scanFormat, scanContent);
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

    public void scan_buscarProducto(String formato, String content) {

        if (formato.equals("QR_CODE")) {

            // content = codpro
            productos = obj_dbclasses.getProductosXcliente_codpro(codcli,
                    content);
            Log.d("ProductoActivity ::scan_buscarProducto::",
                    "formato(QR_CODE)");

        } else if (formato.equals("EAN_13")) {

            // content = ean13
            Log.d("ProductoActivity ::scan_buscarProducto::", "formato(EAN_13)");
            productos = obj_dbclasses.getProductosXean13(codcli, content);

        } else {
            Toast.makeText(getApplicationContext(), "Formato incorrecto",
                    Toast.LENGTH_LONG).show();
        }

    }

    class async_busqueda_scan extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            pDialog = new ProgressDialog(ProductoActivity.this);
            pDialog.setMessage("Buscando....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            String formato = params[0];
            String content = params[1];

            scan_buscarProducto(formato, content);

            return "ok";
        }

        protected void onPostExecute(String result) {

            pDialog.dismiss();// ocultamos progess dialog.

            if (productos.length != 0) {
                // crear_vistaDialogo();

                //

                int position = 0;

                edtBusqueda.setText(productos[position].getDescripcion());
                String undMedidas[] = obj_dbclasses
                        .getUndmedidas(productos[position].getCodprod());
                ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
                        getApplicationContext(),
                        layout.simple_spinner_item, undMedidas);
                adapter.setDropDownViewResource(layout.simple_spinner_dropdown_item);
                spnUndMedidas.setAdapter(adapter);

                politica_cliente = obj_dbclasses.getPoliticaCLiente(codven,
                        productos[position].getCodprod(), codcli);

                boolean existe = false;
                if (politica_cliente[0].getSec_politica().equals("-1")) {

                } else {
                    auxiliar = pol_prec[0];
                    for (int i = 0; i < pol_prec.length; i++) {

                        String secCliente = politica_cliente[0]
                                .getSec_politica();
                        String secVende = pol_prec[i].getSec_politica();
                        Log.w("CLIENTE POLITICAS****", secCliente + " - - "
                                + secVende);
                        if (secCliente.equalsIgnoreCase(secVende)) {

                            pol_prec[0] = politica_cliente[0];
                            pol_prec[i] = auxiliar;
                            existe = true;
                        }

                    }// SI no existe se a�ade el registro
                    if (!existe) {
                        pol_prec[0] = politica_cliente[0];
                        pol_prec[pol_prec.length] = auxiliar;
                    }

                }

                fact_conv = productos[position].getFact_conv();
                peso = Math.round(productos[position].getPeso() * 100) / 100.0;
                prec_act = Math.round(productos[position].getPrecio() * 100) / 100.0;
                precUnd_act = Math
                        .round(productos[position].getPrecioUnidad() * 100) / 100.0;
                codigo_act = productos[position].getCodprod();
                stock = productos[position].getStock();
                sec_politica = productos[position].getSec_politica();
                Log.w("POLITICA_PRECIO", "politica- " + sec_politica);

                edtCantidad.setInputType(InputType.TYPE_CLASS_NUMBER);

                edtCantidad.requestFocus();

                showSoftKeyboard(edtCantidad);

            } else {
                Toast.makeText(getApplicationContext(),
                        "No se encontro Producto", Toast.LENGTH_SHORT).show();
            }

            Log.e("onPostExecute=", "" + result);

        }
    }

    public void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

            imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);

            Log.i("INPUT METHOD", "" + imm.isActive(view));
        }
    }

}

