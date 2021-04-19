package com.example.sm_tubo_plast.genesys.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.BEAN.BEAN_AgendaCalendario;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AdapterDiasCalendario extends RecyclerView.Adapter<AdapterDiasCalendario.ViewHolder> implements View.OnClickListener{

    private final String TAg = this.getClass().getSimpleName();
    private View.OnClickListener listener;
    Context context;
    ArrayList<BEAN_AgendaCalendario> listaAgenda;

    Date fecha_hoy;
    public AdapterDiasCalendario(Context context,ArrayList<BEAN_AgendaCalendario> listaAgenda) {
        this.listaAgenda = listaAgenda;
        this.context = context;
        fecha_hoy= Calendar.getInstance().getTime();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vistaGeneral= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dias_calendario, null, false);
        vistaGeneral.findViewById(R.id.layoutContainer).setOnClickListener(listener);
        return new ViewHolder(vistaGeneral);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        BEAN_AgendaCalendario agenda=listaAgenda.get(position);

        holder.layoutContainer.setId(position);


        holder.txt_nro_dias.setText(""+agenda.getDia());
        holder.txt_cant_ejecutada.setText(""+agenda.getCant_ejecutada());
        holder.txt_cant_proxima_visita.setText(""+agenda.getCant_proxima_visita());
        holder.txt_programadaTaller.setText(""+agenda.getT_cant_programada());
        holder.txt_cant_cumpleano.setText(""+agenda.getCant_cumpleano());


        if (agenda.getCant_ejecutada()+agenda.getCant_proxima_visita()+agenda.getT_cant_programada()/*+agenda.getCant_cumpleano()*/>0){
            holder.layoutCampo.setVisibility(View.VISIBLE);
            holder.layoutTaller.setVisibility(View.VISIBLE);
            holder.txt_cant_ejecutada.setVisibility(View.VISIBLE);
            holder.txt_cant_proxima_visita.setVisibility(View.VISIBLE);
            holder.txt_programadaTaller.setVisibility(View.VISIBLE);
            if (agenda.getCant_cumpleano()>0){
                holder.txt_cant_cumpleano.setVisibility(View.VISIBLE);
            }else holder.txt_cant_cumpleano.setVisibility(View.INVISIBLE);

        }else if(agenda.getCant_cumpleano()>0){
            holder.layoutCampo.setVisibility(View.INVISIBLE);
            holder.layoutTaller.setVisibility(View.VISIBLE);
            holder.txt_programadaTaller.setVisibility(View.INVISIBLE);
            holder.txt_cant_cumpleano.setVisibility(View.VISIBLE);
        }else{
            holder.layoutCampo.setVisibility(View.INVISIBLE);
            holder.layoutTaller.setVisibility(View.INVISIBLE);
            holder.txt_cant_ejecutada.setVisibility(View.INVISIBLE);
            holder.txt_cant_proxima_visita.setVisibility(View.INVISIBLE);
            holder.txt_programadaTaller.setVisibility(View.INVISIBLE);
            holder.txt_cant_cumpleano.setVisibility(View.INVISIBLE);
        }

        if (agenda.getDia()>0) holder.txt_nro_dias.setVisibility(View.VISIBLE);
        else holder.txt_nro_dias.setVisibility(View.INVISIBLE);



        if (position%7==0){
            if (agenda.isFechaActual()){
                holder.txt_nro_dias.setTextColor(context.getResources().getColor(R.color.pink_700));
            }else holder.txt_nro_dias.setTextColor(context.getResources().getColor(R.color.red_500));
        }else{
            if (agenda.isFechaActual()) holder.txt_nro_dias.setTextColor(context.getResources().getColor(R.color.blue_500));
            else holder.txt_nro_dias.setTextColor(context.getResources().getColor(R.color.grey_600));
        }

        if (agenda.getFondoColor()!=0){
            holder.layoutContainer.setBackgroundColor(agenda.getFondoColor());
        }


    }

    @Override
    public int getItemCount() {
        return listaAgenda.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }
    @Override
    public void onClick(View view) {
        if (listener!=null) {
            listener.onClick(view);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout layoutContainer;
        LinearLayout layoutCampo, layoutTaller;
        TextView txt_nro_dias, txt_cant_proxima_visita, txt_cant_ejecutada,  txt_programadaTaller,
                txt_cant_cumpleano;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutContainer=itemView.findViewById(R.id.layoutContainer);
            layoutCampo=itemView.findViewById(R.id.layoutCampo);
            layoutTaller=itemView.findViewById(R.id.layoutTaller);
            txt_nro_dias=itemView.findViewById(R.id.txt_nro_dias);
            txt_cant_proxima_visita=itemView.findViewById(R.id.txt_cant_proxima_visita);
            txt_cant_ejecutada=itemView.findViewById(R.id.txt_cant_ejecutada);
            txt_programadaTaller=itemView.findViewById(R.id.txt_programadaTaller);
            txt_cant_cumpleano=itemView.findViewById(R.id.txt_cant_cumpleano);
        }
    }
}
