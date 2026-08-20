package com.example.sm_tubo_plast.genesys.fuerza_ventas.Google.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.sm_tubo_plast.R;
import com.google.maps.android.ui.IconGenerator;

public class UtilMarker {
    Activity activity;

    public UtilMarker(Activity activity) {
        this.activity = activity;
    }

    public Bitmap getMarkerDespachoIconWithLabel(String label) {
        IconGenerator iconGenerator = new IconGenerator(activity);
        View markerView = LayoutInflater.from(activity).inflate(R.layout.layout_marker_punto_despacho, null);
        //ImageView imgMarker = markerView.findViewById(R.id.img_marker);
        TextView tvLabel = markerView.findViewById(R.id.tv_label);
        //imgMarker.setImageResource(R.drawable.marker);
        //imgMarker.setRotation(angle);
        tvLabel.setText(label);
        //tvLabel.setTextColor(activity.getResources().getColor(R.color.white));
        iconGenerator.setContentView(markerView);
        iconGenerator.setBackground(null);
        return iconGenerator.makeIcon(label);
    }

    public Bitmap getMarkerIconWithLabel(String label) {
        IconGenerator iconGenerator = new IconGenerator(activity);
        View markerView = LayoutInflater.from(activity).inflate(R.layout.layout_marker_punto_map, null);
        //ImageView imgMarker = markerView.findViewById(R.id.img_marker);
        TextView tvLabel = markerView.findViewById(R.id.tv_label);
        //imgMarker.setImageResource(R.drawable.marker);
        //imgMarker.setRotation(angle);
        if(label==null){
            tvLabel.setText("");
            tvLabel.setVisibility(View.GONE);
        }else tvLabel.setText(label);
        //tvLabel.setTextColor(activity.getResources().getColor(R.color.white));
        iconGenerator.setContentView(markerView);
        iconGenerator.setBackground(null);
        return iconGenerator.makeIcon(label!=null?label:"");
    }
}
