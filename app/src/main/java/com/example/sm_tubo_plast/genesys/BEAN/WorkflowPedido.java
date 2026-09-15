package com.example.sm_tubo_plast.genesys.BEAN;

import com.example.sm_tubo_plast.constans.pedidos.workflow.WorkflowAprobaciones;

public class WorkflowPedido {
    private String oc_numero;
    private String codigo_bloqueo;
    private int estado;
    private String detalle;

    public WorkflowPedido() {
    }

    public WorkflowPedido(String oc_numero, String codigo_bloqueo, int estado, String detalle) {
        this.oc_numero = oc_numero;
        this.codigo_bloqueo = codigo_bloqueo;
        this.estado = estado;
        this.detalle=detalle;
    }

    public String getOcNumero() {
        return oc_numero;
    }

    public void setOcNumero(String oc_numero) {
        this.oc_numero = oc_numero;
    }

    public String getCodigoBloqueo() {
        return codigo_bloqueo;
    }

    public void setCodigoBloqueo(String codigo_bloqueo) {
        this.codigo_bloqueo = codigo_bloqueo;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public static WorkflowPedido convertFrom(WorkflowAprobaciones wped,
                                             String oc_numero,
                                             String detalle){
        return new WorkflowPedido(
                oc_numero,
                wped.getCodigoBloqueo(),
                -1,
                detalle
        );
    }
}