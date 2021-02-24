package com.example.fuerzaventaschema.genesys.fuerza_ventas.cliente;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.fuerzaventaschema.R;
import com.example.fuerzaventaschema.genesys.BEAN.FichaCliente;
import com.example.fuerzaventaschema.genesys.DAO.DAO_RegistroCliente;

public class CH_FichaCliente2 extends AppCompatActivity {

    Spinner spn_categoria,spn_tipoSector,spn_unidadNegocio,spn_sector,spn_pais,spn_tipologia,spn_procedencia,spn_local,spn_monedaFacturacion,spn_limiteCredito,spn_estado;
    EditText edt_packPegamento,edt_fConstitucion,edt_fAniversario,edt_fRegistro,edt_licMunicipal,edt_descuento,edt_ibc,                       edt_ciiu;
    EditText edt_nRucExterior,edt_limiteCredito,edt_diasPlazo;
    CheckBox check_vinculada,check_muestrario,check_infocorp,check_limiteCredito,check_tieneSurcursales;

    Button btn_atras,btn_guardar,btn_siguiente;
    Bundle bundle;

    String codigoCategoria;
    String packPegamento;
    String fConstitucion;
    String fAniversario;
    String fRegistro;
    String licMunicipal;
    String codigoTipoSector;
    String ibc;
    String codigoUnidadNegocio;
    String descuento;
    String codigoSector;
    String codigoPais;
    String ciiu;
    String nRucExterior;
    String codigoTipologia;
    String codigoProcedencia;
    String codigolocal;
    boolean vinculada;
    boolean muestrario;
    boolean inforcorp;
    String codigoMonedaFacturacion;
    boolean checkLimiteCredito;
    String codigoLimiteCredito;
    String limiteCredito;
    String diasPlazo;
    boolean tieneSurcursales;
    String codigoEstado;


    DAO_RegistroCliente dao_registroCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_h__ficha_cliente2);

        bundle = getIntent().getExtras();
        dao_registroCliente = new DAO_RegistroCliente(getApplicationContext());

        spn_categoria = (Spinner) findViewById(R.id.spn_categoria);
        edt_packPegamento = (EditText) findViewById(R.id.edt_packPegamento);
        edt_fConstitucion = (EditText) findViewById(R.id.edt_fConstitucion);
        edt_fAniversario = (EditText) findViewById(R.id.edt_fAniversario);
        edt_fRegistro = (EditText) findViewById(R.id.edt_fRegistro);
        edt_licMunicipal = (EditText) findViewById(R.id.edt_licMunicipal);

        spn_tipoSector = (Spinner) findViewById(R.id.spn_tipoSector);
        edt_ibc = (EditText) findViewById(R.id.edt_ibc);
        spn_unidadNegocio = (Spinner) findViewById(R.id.spn_unidadNegocio);
        edt_descuento = (EditText) findViewById(R.id.edt_descuento);
        spn_sector = (Spinner) findViewById(R.id.spn_sector);
        spn_pais = (Spinner) findViewById(R.id.spn_pais);
        edt_ciiu = (EditText) findViewById(R.id.edt_ciiu);
        edt_nRucExterior = (EditText) findViewById(R.id.edt_nRucExterior);
        spn_tipologia = (Spinner) findViewById(R.id.spn_tipologia);
        spn_procedencia = (Spinner) findViewById(R.id.spn_procedencia);
        spn_local = (Spinner) findViewById(R.id.spn_local);

        check_vinculada = (CheckBox) findViewById(R.id.check_vinculada);
        check_muestrario = (CheckBox) findViewById(R.id.check_muestrario);
        check_infocorp = (CheckBox) findViewById(R.id.check_infocorp);
        spn_monedaFacturacion = (Spinner) findViewById(R.id.spn_monedaFacturacion);

        check_limiteCredito = (CheckBox) findViewById(R.id.check_limiteCredito);
        spn_limiteCredito = (Spinner) findViewById(R.id.spn_limiteCredito);
        edt_limiteCredito = (EditText) findViewById(R.id.edt_limiteCredito);
        edt_diasPlazo = (EditText) findViewById(R.id.edt_diasPlazo);
        check_tieneSurcursales = (CheckBox) findViewById(R.id.check_tieneSucursales);
        spn_estado = (Spinner) findViewById(R.id.spn_estado);

        btn_atras = (Button) findViewById(R.id.btn_atras);
        btn_guardar = (Button) findViewById(R.id.btn_guardar);
        btn_siguiente = (Button) findViewById(R.id.btn_siguiente);

        bundle = getIntent().getExtras();
        if (bundle!=null) {
            CargarDatos();
        }else{
            bundle = new Bundle();
        }

        btn_atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Atras();
            }
        });

        btn_siguiente.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                tomarCampos();
                Intent intent = new Intent(CH_FichaCliente2.this,CH_FichaClienteDatos.class);
                //Intent intent = new Intent(CH_FichaCliente2.this,CH_AsignarSucursal.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });

        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if (validarCampos()) {
                tomarCampos();

                FichaCliente fichaCliente = new FichaCliente();
                fichaCliente.setNroAnalisis(bundle.getString("nAnalisis"));
                //fichaCliente.setTipo();
                fichaCliente.setRazonSocial(bundle.getString("razonSocial"));
                fichaCliente.setNombreComercial(bundle.getString("nombreComercial"));
                fichaCliente.setGiroNegocio(bundle.getString("giroNegocio"));
                fichaCliente.setDireccionFiscal(bundle.getString("direccionFiscal"));
                fichaCliente.setEmailFactElec(bundle.getString("emailFacElec"));
                fichaCliente.setEmail(bundle.getString("email"));
                fichaCliente.setTelefono1(bundle.getString("telefono1"));
                fichaCliente.setTelefono2(bundle.getString("telefono2"));
                fichaCliente.setTelefono3(bundle.getString("telefono3"));
                if (bundle.getBoolean("impresionElectronica")) {
                    fichaCliente.setImpresionElectronica("1");
                }else{
                    fichaCliente.setImpresionElectronica("0");
                }

                if (bundle.getBoolean("perceptor")) {
                    fichaCliente.setaPerceptor("1");
                }else{
                    fichaCliente.setaPerceptor("0");
                }

                if (bundle.getBoolean("retenedor")) {
                    fichaCliente.setaRetenedor("1");
                }else{
                    fichaCliente.setaRetenedor("0");
                }

                //fichaCliente.setCategoria();
                fichaCliente.setPackPegamento(packPegamento);
                fichaCliente.setfConstitucion(fConstitucion);
                fichaCliente.setfAnimersario(fAniversario);
                fichaCliente.setfRegistro(fRegistro);
                fichaCliente.setLicMunicipal(licMunicipal);
                //fichaCliente.setTipoSector(bundle.getString("codigoTipoSector"));
                fichaCliente.setIBC(ibc);
                //fichaCliente.setUnidadNegocio(bundle.getString("codigoUnidadNegocio"));
                fichaCliente.setDescuento(descuento);
                //fichaCliente.setSector(bundle.getString("codigoSector"));
                //fichaCliente.setPais(bundle.getString("codigoPais"));
                fichaCliente.setCIIU(ciiu);
                fichaCliente.setnRucExterior(nRucExterior);
                //fichaCliente.setTipologia(bundle.getString("codigoTipologia"));
                //fichaCliente.setProcedencia(bundle.getString("codigoProcedencia"));
                //fichaCliente.setLocal(bundle.getString("codigolocal"));

                if (vinculada) {
                    fichaCliente.setVinculada("1");
                }else{
                    fichaCliente.setVinculada("0");
                }

                if (muestrario) {
                    fichaCliente.setMuestrario("1");
                }else{
                    fichaCliente.setMuestrario("0");
                }

                if (inforcorp) {
                    fichaCliente.setInfocorp("1");
                }else{
                    fichaCliente.setInfocorp("0");
                }

                //fichaCliente.setMonedaFacturacion(bundle.getString("codigoMonedaFacturacion"));
                fichaCliente.setMonedaLimiteCredito(bundle.getString("codigoLimiteCredito"));
					/*
					if (checkLimiteCredito) {
						fichaCliente.("1");
					}else{
						fichaCliente.("0");
					}*/
                fichaCliente.setLimiteCredito(limiteCredito);
                fichaCliente.setDiasPlazo(diasPlazo);

                if (tieneSurcursales) {
                    fichaCliente.setTieneSucursales("1");
                }else{
                    fichaCliente.setTieneSucursales("0");
                }

                //fichaCliente.setEstado(bundle.getString("codigoEstado"));

                if (dao_registroCliente.guardarFichaCliente(fichaCliente)){
                    Toast.makeText(getApplicationContext(),"Ficha guardada", Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent(CH_FichaCliente2.this,CH_FichaClienteDatos.class);
                //Intent intent = new Intent(CH_FichaCliente2.this,CH_AsignarSucursal.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
                //}


            }
        });
    }

    private void CargarDatos() {
        edt_packPegamento.setText(bundle.getString("packPegamento"));
        edt_fConstitucion.setText(bundle.getString("fConstitucion"));
        edt_fAniversario.setText(bundle.getString("fAniversario"));
        edt_fRegistro.setText(bundle.getString("fRegistro"));
        edt_licMunicipal.setText(bundle.getString("licMunicipal"));
        edt_ibc.setText(bundle.getString("ibc"));
        edt_descuento.setText(bundle.getString("descuento"));
        edt_ciiu.setText(bundle.getString("ciiu"));
        edt_nRucExterior.setText(bundle.getString("nRucExterior"));

        boolean vinculada = bundle.getBoolean("vinculada");
        check_vinculada.setChecked(vinculada);

        boolean muestrario = bundle.getBoolean("muestrario");
        check_muestrario.setChecked(muestrario);

        boolean infocorp = bundle.getBoolean("inforcorp");
        check_infocorp.setChecked(infocorp);

        boolean limiteCreido = bundle.getBoolean("checkLimiteCredito");
        check_limiteCredito.setChecked(limiteCreido);

        edt_limiteCredito.setText(bundle.getString("limiteCredito"));

        edt_diasPlazo.setText(bundle.getString("diasPlazo"));

        boolean tieneSucursales = bundle.getBoolean("tieneSurcursales");
        check_tieneSurcursales.setChecked(tieneSucursales);

        String nAnalisis = bundle.getString("nAnalisis");
        if (dao_registroCliente.estaRegistrado(nAnalisis)) {
            btn_guardar.setVisibility(View.GONE);
            btn_siguiente.setVisibility(View.VISIBLE);
        }
    }

    void tomarCampos(){
        //codigoCategoria = spn_categoria.getSelectedItemPosition();
        packPegamento = edt_packPegamento.getText().toString();
        fConstitucion = edt_fConstitucion.getText().toString();
        fAniversario = edt_fAniversario.getText().toString();
        fRegistro = edt_fRegistro.getText().toString();
        licMunicipal = edt_licMunicipal.getText().toString();
        //codigoTipoSector = spn_tipoSector.getSelectedItemPosition();
        ibc = edt_ibc.getText().toString();
        //codigoUnidadNegocio = spn_unidadNegocio.getSelectedItem();
        descuento = edt_descuento.getText().toString();
        //codigoSector = spn_sector.getSelectedItemPosition();
        //codigoPais = spn_pais.getSelectedItemPosition();
        ciiu = edt_ciiu.getText().toString();
        nRucExterior = edt_nRucExterior.getText().toString();
        //codigoTipologia = spn_tipologia.getSelectedItemPosition();
        //codigoProcedencia = spn_procedencia.getSelectedItemPosition();
        //codigolocal = spn_local.getSelectedItemPosition();
        vinculada = check_vinculada.isChecked();
        muestrario = check_muestrario.isChecked();
        inforcorp = check_infocorp.isChecked();
        //codigoMonedaFacturacion = spn_monedaFacturacion.getSelectedItemPosition();
        checkLimiteCredito = check_limiteCredito.isChecked();
        //codigoLimiteCredito = spn_limiteCredito.getSelectedItemPosition();
        limiteCredito = edt_limiteCredito.getText().toString();
        diasPlazo = edt_diasPlazo.getText().toString();
        tieneSurcursales = check_tieneSurcursales.isChecked();
        //codigoEstado = spn_estado.getSelectedItemPosition();


        //bundle.putString("categoria", codigoCategoria);
        bundle.putString("packPegamento", packPegamento);
        bundle.putString("fConstitucion", fConstitucion);
        bundle.putString("fAniversario", fAniversario);
        bundle.putString("fRegistro", fRegistro);
        bundle.putString("licMunicipal", licMunicipal);
        //bundle.putString("codigoTipoSector", codigoTipoSector);
        bundle.putString("ibc", ibc);
        //bundle.putString("codigoUnidadNegocio", codigoUnidadNegocio);
        bundle.putString("descuento", descuento);
        //bundle.putString("codigoSector", codigoSector);
        //bundle.putString("codigoPais", codigoPais);
        bundle.putString("ciiu", ciiu);
        bundle.putString("nRucExterior", nRucExterior);
        //bundle.putString("codigoTipologia", codigoTipologia);
        //bundle.putString("codigoProcedencia", codigoProcedencia);
        //bundle.putString("codigolocal", codigolocal);
        bundle.putBoolean("vinculada", vinculada);
        bundle.putBoolean("muestrario", muestrario);
        bundle.putBoolean("inforcorp", inforcorp);
        //bundle.putString("codigoMonedaFacturacion", codigoMonedaFacturacion);
        bundle.putBoolean("checkLimiteCredito", checkLimiteCredito);
        //intent.putExtra("codigoLimiteCredito", codigoLimiteCredito);
        bundle.putString("limiteCredito", limiteCredito);
        bundle.putString("diasPlazo", diasPlazo);
        bundle.putBoolean("tieneSurcursales", tieneSurcursales);
        //bundle.putString("codigoEstado", codigoEstado);
    }

    boolean validarCampos(){
        tomarCampos();
        if (packPegamento.equals("")) {
            edt_packPegamento.setError("Campo requerido");
            return false;
        }
        if (fConstitucion.equals("")) {
            edt_fConstitucion.setError("Campo requerido");
            return false;
        }
        if (fAniversario.equals("")) {
            edt_fAniversario.setError("Campo requerido");
            return false;
        }
        if (fRegistro.equals("")) {
            edt_fRegistro.setError("Campo requerido");
            return false;
        }
        if (licMunicipal.equals("")) {
            edt_licMunicipal.setError("Campo requerido");
            return false;
        }
        if (ibc.equals("")) {
            edt_ibc.setError("Campo requerido");
            return false;
        }
        if (descuento.equals("")) {
            edt_descuento.setError("Campo requerido");
            return false;
        }
        if (ciiu.equals("")) {
            edt_ciiu.setError("Campo requerido");
            return false;
        }
        if (nRucExterior.equals("")) {
            edt_nRucExterior.setError("Campo requerido");
            return false;
        }
        if (limiteCredito.equals("")) {
            edt_limiteCredito.setError("Campo requerido");
            return false;
        }
        if (diasPlazo.equals("")) {
            edt_diasPlazo.setError("Campo requerido");
            return false;
        }
        return true;
    }

    void Atras(){
        tomarCampos();
        Intent intent = new Intent(CH_FichaCliente2.this,CH_FichaCliente1.class);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Atras();
        super.onBackPressed();
    }

}


