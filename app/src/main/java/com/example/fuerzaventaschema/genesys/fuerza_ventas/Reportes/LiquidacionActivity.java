package com.example.fuerzaventaschema.genesys.fuerza_ventas.Reportes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fuerzaventaschema.R;
import com.example.fuerzaventaschema.genesys.datatypes.DBclasses;
import com.example.fuerzaventaschema.genesys.util.GlobalFunctions;

import java.text.DecimalFormat;

public class LiquidacionActivity extends FragmentActivity {

    DBclasses helper;
    TextView tv_cobranza_soles,tv_cobranza_dolares,
            tv_deposito_soles, tv_deposito_dolares,
            tv_saldo_soles, tv_saldo_dolares;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liquidacion);

        helper = new DBclasses(getApplicationContext());

		/*DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
		simbolos.setDecimalSeparator('.');
		simbolos.setGroupingSeparator(',');*/

        //DecimalFormat formateador = new DecimalFormat("###,##0.00", simbolos);

        DecimalFormat formateador = GlobalFunctions.formateador();


        tv_cobranza_soles = (TextView)findViewById(R.id.act_liquidacion_txtcobranza_soles);
        tv_cobranza_dolares = (TextView)findViewById(R.id.act_liquidacion_txtcobranza_dolares);
        tv_deposito_soles = (TextView)findViewById(R.id.act_liquidacion_txtdepositos_soles);
        tv_deposito_dolares = (TextView)findViewById(R.id.act_liquidacion_txtdepositos_dolares);
        tv_saldo_soles = (TextView)findViewById(R.id.act_liquidacion_txtsaldo_soles);
        tv_saldo_dolares = (TextView)findViewById(R.id.act_liquidacion_txtsaldo_dolares);

        try{
            double total_cobranza_soles = Double.parseDouble(helper.getMontoTotalCobranzaXmoneda("02"));
            double total_cobranza_dolares = Double.parseDouble(helper.getMontoTotalCobranzaXmoneda("01"));
            double deposito_soles = Double.parseDouble(helper.getMontoTotalDeposito("02"));
            double deposito_dolares = Double.parseDouble(helper.getMontoTotalDeposito("01"));

            tv_cobranza_soles.setText("(MN) S/. "+ formateador.format(total_cobranza_soles));
            tv_cobranza_dolares.setText("(USD) $ "+ formateador.format(total_cobranza_dolares));
            tv_deposito_soles.setText("(MN) S/. "+ formateador.format(deposito_soles));
            tv_deposito_dolares.setText("(USD) $ "+ formateador.format(deposito_dolares));
            tv_saldo_soles.setText("(MN) S/. "+formateador.format(GlobalFunctions.redondear((total_cobranza_soles - deposito_soles))));
            tv_saldo_dolares.setText("(USD) $ "+formateador.format(GlobalFunctions.redondear((total_cobranza_dolares - deposito_dolares))));
        }catch(Exception e){
            Toast.makeText(this, "Vaya, algo ha salido mal!", Toast.LENGTH_SHORT).show();
        }
    }
}

