package com.example.sm_tubo_plast.genesys.fuerza_ventas;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.adapters.CobranzaAdapter;
import com.example.sm_tubo_plast.genesys.datatypes.DBSync_soap_manager;
import com.example.sm_tubo_plast.genesys.datatypes.DBclasses;
import com.example.sm_tubo_plast.genesys.util.GlobalFunctions;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import me.piruin.quickaction.ActionItem;
import me.piruin.quickaction.QuickAction;

public class CobranzaActivity2 extends AppCompatActivity {

    DBclasses obj_dbclasses;
    static final String KEY_TOTAL = "total";
    static final String KEY_SALDO = "saldo";
    static final String KEY_CLIENTE="cliente";
    static final String KEY_SECUENCIA="secuencia";
    static final String KEY_CANTIDAD="cantidad";
    static final String KEY_NUMERO="numero";

    SharedPreferences.Editor editor;
    SharedPreferences prefs;

    public String secuen ,acuenta_total;
    public String saldo="0";
    String total="0";
    String nomcli, numero;
    ProgressDialog pDialog;
    EditText inputSearch;
    TextView txt_total_cobranzas;
    TextView txt_cantidad_cobranzas;
    TextView txt_total_acuenta;
    TextView txt_total_saldo;
    DBSync_soap_manager soap_manager;

    ArrayList<HashMap<String, String>> alhm_cobranza_soles =new ArrayList<HashMap<String,String>>();
    ArrayList<HashMap<String, String>> alhm_cobranza_dolares =new ArrayList<HashMap<String,String>>();

    ArrayList<HashMap<String, String>> searchResults;
    ListView list;
    CobranzaAdapter adapter_cobranza;

    private static final int ID_AMORTIZAR = 1;
    private static final int ID_DETALLE = 2;
    AlertDialog.Builder alert_bld_cobranza;
    Boolean libre,programada ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cobranza2);

        alert_bld_cobranza = new AlertDialog.Builder(this);
        //titleBar.afterSetContentView();
        obj_dbclasses= new DBclasses(getApplicationContext());
        setTitle("Cuentas por Cobrar");

        prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        programada = prefs.getString("Programado", "1").equals("1"); // 0 :Inactivo 1:Activado
        libre = prefs.getString("Libre", "1").equals("1"); // 0 :Inactivo 1:Activado

        //´para que no salga el teclado al iniciar activity
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        list = (ListView)findViewById(R.id.lv_cobranza);
        inputSearch = (EditText)findViewById(R.id.inputSearch_cobranzas);
        txt_cantidad_cobranzas = (TextView)findViewById(R.id.cobranza_activity_txtCantidad_cobranzas);
        txt_total_cobranzas = (TextView)findViewById(R.id.cobranza_activity_txtTotal_cobranzas);
        txt_total_acuenta = (TextView)findViewById(R.id.cobranza_activity_txtTotal_acuenta);
        txt_total_saldo = (TextView)findViewById(R.id.cobranza_activity_txtTotal_saldo);

        soap_manager = new DBSync_soap_manager(getApplicationContext());

        new cargarCobranzas().execute("");

        ActionItem addItem 		= new ActionItem(ID_AMORTIZAR, "Amortizar", (R.drawable.pagar));
        ActionItem acceptItem 	= new ActionItem(ID_DETALLE, "Ver Detalle", (R.drawable.detalle2));

        final QuickAction mQuickAction 	= new QuickAction(this);

        mQuickAction.addActionItem(addItem);
        mQuickAction.addActionItem(acceptItem);


        //setup the action item click listener
        mQuickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {


            @Override
            public void onItemClick(ActionItem item) {
                int actionId=item.getActionId();

                if (actionId == ID_AMORTIZAR) {

                    Intent i=new Intent(getApplicationContext(), CuentasXCobrarActivity2.class);

                    i.putExtra("CODCLI", obj_dbclasses.obtenerCodigoCliente(nomcli));
                    i.putExtra("NOMCLI", nomcli);
                    i.putExtra("ORIGEN", "clienteac");
                    startActivity(i);
                    finish();

                }
                else {

                    Intent intent = new Intent(CobranzaActivity2.this,DetalleCtasCobrarActivity.class);
                    intent.putExtra("numfactura",numero);
                    intent.putExtra("SECUENCIA",secuen);
                    intent.putExtra("TOTAL",total);
                    intent.putExtra("ACUENTA_TOTAL", acuenta_total);
                    intent.putExtra("SALDO", saldo);

                    startActivity(intent);


                }
            }
        });



        list.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //	mQuickAction.show(view);
	    				/*String	numoc= ((TextView) view.findViewById(R.id.rpt_pedido_tv_oc_numero)).getText().toString();
	    				String	cond= ((TextView) view.findViewById(R.id.rpt_pedido_tv_tipopago)).getText().toString();
	    				cond_pago = cond;
		    			oc_numero=numoc;*/
                //change the right arrow icon to selected state


                //String	sal= ((TextView) view.findViewById(R.id.item_cobranza_tv_saldo)).getText().toString();
                //String	tot= ((TextView) view.findViewById(R.id.item_cobranza_tv_total)).getText().toString();
                //String sec= ((TextView) view.findViewById(R.id.item_cobranza_tv_secuencia)).getText().toString();

                String nomcli2= ((TextView) view.findViewById(R.id.item_cobranza_tv_cliente)).getText().toString();
                //codigo
                //String numf= ((TextView) view.findViewById(R.id.item_cobranza_tv_numero)).getText().toString();

                //numero=numf;
                //total = tot.substring(10);
                //saldo=sal.substring(10);
                //secuen=sec;
                //acuenta_total=Double.parseDouble(total)-Double.parseDouble(saldo)+"";
                nomcli=nomcli2;


                Intent i=new Intent(getApplicationContext(), CuentasXCobrarActivity2.class);
                i.putExtra("CODCLI", obj_dbclasses.obtenerCodigoCliente(nomcli));
                i.putExtra("NOMCLI", nomcli);
                i.putExtra("ORIGEN", "cobranzaac2");
                startActivity(i);
                finish();

            }
        });


        inputSearch.addTextChangedListener(new TextWatcher() {



            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                try{
                    searchResults.clear();
                    if (TextUtils.isDigitsOnly(charSequence)) {
                        for (int x = 0; x < alhm_cobranza_soles.size(); x++) {
                            String ruc = alhm_cobranza_soles.get(x).get("codigo").toString();
                            if (ruc.indexOf(""+charSequence) != -1) {
                                searchResults.add(alhm_cobranza_soles.get(x));
                            }
                        }
                    } else {
                        for(int i=0;i<alhm_cobranza_soles.size();i++){
                            String nomcli=alhm_cobranza_soles.get(i).get(KEY_CLIENTE).toString();
                            if (nomcli.indexOf(charSequence.toString().toUpperCase()) != -1) {
                                searchResults.add(alhm_cobranza_soles.get(i));
                            }
                        }
                    }




                    calcular_totales();
                    adapter_cobranza.notifyDataSetChanged();

                }catch(Exception e){
                    e.printStackTrace();
                }
            }


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });


        /* ***************ENVIAR MENSAJE DE SINCRONIZACION************** */
        SharedPreferences preferencias_configuracion;
        preferencias_configuracion = getSharedPreferences("preferencias_configuracion", Context.MODE_PRIVATE);
        boolean sincronizacionCorrecta = preferencias_configuracion.getBoolean("preferencias_sincronizacionCorrecta", false);
        if (sincronizacionCorrecta == false) {
            AlertDialog.Builder alerta = new AlertDialog.Builder(CobranzaActivity2.this);
            alerta.setTitle("Sincronizacion incompleta");
            alerta.setMessage("Los datos estan incompletos, sincronice correctamente");
            alerta.setIcon(R.drawable.icon_warning);
            alerta.setCancelable(false);
            alerta.setPositiveButton("OK", null);
            alerta.show();
        }
        /****************************************************************/

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void crearDialogo(){

        final Dialog dialogo = new Dialog(this);

        dialogo.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialogo.setContentView(R.layout.dialog_motivo_noventa);
        dialogo.setCancelable(false);
        dialogo.show();

    }

    public BigDecimal redondear(double val){
        String r = val+"";
        BigDecimal big = new BigDecimal(r);
        big = big.setScale(2, RoundingMode.HALF_UP);
        return big;
    }

    //Correccion de la demora al entrar al modulo cobranza
    //se estaba cargando en el hilo principal
    //cambio a async task 07-08-2013

    class cargarCobranzas extends AsyncTask<String, Integer, ArrayList<HashMap<String, String>> > {
        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();


        protected void onPreExecute() {
            //para el progress dialog
            pDialog = new ProgressDialog(CobranzaActivity2.this);
            pDialog.setMessage("Cargando...");
            pDialog.setIndeterminate(false);
            pDialog.show();
        }


        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(String... params) {
            String buscar = params[0];

            try{

                alhm_cobranza_soles = obj_dbclasses.getCtas_Ingresos_ResumenxCliente();//getCtas_IngresosxCliente2

            }
            catch(Exception e)        {
                Log.e("log_tag", "Error parsing data "+e.toString());
            }

            return alhm_cobranza_soles;
        }


        @Override
        protected void onPostExecute(ArrayList<HashMap<String, String>> result) {

            pDialog.dismiss();//ocultamos progress dialog.

            searchResults = new ArrayList<HashMap<String,String>>(alhm_cobranza_soles);

            adapter_cobranza = new CobranzaAdapter(CobranzaActivity2.this, searchResults);
            list.setAdapter(adapter_cobranza);

            LayoutInflater inflater = LayoutInflater.from(CobranzaActivity2.this);
            View emptyView = inflater.inflate(R.layout.list_empty_clients, null);
            emptyView.setVisibility(View.GONE);
            ((ViewGroup) list.getParent()).addView(emptyView);
            list.setEmptyView(emptyView);

            // cli_adapter=new Clientes_LazyAdapter(this, searchResults);
            list.setAdapter(adapter_cobranza);
            calcular_totales();

            //Log.d("", "Totales FIN");
        }
    }

    private void calcular_totales(){

        double total_dolares=0.0;
        double total_Soles=0.0;
        int total_cantidad_E = 0;
        int total_cantidad_R = 0;
        //int asignado;
        String asignado;

        DecimalFormat formateador = GlobalFunctions.formateador();

        for(int i=0;i<searchResults.size();i++){
            //asignado=Integer.parseInt(searchResults.get(i).get("asignado"));
            asignado=searchResults.get(i).get("asignado");
            //Log.e("asignado", "asignado: "+asignado);
            if(asignado.equals("1") ){
                total_Soles = total_Soles + Double.parseDouble(searchResults.get(i).get("total"));
                total_dolares = total_dolares + Double.parseDouble(searchResults.get(i).get("total_acuenta"));
                total_cantidad_E = total_cantidad_E + Integer.parseInt(searchResults.get(i).get("entregar"));
                total_cantidad_R = total_cantidad_R + Integer.parseInt(searchResults.get(i).get("aceptar"));

                // Log.e("totales Ingreso", "Sumar "+total_Soles +" sola"+total_dolares);
            }
        }

        // Log.e("totales ", ""+total_Soles+"-"+total_dolares+"-");
        txt_total_cobranzas.setText("S/." + formateador.format(GlobalFunctions.redondear(total_Soles)));
        txt_cantidad_cobranzas.setText("$."+ formateador.format(GlobalFunctions.redondear(total_dolares)));
        txt_total_acuenta.setText("Entregar " +total_cantidad_E+" L.");
        txt_total_saldo.setText("Recoger "+total_cantidad_R+" L.");


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //Alternativa 1
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cobranza, menu);
        return true;

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_obtener_cuentas_ingreso:
                new asyncCobranza().execute("");
                return true;
            case R.id.menu_configuracion_cuentas_ingreso:
                mostrarDialogoConfiguracion();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void mostrarDialogoConfiguracion() {
        // TODO Auto-generated method stub
        programada = prefs.getString("Programado", "1").equals("1"); // 0 :Inactivo 1:Activado
        libre = prefs.getString("Libre", "1").equals("1"); // 0 :Inactivo 1:Activado

        boolean[] alternativas = {programada, libre};
        alert_bld_cobranza.setTitle("Sincronizar Cobranzas ");
        alert_bld_cobranza.setMultiChoiceItems(new CharSequence[] { "Programadas", "Libres" },alternativas, new DialogInterface.OnMultiChoiceClickListener() {
            public void onClick(DialogInterface dialog,
                                int whichButton, boolean isChecked) {

            }
        });
        alert_bld_cobranza.setPositiveButton("Aceptar",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int whichButton) {
                ListView list = ((AlertDialog) dialog).getListView();
                StringBuilder sb = new StringBuilder();

                for (int i = 0; i < list.getCount(); i++) {
                    boolean checked = list.isItemChecked(i);
                    if (checked) {
                        if (sb.length() > 0) sb.append(",");
                        sb.append(list.getItemIdAtPosition(i));
                    }
                }

                StringTokenizer stTexto = new StringTokenizer(sb.toString(), ",");
                String[] tablas = new String[stTexto.countTokens()];
                int a = 0;
                while (stTexto.hasMoreTokens()) {
                    tablas[a] = stTexto.nextToken().toString();
                    String as = tablas[a].toString();
                    a = a + 1;
                }

                editor = prefs.edit();
                editor.putString("Programado","0");
                editor.putString("Libre","0");

                for (int i = 0; i < tablas.length; i++) {
                    int valor = Integer.parseInt(tablas[i].toString());

                    switch (valor) {
                        case 0:
                            editor.putString("Programado","1");
                            break;
                        case 1:
                            editor.putString("Libre","1");
                            break;
                        default:
                            break;
                    }
                }
                editor.apply();
            }
        })
                .setNegativeButton("Cancelar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog,
                                    int whichButton) {
                            }
                        }).show();

    }

    class asyncCobranza extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(CobranzaActivity2.this);
            pDialog.setMessage("Sincronizando....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            SharedPreferences prefs3 = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
            String codven = prefs3.getString("codven", "0");

            String servidorBD = prefs3.getString("url", "0");
            String nombreBD = prefs3.getString("catalog", "0");
            String usuarioBD = prefs3.getString("userid", "0");
            String contrasenaBD = prefs3.getString("contrasenaid", "0");

            try {
                soap_manager.Sync_tabla_cta_ingresos_resumen(codven, servidorBD, nombreBD, usuarioBD, contrasenaBD);
            } catch (Exception e) {
                Log.d("COBRANZA","Error al sincronizar :"+e);
            }

            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            pDialog.dismiss();// ocultamos progess dialog.
            Log.e("onPostExecute=", "" + result);
        }

    }


}
