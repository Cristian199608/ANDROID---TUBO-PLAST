package com.example.sm_tubo_plast.genesys.fuerza_ventas;

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
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.BEAN.Expectativa;
import com.example.sm_tubo_plast.genesys.BEAN.ItemProducto;
import com.example.sm_tubo_plast.genesys.BEAN.Motivo;
import com.example.sm_tubo_plast.genesys.BEAN.RegistroGeneralMovil;
import com.example.sm_tubo_plast.genesys.DAO.DAO_RegistrosGeneralesMovil;
import com.example.sm_tubo_plast.genesys.adapters.CH_Adapter_itemDevolucionDocumento;
import com.example.sm_tubo_plast.genesys.datatypes.DBSync_soap_manager;
import com.example.sm_tubo_plast.genesys.datatypes.DBclasses;
import com.example.sm_tubo_plast.genesys.util.GlobalFunctions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class CH_BuscarProductoDevolucion extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "CH_BuscarProductoDevolucion";
    final int BUSQUEDA_DESCRIPCIONX 	= 0;
    final int BUSQUEDA_PROVEEDORX 	= 1;
    final int BUSQUEDA_CODIGOX 		= 2;

    public final static String BUSQUEDA_DESCRIPCION = "descripcion";
    public final static String BUSQUEDA_PROVEEDOR = "proveedor";
    public final static String BUSQUEDA_CODIGO = "codigo";

    int TIPO_BUSQUEDA = 0;

    ListView lstProductos;
    ItemProducto[] productos;
    ProgressDialog pDialog;

    Button btn_agregar, btn_cancelar;


    Dialog dialog_promociones;
    private RadioGroup rgroup_busqueda;

    DBclasses database;
    DAO_RegistrosGeneralesMovil DAORegistroGenerales;
    String codigoCliente="",codigoVendedor="",codigoProducto="",origen="";

    //Busqueda Producto Devolucion
    RadioButton rbtn_descripcion,rbtn_proveedor,rbtn_codigoProducto;
    EditText edt_busqueda,edt_cantidad,edt_lote;
    Spinner spn_unidad;
    ImageButton btn_buscar,btn_consultar;

    TextView tv_tipoDocumento,tv_serie,tv_numero,tv_fechaEmision,tv_fechaVencimientoLote;
    Spinner spn_motivoDevolucion,spn_expectativa,spn_envase,spn_contenido,spn_proceso;
    EditText edt_observaciones;

    SharedPreferences preferencias_configuracion;
    SharedPreferences.Editor editor_preferencias;

    ArrayList<Expectativa> listaExpectativa;
    ArrayList<Motivo> listaMotivo;
    ArrayList<RegistroGeneralMovil> listaEnvase;
    ArrayList<RegistroGeneralMovil> listaContenido;

    int cantidadMaxima = 0;

    double peso;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch_activity_buscar_producto_devolucion);

        database = new DBclasses(getApplicationContext());
        DAORegistroGenerales = new DAO_RegistrosGeneralesMovil(getApplicationContext());
        preferencias_configuracion = getSharedPreferences("preferencias_configuracion", Context.MODE_PRIVATE);

        rgroup_busqueda		= (RadioGroup) findViewById(R.id.rgroup_busqueda);
        rbtn_descripcion	= (RadioButton) findViewById(R.id.rbtn_descripcion);
        rbtn_proveedor		= (RadioButton) findViewById(R.id.rbtn_proveedor);
        rbtn_codigoProducto	= (RadioButton) findViewById(R.id.rbtn_codigoProducto);

        edt_busqueda 		= (EditText) findViewById(R.id.edt_busqueda);
        btn_buscar			= (ImageButton) findViewById(R.id.btn_buscar);

        edt_cantidad 		= (EditText) findViewById(R.id.edt_cantidad);
        edt_lote 			= (EditText) findViewById(R.id.edt_lote);
        spn_unidad			= (Spinner) findViewById(R.id.spn_unidad);
        btn_consultar		= (ImageButton) findViewById(R.id.btn_consultar);

        tv_tipoDocumento	= (TextView) findViewById(R.id.tv_tipoDocumentoD);
        tv_serie		= (TextView) findViewById(R.id.tv_serieD);
        tv_numero		= (TextView) findViewById(R.id.tv_numeroD);
        tv_fechaEmision		= (TextView) findViewById(R.id.tv_fechaEmisionD);
        tv_fechaVencimientoLote = (TextView) findViewById(R.id.tv_fechaVencimientoLote);

        spn_motivoDevolucion= (Spinner) findViewById(R.id.spn_motivoDevolucion);
        spn_expectativa		= (Spinner) findViewById(R.id.spn_expectativa);
        spn_envase			= (Spinner) findViewById(R.id.spn_envase);
        spn_contenido		= (Spinner) findViewById(R.id.spn_contenido);
        spn_proceso			= (Spinner) findViewById(R.id.spn_proceso);
        edt_observaciones	= (EditText) findViewById(R.id.edt_observaciones);

        btn_agregar 		= (Button) findViewById(R.id.btn_agregarDevolucion);
        btn_cancelar 		= (Button) findViewById(R.id.btn_cancelarDevolucion);

        btn_agregar.setOnClickListener(this);
        btn_cancelar.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        codigoCliente = "" + bundle.getString("codigoCliente");
        codigoVendedor = "" + bundle.getString("codigoVendedor");
        origen = "" + bundle.getString("origen");

        Log.d("origen",bundle.getString("origen"));
        Log.d("codigoCliente",bundle.getString("codigoCliente"));
        Log.d("codigoVendedor",bundle.getString("codigoVendedor"));

        //--- Carga de los spinner -------------------------------------------------------------------------------------------------------
        listaMotivo = database.getMotivos();
        listaExpectativa = database.getExpectativas();
        listaEnvase = DAORegistroGenerales.getEnvase();
        listaContenido = DAORegistroGenerales.getContenido();


        ArrayList<CharSequence> motivos = new ArrayList<>();
        for (int i = 0; i < listaMotivo.size(); i++) {
            motivos.add(listaMotivo.get(i).getMotivo());
        }
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(getApplicationContext(),R.layout.spinner_item,motivos);
        spn_motivoDevolucion.setAdapter(adapter);

        ArrayList<CharSequence> expectativas = new ArrayList<>();
        for (int i = 0; i < listaExpectativa.size(); i++) {
            expectativas.add(listaExpectativa.get(i).getExpectativa());
        }
        adapter = new ArrayAdapter<CharSequence>(getApplicationContext(),R.layout.spinner_item,expectativas);
        spn_expectativa.setAdapter(adapter);

        ArrayList<CharSequence> envases = new ArrayList<>();
        for (int i = 0; i < listaEnvase.size(); i++) {
            envases.add(listaEnvase.get(i).getValor());
        }
        adapter = new ArrayAdapter<CharSequence>(getApplicationContext(),R.layout.spinner_item,envases);
        spn_envase.setAdapter(adapter);

        ArrayList<CharSequence> contenidos = new ArrayList<>();
        for (int i = 0; i < listaContenido.size(); i++) {
            contenidos.add(listaContenido.get(i).getValor());
        }
        adapter = new ArrayAdapter<CharSequence>(getApplicationContext(),R.layout.spinner_item,contenidos);
        spn_contenido.setAdapter(adapter);

        //--- ***************** -------------------------------------------------------------------------------------------------------



        edt_busqueda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_busqueda.setText("");

                tv_tipoDocumento.setText("");
                tv_serie.setText("");
                tv_numero.setText("");
                tv_fechaEmision.setText("");
                tv_fechaVencimientoLote.setText("");

                codigoProducto = "";
            }
        });

        edt_busqueda.setOnEditorActionListener(new TextView.OnEditorActionListener() {

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


        edt_cantidad.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    ConsultarProducto();
                    return true;
                } else {
                    return false;
                }
            }
        });

        btn_buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuscarProducto();
            }
        });

        btn_consultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConsultarProducto();
            }
        });


        rgroup_busqueda.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                switch (arg1) {
                    case R.id.rbtn_descripcion:
                        edt_busqueda.setText("");

                        tv_tipoDocumento.setText("");
                        tv_serie.setText("");
                        tv_numero.setText("");
                        tv_fechaEmision.setText("");
                        tv_fechaVencimientoLote.setText("");
                        codigoProducto = "";

                        edt_busqueda.setHint("Descripcion");
                        edt_busqueda.requestFocus();
                        edt_busqueda.setInputType(InputType.TYPE_CLASS_TEXT);
                        TIPO_BUSQUEDA = 0;

                        editor_preferencias = preferencias_configuracion.edit();
                        editor_preferencias.putString("preferencias_busquedaProducto",BUSQUEDA_DESCRIPCION);
                        editor_preferencias.commit();
                        break;
                    case R.id.rbtn_codigoProducto:
                        edt_busqueda.setText("");

                        tv_tipoDocumento.setText("");
                        tv_serie.setText("");
                        tv_numero.setText("");
                        tv_fechaEmision.setText("");
                        tv_fechaVencimientoLote.setText("");
                        codigoProducto = "";

                        edt_busqueda.setHint("Codigo");
                        edt_busqueda.requestFocus();
                        edt_busqueda.setInputType(InputType.TYPE_CLASS_NUMBER);
                        TIPO_BUSQUEDA = 2;

                        editor_preferencias = preferencias_configuracion.edit();
                        editor_preferencias.putString("preferencias_busquedaProducto",BUSQUEDA_CODIGO);
                        editor_preferencias.commit();
                        break;
                    case R.id.rbtn_proveedor:
                        edt_busqueda.setText("");

                        tv_tipoDocumento.setText("");
                        tv_serie.setText("");
                        tv_numero.setText("");
                        tv_fechaEmision.setText("");
                        tv_fechaVencimientoLote.setText("");
                        codigoProducto = "";

                        edt_busqueda.setHint("Codigo Proveedor");
                        edt_busqueda.requestFocus();
                        edt_busqueda.setInputType(InputType.TYPE_CLASS_NUMBER);
                        TIPO_BUSQUEDA = 1;

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
                ((RadioButton)rgroup_busqueda.getChildAt(0)).setChecked(true);
                break;
            case BUSQUEDA_PROVEEDOR:
                ((RadioButton)rgroup_busqueda.getChildAt(1)).setChecked(true);
                break;
            case BUSQUEDA_CODIGO:
                ((RadioButton)rgroup_busqueda.getChildAt(2)).setChecked(true);
                break;
            default:
                break;
        }

    }

    public void ConsultarProducto(){
        final String lote = edt_lote.getText().toString();

        if (!codigoProducto.equals("") && !edt_busqueda.getText().equals("")) {
            new AsyncTask<Void, Void, Void>() {
                String respuesta;

                @Override
                protected void onPreExecute() {
                    pDialog = new ProgressDialog(CH_BuscarProductoDevolucion.this);
                    pDialog.setMessage("Consultando....");
                    pDialog.setIndeterminate(false);
                    pDialog.setCancelable(false);
                    pDialog.show();
                }

                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        Log.d(TAG+"::", "codigoVendedor: " + codigoVendedor);
                        Log.d(TAG+"::", "codigoCliente: " + codigoCliente);
                        Log.d(TAG+"::", "lote: " + lote);

                        DBSync_soap_manager soap_manager = new DBSync_soap_manager(getApplicationContext());
                        respuesta = soap_manager.sincro_obtenerDocumentosDevolucion_json(codigoProducto, lote, codigoCliente);

                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Error al sincronizar", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void result) {
                    pDialog.dismiss();
                    Log.d(TAG, "RESPUESTA:"+respuesta);

                    Type listType = new TypeToken<ArrayList<Map<String, Object>>>() {}.getType();

                    if (!respuesta.equals("")) {
                        if (!respuesta.equals("[]")) {
                            try {
                                Gson gson = new Gson();
                                ArrayList<HashMap<String, Object>> listMap = gson.fromJson(respuesta,listType);
                                if (!listMap.isEmpty()) {
                                    crear_vistaDialogoDocumentos(listMap);
                                }
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), "No se pudo obtener resultados", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), "No se obtuvo resultados", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "No se pudo obtener resultados", Toast.LENGTH_SHORT).show();
                    }
                }

            }.execute();
        }else{
            edt_busqueda.setError(Html.fromHtml("<font color='#424242'>Producto no valido</font>"));
            edt_busqueda.requestFocus();
        }
    }

    public void BuscarProducto(){
        try {
            if (TextUtils.isDigitsOnly(edt_busqueda.getText().toString())) {
                new async_busqueda().execute();
            }else{
                if (edt_busqueda.getText().toString().length() < 3) {
                    edt_busqueda.setError(Html.fromHtml("<font color='#424242'>Ingrese al menos 3 caracteres</font>"));
                }else{
                    new async_busqueda().execute();
                }
            }
        }
        catch (Exception e) {
            edt_busqueda.setError(Html.fromHtml("<font color='#424242'>Valor no permitido</font>"));
        }
    }

    public void BuscarDatosProducto() {
        Log.d(TAG, "TIPO BUSQUEDA:"+TIPO_BUSQUEDA);
        if (TIPO_BUSQUEDA == BUSQUEDA_DESCRIPCIONX) {
            StringBuilder builder = new StringBuilder();
            StringTokenizer tokens = new StringTokenizer(edt_busqueda.getText().toString());
            while (tokens.hasMoreTokens()) {
                builder.append(tokens.nextToken());
                builder.append("%");
            }
            productos = database.getProductosXcliente(codigoCliente,builder.toString());
        } else if (TIPO_BUSQUEDA == BUSQUEDA_PROVEEDORX) {
            productos = database.getProductosXProveedor(codigoCliente,edt_busqueda.getText().toString());
        } else {
            productos = database.getProductosXcliente_codpro(codigoCliente,edt_busqueda.getText().toString());
        }
    }

    public void crear_vistaDialogo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Productos");

        ListView lista = new ListView(this);

        builder.setView(lista).setAdapter(
                new ProductoAdapter(CH_BuscarProductoDevolucion.this, productos),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        String afecto = productos[position].getAfecto();
                        edt_busqueda.setText(productos[position].getDescripcion());

                        String undMedidas[] = database.getUndmedidas(productos[position].getCodprod());
                        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(getApplicationContext(),R.layout.spinner_item,undMedidas);
                        adapter.setDropDownViewResource(R.layout.spinner_item);
                        spn_unidad.setAdapter(adapter);

                        codigoProducto 	= productos[position].getCodprod();
                        //factorConversion= productos[position].getFact_conv();
                        peso 			= Math.round(productos[position].getPeso() * 100) / 100.0;
                        //precioProducto 	= Math.round(productos[position].getPrecio() * 100) / 100.0;
                        //precUnd_act 	= Math.round(productos[position].getPrecioUnidad() * 100) / 100.0;
                        //sec_politica	= productos[position].getSec_politica();

                        edt_cantidad.requestFocus();
                    }
                });
        builder.create().show();
    }

    public void crear_vistaDialogoDocumentos(final ArrayList<HashMap<String, Object>> listaDocumentos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Lista de Documentos");

        ListView lv_doc = new ListView(this);

        builder.setView(lv_doc).setAdapter(
                new CH_Adapter_itemDevolucionDocumento(CH_BuscarProductoDevolucion.this, listaDocumentos),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        try {
                            HashMap<String, Object> documento = listaDocumentos.get(position);
                            tv_tipoDocumento.setText(""+documento.get("tipoDocumento"));
                            tv_serie.setText(""+documento.get("serie"));
                            tv_numero.setText(""+documento.get("numeroDocumento"));
                            tv_fechaEmision.setText(""+documento.get("fechaDocumento"));
                            edt_lote.setText(""+documento.get("codigoLote"));
                            cantidadMaxima	= Integer.parseInt((String) documento.get("fq_pedida"));
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Ocurrio un problema con los datos consultados", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        builder.create().show();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_agregarDevolucion:
                if (edt_busqueda.getText().toString().matches("")|| database.getProductoXNombre(edt_busqueda.getText().toString()) == 0) {
                    edt_busqueda.setError(Html.fromHtml("<font color='#424242'>Nombre no es correcto</font>"));
                    break;
                }
                if (edt_cantidad.getText().toString().matches("")|| (Integer.parseInt(edt_cantidad.getText().toString())) == 0) {
                    edt_cantidad.setError(Html.fromHtml("<font color='#424242'>Ingrese cantidad</font>"));
                    break;
                }
                if (tv_tipoDocumento.getText().toString().matches("") || tv_tipoDocumento.getText().length()==0) {
                    tv_tipoDocumento.setError(Html.fromHtml("<font color='#424242'>Campo requerido</font>"));
                    break;
                }
                if (tv_serie.getText().toString().matches("") || tv_serie.getText().length()==0) {
                    tv_serie.setError(Html.fromHtml("<font color='#424242'>Campo requerido</font>"));
                    break;
                }
                if (tv_numero.getText().toString().matches("") || tv_numero.getText().length()==0) {
                    tv_numero.setError(Html.fromHtml("<font color='#424242'>Campo requerido</font>"));
                    break;
                }

                if (edt_cantidad.getText().toString().length() != 0 || edt_busqueda.getText().toString().length() != 0) {
                    int cant = Integer.parseInt(edt_cantidad.getText().toString());

                    if (cant>cantidadMaxima) {
                        edt_cantidad.setError(Html.fromHtml("<font color='#424242'>Cantidad maxima: "+cantidadMaxima+"</font>"));
                        break;
                    }

                    String desunimed 		= spn_unidad.getSelectedItem().toString();
                    String descripcion 		= edt_busqueda.getText().toString().trim();
                    String lote 			= edt_lote.getText().toString();
                    String tipoDocumento	= tv_tipoDocumento.getText().toString();
                    String serie			= tv_serie.getText().toString();
                    String numero			= tv_numero.getText().toString();
                    String fechaEmision		= tv_fechaEmision.getText().toString();

                    String codigoMotivo 	= "";
                    String codigoExpectativa= "";


                    if (listaMotivo.size()>0) {
                        codigoMotivo 	= listaMotivo.get(spn_motivoDevolucion.getSelectedItemPosition()).getCodigo();
                    }

                    if (listaExpectativa.size()>0) {
                        codigoExpectativa= listaExpectativa.get(spn_expectativa.getSelectedItemPosition()).getCodigo();
                    }

                    String codigoEnvase 	= listaEnvase.get(spn_envase.getSelectedItemPosition()).getCodValor();
                    String codigoContenido 	= listaContenido.get(spn_contenido.getSelectedItemPosition()).getCodValor();
                    String observaciones 	= edt_observaciones.getText().toString();

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("tipo", "AGREGAR-DEVOLUCION");
                    returnIntent.putExtra("codigoProducto", codigoProducto);
                    returnIntent.putExtra("descripcion", descripcion);
                    returnIntent.putExtra("desunimed", desunimed);
                    returnIntent.putExtra("cantidad", cant);
                    returnIntent.putExtra("peso", peso);
                    returnIntent.putExtra("lote", lote);
                    returnIntent.putExtra("tipoDocumento", tipoDocumento);
                    returnIntent.putExtra("serie", serie);
                    returnIntent.putExtra("numero", numero);
                    returnIntent.putExtra("fechaEmision", fechaEmision);
                    returnIntent.putExtra("codigoMotivo", codigoMotivo);
                    returnIntent.putExtra("codigoExpectativa", codigoExpectativa);
                    returnIntent.putExtra("envase", codigoEnvase);
                    returnIntent.putExtra("contenido", codigoContenido);
                    returnIntent.putExtra("observaciones", observaciones);

                    setResult(RESULT_OK, returnIntent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Complete los campos",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_cancelarDevolucion:
                CH_BuscarProductoDevolucion.this.finish();
                break;
        }
    }

    class async_busqueda extends AsyncTask<Void, String, String> {

        protected void onPreExecute() {
            pDialog = new ProgressDialog(CH_BuscarProductoDevolucion.this);
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
            undMedidas = database.getUndmedidas(productos[position].getCodprod());
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

    public void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

            imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);

            Log.i("INPUT METHOD", "" + imm.isActive(view));
        }
    }

}
