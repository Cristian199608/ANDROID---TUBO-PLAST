package com.example.sm_tubo_plast.genesys.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.Retrofit.Result.bean.ResultComprobante;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class ComprobantesRecyclerAdapter
        extends RecyclerView.Adapter<ComprobantesRecyclerAdapter.ViewHolder> {

    public interface OnComprobanteClickListener {
        void onProcesarClick(ResultComprobante comprobante);
    }

    private final ArrayList<ResultComprobante> lista;
    private final OnComprobanteClickListener listener;

    public ComprobantesRecyclerAdapter(
            ArrayList<ResultComprobante> lista,
            OnComprobanteClickListener listener) {

        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comprobantes, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position) {

        ResultComprobante comprobante = lista.get(position);

        holder.tvTipoDocumento.setText(
                comprobante.getTipo_documento()
        );

        holder.tvNumero.setText(
                comprobante.getSerie()
                        + "-"
                        + comprobante.getNumero()
        );

        holder.tvFecha.setText(
                comprobante.getFecha_emision()
        );

        holder.tvCliente.setText(
                comprobante.getCliente()
        );

        holder.tvRuc.setText(
                comprobante.getRuc()
        );

        holder.tvImporte.setText("S/ "+comprobante.getImporte_total());



        holder.tvEstado.setText(
                comprobante.getEstado()
        );

        holder.btnProcesar.setOnClickListener(v -> {

            if (listener != null) {
                listener.onProcesarClick(comprobante);
            }

        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvTipoDocumento;
        TextView tvNumero;
        TextView tvFecha;
        TextView tvCliente;
        TextView tvRuc;
        TextView tvImporte;
        TextView tvEstado;

        Button btnProcesar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTipoDocumento = itemView.findViewById(R.id.tvTipoDocumento);
            tvNumero = itemView.findViewById( R.id.tvNumero);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvCliente = itemView.findViewById(R.id.tvCliente);
            tvRuc = itemView.findViewById(R.id.tvRuc);
            tvImporte = itemView.findViewById(R.id.tvImporte);
            tvEstado = itemView.findViewById(R.id.tvEstado);
            btnProcesar = itemView.findViewById(R.id.btnProcesar);
        }
    }
}