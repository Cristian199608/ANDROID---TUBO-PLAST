package com.example.sm_tubo_plast.genesys.Retrofit.request.pedido.det;

public class RequestAuditoriaPedidoSAP {

    private String cod_vendedor;
    private String usuario;
    private String version_app;
    private String latitud;
    private String longitud;

    public RequestAuditoriaPedidoSAP() {
    }

    public RequestAuditoriaPedidoSAP(String cod_vendedor,
                                     String usuario,
                                     String version_app,
                                     String latitud,
                                     String longitud) {

        this.cod_vendedor = cod_vendedor;
        this.usuario = usuario;
        this.version_app = version_app;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public String getCod_vendedor() {
        return cod_vendedor;
    }

    public void setCod_vendedor(String cod_vendedor) {
        this.cod_vendedor = cod_vendedor;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getVersion_app() {
        return version_app;
    }

    public void setVersion_app(String version_app) {
        this.version_app = version_app;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }
}