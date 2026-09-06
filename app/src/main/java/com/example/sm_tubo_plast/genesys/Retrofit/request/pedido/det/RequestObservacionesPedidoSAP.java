package com.example.sm_tubo_plast.genesys.Retrofit.request.pedido.det;

public class RequestObservacionesPedidoSAP {

    private String pedido;
    private String descuento;
    private String tipo_producto;
    private String adicional;
    private String documento;
    private String despacho;

    public RequestObservacionesPedidoSAP() {
    }

    public RequestObservacionesPedidoSAP(String pedido,
                                         String descuento,
                                         String tipo_producto,
                                         String adicional,
                                         String documento,
                                         String despacho) {

        this.pedido = pedido;
        this.descuento = descuento;
        this.tipo_producto = tipo_producto;
        this.adicional = adicional;
        this.documento = documento;
        this.despacho = despacho;
    }

    public String getPedido() {
        return pedido;
    }

    public void setPedido(String pedido) {
        this.pedido = pedido;
    }

    public String getDescuento() {
        return descuento;
    }

    public void setDescuento(String descuento) {
        this.descuento = descuento;
    }

    public String getTipo_producto() {
        return tipo_producto;
    }

    public void setTipo_producto(String tipo_producto) {
        this.tipo_producto = tipo_producto;
    }

    public String getAdicional() {
        return adicional;
    }

    public void setAdicional(String adicional) {
        this.adicional = adicional;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getDespacho() {
        return despacho;
    }

    public void setDespacho(String despacho) {
        this.despacho = despacho;
    }
}