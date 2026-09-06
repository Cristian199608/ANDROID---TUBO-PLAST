package com.example.sm_tubo_plast.genesys.Retrofit.request.pedido.det;

public class RequestTotalesPedidoSAP {

    private double subtotal;
    private double igv;
    private double descuento_total;
    private double total;
    private double peso_total;
    private double volumen_total;

    public RequestTotalesPedidoSAP() {
    }

    public RequestTotalesPedidoSAP(double subtotal,
                                   double igv,
                                   double descuento_total,
                                   double total,
                                   double peso_total,
                                   double volumen_total) {

        this.subtotal = subtotal;
        this.igv = igv;
        this.descuento_total = descuento_total;
        this.total = total;
        this.peso_total = peso_total;
        this.volumen_total = volumen_total;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getIgv() {
        return igv;
    }

    public void setIgv(double igv) {
        this.igv = igv;
    }

    public double getDescuento_total() {
        return descuento_total;
    }

    public void setDescuento_total(double descuento_total) {
        this.descuento_total = descuento_total;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getPeso_total() {
        return peso_total;
    }

    public void setPeso_total(double peso_total) {
        this.peso_total = peso_total;
    }

    public double getVolumen_total() {
        return volumen_total;
    }

    public void setVolumen_total(double volumen_total) {
        this.volumen_total = volumen_total;
    }
}