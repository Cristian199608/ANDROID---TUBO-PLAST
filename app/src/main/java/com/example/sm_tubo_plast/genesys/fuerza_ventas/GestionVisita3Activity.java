package com.example.sm_tubo_plast.genesys.fuerza_ventas;


import android.app.DatePickerDialog;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.BEAN.Cliente_Contacto;
import com.example.sm_tubo_plast.genesys.BEAN.San_Visitas;
import com.example.sm_tubo_plast.genesys.DAO.DAO_Cliente;
import com.example.sm_tubo_plast.genesys.DAO.DAO_Cliente_Contacto;
import com.example.sm_tubo_plast.genesys.DAO.DAO_San_Visitas;
import com.example.sm_tubo_plast.genesys.DAO.DAO_San_opciones;
import com.example.sm_tubo_plast.genesys.hardware.LocationApiGoogle;
import com.example.sm_tubo_plast.genesys.hardware.Permiso_Adroid;
import com.example.sm_tubo_plast.genesys.hardware.RequestPermisoUbicacion;
import com.example.sm_tubo_plast.genesys.hardware.TaskCheckUbicacion;
import com.example.sm_tubo_plast.genesys.service.WS_Cliente_Contacto;
import com.example.sm_tubo_plast.genesys.service.WS_San_Visitas;
import com.example.sm_tubo_plast.genesys.datatypes.DBClientes;
import com.example.sm_tubo_plast.genesys.datatypes.DBPedido_Cabecera;
import com.example.sm_tubo_plast.genesys.datatypes.DB_DireccionClientes;
import com.example.sm_tubo_plast.genesys.datatypes.DB_ObjPedido;
import com.example.sm_tubo_plast.genesys.datatypes.DBclasses;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.CustomView.CrearCliente_Contacto;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.Reportes.ReportesPedidosCotizacionYVisitaActivity;
import com.example.sm_tubo_plast.genesys.util.CustomDateTimePicker;
import com.example.sm_tubo_plast.genesys.util.CustomTimPicker;
import com.example.sm_tubo_plast.genesys.util.GlobalFunctions;
import com.example.sm_tubo_plast.genesys.util.GlobalVar;
import com.example.sm_tubo_plast.genesys.util.UtilView;
import com.example.sm_tubo_plast.genesys.util.UtilViewMensaje;
import com.example.sm_tubo_plast.genesys.util.VARIABLES;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class GestionVisita3Activity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private static final String TAG="GestionVisita3Activity";
    private static final String SIN_FECHA="SIN FECHA";
    private static final String SIN_HORA="SIN HORA";
    private static final String NO_HAY_PROXIMA_VISITA="NO HAY PROXIMA VISITA";

    public static final String PROGRAMACION_VISITA="PROGRAMACION_VISITA";
    public static final String RE_PROGRAMACION_VISITA="RE_PROGRAMACION_VISITA";
    public static final String VISITA_PLANIFICADA="VISITA_PLANIFICADA";
    public static final String MODIFICAR_PROGRAMACION="MODIFICAR_PROGRAMACION";
    public static final String MODIFICAR_VISITA="MODIFICAR_VISITA";
    public static final String GESTIONAR_VISITA="GESTIONAR_VISITA";

    public static final String ESTADO_VISITA_COMPLETADA="Completada";
    public static final String ESTADO_VISITA_Libre="Libre";
    public static final String OC_NUMERO_REPROGRAMADO="RE-";



    DBclasses dBclasses=null;
    String ORIGEN="";
    String TIPO_GESTION="";
    String ID_RRHH="";
    ArrayList<San_Visitas> LISTA_VISITAS=new ArrayList<>();
    String FINAL_OC_NUMERO=null;
    String CODIGO_CRM="";
    String COD_VEND="";
    String NOMBRE_CRM="";
    EditText et_motivo_reporgramacion, tv_nombres, et_Obervacion, et_comentario_visita, et_comentario_proxima_visita;
    TextView  txt_oc_numero, et_fecha_visita_manual, et_fecha_proximavisita,et_hora_inicio, et_hora_fin, txt_ubicacion, et_hora_inicio_next;
    Spinner SpinnerTipoVisita, SpinnerTipoActividad, SpinnerProximaTipoVisita, SpinnerProximaActividad,SpinnerDirecciones, SpinnerContacto;
    FloatingActionButton FAB_enviar_visita;

    Switch  sw_isProximaVisita;
    CheckBox check_validar_fehca_visita;
    LinearLayout layout_container_prroima_visita;



    LocationApiGoogle locationApiGoogle;
    TaskCheckUbicacion taskCheckUbicacion;

    Location LOCATION;
    int DISTACIA_to_Colegio=-1;
    String fechaOcOriginal=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_visita3);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            
            ID_RRHH=bundle.getString("ID_RRHH",  "");
            FINAL_OC_NUMERO=bundle.getString("OC_NUMERO",  null);
            CODIGO_CRM=bundle.getString("CODIGO_CRM",  null);
            NOMBRE_CRM=bundle.getString("NOMBRE_INSTI",  "Sin Nombre");
            COD_VEND=bundle.getString("COD_VEND",  null);
            ORIGEN=bundle.getString("ORIGEN",  null);
            TIPO_GESTION=bundle.getString("TIPO_GESTION",  null);
            if (ID_RRHH.length()!=0 && FINAL_OC_NUMERO!=null && CODIGO_CRM!=null && COD_VEND!=null && ORIGEN!=null && TIPO_GESTION!=null){
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

    protected void onPause() {
        super.onPause();
        if (taskCheckUbicacion!=null){
            taskCheckUbicacion.RemoveLocation();
        }
    }

    protected void onResume() {
        super.onResume();
        if (!LocationApiGoogle.checkPlayServices(this, Permiso_Adroid.PLAY_SERVICES_RESOLUTION_REQUEST)) {
            UtilViewMensaje.MENSAJE_simple(this, "Google Play", "Necesitas instalar Google Play Services para usar las ubicaciones de los clientes ");
        }else {
            if (taskCheckUbicacion!=null){
                taskCheckUbicacion.RequestLocationUpdates();
            }
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (locationApiGoogle!=null){
            if (locationApiGoogle.fusedLocationClient!=null && locationApiGoogle.locationCallback!=null){
                locationApiGoogle.fusedLocationClient.removeLocationUpdates(locationApiGoogle.locationCallback);
            }
        }
        if (taskCheckUbicacion!=null){
            taskCheckUbicacion.RemoveLocation();
        }

    }
    private void Star_Check_Permiso_Ubicacion(){
        new RequestPermisoUbicacion(this, Permiso_Adroid.PERMISO_PARA_ACCEDER_A_LOCALIZACION, new RequestPermisoUbicacion.MyListener() {
            @Override
            public void Result(int isConcedido) {
                if (Permiso_Adroid.IS_PERMISO_DENEGADO==isConcedido){
                    UtilViewMensaje.MENSAJE_simple(GestionVisita3Activity.this, "Permiso denegado", "No podras acceder a la ubicación");
                }
                else if (Permiso_Adroid.IS_PERMISO_CONCEDIDO==isConcedido){
                    StartUbicacionApiGoogle();
                }
            }
        });
    }
    private void StartUbicacionApiGoogle(){
        locationApiGoogle=new LocationApiGoogle(this, new LocationApiGoogle.Listener() {
            @Override
            public void onConnected(Bundle bundle) {

                taskCheckUbicacion= new TaskCheckUbicacion(GestionVisita3Activity.this, new TaskCheckUbicacion.MyListener() {
                    @Override
                    public void result(boolean isOk) {
                        if (isOk) {
                            locationApiGoogle.ForzarUltimaUbicacion();
                            locationApiGoogle.StartLocationCallback(LocationApiGoogle.UPDATE_INTERVAL_3_segundos, LocationApiGoogle.FASTEST_INTERVAL_3_segundos, LocationRequest.PRIORITY_HIGH_ACCURACY);

                        }else{
                            locationApiGoogle.checkGPSActivate();
                        }
                    }
                });

            }

            @Override
            public void onConnectionSuspended(int i) {
                Location_cambiado_2(null, "Conexión a google maps esta supendida", false);
            }

            @Override
            public void onConnectionFailed(ConnectionResult location) {
                Location_cambiado_2(null, "No se pudo conectarse a google maps", false);
            }

            @Override
            public void LastLocation(Location location) {
                LOCATION=location;
                if (location!=null){
                    Log.i(TAG, "StartUbicacionApiGoogle:: LastLocation:: Latitude : " + location.getLatitude() + "Longitude : " + location.getLongitude());
                }else{
                    Toast.makeText(GestionVisita3Activity.this, "Error, no se pudo obtener la ubicación", Toast.LENGTH_SHORT).show();
                }
                String direccion_name=VARIABLES.OBTENER_DESCRIPCION_DIRECCIION_from_CORDENADA(GestionVisita3Activity.this, LOCATION.getAltitude(), LOCATION.getLongitude());
                Location_cambiado_2(LOCATION, direccion_name, true);
            }
        });
        locationApiGoogle.ApiLocationGoogleConectar();
    }


    private void StartActivity() {
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        tv_nombres=findViewById(R.id.tv_nombres);

        txt_oc_numero=findViewById(R.id.txt_oc_numero);
        SpinnerTipoVisita=findViewById(R.id.SpinnerTipoVisita);
        SpinnerTipoActividad=findViewById(R.id.SpinnerTipoActividad);
        et_comentario_visita=findViewById(R.id.et_comentario_visita);
        txt_ubicacion=findViewById(R.id.txt_ubicacion);
        et_fecha_visita_manual=findViewById(R.id.et_fecha_visita_manual);
        et_hora_inicio=findViewById(R.id.et_hora_inicio);
        et_hora_fin=findViewById(R.id.et_hora_fin);
        et_fecha_proximavisita=findViewById(R.id.et_fecha_proximavisita);
        et_motivo_reporgramacion=findViewById(R.id.et_motivo_reporgramacion);
        SpinnerProximaTipoVisita=findViewById(R.id.SpinnerProximaTipoVisita);
        SpinnerProximaActividad=findViewById(R.id.SpinnerProximaActividad);
        SpinnerDirecciones=findViewById(R.id.SpinnerDirecciones);
        et_comentario_proxima_visita=findViewById(R.id.et_comentario_proxima_visita);
        check_validar_fehca_visita=findViewById(R.id.check_validar_fehca_visita);
        sw_isProximaVisita=findViewById(R.id.sw_isProximaVisita);
        et_hora_inicio_next=findViewById(R.id.et_hora_inicio_next);
        layout_container_prroima_visita=findViewById(R.id.layout_container_prroima_visita);
        FAB_enviar_visita=findViewById(R.id.FAB_enviar_visita);
        SpinnerContacto=findViewById(R.id.SpinnerContacto);
        TextView label_reprogramacion=findViewById(R.id.label_reprogramacion);

        label_reprogramacion.setVisibility(View.GONE);
        et_motivo_reporgramacion.setVisibility(View.GONE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (TIPO_GESTION.equals(VISITA_PLANIFICADA) || TIPO_GESTION.equals(GESTIONAR_VISITA) ) {
            getSupportActionBar().setTitle("GESTION DE VISITA");
        }
        else if (TIPO_GESTION.equals(MODIFICAR_PROGRAMACION)) {
            getSupportActionBar().setTitle("MODIFICACION DE LA PROX. VISITA");
        }
        else if (TIPO_GESTION.equals(MODIFICAR_VISITA)) {
            getSupportActionBar().setTitle("MODIFICACION DE LA VISITA");
        }

        else if (TIPO_GESTION.equals(PROGRAMACION_VISITA)) {
            getSupportActionBar().setTitle("PROGRAMACION DE LA VISITA");
        }
        else if (TIPO_GESTION.equals(RE_PROGRAMACION_VISITA)) {

                getSupportActionBar().setTitle("REPROGRAMACION DE LA VISITA");
                label_reprogramacion.setVisibility(View.VISIBLE);
                et_motivo_reporgramacion.setVisibility(View.VISIBLE);
                Motivo_reprogramacion();
                //label_reprogramacion

        }else{
            getSupportActionBar().setTitle("GESTION VISITA");
        }


        txt_ubicacion.setText("Obteniendo ubicación...");
        dBclasses = new DBclasses(this);

        HabilitarCambioCantacto(false);
        if (ORIGEN.equalsIgnoreCase(ActividadCampoAgendadoActivity.TAG) || ORIGEN.equalsIgnoreCase(ReportesPedidosCotizacionYVisitaActivity.TAG)){

            LISTA_VISITAS= DAO_San_Visitas.GetVisitasByOc_numero(dBclasses, FINAL_OC_NUMERO, TIPO_GESTION);

            if (ID_RRHH.equals("-1")){
                ID_RRHH=LISTA_VISITAS.get(0).getId_rrhh();
                InHabilitarAllEjecucionVisita();
                InHabilitarProximoVisita();
            }
        }

        DBClientes cliente= dBclasses.getClientexCodigo(""+ID_RRHH);
        //ArrayList<RecursosHumanos> listarrhh=SQ_LITE.getReccursosHumanos_x_INSTI_and_rrhh(""+CODIGO_CRM, 0+ID_RRHH);

        if (cliente!=null){

            tv_nombres.setText(cliente.getNomcli());
        }
        et_hora_inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                int limit_min_minuto=-1;
                if (TIPO_GESTION.equals(VISITA_PLANIFICADA)) {
                    String valor=dBclasses.getConfiguracionByName("visita_minutos_retraso", "-1");
                    limit_min_minuto =Integer.parseInt(valor);//convertimos en segundos con (* 60)
                }
                obtenerHora(et_hora_inicio, limit_min_minuto);
            }
        });

        et_hora_fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                GestionVisita3Activity.this.obtenerHora(et_hora_fin, -1);
            }
        });
        et_fecha_visita_manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                GestionVisita3Activity.this.CustomDateTimePicker(et_fecha_visita_manual,false);
            }
        });

        et_fecha_proximavisita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                if (TIPO_GESTION.equals(GESTIONAR_VISITA) || TIPO_GESTION.equals(VISITA_PLANIFICADA)|| TIPO_GESTION.equals(RE_PROGRAMACION_VISITA) || et_fecha_proximavisita.getText().toString().length()==0){
                    GestionVisita3Activity.this.CustomDateTimePicker(et_fecha_proximavisita,false);
                }else{
                    UtilViewMensaje.MENSAJE_simple(GestionVisita3Activity.this, "Modificar fecha?", "Para cambiar la fecha debe elegir la opión reprogramar.");
                }

            }
        });

        et_hora_inicio_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                GestionVisita3Activity.this.obtenerHora(et_hora_inicio_next,-1);
            }
        });
        check_validar_fehca_visita.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked){
                    et_hora_inicio.setEnabled(true);
                    return;
                }
                int index=0;
                if (TextUtils.isEmpty(et_fecha_visita_manual.getText().toString())){
                    index++;
                    et_fecha_visita_manual.setHintTextColor(getResources().getColor(R.color.pink_600));
                }

                if (TextUtils.isEmpty(et_hora_inicio.getText().toString())){
                    index++;
                    et_hora_inicio.setHintTextColor(getResources().getColor(R.color.pink_600));
                }
                if (index>0){
                    check_validar_fehca_visita.setChecked(false);
                    return;
                }

                WS_San_Visitas ws_san_visitas=new WS_San_Visitas(GestionVisita3Activity.this, dBclasses);
                ws_san_visitas.getFecha_servidor(new WS_San_Visitas.MyListener() {
                    @Override
                    public void fecha_servidor(String fecha_server) {
                        if (fecha_server==null){
                            check_validar_fehca_visita.setChecked(false);
                            return;
                        }
                        long minutos_retraso=60000*2;

                        long fecha_server_long=VARIABLES.convertirFechaHora_dd_mm_yyyy__HH_mm_to_long(fecha_server);
                        long fecha_celular=VARIABLES.getFechaHora_actual_long();
                        long diferencia=fecha_server_long-fecha_celular;
                        if (diferencia>minutos_retraso || diferencia<-minutos_retraso){
                            UtilViewMensaje.MENSAJE_simple(GestionVisita3Activity.this,
                                    "Fecha inválida",
                                    "La fecha y hora del celular no son correctas. " +
                                            "\n\nHora Celular : "+VARIABLES.convertirFecha_from_long(fecha_celular)+
                                            "\nHora Servidor : "+VARIABLES.convertirFecha_from_long(fecha_server_long));
                            et_hora_inicio.setText("");
                            check_validar_fehca_visita.setChecked(false);
                        }else{
                            et_hora_inicio.setEnabled(false);
                        }
                    }
                });

            }
        });

        GetionSwitchProximaVisita();
        String numOc=dBclasses.obtenerMaxNumOc(COD_VEND);
        if (ORIGEN.equalsIgnoreCase(ActividadCampoAgendadoActivity.TAG) || ORIGEN.equalsIgnoreCase(ReportesPedidosCotizacionYVisitaActivity.TAG)){

            RecuperarDatos();
            if(TIPO_GESTION.equals(VISITA_PLANIFICADA)) {//es visita
                et_fecha_visita_manual.setEnabled(false);
                txt_oc_numero.setText(COD_VEND + calcularSecuencia(numOc));
                Star_Check_Permiso_Ubicacion();
                PoblarSpinnersTIPO_VISITA_NEXT( "", "");
                HabilitarCambioCantacto(true);
                GestionSanContactos();
            }else{//ejecutada= solo actualización

                if (TIPO_GESTION.equals(RE_PROGRAMACION_VISITA) || TIPO_GESTION.equals(MODIFICAR_PROGRAMACION)){
                    txt_ubicacion.setVisibility(View.GONE);
                    HabilitarCamposParaProgrmarNextVisita();
                    if (TIPO_GESTION.equals(MODIFICAR_PROGRAMACION)){
                        et_fecha_visita_manual.setEnabled(false);
                        txt_oc_numero.setText(FINAL_OC_NUMERO);
                    }else{

                        txt_oc_numero.setText(COD_VEND + calcularSecuencia(numOc));
                    }
                }else{
                    txt_ubicacion.setVisibility(View.VISIBLE);
                    txt_oc_numero.setText(FINAL_OC_NUMERO);
                    InHabilitarAllEjecucionVisita();
                    HabilitarEdicionEstaVisita(true);
                }
            }
            DBPedido_Cabecera dbPedido_cabecera=dBclasses.getPedido_cabecera(FINAL_OC_NUMERO);
            fechaOcOriginal=dbPedido_cabecera.getFecha_oc();
            int sitio_enfa=0;
            if (dbPedido_cabecera!=null){
                sitio_enfa=Integer.parseInt(dbPedido_cabecera.getSitio_enfa());
            }
            PoblarSpinnersDireccion(SpinnerDirecciones, sitio_enfa);

        }else{
            txt_oc_numero.setText(COD_VEND + calcularSecuencia(numOc));
            PoblarSpinnersDireccion(SpinnerDirecciones, 0);
            PoblarSpinnersTIPO_VISITA( "", "");
            PoblarSpinnersCliente_Contacto( 0);

            if (TIPO_GESTION.equals(PROGRAMACION_VISITA) || TIPO_GESTION.equals(MODIFICAR_PROGRAMACION)){
                HabilitarCamposParaProgrmarNextVisita();
                txt_ubicacion.setVisibility(View.GONE);

            }else{
                et_fecha_visita_manual.setText(VARIABLES.GET_FECHA_ACTUAL_STRING());
                et_fecha_visita_manual.setEnabled(false);
                Star_Check_Permiso_Ubicacion();
                PoblarSpinnersTIPO_VISITA_NEXT( "", "");
                PoblarSpinners(SpinnerProximaActividad, GlobalVar.SAN_OPCIONES.TIPO_ACTIVIDD, "");
            }
        }



        //GestionSpinner(SpinnerTipoVisita);
        GestionSpinner(SpinnerTipoActividad);
        GestionSpinner(SpinnerProximaActividad);
        FAB_enviar_visita.setOnClickListener(V->GuardarGestionVisita());
    }
    private void Motivo_reprogramacion(){
        UtilView.AlertViewSimpleConEdittext dd=new UtilView.AlertViewSimpleConEdittext(this);
        dd.titulo="Reprogramación";
        dd.mensaje="Ingrese la descripción del motivo";
        dd.min_caracteres=10;
        dd.hint="Ingrese al menos "+dd.min_caracteres+" caracteres";
        dd.texto_cargado="";
        dd.cancelable=false;
        dd.start(new UtilView.AlertViewSimpleConEdittext.Listener() {
            @Override
            public String resultOK(String descripcion) {
                if (descripcion==null){
                    dd.FinalizarActivity();
                    return null;
                }
                et_motivo_reporgramacion.setText(descripcion);
                return null;
            }

            @Override
            public String resultBucle(String s) {
                Motivo_reprogramacion();
                return null;
            }
        });
    }
    private void FINISH(){
        finish();
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
                et_fecha_visita_manual.setText(item.getFecha_planificada().substring(0,10));
                et_comentario_visita.setText(item.getComentario_actividad());
                if (TIPO_GESTION.equals(VISITA_PLANIFICADA)) {
                    getSupportActionBar().setSubtitle("Inicio "+item.getHora_inicio_ejecución()+" hrs, fin "+item.getHora_Fin_Ejecución()+" hrs");
                }else{
                    et_hora_inicio.setText(item.getHora_inicio_ejecución());
                    et_hora_fin.setText(item.getHora_Fin_Ejecución());
                }

                PoblarSpinners(SpinnerProximaActividad, GlobalVar.SAN_OPCIONES.TIPO_ACTIVIDD, "");
                DISTACIA_to_Colegio=item.getDistancia();
                txt_ubicacion.setText(""+ VARIABLES.OBTENER_DESCRIPCION_DIRECCIION_from_CORDENADA(this, LOCATION.getLatitude(),LOCATION.getLongitude())
                        +"\nDistancia apróximada al cliente es "+calcularKM_and_Metros(DISTACIA_to_Colegio)
                        +" \nALTITUD: "+VARIABLES.formater_thow_decimal.format(item.getAltitud())+" m.s.n.m");

                sw_isProximaVisita.setChecked(TIPO_GESTION.equals(VISITA_PLANIFICADA));
                if (LISTA_VISITAS.size()==1) {
                    PoblarSpinnersTIPO_VISITA_NEXT( "", "");
                }
                PoblarSpinnersCliente_Contacto( item.getId_contacto());

            }else {
                PoblarSpinnersTIPO_VISITA_NEXT( ""+item.getTipo_visita(), ""+ item.getActividad());
                if (item.getFecha_planificada().contains(NO_HAY_PROXIMA_VISITA)){
                    et_fecha_proximavisita.setText(item.getFecha_planificada().replace(NO_HAY_PROXIMA_VISITA, ""));
                }else{
                    et_fecha_proximavisita.setText(item.getFecha_planificada().substring(0,10));
                }
                et_hora_inicio_next.setText(item.getHora_inicio_ejecución().replace(SIN_HORA, ""));
//                PoblarSpinners(SpinnerProximaActividad, GlobalVar.SAN_OPCIONES.TIPO_ACTIVIDD, ""+item.getActividad());
                et_comentario_proxima_visita.setText(item.getComentario_actividad());
                sw_isProximaVisita.setChecked(true);
                sw_isProximaVisita.setEnabled(false);
            }
            index++;
        }

    }
    private String calcularKM_and_Metros(int metros_totales){
        int km=metros_totales/1000;
        int metro=metros_totales%1000;
        return km+" km, "+metro+" metros";
    }

    private void InHabilitarAllEjecucionVisita(){
        txt_ubicacion.setEnabled(false);
        et_hora_inicio.setEnabled(false);
        et_hora_fin.setEnabled(false);
        SpinnerTipoVisita.setEnabled(false);
        SpinnerTipoActividad.setEnabled(false);
        et_comentario_visita.setEnabled(false);
        SpinnerDirecciones.setEnabled(false);
        SpinnerContacto.setEnabled(false);
        et_fecha_visita_manual.setEnabled(false);

        tv_nombres.setVisibility(View.VISIBLE);


        UtilView.Efectos(this,et_hora_inicio, R.color.red_200);
        UtilView.Efectos(this,et_hora_fin, R.color.red_200);
        //UtilView.Efectos(this,et_comentario_visita, R.color.red_200);
    }
    private void HabilitarEdicionEstaVisita(boolean enabled){

        et_hora_fin.setEnabled(enabled);
        SpinnerContacto.setEnabled(true);
        et_comentario_visita.setEnabled(enabled);

    }
    private void  HabilitarCamposParaProgrmarNextVisita(){
        sw_isProximaVisita.setChecked(false);
        sw_isProximaVisita.setEnabled(false);
        sw_isProximaVisita.setVisibility(View.GONE);
        layout_container_prroima_visita.setVisibility(View.GONE);
    }
    private void HabilitarCambioCantacto(boolean enabled){
        tv_nombres.setVisibility(View.VISIBLE);
    }

    private void InHabilitarProximoVisita(){
        et_fecha_proximavisita.setEnabled(false);
        SpinnerProximaTipoVisita.setEnabled(false);
        SpinnerProximaActividad.setEnabled(false);
        et_comentario_visita.setEnabled(false);
        et_comentario_proxima_visita.setEnabled(false);

        SpinnerDirecciones.setEnabled(false);
        FAB_enviar_visita.setVisibility(View.GONE);
        sw_isProximaVisita.setEnabled(false);
        //FAB_enviar_visita.setEnabled(false);
        HabilitarCambioCantacto(false);
        UtilView.Efectos(this,et_fecha_proximavisita, R.color.red_200);
        UtilView.Efectos(this,et_comentario_visita, R.color.red_200);
        UtilView.Efectos(this,et_comentario_proxima_visita, R.color.red_200);

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


    private void PoblarSpinnersCliente_Contacto(int id_contacto){
        DAO_Cliente_Contacto dao_cliente_contacto=new DAO_Cliente_Contacto();

        ArrayList<Cliente_Contacto> lista=dao_cliente_contacto.getClienteContactoByID(dBclasses, ID_RRHH, 0);
        ArrayList<String> listaString=new ArrayList<>();
        listaString.add("0-Sin Contacto");
        listaString.add("0-Agregar +");
        int posSelected=0;
        for (int i=0; i<lista.size(); i++){
            if (id_contacto == lista.get(i).getId_contacto()){
                Log.i(TAG, "id_contacto buscado es "+id_contacto+" y contacto encontrado en pos i = "+i);
                posSelected+=(listaString.size());
            }
            listaString.add(lista.get(i).getId_contacto()+"-"+lista.get(i).getNombre_contacto());
        }


        SpinnerContacto.setAdapter(UtilView.LLENAR_SPINNER_SIMPLE(this, listaString));
        SpinnerContacto.setSelection(posSelected);

        SpinnerContacto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerContacto.setBackgroundResource(R.drawable.img_for_spinner);
                if (position==1){

                    CrearCliente_Contacto cc=new CrearCliente_Contacto(GestionVisita3Activity.this, dBclasses);
                    cc.VistaCreate(ID_RRHH, contacto -> {
                        if (contacto==null){
                            SpinnerContacto.setSelection(0);
                        }else{
                            PoblarSpinnersCliente_Contacto(contacto.getId_contacto());
                            WS_Cliente_Contacto ws_cliente_contacto=new WS_Cliente_Contacto(GestionVisita3Activity.this, dBclasses);
                            ws_cliente_contacto.EnviarClienteContactoPendienteByCliente(ID_RRHH);
                        }

                    });
                }

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


    private void CustomDateTimePicker(TextView vista_setear,final boolean habilitar_hora){

        final String CERO = "0";
        final Calendar c = Calendar.getInstance();
        final int hora = c.get(Calendar.HOUR_OF_DAY);
        final int minuto = c.get(Calendar.MINUTE);

        CustomDateTimePicker recogerHora=new CustomDateTimePicker(this, new CustomDateTimePicker.OnTimeSetListener() {
            @Override
            public String onDateTimeSet(Calendar myCalendar,String fecha_formateado) {

                String mensaje="";
                try {

                    String myFormat = "yyyy-MM-dd";
                    if (habilitar_hora){
                        myFormat = "yyyy-MM-dd HH:mm"; //In which you need put here
                    }
                    SimpleDateFormat sdformat = new SimpleDateFormat(myFormat, Locale.US);
//VARIABLES
                    vista_setear.setText(sdformat.format(myCalendar.getTime()));

                }catch (Exception e){
                    mensaje="No se ha podido validar la fecha de la programación. Verifique que hayas ingresado valores correctos.";
                }
                return mensaje;
            }
        },hora, minuto, true, habilitar_hora, true);
        recogerHora.Show();
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



    private void obtenerHora(TextView editText, final int limit_min_minuto){
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
        recogerHora.setLimit_min_minuto(limit_min_minuto);
        if (editText.getId()==et_hora_inicio.getId() && TIPO_GESTION.equals(VISITA_PLANIFICADA)){
            //recogerHora.setLong_fecha_referencia_validar(VARIABLES.GetFechaLongFrom_yyyy_mm_dd(LISTA_VISITAS.get(0).getFecha_planificada()));
        }
        recogerHora.Show();
    }


    public void Location_cambiado_2(Location location, String textoDireccion, boolean isDireccion) {
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
                loca.setLatitude(Double.parseDouble(dircli.getLatitud()));
                loca.setLongitude(Double.parseDouble(dircli.getLongitud()));
                double metros_s_cliente=location.getAltitude()-dircli.getAltitud();

                if (loca.getLatitude()!=0 && loca.getLongitude()!=0){
                    DISTACIA_to_Colegio =(int)LOCATION.distanceTo(loca);
                    textoDireccion+="\nDistancia apróximada al cliente es "+calcularKM_and_Metros(DISTACIA_to_Colegio);
                    textoDireccion+="\nAltitud aproximada: "+VARIABLES.formater_thow_decimal.format(metros_s_cliente)+" metros sobre el cliente";
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
            Star_Check_Permiso_Ubicacion();
        });

    }

    public  void GuardarGestionVisita(){

        int index=0;

        if (TIPO_GESTION.equals(PROGRAMACION_VISITA) || TIPO_GESTION.equals(RE_PROGRAMACION_VISITA)){
            if (TextUtils.isEmpty(et_fecha_visita_manual.getText().toString())){
                index++;
                et_fecha_visita_manual.setHintTextColor(getResources().getColor(R.color.pink_600));
            }
        }

        if (SpinnerTipoVisita.getSelectedItemPosition()<=0){
            index++;
            SpinnerTipoVisita.setBackgroundResource(R.drawable.img_for_spinner_pink);
        }
        if (SpinnerTipoActividad.getSelectedItemPosition()<=0){
            index++;
            SpinnerTipoActividad.setBackgroundResource(R.drawable.img_for_spinner_pink);
        }

        if (SpinnerDirecciones.getSelectedItemPosition()<0){
            index++;
            SpinnerDirecciones.setBackgroundResource(R.drawable.img_for_spinner_pink);
        }

        if (TIPO_GESTION.equals(RE_PROGRAMACION_VISITA)){
            if (et_motivo_reporgramacion.getText().toString().length()==0) {
                index++;
                et_motivo_reporgramacion.setHintTextColor(getResources().getColor(R.color.pink_600));
            }
        }


        if (et_hora_inicio.getText().toString().length()==0){
            index++;
            et_hora_inicio.setHintTextColor(getResources().getColor(R.color.pink_600));
        }else{
            try {
                VARIABLES.GetSegundosFrom_hh_mm_to_int(et_hora_inicio.getText().toString());
            } catch (ParseException e) {
                index++;
                et_hora_inicio.setTextColor(getResources().getColor(R.color.pink_600));
                et_hora_inicio.setHintTextColor(getResources().getColor(R.color.pink_600));
                e.printStackTrace();
            }
        }



        if (et_hora_fin.getText().toString().length()==0){
            index++;
            et_hora_fin.setHintTextColor(getResources().getColor(R.color.pink_600));
        }else{
            try {
                VARIABLES.GetSegundosFrom_hh_mm_to_int(et_hora_fin.getText().toString());
            } catch (ParseException e) {
                index++;
                et_hora_fin.setTextColor(getResources().getColor(R.color.pink_600));
                et_hora_fin.setHintTextColor(getResources().getColor(R.color.pink_600));
                e.printStackTrace();
            }
        }

        if (sw_isProximaVisita.isChecked()){

            if (et_fecha_proximavisita.getText().toString().length()<=0){
                index++;
                et_fecha_proximavisita.setHintTextColor(getResources().getColor(R.color.pink_600));
            }
            if (et_hora_inicio_next.getText().toString().length()<=0){
                index++;
                et_hora_inicio_next.setHintTextColor(getResources().getColor(R.color.pink_600));
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


        String fecha_configuracionx = dBclasses.getCambio("Fecha");


        String anio_actual = fecha_configuracionx.substring(8, 10);
        String mes_actual = fecha_configuracionx.substring(3, 5);
        String dia_actual = fecha_configuracionx.substring(0, 2);

        int secactual = 0;

        if (oc.length() < 6) {
            secactual = 1;
            if (mes_actual.length() < 2) {
                mes_actual = cero + mes_actual;
            }
            if (dia_actual.length() < 2) {
                dia_actual = cero + dia_actual;
            }
            //orden = mes_actual + dia_actual + cero + secactual;
            orden = anio_actual + mes_actual + dia_actual + cero + secactual;
            return orden;
        } else {
            //String cadenaFecha = oc.substring(oc.length()-6,oc.length());
            Log.i(TAG, "VER oc numero es "+oc+", tamaño "+oc.length());
            String cadenaFecha = oc.substring(oc.length() - 8, oc.length());

            int aniot = Integer.parseInt(cadenaFecha.substring(0, 2));
            int mest = Integer.parseInt(cadenaFecha.substring(2, 4));
            int diat = Integer.parseInt(cadenaFecha.substring(4, 6));
            int sectem = Integer.parseInt(cadenaFecha.substring(6, 8));

            //Verificar por año
            if (Integer.parseInt(mes_actual) <= mest) {
                if (Integer.parseInt(dia_actual) > diat) {
                    secactual = 1;
                } else
                    secactual = sectem + 1;

            } else {
                secactual = 1;
            }
        }

        if (mes_actual.length() < 2) {
            mes_actual = cero + mes_actual;
        }
        if (dia_actual.length() < 2)
            dia_actual = cero + dia_actual;
        if (secactual < 10) {
            //orden = mes_actual + dia_actual + cero + secactual;
            orden = anio_actual + mes_actual + dia_actual + cero + secactual;
        } else {
            //orden = mes_actual + dia_actual + secactual;
            orden = anio_actual + mes_actual + dia_actual + secactual;
        }

        return orden;

    }


    private void Enviar_Guardar_visitas(){


        ArrayList<San_Visitas> list=new ArrayList<>();
        //acerca de la visita


        String fecha_ejecutada="";
        String fecha_planificada="";
        int item_visita=1;

        if (TIPO_GESTION.equals(PROGRAMACION_VISITA) || TIPO_GESTION.equals(RE_PROGRAMACION_VISITA) || TIPO_GESTION.equals(MODIFICAR_PROGRAMACION)){
            fecha_ejecutada=SIN_FECHA;
            fecha_planificada=et_fecha_visita_manual.getText().toString()+" "+et_hora_inicio.getText().toString();
            if (TIPO_GESTION.equals(RE_PROGRAMACION_VISITA)){
                item_visita=DAO_San_Visitas.getMaxItemByOc_numero(dBclasses.getReadableDatabase(), FINAL_OC_NUMERO);
                item_visita++;
            }

        }else{
            fecha_ejecutada=""+VARIABLES.GET_FECHA_ACTUAL_STRING();
            fecha_planificada=VARIABLES.GET_FECHA_ACTUAL_STRING()+" "+VARIABLES.GET_HORA_ACTUAL_STRING();
        }


        String oc_numero_visitado="";
        String oc_numero_visitar="";

        int id_san_visitas=0;

        if (ORIGEN.equals(ActividadCampoAgendadoActivity.TAG)){
            id_san_visitas=LISTA_VISITAS.get(0).getId();
            fecha_planificada=TIPO_GESTION.equals(PROGRAMACION_VISITA)  
                    || TIPO_GESTION.equals(RE_PROGRAMACION_VISITA)
                    || TIPO_GESTION.equals(MODIFICAR_PROGRAMACION)?fecha_planificada:LISTA_VISITAS.get(0).getFecha_planificada();


            if(!TIPO_GESTION.equals(VISITA_PLANIFICADA)){//ejecutada= solo actualización
                fecha_ejecutada=(LISTA_VISITAS.get(0).getFecha_Ejecutada());
                oc_numero_visitado=LISTA_VISITAS.get(0).getOc_numero_visitado();
                oc_numero_visitar=LISTA_VISITAS.get(0).getOc_numero_visitar();
            }else{//isPLANIFICADA= actualización y creacion de proxima visita
                oc_numero_visitado=txt_oc_numero.getText().toString();
                oc_numero_visitar=FINAL_OC_NUMERO;

            }
        }else{
            if (TIPO_GESTION.equals(PROGRAMACION_VISITA) || TIPO_GESTION.equals(MODIFICAR_PROGRAMACION)){
                oc_numero_visitado="";
                oc_numero_visitar=txt_oc_numero.getText().toString();
            }else{
                oc_numero_visitado=txt_oc_numero.getText().toString();
                oc_numero_visitar="";
            }
        }

        San_Visitas visita=new San_Visitas();
        visita.setId(id_san_visitas);
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

        if (sw_isProximaVisita.isChecked()){
            visita.setFecha_proxima_visita(et_fecha_proximavisita.getText().toString()+" "+et_hora_inicio_next.getText().toString());
        }
        else visita.setFecha_proxima_visita(NO_HAY_PROXIMA_VISITA);

        visita.setHora_inicio_ejecución(et_hora_inicio.getText().toString());
        visita.setHora_Fin_Ejecución(et_hora_fin.getText().toString());
        visita.setFecha_de_modificación(VARIABLES.GET_FECHA_ACTUAL_STRING());

        if (TIPO_GESTION.equals(PROGRAMACION_VISITA) || TIPO_GESTION.equals(MODIFICAR_PROGRAMACION) || TIPO_GESTION.equals(RE_PROGRAMACION_VISITA)){
            visita.setEstado(ESTADO_VISITA_Libre);
        }else{
            visita.setEstado(ESTADO_VISITA_COMPLETADA);
        }
        visita.setTipo_visita(getValorBySpinner(SpinnerTipoVisita));
        visita.setActividad(getValorBySpinner(SpinnerTipoActividad));
        visita.setComentario_actividad(et_comentario_visita.getText().toString());
        visita.setActividad_Proxima(getValorBySpinner(SpinnerProximaActividad));
        visita.setLatitud((LOCATION!=null?LOCATION.getLatitude():0)+ "0");
        visita.setLongitud((LOCATION!=null?LOCATION.getLongitude():0)+ "0");
        visita.setAltitud((LOCATION!=null?LOCATION.getAltitude():0));
        visita.setDistancia(DISTACIA_to_Colegio);
        visita.setItem(item_visita);

        String string = SpinnerContacto.getSelectedItem().toString();
        String[] parts = string.split("-");
        String part1 = parts[0]; // 123

        visita.setId_contacto(Integer.parseInt(part1));
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
            visitaNEXT.setFecha_Ejecutada(SIN_FECHA);
            visitaNEXT.setFecha_planificada(et_fecha_proximavisita.getText().toString()+" "+et_hora_inicio_next.getText().toString());
            visitaNEXT.setFecha_proxima_visita(NO_HAY_PROXIMA_VISITA);
            visitaNEXT.setHora_inicio_ejecución(et_hora_inicio_next.getText().toString());
            visitaNEXT.setHora_Fin_Ejecución(SIN_HORA);
            visitaNEXT.setFecha_de_modificación(VARIABLES.GET_FECHA_ACTUAL_STRING());
            visitaNEXT.setEstado(San_Visitas.ESTADO_VISITA_LIBRE);
            visitaNEXT.setTipo_visita(""+getValorBySpinner(SpinnerProximaTipoVisita));
            visitaNEXT.setActividad(getValorBySpinner(SpinnerProximaActividad));
            visitaNEXT.setComentario_actividad(et_comentario_proxima_visita.getText().toString());
            visitaNEXT.setActividad_Proxima("");
            visitaNEXT.setLatitud("0");
            visitaNEXT.setLongitud("0");
            visitaNEXT.setAltitud(0);
            visitaNEXT.setDistancia(-1);
            visitaNEXT.setItem(item_visita+1);
            list.add(visitaNEXT);
        }

        
        guardarCabeceraPedido((oc_numero_visitado.length()>0?oc_numero_visitado:oc_numero_visitar), list, fecha_planificada);

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

    public String guardarCabeceraPedido(String oc_numero, ArrayList<San_Visitas> list_visitas, String fecha_planificada) {

        String direccion=SpinnerDirecciones.getSelectedItem().toString();
        String [] dir=direccion.split("-");
        int item_direccion=Integer.parseInt(dir[0]);

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

        GuardarVisitaLocal(itemCabecera, fecha_planificada);

        return itemCabecera.getOc_numero();
    }
    private void  GuardarVisitaLocal(DB_ObjPedido itemCabecera, String fecha_hora_planificada){
        SQLiteDatabase __DB=dBclasses.getWritableDatabase();
        __DB.beginTransaction();
        boolean isInsert=false;
        boolean isInsertPregramacion=false;
        String mensaje_error="";

        if(TIPO_GESTION.equals(RE_PROGRAMACION_VISITA)){
            mensaje_error=DAO_San_Visitas.setComentarioReprogramacionVisita(__DB, FINAL_OC_NUMERO , et_motivo_reporgramacion.getText().toString(),
                    fecha_hora_planificada
                    )?"":"No se ha podido actualizar la reprogramación. Vuelva intentarlo";
        }else{
            if (TIPO_GESTION.equals(MODIFICAR_PROGRAMACION)){
                //Actualizamos
                mensaje_error=DAO_San_Visitas.updateFechaProximaVisitaAlVisitado(__DB, FINAL_OC_NUMERO, fecha_hora_planificada)?"":"No se ha podido actualizar la fecha de la visita";
            }

        }

        if (mensaje_error.length()==0){
            if (ORIGEN.equalsIgnoreCase(ActividadCampoAgendadoActivity.TAG)){
                if(TIPO_GESTION.equals(VISITA_PLANIFICADA) ){//isPLANIFICADA= actualización y creacion de proxima visita
                    mensaje_error=dBclasses.AgregarPedidoCabecera_raiz(itemCabecera, __DB)?"":"No se ha podido registrar la cabecera de la visita";
                }else{
                    mensaje_error=dBclasses.deletePedidoCabeceraxOc(itemCabecera.getOc_numero(),__DB)?"":"No se ha podido restaurar la cabecera de la visita";//Eliminamos}
                    if(mensaje_error.length()==0){
                        itemCabecera.setFecha_oc(fechaOcOriginal!=null?fechaOcOriginal:itemCabecera.getFecha_oc());
                        mensaje_error=dBclasses.AgregarPedidoCabecera_raiz(itemCabecera, __DB)?"":"No se ha podido actualizar la cabecera de la visita";///Insertamos nuevo
                    }
                }
            }else{
                mensaje_error=dBclasses.AgregarPedidoCabecera_raiz(itemCabecera, __DB)?"":"No se ha podido registrar la cabecera de la visita";
            }
        }

        if (mensaje_error.length()==0){
            if (ORIGEN.equalsIgnoreCase(ActividadCampoAgendadoActivity.TAG)){
                if(TIPO_GESTION.equals(VISITA_PLANIFICADA) || TIPO_GESTION.equals(RE_PROGRAMACION_VISITA)){//ejecutada= solo actualización
                    if (TIPO_GESTION.equals(RE_PROGRAMACION_VISITA)) {
                        mensaje_error=DAO_San_Visitas.LlenarTabla_San_Visitas( __DB,  itemCabecera.getSan_visitas().get(0))?"":"No se ha podido registrar el detalle de la visita";
                    }else{
                        mensaje_error=DAO_San_Visitas.UpdateVisitaEjecucion( __DB,  itemCabecera.getSan_visitas().get(0))?"":"No se ha podido actualizar el detalle de la visita";
                        if(mensaje_error.length()==0 && itemCabecera.getSan_visitas().size()>1){
                            mensaje_error=DAO_San_Visitas.LlenarTabla_San_Visitas( __DB,  itemCabecera.getSan_visitas().get(1))?"":"No se ha podido agregar el detalle de la visita";
                        }
                    }
                }else{//isPLANIFICADA= actualización y creacion de proxima visita
                    mensaje_error=DAO_San_Visitas.UpdateListaVisitaEjecucion( __DB, itemCabecera.getSan_visitas())?"":"No se ha podido actualizar el detalle de la visita";
                }
            }else{
                mensaje_error=DAO_San_Visitas.LlenarlistaTabla_San_Visitas( __DB,  itemCabecera.getSan_visitas())?"":"No se ha podido registrar el detalle de la visita";;
            }
            String titulo="";
            String mensaje="";
            if (mensaje_error.length()==0){
                titulo="Datos guardados";
                mensaje="Se registró la visita correctamente";
                __DB.setTransactionSuccessful();

                EnviarSanVisitas(itemCabecera.getOc_numero());
            }
            else{
                titulo="Error";

                UtilView.MENSAJE_simple(GestionVisita3Activity.this,
                        ""+titulo, ""+mensaje_error);

            }

        }else{
            UtilView.MENSAJE_simple(GestionVisita3Activity.this, "Error", mensaje_error);
        }
        __DB.endTransaction();
        __DB.close();
    }


    private void EnviarSanVisitas(final String oc_numero){
        WS_San_Visitas ws_san_visitas=new WS_San_Visitas(GestionVisita3Activity.this, dBclasses);
        ws_san_visitas.EnviarVisitasByOc_numero(oc_numero);
    }
}
