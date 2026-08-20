package com.example.sm_tubo_plast.genesys.fuerza_ventas;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.BEAN.Producto;
import com.example.sm_tubo_plast.genesys.DAO.DAO_Producto;
import com.example.sm_tubo_plast.genesys.Retrofit.Result.bean.ResultProducto;
import com.example.sm_tubo_plast.genesys.Retrofit.Result.bean.ResultStockArticulo;
import com.example.sm_tubo_plast.genesys.Retrofit.RetrofilClientCantol;
import com.example.sm_tubo_plast.genesys.Retrofit.request.GetDataCantol;
import com.example.sm_tubo_plast.genesys.Retrofit.request.RequestCliente;
import com.example.sm_tubo_plast.genesys.Retrofit.request.producto.RequestProducto;
import com.example.sm_tubo_plast.genesys.Retrofit.util.WS_RetrofitCustom;
import com.example.sm_tubo_plast.genesys.datatypes.DBSync_soap_manager;
import com.example.sm_tubo_plast.genesys.datatypes.DBclasses;
import com.example.sm_tubo_plast.genesys.util.FontManager;
import com.example.sm_tubo_plast.genesys.util.GlobalFunctions;
import com.example.sm_tubo_plast.genesys.util.VARIABLES;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;

public class CH_InformacionProducto extends AppCompatActivity {

    public static final String TAG = "CH_InformacionProducto";
    String codigoProducto;
    Producto producto;

    EditText edt_codigo,edt_descripcion,edt_unidadMedida;
    TextView tv_totalStockConfirmar, tv_totalStockDisponible, edt_precioLista,
            edtPesoUnitario;
    ListView lv_consultaStock;

    DBSync_soap_manager soap_manager;
    DBclasses database;

    private double totalStockConfirmar = 0.0d;
    private double totalStockDisponible = 0.0d;

    @SuppressLint("NewApi")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_h__informacion_producto);

        DAO_Producto dao_producto = new DAO_Producto(getApplicationContext());
        soap_manager = new DBSync_soap_manager(getApplicationContext());
        database = new DBclasses(getApplicationContext());

        Typeface iconFont = FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME);
        FontManager.markAsIconContainer(findViewById(R.id.icons_container), iconFont);

        //getActionBar().setHomeButtonEnabled(true);

        //------------------------------------------
        edt_codigo = (EditText) findViewById(R.id.edt_codigo);
        edt_descripcion = (EditText) findViewById(R.id.edt_descripcion);
        edt_unidadMedida = (EditText) findViewById(R.id.edt_unidadMedida);
        lv_consultaStock = (ListView) findViewById(R.id.lv_consultaStock);
        tv_totalStockConfirmar = (TextView) findViewById(R.id.tv_totalStockConfirmar);
        tv_totalStockDisponible = (TextView) findViewById(R.id.tv_totalStockDisponible);
        edt_precioLista = (TextView) findViewById(R.id.edt_precioLista);
        edtPesoUnitario = (TextView) findViewById(R.id.edtPesoUnitario);
        //------------------------------------------

        Bundle bundle = getIntent().getExtras();
        codigoProducto = bundle.getString("codigoProducto");

        producto = dao_producto.getInformacionProducto(codigoProducto);
        if (producto!=null) {
            edt_codigo.setText(""+producto.getCodigo());
            edt_descripcion.setText(""+producto.getDescripcion());
            edt_unidadMedida.setText(""+producto.getUnidadMedida());
            edt_precioLista.setText(""+producto.getPrecio_base());
            edtPesoUnitario.setText(""+ VARIABLES.getStringFormaterThreeDecimal(producto.getPeso()));
            if (!producto.getColor().equals("")) {
                edt_codigo.setTextColor(Color.parseColor(producto.getColor()));
            }
        }

        //Cargar Stock
//        new AsyncTask<Void, Void, Void>() {
//            ProgressDialog pDialog;
//            String respuestaStock;
//
//            @Override
//            protected void onPreExecute() {
//                pDialog = new ProgressDialog(CH_InformacionProducto.this);
//                pDialog.setMessage("Cargando Stock....");
//                pDialog.setIndeterminate(false);
//                pDialog.setCancelable(false);
//                pDialog.show();
//            }
//
//            @Override
//            protected Void doInBackground(Void... params) {
//                try {
//                    respuestaStock = soap_manager.sincro_obtenerStockProducto_json(codigoProducto);
//                } catch (Exception e) {
//                    respuestaStock = "";
//                    e.printStackTrace();
//                }
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void result) {
//                Log.d(TAG, "respuestaStock:"+respuestaStock);
//                pDialog.dismiss();
//                respuestConsultarProducto(null/*respuestaStock*/);
//            }
//        }.execute();
        sincronizarProductoStock();
    }

    private void respuestConsultarProducto(ArrayList<ResultStockArticulo> lista) {
        Gson gson = new Gson();

        Type listType = new TypeToken<ArrayList<Map<String, Object>>>() {}.getType();

        if (lista.size()>0) {
            if (true) {
                if (true) {
                    Adapter_consultaStock adapter = new Adapter_consultaStock(CH_InformacionProducto.this, lista);
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
                    //old
                    /*LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) lv_consultaStock.getLayoutParams();
                    params.height = (lv_consultaStock.getCount() * 20);
                    params.width = LinearLayout.LayoutParams.MATCH_PARENT;*/
                }else{
                    Toast.makeText(getApplicationContext(), "Sin lista de stock", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getApplicationContext(), "Sin lista de stock", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "No se pudo consultar stock, vuelva a intentarlo", Toast.LENGTH_SHORT).show();
        }

    }

    public class Adapter_consultaStock extends BaseAdapter {

        protected Activity activity;
        protected ArrayList<ResultStockArticulo> lista;

        public Adapter_consultaStock(Activity activity, ArrayList<ResultStockArticulo> lista){
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
                item = inflater.inflate(R.layout.item_consulta_stock, null);
                holder = new ViewHolder();

                holder.tv_almacen = (TextView) item.findViewById(R.id.tv_almacen);
                holder.tv_stock_actual = (TextView) item.findViewById(R.id.tv_stock_actual);
                holder.tv_stock_separado = (TextView) item.findViewById(R.id.tv_stock_separado);
                holder.tv_stock_xConfirmar = (TextView) item.findViewById(R.id.tv_stock_xConfirmar);
                holder.tv_stockDisponible = (TextView) item.findViewById(R.id.tv_stockDisponible);

                item.setTag(holder);
            } else {
                holder = (ViewHolder) item.getTag();
            }


                ResultStockArticulo dataStock = lista.get(position);
                String nombre=  dataStock.getNombre_almacen();//database.getAlmacenDescripcionResumen((String) jsonData.get("codigoAlmacen"));
                holder.tv_almacen.setText(nombre);
                holder.tv_stock_actual.setText(VARIABLES.formater_integer.format(dataStock.getEn_stock()));
                holder.tv_stock_separado.setText(VARIABLES.formater_integer.format(dataStock.getComprometido()));
                holder.tv_stock_xConfirmar.setText(VARIABLES.formater_integer.format(dataStock.getEn_pedido()));
                holder.tv_stockDisponible.setText(VARIABLES.formater_integer.format(dataStock.getDisponible()));

            try {
                totalStockConfirmar += Double.parseDouble(holder.tv_stock_xConfirmar.getText().toString());
                totalStockDisponible += Double.parseDouble(holder.tv_stockDisponible.getText().toString());
                tv_totalStockConfirmar.setText(""+totalStockConfirmar);
                tv_totalStockDisponible.setText(""+totalStockDisponible);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return item;
        }

        public class ViewHolder {
            TextView tv_almacen,tv_stock_actual, tv_stock_separado, tv_stock_xConfirmar, tv_stockDisponible;
        }

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void sincronizarProductoStock(){
        ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Consultando stock...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        String urlReq=RetrofilClientCantol.UrlPeticiones.getListaStockByProducto(codigoProducto);
        RequestBody body = RetrofilClientCantol.createBodyJson(RequestProducto.Companion.getListaStock(urlReq));
        Call<Object> call = RetrofilClientCantol.getRetrofitInstanceCantolWithToken(this)
                .create(GetDataCantol.class).getCliente(body);
        WS_RetrofitCustom ws_retrofitCustom= new WS_RetrofitCustom(this);
        ws_retrofitCustom.StartPeticion(call, new WS_RetrofitCustom.MyListener() {
            @Override
            public void StartFinish(boolean isFinish) {
                if (!isFinish) pDialog.show();
                else pDialog.dismiss();
            }
            @Override
            public void Result(boolean isOk, String mensaje, Object data) {
                if(!isOk){
                    GlobalFunctions.showCustomToast(
                            CH_InformacionProducto.this,
                            mensaje,
                            GlobalFunctions.TOAST_ERROR);
                    return;
                }
                Gson gson=new Gson();
                final Type malla = new TypeToken<ArrayList<ResultStockArticulo>>() {}.getType();
                final ArrayList<ResultStockArticulo> lista = gson.fromJson(gson.toJson(data), malla);
                Log.i(TAG, "cant stock "+lista.size());
                if(lista.size()==0){
                    GlobalFunctions.showCustomToast(
                            CH_InformacionProducto.this,
                            "No hay lista de stock",
                            GlobalFunctions.TOAST_WARNING);
                }
                respuestConsultarProducto(lista);
            }
        });
    }
}

