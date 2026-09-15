package com.example.sm_tubo_plast.genesys.fuerza_ventas.cliente.AnticiposCliente.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.cliente.AnticiposCliente.beanView.AnticiposClienteUtil;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Locale;

public class AnticiposClienteAdapter
        extends RecyclerView.Adapter<AnticiposClienteAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<AnticiposClienteUtil> lista;

    public AnticiposClienteAdapter(
            Context context,
            ArrayList<AnticiposClienteUtil> lista) {

        this.context = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(
                        R.layout.item_anticipo_cliente,
                        parent,
                        false
                );

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position) {

        AnticiposClienteUtil item = lista.get(position);

        holder.tvDocumento.setText(
                item.getSerie_doc() + "-" + item.getNumero_doc()
        );

        holder.tvObservacion.setText(
                item.getObs() != null ? item.getObs() : ""
        );

        holder.tvMontoDisponible.setText(
                String.format(
                        Locale.US,
                        "S/ %.2f",
                        item.getMonto() != null ? item.getMonto() : 0.00
                )
        );

        holder.chkAnticipo.setOnCheckedChangeListener(null);

        holder.chkAnticipo.setChecked(
                item.isSeleccionado()
        );

        if (item.isSeleccionado()) {

            holder.tilMonto.setVisibility(View.VISIBLE);

            holder.etMonto.setText(
                    String.format(
                            Locale.US,
                            "%.2f",
                            item.getMontoSeleccionado()
                    )
            );

        } else {

            holder.tilMonto.setVisibility(View.GONE);
        }

        holder.chkAnticipo.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {

                    item.setSeleccionado(isChecked);

                    if (isChecked) {

                        holder.tilMonto.setVisibility(View.VISIBLE);

                        if (item.getMontoSeleccionado() <= 0) {

                            item.setMontoSeleccionado(
                                    item.getMonto() != null
                                            ? item.getMonto()
                                            : 0.00
                            );
                        }

                        holder.etMonto.setText(
                                String.format(
                                        Locale.US,
                                        "%.2f",
                                        item.getMontoSeleccionado()
                                )
                        );

                    } else {

                        holder.tilMonto.setVisibility(View.GONE);
                    }
                }
        );

        holder.etMonto.setOnFocusChangeListener(
                (v, hasFocus) -> {

                    if (!hasFocus) {

                        String texto = holder.etMonto
                                .getText()
                                .toString()
                                .trim();

                        if (texto.isEmpty()) {
                            return;
                        }

                        try {

                            double monto = Double.parseDouble(texto);

                            double maximo = item.getMonto() != null
                                    ? item.getMonto()
                                    : 0.00;

                            if (monto < 1.00) {

                                holder.etMonto.setError(
                                        "El monto mínimo es S/ 1.00"
                                );

                                return;
                            }

                            if (monto > maximo) {

                                holder.etMonto.setError(
                                        "El monto máximo es S/ " +
                                                String.format(
                                                        Locale.US,
                                                        "%.2f",
                                                        maximo
                                                )
                                );

                                return;
                            }

                            item.setMontoSeleccionado(monto);

                        } catch (Exception e) {

                            holder.etMonto.setError(
                                    "Monto inválido"
                            );
                        }
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public ArrayList<AnticiposClienteUtil> getLista() {
        return lista;
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder {

        CheckBox chkAnticipo;
        TextView tvDocumento;
        TextView tvObservacion;
        TextView tvMontoDisponible;
        TextInputLayout tilMonto;
        TextInputEditText etMonto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            chkAnticipo = itemView.findViewById(R.id.chkAnticipo);
            tvDocumento = itemView.findViewById(R.id.tvDocumento);
            tvObservacion = itemView.findViewById(R.id.tvObservacion);
            tvMontoDisponible = itemView.findViewById(R.id.tvMontoDisponible);
            tilMonto = itemView.findViewById(R.id.tilMonto);
            etMonto = itemView.findViewById(R.id.etMonto);
        }
    }
}