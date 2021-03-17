package com.example.fuerzaventaschema.genesys.fuerza_ventas;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.example.fuerzaventaschema.R;
import com.example.fuerzaventaschema.genesys.BEAN.ItemProducto;
import com.example.fuerzaventaschema.genesys.DAO.DAO_Producto;
import com.example.fuerzaventaschema.genesys.DAO.DAO_RegistroBonificaciones;
import com.example.fuerzaventaschema.genesys.adapters.CH_Adapter_bonificacionesPendientes;
import com.example.fuerzaventaschema.genesys.adapters.ModelBonificacionPendiente;
import com.example.fuerzaventaschema.genesys.datatypes.DBPolitica_Precio2;
import com.example.fuerzaventaschema.genesys.datatypes.DBSync_soap_manager;
import com.example.fuerzaventaschema.genesys.datatypes.DBclasses;
import com.example.fuerzaventaschema.genesys.util.GlobalFunctions;
import com.example.fuerzaventaschema.genesys.util.VARIABLES;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import android.R.layout;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.Html.ImageGetter;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;
import android.widget.Adapter;
import android.widget.AdapterView;
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
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


@SuppressLint("LongLogTag")
public class ProductoActivity extends AppCompatActivity implements OnClickListener {


    private static final String TAG = "ProductoActivity";

    private static final String DISCONTINUO = "1";

    public final static String BUSQUEDA_DESCRIPCION = "descripcion";
    public final static String BUSQUEDA_PROVEEDOR = "proveedor";
    public final static String BUSQUEDA_CODIGO = "codigo";

    private int clienteTienePercepcion;
    private int clienteTienePercepcionEspecial;
    private double clienteValorPercepcion;

    boolean PROMOCION = true;
    DBclasses obj_dbclasses;
    TextView tv_nombre_producto;
    ListView lstProductos;
    EditText txtProducto, edtBusqueda, edtPrecioUnt, edtCantidad, edt_descuento;
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

    TextView tv_precioSinIGV,tv_precioIncIGV, tv_fechaUltimaVenta, tv_precioUltimaVenta,tv_descuentoPVP;
    TextView tv_totalStockConfirmar, tv_totalStockDisponible;
    ListView lv_consultaStock,lv_consultaPrecios;
    //Switch swt_afecto;
    ToggleButton swt_afecto;
    CheckBox check_precio;

    private double descuentoMin = 0.0;
    private double descuentoMax = 0.0;
    private double valorIGV;

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
    String PRECIO_BASE = "";
    String PRECIO_LISTA = "0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);
        database = new DBclasses(getApplicationContext());
        DAOBonificaciones = new DAO_RegistroBonificaciones(getApplicationContext());
        DAOProducto		  = new DAO_Producto(getApplicationContext());

        preferencias_configuracion = getSharedPreferences("preferencias_configuracion", Context.MODE_PRIVATE);
        obj_dbclasses = new DBclasses(getApplicationContext());
        valorIGV = preferencias_configuracion.getFloat("valorIGV", 0.0f);
        descuentoMax = preferencias_configuracion.getFloat("limiteDescuento", 2.0f);

        Bundle bundle = getIntent().getExtras();
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
        btn_consultarProducto = (Button) findViewById(R.id.btn_consultarProducto);
        btnBonificaciones = (ImageButton) findViewById(R.id.productolyt_btnBonificacion);

        btnCancelar = (Button) findViewById(R.id.productolyt_btnCancelar);
        //swt_afecto = (Switch)findViewById(id.swt_afecto);
        swt_afecto = (ToggleButton)findViewById(R.id.swt_afecto);
        tv_precioSinIGV = (TextView) findViewById(R.id.tv_precioSinIGV);
        tv_precioIncIGV = (TextView) findViewById(R.id.tv_precioIncIGV);
        btnAgregar = (Button) findViewById(R.id.productolyt_btnAgregar);
        lv_consultaStock = (ListView) findViewById(R.id.lv_consultaStock);
        lv_consultaPrecios = (ListView) findViewById(R.id.lv_consultaPrecios);
        tv_descuentoPVP=(TextView)findViewById(R.id.tv_descuentoPVP);

        tv_fechaUltimaVenta = (TextView) findViewById(R.id.tv_fechaUltimaVenta);
        tv_precioUltimaVenta = (TextView) findViewById(R.id.tv_precioUltimaVenta);
        tv_totalStockConfirmar = (TextView) findViewById(R.id.tv_totalStockConfirmar);
        tv_totalStockDisponible = (TextView) findViewById(R.id.tv_totalStockDisponible);
        tv_monedaPrecio = (TextView)findViewById(R.id.tv_monedaPrecio);

        check_precio = (CheckBox) findViewById(R.id.check_precio);
        check_precio.setChecked(true);
        check_precio.setClickable(false);

        if (codigoMoneda.equals(PedidosActivity.MONEDA_PEN)) {
            tv_monedaPrecio.setText("S/.");
        }else{
            tv_monedaPrecio.setText("$.");
        }
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
                            double precioIncIGV = Double.parseDouble(precioProducto) * (1 + valorIGV);
                            tv_precioIncIGV.setText(GlobalFunctions.redondear(String.valueOf(precioIncIGV)));
                            tv_precioIncIGV.setTextColor(getResources().getColor(R.color.green_800));

                            edt_descuento.setText("");
                            edt_descuento.setEnabled(false);
                            edtPrecioUnt.setEnabled(false);
                        }else{
                            edtPrecioUnt.setText(PRECIO_BASE);
                            tv_precioSinIGV.setText(GlobalFunctions.redondear(String.valueOf(PRECIO_BASE)));
                            tv_precioSinIGV.setTextColor(getResources().getColor(R.color.green_500));
                            double precioIncIGV = Double.parseDouble(PRECIO_BASE) * (1 + valorIGV);
                            tv_precioIncIGV.setText(GlobalFunctions.redondear(String.valueOf(precioIncIGV)));
                            tv_precioIncIGV.setTextColor(getResources().getColor(R.color.green_500));

                            edt_descuento.setText("");
                            edt_descuento.setEnabled(true);
                            edtPrecioUnt.setEnabled(false);
                        }
                    }else{
                        edtPrecioUnt.setText(PRECIO_BASE);
                        //quitar el precio sin IGV con IGV
                        edt_descuento.setText("");
                        tv_precioSinIGV.setText("");
                        tv_precioIncIGV.setText("");
                        edtPrecioUnt.setEnabled(true);
                    }
                }
            }
        });



        edt_descuento.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //edtPrecioUnt.setText("");

                //tv_precioSinIGV.setText("");
                //tv_precioIncIGV.setText("");
                //tv_descuentoPVP.setText("");
                //totalStockConfirmar = 0.0;
                //totalStockDisponible = 0.0;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                edt_descuento.setError(null);
                if (edt_descuento.getText().toString().length()>0) {
                    double porcentaje=Integer.parseInt(edt_descuento.getText().toString());

                    if (porcentaje>=0 && porcentaje<=100){
                        ConsultarProductoDemo();
                    }else{
                        edt_descuento.setText("0");
                        edt_descuento.setError("Ingrese un número de 1 a 100");
                    }
                }else{
                    ConsultarProductoDemo();
                }
            }
        });




        edtBusqueda.setOnClickListener(new View.OnClickListener() {
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
                    BuscarProducto();
                    return true;
                } else {
                    return false;
                }
            }
        });


        btnBuscar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                BuscarProducto();
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
                ConsultarProductoDemo();//solo demo
            }


        });



        btnCancelar.setOnClickListener(new View.OnClickListener() {
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
                        edtBusqueda.setInputType(InputType.TYPE_CLASS_NUMBER);
                        btn_consultarProducto.setEnabled(true);
                        flag = 1;

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
                        flag = 2;

                        editor_preferencias = preferencias_configuracion.edit();
                        editor_preferencias.putString("preferencias_busquedaProducto",BUSQUEDA_PROVEEDOR);
                        editor_preferencias.commit();
                        break;
                    default:
                        break;
                }
            }
        });

        String preferencia_busquedaProducto = preferencias_configuracion.getString("preferencias_busquedaProducto", BUSQUEDA_DESCRIPCION);
        switch (preferencia_busquedaProducto) {
            case BUSQUEDA_DESCRIPCION:
                ((RadioButton)rb_filtro.getChildAt(0)).setChecked(true);
                break;
            case BUSQUEDA_PROVEEDOR:
                ((RadioButton)rb_filtro.getChildAt(1)).setChecked(true);
                break;
            case BUSQUEDA_CODIGO:
                ((RadioButton)rb_filtro.getChildAt(2)).setChecked(true);
                break;
            default:
                break;
        }


    }

    public void ConsultarProducto(){
        final String precioGeneral = edtPrecioUnt.getText().toString();
        final String cantidad = edtCantidad.getText().toString();
        descuento = edt_descuento.getText().toString();
        if(descuento.equals("")) descuento="0.0";

        if (valorIGV==0.0) {
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


    private void ConsultarProductoDemo() {

        DecimalFormat formaterPrecioourDecimal = new DecimalFormat("#,###.0000");

        edtPrecioUnt.setText("0.0");
        tv_precioIncIGV.setText("0.0");
        edt_descuento.setEnabled(true);

        double valor_cambio=1;
        if (codigoMoneda.equals(PedidosActivity.MONEDA_PEN)) {
            String tipo_de_cambio  = obj_dbclasses.getCambio("Tipo_cambio");
            valor_cambio  = Double.parseDouble(tipo_de_cambio);
        }


        DBPolitica_Precio2 politica_precio2=obj_dbclasses.GetPoliticaPrecio2ByCliente(codcli, codprod, valor_cambio);
        if(politica_precio2!=null){
            edt_descuento.setEnabled(true);
            double porcentajeDescuentoManual=edt_descuento.getText().toString().trim().length()>0?Double.parseDouble(edt_descuento.getText().toString()):0;

            double precioOriginal=(politica_precio2.getPrecio_base());
            double precioListaConIgv=(politica_precio2.getPrepo_unidad()*1.18);
            double precioListaSinIgv=(politica_precio2.getPrepo_unidad());
            double descuentoConIgv=(politica_precio2.getPrepo_unidad())*(porcentajeDescuentoManual/100);
            double descuentoSinIgv=(politica_precio2.getPrepo_unidad())*(porcentajeDescuentoManual/100);

            descuentoAplicado= VARIABLES.ParseDecimalFour(descuentoSinIgv);
            PRECIO_LISTA=VARIABLES.ParseDecimalFour(precioListaSinIgv);

            double porcentajeDescuentoSitema= Double.parseDouble(VARIABLES.ParseDecimalTwo(((precioOriginal-precioListaSinIgv)*100/precioOriginal)));

            String sms="Precio Lista a "+formaterPrecioourDecimal.format(precioListaSinIgv);
            if (porcentajeDescuentoSitema>0.0){
                sms="Precio Lista con \n"+(porcentajeDescuentoSitema+porcentajeDescuentoManual)+"% dsc a "+formaterPrecioourDecimal.format(precioListaSinIgv);
            }



            btn_consultarProducto.setText(sms);
            edtPrecioUnt.setText(""+formaterPrecioourDecimal.format(precioOriginal));//precio original
            tv_precioSinIGV.setText(""+formaterPrecioourDecimal.format(precioListaSinIgv-descuentoSinIgv)); //precio sin IGV con descuento
            tv_precioIncIGV.setText(""+formaterPrecioourDecimal.format(precioListaConIgv-descuentoConIgv));//precio con IGV con descuento
            tv_descuentoPVP.setText((porcentajeDescuentoManual>0.0?"("+porcentajeDescuentoManual+"%)":"")+" "+formaterPrecioourDecimal.format(descuentoConIgv));//Descuento aplicacido en soles
        }else{

            Toast.makeText(ProductoActivity.this, "No se ha obtenido los precios para el producto", Toast.LENGTH_SHORT).show();
        }
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
                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) lv_consultaStock.getLayoutParams();
                            params.height = (lv_consultaStock.getCount() * tamanio_por_item);
                            params.width = LinearLayout.LayoutParams.MATCH_PARENT;
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



    public void BuscarProducto(){
        try {
            if (flag == 0) {
                if (edtBusqueda.getText().toString().length() < 3) {
                    edtBusqueda.setError(Html.fromHtml("<font color='#424242'>Ingrese al menos 3 caracteres</font>"));
                }
                else {
                    new async_busqueda().execute();
                }
            }
            else {
                if (Integer.parseInt(edtBusqueda.getText().toString()) / 1 > 0) {
                    new async_busqueda().execute();
                }
            }
        }
        catch (Exception e) {
            edtBusqueda.setError(Html.fromHtml("<font color='#424242'>Ingrese solo numeros</font>"));
        }
    }

    public void BuscarDatosProducto() {
        if (flag == 0) {
            StringBuilder builder = new StringBuilder();
            StringTokenizer tokens = new StringTokenizer(edtBusqueda.getText().toString());

            while (tokens.hasMoreTokens()) {
                builder.append(tokens.nextToken());
                builder.append("%");
            }
            productos = obj_dbclasses.getProductosXcliente(codcli,builder.toString());
        } else if (flag == 2) {
            productos = obj_dbclasses.getProductosXProveedor(codcli,edtBusqueda.getText().toString());
        } else {
            productos = obj_dbclasses.getProductosXcliente_codpro(codcli,edtBusqueda.getText().toString());

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
                        String afecto = productos[position].getAfecto();
                        edtBusqueda.setText(productos[position].getDescripcion());

                        String undMedidas[] = obj_dbclasses.getUndmedidas(productos[position].getCodprod());
                        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(getApplicationContext(),R.layout.spinner_item,undMedidas);
                        adapter.setDropDownViewResource(R.layout.spinner_item);
                        spnUndMedidas.setAdapter(adapter);


                        fact_conv 	= productos[position].getFact_conv();
                        peso 		= Math.round(productos[position].getPeso() * 100) / 100.0;
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

                        if (afecto.equals("0") || afecto.equals("")) {
                            swt_afecto.setChecked(false);
                        }else{
                            swt_afecto.setChecked(true);
                        }
                        edtCantidad.requestFocus();
                        btn_consultarProducto.performClick();

                        ConsultarProducto();//Consulta el stcok en linea

                    }
                });
        builder.create().show();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.productolyt_btnAgregar:

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

                    if (origen.equals("PEDIDO_MODIFICAR")) {
                        returnIntent.putExtra("TIPO", "MODIFICAR");
                        returnIntent.putExtra("Codigo", codprod);
                    } else {
                        returnIntent.putExtra("TIPO", "AGREGAR");
                        returnIntent.putExtra("codigoProducto", codprod);
                    }
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
                            if (Double.parseDouble(tv_totalStockDisponible.getText().toString())< Integer.parseInt(edtCantidad.getText().toString())) {
                                edtCantidad.setError(Html.fromHtml("<font color='#424242'>No hay stock suficiente</font>"));
                                GlobalFunctions.showCustomToast(ProductoActivity.this, "Producto discontinuo o producto sin stock", GlobalFunctions.TOAST_WARNING,GlobalFunctions.POSICION_BOTTOM);
                                break;
                            }
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
                        double precio = Double.parseDouble(tv_precioSinIGV.getText().toString());
                        boolean afectoPercepcion = swt_afecto.isChecked();
                        // precio=Double.parseDouble(edtPrecioUnt.getText().toString());
                        // prueba codUnimed
                        String desunimed 	= spnUndMedidas.getSelectedItem().toString();
                        String descripcion 	= edtBusqueda.getText().toString().trim();
                        String precioLista 	=PRECIO_LISTA;
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
                            precioPercepcion = Double.parseDouble(tv_precioIncIGV.getText().toString()) * clienteValorPercepcion;
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
                        returnIntent.putExtra("precioPercepcion", precioPercepcion);

                        if (origen.equals("PEDIDO_MODIFICAR")) {
                            returnIntent.putExtra("TIPO", "MODIFICAR");
                            returnIntent.putExtra("Codigo", codprod);
                        } else {
                            returnIntent.putExtra("TIPO", "AGREGAR");
                            returnIntent.putExtra("codigoProducto", codprod);

                            Log.d("ProductoActivity ::fin return intent::","PrecioPercepcion-> "+precioPercepcion);
                        }
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

            BuscarDatosProducto();

            return "ok";
        }

        protected void onPostExecute(String result) {

            pDialog.dismiss();// ocultamos progess dialog.

            if (productos.length != 0) {
                crear_vistaDialogo();
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
            TextView descripcion;
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

                //holder.tv_almacen.setText(database.getAlmacenDescripcion((String)map.get("codigoAlmacen")));
                holder.tv_almacen.setText(""+jsonData.get("codigoAlmacen"));
                //holder.tv_almacen.setText((String)map.get("codigoAlmacen"));
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
            TextView tv_almacen,tv_stock_xConfirmar, tv_stockDisponible;
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
                        android.R.layout.simple_spinner_item, undMedidas);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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

