package com.example.sm_tubo_plast.genesys.fuerza_ventas.CustomView;

import android.app.Activity;
import android.app.Dialog;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.BEAN.Cliente_Contacto;
import com.example.sm_tubo_plast.genesys.DAO.DAO_Cliente_Contacto;
import com.example.sm_tubo_plast.genesys.datatypes.DBclasses;
import com.example.sm_tubo_plast.genesys.util.CustomDateTimePicker;
import com.example.sm_tubo_plast.genesys.util.UtilViewMensaje;
import com.example.sm_tubo_plast.genesys.util.VARIABLES;

public class CrearCliente_Contacto {



    Activity activity;
    DBclasses dBclasses;
    TextView tvFechaNacimiento;
    EditText et_nombres,etdni, et_telefono,et_celular, et_correo,et_cargo;
    Button text_cancelar,txt_guardar;
    String codcliente;
    Cliente_Contacto clienteContactoEdit=null;

    Dialog dialogo;
    MyListener mylistner;
    public CrearCliente_Contacto(Activity activity, DBclasses dBclasses) {
        this.activity = activity;
        this.dBclasses = dBclasses;
    }

    public void setDataEdit(Cliente_Contacto clienteContacto) {
        this.clienteContactoEdit=clienteContacto;
    }
    public void VistaCreate(String codcliente, MyListener mylistner){
        this.codcliente=codcliente;
        this.mylistner=mylistner;
        dialogo = new Dialog(activity);
        dialogo.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialogo.setContentView(R.layout.layout_cliente_contacto);
        dialogo.setCancelable(false);
        et_nombres=dialogo.findViewById(R.id.et_nombres);
        etdni=dialogo.findViewById(R.id.etdni);
        tvFechaNacimiento=dialogo.findViewById(R.id.tvFechaNacimiento);
        et_telefono=dialogo.findViewById(R.id.et_telefono);
        et_celular=dialogo.findViewById(R.id.et_celular);
        et_correo=dialogo.findViewById(R.id.et_correo);
        et_cargo=dialogo.findViewById(R.id.et_cargo);

        text_cancelar=dialogo.findViewById(R.id.text_cancelar);
        txt_guardar=dialogo.findViewById(R.id.txt_guardar);


        dialogo.show();

        tvFechaNacimiento.setOnClickListener(v -> {
            CustomDateTimePicker custom= new CustomDateTimePicker(activity, (myCalendar, fecha_formateado) -> {
                if (fecha_formateado!=null){
                    tvFechaNacimiento.setText(fecha_formateado);
                    tvFechaNacimiento.setHint(fecha_formateado);
                } else{
                    tvFechaNacimiento.setError("Error Fecha");
                }
                return null;
            }, 0,0, true, false, false);
            custom.setFormatFecha("dd/MM/yyyy");
            custom.Show();

        });
        
        txt_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarDatos();
            }
        });
        text_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogo.dismiss();
                mylistner.crearcion(null);
            }
        });
        poblarFormularioIfIsEdit();
    }

    private void poblarFormularioIfIsEdit() {
        if(clienteContactoEdit==null)
            return;
        //-----------------------------------------------------------------------------------------------
        et_nombres.setText(clienteContactoEdit.getNombre_contacto());
        etdni.setText(clienteContactoEdit.getDni());
        tvFechaNacimiento.setText(clienteContactoEdit.getFec_nacimiento());//parsear fecha
        et_telefono.setText(clienteContactoEdit.getTelefono());
        et_celular.setText(clienteContactoEdit.getCelular());
        et_correo.setText(clienteContactoEdit.getEmail());
        et_cargo.setText(clienteContactoEdit.getCargo());
    }
    private boolean isValidForm(){
        int error=0;
        et_nombres.setError(null);
        if (TextUtils.isEmpty(et_nombres.getText().toString())) {
            et_nombres.setError("Ingrese nombres");
            error++;
        }

        etdni.setError(null);
        /*if (TextUtils.isEmpty(etdni.getText().toString())) {
            etdni.setError("Ingrese nombres");
            error++;
        }*/

        et_correo.setError(null);
        if (!TextUtils.isEmpty(et_correo.getText().toString())) {
            if(!VARIABLES.validarEmail(et_correo.getText().toString())){
                et_correo.setError("Ingrese un correo vàlido");
                error++;
            }
        }

        tvFechaNacimiento.setError(null);
        if (!TextUtils.isEmpty(tvFechaNacimiento.getText().toString())) {
            if(!VARIABLES.isDate(tvFechaNacimiento.getText().toString())){
                tvFechaNacimiento.setError("Ingrese fecha valido dd/mm/yyyy");
                error++;
            }
        }
        return error==0;

    }
    private void guardarDatos(){

        if(!isValidForm()){
            return;
        }


        DAO_Cliente_Contacto dao_cliente_contacto=new DAO_Cliente_Contacto();
        if(clienteContactoEdit==null){
            boolean exisite=dao_cliente_contacto.getClienteContactoByDNI(dBclasses, codcliente, etdni.getText().toString());
            if (exisite){
                UtilViewMensaje.MENSAJE_simple(activity, "Contacto Nuevo?", "Este contacto para este cliente ya existe");
                return;
            }
        }
        //-----------------------------------------------------------------------------------------------
        int nexId=0;
        if(clienteContactoEdit!=null) nexId = clienteContactoEdit.getId_contacto();
        else nexId = dao_cliente_contacto.getNextIdClienteContacto(dBclasses, codcliente);

        Cliente_Contacto contacto=new Cliente_Contacto();
        contacto.setCodcli(codcliente);
        contacto.setId_contacto(nexId);
        contacto.setNombre_contacto(et_nombres.getText().toString().toUpperCase());
        contacto.setDni(etdni.getText().toString());
        contacto.setTelefono(et_telefono.getText().toString());
        contacto.setCelular(et_celular.getText().toString());
        contacto.setEmail(et_correo.getText().toString());
        contacto.setCargo(et_cargo.getText().toString());
        contacto.setFec_nacimiento(tvFechaNacimiento.getText().toString());
        contacto.setEstado("G");
        contacto.setFlag("P");


        boolean isRReister=dao_cliente_contacto.Crear_Contacto(dBclasses, contacto);
        if (isRReister){
            mylistner.crearcion(contacto);
            dialogo.dismiss();
        }
        else{
            showMensaggeError();
        }
    }

    private void showMensaggeError() {
        if(clienteContactoEdit!=null)
            UtilViewMensaje.MENSAJE_simple(activity, "Ohhh!", "No se ha podido actualizar los datos. Vuelva a intentarlo");
        else
            UtilViewMensaje.MENSAJE_simple(activity, "Ohhh!", "No se ha podido crear el contacto. Vuelva a intentarlo");
    }


    public interface  MyListener{
        void crearcion(Cliente_Contacto contacto);
    }
}
