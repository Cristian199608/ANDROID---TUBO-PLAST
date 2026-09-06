package com.example.sm_tubo_plast.constans.pedidos.workflow;

import java.util.ArrayList;
import java.util.List;


public class WorkflowAprobaciones {

    // Códigos de bloqueo
    public static final String FLG_CAMBIO_CATEGORIA = "flg_cambio_categoria";// consultar a CANTOL
    public static final String FLG_CAMBIO_CONDICION_PAGO = "flg_cambio_condicion_pago";//OK::si cliente limite credito = 0 y vende a credito, se activa a workflow
    public static final String FLG_CAMBIO_DIR_ENTREGA = "flg_cambio_dir_entrega";//consultar (no tenemos datos)
    public static final String FLG_CAMBIO_LISTA_PRECIO = "flg_cambio_lista_precio";// cuando es cambio de categoria
    public static final String FLG_CAMBIO_TRANSPORTE = "flg_cambio_transporte";//consultar (no tenemos)
    public static final String FLG_CLIENTE_NUEVO = "flg_cliente_nuevo";// OK::cuando la ultima compra es valor 0
    public static final String FLG_DOCUMENTOS_VENCIDOS = "flg_documentos_vencidos";//mapeado
    public static final String FLG_EXCESO_LINEA_CREDITO = "flg_exceso_linea_credito";//mapeado
    public static final String FLG_PEDIDO_FUERA_HORARIO = "flg_pedido_fuera_horario";// ok::no permitir enviar al servidor, ni guardar local
    public static final String FLG_PROD_RESTRINGIDO = "flg_prod_restringido";//consultar cantol (no tenemos)
    public static final String FLG_SOLICITUD_LINEA_CREDITO = "flg_solicitud_linea_credito";// OK ::cuando no tiene asignado limite credito(0)
    public static final String FLG_VENTA_MARGEN_BAJO = "flg_venta_margen_bajo";//OK --monto minimo de venta


    private String codigoBloqueo;
    private String criterio;

    public WorkflowAprobaciones(String codigoBloqueo, String criterio) {
        this.codigoBloqueo = codigoBloqueo;
        this.criterio = criterio;
    }

    public String getCodigoBloqueo() {
        return codigoBloqueo;
    }

    public String getCriterio() {
        return criterio;
    }

    public static WorkflowAprobaciones getItemBy(ArrayList<WorkflowAprobaciones> list, String nombre){
        for (WorkflowAprobaciones workflowAprobaciones : list) {
            if(workflowAprobaciones.codigoBloqueo.equalsIgnoreCase(nombre))
                return workflowAprobaciones;
        }
        return  null;
    }

    public static ArrayList<WorkflowAprobaciones> getLista() {

        ArrayList<WorkflowAprobaciones> lista = new ArrayList<>();

        lista.add(new WorkflowAprobaciones(
                FLG_CAMBIO_CATEGORIA,
                "Cambio de categoria de cliente."
        ));


        lista.add(new WorkflowAprobaciones(
                FLG_CAMBIO_CONDICION_PAGO,
                "Cambio de condición de pago."
        ));

        lista.add(new WorkflowAprobaciones(
                FLG_CAMBIO_DIR_ENTREGA,
                "Cambio de dirección de entrega."
        ));

        lista.add(new WorkflowAprobaciones(
                FLG_CAMBIO_LISTA_PRECIO,
                "Cambio de lista de precios."
        ));

        lista.add(new WorkflowAprobaciones(
                FLG_CAMBIO_TRANSPORTE,
                "Cambio de transportista."
        ));

        lista.add(new WorkflowAprobaciones(
                FLG_CLIENTE_NUEVO,
                "Cliente nuevo."
        ));

        lista.add(new WorkflowAprobaciones(
                FLG_DOCUMENTOS_VENCIDOS,
                "Facturas vencidas."
        ));

        lista.add(new WorkflowAprobaciones(// saldo disponible - deuda - pedido solo creditos
                FLG_EXCESO_LINEA_CREDITO,
                "Exceso de línea de crédito."
        ));

        lista.add(new WorkflowAprobaciones(
                FLG_PEDIDO_FUERA_HORARIO,
                "Pedido fuera del horario permitido."
        ));

        lista.add(new WorkflowAprobaciones(
                FLG_PROD_RESTRINGIDO,
                "Productos restringidos."
        ));

        lista.add(new WorkflowAprobaciones(
                FLG_SOLICITUD_LINEA_CREDITO,
                "Venta al crédito."
        ));

        lista.add(new WorkflowAprobaciones(
                FLG_VENTA_MARGEN_BAJO,
                "Venta con margen bajo."
        ));

        return lista;
    }
}

