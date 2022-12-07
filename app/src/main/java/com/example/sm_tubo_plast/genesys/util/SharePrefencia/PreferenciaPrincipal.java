package com.example.sm_tubo_plast.genesys.util.SharePrefencia;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.sm_tubo_plast.genesys.datatypes.DB_Servidor;

public class PreferenciaPrincipal {
    Activity activity;
    SharedPreferences prefs;
    public PreferenciaPrincipal(Activity activity) {
        this.activity = activity;

        prefs = activity. getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        prefs.getString("codven", "por_defecto");
    }
    public DB_Servidor geServerSaemovil() {
        prefs = activity. getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        prefs.getString("codven", "por_defecto");
        DB_Servidor dbServidor=new DB_Servidor();
        dbServidor.setTX_SERV_servidorBD(prefs.getString("url", "0"));
        dbServidor.setTX_SERV_nombreBD(prefs.getString("catalog", "0"));
        dbServidor.setTX_SERV_usuario(prefs.getString("userid", "0"));
        dbServidor.setTX_SERV_contrasena(prefs.getString("contrasenaid", "0"));
        return  dbServidor;
    }

    public String ObtenerCodigoVendedor(){
        if (prefs!=null){
            return prefs.getString("codven", "por_defecto");
        }
        return "";
    }

}
