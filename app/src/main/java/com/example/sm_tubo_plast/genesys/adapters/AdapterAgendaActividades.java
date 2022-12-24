package com.example.sm_tubo_plast.genesys.adapters;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.BEAN.San_Visitas;
import com.example.sm_tubo_plast.genesys.DAO.DAO_San_Visitas;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.GestionVisita3Activity;
import com.example.sm_tubo_plast.genesys.service.WS_San_Visitas;
import com.example.sm_tubo_plast.genesys.datatypes.DBclasses;
import com.example.sm_tubo_plast.genesys.util.UtilView;
import com.example.sm_tubo_plast.genesys.util.UtilViewMensaje;
import com.example.sm_tubo_plast.genesys.util.VARIABLES;

import java.util.ArrayList;

public class AdapterAgendaActividades extends RecyclerView.Adapter<AdapterAgendaActividades.ViewHolder> {

    Activity context;
    DBclasses dBclasses;
    ArrayList<San_Visitas> lista_visitas;
    private   IteracionListenerAgenda listener=null;
    String fecha_seleccionado="";

    public AdapterAgendaActividades(Activity context, DBclasses dBclasses, ArrayList<San_Visitas> lista_visitas, String fecha_seleccionado) {
        this.context = context;
        this.dBclasses = dBclasses;
        this.lista_visitas=lista_visitas;
        this.fecha_seleccionado=fecha_seleccionado;

        if (context instanceof AdapterMesesCalendario.IteracionAdapterDias) {
            listener = (IteracionListenerAgenda) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " debes Instanciar la interfas de AdapterAgendaActividades in your activity");
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_agenda_actividades, null, false);
        //view.findViewById(R.id.txt_ln_producto).setOnClickListener(listener);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        if (lista_visitas.size()==0){
            holder.txt_fecha.setText("");
            holder.txt_fecha.setHint("No hay datos para la fecha seleccionado");
            holder.txt_estado.setText("");
            holder.txtNombreInstitucion.setText("");
            holder.txtFechaEjecutada.setText("");
            holder.txt_promotor.setText("");
            holder.txt_gerente.setText("");
            holder.txt_hora_visita.setText("");
            return;
        }
        San_Visitas item=lista_visitas.get(position);


        boolean isPlanificada=true;
        if (DAO_San_Visitas.TIPO_PLANIFICADA.equalsIgnoreCase(item.getTipo_vista())) {
            holder.txt_hora_visita.setText("Hora "+ VARIABLES.FromTime_parseTIME_Exacto12hs(item.getHora_inicio_ejecución()));//holder.txt_hora_visita.setText("");
            holder.txtFechaEjecutada.setVisibility(View.GONE);
            isPlanificada=true;
        }else{
            holder.txt_hora_visita.setText("H. Eje. "+ VARIABLES.FromTime_parseTIME_Exacto12hs(item.getHora_inicio_ejecución()));

            holder.txtFechaEjecutada.setVisibility(View.VISIBLE);
            isPlanificada=false;
        }

        final boolean finalIsPlanificada=isPlanificada;
        holder.RlayoutPrincipal.setOnClickListener(view -> listener.onClickAdapterAgenda(item, finalIsPlanificada));
        holder.img_show_menu.setOnClickListener(view -> {
            ShowPoputMenu(view, position, item, finalIsPlanificada);
        });

        String cumpleTXT= VARIABLES.returnFechaDescripcion(item.getDescripcion_fecha());
        String fechaEjecutada=VARIABLES.returnFechaDescripcion(item.getFecha_Ejecutada());

        if (cumpleTXT.length()>0 && isPlanificada){
            holder.txt_fecha.setVisibility(View.VISIBLE);
        }else{
            holder.txt_fecha.setVisibility(View.GONE);
        }


        holder.txt_fecha.setText(""+cumpleTXT);
        if (item.getOc_numero_visitar().contains("TPLAST")) {
            holder.txtNombreInstitucion.setText("TUBO PLAST - SIDIGE");
        }else{
            holder.txtNombreInstitucion.setText(item.getPromotor());
        }
        holder.txtFechaEjecutada.setText("Fecha ejecutada "+fechaEjecutada);
        holder.txt_estado.setText(item.getEstado());
        holder.txt_promotor.setText(""+item.getEjecutivo_Descripcion());
        holder.txt_gerente.setText(""+item.getCargo_Descripción());
        holder.descripcion_anulaciopn.setText(""+item.getDescripcion_anulacion());

        if (item.getDescripcion_anulacion().length()>0){
            pintarTXT(holder, context.getResources().getColor(R.color.primary_text));
        }else{
            if (item.getEstado().equalsIgnoreCase("libre")){
                pintarTXT(holder, context.getResources().getColor(R.color.red_500));
            }
            else if (item.getEstado().equalsIgnoreCase("completada")){
                pintarTXT(holder, context.getResources().getColor(R.color.blue_500));
            }
            if (item.getFecha_planificada().contains(item.getFecha_Ejecutada())){
                holder.isMismoPlanificada_y_Ejecutada.setVisibility(View.VISIBLE);
            }else holder.isMismoPlanificada_y_Ejecutada.setVisibility(View.INVISIBLE);
        }
    }


    private void pintarTXT(ViewHolder holder, int color){
        holder.txt_estado.setTextColor(color);
        holder.txt_promotor.setTextColor(color);
        holder.txt_gerente.setTextColor(color);
    }
    private void ShowPoputMenu(View view, int position ,final San_Visitas visita, boolean finalIsPlanificada){
        PopupMenu popupMenu=new PopupMenu(context, view,  R.style.amu_Bubble_TextAppearance_Light);
        popupMenu.getMenuInflater().inflate(R.menu.menu_san_visita, popupMenu.getMenu());
        Menu menus=popupMenu.getMenu();
        MenuItem menu_editar_visita=menus.findItem(R.id.menu_editar_visita);
        MenuItem menu_visitar=menus.findItem(R.id.menu_visitar);
        MenuItem menuAnular=menus.findItem(R.id.menu_anular_visita);
        MenuItem menu_reprogramar=menus.findItem(R.id.menu_reprogramar);

        String fechaConfiguracion=dBclasses.getFecha2();
        menu_editar_visita.setEnabled(false);
        if (finalIsPlanificada){//planificado

            String fechaPlanificada=VARIABLES.InvertirFechaCustom(visita.getFecha_planificada().substring(0, 10), "-", "/");

            if (fechaPlanificada.contains(fechaConfiguracion)){
                if(visita.getOc_numero_visitado().length()>0){
                    menu_editar_visita.setEnabled(true);
                    menu_visitar.setEnabled(false);
                    menu_reprogramar.setEnabled(false);
                }else{
                    menu_editar_visita.setEnabled(true);
                    menu_visitar.setEnabled(true);
                }
            }else {
                menu_editar_visita.setEnabled(true);
                menu_visitar.setEnabled(false);
            }

        }else{//esta visitado
            if (DAO_San_Visitas.isVisitado_la_planificacion(dBclasses.getReadableDatabase(), visita.getOc_numero_visitar())){

                //visita.getFecha_Ejecutada() yyyy-mm-dd
                String fechaViista=VARIABLES.InvertirFechaCustom(visita.getFecha_Ejecutada(), "-", "/");
                if (fechaConfiguracion.equals(fechaViista)) {
                    menu_editar_visita.setEnabled(true);
                }
                menu_visitar.setEnabled(false);
                menu_reprogramar.setEnabled(false);
            }
        }
        popupMenu.setOnMenuItemClickListener(item -> {

            switch (item.getItemId()){
                case R.id.menu_visualizar_visita:
                    listener.onClickAdapterAgenda(visita, finalIsPlanificada);
                    break;
                case R.id.menu_editar_visita:
                    if (!dBclasses.verificarPedidoTieneCabecera(dBclasses.getReadableDatabase(), visita.getOc_numero_visitar()) && visita.getOc_numero_visitar().length()>0){
                        UtilViewMensaje.MENSAJE_simple(context, "Sin permiso", "No tiene permiso para modificar la visita");
                    }else{
                        listener.onClickAdapterAgenda_editar(visita);
                    }
                    break;

                case R.id.menu_visitar:

                        listener.onClickAdapterAgenda_visita(visita, finalIsPlanificada);
                    break;
                case R.id.menu_reprogramar:
                    if (!dBclasses.verificarPedidoTieneCabecera(dBclasses.getReadableDatabase(), visita.getOc_numero_visitar())){
                        UtilViewMensaje.MENSAJE_simple(context, "Sin permiso", "No tiene permiso para reprogramar la visita");
                    }else{
                        listener.onClickAdapterAgenda_reprogramar(visita, finalIsPlanificada);
                    }

                    break;
                case R.id.menu_anular_visita:
                    if (!dBclasses.verificarPedidoTieneCabecera(dBclasses.getReadableDatabase(), visita.getOc_numero_visitar())){
                        UtilViewMensaje.MENSAJE_simple(context, "Sin permiso", "No tiene permiso para anular la visita");
                        return true;
                    }

                    UtilView.AlertViewSimpleConEdittext dd=new UtilView.AlertViewSimpleConEdittext(context);
                    dd.titulo="Anulación";
                    dd.mensaje="Ingrese la descripción del motivo";
                    dd.min_caracteres=10;
                    dd.hint="Ingrese al menos "+dd.min_caracteres+" caracteres";
                    dd.texto_cargado=visita.getDescripcion_anulacion();
                    dd.start(new UtilView.AlertViewSimpleConEdittext.Listener() {
                        @Override
                        public String resultOK(String descripcion_anulacion) {
                            if (descripcion_anulacion==null){
                                return null;
                            }
                            if (DAO_San_Visitas.setComentarioAnulacionVisita(dBclasses.getReadableDatabase(), visita.getOc_numero_visitar(), descripcion_anulacion)) {
                                dBclasses.cambiar_flag_pedido_cabecera(visita.getOc_numero_visitar(), "P");
                                WS_San_Visitas ws_san_visitas=new WS_San_Visitas(context, dBclasses);
                                ws_san_visitas.finalizarActitivty=false;
                                ws_san_visitas.EnviarVisitasByOc_numero(visita.getOc_numero_visitar());
                            }else{
                                UtilViewMensaje.MENSAJE_simple(context,"OH!", "No se puedo completar la acción solicitada. Vuelva a intentarlo.");
                            }
                            try {
                                lista_visitas.get(position).setDescripcion_anulacion(descripcion_anulacion);
                                notifyItemChanged(position);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        public String resultBucle(String s) {
                            return null;
                        }
                    });
                    break;
            }
            return true;
        });
        popupMenu.show();
    }

    @Override
    public int getItemCount() {
        if (lista_visitas.size()>0) return lista_visitas.size();
        else return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout RlayoutPrincipal;
        TextView isMismoPlanificada_y_Ejecutada,
                txt_fecha,txtNombreInstitucion,txt_hora_visita, txtFechaEjecutada, txt_estado, txt_promotor, txt_gerente, descripcion_anulaciopn;
        ImageView img_show_menu;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            RlayoutPrincipal=itemView.findViewById(R.id.RlayoutPrincipal);
            isMismoPlanificada_y_Ejecutada=itemView.findViewById(R.id.isMismoPlanificada_y_Ejecutada);
            txt_fecha=itemView.findViewById(R.id.txt_fecha);
            txtNombreInstitucion=itemView.findViewById(R.id.txtNombreInstitucion);
            txt_hora_visita=itemView.findViewById(R.id.txt_hora_visita);
            txtFechaEjecutada=itemView.findViewById(R.id.txtFechaEjecutada);
            txt_estado=itemView.findViewById(R.id.txt_estado);
            txt_promotor=itemView.findViewById(R.id.txt_promotor);
            txt_gerente=itemView.findViewById(R.id.txt_gerente);
            descripcion_anulaciopn=itemView.findViewById(R.id.descripcion_anulaciopn);
            img_show_menu=itemView.findViewById(R.id.img_show_menu);
            UtilView.Efectos_default(itemView.getContext(), img_show_menu);
        }
    }

    public interface IteracionListenerAgenda{
        void onClickAdapterAgenda(San_Visitas itemDia, boolean isPlanificada);
        void onClickAdapterAgenda_visita(San_Visitas itemDia, boolean isPlanificada);
        void onClickAdapterAgenda_editar(San_Visitas itemDia);
        void onClickAdapterAgenda_reprogramar(San_Visitas itemDia, boolean isPlanificada);
    }
}
