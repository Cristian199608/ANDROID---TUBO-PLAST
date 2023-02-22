package com.example.sm_tubo_plast.genesys.BEAN;

public class ReportePedidoCabeceraDetalle {
    private String oc_numero;
    private String ruccli;
    private String codven;
    private String nomcli;
    private String telefono;
    private String nomven;
    private String direccionFiscal;
    private String email_cliente;
    private String email_vendedor;
    private String descripcion;
    private String desforpag;
    private String monto_total;
    private String valor_igv;
    private String subtotal;
    private String peso_total;
    private String fecha_oc;
    private String fecha_mxe;
    private String codpro;
    private int cantidad;
    private String unidad_medida;
    private String despro;
    private String moneda;
    private String precio_bruto;
    private String precio_neto;
    private double pesoTotalProducto;
    private String porcentaje_desc;
    private double porcentaje_desc_extra;
    private String telefono_vendedor;
    private String text_area;
    private String observacion;
    private String observacion2;
    private String observacion3;

    public ReportePedidoCabeceraDetalle(String oc_numero, String ruccli, String codven,
                                     String nomcli, String telefono, String nomven,
                                     String direccionFiscal, String email_cliente, String email_vendedor,
                                     String descripcion, String desforpag, String monto_total,
                                     String valor_igv, String subtotal, String peso_total,
                                     String fecha_oc, String fecha_mxe, String codpro,
                                     int cantidad, String unidad_medida, String despro,
                                     String moneda, String precio_bruto, String precio_neto,
                                     String porcentaje_desc, double porcentaje_desc_extra, String telefono_vendedor,
                                     String text_area, String observacion, String observacion2,
                                     String observacion3,double pesoTotalProducto) {
        this.oc_numero = oc_numero;
        this.ruccli = ruccli;
        this.codven = codven;
        this.nomcli = nomcli;
        this.telefono = telefono;
        this.nomven = nomven;
        this.direccionFiscal = direccionFiscal;
        this.email_cliente = email_cliente;
        this.email_vendedor = email_vendedor;
        this.descripcion = descripcion;
        this.desforpag = desforpag;
        this.monto_total = monto_total;
        this.valor_igv = valor_igv;
        this.subtotal = subtotal;
        this.peso_total = peso_total;
        this.fecha_oc = fecha_oc;
        this.fecha_mxe = fecha_mxe;
        this.codpro = codpro;
        this.cantidad = cantidad;
        this.unidad_medida = unidad_medida;
        this.despro = despro;
        this.moneda = moneda;
        this.precio_bruto = precio_bruto;
        this.precio_neto = precio_neto;
        this.porcentaje_desc = porcentaje_desc;
        this.porcentaje_desc_extra = porcentaje_desc_extra;
        this.telefono_vendedor = telefono_vendedor;
        this.text_area = text_area;
        this.observacion = observacion;
        this.observacion2 = observacion2;
        this.observacion3 = observacion3;
        this.pesoTotalProducto = pesoTotalProducto;
    }


    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getObservacion2() {
        return observacion2;
    }

    public void setObservacion2(String observacion2) {
        this.observacion2 = observacion2;
    }

    public String getObservacion3() {
        return observacion3;
    }

    public void setObservacion3(String observacion3) {
        this.observacion3 = observacion3;
    }

    public String getText_area() {
        return text_area;
    }

    public void setText_area(String text_area) {
        this.text_area = text_area;
    }

    public String getTelefono_vendedor() {
        return telefono_vendedor;
    }

    public void setTelefono_vendedor(String telefono_vendedor) {
        this.telefono_vendedor = telefono_vendedor;
    }

    public String getPorcentaje_desc() {
        return porcentaje_desc;
    }

    public void setPorcentaje_desc(String porcentaje_desc) {
        this.porcentaje_desc = porcentaje_desc;
    }

    public String getOc_numero() {
        return oc_numero;
    }

    public void setOc_numero(String oc_numero) {
        this.oc_numero = oc_numero;
    }

    public String getRuccli() {
        return ruccli;
    }

    public void setRuccli(String ruccli) {
        this.ruccli = ruccli;
    }

    public String getCodven() {
        return codven;
    }

    public void setCodven(String codven) {
        this.codven = codven;
    }

    public String getNomcli() {
        return nomcli;
    }

    public void setNomcli(String nomcli) {
        this.nomcli = nomcli;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNomven() {
        return nomven;
    }

    public void setNomven(String nomven) {
        this.nomven = nomven;
    }

    public String getDireccionFiscal() {
        return direccionFiscal;
    }

    public void setDireccionFiscal(String direccionFiscal) {
        this.direccionFiscal = direccionFiscal;
    }

    public String getEmail_cliente() {
        return email_cliente;
    }

    public void setEmail_cliente(String email_cliente) {
        this.email_cliente = email_cliente;
    }

    public String getEmail_vendedor() {
        return email_vendedor;
    }

    public void setEmail_vendedor(String email_vendedor) {
        this.email_vendedor = email_vendedor;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDesforpag() {
        return desforpag;
    }

    public void setDesforpag(String desforpag) {
        this.desforpag = desforpag;
    }

    public String getMonto_total() {
        return monto_total;
    }

    public void setMonto_total(String monto_total) {
        this.monto_total = monto_total;
    }

    public String getValor_igv() {
        return valor_igv;
    }

    public void setValor_igv(String valor_igv) {
        this.valor_igv = valor_igv;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getPeso_total() {
        return peso_total;
    }

    public void setPeso_total(String peso_total) {
        this.peso_total = peso_total;
    }

    public String getFecha_oc() {
        return fecha_oc;
    }

    public void setFecha_oc(String fecha_oc) {
        this.fecha_oc = fecha_oc;
    }

    public String getFecha_mxe() {
        return fecha_mxe;
    }

    public void setFecha_mxe(String fecha_mxe) {
        this.fecha_mxe = fecha_mxe;
    }

    public String getCodpro() {
        return codpro;
    }

    public void setCodpro(String codpro) {
        this.codpro = codpro;
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

    public String getDespro() {
        return despro;
    }

    public void setDespro(String despro) {
        this.despro = despro;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getPrecio_bruto() {
        return precio_bruto;
    }

    public void setPrecio_bruto(String precio_bruto) {
        this.precio_bruto = precio_bruto;
    }

    public String getPrecio_neto() {
        return precio_neto;
    }

    public void setPrecio_neto(String precio_neto) {
        this.precio_neto = precio_neto;
    }

    public double getPesoTotalProducto() {
        return pesoTotalProducto;
    }

    public void setPesoTotalProducto(double pesoTotalProducto) {
        this.pesoTotalProducto = pesoTotalProducto;
    }

    public double getPorcentaje_desc_extra() {
        return porcentaje_desc_extra;
    }

    public void setPorcentaje_desc_extra(double porcentaje_desc_extra) {
        this.porcentaje_desc_extra = porcentaje_desc_extra;
    }
}
