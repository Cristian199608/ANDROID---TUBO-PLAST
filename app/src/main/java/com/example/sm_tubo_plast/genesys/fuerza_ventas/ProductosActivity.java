package com.example.sm_tubo_plast.genesys.fuerza_ventas;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sm_tubo_plast.R;

public class ProductosActivity extends AppCompatActivity {

    public static final String KEY_NOMBRE="nombre";
    public static final String KEY_FOTO="foto";
    public static final String KEY_DESCRIPCION="descripcion";
    public static final String KEY_PRECIO="precio";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);
    }
}
