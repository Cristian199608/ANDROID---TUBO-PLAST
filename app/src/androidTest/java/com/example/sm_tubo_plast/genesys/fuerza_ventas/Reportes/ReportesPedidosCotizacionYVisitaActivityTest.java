package com.example.sm_tubo_plast.genesys.fuerza_ventas.Reportes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.example.sm_tubo_plast.genesys.datatypes.DBPedido_Cabecera;
import com.example.sm_tubo_plast.genesys.datatypes.DBclasses;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.PedidosActivity;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

public class ReportesPedidosCotizacionYVisitaActivityTest  {

    ReportesPedidosCotizacionYVisitaActivity reportPed;
    DBclasses dBclasses;
    @Before
    public void setUp() throws Exception {
        reportPed= new ReportesPedidosCotizacionYVisitaActivity();

    }

    @Test
    private void clonarPedidoTest() {
        boolean isChangeMoneda= true;
        String tipoRegistro= PedidosActivity.TIPO_PEDIDO;
        reportPed.oc_numero="V3224012503";
//        DBPedido_Cabecera pedClonado=reportPed.clonarPedidoOnlyTest(isChangeMoneda, tipoRegistro);
//        assertNotNull(pedClonado);

    }
}