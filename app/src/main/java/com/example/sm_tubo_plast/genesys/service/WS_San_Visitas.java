package com.example.sm_tubo_plast.genesys.service;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.example.sm_tubo_plast.genesys.DAO.DAO_Cliente_Contacto;
import com.example.sm_tubo_plast.genesys.datatypes.DBSync_soap_manager;
import com.example.sm_tubo_plast.genesys.datatypes.DBclasses;
import com.example.sm_tubo_plast.genesys.service.ConnectionDetector;
import com.example.sm_tubo_plast.genesys.util.UtilView;
import com.example.sm_tubo_plast.genesys.util.UtilViewMensaje;

public class WS_San_Visitas {

    private static final  String TAG = "WS_Cliente_Contacto";
    Activity activity;
    DBclasses dBclasses;
    AsyncTask tarea=null;

    ProgressDialog pDialog;

    SharedPreferences prefs;
    String url;
    String catalog;
    String user;
    String contrasena;
    String servicio;

    public boolean finalizarActitivty=true;
    MyListener myListener;

    public WS_San_Visitas(Activity activity, DBclasses dBclasses) {
        this.activity = activity;
        this.dBclasses = dBclasses;
        prefs = activity.getSharedPreferences("MisPreferencias",Activity.MODE_PRIVATE);
        url = prefs.getString("url", "0");
        catalog = prefs.getString("catalog", "0");
        user = prefs.getString("userid", "0");
        contrasena = prefs.getString("contrasenaid", "0");
        servicio = prefs.getString("servicio", "0");
    }

    public void EnviarVisitasByOc_numero(String oc_numero){
        ProgressDialog pDialog = new ProgressDialog(this.activity);
        pDialog.setMessage("Enviando datos al sistema....");
        pDialog.setIndeterminate(false);

        pDialog.show();
        DBSync_soap_manager soap_manager = new DBSync_soap_manager(this.activity);
        new AsyncTask<Void, Void, String>(){
            @Override
            protected String doInBackground(Void... voids) {

                try {
                    ConnectionDetector cd = new ConnectionDetector(activity);

                    if (cd.hasActiveInternetConnection(activity)) {
                        return soap_manager.actualizarObjPedido_directo(oc_numero);
                    }else{
                        return "error_1";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return "error@"+e.getMessage();
                }

            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                pDialog.dismiss();
                String titulo="";
                String mensaje="";
                if (result.equalsIgnoreCase("E")){
                    titulo="Datos guardados";
                    mensaje="Se registró la visita correctamente";
                }else{
                    titulo="Error";
                    mensaje="No se ha enviado la visita al servidor. Se guardó los datos como pendiente.";
                }
                    UtilView.MENSAJE_simple_finish_return_intent(activity,
                            ""+titulo, ""+mensaje, finalizarActitivty);

            }


        }.execute();
    }

    public void getFecha_servidor(MyListener listener){
        this.myListener=listener;
        ProgressDialog pDialog = new ProgressDialog(this.activity);
        pDialog.setMessage("Validando fecha de la visita");
        pDialog.setIndeterminate(false);

        pDialog.show();
        DBSync_soap_manager soap_manager = new DBSync_soap_manager(this.activity);
        new AsyncTask<Void, Void, String>(){
            @Override
            protected String doInBackground(Void... voids) {

                try {
                    ConnectionDetector cd = new ConnectionDetector(activity);

                    if (cd.hasActiveInternetConnection(activity)) {
                        return  soap_manager.obtenerHoraServBD(url,	catalog,user, contrasena);

                    }else{
                        return "error_1";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return "error_1@"+e.getMessage();
                }

            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                pDialog.dismiss();
                String titulo="";
                String mensaje="";
                if (result.equals("error_1")){
                    titulo="Sin conexión a internet";
                    mensaje="No se pudo validar la fecha y hora.";
                }else  if (result.contains("error_1@")){
                    titulo="Error";
                    mensaje="No se pudo conectarse con el servidor para verificar la feha y hora. Vuelva intentarlo.";
                }
                if (titulo.length()>0){
                    UtilView.MENSAJE_simple(activity,
                            ""+titulo, ""+mensaje);
                    myListener.fecha_servidor(null);
                }else{
                    myListener.fecha_servidor(result);
                }


            }


        }.execute();
    }

    public interface MyListener{
        void fecha_servidor(String fecha_server);
    }

}
