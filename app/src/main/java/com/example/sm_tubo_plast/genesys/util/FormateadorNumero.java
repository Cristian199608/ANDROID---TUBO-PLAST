package com.example.sm_tubo_plast.genesys.util;

import java.text.DecimalFormat;

public class FormateadorNumero {

    public static String formatter2decimal(String numero){
        DecimalFormat formateador = new DecimalFormat("#,###.##");
        double total = Double.parseDouble(numero);
        String numeroFormatter = formateador.format(total);
//        String[] numbers=numeroFormatter.split(".");
       /* if (numbers.length==1){
            numeroFormatter=numeroFormatter+".d00";
        }else{
            if (numbers[0].length()!=2) {
                numeroFormatter=numeroFormatter+"d0";
            }
        }*/
        return numeroFormatter;
    }

    public static String formatter2decimal(double numero){
        DecimalFormat formateador = new DecimalFormat("#,###.##");
        String numeroFormatter = formateador.format(numero);
//        String[] numbers=numeroFormatter.split(".");
       /* if (numbers.length==1){
            numeroFormatter=numeroFormatter+".d00";
        }else{
            if (numbers[0].length()!=2) {
                numeroFormatter=numeroFormatter+"d0";
            }
        }*/
        return numeroFormatter;
    }
}
