package com.example.sm_tubo_plast.genesys.BEAN;

public class WorkflowPedido {
    private String oc_numero;
    private String codigo_bloqueo;
    private int estado;

    public WorkflowPedido() {
    }

    public WorkflowPedido(String oc_numero, String codigo_bloqueo, int estado) {
        this.oc_numero = oc_numero;
        this.codigo_bloqueo = codigo_bloqueo;
        this.estado = estado;
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
}