package com.example.sm_tubo_plast.genesys.fuerza_ventas.Reportes;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.BEAN.ItemProducto;
import com.example.sm_tubo_plast.genesys.datatypes.DBclasses;

import java.util.ArrayList;

public class ReportesCobranzaDetalle extends AppCompatActivity {

    DBclasses helper;
    ArrayList<ItemProducto> productos = new ArrayList<ItemProducto>();
    TextView tv_monto, tv_fecha, tv_direccion, tv_codigo, tv_cond_pago;
    ListView lv_detallePedido;
    String cod_cabecera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportes_cobranza_detalle);
    }
}
