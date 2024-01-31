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
    }

    @Test
    public void databaseNameIsProduction() {
        VARIABLES.ConfigDatabase configDatabase = new VARIABLES.ConfigDatabase();
        if(configDatabase.getDatabaseName().toLowerCase().contains("test")
        || configDatabase.getDatabaseName().toLowerCase().contains("prueba")){
            assertEquals("La base de datos no debe ser de prueba o de testing",true,false);
        }

    }


}