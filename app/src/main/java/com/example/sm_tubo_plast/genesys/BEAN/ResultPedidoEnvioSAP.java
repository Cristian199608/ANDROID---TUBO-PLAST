package com.example.sm_tubo_plast.genesys.BEAN;

public class ResultPedidoEnvioSAP {
    private String estado;
    private String codigo;
    private String mensaje;
    private String oc_numero;
    private int numero_pedido_sap;
    private int docentry;
    private String fecha_procesamiento;

    public ResultPedidoEnvioSAP(String estado,
                                String codigo,
                                String mensaje,
                                String oc_numero,
                                int numero_pedido_sap,
                                int docentry,
                                String fecha_procesamiento) {
        this.estado = estado;
        this.codigo = codigo;
        this.mensaje = mensaje;
        this.oc_numero = oc_numero;
        this.numero_pedido_sap = numero_pedido_sap;
        this.docentry = docentry;
        this.fecha_procesamiento = fecha_procesamiento;
    }

    public String getEstado() {
        return estado;
    }


    public int getNumero_pedido_sap() {
        return numero_pedido_sap;
    }

    public boolean isEnvioAceptado(){
        if (this.estado.equalsIgnoreCase("ACEPTADO")) {
            return true;
        }
        else return false;
    }
}
