package com.example.sm_tubo_plast.genesys.CreatePDF.getComprobacion;

import java.text.DecimalFormat;
import java.util.Objects;

public class Comprobaciones
{
    public static String getComprobacionDeuda(String deuda)
    {
        String devolucion;
        devolucion = deuda.length()>0?deuda:"0";
        DecimalFormat format = new DecimalFormat("0.00");
        return format.format(Double.parseDouble(devolucion));
    }

    public static String getComprobacionObligacion(String obligacion)
    {
        String devolucion;
        devolucion = obligacion.length()>0?obligacion:"0";
        DecimalFormat format = new DecimalFormat("0.00");
        return format.format(Double.parseDouble(devolucion));
    }
}
