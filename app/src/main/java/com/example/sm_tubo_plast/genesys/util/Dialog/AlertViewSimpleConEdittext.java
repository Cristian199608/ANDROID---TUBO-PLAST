package com.example.sm_tubo_plast.genesys.util.Dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sm_tubo_plast.genesys.util.VARIABLES;

public  class AlertViewSimpleConEdittext{
    Activity activity;
    Listener listener;
    public String titulo=null, mensaje=null, hint=null, texto_cargado, mensjae_error;
    public int min_caracteres=10;
    public boolean cancelable=true;
    public boolean type_number=false;
    public AlertViewSimpleConEdittext(Activity activity) {
        this.activity=activity;

    }
    public void start(Listener listener){
        EditText editText=new EditText(activity);
        if (type_number){
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            editText.setGravity(Gravity.CENTER);
        }

        if (hint==null) editText.setHint("Ingrese mínimo "+min_caracteres+" caracteres");
        else{
            editText.setHint(hint);
        }
        if (mensjae_error!=null) editText.setError(mensjae_error);
        if (texto_cargado!=null) editText.setText(texto_cargado);

        AlertDialog.Builder aler=new AlertDialog.Builder(activity);
        if (titulo!=null)aler.setTitle(titulo);
        if (mensaje!=null)aler.setMessage(mensaje);
        aler.setCancelable(cancelable);


        aler.setView(editText);
        aler.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (editText.getText().toString().length()>=min_caracteres){
                    if (type_number) {
                        if (VARIABLES.IsDouble(editText.getText().toString())) {
                            listener.resultOK(editText.getText().toString());
                        }else{
                            Toast.makeText(activity, "Ingrese valor vàlido", Toast.LENGTH_SHORT).show();
                            listener.resultBucle(null);
                        }
                    }else listener.resultOK(editText.getText().toString());


                }else{
                    Toast.makeText(activity, "Ingrese al menos "+min_caracteres+" caracteres", Toast.LENGTH_SHORT).show();
                    listener.resultBucle(null);
                }
            }
        });
        aler.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.resultOK(null);
            }
        });
        aler.create().show();
    }
    public void FinalizarActivity(){
        activity.finish();
    }

    public interface Listener{
        String resultOK(String s);
        String resultBucle(String s);
    }
}
