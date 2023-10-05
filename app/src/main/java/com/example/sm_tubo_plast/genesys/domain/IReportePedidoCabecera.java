package com.example.sm_tubo_plast.genesys.domain;

import android.app.Activity;

import com.example.sm_tubo_plast.genesys.adapters.ReportesPedidosCabeceraRecyclerView;
import com.example.sm_tubo_plast.genesys.datatypes.DBclasses;

public interface IReportePedidoCabecera {
    void setViewByHolder(Activity activity,
                         DBclasses obj_dbclasses,
                         ReportesPedidosCabeceraRecyclerView.ViewHolder viewHolder,
                         int position);
}
