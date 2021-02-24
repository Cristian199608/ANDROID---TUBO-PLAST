package com.example.fuerzaventaschema.genesys.adapters;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fuerzaventaschema.R;
import com.example.fuerzaventaschema.genesys.BEAN.Cliente;
import com.example.fuerzaventaschema.genesys.BEAN.MyItem;

import com.example.fuerzaventaschema.genesys.util.UtilView;
import com.example.fuerzaventaschema.genesys.util.VARIABLES;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class AdapterCLientesMap extends RecyclerView.Adapter<AdapterCLientesMap.ViewHolder> implements View.OnClickListener {

    ArrayList<MyItem> mclientes;
    Activity activity;

    View.OnClickListener miListener;
    MyListener myListener;

    public AdapterCLientesMap(Activity activity, ArrayList<MyItem> mcliente, MyListener myListener) {
        this.mclientes = mcliente;
        this.activity = activity;
        this.myListener=myListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_cliente_map, null,false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.itemView.setId(position);

        final Cliente item=(Cliente)(mclientes.get(position)).getTag();
        holder.tv_cliente.setText(item.getNombre());
        holder.tv_telefono.setText(item.getDb_direccionClientes().getTelefono());
        holder.iten_direccion.setText(item.getDb_direccionClientes().getDireccion());

        int distancia=myListener.DistanciaEntreDosPuntos(mclientes.get(position).getPosition().latitude, mclientes.get(position).getPosition().longitude);
        holder.item_distancia.setText("Distancia apróx. "+ VARIABLES.ConvertirKmToString(distancia));


        holder.imgGoMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UtilView.OperServicioMapas(activity,v, (mclientes.get(position).getPosition()), LinearLayout.VERTICAL);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mclientes.size();
    }

    @Override
    public void onClick(View view) {
        if(miListener!=null){
            miListener.onClick(view);
        }
    }

    public void setOnClickListenerClientes(View.OnClickListener miListener){
        this.miListener=miListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_cliente, tv_telefono, iten_direccion, item_distancia;
        ImageView imgGoMaps;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_cliente=itemView.findViewById(R.id.tv_cliente);
            tv_telefono=itemView.findViewById(R.id.tv_telefono);
            iten_direccion=itemView.findViewById(R.id.iten_direccion);
            item_distancia=itemView.findViewById(R.id.item_distancia);
            imgGoMaps=itemView.findViewById(R.id.imgGoMaps);

            UtilView.Efectos(itemView.getContext(), imgGoMaps, R.color.yellow_500);
        }
    }
    public interface MyListener{
        int DistanciaEntreDosPuntos(double latitud, double logitud);
    }
}
