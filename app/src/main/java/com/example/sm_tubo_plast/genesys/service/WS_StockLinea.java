package com.example.sm_tubo_plast.genesys.service;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.example.sm_tubo_plast.genesys.BEAN.ItemProducto;
import com.example.sm_tubo_plast.genesys.DAO.DAO_Pedido;
import com.example.sm_tubo_plast.genesys.DAO.DAO_Pedido_detalle2;
import com.example.sm_tubo_plast.genesys.datatypes.DBSync_soap_manager;
import com.example.sm_tubo_plast.genesys.datatypes.DBclasses;

import java.util.ArrayList;
import java.util.Arrays;

public class WS_StockLinea extends AsyncTask<Void, Void, String> {
    public interface  Callback{
        void CargadoOK(boolean isok);
    }
    Activity activity;
    ProgressDialog progressDialog=null;
    //ArrayList<ItemProducto> itemProductos=null;
    DBclasses dBclasses;
    String codven;
    String codAlmacen;
    long timeSincronizacion;

    Callback callback;
    public WS_StockLinea(Activity activity, String codven, String codAlmacen, long timeSincronizacion, Callback callback) {
        this.activity = activity;
        this.codven=codven;
        this.codAlmacen=codAlmacen;
        this.timeSincronizacion=timeSincronizacion;
        this.dBclasses=new DBclasses(activity);
        this.callback=callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog=new ProgressDialog(activity);
        progressDialog.setTitle("Stock");
        progressDialog.setMessage("Validando stock en linea...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    @Override
    protected String doInBackground(Void... voids) {

        try {

            ItemProducto[] prods = dBclasses.getProductosXTIME_SYNC(""+timeSincronizacion);
            StringBuilder cadenas=new StringBuilder("0");
            for (ItemProducto prod : prods) {
                cadenas.append(",").append(prod.getCodprod());
            }
            String listaStock_json = new DBSync_soap_manager(activity).
                    getListaStockLinea(codven,cadenas.toString(),codAlmacen);

            if (listaStock_json==null) return null;
            return "ok";
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        progressDialog.dismiss();
        callback.CargadoOK(result!=null);
    }
}

