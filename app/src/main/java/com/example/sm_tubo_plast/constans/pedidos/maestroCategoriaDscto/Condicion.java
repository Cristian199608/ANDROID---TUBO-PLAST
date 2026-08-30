package com.example.sm_tubo_plast.constans.pedidos.maestroCategoriaDscto;

import java.util.ArrayList;
import java.util.List;

public class Condicion {

    private List<String> marca_inc;
    private List<String> marca_not;
    private Condicion forma_pago;
    private double dsct_pct;

    public Condicion(List<String> marca_inc,
                     List<String> marca_not,
                     double dsct_pct,
                     Condicion forma_pago) {
        this.marca_inc = marca_inc;
        this.marca_not = marca_not;
        this.dsct_pct = dsct_pct;
        this.forma_pago = forma_pago;
    }

    public List<String> getMarca_inc() {
        return marca_inc;
    }

    public List<String> getMarca_not() {
        return marca_not;
    }

    public double getDsct_pct() {
        return dsct_pct;
    }

    public Condicion getForma_pago() {
        return forma_pago;
    }
}