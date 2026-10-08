package com.example.sm_tubo_plast.genesys.BEAN;

public class ItemProductoVenta {
    String cip;
    double precioNeto;
    double precioBruto;
    double precioDescuento;
    double cantidad;
    String codUnimed;
    String desUnimed;

    public String getCip() {
        return cip;
    }

    public void setCip(String cip) {
        this.cip = cip;
    }

    public double getPrecioNeto() {
        return precioNeto;
    }

    public void setPrecioNeto(double precioNeto) {
        this.precioNeto = precioNeto;
    }

    public double getPrecioBruto() {
        return precioBruto;
    }

    public void setPrecioBruto(double precioBruto) {
        this.precioBruto = precioBruto;
    }

    public double getPrecioDescuento() {
        return precioDescuento;
    }

    public void setPrecioDescuento(double precioDescuento) {
        this.precioDescuento = precioDescuento;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public String getCodUnimed() {
        return codUnimed;
    }

    public void setCodUnimed(String codUnimed) {
        this.codUnimed = codUnimed;
    }

    public String getDesUnimed() {
        return desUnimed;
    }

    public void setDesUnimed(String desUnimed) {
        this.desUnimed = desUnimed;
    }
}