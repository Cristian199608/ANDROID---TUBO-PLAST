package com.example.sm_tubo_plast.genesys.BEAN;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.DAO.DAO_Cliente_Contacto;
import com.example.sm_tubo_plast.genesys.DAO.DAO_San_Visitas;
import com.example.sm_tubo_plast.genesys.datatypes.DBPedido_Cabecera;
import com.example.sm_tubo_plast.genesys.datatypes.DBclasses;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.GestionVisita3Activity;
import com.example.sm_tubo_plast.genesys.util.IntentTerceros;
import com.example.sm_tubo_plast.genesys.util.UtilTextView;
import com.example.sm_tubo_plast.genesys.util.UtilView;
import com.example.sm_tubo_plast.genesys.util.VARIABLES;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class San_Visitas {

    public static final String ESTADO_VISITA_LIBRE="Libre";
    public static final String ESTADO_VISITA_COMPLETADADO="Completada";

    int ID;
    String Grupo_Campaña;
    String Cod_Promotor;
    String Promotor;
    String Cod_Colegio;
    String descripcion_Colegio;
    String Ejecutivo_Descripcion;
    String cargo_Descripción;
    String Fecha_Ejecutada;
    String Fecha_planificada;
    String Fecha_proxima_visita;
    String Hora_inicio_ejecución;
    String Hora_Fin_Ejecución;
    String fecha_de_modificación;
    String Estado;
    String Actividad;
    String Detalle;
    String Actividad_Proxima;
    String Detalle_Proximo;
    String Comentario_actividad;
    String descripcion_fecha;
    String tipo_vista;
    String id_rrhh;
    String tipo_visita;
    String latitud;
    String longitud;
    int distancia;
    String oc_numero_visitado;
    String oc_numero_visitar;
    int id_contacto;
    private double altitud;;
    private String descripcion_anulacion;;
    private int item;;


    public String getGrupo_Campaña() {
        return Grupo_Campaña;
    }

    public void setGrupo_Campaña(String grupo_Campaña) {
        Grupo_Campaña = grupo_Campaña;
    }

    public String getCod_Promotor() {
        return Cod_Promotor;
    }

    public void setCod_Promotor(String cod_Promotor) {
        Cod_Promotor = cod_Promotor;
    }

    public String getPromotor() {
        return Promotor;
    }

    public void setPromotor(String promotor) {
        Promotor = promotor;
    }

    public String getCod_Colegio() {
        return Cod_Colegio;
    }

    public void setCod_Colegio(String cod_Colegio) {
        Cod_Colegio = cod_Colegio;
    }

    public String getDescripcion_Colegio() {
        return descripcion_Colegio;
    }

    public void setDescripcion_Colegio(String descripcion_Colegio) {
        this.descripcion_Colegio = descripcion_Colegio;
    }

    public String getEjecutivo_Descripcion() {
        return Ejecutivo_Descripcion;
    }

    public void setEjecutivo_Descripcion(String ejecutivo_Descripcion) {
        Ejecutivo_Descripcion = ejecutivo_Descripcion;
    }

    public String getCargo_Descripción() {
        return cargo_Descripción;
    }

    public void setCargo_Descripción(String cargo_Descripción) {
        this.cargo_Descripción = cargo_Descripción;
    }

    public String getFecha_Ejecutada() {
        return Fecha_Ejecutada;
    }

    public void setFecha_Ejecutada(String fecha_Ejecutada) {
        Fecha_Ejecutada = fecha_Ejecutada;
    }

    public String getFecha_planificada() {
        return Fecha_planificada;
    }

    public void setFecha_planificada(String Fecha_planificada) {
        this.Fecha_planificada = Fecha_planificada;
    }

    public String getFecha_proxima_visita() {
        return Fecha_proxima_visita;
    }

    public void setFecha_proxima_visita(String Fecha_proxima_visita) {
        this.Fecha_proxima_visita = Fecha_proxima_visita;
    }

    public int getItem() {
        return item;
    }

    public void setItem(int item) {
        this.item = item;
    }

    public String getHora_inicio_ejecución() {
        return Hora_inicio_ejecución;
    }

    public void setHora_inicio_ejecución(String hora_inicio_ejecución) {
        Hora_inicio_ejecución = hora_inicio_ejecución;
    }

    public String getHora_Fin_Ejecución() {
        return Hora_Fin_Ejecución;
    }

    public void setHora_Fin_Ejecución(String hora_Fin_Ejecución) {
        Hora_Fin_Ejecución = hora_Fin_Ejecución;
    }

    public String getFecha_de_modificación() {
        return fecha_de_modificación;
    }

    public void setFecha_de_modificación(String fecha_de_modificación) {
        this.fecha_de_modificación = fecha_de_modificación;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }

    public String getActividad() {
        return Actividad;
    }

    public void setActividad(String actividad) {
        Actividad = actividad;
    }

    public String getDetalle() {
        return Detalle;
    }

    public void setDetalle(String detalle) {
        Detalle = detalle;
    }

    public String getActividad_Proxima() {
        return Actividad_Proxima;
    }

    public void setActividad_Proxima(String actividad_Proxima) {
        Actividad_Proxima = actividad_Proxima;
    }

    public String getDetalle_Proximo() {
        return Detalle_Proximo;
    }

    public void setDetalle_Proximo(String detalle_Proximo) {
        Detalle_Proximo = detalle_Proximo;
    }


    public String getComentario_actividad() {
        return Comentario_actividad;
    }

    public void setComentario_actividad(String comentario_actividad) {
        Comentario_actividad = comentario_actividad;
    }

    public String getDescripcion_fecha() {
        return descripcion_fecha;
    }

    public void setDescripcion_fecha(String descripcion_fecha) {
        this.descripcion_fecha = descripcion_fecha;
    }

    public String getTipo_vista() {
        return tipo_vista;
    }

    public void setTipo_vista(String tipo_vista) {
        this.tipo_vista = tipo_vista;
    }

    public String getId_rrhh() {
        return id_rrhh;
    }

    public void setId_rrhh(String id_rrhh) {
        this.id_rrhh = id_rrhh;
    }

    public int getId() {
        return ID;
    }

    public void setId(int ID) {
        this.ID = ID;
    }

    public String getTipo_visita() {
        return tipo_visita;
    }

    public void setTipo_visita(String tipo_visita) {
        this.tipo_visita = tipo_visita;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }
    public int getDistancia() {
        return distancia;
    }

    public void setDistancia(int distancia) {
        this.distancia = distancia;
    }

    public String getOc_numero_visitado() {
        return oc_numero_visitado;
    }

    public void setOc_numero_visitado(String oc_numero_visitado) {
        this.oc_numero_visitado = oc_numero_visitado;
    }
    public String getOc_numero_visitar() {
        return this.oc_numero_visitar;
    }

    public void setOc_numero_visitar(String oc_numero_visitar) {
        this.oc_numero_visitar = oc_numero_visitar;
    }

    public int getId_contacto() {
        return id_contacto;
    }

    public void setId_contacto(int id_contacto) {
        this.id_contacto = id_contacto;
    }

    public double getAltitud() {
        return altitud;
    }

    public String getDescripcion_anulacion() {
        return descripcion_anulacion;
    }

    public void setDescripcion_anulacion(String descripcion_anulacion) {
        this.descripcion_anulacion = descripcion_anulacion;
    }

    public void setAltitud(double altitud) {
        this.altitud = altitud;
    }

    public void showDialog_mas(Activity activity, San_Visitas sa, boolean isPlanificada){

        LinearLayout.LayoutParams paramT = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams paramT2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 10);
        LinearLayout.LayoutParams paramT3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        RelativeLayout relativeLayout=new RelativeLayout(activity);
        relativeLayout.setLayoutParams(paramT3);

        LinearLayout linearLayout=new LinearLayout(activity);
        linearLayout.setLayoutParams(paramT3);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        TextView textViewCab=new TextView(activity);
        textViewCab.setLayoutParams(paramT3);
        textViewCab.setText("DETALLE");
        textViewCab.setBackgroundColor(activity.getResources().getColor(R.color.teal_500));
        textViewCab.setTextColor(activity.getResources().getColor(R.color.white));
        textViewCab.setTextSize(20);
        textViewCab.setPadding(5,5,5,2);
        textViewCab.setGravity(Gravity.CENTER_HORIZONTAL);

        linearLayout.addView(textViewCab);

        TextView textView=new TextView(activity);
        textView.setLayoutParams(paramT3);
        textView.setText("Promotor: "+sa.getPromotor());
        textView.setBackgroundColor(activity.getResources().getColor(R.color.teal_500));
        textView.setTextColor(activity.getResources().getColor(R.color.white));
        textView.setTextSize(15);
        textView.setPadding(5,2,5,5);
        textView.setGravity(Gravity.START);

        linearLayout.addView(textView);

        TextView textViewHORA=new TextView(activity);
        textViewHORA.setLayoutParams(paramT3);
        if (isPlanificada)textViewHORA.setText("Ejecutada: "+ VARIABLES.returnFechaDescripcion(sa.getFecha_Ejecutada())+" "+VARIABLES.FromTime_parseTIME_Exacto12hs(sa.getHora_inicio_ejecución()));
        else {
            textViewHORA.setText("Planificada: "+ VARIABLES.returnFechaDescripcion(sa.getFecha_planificada()));
        }


        textViewHORA.setBackgroundColor(activity.getResources().getColor(R.color.teal_500));
        textViewHORA.setTextColor(activity.getResources().getColor(R.color.white));
        textViewHORA.setTextSize(13);
        textViewHORA.setPadding(5,2,5,5);
        textViewHORA.setGravity(Gravity.START);

        linearLayout.addView(textViewHORA);

        TextView presentacion_producto=new TextView(activity);
        presentacion_producto.setLayoutParams(paramT3);
        presentacion_producto.setText("Actividad:");
        presentacion_producto.setTextColor(activity.getResources().getColor(R.color.grey_900));
        presentacion_producto.setTextSize(13);
        presentacion_producto.setPadding(5,2,5,5);

        linearLayout.addView(presentacion_producto);

        TextView txt_presentacion_producto=new TextView(activity);
        txt_presentacion_producto.setLayoutParams(paramT3);
        txt_presentacion_producto.setText(""+sa.getActividad());
        txt_presentacion_producto.setTextColor(activity.getResources().getColor(R.color.grey_700));
        txt_presentacion_producto.setTextSize(13);
        txt_presentacion_producto.setPadding(50,2,5,5);

        linearLayout.addView(txt_presentacion_producto);


        TextView comentario_visita=new TextView(activity);
        comentario_visita.setLayoutParams(paramT3);
        comentario_visita.setText("Comentario:");
        comentario_visita.setTextColor(activity.getResources().getColor(R.color.grey_900));
        comentario_visita.setTextSize(13);
        comentario_visita.setPadding(5,2,5,5);

        linearLayout.addView(comentario_visita);


        TextView txt_comentario_visita=new TextView(activity);
        txt_comentario_visita.setLayoutParams(paramT3);
        txt_comentario_visita.setText(""+sa.getComentario_actividad());
        txt_comentario_visita.setTextColor(activity.getResources().getColor(R.color.grey_700));
        txt_comentario_visita.setTextSize(13);
        txt_comentario_visita.setPadding(50,2,5,5);
        linearLayout.addView(txt_comentario_visita);

        if (!isPlanificada){
            TextView proxima_visita=new TextView(activity);
            proxima_visita.setLayoutParams(paramT3);
            proxima_visita.setText("Próxima Visita:");
            proxima_visita.setTextColor(activity.getResources().getColor(R.color.teal_600));
            proxima_visita.setTextSize(13);
            proxima_visita.setPadding(5,2,5,5);
            linearLayout.addView(proxima_visita);

            TextView txt_proxima_visita=new TextView(activity);
            txt_proxima_visita.setLayoutParams(paramT3);
            txt_proxima_visita.setText(""+ VARIABLES.returnFechaDescripcion(sa.getFecha_proxima_visita()));
            txt_proxima_visita.setTextColor(activity.getResources().getColor(R.color.teal_300));
            txt_proxima_visita.setTextSize(13);
            txt_proxima_visita.setPadding(50,2,5,5);
            linearLayout.addView(txt_proxima_visita);
        }



        relativeLayout.addView(linearLayout);

        new AlertDialog.Builder(activity)
                .setCancelable(false)

                .setView(relativeLayout)
                .setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                })
                .show();
    }

    private TextView SetTitulo(Activity activity, LinearLayout.LayoutParams  param,  String valor){
        UtilTextView presentacion_producto= new UtilTextView(activity);
        presentacion_producto.setLayoutParams(param);
        presentacion_producto.setTextoSize(15);
        presentacion_producto.setTextoColor(R.color.primary_text);
        presentacion_producto.setPadding(10,2,5,5);
        return presentacion_producto.Generate(""+valor);
    }

    private TextView SetTitulo_valor(Activity activity, LinearLayout.LayoutParams  param,  String valor){
        UtilTextView presentacion_producto= new UtilTextView(activity);
        presentacion_producto.setLayoutParams(param);
        presentacion_producto.setTextoSize(13);
        presentacion_producto.setTextoColor(R.color.grey_700);
        presentacion_producto.setPadding(50,2,5,5);
        return presentacion_producto.Generate(valor);
    }
    public void showDialog_ver_tplast(Activity activity, San_Visitas sa, boolean isPlanificada){

        LinearLayout.LayoutParams paramT = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams paramT2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 10);
        LinearLayout.LayoutParams paramT3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);



        LinearLayout linearLayout=new LinearLayout(activity);
        linearLayout.setLayoutParams(paramT3);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        TextView textViewCab=new TextView(activity);
        textViewCab.setLayoutParams(paramT3);
        textViewCab.setText("DETALLE");
        textViewCab.setBackgroundColor(activity.getResources().getColor(R.color.teal_500));
        textViewCab.setTextColor(activity.getResources().getColor(R.color.white));
        textViewCab.setTextSize(20);
        textViewCab.setPadding(5,5,5,2);
        textViewCab.setGravity(Gravity.CENTER_HORIZONTAL);

        linearLayout.addView(textViewCab);

        TextView textView=new TextView(activity);
        textView.setLayoutParams(paramT3);
        textView.setText("CLiente: "+sa.getEjecutivo_Descripcion());
        textView.setBackgroundColor(activity.getResources().getColor(R.color.teal_500));
        textView.setTextColor(activity.getResources().getColor(R.color.white));
        textView.setTextSize(15);
        textView.setPadding(10,2,5,5);
        textView.setGravity(Gravity.START);

        linearLayout.addView(textView);

        TextView textViewHORA=new TextView(activity);
        textViewHORA.setLayoutParams(paramT3);
        int StyleAlerta=AlertDialog.THEME_HOLO_LIGHT;

        if (isPlanificada){
            if (sa.getOc_numero_visitar().contains(GestionVisita3Activity.OC_NUMERO_REPROGRAMADO)){
                StyleAlerta=R.style.MyDialogThemePurple;
            }else if (sa.getDescripcion_anulacion()!=null){
                if (sa.getDescripcion_anulacion().length()>0) {
                    StyleAlerta=R.style.MyDialogThemeRed;
                }

            }
        }
        else {
            StyleAlerta=R.style.MyDialogThemeOrange;
            textViewHORA.setText("Planificada: "+ VARIABLES.returnFechaDescripcion(sa.getFecha_planificada()));
        }


        textViewHORA.setBackgroundColor(activity.getResources().getColor(R.color.teal_500));
        textViewHORA.setTextColor(activity.getResources().getColor(R.color.white));
        textViewHORA.setTextSize(13);
        textViewHORA.setPadding(10,2,5,5);
        textViewHORA.setGravity(Gravity.START);

        linearLayout.addView(textViewHORA);

        linearLayout.addView(SetTitulo(activity, paramT, "Dirección"));

        DBclasses dBclasses=new DBclasses(activity);

        String direccion="Sin Dirección";
        DBPedido_Cabecera dbPedido_cabecera=dBclasses.getPedido_cabecera(isPlanificada?sa.getOc_numero_visitar():sa.getOc_numero_visitado());
        if (dbPedido_cabecera!=null){
            String item_direccion = dbPedido_cabecera.getSitio_enfa();
            direccion=dBclasses.obtenerDireccionxCliente2(sa.getId_rrhh(), item_direccion);

        }

        linearLayout.addView(SetTitulo_valor(activity, paramT, ""+direccion));

        linearLayout.addView(SetTitulo(activity, paramT, "Referencia"));
        linearLayout.addView(SetTitulo_valor(activity, paramT, ""+sa.getTipo_visita()+" - "+sa.getActividad()));

        linearLayout.addView(SetTitulo(activity, paramT, "Fecha"));
        linearLayout.addView(SetTitulo_valor(activity, paramT, "Programada: "+VARIABLES.convertirFecha_from_String(sa.getFecha_planificada())));


        if (!isPlanificada){
            linearLayout.addView(SetTitulo_valor(activity, paramT,
                    "Visitada: "+sa.getFecha_Ejecutada()+" "+ VARIABLES.FromTime_parseTIME_Exacto12hs(sa.getHora_inicio_ejecución())+" - "+ VARIABLES.FromTime_parseTIME_Exacto12hs(sa.getHora_Fin_Ejecución())));
        }

        DAO_Cliente_Contacto dao_cliente_contacto=new DAO_Cliente_Contacto();

        ArrayList<Cliente_Contacto> cliente_contactos=dao_cliente_contacto.getClienteContactoByID(dBclasses, sa.getId_rrhh(), sa.getId_contacto());

        linearLayout.addView(SetTitulo(activity, paramT, "Contacto"));
        if (cliente_contactos.size()>0) {
            Cliente_Contacto contacto=cliente_contactos.get(0);
            linearLayout.addView(SetTitulo_valor(activity, paramT, ""+contacto.getDni()+" - "+contacto.getNombre_contacto()));
        }else{
            linearLayout.addView(SetTitulo_valor(activity, paramT, "Sin contacto"));
        }


        linearLayout.addView(SetTitulo(activity, paramT, "Comentario"));
        linearLayout.addView(SetTitulo_valor(activity, paramT, ""+sa.getComentario_actividad()));

        linearLayout.addView(SetTitulo(activity, paramT, "Próxima Visita"));
        linearLayout.addView(SetTitulo_valor(activity, paramT, "Planificada: "+sa.getFecha_proxima_visita()));

        String fecha_reporgramada=DAO_San_Visitas.tieneReporgramadoVisita(dBclasses.getReadableDatabase(), sa.getId(), !isPlanificada?sa.getOc_numero_visitado():sa.getOc_numero_visitar());
        if (fecha_reporgramada!=null){
            if (fecha_reporgramada.length()>0) {
                linearLayout.addView(SetTitulo_valor(activity, paramT, "Reprogramada "+fecha_reporgramada));
            }
        }else{
            fecha_reporgramada="Error, al leer la data";
            linearLayout.addView(SetTitulo_valor(activity, paramT, "Reprogramada "+fecha_reporgramada));
        }

        if (sa.getDescripcion_anulacion()!=null){
            if (sa.getDescripcion_anulacion().length()>0) {
                linearLayout.addView(SetTitulo(activity, paramT, "Comentario Anulacion/Reprogramación"));
                linearLayout.addView(SetTitulo_valor(activity, paramT, ""+sa.getDescripcion_anulacion()));

            }

        }




        if (!isPlanificada){
            ImageView imageView=new ImageView(activity);
//        imageView.setBackgroundResource(R.drawable.mapa);
            imageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.logo_google_maps));
            imageView.setLayoutParams(paramT3);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LatLng latLng=new LatLng(Double.parseDouble(sa.getLatitud()),Double.parseDouble( sa.getLongitud()));
                    IntentTerceros.lanzarGooleMaps(activity, latLng);
                }
            });

            UtilView.Efectos(activity, imageView, R.color.white);
            linearLayout.addView(imageView);

        }

        RelativeLayout relativeLayout=new RelativeLayout(activity);
        relativeLayout.setLayoutParams(paramT3);

        relativeLayout.addView(linearLayout);

        new AlertDialog.Builder(activity, StyleAlerta)
                .setCancelable(false)

                .setView(relativeLayout)
                .setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                })
                .show();
    }
}
