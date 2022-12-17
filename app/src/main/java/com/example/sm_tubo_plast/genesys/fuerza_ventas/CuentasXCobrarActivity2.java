package com.example.sm_tubo_plast.genesys.fuerza_ventas;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.adapters.Cobranza_LazyAdapter;
import com.example.sm_tubo_plast.genesys.datatypes.DBCta_Ingresos;
import com.example.sm_tubo_plast.genesys.datatypes.DBSync_soap_manager;
import com.example.sm_tubo_plast.genesys.datatypes.DBclasses;
import com.example.sm_tubo_plast.genesys.session.SessionManager;
import com.example.sm_tubo_plast.genesys.util.EditTex.ACG_EditText;
import com.example.sm_tubo_plast.genesys.util.SnackBar.UtilViewSnackBar;
import com.example.sm_tubo_plast.genesys.util.VARIABLES;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.TimeoutException;

import me.piruin.quickaction.ActionItem;
import me.piruin.quickaction.QuickAction;

@SuppressLint("LongLogTag")
public class CuentasXCobrarActivity2 extends AppCompatActivity {
    public static final String TAG = "CuentasXCobrarActivity2";

    public String secuen;
    String codigoVendedor="";

    public static final String KEY_TIPO_DOCUMENTO = "tipo_documento";
    public static final String KEY_NUMERO = "numero_doc";
    public static final String KEY_FECHA_E = "fecha_e";
    public static final String KEY_FECHA_V = "fecha_v";
    public static final String KEY_TOTAL = "total";
    public static final String KEY_SALDO_TOTAL = "saldo_total";
    public static final String KEY_USUARIO = "usuario";

    public String numfactura;
    public String saldo = "0";
    private String total = "0";
    private String saldo_virtual = "0";
    private String codcli, origen,moneda,serie, tipo;
    private String nomcli, formaPago;
    private String acuenta_total, tipo_de_documento;
    ProgressDialog pDialog;
    DBSync_soap_manager soap_manager;
    DBclasses obj_dbclasses;
    ArrayList<HashMap<String, String>> ctas_cobrar;
    ListView list;
    Cobranza_LazyAdapter cta_adapter;

    private static final int ID_PAGAR = 1;
    private static final int ID_DETALLE = 2;
    private SharedPreferences prefs;

    String parametro ="T";

    ArrayList<DBCta_Ingresos> lista_cta_ingresos_original = new ArrayList<DBCta_Ingresos>();
    ArrayList<DBCta_Ingresos> lista_cta_ingresos_result = new ArrayList<DBCta_Ingresos>();

    TextView txtDeudaSoles, txtDeudaDolares, txtTotalDolaresDocumento, txtTotalSolesDocumento;
    TextView txtObligacionDolares, txtTotalObligacionDolaresDocumento, txtObligacionSoles, txtTotalObligacionSolesDocumento;
    EditText inputSearch_documento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuentas_x_cobrar2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //´para que no salga el teclado al iniciar activity
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        inputSearch_documento = (EditText)findViewById(R.id.inputSearch_documento);

        txtDeudaDolares=findViewById(R.id.txtDeudaDolares);
        txtDeudaSoles=findViewById(R.id.txtDeudaSoles);
        txtTotalDolaresDocumento=findViewById(R.id.txtTotalDolaresDocumento);
        txtTotalSolesDocumento=findViewById(R.id.txtTotalSolesDocumento);
        txtObligacionDolares=findViewById(R.id.txtObligacionDolares);
        txtTotalObligacionDolaresDocumento=findViewById(R.id.txtTotalObligacionDolaresDocumento);
        txtObligacionSoles=findViewById(R.id.txtObligacionSoles);
        txtTotalObligacionSolesDocumento=findViewById(R.id.txtTotalObligacionSolesDocumento);

        obj_dbclasses = new DBclasses(getApplicationContext());
        ctas_cobrar = new ArrayList<HashMap<String, String>>();
        list = (ListView) findViewById(R.id.lv_ctas_cobrar);

        Bundle bundle = getIntent().getExtras();
        codcli = "" + bundle.getString("CODCLI");
        nomcli = "" + bundle.getString("NOMCLI");
        origen = "" + bundle.getString("ORIGEN");
        setTitle(nomcli);

        prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        boolean programada = prefs.getString("Programado", "1").equals("1"); // 0 :Inactivo 1:Activado
        boolean libre = prefs.getString("Libre", "1").equals("1"); // 0 :Inactivo 1:Activado

        if(programada && libre)
            parametro ="T";
        else if(programada)
            parametro ="P";
        else if(libre)
            parametro ="L";
        else
            parametro ="T";

        new asyncTask().execute();





        ActionItem addItem = new ActionItem(ID_PAGAR, "Amortizar",(R.drawable.pagar));
		/*ActionItem acceptItem = new ActionItem(ID_DETALLE, "Ver Detalle",
				getResources().getDrawable(R.drawable.detalle));*/

        final QuickAction mQuickAction = new QuickAction(this);

        mQuickAction.addActionItem(addItem);
        //mQuickAction.addActionItem(acceptItem);

        // setup the action item click listener
        mQuickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {


            @Override
            public void onItemClick(ActionItem item) {

                int actionId=item.getActionId();
                if (actionId == ID_PAGAR) { // Mostrar Activity No Venta

                    if (Double.parseDouble(saldo) > 0) {

                        Intent i = new Intent();
                        Log.e(TAG, "forma de Pago: "+formaPago);
                        if (formaPago.equals("COBRAR") || 5==5){
                            i = new Intent(CuentasXCobrarActivity2.this,AmortizarCuentasXCobrarActivity2.class);
                            i.putExtra("SECUENCIA", secuen);
                            i.putExtra("TOTAL", total);
                            i.putExtra("SALDO", saldo);
                            i.putExtra("SALDO_VIRTUAL", saldo_virtual);
                            i.putExtra("CODCLI", codcli);
                            i.putExtra("NOMCLI", nomcli);
                            i.putExtra("ACUENTA_TOTAL", acuenta_total);
                            i.putExtra("ORIGEN", origen);

                            i.putExtra("NUMFACTURA", numfactura);
                            i.putExtra("SERIE", serie);
                            i.putExtra("TIPO", tipo);

                            startActivity(i);
                            //finish();				"RECOGER"
                        }else if ( formaPago.equals("RECOJER") || formaPago.equals("DEJAR") ){
                            //Letra 'Entregar'
                            i = new Intent(CuentasXCobrarActivity2.this,AmortizarCuentasXCobrarActivityLetra.class);
                            i.putExtra("SECUENCIA", secuen);
                            i.putExtra("TOTAL", total);
                            i.putExtra("SALDO", saldo);
                            i.putExtra("SALDO_VIRTUAL", saldo_virtual);
                            i.putExtra("CODCLI", codcli);
                            i.putExtra("NOMCLI", nomcli);
                            i.putExtra("ACUENTA_TOTAL", acuenta_total);
                            i.putExtra("ORIGEN", origen);

                            i.putExtra("NUMFACTURA", numfactura);
                            i.putExtra("SERIE", serie);
                            i.putExtra("TIPO", tipo);

                            startActivity(i);
                            //finish();
                        }else if (formaPago.equals("LIBRE")){
                            Log.d(TAG, "formaPago: Libre   tipoDocumento:"+tipo_de_documento);

                            if(tipo_de_documento.equals("FACTURA") ){
                                Log.d("FA", "Libre");
                                i = new Intent(CuentasXCobrarActivity2.this,AmortizarCuentasXCobrarActivity2.class);
                                i.putExtra("SECUENCIA", secuen);
                                i.putExtra("TOTAL", total);
                                i.putExtra("SALDO", saldo);
                                i.putExtra("SALDO_VIRTUAL", saldo_virtual);
                                i.putExtra("CODCLI", codcli);
                                i.putExtra("NOMCLI", nomcli);
                                i.putExtra("ACUENTA_TOTAL", acuenta_total);
                                i.putExtra("ORIGEN", origen);

                                i.putExtra("NUMFACTURA", numfactura);
                                i.putExtra("SERIE", serie);
                                i.putExtra("TIPO", tipo);

                                startActivity(i);
                                //finish();

                            }else if(tipo_de_documento.equals("LETRA")){
                                Log.d("LT", "Libre");

                                Builder builder = new Builder( CuentasXCobrarActivity2.this);
                                builder.setTitle("Importante");
                                builder.setIcon(R.drawable.ic_alert);
                                builder.setMessage("Seleccione una opción:")
                                        .setCancelable(false)
                                        .setPositiveButton("Amortizar", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                Intent i2 = new Intent();
                                                i2 = new Intent(CuentasXCobrarActivity2.this,AmortizarCuentasXCobrarActivity2.class);
                                                i2.putExtra("SECUENCIA", secuen);
                                                i2.putExtra("TOTAL", total);
                                                i2.putExtra("SALDO", saldo);
                                                i2.putExtra("SALDO_VIRTUAL", saldo_virtual);
                                                i2.putExtra("CODCLI", codcli);
                                                i2.putExtra("NOMCLI", nomcli);
                                                i2.putExtra("ACUENTA_TOTAL", acuenta_total);
                                                i2.putExtra("ORIGEN", origen);

                                                i2.putExtra("NUMFACTURA", numfactura);
                                                i2.putExtra("SERIE", serie);
                                                i2.putExtra("TIPO", tipo);

                                                startActivity(i2);
                                                //finish();
                                            }
                                        })
                                        .setNegativeButton("Entregar", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                Intent i2 = new Intent();
                                                i2 = new Intent(CuentasXCobrarActivity2.this,AmortizarCuentasXCobrarActivityLetra.class);
                                                i2.putExtra("SECUENCIA", secuen);
                                                i2.putExtra("TOTAL", total);
                                                i2.putExtra("SALDO", saldo);
                                                i2.putExtra("SALDO_VIRTUAL", saldo_virtual);
                                                i2.putExtra("CODCLI", codcli);
                                                i2.putExtra("NOMCLI", nomcli);
                                                i2.putExtra("ACUENTA_TOTAL", acuenta_total);
                                                i2.putExtra("ORIGEN", origen);

                                                i2.putExtra("NUMFACTURA", numfactura);
                                                i2.putExtra("SERIE", serie);
                                                i2.putExtra("TIPO", tipo);

                                                startActivity(i2);
                                                //finish();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }

                        }else{
                            Toast.makeText(CuentasXCobrarActivity2.this, "Sin acciones por el momento", Toast.LENGTH_SHORT).show();
                        }

                        Log.d("Nada", "Nada");

                    } else
                        Toast.makeText(getApplicationContext(),"Ya no tiene deudas por amortizar",Toast.LENGTH_LONG).show();

                } else {

                    int cant_detalles = obj_dbclasses.ObtenerDetalleCtaXCobrar2(secuen).size();

                    if (cant_detalles > 0) {
                        Intent i = new Intent(getApplicationContext(),DetalleCtasCobrarActivity.class);
                        i.putExtra("numfactura", numfactura);
                        i.putExtra("TOTAL", total);
                        i.putExtra("ACUENTA_TOTAL", acuenta_total);
                        i.putExtra("SECUENCIA", secuen);
                        i.putExtra("SALDO", saldo);
                        i.putExtra("SALDO_VIRTUAL", saldo_virtual);
                        i.putExtra("MONEDA", moneda);
                        startActivity(i);
                    } else
                        Toast.makeText(getApplicationContext(),"Este documento a�n no tiene pagos efectuados",Toast.LENGTH_LONG).show();
                }
            }
        });


        list.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("ERROR", " " + lista_cta_ingresos_result.get(position).getNumero_factura()+" "
                        + lista_cta_ingresos_result.get(position).getSerie_doc()+" "
                        + lista_cta_ingresos_result.get(position).getCoddoc()+"Cliente "
                        + lista_cta_ingresos_result.get(position).getCodcli()  );

                secuen =  lista_cta_ingresos_result.get(position).getSecuencia();

                numfactura = lista_cta_ingresos_result.get(position).getNumero_factura();
                total =  lista_cta_ingresos_result.get(position).getTotal();//El total viene a ser el saldo
                saldo =  lista_cta_ingresos_result.get(position).getTotal();//El saldo siempre es el total
                saldo_virtual =  lista_cta_ingresos_result.get(position).getSaldo_virtual();
                acuenta_total = Double.parseDouble(total)- Double.parseDouble(saldo_virtual) + "";
                tipo_de_documento = lista_cta_ingresos_result.get(position).getCoddoc();
                serie = lista_cta_ingresos_result.get(position).getSerie_doc();
                tipo = lista_cta_ingresos_result.get(position).getTipo();
                formaPago = lista_cta_ingresos_result.get(position).getEstado_cobranza();

                Log.i("Enviando parametros : numfactura:",numfactura+", serie:"+serie+", tipo:"+tipo+" formaPago :"+formaPago);
                if (Double.parseDouble(saldo_virtual)>0){
                    mQuickAction.show(view);
                }else{
                    UtilViewSnackBar.SnackBarWarning(CuentasXCobrarActivity2.this, list, "Monto a favor de cliente.");
                }

            }
        });

        inputSearch_documento.setHint("Buscar por docuemento, fecha emision o fecha vencimiento");
        new ACG_EditText(this, inputSearch_documento).OnListen(texto ->{
            lista_cta_ingresos_result.clear();
                for (int x = 0; x < lista_cta_ingresos_original.size(); x++) {
                    String texto1 = lista_cta_ingresos_original.get(x).getSerie_doc()+"-"+ lista_cta_ingresos_original.get(x).getNumero_factura();
                    String texto2 = lista_cta_ingresos_original.get(x).getFeccom()+ lista_cta_ingresos_original.get(x).getFecha_vencimiento();
                    if (texto1.toLowerCase().contains(texto.toLowerCase()) || texto2.toLowerCase().contains(texto.toLowerCase())) {
                        lista_cta_ingresos_result.add(lista_cta_ingresos_original.get(x));
                    }
                }
            getSupportActionBar().setSubtitle(lista_cta_ingresos_result.size()+" Documentos Encontrados");
                if (cta_adapter!=null){
                    cta_adapter.notifyDataSetChanged();
                }

        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                RegresarAtras();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onBackPressed() {
        RegresarAtras();
    }
    private void RegresarAtras(){
        if (origen.equals("clienteac")) {
            finish();
        } else {
            Intent i = new Intent(getApplicationContext(), CobranzaActivity2.class);
            i.putExtra("CODCLI", codcli);
            i.putExtra("NOMCLI", nomcli);
            startActivity(i);
            finish();
        }
    }

    public BigDecimal redondear(double val) {
        String r = val + "";
        BigDecimal big = new BigDecimal(r);
        big = big.setScale(2, RoundingMode.HALF_UP);
        return big;
    }

    class asyncTask extends AsyncTask<Void, Void, ArrayList<DBCta_Ingresos>> {

        String user, pass, mensaje="";
        double deudaDolares=00;
        double deudaSoles=00;
        double  totalDolaresDocumento=00;
        double totalSolesDocumento=00;
        double montoFavorClienteObliDolar=0;
        double montoFavorClienteDeudaDolar=0;
        double montoFavorClienteObliSoles=0;
        double montoFavorClienteDeudaSoles=0;

        double obliDolaresTotal=00;
        double obliDolaresTotalDocumento=00;
        double obliSolesTotal=00;
        double obliSolesTotalDocumento=00;
        protected void onPreExecute() {
            // para el progress dialog
            pDialog = new ProgressDialog(CuentasXCobrarActivity2.this);
            pDialog.setMessage("Cargando....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected ArrayList<DBCta_Ingresos> doInBackground( Void... params) {

            final ArrayList<HashMap<String, String>> newsList = new ArrayList<HashMap<String, String>>();

            /*Sincronizar datos--------------------------------------------*/

            prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
            soap_manager = new DBSync_soap_manager(getApplicationContext());

            String  url = prefs.getString("url", "0");
            String  catalog = prefs.getString("catalog", "0");
            String  userid = prefs.getString("userid", "0");
            String  contrasena = prefs.getString("contrasenaid", "0");

            codigoVendedor = new SessionManager(CuentasXCobrarActivity2.this).getCodigoVendedor();


            try {
                Log.d("Documento", codigoVendedor+"-"+codcli+"-"+url+"-"+catalog+"-"+userid+"-"+contrasena+"-");
                Log.d(TAG,"Sync_tabla_cta_ingresos_x_cliente("+codigoVendedor+","+ parametro+","+codcli+","+ url+","+catalog);
                soap_manager.Sync_tabla_cta_ingresos_x_cliente(codigoVendedor, parametro+","+codcli, url,catalog, userid,contrasena );
            }catch(TimeoutException ex){
                mensaje="Se superó el tiempo de respuesta";
            }catch (Exception e) {
                //e.printStackTrace();
                mensaje="Se superó el tiempo de respuesta";
                Log.e(TAG, e.getMessage());
            }
            /*------------------------------------------------------------------------*/

            lista_cta_ingresos_original = obj_dbclasses.VerificarCtasXCobrar(codcli);
            lista_cta_ingresos_result.addAll(lista_cta_ingresos_original);

            try {

                Iterator<DBCta_Ingresos> it = lista_cta_ingresos_result.iterator();


                while (it.hasNext()) {
                    Object objeto = it.next();
                    DBCta_Ingresos cta = (DBCta_Ingresos) objeto;

                   /* HashMap<String, String> map = new HashMap<String, String>();
                    // map.put(KEY_CODCLI,obtenerPersona[i].getCodcli());
                    map.put(KEY_TIPO_DOCUMENTO, cta.getCoddoc());
                    map.put(KEY_NUMERO, cta.getSerie_doc() + cta.getNumero_factura());
                    map.put(KEY_FECHA_E, cta.getFeccom());
                    map.put(KEY_FECHA_V, cta.getFecha_vencimiento());
                    map.put(KEY_TOTAL, cta.getTotal());
                    map.put(KEY_SALDO_TOTAL,  cta.getCodmon() + Double.parseDouble(cta.getSaldo()) + "");
                    map.put(KEY_USUARIO,  cta.getUsername());
                    map.put("Estado",  cta.getEstado_cobranza());
                    map.put("Observaciones",  cta.getObservaciones());
                    map.put("Moneda",  cta.getCodmon());
                    map.put("NroUnicoBanco", cta.getNroUnicoBanco());
                    newsList.add(map);*/

                    long lonVencimiento=VARIABLES.convertirFecha_to_long(cta.getFecha_vencimiento());
                    long lonActual=VARIABLES.GetFechaActua_long();

                    double _saldo=Double.parseDouble(cta.getSaldo());
                    double _totalDoc=Double.parseDouble(cta.getTotal());//saldo por ducumento
                    if (_saldo<0){
                        //montoFavorCliente=_saldo;
                        //_saldo=_saldo*(-1);
                        _totalDoc=0;//por ser a favor de cliente
                        //_saldo=_totalDoc+_saldo;
                    }else{
                        //_saldo=0;
                    }//txtObligacionDolares.setText(VARIABLES.formater_thow_decimal.format(obliDolaresTotalDocumento-obliDolaresTotal));
                    if(cta.getCodmon().equals("$.")){
                        if (lonVencimiento>=lonActual){// por vencer
                            obliDolaresTotal+=  _saldo;
                            obliDolaresTotalDocumento+= _totalDoc;
                        }else{// por vencido
                            deudaDolares+= _saldo;
                            totalDolaresDocumento+= _totalDoc;
                        }
                    } else {
                        if (lonVencimiento>=lonActual){
                            obliSolesTotal+=  _saldo;
                            obliSolesTotalDocumento+= _totalDoc;
                        }else{
                            deudaSoles+= _saldo;
                            totalSolesDocumento+= _totalDoc;
                        }

                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return lista_cta_ingresos_original;
        }

        /*
         * Una vez terminado doInBackground segun lo que halla ocurrido pasamos
         * a la sig. activity o mostramos error
         */
        protected void onPostExecute(ArrayList<DBCta_Ingresos> result) {
            pDialog.dismiss();//ocultamos progess dialog.
            Log.e("onPostExecute=",""+result);
            getSupportActionBar().setSubtitle(lista_cta_ingresos_result.size()+" Documentos Encontrados");

            cta_adapter = new Cobranza_LazyAdapter(CuentasXCobrarActivity2.this, codigoVendedor, lista_cta_ingresos_result);
            list.setAdapter(cta_adapter);


            txtDeudaDolares.setText(VARIABLES.formater_thow_decimal.format(deudaDolares));
            txtDeudaSoles.setText(VARIABLES.formater_thow_decimal.format(deudaSoles));
            txtTotalDolaresDocumento.setText("de "+VARIABLES.formater_thow_decimal.format(totalDolaresDocumento));
            txtTotalSolesDocumento.setText("de "+VARIABLES.formater_thow_decimal.format(totalSolesDocumento));

            txtObligacionDolares.setText(VARIABLES.formater_thow_decimal.format(obliDolaresTotal));
            txtObligacionSoles.setText(VARIABLES.formater_thow_decimal.format(obliSolesTotal));
            txtTotalObligacionDolaresDocumento.setText("de "+VARIABLES.formater_thow_decimal.format(obliDolaresTotalDocumento));
            txtTotalObligacionSolesDocumento.setText("de "+VARIABLES.formater_thow_decimal.format(obliSolesTotalDocumento));

            list.setAdapter(cta_adapter);
            cta_adapter.notifyDataSetChanged();
            if (!mensaje.equals("")) {
                Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
            }
        }

    }

}
