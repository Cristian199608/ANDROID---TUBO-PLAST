package com.example.sm_tubo_plast.genesys.fuerza_ventas.cliente.AnticiposCliente.beanView;

public class AnticiposClienteUtil {

    private String oc_numero;
    private String serie_doc;
    private String numero_doc;
    private Double monto;
    private String obs;

    private boolean seleccionado;
    private double montoSeleccionado;

    public AnticiposClienteUtil() {
    }

    public AnticiposClienteUtil(String oc_numero, String serie_doc, String numero_doc, Double monto, String obs) {
        this.oc_numero = oc_numero;
        this.serie_doc = serie_doc;
        this.numero_doc = numero_doc;
        this.monto = monto;
        this.obs = obs;
        this.seleccionado = false;
        this.montoSeleccionado = monto != null ? monto : 0.00;
    }

    public String getOc_numero() {
        return oc_numero;
    }

    public void setOc_numero(String oc_numero) {
        this.oc_numero = oc_numero;
    }

    public String getSerie_doc() {
        return serie_doc;
    }

    public void setSerie_doc(String serie_doc) {
        this.serie_doc = serie_doc;
    }

    public String getNumero_doc() {
        return numero_doc;
    }

    public void setNumero_doc(String numero_doc) {
        this.numero_doc = numero_doc;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public boolean isSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
    }

    public double getMontoSeleccionado() {
        return montoSeleccionado;
    }

    public void setMontoSeleccionado(double montoSeleccionado) {
        this.montoSeleccionado = montoSeleccionado;
    }
}
