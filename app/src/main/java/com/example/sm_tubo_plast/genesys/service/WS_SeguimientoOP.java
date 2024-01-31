package com.example.sm_tubo_plast.genesys.service;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.example.sm_tubo_plast.genesys.BEAN.ViewSeguimientoPedido;
import com.example.sm_tubo_plast.genesys.BEAN.ViewSeguimientoPedidoDetalle;
import com.example.sm_tubo_plast.genesys.datatypes.DBSync_soap_manager;
import com.example.sm_tubo_plast.genesys.datatypes.DB_Servidor;
import com.example.sm_tubo_plast.genesys.datatypes.DBclasses;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.SeguimientoPedidoActivity;
import com.example.sm_tubo_plast.genesys.session.SessionManager;
import com.example.sm_tubo_plast.genesys.util.SharePrefencia.PreferenciaPrincipal;
import com.example.sm_tubo_plast.genesys.util.UtilView;

import java.util.ArrayList;

public class WS_SeguimientoOP {
    private static final  String TAG = "WS_SeguimientoOP";
    Activity activity;
    AsyncTask tarea=null;

    MyListener myListener;
    MyListenerDetalle myListenerDetalle;
    String codven="";

    public int desde=1;
    final public int nro_item=19;// 0,1...19= 20 items
    ArrayList<ViewSeguimientoPedido> dataPrecargada=new ArrayList<>();
    public WS_SeguimientoOP(Activity activity) {
        this.activity = activity;
        codven = new SessionManager(activity).getCodigoVendedor();

    }

    public void GetOrdenCompraPedido(String codcli, String orden_compra, String fecha_desde,
                                     String fecha_hasta,
                                     String numero_op,
                                     String tipoFiltro,
                                     MyListener myListener) {
        this.myListener=myListener;

        ProgressDialog pDialog = new ProgressDialog(this.activity);
        pDialog.setMessage("Obteniendo datos del sistema....");
        pDialog.setIndeterminate(false);
        if(desde<=1){pDialog.show();}
        pDialog.setCancelable(false);
        DBSync_soap_manager soap_manager = new DBSync_soap_manager(this.activity);
        new AsyncTask<Void, Void, String>() {
            ArrayList<ViewSeguimientoPedido> data= new ArrayList<>();
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Void... voids) {

                try {
                    if (dataPrecargada.size()==0) {
                        ConnectionDetector cd = new ConnectionDetector(activity);
                        //DB_Servidor dbServidorSM=new PreferenciaPrincipal(activity).geServerSaemovil();
                        if (cd.isConnectingToInternet()){
                            if (cd.hasActiveInternetConnection(activity)) {
                                data= soap_manager.get_tplast_seguimiento_pedido(codven,
                                        ""+codcli,
                                        ""+orden_compra,
                                        fecha_desde,
                                        fecha_hasta,
                                        numero_op,
                                        tipoFiltro,
                                        desde,
                                        desde+nro_item);
                                return "OK";
                            } else {
                                return "NO_SERVER";
                            }
                        }else {
                            return "NO_INTERNET";
                        }
                    }else{
                        data=dataPrecargada;
                        return "OK";
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
    public void GetPedidoOP(String numero_op,  MyListenerDetalle myListenerDetalle) {
        this.myListenerDetalle=myListenerDetalle;

        ProgressDialog pDialog = new ProgressDialog(this.activity);
        pDialog.setMessage("Obteniendo datos del sistema....");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);

        pDialog.show();
        DBSync_soap_manager soap_manager = new DBSync_soap_manager(this.activity);
        new AsyncTask<Void, Void, String>() {
            ArrayList<ViewSeguimientoPedidoDetalle> data= new ArrayList<>();
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
                            data= soap_manager.get_tplast_seguimiento_pedido_detalle_json(codven, ""+numero_op);
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
    public  void  setDataCargada(ViewSeguimientoPedido data){
        this.dataPrecargada.clear();
        this.dataPrecargada.add(data);
    }
    public interface MyListener{
        void Reult(boolean valor, ArrayList<ViewSeguimientoPedido> data);
    }
    public interface MyListenerDetalle{
        void ReultDetalle(boolean valor, ArrayList<ViewSeguimientoPedidoDetalle> dataDet);
    }
}
