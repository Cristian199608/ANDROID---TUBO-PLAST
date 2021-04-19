package com.example.sm_tubo_plast.genesys.fuerza_ventas.Reportes;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.datatypes.DBDepositos;
import com.example.sm_tubo_plast.genesys.datatypes.DBSync_soap_manager;
import com.example.sm_tubo_plast.genesys.datatypes.DBclasses;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.DepositosModificarActivity;
import com.example.sm_tubo_plast.genesys.service.ConnectionDetector;
import com.example.sm_tubo_plast.genesys.util.GlobalFunctions;
import com.example.sm_tubo_plast.genesys.util.GlobalVar;
import com.google.gson.JsonParseException;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import me.piruin.quickaction.ActionItem;
import me.piruin.quickaction.QuickAction;

public class ReportesDepositoActivity extends FragmentActivity {

    DBclasses obj_dbclasses;
    public static final String KEY_TOTAL = "total";
    public static final String KEY_NOM_BANCO = "nomban";
    public static final String KEY_COD = "codigo";
    public static final String KEY_NUM_OPE = "numope";
    public static final String KEY_ESTADO = "estado";
    public static final String KEY_SECUENCIA = "secuencia";
    String secuencia = "";
    public String numfactura;
    public String saldo = "0";
    String total = "0";
    String sec_cta_ingresos;
    String codcli;
    LayoutInflater inflater;
    String nomcli;
    String acuenta_total;
    ArrayList<HashMap<String, String>> alhm_depositos = new ArrayList<HashMap<String, String>>();
    ListView list_depositos;
    // ReportesDepositosAdapter adapter_depositos;
    ReportesDepositos_Adapter adapter;
    private static final int ID_MODIFICAR = 1;
    private static final int ID_DETALLE = 2;
    private static final int ID_DELETE = 0;

    ConnectionDetector connection;

    public String oc_numero = "";
    public String cond_pago = "";

    private int mSelectedRow = 0;
    public double total_monto, total_dolares;
    ArrayList<DBDepositos> lista_depositos;
    TextView edt_monto, edt_montoDolares;
    public String globalFlag;

    private DecimalFormat formateador;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportes_deposito);

        obj_dbclasses = new DBclasses(getApplicationContext());

        formateador = GlobalFunctions.formateador();

        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        cargarDepositos();

        connection = new ConnectionDetector(getApplicationContext());

        edt_monto = (TextView) findViewById(R.id.rpt_depositos_txtTotal);
        edt_montoDolares = (TextView) findViewById(R.id.rpt_depositos_txtTotal_dolares);
        list_depositos = (ListView) findViewById(R.id.lv_reportes_depositos);

        edt_monto.setText("Total: S/. "
                + GlobalFunctions.redondear(total_monto));
        edt_montoDolares.setText("Total: $. "
                + GlobalFunctions.redondear(total_dolares));

        cargarListView();

        ActionItem addItem = new ActionItem(ID_MODIFICAR, "Modificar",R.drawable.modificar2);
        ActionItem acceptItem = new ActionItem(ID_DETALLE, "Ver Detalle",R.drawable.ver2);
        ActionItem deleteItem = new ActionItem(ID_DELETE, "Eliminar",R.drawable.ic_eliminar);

        final QuickAction mQuickAction = new QuickAction(this);

        mQuickAction.addActionItem(addItem);
        // mQuickAction.addActionItem(acceptItem);
        mQuickAction.addActionItem(deleteItem);

        // setup the action item click listener
        mQuickAction
                .setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
                    @Override
                    public void onItemClick(ActionItem item) {
                        int actionId=item.getActionId();

                        if (actionId == ID_MODIFICAR) {
                            async_getFlagDeposito con = new async_getFlagDeposito();
                            con.execute(secuencia, "MODIFICAR");

                        } else if (actionId == ID_DELETE) {
                            async_getFlagDeposito con = new async_getFlagDeposito();
                            con.execute(secuencia, "ELIMINAR");
                        } else {
                            /*
                             * Intent i=new Intent(getApplicationContext(),
                             * ReportesDetallePedidoActivity.class);
                             * i.putExtra("COD_PEDIDO_CABECERA",oc_numero);
                             * i.putExtra("COND_PAGO",cond_pago);
                             *
                             * startActivity(i);
                             */
                            Toast.makeText(getApplicationContext(),
                                    "Ver detalle", Toast.LENGTH_LONG).show();

                        }
                    }
                });


        list_depositos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                mSelectedRow = position; // set the selected row

                mQuickAction.show(view);
                String sec = ((TextView) view
                        .findViewById(R.id.rpt_deposito_tv_coddeposito))
                        .getText().toString();
                // String cond= ((TextView)
                // view.findViewById(R.id.rpt_pedido_tv_tipopago)).getText().toString();
                // cond_pago = cond;
                // oc_numero=numoc;
                // change the right arrow icon to selected state
                secuencia = sec;
            }
        });
    }

    private void cargarListView() {
        // TODO Auto-generated method stub

        LayoutInflater inflater = LayoutInflater.from(this);
        View emptyView = inflater
                .inflate(R.layout.list_empty_view_unused, null);
        emptyView.setVisibility(View.GONE);
        ((ViewGroup) list_depositos.getParent()).addView(emptyView);
        list_depositos.setEmptyView(emptyView);

        adapter = new ReportesDepositos_Adapter(getApplicationContext(),
                R.layout.main_clientes, alhm_depositos);

        list_depositos.setAdapter(adapter);

        if (alhm_depositos.isEmpty()) {
            edt_monto.setVisibility(View.GONE);
            edt_montoDolares.setVisibility(View.GONE);
        }

    }


    public void poputMenu(View view){
        final PopupMenu popupMenu = new PopupMenu(ReportesDepositoActivity.this, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_reportespedidos, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_reportespedidos:

                        new asyncEnviarPendientes().execute("");
                        return true;

                    case R.id.menu_reportespedidos_verificar:

                        new asyncVerificarDepositos().execute("");
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();

    }

    class asyncEnviarPendientes extends AsyncTask<String, String, String> {

        String user, pass;
        ProgressDialog pDialog;

        protected void onPreExecute() {
            // para el progress dialog
            pDialog = new ProgressDialog(ReportesDepositoActivity.this);
            pDialog.setMessage("Enviando pendientes....\n\n"
                    + GlobalVar.urlService);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... params) {
            // obtnemos usr y pass
            String mensaje = "";
            DBSync_soap_manager soap_manager = new DBSync_soap_manager(
                    getApplicationContext());

            ConnectionDetector connection = new ConnectionDetector(
                    getApplicationContext());

            if (connection.hasActiveInternetConnection(getApplicationContext())) {

                int respuesta;

                try {

                    respuesta = soap_manager.actualizarDepositos();

                    if (respuesta == 0) {
                        mensaje = "No hay depositos pendientes";
                    } else {
                        mensaje = "Envio completo";
                    }

                } catch (JsonParseException e) {
                    mensaje = "No se pudo verificar";
                } catch (Exception e) {
                    mensaje = "Error al enviar";
                }
            } else {
                mensaje = "Sin conexion al Servidor";
            }

            alhm_depositos.clear();
            cargarDepositos();

            return mensaje;
        }

        /*
         * Una vez terminado doInBackground segun lo que halla ocurrido pasamos
         * a la sig. activity o mostramos error
         */
        protected void onPostExecute(String mensaje) {

            Toast toast = Toast.makeText(getApplicationContext(), mensaje,
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

            pDialog.dismiss();// ocultamos progess dialog.
            Log.e("onPostExecute= Enviado", "" + mensaje);

            ((ReportesDepositos_Adapter) list_depositos.getAdapter())
                    .notifyDataSetChanged();

        }

    }

    public class ReportesDepositos_Adapter extends
            ArrayAdapter<HashMap<String, String>> {
        Activity context;
        private ArrayList<HashMap<String, String>> data;

        private ArrayList<HashMap<String, String>> mOriginalValues;

        public ReportesDepositos_Adapter(Context context,
                                         int textViewResourceId,
                                         ArrayList<HashMap<String, String>> Strings) {

            // let android do the initializing :)
            super(context, textViewResourceId, Strings);

        }

        private class ViewHolder {

            TextView nomban, numope, total, cod, estado, secuencia;
            ImageView foto;
        }

        ViewHolder viewHolder;

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {

                convertView = inflater.inflate(R.layout.item_reporte_deposito,
                        null);
                viewHolder = new ViewHolder();

                // cache the views
                viewHolder.nomban = (TextView) convertView
                        .findViewById(R.id.rpt_deposito_tv_nombanco);
                viewHolder.numope = (TextView) convertView
                        .findViewById(R.id.rpt_deposito_tv_numoperacion);
                viewHolder.total = (TextView) convertView
                        .findViewById(R.id.rpt_deposito_tv_total);
                viewHolder.cod = (TextView) convertView
                        .findViewById(R.id.rpt_deposito_tv_coddeposito);
                viewHolder.estado = (TextView) convertView
                        .findViewById(R.id.rpt_deposito_tv_estado);
                viewHolder.secuencia = (TextView) convertView
                        .findViewById(R.id.rpt_deposito_tv_secuencia);
                viewHolder.foto = (ImageView) convertView
                        .findViewById(R.id.rpt_depostio_tv_flecha);

                convertView.setTag(viewHolder);

            } else
                viewHolder = (ViewHolder) convertView.getTag();

            if (alhm_depositos.get(position).get("flag").toString().equals("E")) {
                viewHolder.foto.setBackgroundColor(Color.rgb(46, 178, 0));
            } else if (alhm_depositos.get(position).get("flag").toString()
                    .equals("I")) {
                viewHolder.foto.setBackgroundColor(Color.rgb(255, 255, 0));
            } else if (alhm_depositos.get(position).get("flag").toString()
                    .equals("P")) {
                viewHolder.foto.setBackgroundColor(Color.rgb(255, 30, 30));
                viewHolder.estado.setTextColor(Color.rgb(255, 30, 30));
            } else if (alhm_depositos.get(position).get("flag").toString()
                    .equals("T")) {
                viewHolder.foto.setBackgroundColor(Color.rgb(75, 0, 130));
                viewHolder.estado.setTextColor(Color.rgb(75, 0, 130));
            }

            viewHolder.nomban.setText(alhm_depositos.get(position)
                    .get("nomban").toString());
            viewHolder.numope.setText(alhm_depositos.get(position)
                    .get("numope").toString());
            viewHolder.total.setText(alhm_depositos.get(position).get("total")
                    .toString());
            viewHolder.cod.setText(alhm_depositos.get(position).get("codigo")
                    .toString());
            viewHolder.estado.setText(alhm_depositos.get(position)
                    .get("estado").toString());
            viewHolder.secuencia.setText("Secuencia: "
                    + alhm_depositos.get(position).get("secuencia").toString());
            // return the view to be displayed
            return convertView;
        }

    }

    public void cargarDepositos() {
        lista_depositos = new ArrayList<DBDepositos>();
        lista_depositos.clear();
        total_monto = 0;
        total_dolares = 0;
        lista_depositos = obj_dbclasses.getDepositos();
        Iterator<DBDepositos> it = lista_depositos.iterator();

        while (it.hasNext()) {
            Object objeto = it.next();
            DBDepositos cta = (DBDepositos) objeto;

            HashMap<String, String> map = new HashMap<String, String>();
            // map.put(KEY_CODCLI,obtenerPersona[i].getCodcli());
            map.put(KEY_NOM_BANCO,
                    "Banco: "
                            + obj_dbclasses.obtenerNombreBanco(cta
                            .getId_banco()));
            map.put(KEY_NUM_OPE, "N° operacion: " + cta.getNum_ope());
            map.put(KEY_SECUENCIA, cta.getSecuencia());

            if (cta.getMoneda().toString().equals("02")) {
                map.put(KEY_TOTAL,
                        "S/. "
                                + formateador.format(Double.parseDouble(cta
                                .getMonto())));
                total_monto = total_monto + Double.parseDouble(cta.getMonto());
            } else {
                map.put(KEY_TOTAL,
                        "$. "
                                + formateador.format(Double.parseDouble(cta
                                .getMonto())));
                total_dolares = total_dolares
                        + Double.parseDouble(cta.getMonto());
            }

            map.put(KEY_COD, "" + cta.getSecuencia());
            map.put("flag", "" + cta.getBI_DEPO_FLAG());
            map.put(KEY_ESTADO, cta.getMensaje());

            alhm_depositos.add(map);

        }
    }

    class asyncVerificarDepositos extends AsyncTask<String, String, String> {

        String user, pass;
        ProgressDialog pDialog;

        protected void onPreExecute() {
            // para el progress dialog
            pDialog = new ProgressDialog(ReportesDepositoActivity.this);
            pDialog.setMessage("Verificando con Servidor....\n\n"
                    + GlobalVar.urlService);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... params) {

            String mensaje = "";
            DBSync_soap_manager soap_manager = new DBSync_soap_manager(
                    getApplicationContext());

            if (connection.hasActiveInternetConnection(getApplicationContext())) {

                try {
                    soap_manager.verificarDepositosEnviados();
                } catch (Exception ex) {
                    mensaje = "Error al enviar";
                }

                mensaje = "Verificacion completa";

            } else {
                mensaje = "Sin conexion al Servidor";
            }

            alhm_depositos.clear();
            cargarDepositos();

            return mensaje;
        }

        /*
         * Una vez terminado doInBackground segun lo que halla ocurrido pasamos
         * a la sig. activity o mostramos error
         */
        protected void onPostExecute(String mensaje) {

            Toast toast = Toast.makeText(getApplicationContext(), mensaje,
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

            pDialog.dismiss();// ocultamos progess dialog.
            Log.e("onPostExecute= Enviado", "" + mensaje);

            ((ReportesDepositos_Adapter) list_depositos.getAdapter())
                    .notifyDataSetChanged();

        }

    }

    class async_getFlagDeposito extends AsyncTask<String, String, String> {
        ProgressDialog pDialog;
        String tipo = "";
        String secu = "";

        @Override
        protected void onPreExecute() {
            // para el progress dialog

            pDialog = new ProgressDialog(ReportesDepositoActivity.this);
            pDialog.setMessage("Verificando...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            secu = params[0].toString();
            tipo = params[1].toString();

            String ret = "";

            DBSync_soap_manager sm = new DBSync_soap_manager(
                    ReportesDepositoActivity.this);

            if (connection.hasActiveInternetConnection(getApplicationContext())) {

                try {
                    ret = sm.getFlagDeposito(secuencia);
                } catch (Exception e) {
                    // Error al enviar
                    ret = "error_2";
                }

            } else {
                // Sin conexion al Servidor
                ret = "error_1";
            }

            return ret;
        }

        @Override
        protected void onPostExecute(String result) {

            pDialog.dismiss();// ocultamos progess dialog.

            if (result.equals("error_1")) {

                // Sin conexion al Servidor
                // No se pudo verificar asi que no se continua con la accion
                // seleccionada
                AlertDialog.Builder alerta = new AlertDialog.Builder(
                        ReportesDepositoActivity.this);
                alerta.setTitle("Sin conexion al servidor");
                alerta.setMessage("Por el momento no es posible completar la accion");
                alerta.setCancelable(false);
                alerta.setPositiveButton("OK", null);
                alerta.show();

            }

            else if (result.equals("error_2")) {

                // Error en el envio
                // No se pudo verificar asi que no se continua con la accion
                // seleccionada
                AlertDialog.Builder alerta = new AlertDialog.Builder(
                        ReportesDepositoActivity.this);
                alerta.setMessage("Por el momento no es posible completar la accion");
                alerta.setCancelable(false);
                alerta.setPositiveButton("OK", null);
                alerta.show();

            }

            else {

                if (tipo.equals("MODIFICAR")) {

                    if (result.equals("T")) {
                        // Transferido y no se puede modificar
                        AlertDialog.Builder alerta = new AlertDialog.Builder(
                                ReportesDepositoActivity.this);
                        alerta.setTitle("MODIFICAR");
                        alerta.setMessage("El deposito ya ha sido tranferido\nComuniquese con el administrador");
                        alerta.setCancelable(false);
                        alerta.setPositiveButton("OK", null);
                        alerta.show();
                    } else if (!result.equals("T")) {
                        // Se procede a modificar
                        Intent i = new Intent(getApplicationContext(),
                                DepositosModificarActivity.class);
                        i.putExtra("secuencia", secuencia);
                        startActivity(i);
                        finish();
                    }

                }

                else if (tipo.equals("ELIMINAR")) {

                    if (result.equals("T")) {
                        // Transferido y no se puede modificar
                        AlertDialog.Builder alerta = new AlertDialog.Builder(
                                ReportesDepositoActivity.this);
                        alerta.setTitle("ELIMINAR");
                        alerta.setMessage("El deposito ya ha sido tranferido\nComuniquese con el administrador");
                        alerta.setCancelable(false);
                        alerta.setPositiveButton("OK", null);
                        alerta.show();
                    } else if (result.equals("0")) {
                        // No tiene un flag en el servidor, es decir no existe
                        // en el servidor
                        AlertDialog.Builder alerta = new AlertDialog.Builder(
                                ReportesDepositoActivity.this);
                        alerta.setTitle("ELIMINAR");
                        alerta.setMessage("El deposito no se encuentra en el servidor\nPor favor verifiquelo");
                        alerta.setCancelable(false);
                        alerta.setPositiveButton("OK", null);
                        alerta.show();
                    } else if (!result.equals("T")) {
                        cargarDialogoEliminar();
                    }

                }

            }

        }

    }

    public void cargarDialogoEliminar() {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                ReportesDepositoActivity.this);
        alertDialog.setTitle("Eliminar");
        alertDialog.setMessage("Se eliminará permanentemente");
        alertDialog.setIcon(R.drawable.ic_eliminar);
        alertDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        asyncEliminarDepositos con = new asyncEliminarDepositos();
                        con.execute("");

                    }
                });
        alertDialog.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {

                    }
                });
        alertDialog.show();
    }

    class asyncEliminarDepositos extends AsyncTask<String, String, String> {

        String user, pass;
        ProgressDialog pDialog;

        protected void onPreExecute() {
            // para el progress dialog
            pDialog = new ProgressDialog(ReportesDepositoActivity.this);
            pDialog.setMessage("ELiminando....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... params) {
            // obtnemos usr y pass
            String valor = "";
            DBSync_soap_manager soap_manager = new DBSync_soap_manager(
                    getApplicationContext());

            if (connection.hasActiveInternetConnection(getApplicationContext())) {

                try {

                    valor = soap_manager.eliminarDepositosXSecuencia(secuencia);

                } catch (Exception e) {
                    // Cualquier otro error al enviar
                    e.printStackTrace();
                    valor = "error_2";
                }

            } else {
                valor = "error_1";
            }

            return valor;

        }

        /*
         * Una vez terminado doInBackground segun lo que halla ocurrido pasamos
         * a la sig. activity o mostramos error
         */
        protected void onPostExecute(String result) {

            pDialog.dismiss();// ocultamos progess dialog.

            if (result.equals("error_1")) {
                Toast.makeText(getApplicationContext(),
                        "Sin conexion al Servidor", Toast.LENGTH_LONG).show();
            } else if (result.equals("error_2")) {
                Toast.makeText(getApplicationContext(),
                        "Error al enviar datos", Toast.LENGTH_LONG).show();
            } else if (result.equals("1")) {
                obj_dbclasses.eliminar_depositoXsec(secuencia);
                Toast.makeText(getApplicationContext(),
                        "Se elimino correctamente", Toast.LENGTH_LONG).show();
            } else if (result.equals("0")) {
                Toast.makeText(getApplicationContext(), "No se pudo eliminar",
                        Toast.LENGTH_LONG).show();
            }

            alhm_depositos.clear();
            cargarDepositos();

            edt_monto.setText("Total: S/."
                    + GlobalFunctions.redondear(total_monto));

            ((ReportesDepositos_Adapter) list_depositos.getAdapter())
                    .notifyDataSetChanged();

            Log.e("onPostExecute= Enviado", "" + result);

        }

    }

}
