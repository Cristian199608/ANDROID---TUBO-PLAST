package com.example.sm_tubo_plast;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.example.sm_tubo_plast.domain.PedidoCabeceraUC.ClonarPedidoUseCase;
import com.example.sm_tubo_plast.genesys.DAO.DAO_Pedido;
import com.example.sm_tubo_plast.genesys.datatypes.DBPedido_Cabecera;
import com.example.sm_tubo_plast.genesys.datatypes.DBclasses;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.PedidosActivity;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.Reportes.ReportesPedidosCotizacionYVisitaActivity;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import androidx.test.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public  class DBHelperTest {

    private DBclasses dbHelper;
    Context context;
    DAO_Pedido daoPedidoDetalle;
    String codven="V32";
    @Before
    public void setup() {
        context = InstrumentationRegistry.getTargetContext();
        dbHelper = new DBclasses(context);
        daoPedidoDetalle= new DAO_Pedido(context);
    }

    @Test
    public void testGetRegistroPedidoCabexcera() {
        // Configurar el cursor de prueba con los datos necesarios
        ReportesPedidosCotizacionYVisitaActivity reportPed= new ReportesPedidosCotizacionYVisitaActivity();
        DBPedido_Cabecera pedCabecera = dbHelper.getRegistroPedidoCabecera("V3224012503");
        assertTrue(pedCabecera.getOc_numero().equals("V3224012503"));

    }


    @Test
    public void inocarClonarPedido() {
        // Configurar el cursor de prueba con los datos necesarios
        String oc_numero="V3224012502";
        String oc_numeroNew="";
        try{
            boolean isChangeMoneda= true;
            String tipoRegistro= PedidosActivity.TIPO_PEDIDO;

            ClonarPedidoUseCase clonarPedidoUseCase= new ClonarPedidoUseCase(
                    dbHelper,
                    daoPedidoDetalle,
                    codven,
                    oc_numero
            );

            DBPedido_Cabecera pedPre=dbHelper.getPedido_cabecera(oc_numero);
            DBPedido_Cabecera pedCab=clonarPedidoUseCase.invoke(isChangeMoneda, tipoRegistro);
            assertNotNull(pedCab);
            oc_numeroNew=pedCab.getOc_numero();
            assertEquals(pedCab.getTipoRegistro(), tipoRegistro);
            assertNotEquals(pedCab.getMoneda(), pedPre.getMoneda());

        }catch (Exception e){
            assertNull(e);
        }finally {
            dbHelper.eliminar_pedido(oc_numeroNew);
        }

    }

    @After
    public void cleanup() {
        dbHelper.close();
    }


}