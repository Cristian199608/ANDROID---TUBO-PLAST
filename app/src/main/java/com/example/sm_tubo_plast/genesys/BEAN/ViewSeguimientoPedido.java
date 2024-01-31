package com.example.sm_tubo_plast.genesys.BEAN;

public class ViewSeguimientoPedido {

    private  String coddoc;
    private  double total;
    private  double saldo;
    private  String num_op;
    private  String moneda;
    private  String fecha_rect;
    private  String fecha_autorizado;
    private  String orden_compra;
    private  String codcli;
    private  String nomcli;
    private  double monto_total_entregado;
    private  int cant_guias;
    private  String primer_entrega;
    private  String ultima_entrega;
    private  double monto_grop;
    private  String dias;

    public String getCoddoc() {
        return coddoc;
    }

    public void setCoddoc(String coddoc) {
        this.coddoc = coddoc;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public String getNum_op() {
        return num_op;
    }

    public void setNum_op(String num_op) {
        this.num_op = num_op;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getFecha_rect() {
        return fecha_rect;
    }

    public void setFecha_rect(String fecha_rect) {
        this.fecha_rect = fecha_rect;
    }

    public String getFecha_autorizado() {
        return fecha_autorizado;
    }

    public void setFecha_autorizado(String fecha_autorizado) {
        this.fecha_autorizado = fecha_autorizado;
    }

    public String getOrden_compra() {
        return orden_compra;
    }

    public void setOrden_compra(String orden_compra) {
        this.orden_compra = orden_compra;
    }

    public String getCodcli() {
        return codcli;
    }

    public void setCodcli(String codcli) {
        this.codcli = codcli;
    }

    public double getMonto_total_entregado() {
        return monto_total_entregado;
    }

    public void setMonto_total_entregado(double monto_total_entregado) {
        this.monto_total_entregado = monto_total_entregado;
    }

    public int getCant_guias() {
        return cant_guias;
    }

    public void setCant_guias(int cant_guias) {
        this.cant_guias = cant_guias;
    }

    public String getPrimer_entrega() {
        return primer_entrega;
    }

    public void setPrimer_entrega(String primer_entrega) {
        this.primer_entrega = primer_entrega;
    }

    public String getUltima_entrega() {
        return ultima_entrega;
    }

    public void setUltima_entrega(String ultima_entrega) {
        this.ultima_entrega = ultima_entrega;
    }

    public double getMonto_grop() {
        return monto_grop;
    }

    public void setMonto_grop(double monto_grop) {
        this.monto_grop = monto_grop;
    }

    public String getDias() {
        return dias;
    }

    public void setDias(String dias) {
        this.dias = dias;
    }

    public String getNomcli() {
        return nomcli;
    }

    public void setNomcli(String nomcli) {
        this.nomcli = nomcli;
    }
}
