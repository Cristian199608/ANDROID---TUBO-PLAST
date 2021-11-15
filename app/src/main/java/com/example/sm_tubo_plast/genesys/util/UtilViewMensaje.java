package com.example.sm_tubo_plast.genesys.util;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

public class UtilViewMensaje {

    public static void MENSAJE_simple_finish_return_intent(final Activity activity, String titulo, String mensaje, final boolean finish){
        AlertDialog.Builder dialog=new AlertDialog.Builder(activity);
        dialog.setTitle(titulo);
        dialog.setMessage(mensaje);
        dialog.setPositiveButton("Ok",null);
        dialog.create();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(finish){
                    Intent data=new Intent();
                    activity.setResult(activity.RESULT_OK, data);
                    activity.finish();
                }
            }
        });
        dialog.show();
    }


    public static void MENSAJE_simple_finish(final Activity activity, String titulo, String mensaje, final boolean finish){
        AlertDialog.Builder dialog=new AlertDialog.Builder(activity);
        dialog.setTitle(titulo);
        dialog.setMessage(mensaje);
        dialog.setPositiveButton("Ok",null);
        dialog.create();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(finish){
                    activity.finish();
                }
            }
        });
        dialog.show();
    }

    public static void MENSAJE_simple(Activity activity, String titulo, String mensaje){
        AlertDialog.Builder dialog=new AlertDialog.Builder(activity);
        dialog.setTitle(titulo);
        dialog.setMessage(mensaje);
        dialog.setPositiveButton("Ok",null);
        dialog.setCancelable(true);
        dialog.create();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });
        dialog.show();
    }


}
