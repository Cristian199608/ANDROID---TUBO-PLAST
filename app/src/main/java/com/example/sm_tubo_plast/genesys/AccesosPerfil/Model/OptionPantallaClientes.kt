package com.example.sm_tubo_plast.genesys.AccesosPerfil.Model

import android.util.Log

class OptionPantallaClientes {
    var VerInformacionCliente: Boolean=false;
    var GeolocalizarCliente: Boolean=false;
    var Cotizacion: Boolean=false;
    var OdenPedido: Boolean=false;
    var EstadoDeCuenta: Boolean=false;
    var GestionarVisita: Boolean=false;
    var ProgramarVisita: Boolean=false;
    var MotivoNoVenta: Boolean=false;
    var Observacion: Boolean=false;
    var BajaDeCliente: Boolean=false;

    private var _password: String="false";
    var codigoNivel: String
        get() = _password.capitalize();
        set(valor) {
            _password=valor;
        }

    var passwordx: Boolean? = null
            get() {
                return field;
            }
            set(value) {
                Log.i("OPT", "SET VALUE")
                field=value
            }


}