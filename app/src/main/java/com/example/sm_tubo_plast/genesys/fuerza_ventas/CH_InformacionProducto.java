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
import com.example.sm_tubo_plast.genesys.datatypes.DBSync_soap_manager;
import com.example.sm_tubo_plast.genesys.datatypes.DBclasses;
import com.example.sm_tubo_plast.genesys.util.FontManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CH_InformacionProducto extends AppCompatActivity {

    public static final String TAG = "CH_InformacionProducto";
    String codigoProducto;
    Producto producto;

    EditText edt_codigo,edt_descripcion,edt_unidadMedida;
    TextView tv_totalStockConfirmar, tv_totalStockDisponible;
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
        //------------------------------------------

        Bundle bundle = getIntent().getExtras();
        codigoProducto = bundle.getString("codigoProducto");

        producto = dao_producto.getInformacionProducto(codigoProducto);
        if (producto!=null) {
            edt_codigo.setText(""+producto.getCodigo());
            edt_descripcion.setText(""+producto.getDescripcion());
            edt_unidadMedida.setText(""+producto.getUnidadMedida());
            if (!producto.getColor().equals("")) {
                edt_codigo.setTextColor(Color.parseColor(producto.getColor()));
            }
        }

        //Cargar Stock
        new AsyncTask<Void, Void, Void>() {
            ProgressDialog pDialog;
            String respuestaStock;

            @Override
            protected void onPreExecute() {
                pDialog = new ProgressDialog(CH_InformacionProducto.this);
                pDialog.setMessage("Cargando Stock....");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(false);
                pDialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    respuestaStock = soap_manager.sincro_obtenerStockProducto_json(codigoProducto);
                } catch (Exception e) {
                    respuestaStock = "";
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                Log.d(TAG, "respuestaStock:"+respuestaStock);
                pDialog.dismiss();
                respuestConsultarProducto(respuestaStock);
            }
        }.execute();
    }

    private void respuestConsultarProducto(String respuestaStock) {
        Gson gson = new Gson();

        Type listType = new TypeToken<ArrayList<Map<String, Object>>>() {}.getType();

        if (!respuestaStock.equals("")) {
            ArrayList<HashMap<String, Object>> listMap2 = gson.fromJson(respuestaStock,listType);
            if (listMap2 != null) {
                if (!listMap2.isEmpty()) {
                    Adapter_consultaStock adapter = new Adapter_consultaStock(CH_InformacionProducto.this, listMap2);
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
        protected ArrayList<HashMap<String, Object>> lista;

        public Adapter_consultaStock(Activity activity, ArrayList<HashMap<String, Object>> lista){
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
                holder.tv_stock_xConfirmar = (TextView) item.findViewById(R.id.tv_stock_xConfirmar);
                holder.tv_stockDisponible = (TextView) item.findViewById(R.id.tv_stockDisponible);

                item.setTag(holder);
            } else {
                holder = (ViewHolder) item.getTag();
            }


            try {

            //JSONObject jsonData = null listaArray.getJSONObject(position);

            //HashMap<String, Object> map = lista.get(position);

            //holder.tv_almacen.setText(database.getAlmacenDescripcion((String)map.get("codigoAlmacen")));

                JSONObject jsonData = new JSONObject(lista.get(position));
                holder.tv_almacen.setText(database.getAlmacenDescripcionResumen((String) jsonData.get("codigoAlmacen")));
                //holder.tv_almacen.setText((String)map.get("codigoAlmacen"));
                holder.tv_stock_xConfirmar.setText((String)jsonData.get("stock_x_confirmar"));
                holder.tv_stockDisponible.setText((String)jsonData.get("stock_disponible"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                totalStockConfirmar += Double.parseDouble(holder.tv_stock_xConfirmar.getText().toString());
                totalStockDisponible += Double.parseDouble(holder.tv_stockDisponible.getText().toString());
                //Log.d(TAG, "totalStockDisponible:"+totalStockDisponible+" + "+holder.tv_stockDisponible.getText() );
                tv_totalStockConfirmar.setText(""+totalStockConfirmar);
                tv_totalStockDisponible.setText(""+totalStockDisponible);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return item;
        }

        public class ViewHolder {
            TextView tv_almacen,tv_stock_xConfirmar, tv_stockDisponible;
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
}

