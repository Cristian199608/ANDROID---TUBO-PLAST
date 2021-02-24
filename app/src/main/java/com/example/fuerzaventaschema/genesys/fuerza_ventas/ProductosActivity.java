package com.example.fuerzaventaschema.genesys.fuerza_ventas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.fuerzaventaschema.R;

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
