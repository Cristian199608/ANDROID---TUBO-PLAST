package com.example.fuerzaventaschema.genesys.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fuerzaventaschema.R;
import com.example.fuerzaventaschema.genesys.BEAN.BEAN_AgendaCalendario;
import com.example.fuerzaventaschema.genesys.util.VARIABLES;

import java.util.ArrayList;

public class AdapterMesesCalendario extends RecyclerView.Adapter<AdapterMesesCalendario.ViewHolder> implements View.OnClickListener{

    private final String TAg = this.getClass().getSimpleName();
    private View.OnClickListener listener;
    private Context context;

    String[] MESES_anio;
    int ANIO=2019;
    int MES=0;
    ArrayList<AdapterDiasCalendario> listAdapterDias=new ArrayList<>();
    ArrayList<BEAN_AgendaCalendario> listaDias;

    private IteracionAdapterDias myListener=null;

    ViewHolder holder=null;

    public AdapterMesesCalendario(Context context,ArrayList<BEAN_AgendaCalendario>listaDias, int ANIO,  int MES) {
        this.context = context;
        this.listaDias = listaDias;
        this.ANIO=ANIO;
        this.MES=MES;
        if (context instanceof IteracionAdapterDias) {
            myListener = (IteracionAdapterDias) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " debes Instanciar la interfas IteracionAdapterDias in your activity");
        }
        MESES_anio= new String[]{VARIABLES.MESES[this.MES]};
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_calendario, null, false);
        //view.findViewById(R.id.txt_ln_producto).setOnClickListener(listener);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder vh, int position) {


        holder=vh;
        AdapterDiasCalendario adapterDias=new AdapterDiasCalendario(context, listaDias);
        listAdapterDias.add(adapterDias);

        GridLayoutManager gridCustom = new GridLayoutManager(context,7,GridLayoutManager.VERTICAL,false);
        holder.recyclerViewDias.setLayoutManager(gridCustom);
        holder.recyclerViewDias.setAdapter(listAdapterDias.get(position));
        holder.tv_nomBreMes.setText(MESES_anio[position]+" - "+ANIO);

        adapterDias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos=view.getId();
                myListener.onClickAdapterDias(listaDias.get(pos));
            }
        });

    }

    @Override
    public int getItemCount() {
        return MESES_anio.length;
    }

    @Override
    public void onClick(View view) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerViewDias;
        TextView tv_nomBreMes;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerViewDias=itemView.findViewById(R.id.recyclerViewDias);
            tv_nomBreMes=itemView.findViewById(R.id.tv_nomBreMes);
        }
    }

    public interface IteracionAdapterDias{
        void onClickAdapterDias(BEAN_AgendaCalendario itemDia);
    }

    public void  SimularCarga(String mes_anio){

        if (holder!=null){
            holder.tv_nomBreMes.setText(mes_anio);
            holder.recyclerViewDias.setVisibility(View.INVISIBLE);
        }
    }
}
