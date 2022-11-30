package com.example.sm_tubo_plast.genesys.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.example.sm_tubo_plast.R;

public class UtilViewPromocionDetalle {
    Activity activity;
    int secuencia_promocion;

    public UtilViewPromocionDetalle(Activity activity, int secuencia_promocion) {
        this.activity = activity;
        this.secuencia_promocion = secuencia_promocion;
    }
    public void Show(){
        AlertDialog.Builder alerta= new AlertDialog.Builder(activity);
        View alertLayout = ((LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate (R.layout.layout_promocion_detalle, null);

    }
}
