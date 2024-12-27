package com.example.sm_tubo_plast.genesys.CreatePDF.Util;

import static org.junit.Assert.assertEquals;


import org.junit.Test;

public class PDFUtilTest {

    @Test
    public void getPorcentajeDsctByTest(){
        double montoConDesc=75;
        double montoDesc=25;
        double pctajeDesc=PDFUtil.getPorcentajeDsctBy(montoConDesc, montoDesc);
        assertEquals( 25, pctajeDesc,0);

        montoConDesc=600.495;
        montoDesc=1754.555;
        pctajeDesc=PDFUtil.getPorcentajeDsctBy(montoConDesc, montoDesc);
        assertEquals( 25, pctajeDesc,0);
    }
    @Test
    public void getRedondeo3DecimalTest(){
        //-------------------------------TEST 1----------------------------------------------------------------
        double valor=152.12356;
        double redondeado=PDFUtil.getRedondeo3Decimal(valor);
        assertEquals(152.124, redondeado, 0);
        //-------------------------------test 2----------------------------------------------------------------
        valor=152.12346;
        redondeado=PDFUtil.getRedondeo3Decimal(valor);
        assertEquals(152.123, redondeado, 0);
        //-------------------------------TEST 3----------------------------------------------------------------
        valor=152.12476;
        redondeado=PDFUtil.getRedondeo3Decimal(valor);
        assertEquals(152.125, redondeado, 0);
        //-------------------------------TEST 4----------------------------------------------------------------
        valor=152.1256;
        redondeado=PDFUtil.getRedondeo3Decimal(valor);
        assertEquals(152.126, redondeado, 0);


    }
}