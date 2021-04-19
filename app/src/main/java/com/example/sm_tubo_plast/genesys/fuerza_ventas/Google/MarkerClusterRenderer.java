package com.example.sm_tubo_plast.genesys.fuerza_ventas.Google;


import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.BEAN.MyItem;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

public class MarkerClusterRenderer extends DefaultClusterRenderer<MyItem> {

    ClusterManager clusterManager;
    Activity activity;

    private final IconGenerator mIconGenerator;
    private final IconGenerator mClusterIconGenerator;

    public MarkerClusterRenderer(Activity activity, GoogleMap map,
                                 ClusterManager<MyItem> clusterManager) {
        super(activity, map, clusterManager);
        this.activity=activity;
        this.clusterManager=clusterManager;

        mIconGenerator = new IconGenerator(activity);
        mClusterIconGenerator = new IconGenerator(activity);

    }



    @Override
    protected void onBeforeClusterItemRendered(MyItem item, MarkerOptions markerOptions) {
        // use this to make your change to the marker option
        // for the marker before it gets render on the map
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
        markerOptions.title(item.titulo);
        markerOptions.snippet(item.snipet);

        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(getMarkerIconWithLabel(item.titulo)));

    }



    @Override
    protected void onClusterRendered(Cluster<MyItem> cluster, Marker marker) {

        //mas iconos
        //https://developers.google.com/maps/documentation/javascript/marker-clustering

        //mClusterImageView.setImageDrawable(activity.getDrawable(R.drawable.img_cluster_blue));
        //Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
        marker.setIcon(BitmapDescriptorFactory.fromBitmap(getMarKarCluestered(cluster.getSize())));
    }

    public Bitmap getMarkerIconWithLabel(String label) {
        IconGenerator iconGenerator = new IconGenerator(activity);
        View markerView = LayoutInflater.from(activity).inflate(R.layout.layout_marker_custom, null);
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

    public Bitmap getMarKarCluestered(int cantidad) {
        IconGenerator iconGenerator = new IconGenerator(activity);
        View markerView = LayoutInflater.from(activity).inflate(R.layout.layout_cluster_custom, null);
        ImageView imgMarker = markerView.findViewById(R.id.img_cluster);
        TextView tvLabel = markerView.findViewById(R.id.txt_cantidad);
        if (cantidad<=10){
            imgMarker.setImageResource(R.drawable.marker_cluster_blue);
        }
        else if (cantidad<=50){
            imgMarker.setImageResource(R.drawable.marker_cluster_yellow);
        }
        else if (cantidad>=51){
            imgMarker.setImageResource(R.drawable.marker_cluster_red);
        }
        //imgMarker.setImageResource(R.drawable.marker);
        //imgMarker.setRotation(angle);
        tvLabel.setText(""+cantidad);
        iconGenerator.setContentView(markerView);
        iconGenerator.setBackground(null);
        return iconGenerator.makeIcon("");
    }


}
