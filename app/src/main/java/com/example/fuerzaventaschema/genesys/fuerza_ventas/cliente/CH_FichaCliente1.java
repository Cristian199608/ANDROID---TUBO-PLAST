package com.example.fuerzaventaschema.genesys.fuerza_ventas.cliente;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.example.fuerzaventaschema.R;
import com.example.fuerzaventaschema.genesys.BEAN.Objeto;
import com.example.fuerzaventaschema.genesys.DAO.DAO_RegistroCliente;

import java.util.ArrayList;

public class CH_FichaCliente1 extends AppCompatActivity {

    Button btn_siguiente;
    EditText edt_nAnalisis,edt_razonSocial,edt_nombreComercial,edt_giroNegocio,edt_direccionFiscal,edt_emailFacElec,edt_email,edt_telefono1,edt_telefono2,edt_telefono3;
    Spinner spn_tipo;
    CheckBox check_impresionElectronica;
    RadioGroup rGroup_perceptor,rGroup_retenedor;
    RadioButton rbtn_perceptorSi,rbtn_perceptorNo;
    RadioButton rbtn_retenedorSi,rbtn_retenedorNo;

    ArrayList<Objeto> listaTipoCliente = new ArrayList<Objeto>();
    ArrayList<String> tiposCliente = new ArrayList<String>();
    Bundle bundle;

    DAO_RegistroCliente dao_registroCliente;
    int nroCliente=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_h__ficha_cliente1);

        dao_registroCliente = new DAO_RegistroCliente(getApplicationContext());

        edt_nAnalisis = (EditText) findViewById(R.id.edt_nAnalisis);
        edt_razonSocial = (EditText) findViewById(R.id.edt_razonSocial);
        edt_nombreComercial = (EditText) findViewById(R.id.edt_nombreComercial);
        edt_giroNegocio = (EditText) findViewById(R.id.edt_giroNegocio);
        edt_direccionFiscal = (EditText) findViewById(R.id.edt_direccionFiscal);
        edt_emailFacElec = (EditText) findViewById(R.id.edt_emailFactElect);
        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_telefono1 = (EditText) findViewById(R.id.edt_telefono1);
        edt_telefono2 = (EditText) findViewById(R.id.edt_telefono2);
        edt_telefono3 = (EditText) findViewById(R.id.edt_telefono3);
        spn_tipo = (Spinner) findViewById(R.id.spn_tipo);
        check_impresionElectronica = (CheckBox) findViewById(R.id.check_impresionElectronica);
        rbtn_perceptorSi = (RadioButton) findViewById(R.id.rbtn_perceptorSi);
        rbtn_perceptorNo = (RadioButton) findViewById(R.id.rbtn_perceptorNo);
        rbtn_retenedorSi = (RadioButton) findViewById(R.id.rbtn_retenedorSi);
        rbtn_retenedorNo = (RadioButton) findViewById(R.id.rbtn_retenedorNo);

        bundle = getIntent().getExtras();
        if (bundle!=null) {
            CargarDatos();
        }else{
            bundle = new Bundle();
            nroCliente = dao_registroCliente.getMaxNumeroCliente() + 1;
            edt_nAnalisis.setText(String.valueOf(nroCliente));
        }


        btn_siguiente = (Button) findViewById(R.id.btn_siguiente);
        btn_siguiente.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String nAnalisis 		= edt_nAnalisis.getText().toString();
                String razonSocial 		= edt_razonSocial.getText().toString();
                String nombreComercial 	= edt_nombreComercial.getText().toString();
                String giroNegocio 		= edt_giroNegocio.getText().toString();
                String direccionFiscal 	= edt_direccionFiscal.getText().toString();
                String emailFacElec 	= edt_emailFacElec.getText().toString();
                String email 			= edt_email.getText().toString();
                String telefono1 		= edt_telefono1.getText().toString();
                String telefono2 		= edt_telefono2.getText().toString();
                String telefono3 		= edt_telefono3.getText().toString();
                //String tipoCliente 		= listaTipoCliente.get(spn_tipo.getSelectedItemPosition()).getCodigo();
                boolean impresionElectronica = check_impresionElectronica.isChecked();
                boolean perceptor = false;
                if (rbtn_perceptorSi.isChecked()) {
                    perceptor = true;
                }

                boolean retenedor = false;
                if (rbtn_retenedorSi.isChecked()) {
                    retenedor = true;
                }

                Intent intent = new Intent(CH_FichaCliente1.this,CH_FichaCliente2.class);

                bundle.putString("nAnalisis", nAnalisis);
                bundle.putString("razonSocial", razonSocial);
                bundle.putString("nombreComercial", nombreComercial);
                bundle.putString("giroNegocio", giroNegocio);
                bundle.putString("direccionFiscal", direccionFiscal);
                bundle.putString("emailFacElec", emailFacElec);
                bundle.putString("email", email);
                bundle.putString("telefono1", telefono1);
                bundle.putString("telefono2", telefono2);
                bundle.putString("telefono3", telefono3);
                //bundle.putString("tipoCliente", tipoCliente);
                bundle.putBoolean("impresionElectronica", impresionElectronica);
                bundle.putBoolean("perceptor", perceptor);
                bundle.putBoolean("retenedor", retenedor);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
    }
    private void CargarDatos() {
        edt_nAnalisis.setText(bundle.getString("nAnalisis"));
        edt_razonSocial.setText(bundle.getString("razonSocial"));
        edt_nombreComercial.setText(bundle.getString("nombreComercial"));
        edt_giroNegocio.setText(bundle.getString("giroNegocio"));
        edt_direccionFiscal.setText(bundle.getString("direccionFiscal"));
        edt_emailFacElec.setText(bundle.getString("emailFacElec"));
        edt_email.setText(bundle.getString("email"));
        edt_telefono1.setText(bundle.getString("telefono1"));
        edt_telefono2.setText(bundle.getString("telefono2"));
        edt_telefono3.setText(bundle.getString("telefono3"));
        //(bundle.getString("tipoCliente"));
        boolean impresionElectronica = bundle.getBoolean("impresionElectronica");
        check_impresionElectronica.setChecked(impresionElectronica);

        boolean perceptor = bundle.getBoolean("perceptor");
        if (perceptor==true) {
            rbtn_perceptorSi.setChecked(perceptor);
        }
        boolean retenedor = bundle.getBoolean("retenedor");
        if (retenedor==true) {
            rbtn_retenedorSi.setChecked(retenedor);
        }
    }

    @Override
    public void onBackPressed() {

        showDialogSalir();
    }

    void showDialogSalir(){
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_alert)
                .setTitle("Ficha cliente")
                .setMessage("Se perderan los datos del cliente")
                .setPositiveButton("Confirmar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,int which) {
                                finish();
                            }
                        }).setNegativeButton("Cancelar", null).show();
    }

}
