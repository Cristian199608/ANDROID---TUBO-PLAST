package com.example.sm_tubo_plast.genesys.fuerza_ventas.cliente;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sm_tubo_plast.R;

public class CH_SucursalesCliente extends AppCompatActivity {
    final static int MENU_AGREGAR = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_h__sucursales_cliente);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, MENU_AGREGAR, Menu.NONE, "MenuAgregar").setIcon(R.drawable.icon_flat_add).setTitle("Asignar sucursal").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case MENU_AGREGAR:
                Intent intent = new Intent(CH_SucursalesCliente.this,CH_AsignarSucursal.class);
                startActivity(intent);
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}

