package com.example.sm_tubo_plast.genesys.BEAN;


import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class MyItem implements ClusterItem {
    private final LatLng mPosition;
    private Object tag;
    public String titulo;
    public String snipet;


    public MyItem(double lat, double lng, String titulo, String snipet, Object tag ) {
        mPosition = new LatLng(lat, lng);
        this.tag=tag;
        this.titulo=titulo;
        this.snipet=snipet;
    }


    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return titulo;
    }

    @Override
    public String getSnippet() {
        return snipet;
    }

    public Object getTag() {
        return tag;
    }


}
