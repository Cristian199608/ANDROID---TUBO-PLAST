package com.example.fuerzaventaschema.genesys.fuerza_ventas.cliente;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Iterator;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.fuerzaventaschema.R;
import com.example.fuerzaventaschema.genesys.datatypes.DBClientes;
import com.example.fuerzaventaschema.genesys.datatypes.DBclasses;
import com.example.fuerzaventaschema.genesys.fuerza_ventas.ClienteDetalleActivity;

@SuppressLint("LongLogTag")
public class ClienteInformacionActivity extends AppCompatActivity {

    String telefono="";
    String codcli="";
    DBclasses obj_dbclasses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_informacion);

        obj_dbclasses= new DBclasses(getApplicationContext());
        Bundle bundle = getIntent().getExtras();
        codcli=""+bundle.getString("CODCLI");
        EditText edt_nombre=(EditText)findViewById(R.id.cliente_info_edt_nombre);
        EditText edt_ruc = (EditText)findViewById(R.id.cliente_info_edt_ruc);
        EditText edt_credito=(EditText)findViewById(R.id.cliente_info_edt_credito);
        EditText edt_tiempo=(EditText)findViewById(R.id.cliente_info_edt_tiempo);
        Button button = (Button) findViewById(R.id.cliente_info_btn_telefono);
        EditText edt_telefono=(EditText)findViewById(R.id.cliente_info_edt_telefono);
        PhoneCallListener phoneListener = new PhoneCallListener();
        TelephonyManager telephonyManager = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneListener,PhoneStateListener.LISTEN_CALL_STATE);


	/*	EndCallListener callListener = new EndCallListener();
		TelephonyManager mTM = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
		mTM.listen(callListener, PhoneStateListener.LISTEN_CALL_STATE);
		*/

        // add button listener
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+telefono));
                startActivity(callIntent);

            }

        });

        ArrayList<DBClientes> al_clientes=new ArrayList<DBClientes>();
        al_clientes= obj_dbclasses.getClientesxCodigo(codcli);
        Iterator<DBClientes> it= al_clientes.iterator();

        while ( it.hasNext() ) {
            Object objeto = it.next();
            DBClientes clientess = (DBClientes)objeto;
            edt_nombre.setText(""+clientess.getNomcli());
            edt_ruc.setText(""+clientess.getRuccli());
            edt_credito.setText(""+clientess.getLimite_credito());
            edt_tiempo.setText(""+clientess.getTiempo_credito());
            telefono = obj_dbclasses.getTelefonoxCliente(clientess.getCodcli());
            edt_telefono.setText(""+telefono);
        }
    }


    private void call() {
        try {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse(telefono));
            startActivity(callIntent);
        } catch (ActivityNotFoundException activityException) {
            Log.e("helloandroid dialing example", "Call failed");
        }
    }

    private class PhoneCallListener extends PhoneStateListener {

        private boolean isPhoneCalling = false;

        String LOG_TAG = "LOGGING 123";

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            if (TelephonyManager.CALL_STATE_RINGING == state) {
                // phone ringing
                Log.i(LOG_TAG, "RINGING, number: " + incomingNumber);
            }

            if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
                // active
                Log.i(LOG_TAG, "OFFHOOK");

                isPhoneCalling = true;
            }

            if (TelephonyManager.CALL_STATE_IDLE == state) {
                // run when class initial and phone call ended,
                // need detect flag from CALL_STATE_OFFHOOK
                Log.i(LOG_TAG, "IDLE");

                if (isPhoneCalling) {

                    Log.i(LOG_TAG, "restart app");

                    // restart app
                    Intent i = getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage(
                                    getBaseContext().getPackageName());
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);

                    isPhoneCalling = false;
                }

            }
        }
    }

    private class EndCallListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            if(TelephonyManager.CALL_STATE_RINGING == state) {
                Log.i("LLAMADAS", "RINGING, number: " + incomingNumber);
            }
            if(TelephonyManager.CALL_STATE_OFFHOOK == state) {
                //wait for phone to go offhook (probably set a boolean flag) so you know your app initiated the call.
                Log.i("LLAMADAS", "OFFHOOK");
            }
            if(TelephonyManager.CALL_STATE_IDLE == state) {

                Intent i=new Intent(getApplicationContext(), ClienteDetalleActivity.class);

                i.putExtra("CODCLI",codcli);

                startActivity(i);
                Log.i("LLAMADAS", "IDLE");
            }
        }
    }

}
