package com.example.sm_tubo_plast.genesys.AccesosPerfil

import android.app.Activity
import android.content.Context
import com.example.sm_tubo_plast.genesys.AccesosPerfil.Model.OptionMenuPrinicipal
import com.example.sm_tubo_plast.genesys.AccesosPerfil.Model.OptionPantallaClientes
import com.example.sm_tubo_plast.genesys.DAO.DAO_Roles_accesos_app
import com.example.sm_tubo_plast.genesys.session.SessionManager

class AccesosOpciones {
    class OptionMenuPrincipal(context: Context?) {
        var daoRolesAccesosApp: DAO_Roles_accesos_app
        init {
            daoRolesAccesosApp = DAO_Roles_accesos_app(context!!)
            val prefer=SessionManager(context as Activity);
            daoRolesAccesosApp.setcodigoRol(prefer.codigoNivel!!);
        }
        fun accesoOptionMenuPrincipal(): OptionMenuPrinicipal {
            val menu = OptionMenuPrinicipal()

            menu.menuCliente                = daoRolesAccesosApp.getOpcionPermitido("MenuClientes", "%");
            menu.menuCotizacionAndPedido    = daoRolesAccesosApp.getOpcionPermitido("MenuCotizacionYPedido", "%");
            menu.menuAgenda                 = daoRolesAccesosApp.getOpcionPermitido("MenuAgenda", "%");
            menu.menuProducto               = daoRolesAccesosApp.getOpcionPermitido("MenuProductos", "%");
            menu.menuSeguimientoOp          = daoRolesAccesosApp.getOpcionPermitido("MenuSeguimientoOP", "%");
            menu.menuCuentasXCobrar         = daoRolesAccesosApp.getOpcionPermitido("MenuCuentaXCobrar", "%");
            menu.menuVentas                 = daoRolesAccesosApp.getOpcionPermitido("MenuVentas", "%");
            menu.menuEstadistica            = daoRolesAccesosApp.getOpcionPermitido("MenuEstadistica", "%");
            menu.menuConsultaFacturas       = daoRolesAccesosApp.getOpcionPermitido("MenuConsultaFacturas", "%");
            menu.menuReportes               = daoRolesAccesosApp.getOpcionPermitido("MenuReportes", "%");
            menu.menuCuotaVentas            = daoRolesAccesosApp.getOpcionPermitido("MenuCuotaVentas", "%");
            menu.menuSincronizar            = daoRolesAccesosApp.getOpcionPermitido("MenuSincronizar", "%");
            return menu;
        }
    }

    class PantallaClientes(context: Context?) {
        var daoRolesAccesosApp: DAO_Roles_accesos_app

        init {
            daoRolesAccesosApp = DAO_Roles_accesos_app(context!!)
            val prefer=SessionManager(context as Activity);
            daoRolesAccesosApp.setcodigoRol(prefer.codigoNivel!!);
        }
        fun option(): OptionPantallaClientes {
            val option = OptionPantallaClientes()

            option.VerInformacionCliente    = daoRolesAccesosApp.getOpcionPermitido("MenuClientes","VerInformacionCliente");
            option.GeolocalizarCliente      = daoRolesAccesosApp.getOpcionPermitido("MenuClientes","GeolocalizarCliente");
            option.Cotizacion               = daoRolesAccesosApp.getOpcionPermitido("MenuClientes","Cotizacion");
            option.OdenPedido               = daoRolesAccesosApp.getOpcionPermitido("MenuClientes","OdenPedido");
            option.EstadoDeCuenta           = daoRolesAccesosApp.getOpcionPermitido("MenuClientes","EstadoDeCuenta");
            option.GestionarVisita          = daoRolesAccesosApp.getOpcionPermitido("MenuClientes","GestionarVisita");
            option.ProgramarVisita          = option.GestionarVisita;
            option.MotivoNoVenta            = daoRolesAccesosApp.getOpcionPermitido("MenuClientes","MotivoNoVenta");
            option.Observacion              = daoRolesAccesosApp.getOpcionPermitido("MenuClientes","Observacion");
            option.BajaDeCliente            = daoRolesAccesosApp.getOpcionPermitido("MenuClientes","BajaDeCliente");
            option.AutoAsignarClienteCartera= daoRolesAccesosApp.getOpcionPermitido("MenuClientes","AutoAsignarClienteCartera");
            return option;
        }
    }

}