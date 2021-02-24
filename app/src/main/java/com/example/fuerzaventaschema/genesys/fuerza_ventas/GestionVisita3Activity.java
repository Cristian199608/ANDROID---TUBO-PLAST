package com.example.fuerzaventaschema.genesys.fuerza_ventas;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;


import com.example.fuerzaventaschema.R;
import com.example.fuerzaventaschema.genesys.BEAN.San_Visitas;
import com.example.fuerzaventaschema.genesys.DAO.DAO_Cliente;
import com.example.fuerzaventaschema.genesys.DAO.DAO_San_Visitas;
import com.example.fuerzaventaschema.genesys.DAO.DAO_San_opciones;
import com.example.fuerzaventaschema.genesys.datatypes.DBClientes;
import com.example.fuerzaventaschema.genesys.datatypes.DBPedido_Cabecera;
import com.example.fuerzaventaschema.genesys.datatypes.DBSync_soap_manager;
import com.example.fuerzaventaschema.genesys.datatypes.DB_DireccionClientes;
import com.example.fuerzaventaschema.genesys.datatypes.DB_ObjPedido;
import com.example.fuerzaventaschema.genesys.datatypes.DBclasses;
import com.example.fuerzaventaschema.genesys.fuerza_ventas.Reportes.ReportesPedidosActivity;
import com.example.fuerzaventaschema.genesys.hardware.ObtenerLocalizacion2;
import com.example.fuerzaventaschema.genesys.service.ConnectionDetector;
import com.example.fuerzaventaschema.genesys.util.CustomTimPicker;
import com.example.fuerzaventaschema.genesys.util.GlobalFunctions;
import com.example.fuerzaventaschema.genesys.util.GlobalVar;
import com.example.fuerzaventaschema.genesys.util.UtilView;
import com.example.fuerzaventaschema.genesys.util.VARIABLES;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sun.mail.imap.protocol.ID;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class GestionVisita3Activity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, ObtenerLocalizacion2.InterfaceUbicacion {
    private static final String TAG="GestionVisita3Activity";
    private static final String SIN_HORA="SIN HORA";
    private static final String NO_HAY_PROXIMA_VISITA="NO HAY PROXIMA VISITA";


    DBclasses dBclasses=null;
    String ORIGEN="";
    String ID_RRHH="";
    ArrayList<San_Visitas> LISTA_VISITAS=new ArrayList<>();
    String OC_NUMERO="";
    boolean isPLANIFICADA=false;
    String CODIGO_CRM="";
    String COD_VEND="";
    String NOMBRE_CRM="";
    EditText tv_nombres, et_Obervacion, et_comentario_visita, et_comentario_proxima_visita;
    TextView txt_oc_numero, et_fecha_proximavisita,et_hora_inicio, et_hora_fin, txt_ubicacion;
    Spinner SpinnerTipoVisita, SpinnerTipoActividad, SpinnerProximaTipoVisita, SpinnerProximaActividad,SpinnerDirecciones;
    FloatingActionButton FAB_enviar_visita;
    AutoCompleteTextView autoCompleteTextView_san_contactos;
    Switch sw_isProximaVisita;
    LinearLayout layout_container_prroima_visita;

    ObtenerLocalizacion2 UBICACION;
    Location LOCATION;
    int DISTACIA_to_Colegio=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_visita3);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            ID_RRHH=bundle.getString("ID_RRHH",  "");
            OC_NUMERO=bundle.getString("OC_NUMERO",  null);
            CODIGO_CRM=bundle.getString("CODIGO_CRM",  null);
            NOMBRE_CRM=bundle.getString("NOMBRE_INSTI",  "Sin Nombre");
            COD_VEND=bundle.getString("COD_VEND",  null);
            isPLANIFICADA=bundle.getBoolean("isPLANIFICADA",  false);
            ORIGEN=bundle.getString("ORIGEN",  null);
            if (ID_RRHH.length()!=0 && OC_NUMERO!=null && CODIGO_CRM!=null && COD_VEND!=null && ORIGEN!=null){
                StartActivity();
            }else{
                UtilView.MENSAJE_simple_finish(this, "Sin dato", "No se ha recivido uno o mas datos", true);
            }
        }else{
            UtilView.MENSAJE_simple_finish(this, "Sin dato", "No se ha recivido ningún dato", true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.menu_gestion_visita3, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                BackPressed();
                break;
//            case R.id.menu_guardar:
//                GuardarGestionVisita();
//                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private  void BackPressed(){
        finish();

    }

    @Override
    public void onBackPressed() {
        BackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (UBICACION!=null){
            UBICACION.activarGPS(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (UBICACION!=null){
            UBICACION.activarGPS(true);
        }
    }
    private void StartActivity() {
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("GESTION VISITA");
        //getSupportActionBar().setSubtitle("COLEGIO: "+NOMBRE_CRM);


        tv_nombres=findViewById(R.id.tv_nombres);
        autoCompleteTextView_san_contactos=findViewById(R.id.autoCompleteTextView_san_contactos);
        txt_oc_numero=findViewById(R.id.txt_oc_numero);
        SpinnerTipoVisita=findViewById(R.id.SpinnerTipoVisita);
        SpinnerTipoActividad=findViewById(R.id.SpinnerTipoActividad);
        et_comentario_visita=findViewById(R.id.et_comentario_visita);
        txt_ubicacion=findViewById(R.id.txt_ubicacion);
        et_hora_inicio=findViewById(R.id.et_hora_inicio);
        et_hora_fin=findViewById(R.id.et_hora_fin);
        et_fecha_proximavisita=findViewById(R.id.et_fecha_proximavisita);
        SpinnerProximaTipoVisita=findViewById(R.id.SpinnerProximaTipoVisita);
        SpinnerProximaActividad=findViewById(R.id.SpinnerProximaActividad);
        SpinnerDirecciones=findViewById(R.id.SpinnerDirecciones);
        et_comentario_proxima_visita=findViewById(R.id.et_comentario_proxima_visita);
        sw_isProximaVisita=findViewById(R.id.sw_isProximaVisita);
        layout_container_prroima_visita=findViewById(R.id.layout_container_prroima_visita);
        FAB_enviar_visita=findViewById(R.id.FAB_enviar_visita);
        VARIABLES VAR =new VARIABLES();
        txt_ubicacion.setText("Obteniendo ubicación...");
        dBclasses = new DBclasses(this);

        HabilitarCambioCantacto(false);
        if (ORIGEN.equalsIgnoreCase(ActividadCampoAgendadoActivity.TAG) || ORIGEN.equalsIgnoreCase(ReportesPedidosActivity.TAG)){
            LISTA_VISITAS= DAO_San_Visitas.GetVisitasByOc_numero(dBclasses, OC_NUMERO, isPLANIFICADA);
            if (ID_RRHH.equals("-1")){
                ID_RRHH=LISTA_VISITAS.get(0).getId_rrhh();
                InHabilitarEjecucionVisita();
                InHabilitarProximoVisita();
            }
        }

        DBClientes cliente= dBclasses.getClientexCodigo(""+ID_RRHH);
        //ArrayList<RecursosHumanos> listarrhh=SQ_LITE.getReccursosHumanos_x_INSTI_and_rrhh(""+CODIGO_CRM, 0+ID_RRHH);

        if (cliente!=null){

            autoCompleteTextView_san_contactos.setText(cliente.getNomcli());
            tv_nombres.setText(cliente.getNomcli());
        }
        et_hora_inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                GestionVisita3Activity.this.obtenerHora(et_hora_inicio);
            }
        });
        et_hora_fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                GestionVisita3Activity.this.obtenerHora(et_hora_fin);
            }
        });
        et_fecha_proximavisita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                GestionVisita3Activity.this.DatePicketCustom();
            }
        });

        GetionSwitchProximaVisita();
        String numOc=dBclasses.obtenerNumOC(COD_VEND);
        if (ORIGEN.equalsIgnoreCase(ActividadCampoAgendadoActivity.TAG) || ORIGEN.equalsIgnoreCase(ReportesPedidosActivity.TAG)){

            RecuperarDatos();
            if(isPLANIFICADA) {//es visita
                txt_oc_numero.setText(COD_VEND + calcularSecuencia(numOc));
                UBICACION=new ObtenerLocalizacion2(this);
                PoblarSpinnersTIPO_VISITA_NEXT( "", "");
                HabilitarCambioCantacto(true);
                GestionSanContactos();
            }else{//ejecutada= solo actualización
                txt_oc_numero.setText(OC_NUMERO);
                InHabilitarEjecucionVisita();
            }
            DBPedido_Cabecera dbPedido_cabecera=dBclasses.getPedido_cabecera(OC_NUMERO);
            if (dbPedido_cabecera!=null){
                PoblarSpinnersDireccion(SpinnerDirecciones, Integer.parseInt(dbPedido_cabecera.getSitio_enfa()));
            }

        }else{
            UBICACION=new ObtenerLocalizacion2(this);
            txt_oc_numero.setText(COD_VEND + calcularSecuencia(numOc));
            PoblarSpinnersTIPO_VISITA( "", "");
            PoblarSpinnersTIPO_VISITA_NEXT( "", "");
            PoblarSpinners(SpinnerProximaActividad, GlobalVar.SAN_OPCIONES.TIPO_ACTIVIDD, "");
            PoblarSpinnersDireccion(SpinnerDirecciones, 0);

        }



        //GestionSpinner(SpinnerTipoVisita);
        GestionSpinner(SpinnerTipoActividad);
        GestionSpinner(SpinnerProximaActividad);
        FAB_enviar_visita.setOnClickListener(V->GuardarGestionVisita());

    }
    private  void GetionSwitchProximaVisita(){
        sw_isProximaVisita.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    layout_container_prroima_visita.setVisibility(View.VISIBLE);
                }else {
                    layout_container_prroima_visita.setVisibility(View.GONE);
                }
            }
        });
        layout_container_prroima_visita.setVisibility(View.VISIBLE);
    }
    private void GestionSanContactos(){

//        //autoCompleteTextView_san_contactos.setEnabled(false);
//        ArrayList<RecursosHumanos> listaSan_contacto=SQ_LITE.getReccursosHumanosContactoByInti(CODIGO_CRM);
//        ArrayList<String> strings=new ArrayList<>();
//        int index=0;
//        for (RecursosHumanos item:listaSan_contacto){
//            index++;
//            strings.add(index+"- "+item.getNombres()+" "+item.getApe_paterno()+" "+item.getApe_materno());
//        }
//
//        autoCompleteTextView_san_contactos.setDropDownBackgroundResource(UtilView.getBackgroundWhiteInteger()[0]);
//        autoCompleteTextView_san_contactos.setThreshold(1);
//        autoCompleteTextView_san_contactos.setOnFocusChangeListener((v, hasFocus) -> {
//            //if (hasFocus)autoCompleteTextView_san_contactos.showDropDown();
//        });
//        autoCompleteTextView_san_contactos.setOnClickListener((v) -> {
//            autoCompleteTextView_san_contactos.showDropDown();
//        });
//        autoCompleteTextView_san_contactos.setOnItemClickListener((parent, view, position, id) -> {
//
//            String string = ""+autoCompleteTextView_san_contactos.getText().toString();
//            String[] parts = string.split("-");
//            String part1 = parts[0];
//
//            RecursosHumanos itemSelect=listaSan_contacto.get(Integer.parseInt(part1)-1);
//            autoCompleteTextView_san_contactos.setText(itemSelect.getNombres());
//            ID_RRHH=itemSelect.getId_rrhh();
//            StartActivity();
//        });
//        autoCompleteTextView_san_contactos.setAdapter(UtilView.LLENAR_SPINNER_SIMPLE( this,  strings));
//        //autoCompleteTextView_san_contactos.setEnabled(true);
    }

    private void RecuperarDatos(){
        int index=1;
        for (San_Visitas item:LISTA_VISITAS){
            if (index==1){
                LOCATION=new Location("");
                LOCATION.setLatitude(Double.parseDouble(item.getLatitud()));
                LOCATION.setLongitude(Double.parseDouble(item.getLongitud()));
                //PoblarSpinners(SpinnerTipoVisita, GlobalVar.SAN_OPCIONES.TIPO_VISITA, ""+item.getTipo_visita());
                PoblarSpinnersTIPO_VISITA( ""+item.getTipo_visita(), item.getActividad());
                //PoblarSpinners(SpinnerTipoActividad, GlobalVar.SAN_OPCIONES.TIPO_ACTIVIDD, ""+item.getActividad());
                et_comentario_visita.setText(item.getComentario_actividad());
                et_hora_inicio.setText(item.getHora_inicio_ejecución());
                et_hora_fin.setText(item.getHora_Fin_Ejecución());
                PoblarSpinners(SpinnerProximaActividad, GlobalVar.SAN_OPCIONES.TIPO_ACTIVIDD, "");
                DISTACIA_to_Colegio=item.getDistancia();
                txt_ubicacion.setText(""+ VARIABLES.OBTENER_DESCRIPCION_DIRECCIION_from_CORDENADA(this, LOCATION.getLatitude(),LOCATION.getLongitude())
                        +"\\nDistancia apróximada al colegio es "+calcularKM_and_Metros(DISTACIA_to_Colegio)+" metros");
                sw_isProximaVisita.setChecked(false);
                if (LISTA_VISITAS.size()==1) {
                    PoblarSpinnersTIPO_VISITA_NEXT( "", "");
                }

            }else {
                PoblarSpinnersTIPO_VISITA_NEXT( ""+item.getTipo_visita(), ""+ item.getActividad());
                et_fecha_proximavisita.setText(item.getFecha_planificada().replace(NO_HAY_PROXIMA_VISITA, ""));
                et_fecha_proximavisita.setText(item.getFecha_planificada().replace(NO_HAY_PROXIMA_VISITA, ""));
//                PoblarSpinners(SpinnerProximaActividad, GlobalVar.SAN_OPCIONES.TIPO_ACTIVIDD, ""+item.getActividad());
                et_comentario_proxima_visita.setText(item.getComentario_actividad());
                sw_isProximaVisita.setChecked(true);
            }
            index++;
        }

    }
    private String calcularKM_and_Metros(int metros_totales){
        int km=metros_totales/1000;
        int metro=metros_totales%1000;
        return km+" km, "+metro+" metros";
    }

    private void InHabilitarEjecucionVisita(){
        txt_ubicacion.setEnabled(false);
        et_hora_inicio.setEnabled(false);
        et_hora_fin.setEnabled(false);
        SpinnerTipoVisita.setEnabled(false);
        SpinnerTipoActividad.setEnabled(false);
        et_comentario_visita.setEnabled(false);

        tv_nombres.setVisibility(View.VISIBLE);
        autoCompleteTextView_san_contactos.setVisibility(View.GONE);
        autoCompleteTextView_san_contactos.setEnabled(false);

        UtilView.Efectos(this,et_hora_inicio, R.color.red_200);
        UtilView.Efectos(this,et_hora_fin, R.color.red_200);
        UtilView.Efectos(this,et_comentario_visita, R.color.red_200);
    }
    private void HabilitarCambioCantacto(boolean enabled){

        if (enabled){
            tv_nombres.setVisibility(View.GONE);
            autoCompleteTextView_san_contactos.setVisibility(View.VISIBLE);
            autoCompleteTextView_san_contactos.setEnabled(true);
        }else{
            tv_nombres.setVisibility(View.VISIBLE);
            autoCompleteTextView_san_contactos.setVisibility(View.GONE);
            autoCompleteTextView_san_contactos.setEnabled(false);
        }

    }

    private void InHabilitarProximoVisita(){
        et_fecha_proximavisita.setEnabled(false);
        SpinnerProximaTipoVisita.setEnabled(false);
        SpinnerProximaActividad.setEnabled(false);
        et_comentario_visita.setEnabled(false);
        et_comentario_proxima_visita.setEnabled(false);
        autoCompleteTextView_san_contactos.setEnabled(false);
        //FAB_enviar_visita.setEnabled(false);
        HabilitarCambioCantacto(false);
        UtilView.Efectos(this,et_fecha_proximavisita, R.color.red_200);
        UtilView.Efectos(this,et_comentario_visita, R.color.red_200);
        UtilView.Efectos(this,et_comentario_visita, R.color.red_200);

    }

    private void PoblarSpinners(Spinner spinnerLocal, String ClaveOpcion, String valorColegio){
        final ArrayList<String> listaValosInstancia= DAO_San_opciones.getListaOpcionesByInstancia(dBclasses,ClaveOpcion) ;
        spinnerLocal.setAdapter(UtilView.LLENAR_SPINNER_SIMPLE(this, listaValosInstancia));
        int index=0;
        for (int i=0; i<listaValosInstancia.size(); i++){
            if (valorColegio.equalsIgnoreCase(""+listaValosInstancia.get(i))){
                index=i;
                break;
            }
        }
        spinnerLocal.setSelection(index);
    }
    private void PoblarSpinnersDireccion(Spinner spinnerLocal, int itemDireccion){
        ArrayList<DB_DireccionClientes> dirs=dBclasses.getDireccionesxCliente(""+ID_RRHH);
        int index=0;
        int forInt=0;
        final ArrayList<String> listaString=new ArrayList<>();
        for (DB_DireccionClientes item:dirs){
            if (itemDireccion==Integer.parseInt(item.getItem())){
                index=forInt;
            }
            listaString.add(item.getItem()+"-"+item.getDireccion());
            forInt++;
        }
        spinnerLocal.setAdapter(UtilView.LLENAR_SPINNER_SIMPLE(this, listaString));
        spinnerLocal.setSelection(index);
    }
    private void PoblarSpinnersTIPO_VISITA(String valorSelected, final String tipo_actividad_colegio){
        final ArrayList<String> listaValosInstancia= DAO_San_opciones.getListaOpcionesByInstanciaByCodigo(dBclasses,GlobalVar.SAN_OPCIONES.TIPO_VISITA) ;
        SpinnerTipoVisita.setAdapter(UtilView.LLENAR_SPINNER_SIMPLE(this, listaValosInstancia));
        for (int i=0; i<listaValosInstancia.size(); i++){
            if (valorSelected.equalsIgnoreCase(""+listaValosInstancia.get(i))){
                SpinnerTipoVisita.setSelection(i);
                break;
            }
        }

        SpinnerTipoVisita.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerTipoVisita.setBackgroundResource(R.drawable.img_for_spinner);
                PoblarSpinners(SpinnerTipoActividad, getValorBySpinner(SpinnerTipoVisita), ""+tipo_actividad_colegio);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void PoblarSpinnersTIPO_VISITA_NEXT(String valorSelected, final String tipo_actividad_colegio){
        final Spinner spinnerLocal=SpinnerProximaTipoVisita;
        final ArrayList<String> listaValosInstancia= DAO_San_opciones.getListaOpcionesByInstanciaByCodigo(dBclasses,GlobalVar.SAN_OPCIONES.TIPO_VISITA) ;
        spinnerLocal.setAdapter(UtilView.LLENAR_SPINNER_SIMPLE(this, listaValosInstancia));
        for (int i=0; i<listaValosInstancia.size(); i++){
            if (valorSelected.equalsIgnoreCase(""+listaValosInstancia.get(i))){
                spinnerLocal.setSelection(i);
                break;
            }
        }

        spinnerLocal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerProximaTipoVisita.setBackgroundResource(R.drawable.img_for_spinner);
                PoblarSpinners(SpinnerProximaActividad, getValorBySpinner(spinnerLocal), ""+tipo_actividad_colegio);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    private void DatePicketCustom(){

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.DAY_OF_YEAR, 1);

        DatePickerDialog dialog = new DatePickerDialog(this, this,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        dialog.show();
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdformat = new SimpleDateFormat(myFormat, Locale.US);
        Calendar myCalendar = Calendar.getInstance();
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, month);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        et_fecha_proximavisita.setText(sdformat.format(myCalendar.getTime()));
    }

    private void GestionSpinner(final Spinner spinner){
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner.setBackgroundResource(R.drawable.img_for_spinner);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }



    private void obtenerHora(TextView editText){
        final String CERO = "0";
        final Calendar c = Calendar.getInstance();
        final int hora = c.get(Calendar.HOUR_OF_DAY);
        final int minuto = c.get(Calendar.MINUTE);

        CustomTimPicker recogerHora=new CustomTimPicker(this, new CustomTimPicker.OnTimeSetListener() {
            @Override
            public String onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String horaFormateada =  String.valueOf((hourOfDay < 10)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay));
                String minutoFormateada =  String.valueOf((minute < 10)? String.valueOf(CERO + minute) : String.valueOf(minute));
                horaFormateada=horaFormateada+":"+minutoFormateada;

                String mensaje="";
                try {
                    if (editText.getId()==et_hora_fin.getId()){
                        if (!TextUtils.isEmpty(et_hora_inicio.getText().toString())){
                            int segundosInicio=VARIABLES.GetSegundosFrom_hh_mm_to_int(et_hora_inicio.getText().toString());
                            int segundosFin=VARIABLES.GetSegundosFrom_hh_mm_to_int(horaFormateada);
                            if (segundosFin <= segundosInicio){
                                mensaje="Hora fin debe ser mayor a la hora de inicio.";
                            }
                        }else{
                            mensaje="Primero debes seleccionar la hora de inicio.";
                        }
                    }else if (editText.getId()==et_hora_inicio.getId()){
                        et_hora_fin.setText("");
                    }
                }catch (Exception e){
                    mensaje="No se ha podido validar lo hora de la reunión. Verifique que hayas ingresado valores correctos.";
                }

                if (mensaje.length()==0){
                    editText.setText(horaFormateada);
                }

                return mensaje;

            }


        },hora, minuto, true, true);

        recogerHora.Show();
    }

    @Override
    public void Location_cambiado(Location location, String textoDireccion, boolean isDireccion) {
        this.LOCATION=location;
        boolean isDistanciaMedido=false;
        try {
            String direccion=SpinnerDirecciones.getSelectedItem().toString();
            String [] dir=direccion.split("-");
            int item=Integer.parseInt(dir[0]);
            DAO_Cliente dao_cliente=new DAO_Cliente(GestionVisita3Activity.this);
            DB_DireccionClientes dircli=dao_cliente.getDireccionClienteByItem(""+ID_RRHH,""+item);
            //ArrayList<DB_DireccionClientes> dirCliente=dBclasses.getDireccionesxCliente(""+ID_RRHH);

            if (dircli==null){
                textoDireccion+="\nEste cliente con la dirección seleccionado aún no esta geolocalizado, por ello no podemos obtener la distancia entre el cliente y tu ubicación";
            }else{
                Location loca=new Location("");
                loca.setLatitude(Double.parseDouble(dircli.getLongitud()));
                loca.setLongitude(Double.parseDouble(dircli.getLongitud()));
                if (loca.getLatitude()!=0 && loca.getLongitude()!=0){
                    DISTACIA_to_Colegio =(int)LOCATION.distanceTo(loca);
                    textoDireccion+="\nDistancia apróximada al cliente es "+calcularKM_and_Metros(DISTACIA_to_Colegio);
                    isDistanciaMedido=true;
                }else {
                    isDistanciaMedido=false;
                    textoDireccion+="\nEste cliente con la dirección seleccionado aún no esta geolocalizado, por ello no podemos obtener la distancia entre el cliente y tu ubicación";
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            isDistanciaMedido=false;
            textoDireccion+="\nNo se ha podido medir la distancia entre el cliente y tu ubicación.";
        }


        Animation animLslide_right_in= AnimationUtils.loadAnimation(this, R.anim.slide_right_in);
        txt_ubicacion.startAnimation(animLslide_right_in);
        txt_ubicacion.setText(textoDireccion);
        if (isDireccion){
            if (isDistanciaMedido){
                txt_ubicacion.setTextColor(getResources().getColor(R.color.green_600));
            }else{
                txt_ubicacion.setTextColor(getResources().getColor(R.color.orange_700));
            }
        }else{
            txt_ubicacion.setText(txt_ubicacion.getText().toString()+"\nToque para reintentar.");
            txt_ubicacion.setTextColor(getResources().getColor(R.color.red_400));
        }
        txt_ubicacion.setOnClickListener(v -> {
            UBICACION=new ObtenerLocalizacion2(this);
        });

    }

    public  void GuardarGestionVisita(){

        int index=0;
        if (SpinnerTipoVisita.getSelectedItemPosition()<=0){
            index++;
            SpinnerTipoVisita.setBackgroundResource(R.drawable.img_for_spinner_pink);
        }
        if (SpinnerTipoActividad.getSelectedItemPosition()<=0){
            index++;
            SpinnerTipoActividad.setBackgroundResource(R.drawable.img_for_spinner_pink);
        }
        if (et_hora_inicio.getText().toString().length()<=0){
            index++;
            et_hora_inicio.setHintTextColor(getResources().getColor(R.color.pink_600));
        }
        if (et_hora_fin.getText().toString().length()<=0){
            index++;
            et_hora_fin.setHintTextColor(getResources().getColor(R.color.pink_600));
        }
        if (sw_isProximaVisita.isChecked()){

            if (et_fecha_proximavisita.getText().toString().length()<=0){
                index++;
                et_fecha_proximavisita.setHintTextColor(getResources().getColor(R.color.pink_600));
            }
            if (SpinnerProximaTipoVisita.getSelectedItemPosition()<=0){
                index++;
                SpinnerProximaTipoVisita.setBackgroundResource(R.drawable.img_for_spinner_pink);
            }
            if (SpinnerProximaActividad.getSelectedItemPosition()<=0){
                index++;
                SpinnerProximaActividad.setBackgroundResource(R.drawable.img_for_spinner_pink);
            }
        }
        if (index>0){
            UtilView.MENSAJE_simple(this, "Datos Vacios", "LLene todos los campos necesarios");
        }else{
            Enviar_Guardar_visitas();
        }
    }

    public  String calcularSecuencia(String oc) {
        String cero = "0";
        String orden = "";

        // obtengo la fecha de la tabla configuracion
        // String fecha_configuracion = dbclass.getCambio("Fecha");

        // String mes_actual=(calendar.get(Calendar.MONTH)+1)+"";
        // String dia_actual=calendar.get(Calendar.DAY_OF_MONTH)+"";
        String fecha_configuracionx = dBclasses.getCambio("Fecha");

        String mes_actual = fecha_configuracionx.substring(3, 5);
        String dia_actual = fecha_configuracionx.substring(0, 2);

        int secactual = 0;

        Log.d("calculando secuencia...", oc+"");

        if (oc.length() < 6) {
            Log.d("calculando secuencia...", oc+" < 6");
            secactual = 1;
            if (mes_actual.length() < 2) {
                mes_actual = cero + mes_actual;
            }
            if (dia_actual.length() < 2) {
                dia_actual = cero + dia_actual;
            }
            Log.d("calculando secuencia...","orden = "+mes_actual +"+"+ dia_actual +"+"+ cero +"+"+ secactual);

            orden = mes_actual + dia_actual + cero + secactual;
            return orden;
        } else {
            Log.d("calculando secuencia...", oc+" > 6");
            String cadenaFecha = oc.substring(oc.length()-6,oc.length());

            int mest = Integer.parseInt(cadenaFecha.substring(0, 2));
            int diat = Integer.parseInt(cadenaFecha.substring(2, 4));
            int sectem = Integer.parseInt(cadenaFecha.substring(4, 6));

            Log.d("calculando secuencia...","diat:"+diat);
            Log.d("calculando secuencia...","mest:"+mest);
            Log.d("calculando secuencia...","sectem:"+sectem);

            if (Integer.parseInt(mes_actual) <= mest) {
                Log.d("calculando secuencia...","mes_actual < mest");
                if (Integer.parseInt(dia_actual) > diat) {
                    secactual = 1;
                    Log.d("calculando secuencia...","secactual = 1;  ->"+secactual);
                } else
                    secactual = sectem + 1;
                Log.d("calculando secuencia...","secactual = sectem + 1; ->"+secactual);

            } else {
                secactual = 1;
                Log.d("calculando secuencia...","secactual = 1;");
            }
        }

        if (mes_actual.length() < 2) {
            mes_actual = cero + mes_actual;
        }
        if (dia_actual.length() < 2) {
            dia_actual = cero + dia_actual;
        }
        if (secactual < 10) {
            Log.d("calculando secuencia...","secactual < 10");
            orden = mes_actual + dia_actual + cero + secactual;
            Log.d("calculando secuencia...","orden = mes_actual + dia_actual + cero + secactual;");
            Log.d("calculando secuencia...","orden = "+mes_actual +"+"+ dia_actual +"+"+ cero +"+"+ secactual);
        } else {
            Log.d("calculando secuencia...","secactual > 10");
            Log.d("calculando secuencia...","orden = mes_actual + dia_actual + secactual");
            orden = mes_actual + dia_actual + secactual;
            Log.d("calculando secuencia...","orden = "+mes_actual +"+"+ dia_actual +"+"+ secactual);
        }

        return orden;
    }


    private void Enviar_Guardar_visitas(){


        ArrayList<San_Visitas> list=new ArrayList<>();
        //acerca de la visita
        String numOc=dBclasses.obtenerNumOC(COD_VEND);
        String fecha_ejecutada=""+VARIABLES.GET_FECHA_ACTUAL_STRING();
        String oc_numero_visitado="";
        String oc_numero_visitar="";
        String fecha_planificada=VARIABLES.GET_FECHA_ACTUAL_STRING();
        San_Visitas visita=new San_Visitas();
        if (ORIGEN.equals(ActividadCampoAgendadoActivity.TAG)){
            visita.setId(LISTA_VISITAS.get(0).getId());
            fecha_planificada=(LISTA_VISITAS.get(0).getFecha_planificada());
            if(!isPLANIFICADA){//ejecutada= solo actualización
                fecha_ejecutada=(LISTA_VISITAS.get(0).getFecha_Ejecutada());
                oc_numero_visitado=OC_NUMERO;
            }else{//isPLANIFICADA= actualización y creacion de proxima visita
                oc_numero_visitado=COD_VEND + calcularSecuencia(numOc);
                oc_numero_visitar=OC_NUMERO;
            }
        }else{
            oc_numero_visitado=COD_VEND + calcularSecuencia(numOc);
            visita.setOc_numero_visitado(oc_numero_visitado);
            oc_numero_visitar="";
        }
        visita.setOc_numero_visitado(oc_numero_visitado);
        visita.setOc_numero_visitar(oc_numero_visitar);
        visita.setGrupo_Campaña("Visitas");
        visita.setCod_Promotor(COD_VEND);
        visita.setPromotor(dBclasses.getNombreVendedor(COD_VEND));
        visita.setCod_Colegio(CODIGO_CRM);
        visita.setDescripcion_Colegio(NOMBRE_CRM);
        visita.setId_rrhh(ID_RRHH);
        visita.setEjecutivo_Descripcion(tv_nombres.getText().toString());
        visita.setCargo_Descripción("Sin cargo");
        visita.setFecha_Ejecutada(fecha_ejecutada);
        visita.setFecha_planificada(fecha_planificada);
        visita.setFecha_proxima_visita(et_fecha_proximavisita.getText().toString());
        visita.setHora_inicio_ejecución(et_hora_inicio.getText().toString());
        visita.setHora_Fin_Ejecución(et_hora_fin.getText().toString());
        visita.setFecha_de_modificación(VARIABLES.GET_FECHA_ACTUAL_STRING());
        visita.setEstado("Completada");
        visita.setTipo_visita(getValorBySpinner(SpinnerTipoVisita));
        visita.setActividad(getValorBySpinner(SpinnerTipoActividad));
        visita.setComentario_actividad(et_comentario_visita.getText().toString());
        visita.setActividad_Proxima(getValorBySpinner(SpinnerProximaActividad));
        visita.setLatitud((LOCATION!=null?LOCATION.getLatitude():0)+ "0");
        visita.setLongitud((LOCATION!=null?LOCATION.getLongitude():0)+ "0");
        visita.setDistancia(DISTACIA_to_Colegio);
        list.add(visita);
        visita=null;
        //fin------------------------
        //acerca de la proxima visita
        if (sw_isProximaVisita.isChecked()){
            San_Visitas visitaNEXT=new San_Visitas();
            if (LISTA_VISITAS.size()>1){
                visitaNEXT.setId(LISTA_VISITAS.get(1).getId());
                visitaNEXT.setOc_numero_visitado(oc_numero_visitar);
            }else{
                visitaNEXT.setOc_numero_visitado("");
            }
            visitaNEXT.setOc_numero_visitar(oc_numero_visitado);
            visitaNEXT.setGrupo_Campaña("Visitas");
            visitaNEXT.setCod_Promotor(COD_VEND);
            visitaNEXT.setPromotor(dBclasses.getNombreVendedor(COD_VEND));
            visitaNEXT.setCod_Colegio(CODIGO_CRM);
            visitaNEXT.setDescripcion_Colegio(NOMBRE_CRM);
            visitaNEXT.setId_rrhh(ID_RRHH);
            visitaNEXT.setEjecutivo_Descripcion(tv_nombres.getText().toString());
            visitaNEXT.setCargo_Descripción("Sin cargo");
//        visitaNEXT.setFecha_Ejecutada(VARIABLES.GET_DATE_AAA_MM_DD_from_DD_MM_AAAA(GlobalFunctions.getFecha_configuracion(getApplicationContext())));
            visitaNEXT.setFecha_Ejecutada("SIN FECHA");
            visitaNEXT.setFecha_planificada(et_fecha_proximavisita.getText().toString());
            visitaNEXT.setFecha_proxima_visita(NO_HAY_PROXIMA_VISITA);
            visitaNEXT.setHora_inicio_ejecución(SIN_HORA);
            visitaNEXT.setHora_Fin_Ejecución(SIN_HORA);
            visitaNEXT.setFecha_de_modificación(VARIABLES.GET_FECHA_ACTUAL_STRING());
            visitaNEXT.setEstado("Libre");
            visitaNEXT.setTipo_visita(""+getValorBySpinner(SpinnerProximaTipoVisita));
            visitaNEXT.setActividad(getValorBySpinner(SpinnerProximaActividad));
            visitaNEXT.setComentario_actividad(et_comentario_proxima_visita.getText().toString());
            visitaNEXT.setActividad_Proxima("");
            visitaNEXT.setLatitud("0");
            visitaNEXT.setLongitud("0");
            visitaNEXT.setDistancia(0);
            list.add(visitaNEXT);
        }

        guardarCabeceraPedido(oc_numero_visitado, list);

    }

    private  String getValorBySpinner(Spinner spinner){
        try {
            if (spinner.getSelectedItemPosition()>0){
                return   (spinner.getSelectedItem()!=null)?spinner.getSelectedItem().toString():"";
            }else return "";

        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

    public String guardarCabeceraPedido(String oc_numero, ArrayList<San_Visitas> list_visitas) {

        int item_direccion = Integer.parseInt("0");

        DB_ObjPedido itemCabecera=new DB_ObjPedido();
        itemCabecera.setOc_numero(oc_numero);

        itemCabecera.setFecha_oc(VARIABLES.GET_FECHA_ACTUAL_STRING_dd_mm_yyy2()+ " "+ GlobalFunctions.getHoraActual());
        itemCabecera.setMonto_total("0");
        itemCabecera.setSubtotal("0");
        itemCabecera.setPercepcion_total("0");
        itemCabecera.setFecha_mxe("");
        itemCabecera.setMoneda("");
        itemCabecera.setEstado("A");
        itemCabecera.setValor_igv("0");
        itemCabecera.setUsername(dBclasses.getNombreUsuarioXcodvend(COD_VEND));
        itemCabecera.setRuta("ruta");
        itemCabecera.setCond_pago("");
        itemCabecera.setCod_cli(ID_RRHH);
        itemCabecera.setCod_emp(COD_VEND);
        itemCabecera.setObserv("");
        itemCabecera.setCod_noventa(GlobalVar.CODIGO_VISITA_CLIENTE);
        itemCabecera.setPeso_total("0.0");
        itemCabecera.setFlag("P");
        itemCabecera.setLatitud((LOCATION!=null?LOCATION.getLatitude():0)+ "");
        itemCabecera.setLongitud((LOCATION!=null?LOCATION.getLongitude():0)+ "");
        itemCabecera.setSitio_enfa("" + item_direccion);
        itemCabecera.setTipoRegistro("");
        itemCabecera.setTotalSujetoPercepcion("0");

        itemCabecera.setFlagDespacho("");
        itemCabecera.setCodigoSucursal("0");
        itemCabecera.setNumeroOrdenCompra("0");
        itemCabecera.setCodigoPrioridad("0");
        itemCabecera.setCodigoPuntoEntrega("0");
        itemCabecera.setCodigoTipoDespacho("0");
        itemCabecera.setCodigoSucursal("0");
        itemCabecera.setFlagEmbalaje("0");
        itemCabecera.setFlagPedido_Anticipo("0");
        itemCabecera.setCodigoTransportista("0");
        itemCabecera.setCodigoAlmacen("0");
        itemCabecera.setObservacion2("");
        itemCabecera.setObservacion3("");
        itemCabecera.setObservacionDescuento("");
        itemCabecera.setObservacionTipoProducto("");



        itemCabecera.setSan_visitas(list_visitas);

        GuardarVisitaLocal(itemCabecera);

        return itemCabecera.getOc_numero();
    }
    private void  GuardarVisitaLocal(DB_ObjPedido itemCabecera){
        SQLiteDatabase __DB=dBclasses.getWritableDatabase();
        __DB.beginTransaction();
        boolean isInsert=false;
        if (ORIGEN.equalsIgnoreCase(ActividadCampoAgendadoActivity.TAG)){
            if(isPLANIFICADA){//isPLANIFICADA= actualización y creacion de proxima visita
                isInsert=dBclasses.AgregarPedidoCabecera_raiz(itemCabecera, __DB);
            }else
                isInsert=true;
        }else{
            isInsert=dBclasses.AgregarPedidoCabecera_raiz(itemCabecera, __DB);
        }

        if (isInsert){
            if (ORIGEN.equalsIgnoreCase(ActividadCampoAgendadoActivity.TAG)){
                if(!isPLANIFICADA){//ejecutada= solo actualización
                    isInsert=DAO_San_Visitas.UpdateListaVisitaEjecucion( __DB, itemCabecera.getSan_visitas());
                }else{//isPLANIFICADA= actualización y creacion de proxima visita
                    isInsert=DAO_San_Visitas.UpdateVisitaEjecucion( __DB,  itemCabecera.getSan_visitas().get(0));
                    if(isInsert){
                        isInsert=DAO_San_Visitas.LlenarTabla_San_Visitas( __DB,  itemCabecera.getSan_visitas().get(1));
                    }
                }
            }else{
                isInsert=DAO_San_Visitas.LlenarlistaTabla_San_Visitas( __DB,  itemCabecera.getSan_visitas());
            }
            String titulo="";
            String mensaje="";
            if (isInsert){
                titulo="Datos guardados";
                mensaje="Se registró la visita correctamente";
                __DB.setTransactionSuccessful();

                EnviarSanVisitas(itemCabecera.getOc_numero());
            }
            else{
                titulo="Error";
                mensaje="No se ha registrado la visita.\nVuelva a intentar";
                UtilView.MENSAJE_simple(GestionVisita3Activity.this,
                    ""+titulo, ""+mensaje);

            }
//            UtilView.MENSAJE_simple_finish_return_intent(GestionVisita3Activity.this,
//                    ""+titulo, ""+mensaje, isInsert);

        }else{
            UtilView.MENSAJE_simple(GestionVisita3Activity.this, "Error", "No se pudo registrar los datos en la cabecera. \nVuelva a intentar");
        }
        __DB.endTransaction();
        __DB.close();
    }


    private void EnviarSanVisitas(final String oc_numero){
        ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Enviando datos al sistema....");
        pDialog.setIndeterminate(false);

        pDialog.show();
        DBSync_soap_manager soap_manager = new DBSync_soap_manager(GestionVisita3Activity.this);
        new AsyncTask<Void, Void, String>(){
            @Override
            protected String doInBackground(Void... voids) {

                try {
                    ConnectionDetector cd = new ConnectionDetector(getApplicationContext());

                    if (cd.hasActiveInternetConnection(getApplicationContext())) {
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
                            UtilView.MENSAJE_simple_finish_return_intent(GestionVisita3Activity.this,
                    ""+titulo, ""+mensaje, true);

            }


        }.execute();
    }
}
