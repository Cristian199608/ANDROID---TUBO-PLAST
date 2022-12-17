package com.example.sm_tubo_plast.genesys.fuerza_ventas;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.datatypes.DBBanco;
import com.example.sm_tubo_plast.genesys.datatypes.DBCtas_xbanco;
import com.example.sm_tubo_plast.genesys.datatypes.DBDepositos;
import com.example.sm_tubo_plast.genesys.datatypes.DBSync_soap_manager;
import com.example.sm_tubo_plast.genesys.datatypes.DBclasses;
import com.example.sm_tubo_plast.genesys.service.ConnectionDetector;
import com.example.sm_tubo_plast.genesys.service.CustomTitleBar;
import com.example.sm_tubo_plast.genesys.session.SessionManager;
import com.google.gson.JsonParseException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

@SuppressLint("LongLogTag")
public class DepositosModificarActivity extends AppCompatActivity implements View.OnClickListener {
    ProgressDialog pDialog;
    Button btn_agregar, btn_cancelar;
    String codven ;
    private RadioGroup rb_moneda;
    Spinner spn_banco, spn_cta;
    String nomban [] ;
    String codban [] ;
    String moneda [] ;
    int cod_cta[];
    String num_cta [] ;
    String lista_cod_cuenta [] ;
    ArrayList<DBBanco> al_bancos;

    ArrayList<DBCtas_xbanco>  al_ctas_xbanco;
    String secuencia;
    ArrayList<DBDepositos> al_depositos;
    int id_banco=0, id_cuenta=0;
    // flag for Internet connection status
    Boolean isInternetPresent = false;
    DBDepositos dbdepositos ;
    // Connection detector class
    ConnectionDetector cd;
    private static final int Fecha_Dialog_ID = 1;
    String numfactura, saldo;
    DBclasses obj_dbclasses;
    Calendar calendar = Calendar.getInstance();
    private EditText edt_FechaEntrega,edt_secuencia,edt_saldo, edt_acuenta, edt_numope, edt_monto;
    //FECHA ACTUAL
    int año_act;
    int mes_act;
    int dia_act;
    int dia,mes,año;


    private DatePickerDialog.OnDateSetListener dateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {

                    año = year;
                    mes = monthOfYear;
                    dia = dayOfMonth;

                    edt_FechaEntrega.setText("");

                    if(año<año_act){
                        ColocarFecha();
                    }
                    else if(año==año_act && (mes+1)<mes_act){
                        ColocarFecha();
                    }
                    else if(año==año_act && (mes+1)==mes_act && dia<=dia_act){
                        ColocarFecha();
                    }
                    else{
                        error();
                        colocarFechaActual();
                    }

                }

                public void error(){
                    Toast.makeText(getApplicationContext(), "Fecha Invalida", Toast.LENGTH_SHORT).show();
                }
            };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomTitleBar titleBar = new CustomTitleBar(this, R.string.lbldepositos, R.drawable.report_deposito_v4);

        setContentView(R.layout.activity_depositos_modificar);

        titleBar.afterSetContentView();

        obj_dbclasses = new DBclasses(getApplicationContext());
        edt_FechaEntrega = (EditText)findViewById(R.id.deposito__modificar_edt_fecha);
        edt_numope= (EditText)findViewById(R.id.deposito__modificar_edt_noperacion);
        edt_monto=(EditText)findViewById(R.id.deposito__modificar_edt_monto);
        rb_moneda = (RadioGroup) findViewById(R.id.deposito__modificar_rb_moneda);
        spn_banco = (Spinner)findViewById(R.id.deposito_modificar_spn_banco);
        spn_cta = (Spinner)findViewById(R.id.deposito_modificar_spn_cta);
        btn_agregar= (Button)findViewById(R.id.depositos__modificar_btnAgregar);
        btn_cancelar= (Button)findViewById(R.id.depositos_modificar_btnCancelar);
        edt_secuencia=(EditText)findViewById(R.id.deposito_modificar_edt_secuencia);
        btn_agregar.setOnClickListener(this);
        cd = new ConnectionDetector(getApplicationContext());

        //obtener la fecha de la tabla configuracion

        String fecha = obj_dbclasses.getCambio("Fecha");

        mes_act = Integer.parseInt(fecha.substring(3,5));
        dia_act = Integer.parseInt(fecha.substring(0,2));
        año_act = Integer.parseInt(fecha.substring(6,10));

        rb_moneda.setEnabled(false);
        codven = new SessionManager(this).getCodigoVendedor();

        Bundle bundle = getIntent().getExtras();
        secuencia=""+bundle.getString("secuencia");
        edt_secuencia.setText(secuencia);
        al_depositos= new ArrayList<DBDepositos>();
        al_depositos= obj_dbclasses.getDepositosxSec(secuencia);

        mostrarBancos();
        colocarFechaActual();

        spn_banco.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                String text = spn_banco.getSelectedItem().toString();
                for(int i=0; i<nomban.length; i++){
                    if(text.equals(nomban[i])){
                        id_banco=i;
                    }
                }
                mostrarCtas_xBanco(codban[id_banco]);


            }


            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });

        spn_cta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                String text = spn_cta.getSelectedItem().toString();
                for(int i=0; i<num_cta.length; i++){
                    if(text.equals(num_cta[i])){
                        id_cuenta=i;
                    }
                }

                rb_moneda.setEnabled(true);
                if(moneda[id_cuenta].equals("01")){
                    rb_moneda.check(R.id.rb_soles);
                }
                else
                    rb_moneda.check(R.id.rb_dolares);

            }



            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });


        cargarDatos();


        btn_cancelar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finish();
            }
        });


    }

    public void ColocarFecha(){

        String _dia,_mes;

        if(dia>0 && dia<10){
            _dia="0"+dia;
        }else{
            _dia=""+dia;
        }

        if((mes+1)<10){
            _mes="0"+(mes+1);
        }else{
            _mes=""+(mes+1);
        }

        edt_FechaEntrega.setText(new StringBuilder()
                .append(_dia).append("/")
                .append(_mes)
                .append("/")
                .append(año));

    }


    public void colocarFechaActual(){

        String _dia,_mes;

        if(dia_act>0 && dia_act<10){
            _dia="0"+dia_act;
        }else{
            _dia=""+dia_act;
        }

        if(mes_act<10){
            _mes="0"+(mes_act);
        }else{
            _mes=""+(mes_act);
        }

        edt_FechaEntrega.setText(new StringBuilder()
                .append(_dia).append("/")
                .append(_mes)
                .append("/")
                .append(año_act));

    }


    @Override
    protected Dialog onCreateDialog(int id){

        switch (id) {
            case Fecha_Dialog_ID:
                return new DatePickerDialog(this,dateSetListener,año,mes,dia);

            default:
                return null;
        }
    }
    //Picker Dialog funcion de button en layout
    public void Mostrar_date(View view){

        dia = calendar.get(Calendar.DAY_OF_MONTH);
        mes = calendar.get(Calendar.MONTH);
        año = calendar.get(Calendar.YEAR);

        showDialog(Fecha_Dialog_ID);
    }

    public void mostrarBancos(){
        al_bancos= new ArrayList<DBBanco>();
        al_bancos= obj_dbclasses.getBancos();
        obj_dbclasses.close();
        Iterator<DBBanco> it=al_bancos.iterator();
        int i=0;
        nomban = new String [al_bancos.size()];
        codban = new String [al_bancos.size()];
        while ( it.hasNext() ) {
            Object objeto = it.next();
            DBBanco dbbanco = (DBBanco)objeto;
            nomban[i]=dbbanco.getNomban();
            codban[i]=dbbanco.getCodban();
            i++;
        }



        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(getApplicationContext(),
                android.R.layout.simple_spinner_item, nomban);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_banco.setAdapter(adapter);

        //   mostrarCondPago();
    }

    private void mostrarCtas_xBanco(String cod) {
        // TODO Auto-generated method stub
        al_ctas_xbanco= new ArrayList<DBCtas_xbanco>();
        al_ctas_xbanco= obj_dbclasses.getCtas_xBanco(cod);
        obj_dbclasses.close();
        Iterator<DBCtas_xbanco> it=al_ctas_xbanco.iterator();
        int i=0;
        num_cta = new String [al_ctas_xbanco.size()];
        cod_cta = new int[al_ctas_xbanco.size()];
        moneda = new String[al_ctas_xbanco.size()];
        while ( it.hasNext() ) {
            Object objeto = it.next();
            DBCtas_xbanco dbctas_xbanco = (DBCtas_xbanco)objeto;
            num_cta[i]=dbctas_xbanco.getCta_cte();
            cod_cta[i]=dbctas_xbanco.getItem();
            moneda[i]=dbctas_xbanco.getCodmon();
            i++;
        }



        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(getApplicationContext(),
                android.R.layout.simple_spinner_item, num_cta);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_cta.setAdapter(adapter);

    }

    public void actualizarDeposito(){

        String fecha = edt_FechaEntrega.getText().toString();
        DBDepositos dbdepositos2= new DBDepositos();

        dbdepositos2.setId_banco(codban[id_banco]);
        dbdepositos2.setId_num_cta(cod_cta[id_cuenta]);
        dbdepositos2.setFecha(fecha+ " "+ getHoraActual() );
        dbdepositos2.setNum_ope(edt_numope.getText().toString());
        dbdepositos2.setMoneda(moneda[id_cuenta]);
        dbdepositos2.setMonto(edt_monto.getText().toString());
        dbdepositos2.setEstado("M");
        dbdepositos2.setCodven(codven);
        dbdepositos2.setBI_DEPO_FLAG("P");

        obj_dbclasses.actualizarDepositos(dbdepositos2, secuencia);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch(v.getId()){
            case R.id.depositos__modificar_btnAgregar:

                AlertDialog.Builder builder = new AlertDialog.Builder(DepositosModificarActivity.this);
                builder.setTitle("Importante");
                builder.setIcon(R.drawable.ic_alert);
                builder.setMessage("Se guardarán todos los datos")

                        .setCancelable(false)
                        .setPositiveButton("Enviar al servidor", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                actualizarDeposito();
                                new asyncModificar().execute("","");

                            }
                        })
                        .setNegativeButton("Guardar Localmente", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                actualizarDeposito();
                                finish();

		                 /*Intent intent2 = new Intent(DepositosActivity.this, ReportesActivity.class );
		              	 intent2.putExtra("origen", "DepositosActivity");
		                 startActivity(intent2);*/
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = builder.create();


                if(edt_numope.getText().toString().matches("")){
                    edt_numope.setError("Ingrese el N° operacion");
                    break;
                }
                if(edt_monto.getText().toString().matches("")){
                    edt_monto.setError("Ingrese el monto");
                    break;
                }

                alert.show();
        }


    }

    @Override
    public void onBackPressed() {


    }

    class asyncModificar extends AsyncTask< String, String, String > {

        String user,pass;
        protected void onPreExecute() {
            //para el progress dialog
            pDialog = new ProgressDialog(DepositosModificarActivity.this);
            pDialog.setMessage("Modificando....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... params) {

            DBSync_soap_manager soap_manager = new DBSync_soap_manager(getApplicationContext());

            String valor = "";

            if (cd.hasActiveInternetConnection(getApplicationContext())) {

                try {
                    valor = soap_manager.actualizarDepositos2(secuencia);
                }
                catch (JsonParseException e) {
                    //excepcion al parsear json
                    valor = "error_2";
                }
                catch(Exception e){
                    //cualquier otro error al enviar datos
                    valor = "error_3";
                }
            }
            else
            {
                valor = "error_1";
            }

            return valor;
        }

        /*Una vez terminado doInBackground segun lo que halla ocurrido
        pasamos a la sig. activity
        o mostramos error*/

        protected void onPostExecute(String result) {

            pDialog.dismiss();//ocultamos progess dialog.


            if(result.equals("E")){

                crear_dialogo_post_envio("ENVIO CORRECTO", "El deposito fue ingresado al Servidor", R.drawable.check);

            }
            else if(result.equals("I")){

                crear_dialogo_post_envio("ATENCION", "No se pudieron guardar todos los datos", R.drawable.alert);

            }
            else if(result.equals("P")){

                crear_dialogo_post_envio("ATENCION", "El servidor no pudo ingresar este deposito", R.drawable.ic_alert);

            }
            else if(result.equals("T")){

                crear_dialogo_post_envio("ATENCION", "Este deposito ya se encuentra en proceso de facturacion \nComuniquese con el administrador", R.drawable.ic_alert);

            }
            else if(result.equals("error_1")){

                crear_dialogo_post_envio("SIN CONEXION", "Es probable que no tenga acceso a la red \nEl deposito se guardo localmente", R.drawable.ic_alert);

            }
            else if(result.equals("error_2")){

                crear_dialogo_post_envio("ATENCION", "El deposito fue enviado pero no se pudo verificar \nVerifique manualmente", R.drawable.alert);

            }
            else if(result.equals("error_3")){

                crear_dialogo_post_envio("ERROR AL ENVIAR", "No se pudo Enviar este deposito \nSe guardo localmente", R.drawable.ic_alert);

            }


            Log.e("onPostExecute= DEPOSITOs",""+result);

        }

    }


    public String getHoraActual(){

        Calendar calendar1 = Calendar.getInstance();

        String hora = ""+calendar1.get(Calendar.HOUR_OF_DAY);
        String min = ""+calendar1.get(Calendar.MINUTE);
        String seg = ""+calendar1.get(Calendar.SECOND);

        if(hora.length()<2){
            hora="0"+hora;
        }

        if(min.length()<2){
            min="0"+min;
        }

        if(seg.length()<2){
            seg="0"+seg;
        }

        return hora+":"+min+":"+seg;
    }

    public String calcularSecuencia(){

        String mes_actual=(calendar.get(Calendar.MONTH)+1)+"";
        String dia_actual=calendar.get(Calendar.DAY_OF_MONTH)+"";
        String sec= obj_dbclasses.getDepositos_Max();
        int secactual=0;
        String cero="0";



        if(sec.length()<10){
            secactual=1;
            if(mes_actual.length()<2){
                mes_actual=cero+mes_actual;}
            if(dia_actual.length()<2){
                dia_actual=cero+dia_actual;
            }
            sec=codven +mes_actual+dia_actual+cero+secactual;
            return sec;
        }
        else {
            int diat=Integer.parseInt(sec.substring(8, 10));
            int mest=Integer.parseInt(sec.substring(6, 8));
            int sectem=Integer.parseInt(sec.substring(10,12));

            if(Integer.parseInt(mes_actual)<=mest){
                if(Integer.parseInt(dia_actual)>diat){
                    secactual=1;
                }
                else
                    secactual=sectem+1;

            }
            else{
                secactual=1;
            }
        }

        if(mes_actual.length()<2){
            mes_actual=cero+mes_actual;}
        if(dia_actual.length()<2){
            dia_actual=cero+dia_actual;
        }

        if(secactual<10){
            sec=codven + mes_actual+dia_actual+cero+secactual;
        }
        else{
            sec =codven + mes_actual + dia_actual + secactual;
        }

        return sec;
    }
    public void cargarDatos(){

        Iterator<DBDepositos> it=al_depositos.iterator();

        while ( it.hasNext() ) {
            Object objeto = it.next();
            dbdepositos= (DBDepositos)objeto;
            edt_monto.setText((dbdepositos.getMonto()));
            edt_numope.setText(dbdepositos.getNum_ope());
            edt_FechaEntrega.setText(dbdepositos.getFecha());
            spn_banco.setSelection(calcBan(dbdepositos.getId_banco()));
            spn_cta.setSelection(calcCta(dbdepositos.getId_num_cta()));
        }

    }

    private int calcCta(int id_num_cta) {
        int valor=0;
        for(int i=0; i<cod_cta.length; i++){
            if(cod_cta[i]==id_num_cta){
                valor=i;
                break;

            }
        }

        return valor;
    }

    private int calcBan(String id_banco) {
        int valor=0;
        for(int i=0; i<codban.length; i++){
            if(codban[i].equals(id_banco)){
                valor=i;
                break;

            }
        }
        mostrarCtas_xBanco(codban[valor]);
        return valor;

    }


    private void crear_dialogo_post_envio(String titulo, String mensaje, int icon){

        AlertDialog.Builder dialogo2 = new AlertDialog.Builder(DepositosModificarActivity.this);
        dialogo2.setTitle(titulo);
        dialogo2.setMessage(mensaje);
        dialogo2.setIcon(icon);
        dialogo2.setCancelable(false);
        dialogo2.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {

                finish();

            }
        });

        dialogo2.show();

    }

}
