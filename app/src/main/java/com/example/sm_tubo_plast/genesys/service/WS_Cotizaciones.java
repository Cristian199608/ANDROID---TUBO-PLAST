package com.example.sm_tubo_plast.genesys.service;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.example.sm_tubo_plast.genesys.BEAN.ViewSeguimientoPedido;
import com.example.sm_tubo_plast.genesys.BEAN.ViewSeguimientoPedidoDetalle;
import com.example.sm_tubo_plast.genesys.BEAN_API.CotizacionCabeceraApi;
import com.example.sm_tubo_plast.genesys.BEAN_API.CotizacionDetalleApi;
import com.example.sm_tubo_plast.genesys.datatypes.DBSync_soap_manager;
import com.example.sm_tubo_plast.genesys.session.SessionManager;
import com.example.sm_tubo_plast.genesys.util.UtilView;
import com.example.sm_tubo_plast.genesys.util.VARIABLES;

import java.util.ArrayList;

public class WS_Cotizaciones {
    private static final  String TAG = "WS_Cotizaciones";
    Activity activity;
    AsyncTask tarea=null;

    MyListener myListener;
    MyListenerDetalle myListenerDetalle;
    String codven="";

    public int desde=1;
    final public int nro_item=19;// 0,1...19= 20 items

    public WS_Cotizaciones(Activity activity) {
        this.activity = activity;
        codven = new SessionManager(activity).getCodigoVendedor();

    }

    public void getDataCabeceras(String codigo_pedido, String codcli,
                                 String fecha_desde,
                                 String fecha_hasta,
                                 int desde,
                                 int hasta,
                                 MyListener myListener) {
        this.myListener=myListener;

        String mensaje=desde<=1?"Obteniendo datos del sistema....":"Obteniendo mas datos del sistema....";
        ProgressDialog pDialog = new ProgressDialog(this.activity);
        pDialog.setMessage(mensaje);
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        if(desde<=1)pDialog.show();

        DBSync_soap_manager soap_manager = new DBSync_soap_manager(this.activity);
        new AsyncTask<Void, Void, String>() {
            ArrayList<CotizacionCabeceraApi> data= new ArrayList<>();
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Void... voids) {

                try {
                    ConnectionDetector cd = new ConnectionDetector(activity);
                    if (cd.isConnectingToInternet()){
                        if (cd.hasActiveInternetConnection(activity)) {
                            if (!VARIABLES.isProduccion) {
                                Thread.sleep(1000);
                            }
                            data= soap_manager.SyncTBCotizacionCabeceraApi(codven,codigo_pedido, codcli, fecha_desde, fecha_hasta, desde, hasta);
                            return "OK";
                        } else {
                            return "NO_SERVER";
                        }
                    }else {
                        return "NO_INTERNET";
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    return "error@" + e.getMessage();
                }
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                pDialog.dismiss();
                String titulo = "";
                String mensaje = "";
                if (result.equalsIgnoreCase("OK")) {
                    titulo = "Mensaje";
                    mensaje = "Datos obtenidos correctamente";

                    if (data.size()==0)UtilView.MENSAJE_simple(activity, "Sin dato", "No hay pedidos disponibles");

                    myListener.Reult(true, data);
                }
                else if (result.equalsIgnoreCase("NO_INTERNET")) {
                    titulo = "Sin Conexión";
                    mensaje = "No tienes acceso a internet";
                }
                else if (result.equalsIgnoreCase("NO_SERVER")) {
                    titulo = "Sin acceso al servidor.";
                    mensaje = "No pudo establecer la conexión al servidor. Vuelva a intentarlo.";
                }else{
                    titulo = "Error";
                    mensaje = "No se ha podido obtener la información, vuelva a intenrarlo. ";
                }
                if (!result.equalsIgnoreCase("OK")){
                    UtilView.MENSAJE_simple_finish_return_intent(activity,
                            "" + titulo, "" + mensaje, false);
                }
            }

        }.execute();
    }
    public void getDataDetalles(String codigo_pedido,  MyListenerDetalle myListenerDetalle) {
        this.myListenerDetalle=myListenerDetalle;

        ProgressDialog pDialog = new ProgressDialog(this.activity);
        pDialog.setMessage("Obteniendo el detalle del pedido "+codigo_pedido+" datos del sistema....");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);

        pDialog.show();
        DBSync_soap_manager soap_manager = new DBSync_soap_manager(this.activity);
        new AsyncTask<Void, Void, String>() {
            ArrayList<CotizacionDetalleApi> data= new ArrayList<>();
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Void... voids) {

                try {

                    ConnectionDetector cd = new ConnectionDetector(activity);
                    if (cd.isConnectingToInternet()){
                        if (cd.hasActiveInternetConnection(activity)) {
                            if (!VARIABLES.isProduccion) {
                                Thread.sleep(1000);
                            }
                            data= soap_manager.SyncTBCotizacionDetalleApi(codven, codigo_pedido);
                            return "OK";
                        } else {
                            return "NO_SERVER";
                        }
                    }else {
                        return "NO_INTERNET";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return "error@" + e.getMessage();
                }
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                pDialog.dismiss();
                String titulo = "";
                String mensaje = "";
                if (result.equalsIgnoreCase("OK")) {
                    titulo = "Mensaje";
                    mensaje = "Datos obtenidos correctamente";
                    myListenerDetalle.ReultDetalle(true, data);
                }
                else if (result.equalsIgnoreCase("NO_INTERNET")) {
                    titulo = "Sin Conexión";
                    mensaje = "No tienes acceso a internet";
                }
                else if (result.equalsIgnoreCase("NO_SERVER")) {
                    titulo = "Sin acceso al servidor.";
                    mensaje = "No pudo establecer la conexión al servidor. Vuelva a intentarlo.";
                }else{
                    titulo = "Error";
                    mensaje = "No se ha podido obtener la información, vuelva a intenrarlo. ";
                }
                if (!result.equalsIgnoreCase("OK")){
                    UtilView.MENSAJE_simple_finish_return_intent(activity,
                            "" + titulo, "" + mensaje, false);
                }
            }

        }.execute();
    }

    public interface MyListener{
        void Reult(boolean success, ArrayList<CotizacionCabeceraApi> data);
    }
    public interface MyListenerDetalle{
        void ReultDetalle(boolean success, ArrayList<CotizacionDetalleApi> dataDet);
    }
}
