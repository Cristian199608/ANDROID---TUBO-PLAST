package com.example.sm_tubo_plast.constans.pedidos.maestroCategoriaDscto;

import java.util.ArrayList;
import java.util.List;

public class Condicion {

    private List<String> marca_inc;
    private List<String> marca_not;
    private double dsct_pct;

    public Condicion(List<String> marca_inc,
                     List<String> marca_not,
                     double dsct_pct) {
        this.marca_inc = marca_inc;
        this.marca_not = marca_not;
        this.dsct_pct = dsct_pct;
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
}