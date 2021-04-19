package com.example.sm_tubo_plast.genesys.fuerza_ventas;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sm_tubo_plast.R;

public class Pedidos2Activity extends AppCompatActivity {

    public static String CLIENTE_TIENE_PERCEPCION="";
   public static String CLIENTE_TIENE_PERCEPCION_ESPECIAL="";
   public static String CLIENTE_VALOR_PERCEPCION="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos2);
    }
}
