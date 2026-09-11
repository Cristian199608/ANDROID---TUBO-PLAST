package com.example.sm_tubo_plast.genesys.Retrofit.Result.bean.ResultCoprobanteBytes;

import java.util.List;

public class ResultCoprobanteBytes {
    private boolean success;
    private String codigo;
    private String mensaje;
    private String nombre_archivo;
    private List<Integer> contenido_pdf;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getNombre_archivo() {
        return nombre_archivo;
    }

    public void setNombre_archivo(String nombre_archivo) {
        this.nombre_archivo = nombre_archivo;
    }

    public List<Integer> getContenido_pdf() {
        return contenido_pdf;
    }

    public void setContenido_pdf(List<Integer> contenido_pdf) {
        this.contenido_pdf = contenido_pdf;
    }
}