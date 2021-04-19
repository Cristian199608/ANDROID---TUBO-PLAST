package com.example.sm_tubo_plast.genesys.fuerza_ventas;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.cliente.ClienteInformacionActivity;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.cliente.ClienteUbicacionActivity;

public class ClienteDetalleActivity extends TabActivity {
    String codcli="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_detalle);


        Bundle bundle = getIntent().getExtras();
        codcli=""+bundle.getString("CODCLI");

        //TabWidget tabs = (TabWidget) findViewById(android.R.id.tabs);
        //tabs.setBackgroundResource(R.drawable.tab_icon_background);

        TabHost tabHost = getTabHost();

        // Tab for Photos
        TabSpec photospec = tabHost.newTabSpec("Cliente");
        photospec.setIndicator("Cliente", getResources().getDrawable(R.drawable.icon_info_cliente));
        Intent photosIntent = new Intent(this, ClienteInformacionActivity.class);
        photosIntent.putExtra("CODCLI", codcli);
        photospec.setContent(photosIntent);

        // Tab for Songs
        TabSpec songspec = tabHost.newTabSpec("Ubicacion");
        // setting Title and Icon for the Tab
        songspec.setIndicator("Ubicacion", getResources().getDrawable(R.drawable.icon_songs_tab));
        Intent songsIntent = new Intent(this, ClienteUbicacionActivity.class);
        songsIntent.putExtra("CODCLI",codcli);
        songspec.setContent(songsIntent);
        // Tab for Videos
        TabSpec videospec = tabHost.newTabSpec("Plan de visita");
        videospec.setIndicator("Plan de visita", getResources().getDrawable(R.drawable.icon_visita_cliente));
        Intent videosIntent = new Intent(this, ClientePlanVisitaActivity.class);
        videospec.setContent(videosIntent);

        // Adding all TabSpec to TabHost
        tabHost.addTab(photospec); // Adding photos tab
        tabHost.addTab(songspec); // Adding songs tab
        tabHost.addTab(videospec); // Adding videos tab

        for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
        {
            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title); //Unselected Tabs
            tv.setTextColor(Color.parseColor("#ffffff"));
        }
      /*  TextView tv = (TextView) tabHost.getCurrentTabView().findViewById(android.R.id.title); //for Selected Tab
        tv.setTextColor(Color.parseColor("#000000"));*/
    }


}
