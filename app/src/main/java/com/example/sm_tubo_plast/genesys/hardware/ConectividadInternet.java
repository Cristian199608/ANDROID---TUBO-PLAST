package com.example.sm_tubo_plast.genesys.hardware;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class ConectividadInternet {

    Context _context;

    public ConectividadInternet(Context _context) {
        this._context = _context;
    }

    public boolean isConnectingToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Log.i("isConnectingToInternet","comprobando...");
        if (connectivity != null)
        {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info!= null &&  info.isConnectedOrConnecting()) {
                Log.i("isConnectingToInternet","true");
                return true;
            }
            Log.i("isConnectingToInternet","false");
            return false;
        }
        Log.i("isConnectingToInternet","false");
        return false;
    }
}


