package com.example.sm_tubo_plast.genesys.fuerza_ventas.cliente.AnticiposCliente;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.BEAN.PedidoAnticipoDetalle;
import com.example.sm_tubo_plast.genesys.DAO.DAO_PedidoAnticipoDetalle;
import com.example.sm_tubo_plast.genesys.datatypes.DBclasses;

import java.util.HashMap;

public class AnticiposClienteActivity extends AppCompatActivity {
    private static final String TAG = "AnticiposClienteActivity";
    public static void startActivity(Activity activity, String oc_numero){
        Intent intent = new Intent(
                activity,
                AnticiposClienteActivity.class
        );
        intent.putExtra("oc_numero", oc_numero);
        activity.startActivityForResult(intent, 1001);
    }


    String oc_numero;
    DBclasses dBclasses;
    DAO_PedidoAnticipoDetalle daoPedidoAnticipoDetalle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anticipos_cliente);
        Bundle param=getIntent().getExtras();
        oc_numero =param.getString("oc_numero", null);
        config();
    }

    private void config(){
        dBclasses=new DBclasses(this);
        daoPedidoAnticipoDetalle=new DAO_PedidoAnticipoDetalle(dBclasses);
    }

    private void listarData(){
        HashMap<String, Double> lista = PedidoAnticipoDetalle.Companion.obtenerAnticiposPedido(daoPedidoAnticipoDetalle.getDataBy(oc_numero));


    }
}