package com.example.sm_tubo_plast.genesys.fuerza_ventas.cliente;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.BEAN.Cliente;
import com.example.sm_tubo_plast.genesys.BEAN.Cliente_Contacto;
import com.example.sm_tubo_plast.genesys.DAO.DAO_Cliente;
import com.example.sm_tubo_plast.genesys.DAO.DAO_Cliente_Contacto;
import com.example.sm_tubo_plast.genesys.datatypes.DB_DireccionClientes;
import com.example.sm_tubo_plast.genesys.datatypes.DBclasses;
import com.example.sm_tubo_plast.genesys.util.FontManager;
import com.example.sm_tubo_plast.genesys.util.UtilView;
import com.example.sm_tubo_plast.genesys.util.VARIABLES;

import java.util.ArrayList;
import java.util.List;

public class CH_InformacionCliente extends AppCompatActivity {

    String codigoCliente;
    Cliente cliente;
    DBclasses dBclasses;

    EditText tv_ruc,tv_sector, tv_rubro,tv_razonSocial,tv_direccionFiscal,tv_tipo_cliente,tv_telefono,
            tv_email, tv_canal,tv_moneda,tv_montoCredito, tv_disponibleCredito,tv_unidadNegocio,tv_monedaFacturacion;
    EditText tv_direccion_sucursal, tv_telefono_sucursal, tvVendedoresAsignados,
            tvFechaNacimiento, tvDNI, tv_cargo_contacto, tv_email_contacto, tv_celular, tv_telefono_contacto;
    Spinner spn_direccion, SpinnerContacto;

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
        tv_tipo_cliente = (EditText) findViewById(R.id.tv_tipo_cliente);
        tv_rubro = (EditText) findViewById(R.id.tv_rubro);
        tv_email = (EditText) findViewById(R.id.tv_email);
        tv_telefono = (EditText) findViewById(R.id.tv_telefono);
        tv_canal = (EditText) findViewById(R.id.tv_canal);
        tv_moneda = (EditText) findViewById(R.id.tv_moneda);
        tv_montoCredito = (EditText) findViewById(R.id.tv_montoCredito);
        tv_disponibleCredito = (EditText) findViewById(R.id.tv_disponibleCredito);
        tv_unidadNegocio = (EditText) findViewById(R.id.tv_unidadNegocio);
        tv_monedaFacturacion = (EditText) findViewById(R.id.tv_monedaFacturacion);
        spn_direccion = findViewById(R.id.spn_direccion);
        tv_direccion_sucursal = findViewById(R.id.tv_direccion_sucursal);
        tv_telefono_sucursal = findViewById(R.id.tv_telefono_sucursal);
        tv_cargo_contacto = findViewById(R.id.tv_cargo_contacto);
        tvFechaNacimiento = findViewById(R.id.tvFechaNacimiento);
        tvDNI = findViewById(R.id.tvDNI);
        tv_email_contacto = findViewById(R.id.tv_email_contacto);
        tv_telefono_contacto = findViewById(R.id.tv_telefono_contacto);
        tvVendedoresAsignados = findViewById(R.id.tvVendedoresAsignados);
        tv_celular = findViewById(R.id.tv_celular);
        SpinnerContacto = findViewById(R.id.SpinnerContacto);
        //------------------------------------------

        Bundle bundle = getIntent().getExtras();
        codigoCliente = bundle.getString("codigoCliente");

        dBclasses=new DBclasses(this);
        cliente = dao_Cliente.getInformacionCliente(codigoCliente);
        if (cliente!=null) {
            tv_ruc.setText(cliente.getCodigoCliente());
            tv_sector.setText(cliente.getSector());
            tv_razonSocial.setText(cliente.getNombre());
            tv_direccionFiscal.setText(cliente.getDireccionFiscal());
            tv_telefono.setText(cliente.getTelefono());
            tv_canal.setText(cliente.getCanal());
            tv_moneda.setText(cliente.getMonedaCredito());
            tv_montoCredito.setText(VARIABLES.formater_thow_decimal.format(Double.parseDouble(cliente.getLimiteCredito())));
            tv_disponibleCredito.setText(VARIABLES.formater_thow_decimal.format(Double.parseDouble(cliente.getDisponible_credido())));
            tv_unidadNegocio.setText(cliente.getUnidadNegocio());
            tv_monedaFacturacion.setText(cliente.getMonedaDocumento());
            tv_email.setText(cliente.getEmail());

            tv_rubro.setText(dBclasses.getRegistrosGeneralesMovilByCodigo(cliente.getRubro_cliente(), "Sin valor"));
            tv_tipo_cliente.setText(dBclasses.getRegistrosGeneralesMovilByCodigo(cliente.getTipo_cliente(), "Sin valor"));

            String []spliCodvens=cliente.getCodven_asginados().split(",");
            StringBuilder vendedores= new StringBuilder();
            for (int i = 0; i < spliCodvens.length; i++) {
                if (vendedores.toString().length()>0) vendedores.append("\n");
                vendedores.append(i + 1).append(") ").append(dBclasses.getVendedorByCodven(spliCodvens[i]));
            }
            tvVendedoresAsignados.setText(vendedores.toString());

            GestionarSucursales();
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

    private void GestionarSucursales(){

        final ArrayList<DB_DireccionClientes> direcciones = dBclasses.obtenerDirecciones_cliente2(codigoCliente);
        List<String> direccionesList = new ArrayList<String>();
        for (int i=0;i<direcciones.size();i++) {
            direccionesList.add(direcciones.get(i).getDireccion());
        }

        ArrayAdapter<String> direccionAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item, direccionesList);
        spn_direccion.setAdapter(direccionAdapter);
        spn_direccion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tv_direccion_sucursal.setText(direcciones.get(position).getDireccion());
                tv_telefono_sucursal.setText(direcciones.get(position).getTelefono());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        PoblarSpinnersCliente_Contacto(dBclasses);

    }
    private void PoblarSpinnersCliente_Contacto(  DBclasses dBclasses) {
        DAO_Cliente_Contacto dao_cliente_contacto = new DAO_Cliente_Contacto();

        ArrayList<Cliente_Contacto> lista = dao_cliente_contacto.getClienteContactoByID(dBclasses, ""+codigoCliente, 0);
        ArrayList<String> listaString = new ArrayList<>();
        listaString.add("Sin Contacto");
        int posSelected = 0;
        for (int i = 0; i < lista.size(); i++) {
            listaString.add(lista.get(i).getNombre_contacto() + "-" + lista.get(i).getDni());
            posSelected=1;
        }


        SpinnerContacto.setAdapter(UtilView.LLENAR_SPINNER_SIMPLE(this, listaString));
        SpinnerContacto.setSelection(posSelected);
        if (lista.size()>0){
            SpinnerContacto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    position--;
                    if (position>=0){
                        tv_cargo_contacto.setText(lista.get(position).getCargo());
                        tvFechaNacimiento.setText(lista.get(position).getFec_nacimiento());
                        tvDNI.setText(lista.get(position).getDni());
                        tv_email_contacto.setText(lista.get(position).getEmail());
                        tv_telefono_contacto.setText(lista.get(position).getTelefono());
                        tv_celular.setText(lista.get(position).getCelular());
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

    }
}
