package com.example.sm_tubo_plast.genesys.adapters;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.BEAN.ReportePedidoCabeceraBEAN;
import com.example.sm_tubo_plast.genesys.datatypes.DBclasses;
import com.example.sm_tubo_plast.genesys.domain.IReportePedidoCabecera;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.Reportes.ReportesPedidosCotizacionYVisitaActivity;

import java.util.ArrayList;

public class ReportesPedidosCabeceraRecyclerView extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_FOOTER = 1;
    Activity activity;
    ArrayList<IReportePedidoCabecera> IdataCabceraLista;
    DBclasses dBclasses;
    boolean enableFooterView = false;
    View layoutFooter;

    MyCallback myCallback;
    MyCallbackLoadMoreData myCallbackLoadMoreData;
    public interface MyCallback{
        void onClikItem(int position, View view);
    }
    public interface MyCallbackLoadMoreData{
        void onLoad();
    }
    public ReportesPedidosCabeceraRecyclerView(Activity activity, DBclasses dBclasses, ArrayList<IReportePedidoCabecera> reportePedidoCabeceraArrayList) {
        this.activity = activity;
        this.dBclasses = dBclasses;
        this.IdataCabceraLista = reportePedidoCabeceraArrayList;
    }
    public void clearDataAndReset(){
        this.IdataCabceraLista.clear();
        myCallbackLoadMoreData=null;
        if(layoutFooter!=null){
            layoutFooter.setVisibility(View.GONE);
        }
        notifyDataSetChanged();
    }
    //set onclick interface
    public void setOnClick(MyCallback myCallback){
        this.myCallback=myCallback;
    }
    public void setCallbackLoadMoreData(MyCallbackLoadMoreData myCallback){
        this.myCallbackLoadMoreData=myCallback;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //create view
        if(viewType==VIEW_TYPE_ITEM){
            View singleRow = LayoutInflater.from(parent.getContext())  .inflate(R.layout.item_reporte_pedido, parent, false);
            return new ViewHolderItem(singleRow);
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_view_progress, parent, false);
            return new ViewHolderFotter(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holderType, int position) {
        if(holderType instanceof ViewHolderItem){
            ViewHolderItem holder = (ViewHolderItem) holderType;
            IReportePedidoCabecera IitemReporte=   IdataCabceraLista.get(position);
            IitemReporte.setViewByHolder(activity, dBclasses, holder, position);
            OnClickCustom(holder.imgCampanaYellow);
            holder.itemView.setOnClickListener(v ->{
                if(myCallback!=null){
                    myCallback.onClikItem(position, v);
                }
            });
            if(position+1==IdataCabceraLista.size() && myCallbackLoadMoreData!=null){//cargamos mas datos
                myCallbackLoadMoreData.onLoad();
            }
        }else{
            layoutFooter= ((ViewHolderFotter)holderType).itemView;
            if(!enableFooterView)removeFooterView();
        }

    }

    @Override
    public int getItemCount() {
        return IdataCabceraLista.size()+(1);
    }

    @Override
    public int getItemViewType(int position) {
        // Devuelve el tipo de vista según la posición
        return (position == IdataCabceraLista.size()) ? VIEW_TYPE_FOOTER : VIEW_TYPE_ITEM;
    }
    public static class ViewHolderItem extends RecyclerView.ViewHolder {
        public TextView nomcliente, tipopago, total, numoc, estado, moneda, tv_pedidoAnterior, labell, edtFechavisita, edtObservacion_pedido;
        public ImageView foto;
        public TextView tv_tipoRegistro, imgCampanaYellow;

        public ViewHolderItem(@NonNull View convertView) {
            super(convertView);

            nomcliente = (TextView) convertView.findViewById(R.id.rpt_pedido_tv_cliente);
            tipopago = (TextView) convertView.findViewById(R.id.rpt_pedido_tv_tipopago);
            total = (TextView) convertView.findViewById(R.id.rpt_pedido_tv_total);
            numoc = (TextView) convertView.findViewById(R.id.rpt_pedido_tv_oc_numero);
            estado = (TextView) convertView.findViewById(R.id.rpt_pedido_tv_estado);
            foto = (ImageView) convertView.findViewById(R.id.item_reporte_pedido_flecha);
            moneda = (TextView) convertView.findViewById(R.id.tv_monedaReporte);
            tv_tipoRegistro = (TextView) convertView.findViewById(R.id.tv_tipoRegistro);
            tv_pedidoAnterior = (TextView) convertView.findViewById(R.id.rpt_pedido_tv_pedidoAnterior);
            labell = (TextView) convertView.findViewById(R.id.labell);
            imgCampanaYellow = (TextView) convertView.findViewById(R.id.imgCampanaYellow);
            edtObservacion_pedido = (TextView) convertView.findViewById(R.id.edtObservacion_pedido);
            edtFechavisita = (TextView) convertView.findViewById(R.id.edtFechavisita);
        }
    }

    public static class ViewHolderFotter extends RecyclerView.ViewHolder {

        public ViewHolderFotter(@NonNull View itemView) {
            super(itemView);
        }
    }

    private void OnClickCustom(View view){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(activity,"Producto guardado con observación, pues el descuento supera a 3%", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
    }
    public void removeFooterView() {
        enableFooterView=false;
        if(layoutFooter!=null)layoutFooter.setVisibility(View.GONE);
    }

    public void addFooterView() {
        enableFooterView=true;
        if(layoutFooter!=null)layoutFooter.setVisibility(View.VISIBLE);
    }
}
