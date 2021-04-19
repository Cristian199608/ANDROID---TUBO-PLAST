package com.example.sm_tubo_plast.genesys.fuerza_ventas.cliente;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sm_tubo_plast.R;

public class CH_RegistroClientesNuevos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_h__registro_clientes_nuevos);
//        getActionBar().setHomeButtonEnabled(false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_registro_clientes, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nuevoCliente) {
            Intent intent = new Intent(CH_RegistroClientesNuevos.this, CH_FichaCliente1.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.enviarPendientes){

        }
        return super.onOptionsItemSelected(item);
    }

    class asyncCargarRegistros extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
        }



    }

}
