package com.example.sm_tubo_plast.genesys.fuerza_ventas;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.BEAN.BEAN_AgendaCalendario;
import com.example.sm_tubo_plast.genesys.BEAN.San_Visitas;
import com.example.sm_tubo_plast.genesys.DAO.DAO_San_Visitas;
import com.example.sm_tubo_plast.genesys.adapters.AdapterAgendaActividades;
import com.example.sm_tubo_plast.genesys.adapters.AdapterMesesCalendario;
import com.example.sm_tubo_plast.genesys.datatypes.DBclasses;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.Fragment.FragmentContenedorAgendado;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.Fragment.Fragment_actividad_campo;
import com.example.sm_tubo_plast.genesys.util.UtilView;
import com.example.sm_tubo_plast.genesys.util.VARIABLES;

import java.util.ArrayList;
import java.util.Calendar;

@SuppressLint("LongLogTag")
public class ActividadCampoAgendadoActivity
        extends AppCompatActivity
        implements AdapterMesesCalendario.IteracionAdapterDias,
        AdapterAgendaActividades.IteracionListenerAgenda,
        FragmentContenedorAgendado.OnFragmentInteractionListener,
        Fragment_actividad_campo.OnFragmentInteractionListener
{
    public static final String TAG="ActividadCampoAgendadoActivity";
    private static final int MENU_CAMBIAR_CALENDARIO = 0;
    private  boolean REFRESH_DATA = false;
    private  int MENU_ESTADO_CALENDARIO = 1;
    private  int ACTUAL_POSITION_MES = -1;
    private  int ACTUAL_SELECTED_ANIO = 2019;
    private static final int MENU_CALENDARIO1 = 1;
    private static final int MENU_CALENDARIO2 = 2;
    RecyclerView recyclerView;
    RelativeLayout contenedor_fragmentAgendado;
    AdapterMesesCalendario ADAPTER_meses;
    AdapterAgendaActividades ADAPTER_agenda;
    LinearLayout layout_direccionales;

    ProgressBar progressBar;
    TextView txt_mes_anio;
    TextView TVBack, TVNext;
    Spinner spn_meses_anio;
    boolean isGoBack=true;
    boolean SPN_selected=false;
    String FECHA_SELECCIONADO="";

    ArrayList<San_Visitas> lista_visitasORIGINAL =new ArrayList<>();
    ArrayList<San_Visitas> lista_visitasMOV =new ArrayList<>();

    DBclasses dBclasses;

    AsynCargarCalendario AsynCargaCalendario;

    FragmentContenedorAgendado contenedorAgendado;
    Fragment_actividad_campo fragment_actividad_campo;
    String CODVEN="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_campo_agendado);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Actividades");

        Bundle bundle = getIntent().getExtras();
        CODVEN = "" + bundle.getString("codven");

        progressBar=findViewById(R.id.progressBar);
        recyclerView=findViewById(R.id.recyclerView);
        contenedor_fragmentAgendado=findViewById(R.id.contenedor_fragmentAgendado);
        layout_direccionales=findViewById(R.id.layout_direccionales);
        txt_mes_anio=findViewById(R.id.txt_mes_anio);
        spn_meses_anio=findViewById(R.id.spn_meses_anio);
        TVBack=findViewById(R.id.TVBack);
        TVNext=findViewById(R.id.TVNext);

        UtilView.Efectos(getApplicationContext(), TVBack, R.color.red_500);
        UtilView.Efectos(getApplicationContext(), TVNext, R.color.blue_500);


        dBclasses=new DBclasses(this);


        spn_meses_anio.setAdapter(UtilView.LLENAR_SPINNER_SIMPLE2(this, VARIABLES.MESES));
        txt_mes_anio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spn_meses_anio.performClick();
            }
        });

        spn_meses_anio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos   , long l) {
                if (SPN_selected){
                    if (MENU_ESTADO_CALENDARIO==MENU_CALENDARIO1){
                        LlenarRecyclerView(pos, null, null);
                    }
                    else if (MENU_ESTADO_CALENDARIO==MENU_CALENDARIO2){
                        String f="0"+(pos+1);
                        FECHA_SELECCIONADO=""+ACTUAL_SELECTED_ANIO+"-"+(f.substring(f.length() -2));
                        LlenarRecyclerView2(pos, null, null);
                    }
                }
                else SPN_selected=true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        int mesActual =   Calendar.getInstance().get(Calendar.MONTH);
        ACTUAL_SELECTED_ANIO =   Calendar.getInstance().get(Calendar.YEAR);

        LlenarRecyclerView(mesActual, null, null);

//
////        fragment_taller_visita=new Fragment_taller_visita("", "");
//        fragment_actividad_campo=new Fragment_actividad_campo();
//        FragmentContenedorAgendado contenedorAgendado=new FragmentContenedorAgendado(fragment_taller_visita);
//        FragmentManager fm=getSupportFragmentManager();
//        FragmentTransaction Ftransaction=fm.beginTransaction();
//        Ftransaction.add(R.id.contenedor_fragmentAgendado, contenedorAgendado, "contenedor_fragmentAgendado");
//        Ftransaction.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, MENU_CAMBIAR_CALENDARIO, Menu.NONE, "Cambiar Calendario").setIcon(R.drawable.ic_swap_horiz_black_24dp).setTitle("Cambiar Calendario").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (!isGoBack){
            backPresed();
        }else{
            switch (item.getItemId()) {
                case android.R.id.home:
                    backPresed();
                    break;
                case MENU_CAMBIAR_CALENDARIO:

                    int mesRefres=ACTUAL_POSITION_MES;
                    ACTUAL_POSITION_MES=-1;

                    String f="0"+(mesRefres+1);
                    FECHA_SELECCIONADO=""+ACTUAL_SELECTED_ANIO+"-"+(f.substring(f.length() -2));
                    if (MENU_ESTADO_CALENDARIO==MENU_CALENDARIO1){
                        if (lista_visitasMOV!=null) lista_visitasMOV.clear();
                        lista_visitasMOV=null;
                        //lista_visitasMOV.addAll(lista_visitasORIGINAL);
                        MENU_ESTADO_CALENDARIO=MENU_CALENDARIO2;


                        Animation animation1= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_ocultar);

                        Animation animation2=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_aparecer);
                        LlenarRecyclerView2(mesRefres, animation1, animation2);
                    }
                    else if (MENU_ESTADO_CALENDARIO==MENU_CALENDARIO2){

                        if (lista_visitasMOV!=null){
                            lista_visitasMOV.clear();
                            lista_visitasMOV.addAll(lista_visitasORIGINAL);
                        }

                        MENU_ESTADO_CALENDARIO=MENU_CALENDARIO1;
                        Animation animation=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_aparecer);
                        LlenarRecyclerView(mesRefres, animation, null);
                    }
                    break;
            }
        }
        return super.onOptionsItemSelected(item);
    }



    private void LlenarRecyclerView(int nroMes, Animation animation, Animation animation2){

        String f="0"+(nroMes+1);

        if (ACTUAL_POSITION_MES!=nroMes || REFRESH_DATA){
            REFRESH_DATA=false;
            ACTUAL_POSITION_MES=nroMes;
            if (AsynCargaCalendario!=null)AsynCargaCalendario.cancel(true);

            lista_visitasMOV= DAO_San_Visitas.getSan_VisitasByFecha(dBclasses, CODVEN,"%", ""+ACTUAL_SELECTED_ANIO+"-"+(f.substring(f.length() -2))+"-");

            txt_mes_anio.setText(VARIABLES.MESES[nroMes]);
            if (ADAPTER_meses!=null)ADAPTER_meses.SimularCarga(VARIABLES.MESES[nroMes]+" - "+ACTUAL_SELECTED_ANIO);
            AsynCargaCalendario= new AsynCargarCalendario();
            AsynCargaCalendario.execute();
        }
        recyclerView.setVisibility(View.VISIBLE);
        contenedor_fragmentAgendado.setVisibility(View.GONE);
        if (animation!=null){
            recyclerView.setAnimation(animation);
            SPN_selected=false;
            spn_meses_anio.setSelection(nroMes);
        }
        if (animation2!=null){
            contenedor_fragmentAgendado.setAnimation(animation2);
        }
    }

    private void LlenarRecyclerView2(int nroMes, Animation animation1, Animation animation2){

        if (contenedorAgendado!=null)contenedorAgendado.viewPagerItem(1);
        ACTUAL_POSITION_MES=nroMes;
        txt_mes_anio.setText(VARIABLES.MESES[nroMes]);
        recyclerView.setVisibility(View.GONE);
        contenedor_fragmentAgendado.setVisibility(View.VISIBLE);
        if (animation1!=null) recyclerView.setAnimation(animation1);
        if (animation2!=null) contenedor_fragmentAgendado.setAnimation(animation2);
        if (ACTUAL_POSITION_MES!=nroMes){

//                lista_visitasORIGINAL= DAO_San_Visitas.getSan_VisitasByFecha(dBclasses, "%", ""+ACTUAL_SELECTED_ANIO+"-"+(f.substring(f.length() -2))+"-");
//                lista_visitasMOV.clear();
//                lista_visitasMOV.addAll(lista_visitasORIGINAL);

        }


        boolean add=false;
        if (contenedorAgendado==null) add=true;

        if(add){
            fragment_actividad_campo=new Fragment_actividad_campo(dBclasses,CODVEN, lista_visitasMOV, ""+FECHA_SELECCIONADO);
            fragment_actividad_campo.LlenarRecyclerView(ACTUAL_SELECTED_ANIO, ACTUAL_POSITION_MES );




            contenedorAgendado=new FragmentContenedorAgendado(fragment_actividad_campo);
            FragmentManager fm=getSupportFragmentManager();
            FragmentTransaction Ftransaction=fm.beginTransaction();

            Ftransaction.add(R.id.contenedor_fragmentAgendado2, contenedorAgendado, "contenedor_fragmentAgendado");
            //else Ftransaction.replace(R.id.contenedor_fragmentAgendado2, contenedorAgendado, "contenedor_fragmentAgendado");
            Ftransaction.commit();
        }else{
            if (lista_visitasMOV==null) fragment_actividad_campo.LlenarRecyclerView(FECHA_SELECCIONADO, ACTUAL_SELECTED_ANIO, ACTUAL_POSITION_MES );
            else fragment_actividad_campo.refresRecyclerView(lista_visitasMOV,  FECHA_SELECCIONADO);

            contenedorAgendado.CambiarTituloByTAB(0, "Actividad ("+fragment_actividad_campo.sizeLISTA()+")");
        }

        //recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        //ADAPTER_agenda=new AdapterAgendaActividades(ActividadCampoAgendadoActivity.this,dBclasses, this.lista_visitasMOV, ACTUAL_SELECTED_ANIO, VARIABLES.MESES[nroMes]);
        //recyclerView2.setAdapter(ADAPTER_agenda);
    }


    public void AnteriorMes(View view){
        if (ACTUAL_POSITION_MES>0){

            String f="0"+(ACTUAL_POSITION_MES);
            FECHA_SELECCIONADO=""+ACTUAL_SELECTED_ANIO+"-"+(f.substring(f.length() -2));

            if (MENU_ESTADO_CALENDARIO==MENU_CALENDARIO1){
                Animation animation=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_left_in);
                Animation animation2=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_botom_out);
                LlenarRecyclerView((ACTUAL_POSITION_MES-1), animation, null);
            }
            else if (MENU_ESTADO_CALENDARIO==MENU_CALENDARIO2){
                Animation animation=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_left_in);
                LlenarRecyclerView2((ACTUAL_POSITION_MES-1), null, animation);
            }
        }else{
            if (view!=null){
                ACTUAL_POSITION_MES=12;
                ACTUAL_SELECTED_ANIO-=1;
                AnteriorMes(null);
            }
        }
    }
    public void SiguienteMes(View view){

        if (ACTUAL_POSITION_MES<11){
            String f="0"+(ACTUAL_POSITION_MES+2);
            FECHA_SELECCIONADO=""+ACTUAL_SELECTED_ANIO+"-"+(f.substring(f.length() -2));
            if (MENU_ESTADO_CALENDARIO==MENU_CALENDARIO1){
                Animation animation=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_right_in);
                LlenarRecyclerView((ACTUAL_POSITION_MES+1), animation, null);
            }
            else if (MENU_ESTADO_CALENDARIO==MENU_CALENDARIO2){
                Animation animation=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_right_in);
                LlenarRecyclerView2((ACTUAL_POSITION_MES+1) , null, animation);
            }
        }else{
            if (view!=null){
                ACTUAL_POSITION_MES=-1;
                ACTUAL_SELECTED_ANIO+=1;
                SiguienteMes(null);
            }
        }

    }


    @Override
    public void onClickAdapterDias(BEAN_AgendaCalendario itemDia) {


        FECHA_SELECCIONADO=itemDia.getFecha();
        lista_visitasMOV=DAO_San_Visitas.getSan_VisitasByFecha(dBclasses, CODVEN,"%", ""+FECHA_SELECCIONADO);
        if (lista_visitasMOV.size()>0  || itemDia.getCant_cumpleano()>0){
            this.isGoBack=false;
            Log.i(TAG, "onClickAdapterDias:: "+FECHA_SELECCIONADO);
            Animation animation1=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_top_out);
            Animation animation2=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_botom_in);

            layout_direccionales.setVisibility(View.GONE);
            layout_direccionales.setAnimation(animation1);
            this.LlenarRecyclerView2(ACTUAL_POSITION_MES, animation1, animation2);
        }else {
            lista_visitasMOV.clear();
            lista_visitasMOV.addAll(lista_visitasORIGINAL);
            Toast.makeText(this, "No hay datos para mostrar en la fecha seleccionada", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onBackPressed() {
        backPresed();
    }

    private void backPresed(){
        if (isGoBack){
            super.onBackPressed();
        }else {

            isGoBack=!isGoBack;
            Animation animation1=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_top_in);
            Animation animation2=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_botom_out);
            layout_direccionales.setVisibility(View.VISIBLE);
            layout_direccionales.setAnimation(animation1);
            lista_visitasMOV.clear();
            lista_visitasMOV.addAll(lista_visitasORIGINAL);

            LlenarRecyclerView(ACTUAL_POSITION_MES, animation1, animation2);

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            REFRESH_DATA = true;
        } else {

        }
    }

    @Override
    public  void onClickAdapterAgenda(San_Visitas san_visitas, boolean isPlanificada){
        new San_Visitas().showDialog_mas(ActividadCampoAgendadoActivity.this, san_visitas, isPlanificada);
    }

    @Override
    public  void onClickAdapterAgenda_editar(San_Visitas san_visitas, boolean isPlanificada){

        String oc_numero="";
        if (isPlanificada) {
            oc_numero=san_visitas.getOc_numero_visitar();
        }else{
            oc_numero=san_visitas.getOc_numero_visitado();
        }

        if (oc_numero.length()>0) {
            Intent intent=new Intent(this, GestionVisita3Activity.class);
            intent.putExtra("ID_RRHH", san_visitas.getId_rrhh());
            intent.putExtra("OC_NUMERO", oc_numero);
            intent.putExtra("CODIGO_CRM",san_visitas.getCod_Colegio());
            intent.putExtra("NOMBRE_INSTI",san_visitas.getDescripcion_Colegio());
            intent.putExtra("COD_VEND", CODVEN);
            intent.putExtra("isPLANIFICADA", isPlanificada);
            intent.putExtra("ORIGEN", TAG);

            startActivityForResult(intent, 1);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    class AsynCargarCalendario extends AsyncTask<String, String, String> {
        ArrayList<BEAN_AgendaCalendario> listaDias=new ArrayList<>();
        @Override
        protected void onPreExecute() {

            txt_mes_anio.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {


            listaDias=new BEAN_AgendaCalendario().ObtenerDiasByMes(dBclasses, CODVEN,ActividadCampoAgendadoActivity.this, ACTUAL_SELECTED_ANIO, ACTUAL_POSITION_MES);

            ADAPTER_meses=new AdapterMesesCalendario(ActividadCampoAgendadoActivity.this, listaDias, ACTUAL_SELECTED_ANIO,ACTUAL_POSITION_MES);

            if (ACTUAL_POSITION_MES>0){
                ADAPTER_meses.notifyDataSetChanged();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    recyclerView.setLayoutManager(new LinearLayoutManager(ActividadCampoAgendadoActivity.this));
                    recyclerView.setAdapter(ADAPTER_meses);
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            txt_mes_anio.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

        }
    }

}
