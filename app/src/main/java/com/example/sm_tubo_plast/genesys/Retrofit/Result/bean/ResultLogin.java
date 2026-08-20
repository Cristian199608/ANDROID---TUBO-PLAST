package com.example.sm_tubo_plast.genesys.Retrofit.Result.bean;

public class ResultLogin {
    String token;
    Vendedorx vendedor;

    public String getToken() {
        return token;
    }

    public Vendedorx getVendedor() {
        return vendedor;
    }

    public class Vendedorx {
        String codigo;
        String nombre;

        public String getCodigo() {
            return codigo;
        }

        public String getNombre() {
            return nombre;
        }
    }
}
