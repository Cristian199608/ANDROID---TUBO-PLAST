package com.example.sm_tubo_plast.domain.PedidoCabeceraUC;

import android.app.Activity;

import com.example.sm_tubo_plast.genesys.DAO.DAO_Pedido;
import com.example.sm_tubo_plast.genesys.DAO.DAO_RegistroBonificaciones;
import com.example.sm_tubo_plast.genesys.datatypes.DBPedido_Cabecera;
import com.example.sm_tubo_plast.genesys.datatypes.DB_RegistroBonificaciones;
import com.example.sm_tubo_plast.genesys.datatypes.DBclasses;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.PedidosActivity;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.Reportes.ReportesPedidosCotizacionYVisitaActivity;
import com.example.sm_tubo_plast.genesys.util.GlobalFunctions;

import java.util.ArrayList;

public class ClonarPedidoUseCase {
    DAO_Pedido DAOPedidoDetalle;
    DBclasses obj_dbclasses;
    DAO_RegistroBonificaciones daoRegistroBonificaciones;
    String oc_numero;
    String codven;
    //constructor
    public ClonarPedidoUseCase(DBclasses obj_dbclasses,
                               DAO_Pedido DAOPedidoDetalle,
                               DAO_RegistroBonificaciones daoRegistroBonificaciones,
                               String codven,
                               String oc_numero
    ){
        this.obj_dbclasses=obj_dbclasses;
        this.DAOPedidoDetalle=DAOPedidoDetalle;
        this.daoRegistroBonificaciones=daoRegistroBonificaciones;
        this.codven=codven;
        this.oc_numero=oc_numero;
    }


    public DBPedido_Cabecera invoke(boolean cambiarMoneda, String tipoRegistro) {

        DBPedido_Cabecera cabecera = obj_dbclasses.getRegistroPedidoCabecera(oc_numero);
        double tipocambio =  Double.parseDouble(obj_dbclasses.getCambio("Tipo_cambio"));

        String pedidoAnterior = cabecera.getOc_numero();
        String fechaOc = cabecera.getFecha_oc();
        String numOc = obj_dbclasses.obtenerMaxNumOc(codven);
        String fecha_configuracion = obj_dbclasses.getCambio("Fecha");
        String nuevoOc_numero = codven + PedidosActivity.calcularSecuencia(numOc,fecha_configuracion);
        cabecera.setOc_numero(nuevoOc_numero);
        cabecera.setFecha_oc(fechaOc);
        cabecera.setPedidoAnterior(pedidoAnterior);
        cabecera.setTipoRegistro(tipoRegistro);
        if(cambiarMoneda){
            if (!cabecera.convertirMonedaFrom(tipocambio)) {
                //GlobalFunctions.showCustomToast(ReportesPedidosCotizacionYVisitaActivity.this, "Error al realizar la conversiòn de la moneda! "+nuevoOc_numero, GlobalFunctions.TOAST_ERROR);
                return null;
            }
        }

        DAOPedidoDetalle.ClonarPedido(cabecera, cambiarMoneda, tipocambio, obj_dbclasses, daoRegistroBonificaciones);//Se guarda referencia del pedido anterior
        //GlobalFunctions.showCustomToast(ReportesPedidosCotizacionYVisitaActivity.this, "Nuevo pedido Generado ! "+nuevoOc_numero, GlobalFunctions.TOAST_DONE);
        return cabecera;

    }
}
