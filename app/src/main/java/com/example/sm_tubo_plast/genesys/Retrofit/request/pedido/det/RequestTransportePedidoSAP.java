package com.example.sm_tubo_plast.genesys.Retrofit.request.pedido.det;

public class RequestTransportePedidoSAP {

        private String codigo_transporte;
        private String codigo_sucursal_transporte;
        private String direccion_agencia_transporte;
        private String direccion_entrega_transporte;
        private String distrito;
        private String provincia;
        private String departamento;

        public RequestTransportePedidoSAP() {
        }

        public RequestTransportePedidoSAP(String codigo_transporte,
                          String codigo_sucursal_transporte,
                          String direccion_agencia_transporte,
                          String direccion_entrega_transporte,
                          String distrito,
                          String provincia,
                          String departamento) {

            this.codigo_transporte = codigo_transporte;
            this.codigo_sucursal_transporte = codigo_sucursal_transporte;
            this.direccion_agencia_transporte = direccion_agencia_transporte;
            this.direccion_entrega_transporte = direccion_entrega_transporte;
            this.distrito = distrito;
            this.provincia = provincia;
            this.departamento = departamento;
        }

    public String getCodigo_transporte() {
        return codigo_transporte;
    }

    public void setCodigo_transporte(String codigo_transporte) {
        this.codigo_transporte = codigo_transporte;
    }

    public String getCodigo_sucursal_transporte() {
        return codigo_sucursal_transporte;
    }

    public void setCodigo_sucursal_transporte(String codigo_sucursal_transporte) {
        this.codigo_sucursal_transporte = codigo_sucursal_transporte;
    }

    public String getDireccion_agencia_transporte() {
        return direccion_agencia_transporte;
    }

    public void setDireccion_agencia_transporte(String direccion_agencia_transporte) {
        this.direccion_agencia_transporte = direccion_agencia_transporte;
    }

    public String getDireccion_entrega_transporte() {
        return direccion_entrega_transporte;
    }

    public void setDireccion_entrega_transporte(String direccion_entrega_transporte) {
        this.direccion_entrega_transporte = direccion_entrega_transporte;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }
}