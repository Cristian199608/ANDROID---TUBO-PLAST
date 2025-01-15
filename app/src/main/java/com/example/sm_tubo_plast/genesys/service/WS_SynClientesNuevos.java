package com.example.sm_tubo_plast.genesys.service;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.datatypes.DBSync_soap_manager;
import com.example.sm_tubo_plast.genesys.util.GlobalFunctions;
import com.example.sm_tubo_plast.genesys.util.GlobalVar;

public class WS_SynClientesNuevos extends AsyncTask<String, String, String> {

    Activity activity;
    String codven;

    MyCallback myCallback;
    public interface MyCallback{
        void result(boolean ok);
    }
    public WS_SynClientesNuevos(Activity activity, String codven) {
        this.activity = activity;
        this.codven = codven;
    }

    public void ejecutar(MyCallback myCallback ){
        this.myCallback=myCallback;
        this.execute();
    }

    //-----------------------------------------------------------------------------------------------
    String NombreMetodo="";
    ProgressDialog pDialog=null;
    int cantCliNew =0;
    @Override
    protected void onPreExecute() {
        // para el progress dialog
        pDialog = new ProgressDialog(activity);
        pDialog.setMessage("Sincronizando clientes nuevos...");
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... voids) {
        String error="0";
        ConnectionDetector cd = new ConnectionDetector(activity);
        DBSync_soap_manager soap_manager=new DBSync_soap_manager(activity);
        if (cd.hasActiveInternetConnection(activity)) {
            String datetime = "";
            int valor=0;

            String ventana="Lista Clientes";
            int isLOGIN=1;
            String servidorBD_ERP="";
            String nombreBD_ERP="";
            String usuarioBD_ERP="";
            String contrasenaBD_ERP="";

            try{

                servidorBD_ERP=soap_manager.url;
                nombreBD_ERP=soap_manager.catalog;
                usuarioBD_ERP=soap_manager.user;
                contrasenaBD_ERP=soap_manager.contrasena;
            } catch (Exception e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, "No se pudo obtener servicio de conexión", Toast.LENGTH_SHORT).show();
                    }
                });
                NombreMetodo="(-1)getServiciosBDLocal";
                NombreMetodo+=" No se pudo obtener servicio ERP. Error => "+e.getMessage();
                e.printStackTrace();
            }
            try {
                //Va consumir el mismo la bd SAEmoviles
                NombreMetodo="(1)obtenerHoraServBD";
                publishProgress("5");
                datetime = soap_manager.obtenerHoraServBD(servidorBD_ERP, nombreBD_ERP,	usuarioBD_ERP, contrasenaBD_ERP );

                publishProgress("10");
                NombreMetodo="("+valor+")Sync_tabla_clientexVendedor";
                cantCliNew =soap_manager.Sync_tabla_clientexVendedor(codven, "HOY",servidorBD_ERP, nombreBD_ERP,	usuarioBD_ERP, contrasenaBD_ERP, 0,1000);
                if(cantCliNew >0){
                    publishProgress("20");
                    NombreMetodo="("+valor+")Sync_tabla_direccion_cliente";
                    soap_manager.Sync_tabla_direccion_cliente(codven, "HOY", servidorBD_ERP, nombreBD_ERP,	usuarioBD_ERP, contrasenaBD_ERP, 0,1000);

                    publishProgress("30");
                    NombreMetodo="("+valor+")Sync_tabla_ZnfProgramacionClientes";
                    soap_manager.Sync_tabla_ZnfProgramacionClientes(codven, "HOY", servidorBD_ERP, nombreBD_ERP,	usuarioBD_ERP, contrasenaBD_ERP, 0,1000);

                    publishProgress("40");
                    NombreMetodo="("+valor+")Sync_tabla_cliente_contacto_vendedor";
                    soap_manager.Sync_tabla_cliente_contacto_vendedor(codven, "HOY",servidorBD_ERP, nombreBD_ERP,	usuarioBD_ERP, contrasenaBD_ERP, 0,1000);

                    publishProgress("50");
                    NombreMetodo="("+valor+")Sync_tabla_transporte";
                    soap_manager.Sync_tabla_transporte(codven,"HOY",servidorBD_ERP, nombreBD_ERP,	usuarioBD_ERP, contrasenaBD_ERP, 0,1000);

                    publishProgress("80");
                    NombreMetodo="("+valor+")Sync_tabla_lugarEntrega";
                    soap_manager.Sync_tabla_lugarEntrega(codven,"HOY",servidorBD_ERP, nombreBD_ERP,	usuarioBD_ERP, contrasenaBD_ERP, 0,1000);
                }

            } catch (Exception e) {
                error = "1";
                NombreMetodo+=" Error => "+e.getMessage();
                e.printStackTrace();
                Log.i("obtener_hora_servbd",	"No se pudo obtener la hora del servidor");

            }


            String fecha = GlobalFunctions.getFecha_configuracion(activity);
            String resultado = "";

            if (error.equals("0")) {
                resultado = "OK";
                NombreMetodo="";
            } else {
                resultado = "ERROR";
            }
            try {
                //NO existe la tabla log_sincronizacion en el ERP
                soap_manager.actualizarLogSincro(codven, datetime, fecha,   "Lista cliente "+resultado+" = "+NombreMetodo);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                publishProgress("100");
            }



        }else{
            error = "2";
        }


        return error;
    }
    protected void onProgressUpdate(String... progress) {
        Log.d("SINCRONIZAR ACTIVITY", progress[0]);
        pDialog.setProgress(Integer.parseInt(progress[0]));
    }

    @Override
    protected void onPostExecute(String result) {

        pDialog.dismiss();// ocultamos progess dialog.
        Log.e("onPostExecute=", "" + result);

        if (result.equals("0")) {

            if(cantCliNew ==0){
                GlobalFunctions.showCustomToast(activity, "No se encontró clientes nuevos", GlobalFunctions.TOAST_WARNING);
            }
            else GlobalFunctions.showCustomToast(activity, "Sincronización correcta", GlobalFunctions.TOAST_DONE);

            if(myCallback!=null)myCallback.result(true);
            //new cargarClientes().execute();

        }else if (result.equals("1")) {
            AlertDialog.Builder alerta = new AlertDialog.Builder(activity);
            alerta.setMessage("Algunas tablas no se sincronizaroncorrectamente\nSincronice nuevamente");
            alerta.setIcon(R.drawable.check);
            alerta.setCancelable(false);
            alerta.setPositiveButton("OK", null);
            alerta.show();
            if(myCallback!=null)myCallback.result(false);
        }
        else if (result.equals("2")) {
            AlertDialog.Builder alerta = new AlertDialog.Builder(activity);
            alerta.setMessage("Sin conexion al Servidor:\n" + GlobalVar.urlService);
            alerta.setIcon(R.drawable.check);
            alerta.setCancelable(false);
            alerta.setPositiveButton("OK", null);
            alerta.show();
            if(myCallback!=null)myCallback.result(false);
        }

        super.onPostExecute(result);
    }

}
