package com.example.sm_tubo_plast.genesys.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class UtilMensaje {
    Activity activity;
    String titulo;
    String mensaje;
    MyListener myListener;

    String btn_positivo="OK";
    String btn_negativo="Cancelar";
    boolean forzar_listener=false;

    public UtilMensaje(Activity activity, String titulo, String mensaje) {
        this.activity = activity;
        this.titulo = titulo;
        this.mensaje = mensaje;
    }

    public  void  show(MyListener myListener){
        this.myListener = myListener;
        if (forzar_listener){
            myListener.result(true);
            return;
        }

        AlertDialog.Builder dialog=new AlertDialog.Builder(activity);
        dialog.setTitle(titulo);
        dialog.setMessage(mensaje);
        dialog.setPositiveButton(""+btn_positivo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (myListener!=null) {
                    myListener.result(true);
                }
            }
        });

        dialog.setNegativeButton(""+btn_negativo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (myListener!=null) {
                    myListener.result(false);
                }
            }
        });

        dialog.setCancelable(true);
        dialog.create();

        dialog.show();
    }

    public void setBtn_positivo(String btn_positivo) {
        this.btn_positivo = btn_positivo;
    }

    public void setBtn_negativo(String btn_negativo) {
        this.btn_negativo = btn_negativo;
    }

    public void setForzar_listener(boolean forzar_listener) {
        this.forzar_listener = forzar_listener;
    }

    public  interface MyListener{
        void result(boolean ok);
    }
}
