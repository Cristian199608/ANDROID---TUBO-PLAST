package com.example.sm_tubo_plast.genesys.BEAN;

public class PedidoDetalleDescuento {
    public static final String TIPO_DSCTO_CATEGORIA_PRECIO = "categoria_precio";
    public static final String TIPO_DSCTO_PRONTO_PAGO_CONTADO = "pronto_pago_contado";
    public static final String TIPO_DSCTO_BONIFICACION = "bonificacion";

    private String oc_numero;
    private String codpro;
    private int item;
    private double pcjt_desc;
    private double monto_desc;
    private String tipo_desc;

    public PedidoDetalleDescuento(){

    }
    public PedidoDetalleDescuento(
            String ocNumero,
            String codproValue,
            int itemValue,
            double pcjtDesc,
            double montoDesc,
            String tipoDesc
    ) {
        this.oc_numero = ocNumero;
        this.codpro = codproValue;
        this.item = itemValue;
        this.pcjt_desc = pcjtDesc;
        this.monto_desc = montoDesc;
        this.tipo_desc = tipoDesc;
    }

    public String getOc_numero() {
        return oc_numero;
    }

    public String getCodpro() {
        return codpro;
    }

    public int getItem() {
        return item;
    }

    public double getPcjt_desc() {
        return pcjt_desc;
    }

    public double getMonto_desc() {
        return monto_desc;
    }

    public String getTipo_desc() {
        return tipo_desc;
    }
}
