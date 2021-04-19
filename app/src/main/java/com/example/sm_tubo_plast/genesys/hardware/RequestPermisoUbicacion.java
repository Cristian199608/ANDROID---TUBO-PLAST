package com.example.sm_tubo_plast.genesys.hardware;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

public class RequestPermisoUbicacion  {
    Activity activity;
     int REQUEST_CODE;
    MyListener myListener;

    public RequestPermisoUbicacion(Activity activity, int REQUEST_CODE, MyListener myListener) {
        this.activity = activity;
        this.REQUEST_CODE = REQUEST_CODE;
        this.myListener = myListener;
        vv();
    }

    public void vv(){
        int permissionCheckFineLocation = ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionCheckCoarseLocation= ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        int packag= PackageManager.PERMISSION_GRANTED;
        if (permissionCheckFineLocation !=  packag       ||         permissionCheckCoarseLocation!=packag) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                myListener.Result(Permiso_Adroid.IS_PERMISO_POR_CONCENDER);
                showExplanation("Importante", "Se requiere acceder a la ubicación", android.Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_CODE);
            } else {
                myListener.Result(Permiso_Adroid.IS_PERMISO_POR_CONCENDER);
                requestPermission(android.Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_CODE);
            }
        } else {
            myListener.Result(Permiso_Adroid.IS_PERMISO_CONCEDIDO);
        }
    }

    private void requestPermission(String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions(activity,
                new String[]{permissionName}, permissionRequestCode);
    }
    private void showExplanation(String title, String message, final String permission, final int permissionRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title)
        .setCancelable(false)
        .setMessage(message)
        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                myListener.Result(Permiso_Adroid.IS_PERMISO_DENEGADO);
            }
        })
        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                requestPermission(permission, permissionRequestCode);
            }
        });
        builder.create().show();
    }

    public interface MyListener{
        void  Result(int isConcedido);
    }
}
