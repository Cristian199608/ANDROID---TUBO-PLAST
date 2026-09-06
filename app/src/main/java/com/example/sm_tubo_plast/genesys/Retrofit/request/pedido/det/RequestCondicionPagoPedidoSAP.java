package com.example.sm_tubo_plast.genesys.Retrofit.request.pedido.det;

public class RequestCondicionPagoPedidoSAP {

    private String codigo_condicion_pago;
    private String descripcion;

    public RequestCondicionPagoPedidoSAP() {
    }

    public RequestCondicionPagoPedidoSAP(String codigo_condicion_pago,
                                         String descripcion) {

        this.codigo_condicion_pago = codigo_condicion_pago;
        this.descripcion = descripcion;
    }

    // getters y setters
}