package com.example.sm_tubo_plast.genesys.util.SharePrefencia;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.sm_tubo_plast.genesys.datatypes.DB_Servidor;

public class PreferenciaConfiguracion {
    Activity activity;
    SharedPreferences prefs;
    public PreferenciaConfiguracion(Activity activity) {
        this.activity = activity;
        prefs = activity. getSharedPreferences("preferencias_configuracion", Context.MODE_PRIVATE);

    }

    public float getValorIgv(){
        if (prefs!=null){
            return prefs.getFloat("valorIGV", 0.0f);
        }
        return 0;
    }
}
