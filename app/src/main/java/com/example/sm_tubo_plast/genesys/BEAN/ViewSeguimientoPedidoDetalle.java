package com.example.sm_tubo_plast.genesys.BEAN;

public class ViewSeguimientoPedidoDetalle {

    private String codcli;
    private String nombres;
    private String moneda;
    private String movimiento;
    private String codigo_op;
    private String numero_op;
    private String fecha_apertura;
    private double monto_total;
    private double monto_saldo;
    private String fecha_entrega;
    private String item;
    private int cantidad_comprado;
    private int cantidad_salida;
    private String producto;
    private double stock_actual;
    private double stock_separado;
    private int cantidad_entregado;

    public String getCodcli() {
        return codcli;
    }

    public void setCodcli(String codcli) {
        this.codcli = codcli;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(String movimiento) {
        this.movimiento = movimiento;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getCodigo_op() {
        return codigo_op;
    }

    public void setCodigo_op(String codigo_op) {
        this.codigo_op = codigo_op;
    }

    public String getNumero_op() {
        return numero_op;
    }

    public void setNumero_op(String numero_op) {
        this.numero_op = numero_op;
    }

    public String getFecha_apertura() {
        return fecha_apertura;
    }

    public void setFecha_apertura(String fecha_apertura) {
        this.fecha_apertura = fecha_apertura;
    }

    public double getMonto_total() {
        return monto_total;
    }

    public void setMonto_total(double monto_total) {
        this.monto_total = monto_total;
    }

    public double getMonto_saldo() {
        return monto_saldo;
    }

    public void setMonto_saldo(double monto_saldo) {
        this.monto_saldo = monto_saldo;
    }

    public String getFecha_entrega() {
        return fecha_entrega;
    }

    public void setFecha_entrega(String fecha_entrega) {
        this.fecha_entrega = fecha_entrega;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getCantidad_comprado() {
        return cantidad_comprado;
    }

    public void setCantidad_comprado(int cantidad_comprado) {
        this.cantidad_comprado = cantidad_comprado;
    }

    public int getCantidad_salida() {
        return cantidad_salida;
    }

    public void setCantidad_salida(int cantidad_salida) {
        this.cantidad_salida = cantidad_salida;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public double getStock_actual() {
        return stock_actual;
    }

    public void setStock_actual(double stock_actual) {
        this.stock_actual = stock_actual;
    }

    public double getStock_separado() {
        return stock_separado;
    }

    public void setStock_separado(double stock_separado) {
        this.stock_separado = stock_separado;
    }

    public int getCantidad_entregado() {
        return cantidad_entregado;
    }

    public void setCantidad_entregado(int cantidad_entregado) {
        this.cantidad_entregado = cantidad_entregado;
    }
}
