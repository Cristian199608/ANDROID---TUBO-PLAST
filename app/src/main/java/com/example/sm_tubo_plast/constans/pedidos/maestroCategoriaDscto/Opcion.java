package com.example.sm_tubo_plast.constans.pedidos.maestroCategoriaDscto;

public class Opcion {

    private String descripcion;
    private Condicion condicion;

    public Opcion(String descripcion, Condicion condicion) {
        this.descripcion = descripcion;
        this.condicion = condicion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Condicion getCondicion() {
        return condicion;
    }
}