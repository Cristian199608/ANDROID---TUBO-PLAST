package com.example.sm_tubo_plast.genesys.Retrofit.request.pedido.det;

public class RequestComercialPedidoSAP {

    private String moneda;
    private String tipo_cambio;
    private RequestCondicionPagoPedidoSAP condicion_pago;

    public RequestComercialPedidoSAP() {
    }

    public RequestComercialPedidoSAP(String moneda,
                                     String tipo_cambio,
                                     RequestCondicionPagoPedidoSAP condicion_pago) {

        this.moneda = moneda;
        this.tipo_cambio = tipo_cambio;
        this.condicion_pago = condicion_pago;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getTipo_cambio() {
        return tipo_cambio;
    }

    public void setTipo_cambio(String tipo_cambio) {
        this.tipo_cambio = tipo_cambio;
    }

    public RequestCondicionPagoPedidoSAP getCondicion_pago() {
        return condicion_pago;
    }

    public void setCondicion_pago(RequestCondicionPagoPedidoSAP condicion_pago) {
        this.condicion_pago = condicion_pago;
    }
}