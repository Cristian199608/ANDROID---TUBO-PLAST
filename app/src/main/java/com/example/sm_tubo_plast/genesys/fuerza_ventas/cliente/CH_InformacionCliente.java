package com.example.sm_tubo_plast.genesys.fuerza_ventas.cliente;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.BEAN.Cliente;
import com.example.sm_tubo_plast.genesys.DAO.DAO_Cliente;
import com.example.sm_tubo_plast.genesys.util.FontManager;

public class CH_InformacionCliente extends AppCompatActivity {

    String codigoCliente;
    Cliente cliente;

    EditText tv_ruc,tv_sector,tv_razonSocial,tv_direccionFiscal,tv_giro,tv_telefono,tv_canal,tv_moneda,tv_montoCredito,tv_unidadNegocio,tv_monedaFacturacion;
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_h__informacion_cliente);

        DAO_Cliente dao_Cliente = new DAO_Cliente(getApplicationContext());
        Typeface iconFont = FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME);
        FontManager.markAsIconContainer(findViewById(R.id.icons_container), iconFont);



        //------------------------------------------
        tv_ruc = (EditText) findViewById(R.id.tv_ruc);
        tv_sector = (EditText) findViewById(R.id.tv_sector);
        tv_razonSocial = (EditText) findViewById(R.id.tv_razonSocial);
        tv_direccionFiscal = (EditText) findViewById(R.id.tv_direccionFiscal);
        tv_giro = (EditText) findViewById(R.id.tv_giro);
        tv_telefono = (EditText) findViewById(R.id.tv_telefono);
        tv_canal = (EditText) findViewById(R.id.tv_canal);
        tv_moneda = (EditText) findViewById(R.id.tv_moneda);
        tv_montoCredito = (EditText) findViewById(R.id.tv_montoCredito);
        tv_unidadNegocio = (EditText) findViewById(R.id.tv_unidadNegocio);
        tv_monedaFacturacion = (EditText) findViewById(R.id.tv_monedaFacturacion);
        //------------------------------------------

        Bundle bundle = getIntent().getExtras();
        codigoCliente = bundle.getString("codigoCliente");

        cliente = dao_Cliente.getInformacionCliente(codigoCliente);
        if (cliente!=null) {
            tv_ruc.setText(cliente.getCodigoCliente());
            tv_sector.setText(cliente.getSector());
            tv_razonSocial.setText(cliente.getNombre());
            tv_direccionFiscal.setText(cliente.getDireccionFiscal());
            tv_giro.setText(cliente.getGiro());
            tv_telefono.setText(cliente.getTelefono());
            tv_canal.setText(cliente.getCanal());
            tv_moneda.setText(cliente.getMonedaCredito());
            tv_montoCredito.setText(cliente.getLimiteCredito());
            tv_unidadNegocio.setText(cliente.getUnidadNegocio());
            tv_monedaFacturacion.setText(cliente.getMonedaDocumento());
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
