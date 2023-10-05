package com.example.sm_tubo_plast.genesys.BEAN;

import android.app.Activity;

import com.example.sm_tubo_plast.genesys.adapters.ReportesPedidosCabeceraRecyclerView;
import com.example.sm_tubo_plast.genesys.datatypes.DBclasses;
import com.example.sm_tubo_plast.genesys.domain.IReportePedidoCabecera;

public class ReportePedidoLineaCabeceraBEAN implements IReportePedidoCabecera {
    String nomcli;
    String codcli;
    String tipopago;
    String total;
    String numoc;
    String moneda;
    String fecha;


    @Override
    public void setViewByHolder(Activity activity, DBclasses obj_dbclasses, ReportesPedidosCabeceraRecyclerView.ViewHolder viewHolder, int position) {


    }
}

