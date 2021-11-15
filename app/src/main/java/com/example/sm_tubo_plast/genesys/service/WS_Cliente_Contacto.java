package com.example.sm_tubo_plast.genesys.service;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.example.sm_tubo_plast.genesys.BEAN.Cliente_Contacto;
import com.example.sm_tubo_plast.genesys.DAO.DAO_Cliente_Contacto;
import com.example.sm_tubo_plast.genesys.datatypes.DBSync_soap_manager;
import com.example.sm_tubo_plast.genesys.datatypes.DBclasses;
import com.example.sm_tubo_plast.genesys.service.ConnectionDetector;
import com.example.sm_tubo_plast.genesys.util.GlobalVar;
import com.example.sm_tubo_plast.genesys.util.UtilView;
import com.example.sm_tubo_plast.genesys.util.UtilViewMensaje;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class WS_Cliente_Contacto {
    private static final  String TAG = "WS_Cliente_Contacto";
    Activity activity;
    DBclasses dBclasses;
    AsyncTask tarea=null;
    DAO_Cliente_Contacto dao_cliente_contacto;

    ProgressDialog pDialog;

    SharedPreferences prefs;
    String url;
    String catalog;
    String user;
    String contrasena;
    String servicio;

    public WS_Cliente_Contacto(Activity activity, DBclasses dBclasses) {
        this.activity = activity;
        this.dBclasses = dBclasses;
        prefs = activity.getSharedPreferences("MisPreferencias",Activity.MODE_PRIVATE);
        url = prefs.getString("url", "0");
        catalog = prefs.getString("catalog", "0");
        user = prefs.getString("userid", "0");
        contrasena = prefs.getString("contrasenaid", "0");
        servicio = prefs.getString("servicio", "0");
        dao_cliente_contacto=new DAO_Cliente_Contacto();

    }

    public void EnviarCliente_Contacto(){
        pDialog = new ProgressDialog(activity);
        pDialog.setMessage("Enviando información...");
        pDialog.setIndeterminate(false);
        pDialog.show();

        tarea=new AsyncTask() {
            int isOk=-1;
            @Override
            protected Object doInBackground(Object[] objects) {

                try {
                    ConnectionDetector cd=new ConnectionDetector(activity);
                    if (cd.hasActiveInternetConnection(activity)){
                        ArrayList<Cliente_Contacto> lista =dao_cliente_contacto.getClientesPendientesAll(dBclasses);
                        String cadena=new Gson().toJson(lista);
                        isOk=EnviarClienteContacto(cadena);

                    }else{
                        isOk=-1;
                    }
                }catch (Exception e){
                    isOk=-2;
                    e.printStackTrace();
                }
                return null;
            }


            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                pDialog.dismiss();
                if (isOk==-1) {
                    UtilViewMensaje.MENSAJE_simple(activity, "Sin internet", "No estas conectado a una red. Vuelva a intentarlo.");
                }else if(isOk==0){
                    UtilViewMensaje.MENSAJE_simple(activity, "Sin datos", "No hay datos pendientes por ahora");
                }
                else if(isOk==1){
                    UtilViewMensaje.MENSAJE_simple(activity, "Enviado", "Los datos pendientes se enviaron correctamente. ");
                }
                else if(isOk==-2){
                    UtilViewMensaje.MENSAJE_simple(activity, "Error", "No se ha podido enviar al servidor. Vuelva a intentarlo.");
                }

            }
        };
        tarea.execute();
    }


    public void EnviarClienteContactoPendienteByCliente(String codcliente){

        pDialog = new ProgressDialog(activity);
        pDialog.setMessage("Enviando información...");
        pDialog.setIndeterminate(false);
        pDialog.show();

        tarea=new AsyncTask() {
            int isOk=-1;
            @Override
            protected Object doInBackground(Object[] objects) {

                try {
                    ConnectionDetector cd=new ConnectionDetector(activity);
                    if (cd.hasActiveInternetConnection(activity)){

                        ArrayList<Cliente_Contacto> lista =dao_cliente_contacto.getClienteContactoByID(dBclasses, codcliente, 0);
                        String cadena=new Gson().toJson(lista);
                        isOk=EnviarClienteContacto(cadena);

                    }else{
                        isOk=-1;
                    }
                }catch (Exception e){
                    isOk=-2;
                    e.printStackTrace();
                }
                return null;
            }


            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                pDialog.dismiss();
                if (isOk==-1) {
                    UtilViewMensaje.MENSAJE_simple(activity, "Sin internet", "No se pudo enviar al servidor, pero se gurdó como pendiente de envio");
                }else if(isOk==0){
                    UtilViewMensaje.MENSAJE_simple(activity, "Sin datos", "No hay datos para enviar");
                }
                else if(isOk==1){
                    UtilViewMensaje.MENSAJE_simple(activity, "Guardado", "Contacto del cliente guardado en el servidor ");
                }
                else if(isOk==-2){
                    UtilViewMensaje.MENSAJE_simple(activity, "Error", "No se ha podido enviar al servidor, pero se gurdó como pendiente de envio");
                }

            }
        };
        tarea.execute();


    }

    private int EnviarClienteContacto(String cadena) throws Exception{




        String SOAP_ACTION= "http://tempuri.org/actualizar_cliente_contacto_json";
        String METHOD_NAME="actualizar_cliente_contacto_json";

        SoapObject Request=new SoapObject(DBSync_soap_manager.NAMESPACE, METHOD_NAME);
        Request.addProperty("cadena", cadena);
        Request.addProperty("url", url);
        Request.addProperty("catalog", catalog);
        Request.addProperty("user", user);
        Request.addProperty("password", contrasena);
        SoapSerializationEnvelope Soapenvelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
        Soapenvelope.dotNet=true;
        Soapenvelope.setOutputSoapObject(Request);

        HttpTransportSE transporte=new HttpTransportSE(DBSync_soap_manager.URL+ GlobalVar.urlService);
        //HttpTransportSE transporte=new HttpTransportSE("http://190.40.100.50/SAEMV-WEB/service.asmx");

        long beforecall = System.currentTimeMillis();

        try{
            transporte.call(SOAP_ACTION, Soapenvelope);
            Log.i(TAG,"RESPUESTA EN: "+(System.currentTimeMillis()-beforecall)+"miliseg");

            SoapPrimitive result =(SoapPrimitive)Soapenvelope.getResponse();
            Log.i(TAG, "resuesta "+result.toString());
            JSONArray jsonstring = new JSONArray(result.toString());
            Log.d(TAG, jsonstring.toString());



            for (int i = 0; i < jsonstring.length(); i++) {
                JSONObject jsonData = jsonstring.getJSONObject(i);
                dao_cliente_contacto.actualizar_cliente_contacto_flag(
                        dBclasses,
                        jsonData.getString("codcli").trim(),
                        jsonData.getString("dni").trim(),
                        jsonData.getString("flag").trim());
            }

            Log.i(TAG, "SINCRONIZADA");

            return 1;
        }catch(Exception e){
            e.printStackTrace();
            Log.e(TAG, "NO SINCRONIZADA");
            throw new Exception(e);
        }

    }
}

