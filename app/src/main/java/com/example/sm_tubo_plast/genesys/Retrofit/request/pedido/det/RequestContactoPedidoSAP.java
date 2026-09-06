package com.example.sm_tubo_plast.genesys.Retrofit.request.pedido.det;

public class RequestContactoPedidoSAP {

    private String nombre_proyecto;
    private String nombre_contacto;
    private String telefono_contacto;

    public RequestContactoPedidoSAP() {
    }

    public RequestContactoPedidoSAP(String nombre_proyecto,
                                    String nombre_contacto,
                                    String telefono_contacto) {

        this.nombre_proyecto = nombre_proyecto;
        this.nombre_contacto = nombre_contacto;
        this.telefono_contacto = telefono_contacto;
    }

    public String getNombre_proyecto() {
        return nombre_proyecto;
    }

    public void setNombre_proyecto(String nombre_proyecto) {
        this.nombre_proyecto = nombre_proyecto;
    }

    public String getNombre_contacto() {
        return nombre_contacto;
    }

    public void setNombre_contacto(String nombre_contacto) {
        this.nombre_contacto = nombre_contacto;
    }

    public String getTelefono_contacto() {
        return telefono_contacto;
    }

    public void setTelefono_contacto(String telefono_contacto) {
        this.telefono_contacto = telefono_contacto;
    }
}