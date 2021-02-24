package com.example.fuerzaventaschema.genesys.fuerza_ventas.Reportes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.example.fuerzaventaschema.R;
import com.example.fuerzaventaschema.genesys.adapters.ReportesCobranzaAdapter;
import com.example.fuerzaventaschema.genesys.datatypes.DBIngresos;
import com.example.fuerzaventaschema.genesys.datatypes.DBSync_soap_manager;
import com.example.fuerzaventaschema.genesys.datatypes.DBclasses;
import com.example.fuerzaventaschema.genesys.fuerza_ventas.AmortizarCuentasXCobrarActivity2;
import com.example.fuerzaventaschema.genesys.service.ConnectionDetector;
import com.example.fuerzaventaschema.genesys.util.GlobalFunctions;
import com.example.fuerzaventaschema.genesys.util.GlobalVar;
import com.google.gson.JsonParseException;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioGroup.OnCheckedChangeListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import me.piruin.quickaction.ActionItem;
import me.piruin.quickaction.QuickAction;


public class ReportesCobranzaActivity extends FragmentActivity {

    DBclasses obj_dbclasses;
    public static final String KEY_TOTAL = "total";
    public static final String KEY_CLIENTE="cliente";
    public static final String KEY_SECUENCIA="secuencia";
    public static final String KEY_CANTIDAD="cantidad";
    public static final String KEY_ESTADO="estado";
    public static final String KEY_MENSAJE = "mensaje";
    public static final String KEY_SERIE="serie";
    String busqueda="";
    public String numfactura;
    public String saldo="0";
    String total="0";
    String sec_cta_ingresos;
    String codcli;
    String nomcli;
    String acuenta_total, acuenta;
    ArrayList<HashMap<String, String>> alhm_cobranza=new ArrayList<HashMap<String,String>>();
    ListView list;
    ReportesCobranzaAdapter adapter_cobranza;

    private static final int ID_DELETE = 0;
    private static final int ID_MODIFICAR = 1;
    private static final int ID_DETALLE = 2;
    private static final int ID_ANULAR = 3;

    private DecimalFormat formateador;
    private ConnectionDetector cd;

    public String oc_numero="";
    public String cond_pago="";

    int mSelectedRow = 0;
    public double total_monto=0, total_dolares;
    String secuencia, secitm;
    TextView edt_monto3, edt_monto_dolares;
    public String globalFlag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportes_cobranza);


        obj_dbclasses= new DBclasses(getApplicationContext());

        cd = new ConnectionDetector(getApplicationContext());

        edt_monto3= (TextView)findViewById(R.id.rpt_cobranza_txtTotal);
        edt_monto_dolares= (TextView)findViewById(R.id.rpt_cobranza_txtTotal_dolares);

        formateador = GlobalFunctions.formateador();

        //cargarCobranza("","");
        new asyncBuscar().execute("", "");

        edt_monto_dolares.setText("Total: $. "+GlobalFunctions.redondear(total_dolares));
        edt_monto3.setText("Total : S/. "+GlobalFunctions.redondear(total_monto));
        list=(ListView)findViewById(R.id.lv_reportes_cobranza);
        //  adapter_cobranza=new ReportesCobranzaAdapter(this, alhm_cobranza);

        cargarListView();


        //ActionItem addItem 		= new ActionItem(ID_MODIFICAR, "Modificar", getResources().getDrawable(R.drawable.modificar2));
        ActionItem acceptItem 	= new ActionItem(ID_DETALLE, "Ver Detalle", R.drawable.ver2);
        ActionItem deleteItem 	= new ActionItem(ID_DELETE, "Eliminar", R.drawable.ic_eliminar);
        ActionItem anularItem	= new ActionItem(ID_ANULAR, "Anular", R.drawable.cancel);

        final QuickAction mQuickAction 	= new QuickAction(this);

        //mQuickAction.addActionItem(addItem);
        //mQuickAction.addActionItem(acceptItem);
        //mQuickAction.addActionItem(deleteItem);
        mQuickAction.addActionItem(anularItem);


        //setup the action item click listener
        mQuickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {


            @Override
            public void onItemClick(ActionItem item) {
                int actionId=item.getActionId();

                if (actionId == ID_MODIFICAR) { // Mostrar Activity No Venta
                    async_getFlagIngreso con= new async_getFlagIngreso();
                    con.execute(secuencia,"MODIFICAR",secitm);

                    //Toast.makeText(getApplicationContext(), "Modificar",Toast.LENGTH_LONG ).show();

                }
                else if (actionId == ID_DELETE) {
                    async_getFlagIngreso con= new async_getFlagIngreso();
                    con.execute(secuencia,"ELIMINAR",secitm);

                }
                else if(actionId == ID_ANULAR){
                    async_getFlagIngreso con= new async_getFlagIngreso();
                    con.execute(secuencia,"ANULAR",secitm);
                }
                else if(actionId == ID_DETALLE) {
                    Intent i=new Intent(getApplicationContext(), ReportesCobranzaDetalle.class);
                    i.putExtra("SECUENCIA",secuencia);
                    i.putExtra("SECITM",secitm);

                    startActivity(i);
                    //	Toast.makeText(getApplicationContext(), "Ver detalle", Toast.LENGTH_LONG).show();

                }
            }
        });



        list.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSelectedRow = position; //set the selected row


                String secuen= ((TextView) view.findViewById(R.id.rpt_cobranza_tv_secuencia)).getText().toString();
                String item= ((TextView) view.findViewById(R.id.rpt_cobranza_tv_cantidad)).getText().toString();
                String acu= ((TextView) view.findViewById(R.id.rpt_cobranza_tv_total2)).getText().toString();

                secuencia = secuen;
                secitm = item;
                acuenta=acu;

                if(!obj_dbclasses.obtenerEstadoIngreso(secuencia, secitm).equals("L")){
                    mQuickAction.show(view);
                }
            }
        });
    }

    private void cargarListView() {
        // TODO Auto-generated method stub

        LayoutInflater inflater = LayoutInflater.from(this);
        View emptyView = inflater.inflate(R.layout.list_empty_view_unused, null);
        emptyView.setVisibility(View.GONE);
        ((ViewGroup) list.getParent()).addView(emptyView);
        list.setEmptyView(emptyView);
        adapter_cobranza=new ReportesCobranzaAdapter(this, alhm_cobranza);

        list.setAdapter(adapter_cobranza);

        if(alhm_cobranza.isEmpty()){
            edt_monto3.setVisibility(View.GONE);
            edt_monto_dolares.setVisibility(View.GONE);
        }
        else{
            ((ViewGroup) list.getParent()).removeView(emptyView);
            emptyView.setVisibility(View.GONE);
            edt_monto3.setVisibility(View.VISIBLE);
            edt_monto_dolares.setVisibility(View.VISIBLE);
            edt_monto3.setText("Total: S/."+GlobalFunctions.redondear(total_monto));
            edt_monto_dolares.setText("Total: $. "+GlobalFunctions.redondear(total_dolares));
        }

    }

    private void cargarCobranza(String bus, String valor) {
        // TODO Auto-generated method stub
        ArrayList<DBIngresos>  lista_ctaingresos= new ArrayList<DBIngresos>();
        if(bus.equalsIgnoreCase("cliente")){
            lista_ctaingresos= obj_dbclasses.getIngresosXCliente(valor);
        }
        else if(bus.equalsIgnoreCase("factura")){
            lista_ctaingresos= obj_dbclasses.getIngresosXFactura(valor);
        }
        else{
            lista_ctaingresos= obj_dbclasses.getIngresos_reportes();
        }

        total_monto=0;
        total_dolares=0;
        Iterator<DBIngresos> it=lista_ctaingresos.iterator();

        while (it.hasNext()) {

            Object objeto = it.next();
            DBIngresos cta = (DBIngresos) objeto;

            HashMap<String, String> map = new HashMap<String, String>();

            String nombre = obj_dbclasses.obtenerNomcliXCodigo(cta.getCodcli());//obj_dbclasses .getCodcliIngresos("" + cta.getSecuencia()));
            Log.d("Item","nombre:"+nombre+" es > 5");

            if (nombre.length() > 5) {
                map.put(KEY_CLIENTE, "" +nombre); //obj_dbclasses .obtenerNomcliXCodigo(obj_dbclasses .getCodcliIngresos("" + cta.getSecuencia())));
                map.put(KEY_CANTIDAD, "" + cta.getSecitm());
                map.put(KEY_SERIE, "Doc: " + cta.getSecuencia());

                if (cta.getCodmon().equals("02")) {
                    map.put(KEY_TOTAL, "S/." + formateador.format(Double.parseDouble(cta.getAcuenta())));
                    total_monto = total_monto + Double.parseDouble(cta.getAcuenta());
                } else {
                    map.put(KEY_TOTAL,"$. "+ formateador.format(Double.parseDouble(cta.getAcuenta())));
                    total_dolares = total_dolares + Double.parseDouble(cta.getAcuenta());
                }

                map.put(KEY_SECUENCIA, "" + cta.getSecuencia());
                map.put("flag", "" + cta.getFlag());

                map.put(KEY_MENSAJE, cta.getMensaje());

                map.put(KEY_ESTADO, cta.getEstado());

                map.put("factura", obj_dbclasses.getFacturaXSecuencia("" + cta.getSecuencia()));
                map.put("tipo_documento", obj_dbclasses .getTipodocumentoCtaIngresos(cta.getSecuencia()));

                alhm_cobranza.add(map);
            }
        }
    }


    public void poputMenu(View view){
        final PopupMenu popupMenu = new PopupMenu(ReportesCobranzaActivity.this, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_reportespedidos, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_reportespedidos:

                        new asyncEnviarPendientes().execute("");
                        return true;

                    case R.id.menu_reportespedidos_buscar:

                        showCustomDialog();
                        return true;

                    case R.id.menu_reportespedidos_verificar:

                        new asyncVerificarCobranza().execute("");
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();

    }

    class asyncVerificarCobranza extends AsyncTask< String, String, String > {

        String user,pass;
        ProgressDialog pDialog;
        protected void onPreExecute() {
            //para el progress dialog
            pDialog = new ProgressDialog(ReportesCobranzaActivity.this);
            pDialog.setMessage("Verificando con Servidor....\n\n"+ GlobalVar.urlService);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... params) {
            //obtnemos usr y passf
            String mensaje="";
            DBSync_soap_manager soap_manager = new DBSync_soap_manager(getApplicationContext());

            ConnectionDetector connection= new ConnectionDetector(getApplicationContext());

            if(connection.hasActiveInternetConnection(getApplicationContext())){

                try{
                    soap_manager.verificarCobranzaEnviados();
                    mensaje = "Verificacion completa";
                }
                catch(Exception e){
                    mensaje = "Error en la verificacion";
                }

            }
            else
            {
                mensaje = "Sin conexion al Servidor";
            }

            alhm_cobranza.clear();
            cargarCobranza("","");

            return mensaje;
        }

        /*Una vez terminado doInBackground segun lo que halla ocurrido
        pasamos a la sig. activity
        o mostramos error*/
        protected void onPostExecute(String mensaje) {


            Toast toast = Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

            pDialog.dismiss();//ocultamos progess dialog.
            Log.e("onPostExecute= Enviado",""+mensaje);

            ((ReportesCobranzaAdapter)list.getAdapter()).notifyDataSetChanged();

        }

    }

    class asyncEnviarPendientes extends AsyncTask< String, String, String > {

        String user,pass;
        ProgressDialog pDialog;
        protected void onPreExecute() {
            //para el progress dialog
            pDialog = new ProgressDialog(ReportesCobranzaActivity.this);
            pDialog.setMessage("Enviando pendientes....\n\n"+GlobalVar.urlService);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... params) {

            String mensaje="";
            DBSync_soap_manager soap_manager = new DBSync_soap_manager(getApplicationContext());

            ConnectionDetector connection= new ConnectionDetector(getApplicationContext());
            if(connection.hasActiveInternetConnection(getApplicationContext())){

                int respuesta;

                try {
                    respuesta = soap_manager.actualizarIngresos();

                    if(respuesta == 0){
                        mensaje = "No hay amortizaciones pendientes";
                    }
                    else if(respuesta == 1){
                        mensaje = "Envio completo";
                    }

                }
                catch(JsonParseException e){
                    mensaje = "No se pudo verificar";
                }
                catch (Exception e) {
                    mensaje = "Error al enviar";
                }

            }
            else
            {
                mensaje = "Sin conexion al Servidor";
            }

            alhm_cobranza.clear();
            cargarCobranza("","");

            return mensaje;

        }

        /*Una vez terminado doInBackground segun lo que halla ocurrido
        pasamos a la sig. activity
        o mostramos error*/
        protected void onPostExecute(String mensaje) {

            Toast toast = Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

            pDialog.dismiss();//ocultamos progess dialog.
            Log.e("onPostExecute= Enviado",""+mensaje);
            ((ReportesCobranzaAdapter)list.getAdapter()).notifyDataSetChanged();

        }

    }


    class async_getFlagIngreso extends AsyncTask<String,String,String>{
        ProgressDialog pDialog;
        String tipo="";
        String secu="";
        String sec_item="";
        @Override
        protected void onPreExecute() {
            //para el progress dialog

            pDialog = new ProgressDialog(ReportesCobranzaActivity.this);
            pDialog.setMessage("Verificando...");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            secu = params[0].toString();
            tipo = params[1].toString();
            sec_item = params[2].toString();

            String ret = "";

            DBSync_soap_manager sm = new DBSync_soap_manager(ReportesCobranzaActivity.this);

            if(cd.hasActiveInternetConnection(getApplicationContext())){

                try{
                    ret = sm.getFlagIngresos(secu, sec_item);
                }
                catch(Exception e){
                    e.printStackTrace();
                    //Error en el envio
                    ret = "error_2";
                }

            }
            else{
                //Sin conexion al Servidor
                ret = "error_1";
            }


            return ret;
        }


        @Override
        protected void onPostExecute(String result) {


            pDialog.dismiss();//ocultamos progess dialog.

            globalFlag = result;


            if(result.equals("error_1")){

                //Sin conexion al Servidor
                //No se pudo verificar asi que no se continua con la accion seleccionada
                AlertDialog.Builder alerta = new AlertDialog.Builder(ReportesCobranzaActivity.this);
                alerta.setTitle("Sin conexion al servidor");
                alerta.setMessage("Por el momento no es posible completar la accion");
                alerta.setCancelable(false);
                alerta.setPositiveButton("OK", null);
                alerta.show();

            }

            else if(result.equals("error_2")){

                //Error en el envio
                //No se pudo verificar asi que no se continua con la accion seleccionada
                AlertDialog.Builder alerta = new AlertDialog.Builder(ReportesCobranzaActivity.this);
                alerta.setMessage("Por el momento no es posible completar la accion");
                alerta.setCancelable(false);
                alerta.setPositiveButton("OK", null);
                alerta.show();

            }

            else{

                if(tipo.equals("MODIFICAR")){

                    if(result.equals("T")){
                        //la amortizacion ya ha sido transferida y no se puede modificar
                        AlertDialog.Builder alerta = new AlertDialog.Builder(ReportesCobranzaActivity.this);
                        alerta.setTitle("MODIFICAR");
                        alerta.setMessage("Esta amorizacion ya ha sido tranferido\nComuniquese con el administrador");
                        alerta.setCancelable(false);
                        alerta.setPositiveButton("OK", null);
                        alerta.show();
                    }
                    else if(!result.equals("T")){
                        //Se procede a modificar
                        finish();
                        Intent i=new Intent(getApplicationContext(), AmortizarCuentasXCobrarActivity2.class);
                        i.putExtra("NUMFACTURA",numfactura);
                        i.putExtra("SECUENCIA", secuencia);
                        i.putExtra("SECITM", secitm);
                        i.putExtra("TOTAL", total);
                        i.putExtra("SALDO", saldo);
                        i.putExtra("CODCLI", codcli);
                        i.putExtra("NOMCLI", nomcli);
                        i.putExtra("ACUENTA_TOTAL", acuenta.substring(3));
                        i.putExtra("ORIGEN", "REPORTES");

                        startActivity(i);
                    }

                }
                else if(tipo.equals("ELIMINAR")){

                    if(result.equals("T")){
                        //la amortizacion ya ha sido transferida y no se puede modificar
                        AlertDialog.Builder alerta = new AlertDialog.Builder(ReportesCobranzaActivity.this);
                        alerta.setTitle("ELIMINAR");
                        alerta.setMessage("Esta amorizacion ya ha sido tranferido\nComuniquese con el administrador");
                        alerta.setCancelable(false);
                        alerta.setPositiveButton("OK", null);
                        alerta.show();
                    }
                    else if(result.equals("0")){
                        ////No tiene un flag en el servidor, es decir no existe en el servidor
                        AlertDialog.Builder alerta = new AlertDialog.Builder(ReportesCobranzaActivity.this);
                        alerta.setTitle("ELIMINAR");
                        alerta.setMessage("La amortizacion no se encuentra en el servidor\nPor favor verifiquela");
                        alerta.setCancelable(false);
                        alerta.setPositiveButton("OK", null);
                        alerta.show();
                    }
                    else if(!result.equals("T")){
                        //Se procede a eliminar
                        cargarDialogoEliminar();
                    }

                }
                else if(tipo.equals("ANULAR")){

                    if(result.equals("T")){
                        //la amortizacion ya ha sido transferida y no se puede Anular
                        AlertDialog.Builder alerta = new AlertDialog.Builder(ReportesCobranzaActivity.this);
                        alerta.setTitle("ELIMINAR");
                        alerta.setMessage("Esta amorizacion ya ha sido tranferido\nComuniquese con el administrador");
                        alerta.setCancelable(false);
                        alerta.setPositiveButton("OK", null);
                        alerta.show();
                    }
                    else if(!result.equals("T")){
                        //Se procede con la Anulacion
                        cargarDialogoAnulacion();
                    }

                }

            }

        }

    }

    public void cargarDialogoEliminar(){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ReportesCobranzaActivity.this);
        alertDialog.setTitle("Eliminar");
        alertDialog.setMessage("Se eliminar� permanentemente");
        alertDialog.setIcon(R.drawable.ic_eliminar);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                asyncEliminarIngresos con= new asyncEliminarIngresos();
                con.execute(secuencia,"MODIFICAR",secitm);

            }
        });
        alertDialog. setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {

            }
        });
        alertDialog.show();
    }


    class asyncEliminarIngresos extends AsyncTask< String, String, String > {

        String user,pass;
        ProgressDialog pDialog;
        protected void onPreExecute() {
            //para el progress dialog
            pDialog = new ProgressDialog(ReportesCobranzaActivity.this);
            pDialog.setMessage("ELiminando....");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... params) {
            //obtnemos usr y pass
            String valor="";
            DBSync_soap_manager soap_manager = new DBSync_soap_manager(getApplicationContext());

            if(cd.hasActiveInternetConnection(getApplicationContext())){

                try {

                    valor=soap_manager.eliminarIngresoxSecuencia(secuencia, secitm);

                } catch (Exception e) {
                    //Cualquier otro error al enviar
                    e.printStackTrace();
                    valor = "error_2";
                }

            }
            else
            {
                valor="error_1";
            }


            return valor;

        }

        /*Una vez terminado doInBackground segun lo que halla ocurrido
        pasamos a la sig. activity
        o mostramos error*/
        protected void onPostExecute(String result) {


            pDialog.dismiss();//ocultamos progess dialog.

            if(result.equals("error_1")){
                Toast.makeText(getApplicationContext(), "Sin conexion al Servidor", Toast.LENGTH_LONG).show();
            }
            else if(result.equals("error_2")){
                Toast.makeText(getApplicationContext(), "Error al enviar datos", Toast.LENGTH_LONG).show();
            }
            else if(result.equals("1")){
                obj_dbclasses.eliminar_Ingreso(secuencia, secitm);
                Toast.makeText(getApplicationContext(), "Se elimino correctamente", Toast.LENGTH_LONG).show();
            }
            else if(result.equals("0")){
                Toast.makeText(getApplicationContext(), "No se pudo eliminar", Toast.LENGTH_LONG).show();
            }

            alhm_cobranza.clear();
            cargarCobranza("","");

            edt_monto3.setText("Total: S/."+GlobalFunctions.redondear(total_monto));

            ((ReportesCobranzaAdapter)list.getAdapter()).notifyDataSetChanged();

            Log.e("onPostExecute= Enviado",""+result);

        }

    }


    class asyncBuscar extends AsyncTask< String, String, String > {

        String user,pass;
        ProgressDialog pDialog;
        protected void onPreExecute() {
            //para el progress dialog
            pDialog = new ProgressDialog(ReportesCobranzaActivity.this);
            pDialog.setMessage("Buscando....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... params) {
            //obtnemos usr y pass
            String valor = params[0].toString();
            String valor2 = params[1].toString();
            try {
                alhm_cobranza.clear();
                cargarCobranza(valor, valor2);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return "E";




        }

        protected void onPostExecute(String result) {
            ((ReportesCobranzaAdapter)list.getAdapter()).notifyDataSetChanged();
            cargarListView();
            pDialog.dismiss();//ocultamos progess dialog.
            Log.e("onPostExecute= Enviado",""+result);

        }

    }

    Dialog bdialog;

    EditText  edt_buscar;
    Button btn_buscar,btn_cancelar;
    String  tipofiltro="cliente";
    protected void showCustomDialog() {


        bdialog = new Dialog(ReportesCobranzaActivity.this,android.R.style.Theme_Translucent);
        bdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        bdialog.setCancelable(true);
        bdialog.setContentView(R.layout.dialog_buscar);

        RadioGroup rb_filtro;

        edt_buscar = (EditText) bdialog.findViewById(R.id.reportes_edt_search);
        btn_buscar = (Button) bdialog.findViewById(R.id.reportes_btn_buscar);
        btn_cancelar = (Button) bdialog.findViewById(R.id.reportes_btn_cancelar);
        rb_filtro = (RadioGroup) bdialog.findViewById(R.id.reportes_rg_tipoFiltro);
        btn_buscar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new asyncBuscar().execute(tipofiltro, edt_buscar.getText().toString());
            }
        });
        btn_cancelar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                bdialog.dismiss();
            }
        });

        bdialog.show();

        rb_filtro.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                // TODO Auto-generated method stub

                switch (arg1) {
                    case R.id.reportes_rb_Cliente:
                        tipofiltro="cliente";
                        edt_buscar.setText("");
                        edt_buscar.setInputType(InputType.TYPE_CLASS_TEXT);
                        break;
                    case R.id.reportes_rb_Documento:
                        edt_buscar.setText("");
                        tipofiltro="factura";
                        edt_buscar.setInputType(InputType.TYPE_CLASS_NUMBER);

                    default:
                        break;
                }
            }
        });
    }

    public void cargarDialogoAnulacion(){

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ReportesCobranzaActivity.this);
        alertDialog.setTitle("Anulacion");
        alertDialog.setMessage("Desea ANULAR esta amortizacion?");
        alertDialog.setIcon(R.drawable.cancel);

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                obj_dbclasses.updateIngresos(secuencia, secitm, "P");

                AlertDialog.Builder alerta = new AlertDialog.Builder(ReportesCobranzaActivity.this);
                alerta.setTitle("Importante");
                alerta.setIcon(R.drawable.ic_alert);
                alerta.setMessage("Se guardaran los datos");
                alerta.setCancelable(false);
                alerta.setPositiveButton("Enviar al servidor", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        obj_dbclasses.cambiar_estado_ingreso(secuencia, secitm, "A");

                        asyncAnularIngresos con = new asyncAnularIngresos();
                        con.execute("");

                    }
                });

                alerta.setNegativeButton("Guardar Localmente", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        obj_dbclasses.cambiar_estado_ingreso(secuencia, secitm, "A");

                        alhm_cobranza.clear();
                        cargarCobranza("","");

                        ((ReportesCobranzaAdapter)list.getAdapter()).notifyDataSetChanged();

                    }
                });

                alerta.show();

            }
        });
        alertDialog. setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {


            }
        });
        alertDialog.show();
    }


    class asyncAnularIngresos extends AsyncTask< String, String, String > {

        String user,pass;
        ProgressDialog pDialog;
        protected void onPreExecute() {
            //para el progress dialog
            pDialog = new ProgressDialog(ReportesCobranzaActivity.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Anulando....");
            pDialog.setIndeterminate(false);

            pDialog.show();
        }

        protected String doInBackground(String... params) {

            String valor = "ok";

            DBSync_soap_manager soap_manager = new DBSync_soap_manager(getApplicationContext());
            ConnectionDetector cd = new ConnectionDetector(getApplicationContext());

            if(cd.hasActiveInternetConnection(getApplicationContext())){

                try {
                    soap_manager.actualizarIngresos2(obj_dbclasses.getIngresos2(secuencia, secitm));
                }
                catch(JsonParseException e){
                    //error al parsear respuesta json
                    valor = "error_2";
                }
                catch (Exception e) {
                    //cualquier otra excepcion al enviar datos
                    valor = "error_3";
                }

            }
            else{
                //Sin conexion al Servidor
                valor = "error_1";

            }

            return valor;
        }

        /*Una vez terminado doInBackground segun lo que halla ocurrido
        pasamos a la sig. activity
        o mostramos error*/
        protected void onPostExecute(String result) {

            pDialog.dismiss();//ocultamos progess dialog.
            Log.e("onPostExecute= Ingresos",""+result);

            String mensaje = "";

            if(result.equals("ok")){
                mensaje = "Envio Correcto";
            }
            else if(result.equals("error_1")){
                mensaje = "Sin conexion al Servidor";
            }
            else if(result.equals("error_2")){
                mensaje = "Error en el envio";
            }
            else if(result.equals("error_3")){
                mensaje = "No se pudo verificar";
            }

            Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_LONG).show();

            alhm_cobranza.clear();
            cargarCobranza("","");

            ((ReportesCobranzaAdapter)list.getAdapter()).notifyDataSetChanged();

        }

    }


}
