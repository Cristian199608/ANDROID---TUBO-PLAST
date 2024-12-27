package com.example.sm_tubo_plast.genesys.CreatePDF.Util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PDFUtil {
    /*
    Paso 1:	sumar total venta + descuento total
paso2:	multiplicar el desceunto total * 100
paso 3:	dividir el valor de paso2 / con valor de paso 1
pas 4: redondear a 3 decimales

     */

    public static double getPorcentajeDsctBy(double totalVentaFinal, double dsc){
        double totalVentaSinDsc=totalVentaFinal+dsc;
        double pscDsct= (dsc*100)/totalVentaSinDsc;

        return getRedondeo3Decimal(pscDsct);
    }
    public static double getRedondeo3Decimal(double pscDsct){
        BigDecimal bd = new BigDecimal(pscDsct).setScale(3, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
