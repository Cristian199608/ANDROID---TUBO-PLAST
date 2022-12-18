package com.example.sm_tubo_plast.genesys.fuerza_ventas.web;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class WebAppAsgnacionClienteInterface {

    Activity activity;

    public WebAppAsgnacionClienteInterface(Activity activity) {
        this.activity = activity;
    }

    @JavascriptInterface
    public void FinalizarVentanaAsignacionCliente(boolean estuvoCerradoTodosModals) {
        if (estuvoCerradoTodosModals) {
            new AlertDialog.Builder(activity)
                    .setTitle("Salir")
                    .setMessage("Regresar a menu clientes")
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            activity.finish();

                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }

}
