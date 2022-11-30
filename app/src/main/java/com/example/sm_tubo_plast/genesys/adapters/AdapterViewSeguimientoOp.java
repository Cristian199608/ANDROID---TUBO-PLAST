package com.example.sm_tubo_plast.genesys.adapters;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.BEAN.Cliente;
import com.example.sm_tubo_plast.genesys.BEAN.MyItem;
import com.example.sm_tubo_plast.genesys.BEAN.ViewSeguimientoPedido;
import com.example.sm_tubo_plast.genesys.util.UtilView;
import com.example.sm_tubo_plast.genesys.util.VARIABLES;

import java.util.ArrayList;

public class AdapterViewSeguimientoOp extends RecyclerView.Adapter<AdapterViewSeguimientoOp.ViewHolder> implements View.OnClickListener {

    ArrayList<ViewSeguimientoPedido> data;
    Activity activity;

    View.OnClickListener miListener;
    MyListener myListener;

    public AdapterViewSeguimientoOp(Activity activity,ArrayList<ViewSeguimientoPedido> data, MyListener myListener) {
        this.data = data;
        this.activity = activity;
        this.myListener=myListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_seguimiento_op, null,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.layoutSeleccion.setId(position);
        ViewSeguimientoPedido item=data.get(position);
        holder.txtOrdenCompra.setText("Orden Compra: "+item.getOrden_compra()+"  | "+ (item.getMoneda().equals("MN")?"SOLES": "DOLARES"));
        holder.txtNumeroOP.setText(""+item.getNum_op());
//        holder.txtNumeroPedido.setText("O.P.: "+item.getNum_op());
        holder.txtTotal.setText("TOTAL: ("+item.getMoneda()+")"+VARIABLES.formater_thow_decimal.format(item.getTotal()));
        double porcentajeEntMonto=item.getMonto_total_entregado()*100/item.getTotal();
        holder.txtTtxtDespachadootal.setText("("+item.getMoneda()+")"+VARIABLES.formater_thow_decimal.format(item.getMonto_total_entregado())
                +" ["+VARIABLES.formater_one_decimal.format(porcentajeEntMonto)+"%]" );

        double porcentajeSaldoMonto=(item.getSaldo()*100/item.getTotal());
        holder.txtSaldo.setText("("+item.getMoneda()+")"+VARIABLES.formater_thow_decimal.format(item.getSaldo())
                +" ["+VARIABLES.formater_one_decimal.format(porcentajeSaldoMonto)+"%]" );

        holder.txtFechaRec.setText(item.getFecha_rect());
        holder.txtAutorizada.setText(item.getFecha_autorizado());
        holder.txtCantGuias.setText(""+item.getCant_guias());

        double porcentajeGROPMonto=(item.getMonto_grop())*100/item.getTotal();
        holder.txtMontoGrop.setText("("+item.getMoneda()+")"+VARIABLES.formater_thow_decimal.format(item.getMonto_grop())
                +"["+VARIABLES.formater_one_decimal.format(porcentajeGROPMonto)+"%]" );

        holder.txtPrimerEntrega.setText(""+item.getPrimer_entrega());
        holder.txtUltimaEntrega.setText(""+item.getUltima_entrega());
        holder.txtDiasEntrega.setText(""+item.getDias());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onClick(View view) {
        if(miListener!=null){
            miListener.onClick(view);
        }
    }

    public void setOnClickListenerOP(View.OnClickListener miListener){
        this.miListener=miListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrdenCompra,  txtNumeroOP, txtTotal, txtTtxtDespachadootal, txtSaldo, txtFechaRec, txtAutorizada, txtCantGuias,
                txtMontoGrop, txtPrimerEntrega, txtUltimaEntrega, txtDiasEntrega;
        LinearLayout layoutSeleccion;
        ImageView imgGoMaps;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrdenCompra= itemView.findViewById(R.id.txtOrdenCompra);
            layoutSeleccion= itemView.findViewById(R.id.layoutSeleccion);
            txtTotal= itemView.findViewById(R.id.txtTotal);
            txtNumeroOP= itemView.findViewById(R.id.txtNumeroOP);
            txtSaldo= itemView.findViewById(R.id.txtSaldo);
            txtTtxtDespachadootal= itemView.findViewById(R.id.txtDespachado);
            txtFechaRec= itemView.findViewById(R.id.txtFechaRec);
            txtAutorizada= itemView.findViewById(R.id.txtAutorizada);
            txtCantGuias= itemView.findViewById(R.id.txtCantGuias);
            txtMontoGrop= itemView.findViewById(R.id.txtMontoGrop);
            txtPrimerEntrega= itemView.findViewById(R.id.txtPrimerEntrega);
            txtUltimaEntrega= itemView.findViewById(R.id.txtUltimaEntrega);
            txtDiasEntrega= itemView.findViewById(R.id.txtDiasEntrega);
            layoutSeleccion.setOnClickListener(AdapterViewSeguimientoOp.this);
        }
    }
    public interface MyListener{
        int DistanciaEntreDosPuntos(double latitud, double logitud);
    }
}
