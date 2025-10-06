package com.example.sm_tubo_plast.genesys.util;

import static org.junit.Assert.assertEquals;


import junit.framework.TestCase;

import org.junit.Test;
import org.junit.Before;

public class VARIABLESTest  {

    VARIABLES variables;
    @Before
    public void onBefore() {
        variables= new VARIABLES();
    }


    @Test
    public void variablesAllBeProduction() {
        variables= new VARIABLES();
         assertEquals(variables.isProduccion,true);
         assertEquals(variables.isProduccion_prueba,false);
         assertEquals(variables.isSetDataPruebas,false);
    }

    @Test
    public void databaseNameIsProduction() {
        VARIABLES.ConfigDatabase configDatabase = new VARIABLES.ConfigDatabase();
        if(configDatabase.getDatabaseName().toLowerCase().contains("test")
        || configDatabase.getDatabaseName().toLowerCase().contains("prueba")){
            assertEquals("La base de datos no debe ser de prueba o de testing",true,false);
        }
    }

    @Test
    public void getDoubleFormaterThreeDecimalTest(){
        double number= 152.1447;
        double valor= VARIABLES.getDoubleFormaterThreeDecimal(number);
        assertEquals(152.145,  valor,0);
        //-------------------------------TEST 2----------------------------------------------------------------
        number= 152.1448;
        valor= VARIABLES.getDoubleFormaterThreeDecimal(number);
        assertEquals(152.145,  valor,0);
        //-------------------------------TEST 3----------------------------------------------------------------
        number= 152.1449;
        valor= VARIABLES.getDoubleFormaterThreeDecimal(number);
        assertEquals(152.145,  valor,0);
        //-------------------------------TEST 4----------------------------------------------------------------
        number= 152.1450;
        valor= VARIABLES.getDoubleFormaterThreeDecimal(number);
        assertEquals(152.145,  valor,0);
        //-------------------------------TEST 5----------------------------------------------------------------
        number= 152.1451;
        valor= VARIABLES.getDoubleFormaterThreeDecimal(number);
        assertEquals(152.145,  valor,0);
        //-------------------------------TEST 6----------------------------------------------------------------
        number= 152.1452;
        valor= VARIABLES.getDoubleFormaterThreeDecimal(number);
        assertEquals(152.145,  valor,0);
        //-------------------------------TEST 7----------------------------------------------------------------
        number= 152.1453;
        valor= VARIABLES.getDoubleFormaterThreeDecimal(number);
        assertEquals(152.145,  valor,0);
        //-------------------------------TEST 8----------------------------------------------------------------
        number= 152.1454;
        valor= VARIABLES.getDoubleFormaterThreeDecimal(number);
        assertEquals(152.145,  valor,0);
        //-------------------------------TEST 9----------------------------------------------------------------

        number= 152.1455;
        valor= VARIABLES.getDoubleFormaterThreeDecimal(number);
        assertEquals(152.146,  valor,3);

    }

    @Test
    public void  testSubString(){
        String cadena="COMBO123";
        String subcadena=cadena.substring(0,5);
        assertEquals("COMBO",subcadena);
    }


}