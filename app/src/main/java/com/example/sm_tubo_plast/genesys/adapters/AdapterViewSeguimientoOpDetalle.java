 package com.example.sm_tubo_plast.genesys.adapters;


import android.app.Activity;
import android.os.Build;
import android.text.Html;
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
import com.example.sm_tubo_plast.genesys.BEAN.ViewSeguimientoPedido;
import com.example.sm_tubo_plast.genesys.BEAN.ViewSeguimientoPedidoDetalle;
import com.example.sm_tubo_plast.genesys.util.VARIABLES;

import java.util.ArrayList;
import java.util.HashMap;

 public class AdapterViewSeguimientoOpDetalle extends RecyclerView.Adapter<AdapterViewSeguimientoOpDetalle.ViewHolder> implements View.OnClickListener {

    ArrayList<ViewSeguimientoPedidoDetalle> data;
    Activity activity;

    View.OnClickListener miListener;
    MyListener myListener;

    ArrayList<Integer> positionCab=new ArrayList<>();
    double saldoTotal;


    public AdapterViewSeguimientoOpDetalle(Activity activity, double saldoTotal, ArrayList<ViewSeguimientoPedidoDetalle> data, MyListener myListener) {
        this.data = data;
        this.activity = activity;
        this.myListener=myListener;
        this.saldoTotal=saldoTotal;
        String tipomovimiento_numdoc="";
        for (int i = 0; i < this.data.size(); i++) {
            if (!(data.get(i).getMovimiento()+data.get(i).getNumero_op()).equals(tipomovimiento_numdoc)){
                tipomovimiento_numdoc=data.get(i).getMovimiento()+data.get(i).getNumero_op();
                positionCab.add(i);
            }
        }
    }

    public void clearData(){
        data.clear();
        positionCab.clear();
        saldoTotal=0;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_seguimiento_op_detalle, null,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
//        holder.radioButton.setId(position);
        ViewSeguimientoPedidoDetalle item=data.get(position);
//        holder.txtItem.setText(item.getItem());
        String tipoM=item.getMovimiento();

        SetTextoFromHtml(holder.txtProducto, "<font color=#212121> <strong>"+item.getItem().trim()+")</strong> "+item.getProducto()+"</font>");
        holder.txtCantidad.setText(""+item.getCantidad_comprado());
        holder.txtEntregado.setText(""+(int)item.getCantidad_entregado());
        holder.txtSaldo.setText(""+(int)(item.getCantidad_comprado()-item.getCantidad_entregado()));
        holder.txtFechaEntregado.setText("Fecha Ent:\n"+item.getFecha_entrega());
        holder.txtStock.setText("Stock: \n"+(item.getStock_actual()-item.getStock_separado()));
        holder.txtDetalleMontoCabecera.setText(item.getMoneda()+":"+VARIABLES.formater_one_decimal.format( tipoM.equals("OP")?saldoTotal:item.getMonto_total()));

        holder.txtDetalleCabecera.setText(item.getMovimiento()+" "+item.getNumero_op()+" Rec: "+item.getFecha_apertura());
        for (int i = 0; i < positionCab.size(); i++) {
            if ( positionCab.get(i)==position){
                holder.layoutCabecera.setVisibility(View.VISIBLE);
                break;
            }else{
                holder.layoutCabecera.setVisibility(View.GONE);
            }
        }
        String dataComprado="<font color=#212121> <strong>Cant= </strong> "+item.getCantidad_comprado()+""+gestionarMonto(tipoM,item.getMonto_total())+"</font>";
        String dataEntegado="<font color=#212121> <strong>Entr= </strong> "+item.getCantidad_entregado()+""+gestionarMonto(tipoM, item.getMonto_total()-item.getMonto_saldo())+"</font>";
        String dataSaldo="<font color=#212121> <strong>Sald= </strong> "+(item.getCantidad_comprado()-item.getCantidad_entregado())+""+gestionarMonto(tipoM, item.getMonto_saldo())+"</font>";
        SetTextoFromHtml(holder.txtCantidad, dataComprado);
        SetTextoFromHtml(holder.txtEntregado,dataEntegado);
        SetTextoFromHtml(holder.txtSaldo, dataSaldo);

//        holder.txtSaldo.setText("Saldo: "+VARIABLES.formater_thow_decimal.format(item.getSaldo()));
        if (position %2 == 0) {
            holder.itemView.setBackgroundColor(activity.getResources().getColor( tipoM.equals("GR02")?R.color.green_100:(tipoM.equals("GROP")?R.color.red_100:R.color.grey_200)));
        } else {
            holder.itemView.setBackgroundColor(activity.getResources().getColor(tipoM.equals("GR02")?R.color.green_50:(tipoM.equals("GROP")?R.color.red_50:R.color.white)));
        }

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

    private String gestionarMonto(String tipo_movimiento, double valor){
        if (tipo_movimiento.equals("GR02") || tipo_movimiento.equals("GROP")){
            return  "";
        }else{
           return  " | "+VARIABLES.formater_thow_decimal.format(valor);
        }

    }
    private void SetTextoFromHtml(TextView textView, String text){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(text , Html.FROM_HTML_MODE_LEGACY));
        } else {
            textView.setText(Html.fromHtml(text));
        }
    }

    public void setOnClickListenerOP(View.OnClickListener miListener){
        this.miListener=miListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtDetalleCabecera, txtProducto, txtCantidad, txtEntregado, txtSaldo, txtFechaEntregado, txtDetalleMontoCabecera, txtStock;
        LinearLayout layoutCabecera;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDetalleCabecera= itemView.findViewById(R.id.txtDetalleCabecera);
//            txtItem= itemView.findViewById(R.id.txtItem);
            txtProducto= itemView.findViewById(R.id.txtProducto);
            txtCantidad= itemView.findViewById(R.id.txtCantidad);
            txtEntregado= itemView.findViewById(R.id.txtEntregado);
            txtSaldo= itemView.findViewById(R.id.txtSaldo);
            txtFechaEntregado= itemView.findViewById(R.id.txtFechaEntregado);
            txtStock= itemView.findViewById(R.id.txtStock);
            txtDetalleMontoCabecera= itemView.findViewById(R.id.txtDetalleMontoCabecera);
            layoutCabecera= itemView.findViewById(R.id.layoutCabecera);
//            txtMonto= itemView.findViewById(R.id.txtMonto);
//            UtilView.Efectos(itemView.getContext(), imgGoMaps, R.color.yellow_500);
        }
    }
    public interface MyListener{
        int DistanciaEntreDosPuntos(double latitud, double logitud);
    }
}
