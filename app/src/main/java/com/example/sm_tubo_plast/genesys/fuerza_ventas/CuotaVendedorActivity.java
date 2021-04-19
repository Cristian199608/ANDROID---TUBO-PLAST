package com.example.sm_tubo_plast.genesys.fuerza_ventas;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.datatypes.DB_CuotaVendedor;
import com.example.sm_tubo_plast.genesys.datatypes.DBclasses;
import com.example.sm_tubo_plast.genesys.util.GlobalFunctions;

import java.text.DecimalFormat;


public class CuotaVendedorActivity extends AppCompatActivity {

    private TextView campo0,campo1,campo2,campo3,campo4,campo5;
    private DBclasses dbclass;
    private String secuencia;
    private LinearLayout grafico1,grafico2;
    //private GraphicalView mChartView;
    private DecimalFormat formato;
    double cuota;
    double ventas_efectivas;

    // Color of each Pie Chart Sections
    int[] colors1 = {Color.parseColor("#A0E440"), Color.parseColor("#32CD32")};
    int[] colors2 = {Color.parseColor("#FE9A2E"), Color.parseColor("#DF7401")};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuota_vendedor);

        Bundle bundle = getIntent().getExtras();
        secuencia = bundle.getString("SECUENCIA");

        dbclass = new DBclasses(getApplicationContext());
        formato = GlobalFunctions.formateador();

        campo0 = (TextView)findViewById(R.id.cuota_vendedor_dato0);
        campo1 = (TextView)findViewById(R.id.cuota_vendedor_dato1);
        campo2 = (TextView)findViewById(R.id.cuota_vendedor_dato2);
        campo3 = (TextView)findViewById(R.id.cuota_vendedor_dato3);
        campo4 = (TextView)findViewById(R.id.cuota_vendedor_dato4);
        campo5 = (TextView)findViewById(R.id.cuota_vendedor_dato5);
        grafico1 = (LinearLayout)findViewById(R.id.cuota_vendedor_graf1);
        grafico2 = (LinearLayout)findViewById(R.id.cuota_vendedor_graf2);

        DB_CuotaVendedor cuota_vendedor = dbclass.getDatosCuotaVendedorXsec(Integer.parseInt(secuencia));

        String participacion = formato.format(Double.parseDouble(cuota_vendedor.getPorcentajeParticipacion()));
        String avance = formato.format(Double.parseDouble(cuota_vendedor.getPorcentaje_Avance()));

        cuota = Double.parseDouble(cuota_vendedor.getCuota());
        ventas_efectivas = Double.parseDouble(cuota_vendedor.getVentas_efectivas());

        campo0.setText(formato.format(Double.parseDouble(cuota_vendedor.getCuota())));
        campo1.setText(formato.format(Double.parseDouble(cuota_vendedor.getVentas())));
        campo2.setText(formato.format(Double.parseDouble(cuota_vendedor.getDevoluciones())));
        campo3.setText(formato.format(Double.parseDouble(cuota_vendedor.getVentas_efectivas())));
        campo4.setText(formato.format(Double.parseDouble(cuota_vendedor.getPorcentajeParticipacion()))+"%");
        campo5.setText(formato.format(Double.parseDouble(cuota_vendedor.getPorcentaje_Avance()))+"%");

        openChart(Double.parseDouble(participacion),grafico1,colors1,1);
        openChart(Double.parseDouble(avance),grafico2,colors2,2);

    }


    private void openChart(double dato, LinearLayout grafico, int[] colors, int num_graf){

        String[] code;
        String Titulo = "";

        // Pie Chart Section Names
        if(num_graf == 1){
            code = new String[] {" Otros \n"+Double.toString(100-dato)+"%"," Yo "+Double.toString(dato)+"%"};
            Titulo = "PARTICIPACION";
        }
        else{
            code = new String[] {" "+formato.format(cuota-ventas_efectivas)+" - "+Double.toString(100-dato)+"%"," "+formato.format(ventas_efectivas)+" - "+Double.toString(dato)+"%"};
            Titulo = "AVANCE DE CUOTA";
        }


//        // Pie Chart Section Value
//        double[] distribution = {(100-dato), dato} ;
//
//        // Instantiating CategorySeries to plot Pie Chart
//        CategorySeries distributionSeries = new CategorySeries("");
//
//        for(int i=0 ;i < distribution.length;i++){
//            // Adding a slice with its values and name to the Pie Chart
//            distributionSeries.add(code[i], distribution[i]);
//        }
//
//        // Instantiating a renderer for the Pie Chart
//        DefaultRenderer defaultRenderer  = new DefaultRenderer();
//        for(int i = 0 ;i<distribution.length;i++){
//            SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
//            seriesRenderer.setColor(colors[i]);
//            seriesRenderer.setDisplayChartValues(true);
//            // Adding a renderer for a slice
//            defaultRenderer.addSeriesRenderer(seriesRenderer);
//        }
//
//        defaultRenderer.setChartTitle(Titulo);
//        defaultRenderer.setChartTitleTextSize(14);
//        defaultRenderer.setLabelsTextSize(14);
//        defaultRenderer.setLabelsColor(Color.WHITE);
//        defaultRenderer.setAntialiasing(true);
//        defaultRenderer.setZoomButtonsVisible(false);
//        defaultRenderer.setShowLegend(false);
//        defaultRenderer.setZoomEnabled(false);
//        defaultRenderer.setClickEnabled(false);
//        defaultRenderer.setPanEnabled(false);
//        defaultRenderer.setScale(0.8f);
//
//
//
//        mChartView = ChartFactory.getPieChartView(getApplicationContext(), distributionSeries, defaultRenderer);
//
//        grafico.addView(mChartView, new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));

    }

}

