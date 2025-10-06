package com.example.sm_tubo_plast;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.example.sm_tubo_plast.domain.PedidoCabeceraUC.ClonarPedidoUseCase;
import com.example.sm_tubo_plast.genesys.DAO.DAO_Pedido;
import com.example.sm_tubo_plast.genesys.DAO.DAO_Pedido_detalle2;
import com.example.sm_tubo_plast.genesys.DAO.DAO_RegistroBonificaciones;
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

import java.lang.reflect.Field;

@RunWith(AndroidJUnit4.class)
public  class DBHelperTest {

    private DBclasses dbHelper;
    Context context;
    DAO_Pedido daoPedidoDetalle;
    DAO_RegistroBonificaciones dao_registroBonificaciones;
    DAO_Pedido_detalle2 dao_pedido_detalle2;
    String codven="V207";
    @Before
    public void setup() {
        context = InstrumentationRegistry.getTargetContext();
        dbHelper = new DBclasses(context);
        daoPedidoDetalle= new DAO_Pedido(context);
        dao_registroBonificaciones= new DAO_RegistroBonificaciones(context);
        dao_pedido_detalle2= new DAO_Pedido_detalle2(context);
    }

    @Test
    public void testGetRegistroPedidoCabexcera() {
        // Configurar el cursor de prueba con los datos necesarios
        ReportesPedidosCotizacionYVisitaActivity reportPed= new ReportesPedidosCotizacionYVisitaActivity();
        DBPedido_Cabecera pedCabecera = dbHelper.getRegistroPedidoCabecera("V3224012503");
        assertTrue(pedCabecera.getOc_numero().equals("V3224012503"));

    }


    @Test
    public void invocarClonarNuevaCotizacionFromCotizacion() {
        // Configurar el cursor de prueba con los datos necesarios
        String oc_numero="V20724013026";
        String oc_numeroNew="";
        try{
            boolean isChangeMoneda= false;
            String tipoRegistroOutput= PedidosActivity.TIPO_COTIZACION;

            ClonarPedidoUseCase clonarPedidoUseCase= new ClonarPedidoUseCase(
                    dbHelper,
                    daoPedidoDetalle,
                    dao_registroBonificaciones,
                    dao_pedido_detalle2,
                    codven,
                    oc_numero
            );
            DBPedido_Cabecera pedPre=dbHelper.getPedido_cabecera(oc_numero);

            DBPedido_Cabecera pedCab=clonarPedidoUseCase.invoke(isChangeMoneda, tipoRegistroOutput);
            assertNotNull(pedCab);
            oc_numeroNew=pedCab.getOc_numero();
            //preramos el object class para verificar que todos son correctos
            DBPedido_Cabecera pedNuevo=dbHelper.getPedido_cabecera(oc_numeroNew);
            Class<?> clase = pedPre.getClass();
            Field[] campos = clase.getDeclaredFields();
            Class<?> claseNuevo = pedNuevo.getClass();
            Field[] camposNuevo = claseNuevo.getDeclaredFields();
            for (int i = 0; i < campos.length; i++) {
                Field campo=campos[i];
                Field campoNuevo=camposNuevo[i];
                campo.setAccessible(true);// Hacer accesible el campo si es privado
                campoNuevo.setAccessible(true);// Hacer accesible el campo si es privado
                String nombreCampo = campo.getName();// Obtener el nombre del campo
                Object valorCampo = campo.get(pedPre); // Obtener el valor del campo en el objeto proporcionado

                Object valorCampo2 = campoNuevo.get(pedNuevo);

                if(nombreCampo.equals("oc_numero")){
                    assertNotEquals(valorCampo, valorCampo2);
                    continue;
                }
                assertEquals("Campo diferente. valor esperado del campo("+nombreCampo+") es "+valorCampo
                        +" pero es "+valorCampo2,valorCampo,valorCampo2);

            }

        }catch (Exception e){
            e.printStackTrace();
            assertNull(e);
        }finally {
            dbHelper.eliminar_pedido(oc_numeroNew);
        }

    }


    @Test
    public void invocarClonarForGeneratePedidoFromCotizacionToPedido() {
        // Configurar el cursor de prueba con los datos necesarios
        String oc_numero="V20724013019";
        String oc_numeroNew="";
        try{
            boolean isChangeMoneda= false;
            String tipoRegistroOutput= PedidosActivity.TIPO_COTIZACION;
            ClonarPedidoUseCase clonarPedidoUseCase= new ClonarPedidoUseCase(
                    dbHelper,
                    daoPedidoDetalle,
                    dao_registroBonificaciones,
                    dao_pedido_detalle2,
                    codven,
                    oc_numero
            );
            DBPedido_Cabecera pedPre=dbHelper.getPedido_cabecera(oc_numero);

            DBPedido_Cabecera pedCab=clonarPedidoUseCase.invoke(isChangeMoneda, tipoRegistroOutput);
            oc_numeroNew=pedCab.getOc_numero();
            //preramos el object class para verificar que todos son correctos
            DBPedido_Cabecera pedNuevo=dbHelper.getPedido_cabecera(oc_numeroNew);
            Class<?> clase = pedPre.getClass();
            Field[] campos = clase.getDeclaredFields();
            Class<?> claseNuevo = pedNuevo.getClass();
            Field[] camposNuevo = claseNuevo.getDeclaredFields();
            for (int i = 0; i < campos.length; i++) {
                Field campo=campos[i];
                Field campoNuevo=camposNuevo[i];
                campo.setAccessible(true);// Hacer accesible el campo si es privado
                campoNuevo.setAccessible(true);// Hacer accesible el campo si es privado
                String nombreCampo = campo.getName();// Obtener el nombre del campo
                Object valorCampo = campo.get(pedPre); // Obtener el valor del campo en el objeto proporcionado

                Object valorCampo2 = campoNuevo.get(pedNuevo);

                if(nombreCampo.equals("oc_numero")){
                    assertNotEquals(valorCampo, valorCampo2);
                    continue;
                }
                if(nombreCampo.equals("tipoRegistro")){
                    assertEquals(valorCampo2, PedidosActivity.TIPO_PEDIDO);
                    continue;
                }
                assertEquals("Campo diferente. valor esperado del campo("+nombreCampo+") es "+valorCampo
                    +" pero es "+valorCampo2,valorCampo,valorCampo2);
            }

        }catch (Exception e){
            e.printStackTrace();
            assertNull(e);
        }finally {
            dbHelper.eliminar_pedido(oc_numeroNew);
        }

    }

    @Test
    public void invocarClonarPedidoForChangeMonedaToDolar() {
        // Configurar el cursor de prueba con los datos necesarios
        String oc_numero="V20724013019";
        String oc_numeroNew="";
        try{
            boolean isChangeMoneda= true;
            String tipoRegistroOutput= PedidosActivity.TIPO_COTIZACION;
            ClonarPedidoUseCase clonarPedidoUseCase= new ClonarPedidoUseCase(
                    dbHelper,
                    daoPedidoDetalle,
                    dao_registroBonificaciones,
                    dao_pedido_detalle2,
                    codven,
                    oc_numero
            );
            DBPedido_Cabecera pedPre=dbHelper.getPedido_cabecera(oc_numero);

            DBPedido_Cabecera pedCab=clonarPedidoUseCase.invoke(isChangeMoneda, tipoRegistroOutput);
            oc_numeroNew=pedCab.getOc_numero();
            //preramos el object class para verificar que todos son correctos
            DBPedido_Cabecera pedNuevo=dbHelper.getPedido_cabecera(oc_numeroNew);
            Class<?> clase = pedPre.getClass();
            Field[] campos = clase.getDeclaredFields();
            Class<?> claseNuevo = pedNuevo.getClass();
            Field[] camposNuevo = claseNuevo.getDeclaredFields();
            for (int i = 0; i < campos.length; i++) {
                Field campo=campos[i];
                Field campoNuevo=camposNuevo[i];
                campo.setAccessible(true);// Hacer accesible el campo si es privado
                campoNuevo.setAccessible(true);// Hacer accesible el campo si es privado
                String nombreCampo = campo.getName();// Obtener el nombre del campo
                Object valorCampo = campo.get(pedPre); // Obtener el valor del campo en el objeto proporcionado

                Object valorCampo2 = campoNuevo.get(pedNuevo);

                if(nombreCampo.equals("oc_numero")){
                    assertNotEquals(valorCampo, valorCampo2);
                    continue;
                }
                if(nombreCampo.equals("moneda")){
                    assertNotEquals(valorCampo, valorCampo2);
                    continue;
                }
                assertEquals("Campo diferente. valor esperado del campo("+nombreCampo+") es "+valorCampo
                        +" pero es "+valorCampo2,valorCampo,valorCampo2);
            }

        }catch (Exception e){
            e.printStackTrace();
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