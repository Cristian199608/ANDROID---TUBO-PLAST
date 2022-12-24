package com.example.sm_tubo_plast.genesys.util;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

public class IntentTerceros {
    Context context;
    public IntentTerceros(Context context) {
        this.context=context;
    }

    public void IntentGoogleMaps(LatLng latLng){
//        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("geo:"+latLng.latitude+","+latLng.longitude));
//        context.startActivity(intent);

        Uri gmmIntentUri = Uri.parse("geo:0,0?q="+latLng.latitude+","+latLng.longitude+"(spniper"+"\n"+"spniper)");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(mapIntent);
        }else{
            UtilView.MENSAJES(context, null, "No tienes instalado google maps. Debes instalar primero.", 0, false);
        }
    }

    public void IntentWazeMaps(LatLng latLng){
        try
        {
            // Launch Waze to look for Hawaii:
            String url = "https://waze.com/ul?ll="+latLng.latitude+","+latLng.longitude+"=10";
            Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( url ) );
            context.startActivity( intent );
        }
        catch ( ActivityNotFoundException ex  )
        {
            // If Waze is not installed, open it in Google Play:
            Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( "market://details?id=com.waze" ) );
            context.startActivity(intent);
        }
    }

    public static void IntentTelefono(Activity activity, String nro_telefono){

        try{
            nro_telefono="tel:"+nro_telefono;
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse(nro_telefono));
            activity.startActivity(intent);
        }catch(Exception e){
            e.printStackTrace();
            Toast.makeText(activity, "No se puede abrir telefóno", Toast.LENGTH_SHORT).show();
        }
    }

    public static void lanzarGooleMaps(Activity activity, LatLng latLng) {
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + latLng.latitude + "," + latLng.longitude + "(Ubicación \n" + "Seleccionada" + ")");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        //mapIntent.getV
        if (mapIntent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(mapIntent);
        } else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
            dialog.setCancelable(true);
            dialog.setMessage("No tienes instalado Google Maps.\n Debes instalar para continuar.");
            dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            dialog.create().show();
        }
    }
}
