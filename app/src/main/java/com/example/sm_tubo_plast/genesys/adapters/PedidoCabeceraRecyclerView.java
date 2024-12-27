package com.example.sm_tubo_plast.genesys.adapters;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.BEAN.DataCabeceraPDF;
import com.example.sm_tubo_plast.genesys.CreatePDF.model.ReportePedidoDetallePDF;
import com.example.sm_tubo_plast.genesys.CreatePDF.PDF;
import com.example.sm_tubo_plast.genesys.DAO.DAO_ReportePedido;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.Reportes.ViewPdfActivity;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class PedidoCabeceraRecyclerView extends RecyclerView.Adapter<PedidoCabeceraRecyclerView.DbViewHolder>{

    ArrayList<DataCabeceraPDF> dataCabeceraArrayList;
    DAO_ReportePedido mydbClass;
    public PedidoCabeceraRecyclerView(DAO_ReportePedido mydbClass, ArrayList<DataCabeceraPDF> objDbPedidoCabecera)
    {
        this.dataCabeceraArrayList = objDbPedidoCabecera;
        this.mydbClass=mydbClass;
    }

    @NonNull
    @Override
    public DbViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View singleRow = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_row_cabecera, parent, false);
        return new DbViewHolder(singleRow);
    }

    @Override
    public void onBindViewHolder(@NonNull DbViewHolder holder, int position) {
        DataCabeceraPDF dataCabecera = dataCabeceraArrayList.get(position);
        holder.oc_numero.setText(dataCabecera.getOc_numero());
        holder.nomcli.setText(dataCabecera.getNomcli());

        String oc_num = holder.oc_numero.getText().toString();

        /*holder.ver_detalles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(holder.itemView.getContext(), MainActivity.class);
                i.putExtra("oc_numero", oc_num);
                holder.itemView.getContext().startActivity(i);
            }
        });*/


        holder.btnViewPdf.setOnClickListener( v -> {
            AlertDialog.Builder elegir = new AlertDialog.Builder(holder.itemView.getContext());
            elegir.setTitle("¿Interno o Cliente?");
            elegir.setMessage("Seleccione el tipo de envío");
            elegir.setCancelable(true);
            elegir.setPositiveButton("Cliente", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Generate_pdf_by_ocumero( holder.itemView,1, dataCabecera);
                }
            });
            elegir.setNegativeButton("Interno", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Generate_pdf_by_ocumero( holder.itemView,2, dataCabecera);
                }
            });
            elegir.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).show();

        });

    }


    @Override
    public int getItemCount() {
        return dataCabeceraArrayList.size();
    }

    public static class DbViewHolder extends RecyclerView.ViewHolder
    {
        TextView oc_numero, nomcli;
        Button ver_detalles, btnViewPdf;

        public DbViewHolder(@NonNull View itemView) {
            super(itemView);

            oc_numero = itemView.findViewById(R.id.OC_NUMERO);
            nomcli = itemView.findViewById(R.id.NOM_CLI);
            ver_detalles = itemView.findViewById(R.id.VER_DETALLES);
            btnViewPdf = itemView.findViewById(R.id.btnViewPdf);
        }
    }

    private void Generate_pdf_by_ocumero(View itemView, int tipoEnvio, DataCabeceraPDF dataCabecera) {
        String nombreArchivo=dataCabecera.getTipoRegistro()+"-"+dataCabecera.getOc_numero();
        try {
            ArrayList<ReportePedidoDetallePDF> listaDetalle=mydbClass.getAllDataByCodigo( dataCabecera.getOc_numero());
            PDF.createPdf(itemView.getContext(),
                    nombreArchivo,
                    /*dataCabecera.getOc_numero(),
                    dataCabecera.getTipoRegistro(),
                    dataCabecera.getRuccli(),
                    dataCabecera.getCodven(),
                    dataCabecera.getNomcli(),
                    dataCabecera.getTelefono(),
                    dataCabecera.getNomven(),
                    dataCabecera.getDireccion(),
                    dataCabecera.getEmail_cliente(),
                    dataCabecera.getEmail_vendedor(),
                    dataCabecera.getDesforpag(),
                    dataCabecera.getMonto_total(),
                    dataCabecera.getValor_igv(),
                    dataCabecera.getSubtotal(),
                    dataCabecera.getPeso_total(),
                    dataCabecera.getFecha_oc(),
                    dataCabecera.getFecha_mxe(),*/
                    null,
                    listaDetalle,
                    -1,
                    tipoEnvio);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(itemView.getContext(), ViewPdfActivity.class);
        intent.putExtra("oc_numero", dataCabecera.getOc_numero());
        intent.putExtra("nombreArchivo", nombreArchivo);
        itemView.getContext().startActivity(intent);
    }

}

