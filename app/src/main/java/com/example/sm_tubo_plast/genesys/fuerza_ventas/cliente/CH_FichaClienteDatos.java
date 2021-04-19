package com.example.sm_tubo_plast.genesys.fuerza_ventas.cliente;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TabHost;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sm_tubo_plast.R;

public class CH_FichaClienteDatos extends AppCompatActivity {

    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_h__ficha_cliente_datos);

        TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);
        tabHost.setup();

        TabHost.TabSpec spec=tabHost.newTabSpec("mitab1");
        spec.setContent(R.id.tab_vendedores);
        spec.setIndicator("Vendedores");
        tabHost.addTab(spec);

        spec=tabHost.newTabSpec("mitab2");
        spec.setContent(R.id.tab_observacion);
        spec.setIndicator("Observacion");
        tabHost.addTab(spec);

        spec=tabHost.newTabSpec("mitab3");
        spec.setContent(R.id.tab_transporte);
        spec.setIndicator("Transporte");
        tabHost.addTab(spec);

        spec=tabHost.newTabSpec("mitab4");
        spec.setContent(R.id.tab_representante);
        spec.setIndicator("Representante");
        tabHost.addTab(spec);

        spec=tabHost.newTabSpec("mitab5");
        spec.setContent(R.id.tab_ubicacion);
        spec.setIndicator("Ubicacion");
        tabHost.addTab(spec);

        tabHost.setCurrentTab(0);

        bundle = getIntent().getExtras();
        if (bundle!=null) {

        }else{
            bundle = new Bundle();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_ficha_clientes_datos, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_sucursales:
                Intent iSucursales = new Intent(CH_FichaClienteDatos.this,CH_SucursalesCliente.class);
                startActivity(iSucursales);
                break;
            case R.id.menu_contactos:
                Intent intent = new Intent(CH_FichaClienteDatos.this,CH_ContactosCliente.class);
                startActivity(intent);
                break;
            case R.id.menu_horarioCobranza:
                Intent intent2 = new Intent(CH_FichaClienteDatos.this,CH_HorariosCliente.class);
                startActivity(intent2);
                break;
            case R.id.menu_horarioEntrega:
                Intent intent3 = new Intent(CH_FichaClienteDatos.this,CH_HorariosCliente.class);
                startActivity(intent3);
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Atras();
        super.onBackPressed();
    }

    void Atras(){
        Intent intent = new Intent(CH_FichaClienteDatos.this,CH_FichaCliente2.class);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

}
