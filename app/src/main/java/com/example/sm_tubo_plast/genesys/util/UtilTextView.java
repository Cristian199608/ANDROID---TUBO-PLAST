package com.example.sm_tubo_plast.genesys.util;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sm_tubo_plast.R;

public class UtilTextView {
    Activity activity;
    LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    int TextoColor= R.color.grey_900;
    int TextoSize= 14;
    int paddingLeft= 5;
    int paddingBottom= 2;
    int paddingTop= 2;
    int paddingEnd= 5;

    public UtilTextView(Activity activity) {
        this.activity = activity;
    }



    public TextView Generate(String valor){
        TextView presentacion_producto=new TextView(activity);
        presentacion_producto.setLayoutParams(layoutParams);
        presentacion_producto.setText(valor);
        presentacion_producto.setTextColor(activity.getResources().getColor(TextoColor));
        presentacion_producto.setTextSize(TextoSize);
        presentacion_producto.setPadding(0+paddingLeft,0+paddingTop,0+paddingEnd,0+paddingBottom);
        return presentacion_producto;

    }

    public void setLayoutParams(LinearLayout.LayoutParams layoutParams) {
        this.layoutParams = layoutParams;
    }

    public void setTextoColor(int textoColor) {
        TextoColor = textoColor;
    }

    public void setTextoSize(int textoSize) {
        TextoSize = textoSize;
    }
    public void setPadding(int left, int top, int end, int bottom) {
        paddingLeft= left;
        paddingBottom= top;
        paddingTop= end;
        paddingEnd= bottom;
    }
}
