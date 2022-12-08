package com.example.sm_tubo_plast.genesys.fuerza_ventas;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.SincronizarActivity2;

public class MenuLiquidacionActivity extends AppCompatActivity {

    String codven;
    String nomcli="";
    String origen="MENU";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_liquidacion);


        SharedPreferences prefs =  getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        codven = prefs.getString("codven", "por_defecto");




        onCreateMainMenu(getWindow(),this);
        Window window=getWindow();
        final Activity activity= this;




        View mostrarStock= window.findViewById(R.id.menu_chofer_ly_stock);
        mostrarStock.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                final Intent iproductos = new Intent(activity,ProductosActivity.class);
                // ipedido.putExtra("codven",codven);
                activity.startActivityForResult(iproductos, 0);
            }
        });

        View mostrarFacturas= window.findViewById(R.id.menu_chofer_ly_facturas);
        mostrarFacturas.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                final Intent ifacturas = new Intent(activity,FacturasActivity.class);
                // ipedido.putExtra("codven",codven);
                activity.startActivityForResult(ifacturas, 0);
            }
        });

        View mostrarCobranza= window.findViewById(R.id.menu_chofer_ly_cobranza);
        mostrarCobranza.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                final Intent idevoluciones = new Intent(activity,DevolucionesActivity.class);
                idevoluciones.putExtra("codven",codven);
                activity.startActivityForResult(idevoluciones, 0);
            }
        });

        View mostrarSincronizar= window.findViewById(R.id.menu_chofer_ly_sincronizar);
        mostrarSincronizar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                final Intent isincronizar = new Intent(activity, SincronizarActivity2.class);

                activity.startActivityForResult(isincronizar, 0);
            }
        });

    }
    public static void onCreateMainMenu(Window window, final Activity activity){
        View mostrar = (View) window.findViewById(R.id.menu_chofer_ly_cobranza);
        View mostrarCliente = (View) window.findViewById(R.id.menu_chofer_ly_facturas);
        View mostrarDepoitos = (View) window.findViewById(R.id.menu_chofer_ly_stock);
        View mostrarSincronizar = (View) window.findViewById(R.id.menu_chofer_ly_sincronizar);

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Cerrando Aplicacion")
                .setMessage("seguro que quieres salir de la aplicaci�n?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }
}
