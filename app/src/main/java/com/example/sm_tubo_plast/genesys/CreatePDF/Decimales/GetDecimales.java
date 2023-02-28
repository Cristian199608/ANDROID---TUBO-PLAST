package com.example.sm_tubo_plast.genesys.CreatePDF.Decimales;

import java.text.DecimalFormat;

public class GetDecimales
{
    public String obtieneDosDecimales(Double number)
    {
        DecimalFormat format = new DecimalFormat("###,###.00");
        return format.format(number);
    }
}
