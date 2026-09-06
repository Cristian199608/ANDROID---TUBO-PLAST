package com.example.sm_tubo_plast.genesys.Retrofit.request.pedido.det;

public class RequestClientePedidoSAP {

        private String cod_cliente;
        private String codigo_punto_entrega;
        private String codigo_sucursal_cliente;
        private String codigo_obra;
        private String lista_precio;

        public RequestClientePedidoSAP() {
        }

    public RequestClientePedidoSAP(String cod_cliente, String codigo_punto_entrega, String codigo_sucursal_cliente, String codigo_obra, String lista_precio) {
        this.cod_cliente = cod_cliente;
        this.codigo_punto_entrega = codigo_punto_entrega;
        this.codigo_sucursal_cliente = codigo_sucursal_cliente;
        this.codigo_obra = codigo_obra;
        this.lista_precio = lista_precio;
    }

    public String getCod_cliente() {
        return cod_cliente;
    }

    public void setCod_cliente(String cod_cliente) {
        this.cod_cliente = cod_cliente;
    }

    public String getCodigo_punto_entrega() {
        return codigo_punto_entrega;
    }

    public void setCodigo_punto_entrega(String codigo_punto_entrega) {
        this.codigo_punto_entrega = codigo_punto_entrega;
    }

    public String getCodigo_sucursal_cliente() {
        return codigo_sucursal_cliente;
    }

    public void setCodigo_sucursal_cliente(String codigo_sucursal_cliente) {
        this.codigo_sucursal_cliente = codigo_sucursal_cliente;
    }

    public String getCodigo_obra() {
        return codigo_obra;
    }

    public void setCodigo_obra(String codigo_obra) {
        this.codigo_obra = codigo_obra;
    }

    public String getLista_precio() {
        return lista_precio;
    }

    public void setLista_precio(String lista_precio) {
        this.lista_precio = lista_precio;
    }
}