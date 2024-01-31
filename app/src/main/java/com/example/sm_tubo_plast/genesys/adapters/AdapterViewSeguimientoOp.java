package com.example.sm_tubo_plast.genesys.adapters;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.BEAN.ViewSeguimientoPedido;
import com.example.sm_tubo_plast.genesys.util.VARIABLES;

import java.util.ArrayList;

public class AdapterViewSeguimientoOp extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_FOOTER = 1;

    ArrayList<ViewSeguimientoPedido> dataOriginal=new ArrayList<>();
    ArrayList<ViewSeguimientoPedido> data=new ArrayList<>();
    Activity activity;
    boolean enableFooterView = false;
    View layoutFooter;

    View.OnClickListener miListener;
    MyListener myListener;
    MyCallbackLoadMoreData myCallbackLoadMoreData;


    public AdapterViewSeguimientoOp(Activity activity,ArrayList<ViewSeguimientoPedido> data, MyListener myListener) {
        this.data = data;
        this.dataOriginal.addAll(data);
        this.activity = activity;
        this.myListener=myListener;
    }
    public void clearDataAndReset(){
        this.data.clear();
        this.dataOriginal.clear();
        notifyDataSetChanged();
        myCallbackLoadMoreData=null;
        if(layoutFooter!=null){
            layoutFooter.setVisibility(View.GONE);
        }
    }
    public void clearDataFilter(){
        this.data.clear();
        notifyDataSetChanged();
    }
    public void setDataFilter(ArrayList<ViewSeguimientoPedido> data){
        this.data.addAll(data);
        notifyDataSetChanged();
    }
    public void addData(ArrayList<ViewSeguimientoPedido> data){
        this.data.addAll(data);
        this.dataOriginal.addAll(data);
        notifyDataSetChanged();
    }
    public ArrayList<ViewSeguimientoPedido> getDataOriginal(){
        return this.dataOriginal;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==VIEW_TYPE_ITEM){
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_seguimiento_op, null,false);
            return new ViewHolder(view);
        }
        else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_view_progress, parent, false);
            return new ViewHolderFotter(view);
        }

    }

    @Override
    public int getItemViewType(int position) {
        // Devuelve el tipo de vista según la posición
        return (position == data.size()) ? VIEW_TYPE_FOOTER : VIEW_TYPE_ITEM;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holderType, final int position) {
        if(holderType instanceof ViewHolderFotter){
            ViewHolderFotter holder=(ViewHolderFotter)holderType;
            layoutFooter=holder.itemView;
            if (!enableFooterView)removeFooterView();
        }
        else if (holderType instanceof ViewHolder){
            ViewHolder holder=(ViewHolder)holderType;
            holder.layoutSeleccion.setId(position);
            ViewSeguimientoPedido item=data.get(position);
            holder.txtItem.setText(""+(position+1));
            holder.txtNombreCliente.setText(item.getNomcli()+"\nOrden Compra: "+item.getOrden_compra()+"  | "+ (item.getMoneda().equals("MN")?"SOLES": "DOLARES"));
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

            if(position==data.size()-1 && myCallbackLoadMoreData!=null){
                myCallbackLoadMoreData.onLoad();
            }
        }


    }

    @Override
    public int getItemCount() {
        return data.size()+1;
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
        TextView txtItem, txtNombreCliente,  txtNumeroOP, txtTotal, txtTtxtDespachadootal, txtSaldo, txtFechaRec, txtAutorizada, txtCantGuias,
                txtMontoGrop, txtPrimerEntrega, txtUltimaEntrega, txtDiasEntrega;
        LinearLayout layoutSeleccion;
        ImageView imgGoMaps;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtItem = itemView.findViewById(R.id.txtItem);
            txtNombreCliente = itemView.findViewById(R.id.txtNombreCliente);
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
    public static class ViewHolderFotter extends RecyclerView.ViewHolder {

        public ViewHolderFotter(@NonNull View itemView) {
            super(itemView);
        }
    }
    public void setCallbackLoadMoreData(MyCallbackLoadMoreData myCallback){
        this.myCallbackLoadMoreData=myCallback;
    }
    public void removeFooterView() {
        enableFooterView=false;
        if(layoutFooter!=null)layoutFooter.setVisibility(View.GONE);
    }

    public void addFooterView() {
        enableFooterView=true;
        if(layoutFooter!=null)layoutFooter.setVisibility(View.VISIBLE);
    }

    public interface MyListener{
        int DistanciaEntreDosPuntos(double latitud, double logitud);
    }
    public interface MyCallbackLoadMoreData{
        void onLoad();
    }


}
