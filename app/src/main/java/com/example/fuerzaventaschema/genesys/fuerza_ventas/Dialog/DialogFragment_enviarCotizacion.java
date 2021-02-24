package com.example.fuerzaventaschema.genesys.fuerza_ventas.Dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.example.fuerzaventaschema.R;
import com.example.fuerzaventaschema.genesys.datatypes.DBclasses;
import com.example.fuerzaventaschema.genesys.fuerza_ventas.Reportes.ReportesPedidosActivity;
import com.example.fuerzaventaschema.genesys.service.ConnectionDetector;
import com.example.fuerzaventaschema.genesys.util.GlobalFunctions;
import com.example.fuerzaventaschema.genesys.util.GlobalVar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;

public  class DialogFragment_enviarCotizacion extends DialogFragment {
    Activity activity;
    String oc_numero;
    String codigoVendedor;
    String correoVendedor;
    String password;
    String asunto;
    String referencia;
    String[] correoClientes;
    DBclasses obj_dbclasses;


    public DialogFragment_enviarCotizacion(Activity activity, DBclasses obj_dbclasses, String oc_numero,String codigoVendedor) {
        this.activity=activity;
        this.obj_dbclasses = obj_dbclasses;
        this.oc_numero = oc_numero;
        this.codigoVendedor = codigoVendedor;

    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View pop = inflater.inflate(R.layout.dialog_enviar_correo, null);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity(),android.R.style.Theme_Material_Light_Dialog_Alert);
        alertBuilder.setView(pop);
        alertBuilder.setTitle("Enviar Cotización");

        final EditText edt_correoVendedor = (EditText) pop.findViewById(R.id.edt_correoVendedor);
        final EditText edt_password = (EditText) pop.findViewById(R.id.edt_password);
        final EditText edt_correoClientes = (EditText) pop.findViewById(R.id.edt_correoClientes);
        final EditText edt_asunto = (EditText) pop.findViewById(R.id.edt_asunto);
        final EditText edt_referencia = (EditText) pop.findViewById(R.id.edt_referencia);

        Button btn_enviar = (Button) pop.findViewById(R.id.btn_enviar);
        Button btn_cancelar = (Button) pop.findViewById(R.id.btn_cancelar);
        String[] ruta = GlobalVar.urlService.split("/");
        Log.d("TEST","http://"+ruta[0]+"/saemovil/saemoviles/vista/PDF.php");

        String vendedorEmail = obj_dbclasses.getVendedorEmail(codigoVendedor);
        if (!vendedorEmail.equals("")) {
            edt_correoVendedor.setEnabled(false);
            edt_correoVendedor.setText(vendedorEmail);
        } else {
            Toast.makeText(activity, "No tiene una direccion de correo asociada a su usuario", Toast.LENGTH_SHORT).show();
        }

        btn_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correoVendedor = edt_correoVendedor.getText().toString().trim();
                password = edt_password.getText().toString();
                asunto = edt_asunto.getText().toString();
                referencia = edt_referencia.getText().toString();

                String listaCorreos = edt_correoClientes.getText().toString().trim();

                if(!correoVendedor.equals("")){
                    if(!password.equals("")){
                        if (!listaCorreos.equals("")) {
                            correoClientes = listaCorreos.split(";");
                            for (int i = 0; i < correoClientes.length; i++) {
                                correoClientes[i] = correoClientes[i].trim();
                                Log.d("TEST", "Cliente: "+i+" "+correoClientes[i]);
                            }

                            new AsyncTask<Void, Void, String>() {

                                ProgressDialog pDialog;

                                protected void onPreExecute() {
                                    // para el progress dialog
                                    pDialog = new ProgressDialog(activity);
                                    pDialog.setMessage("Enviando cotizacion...");
                                    pDialog.setIndeterminate(true);
                                    pDialog.setCancelable(false);
                                    pDialog.show();
                                }

                                @Override
                                protected String doInBackground(Void... params) {
                                    String respuesta="0";
                                    ConnectionDetector conexion = new ConnectionDetector(activity);
                                    if (conexion.isConnectingToInternet()) {

                                        URL url;
                                        HttpURLConnection urlConnection = null;
                                        try {
                                            //url = new URL("http://192.168.1.10:90/PHPPDF/PDF.php");
                                            String[] ruta = GlobalVar.urlService.split("/");
                                            url = new URL("http://"+ruta[0]+"/saemovil/saemoviles/vista/PDF.php");
                                            urlConnection = (HttpURLConnection) url.openConnection();
                                            urlConnection.setRequestMethod("POST");
                                            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                                            //urlConnection.setDoInput(true);
                                            urlConnection.setDoOutput(true);
                                            //urlConnection.setChunkedStreamingMode(0);

                                            JSONObject json = new JSONObject();
                                            json.put("correoVendedor", correoVendedor);
                                            json.put("password", password);
                                            json.put("asunto", asunto);
                                            json.put("referencia", referencia);
                                            json.put("oc_numero", oc_numero);
                                            JSONArray correosJSON = new JSONArray(Arrays.asList(correoClientes));

                                            json.put("correoClientes", correosJSON.toString());

                                            String urlParameters =
                                                    "correoVendedor=" + URLEncoder.encode(correoVendedor, "UTF-8") +
                                                            "&asunto=" + URLEncoder.encode(asunto, "UTF-8");

                                            //urlConnection.setFixedLengthStreamingMode(urlParameters.getBytes().length);
                                            urlConnection.setFixedLengthStreamingMode(json.toString().getBytes().length);
                                            Log.d("TEST", json.toString());
                                            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
                                            out.write(json.toString().getBytes());
                                            out.flush();
                                            out.close();

                                            //OutputStream os = urlConnection.getOutputStream();
                                            //BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                                            //writer.write(urlParameters);
                                            //writer.flush();
                                            //writer.close();
                                            //os.close();

                                            //Get Response
                                            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                                            BufferedReader rd = new BufferedReader(new InputStreamReader(in));

                                            String line;
                                            StringBuffer response = new StringBuffer();
                                            while((line = rd.readLine()) != null) {
                                                response.append(line);
                                            }

                                            rd.close();
                                            respuesta = response.toString();
                                        } catch (Exception e) {
                                            Log.e("TEST","ERROR:"+e.getMessage());
                                            e.printStackTrace();
                                        } finally {
                                            if(conexion!= null) {
                                                urlConnection.disconnect();
                                            }
                                        }
                                    }else{
                                        Toast.makeText(activity, "No se puede conectar con el servidor", Toast.LENGTH_SHORT).show();
                                    }
                                    return respuesta;
                                }

                                @Override
                                protected void onPostExecute(String result) {
                                    Log.d("TEST", result);

                                    try {
                                        JSONObject responseJSON = new JSONObject(result);

                                        if (result!=null) {
                                            if (responseJSON.get("state").equals("1")) {
                                                GlobalFunctions.showCustomToast(getActivity(), "El correo ha sido enviado", GlobalFunctions.TOAST_DONE);
                                            }else{
                                                GlobalFunctions.showCustomToast(getActivity(), Html.fromHtml(""+responseJSON.get("message")).toString(), GlobalFunctions.TOAST_ERROR);
                                            }
                                        }
                                        //dismiss();

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    pDialog.dismiss();
                                    super.onPostExecute(result);
                                }

                            }.execute();

                        }else{
                            edt_correoClientes.setError(Html.fromHtml("<font color='#424242'>Ingrese destinatarios</font>"));
                        }
                    }else{
                        edt_password.setError(Html.fromHtml("<font color='#424242'>Contraseña necesaria</font>"));
                    }
                }else{
                    edt_correoVendedor.setError(Html.fromHtml("<font color='#424242'>Ingrese su correo</font>"));
                }
                Log.d("TEST", "Vendedor: "+correoVendedor);
            }
        });

        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

            }
        });

        return alertBuilder.create();

    }
}