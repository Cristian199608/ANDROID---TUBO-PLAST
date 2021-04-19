package com.example.sm_tubo_plast.genesys.adapters;


import android.content.Context;
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
import com.example.sm_tubo_plast.genesys.datatypes.DBclasses;
import com.example.sm_tubo_plast.genesys.util.UtilView;
import com.example.sm_tubo_plast.genesys.util.VARIABLES;

import java.util.ArrayList;

public class AdapterAgendaActividades extends RecyclerView.Adapter<AdapterAgendaActividades.ViewHolder> {

    Context context;
    DBclasses dBclasses;
    ArrayList<San_Visitas> lista_visitas;
    private   IteracionListenerAgenda listener=null;
    String fecha_seleccionado="";

    public AdapterAgendaActividades(Context context, DBclasses dBclasses, ArrayList<San_Visitas> lista_visitas, String fecha_seleccionado) {
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
            holder.txt_hora_visita.setText("");
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
            ShowPoputMenu(view, item, finalIsPlanificada);
        });

        String cumpleTXT= VARIABLES.returnFechaDescripcion(item.getDescripcion_fecha());
        String fechaEjecutada=VARIABLES.returnFechaDescripcion(item.getFecha_Ejecutada());

        if (cumpleTXT.length()>0 && isPlanificada){
            holder.txt_fecha.setVisibility(View.VISIBLE);
        }else{
            holder.txt_fecha.setVisibility(View.GONE);
        }


        holder.txt_fecha.setText(""+cumpleTXT);
        holder.txtNombreInstitucion.setText(item.getDescripcion_Colegio());
        holder.txtFechaEjecutada.setText("Fecha ejecutada "+fechaEjecutada);
        holder.txt_estado.setText(item.getEstado());
        holder.txt_promotor.setText(""+item.getEjecutivo_Descripcion());
        holder.txt_gerente.setText(""+item.getCargo_Descripción());

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


    private void pintarTXT(ViewHolder holder, int color){
        holder.txt_estado.setTextColor(color);
        holder.txt_promotor.setTextColor(color);
        holder.txt_gerente.setTextColor(color);
    }
    private void ShowPoputMenu(View view,final San_Visitas visita, boolean finalIsPlanificada){
        PopupMenu popupMenu=new PopupMenu(context, view,  R.style.amu_Bubble_TextAppearance_Light);
        popupMenu.getMenuInflater().inflate(R.menu.menu_san_visita, popupMenu.getMenu());
        Menu menus=popupMenu.getMenu();
        MenuItem menuItem=menus.findItem(R.id.menu_editar_visita);
        if (finalIsPlanificada){//planificado y hecho para la proxima_visita
            menuItem.setTitle("Visitar");
            if (visita.getFecha_planificada().contains(VARIABLES.GET_FECHA_ACTUAL_STRING())){
                if(visita.getOc_numero_visitado().length()>0){
                    menuItem.setTitle("Ya esta visitado");
                    menuItem.setEnabled(false);
                }
            }else menuItem.setEnabled(false);

        }else{//por visitar otro día
            if (DAO_San_Visitas.isVisitado_la_planificacion(dBclasses.getReadableDatabase(), visita.getOc_numero_visitado())){
                menuItem.setTitle("Edición no disponible");
                menuItem.setEnabled(false);
            }
        }
        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()){
                case R.id.menu_visualizar_visita:
                    listener.onClickAdapterAgenda(visita, finalIsPlanificada);
                    break;
                case R.id.menu_editar_visita:
                    listener.onClickAdapterAgenda_editar(visita, finalIsPlanificada);
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
                txt_fecha,txtNombreInstitucion,txt_hora_visita, txtFechaEjecutada, txt_estado, txt_promotor, txt_gerente;
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
            img_show_menu=itemView.findViewById(R.id.img_show_menu);
            UtilView.Efectos_default(itemView.getContext(), img_show_menu);
        }
    }

    public interface IteracionListenerAgenda{
        void onClickAdapterAgenda(San_Visitas itemDia, boolean isPlanificada);
        void onClickAdapterAgenda_editar(San_Visitas itemDia, boolean isPlanificada);
    }
}
