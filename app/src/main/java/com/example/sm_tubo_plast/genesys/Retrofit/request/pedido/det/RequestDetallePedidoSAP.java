package com.example.sm_tubo_plast.genesys.Retrofit.request.pedido.det;

import java.util.ArrayList;
import java.util.List;

public class RequestDetallePedidoSAP {

    private String item;
    private String cod_producto;
    private String descripcion_producto;
    private int cantidad;
    private String unidad_medida;

    private double precio_lista;
    private double precio_unitario;
    private double porcentaje_descuento;
    private double importe_descuento;

    private List<RequetDescuentoPedidoSAP> descuentos;

    private double subtotal;
    private String tipo_afectacion_igv;
    private double percepcion;

    private String tipo_producto;
    private String secuencia_promocion;
    private String item_origen_promocion;
    private String codigo_almacen;

    private double peso_unitario;
    private double peso_total;
    private double volumen_unitario;
    private double volumen_total;

    public RequestDetallePedidoSAP() {
        descuentos = new ArrayList<>();
    }

    public RequestDetallePedidoSAP(String item,
                                   String cod_producto,
                                   String descripcion_producto,
                                   int cantidad,
                                   String unidad_medida,
                                   double precio_lista,
                                   double precio_unitario,
                                   double porcentaje_descuento,
                                   double importe_descuento,
                                   List<RequetDescuentoPedidoSAP> descuentos,
                                   double subtotal,
                                   String tipo_afectacion_igv,
                                   double percepcion,
                                   String tipo_producto,
                                   String secuencia_promocion,
                                   String item_origen_promocion,
                                   String codigo_almacen,
                                   double peso_unitario,
                                   double peso_total,
                                   double volumen_unitario,
                                   double volumen_total) {

        this.item = item;
        this.cod_producto = cod_producto;
        this.descripcion_producto = descripcion_producto;
        this.cantidad = cantidad;
        this.unidad_medida = unidad_medida;
        this.precio_lista = precio_lista;
        this.precio_unitario = precio_unitario;
        this.porcentaje_descuento = porcentaje_descuento;
        this.importe_descuento = importe_descuento;
        this.descuentos = descuentos;
        this.subtotal = subtotal;
        this.tipo_afectacion_igv = tipo_afectacion_igv;
        this.percepcion = percepcion;
        this.tipo_producto = tipo_producto;
        this.secuencia_promocion = secuencia_promocion;
        this.item_origen_promocion = item_origen_promocion;
        this.codigo_almacen = codigo_almacen;
        this.peso_unitario = peso_unitario;
        this.peso_total = peso_total;
        this.volumen_unitario = volumen_unitario;
        this.volumen_total = volumen_total;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getCod_producto() {
        return cod_producto;
    }

    public void setCod_producto(String cod_producto) {
        this.cod_producto = cod_producto;
    }

    public String getDescripcion_producto() {
        return descripcion_producto;
    }

    public void setDescripcion_producto(String descripcion_producto) {
        this.descripcion_producto = descripcion_producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getUnidad_medida() {
        return unidad_medida;
    }

    public void setUnidad_medida(String unidad_medida) {
        this.unidad_medida = unidad_medida;
    }

    public double getPrecio_lista() {
        return precio_lista;
    }

    public void setPrecio_lista(double precio_lista) {
        this.precio_lista = precio_lista;
    }

    public double getPrecio_unitario() {
        return precio_unitario;
    }

    public void setPrecio_unitario(double precio_unitario) {
        this.precio_unitario = precio_unitario;
    }

    public double getPorcentaje_descuento() {
        return porcentaje_descuento;
    }

    public void setPorcentaje_descuento(double porcentaje_descuento) {
        this.porcentaje_descuento = porcentaje_descuento;
    }

    public double getImporte_descuento() {
        return importe_descuento;
    }

    public void setImporte_descuento(double importe_descuento) {
        this.importe_descuento = importe_descuento;
    }

    public List<RequetDescuentoPedidoSAP> getDescuentos() {
        return descuentos;
    }

    public void setDescuentos(List<RequetDescuentoPedidoSAP> descuentos) {
        this.descuentos = descuentos;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public String getTipo_afectacion_igv() {
        return tipo_afectacion_igv;
    }

    public void setTipo_afectacion_igv(String tipo_afectacion_igv) {
        this.tipo_afectacion_igv = tipo_afectacion_igv;
    }

    public double getPercepcion() {
        return percepcion;
    }

    public void setPercepcion(double percepcion) {
        this.percepcion = percepcion;
    }

    public String getTipo_producto() {
        return tipo_producto;
    }

    public void setTipo_producto(String tipo_producto) {
        this.tipo_producto = tipo_producto;
    }

    public String getSecuencia_promocion() {
        return secuencia_promocion;
    }

    public void setSecuencia_promocion(String secuencia_promocion) {
        this.secuencia_promocion = secuencia_promocion;
    }

    public String getItem_origen_promocion() {
        return item_origen_promocion;
    }

    public void setItem_origen_promocion(String item_origen_promocion) {
        this.item_origen_promocion = item_origen_promocion;
    }

    public String getCodigo_almacen() {
        return codigo_almacen;
    }

    public void setCodigo_almacen(String codigo_almacen) {
        this.codigo_almacen = codigo_almacen;
    }

    public double getPeso_unitario() {
        return peso_unitario;
    }

    public void setPeso_unitario(double peso_unitario) {
        this.peso_unitario = peso_unitario;
    }

    public double getPeso_total() {
        return peso_total;
    }

    public void setPeso_total(double peso_total) {
        this.peso_total = peso_total;
    }

    public double getVolumen_unitario() {
        return volumen_unitario;
    }

    public void setVolumen_unitario(double volumen_unitario) {
        this.volumen_unitario = volumen_unitario;
    }

    public double getVolumen_total() {
        return volumen_total;
    }

    public void setVolumen_total(double volumen_total) {
        this.volumen_total = volumen_total;
    }
}