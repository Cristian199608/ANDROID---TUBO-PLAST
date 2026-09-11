package com.example.sm_tubo_plast.genesys.Retrofit.request.pedido.util;

import com.example.sm_tubo_plast.genesys.BEAN.PedidoDetalleDescuento;
import com.example.sm_tubo_plast.genesys.Retrofit.request.pedido.RequestPedidoSAP;
import com.example.sm_tubo_plast.genesys.Retrofit.request.pedido.det.RequestAuditoriaPedidoSAP;
import com.example.sm_tubo_plast.genesys.Retrofit.request.pedido.det.RequestClientePedidoSAP;
import com.example.sm_tubo_plast.genesys.Retrofit.request.pedido.det.RequestComercialPedidoSAP;
import com.example.sm_tubo_plast.genesys.Retrofit.request.pedido.det.RequestCondicionPagoPedidoSAP;
import com.example.sm_tubo_plast.genesys.Retrofit.request.pedido.det.RequestContactoPedidoSAP;
import com.example.sm_tubo_plast.genesys.Retrofit.request.pedido.det.RequestDetallePedidoSAP;
import com.example.sm_tubo_plast.genesys.Retrofit.request.pedido.det.RequestEntregaPedidoSAP;
import com.example.sm_tubo_plast.genesys.Retrofit.request.pedido.det.RequestObservacionesPedidoSAP;
import com.example.sm_tubo_plast.genesys.Retrofit.request.pedido.det.RequestTotalesPedidoSAP;
import com.example.sm_tubo_plast.genesys.Retrofit.request.pedido.det.RequestTransportePedidoSAP;
import com.example.sm_tubo_plast.genesys.Retrofit.request.pedido.det.RequetDescuentoPedidoSAP;
import com.example.sm_tubo_plast.genesys.datatypes.DBPedido_Detalle;
import com.example.sm_tubo_plast.genesys.datatypes.DB_ObjPedido;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.PedidosActivity;
import com.example.sm_tubo_plast.genesys.util.VARIABLES;

import java.nio.charset.CoderMalfunctionError;
import java.util.ArrayList;

public class PedidoAppConvertTo_PedidoSAP {
    private String[] _separadorData(String texto){
        String [] data=texto.split(VARIABLES.SEPARADOR_OBSERVACION, -1);
        return data;
    }
    public RequestPedidoSAP generarTramaPedidoToSAP(DB_ObjPedido ped){
        String tipoPedidoSAP= ped.getTipoRegistro();
        if(ped.getTipoRegistro().equalsIgnoreCase(PedidosActivity.TIPO_PEDIDO))
            tipoPedidoSAP="PED";
        else if(ped.getTipoRegistro().equalsIgnoreCase(PedidosActivity.TIPO_COTIZACION))
            tipoPedidoSAP="PRO";

        RequestPedidoSAP data=new RequestPedidoSAP();
        data.setVersion_api("1.0");
        data.setSistema_origen("SAEMOVIL");
        data.setSistema_origen("SAEMOVIL");
        data.setOc_numero(ped.getOc_numero());
        data.setTipo_registro(tipoPedidoSAP);
        data.setTipo_documento(ped.getTipoDocumento().equals("01")?"FACT":"BOL");
        data.setFecha_pedido(VARIABLES.GetFechaStringFrom_dd_mm_yyyy_hhmmssTO_yyyy_mm_dd_hhmmss(ped.getFecha_oc()));
        data.setEstado("NUEVO");

        data.setCliente(getParserDataCliente(ped));
        data.setEntrega(getParserDataEntrega(ped));
        data.setTransporte(getParserDataTransporte(ped));
        data.setContacto(getParserDataContacto(ped));
        data.setComercial(getParserDataComercial(ped));

        data.setDias_vigencia(ped.getDiasVigencia().trim().length()>0?Integer.parseInt(ped.getDiasVigencia()):0);
        data.setNumero_orden_compra(ped.getNumeroOrdenCompra());
        data.setNumero_letras(0);
        data.setAplica_pedido_anticipo(ped.getFlagPedido_Anticipo().equals("1"));
        data.setAnticipos_aplicados(new ArrayList<>());
        data.setAplica_descuento(ped.getDsctoBonificacion()>0.0 || ped.getDsctProntoPagoContado()>0.0);//TODO MEJORAR
        data.setAplica_dsc_pronto_pago(ped.getDsctProntoPagoContado()>0.0);
        data.setAplica_dsc_siguiente_categoria(ped.getIsAplica_dsc_sig_categoria()==1);
        data.setAplica_nota_credito(false);
        data.setNota_credito_aplicado(new ArrayList<>());

        data.setTotales(getParserDataTotales(ped));
        data.setObservaciones(getParserDataObservacions(ped));
        data.setAuditoria(getParserDataAuditoria(ped));
        data.setDetalles(getParserDataDetalle(ped));

        return data;
    }
    private RequestClientePedidoSAP getParserDataCliente(DB_ObjPedido ped){
        RequestClientePedidoSAP cli=new RequestClientePedidoSAP();
        cli.setCod_cliente(ped.getCod_cli());
        cli.setCodigo_sucursal_cliente(ped.getCodigoPuntoEntrega());
        cli.setCodigo_punto_entrega(ped.getFlagDespacho());
        cli.setCodigo_obra(ped.getCodigoObra());
        cli.setLista_precio(ped.getCategoriaClienteVenta());
        return cli;
    }

    private RequestEntregaPedidoSAP getParserDataEntrega(DB_ObjPedido ped){
        RequestEntregaPedidoSAP entr= new RequestEntregaPedidoSAP();
        entr.setFecha_entrega(VARIABLES.GetFechaStringFrom_dd_mm_yyyy_TO_yyyy_mm_dd(ped.getFecha_mxe()));
        //entr.setCodigo_punto_entrega(ped.getFlagDespacho());
        entr.setCodigo_turno(ped.getCodTurno().equals("01")?"M":"T");
        entr.setCodigo_prioridad(ped.getCodigoPrioridad());
        entr.setCodigo_tipo_despacho(ped.getCodigoTipoDespacho());
        entr.setAplica_cliente_recoge(ped.getFlagDespacho().toLowerCase().contains("cliente recoge"));
        entr.setAplica_embalaje(ped.getFlagEmbalaje().equals("1"));
        entr.setAplica_servicio_instalacion(ped.getIsAplicaInstalacion()==1);
        entr.setCodigo_almacen(ped.getCodigoAlmacen());
        return entr;
    }

    private RequestTransportePedidoSAP getParserDataTransporte(DB_ObjPedido ped){
        RequestTransportePedidoSAP tra= new RequestTransportePedidoSAP();
        tra.setCodigo_transporte(ped.getCodigoTransportista());
        tra.setCodigo_sucursal_transporte(ped.getSucursalTransportista());
        tra.setDireccion_agencia_transporte(ped.getDireccionTransportista());
        tra.setDireccion_entrega_transporte(ped.getDireccionTransportista());
        if(ped.getUbigeoTransportista().contains(VARIABLES.SEPARADOR_OBSERVACION)){
            String[] ubigeos= _separadorData(ped.getUbigeoTransportista());
            tra.setDistrito(ubigeos[0]);
            tra.setProvincia(ubigeos[1]);
            tra.setDepartamento(ubigeos[2]);
        }else{
            tra.setDistrito("");
            tra.setProvincia("");
            tra.setDepartamento("");
        }
        return tra;
    }

    private RequestContactoPedidoSAP getParserDataContacto(DB_ObjPedido ped){
        RequestContactoPedidoSAP contact= new RequestContactoPedidoSAP();
        String[] observacions2= _separadorData(ped.getObservacion2());
        String[] observacions= _separadorData(ped.getObserv());
        contact.setNombre_proyecto(observacions2[2]);//index 2 es nombre proyecto
        contact.setNombre_contacto(observacions[0]);
        contact.setTelefono_contacto(observacions[1]);
        return contact;
    }

    private RequestComercialPedidoSAP getParserDataComercial(DB_ObjPedido ped){
        RequestComercialPedidoSAP comer= new RequestComercialPedidoSAP();
        comer.setMoneda(ped.getMoneda().equals("1")?"PEN":"USD");
        comer.setTipo_cambio(ped.getMoneda().equals("1")?"1":"-1");
        comer.setCondicion_pago(
                new RequestCondicionPagoPedidoSAP(
                        ped.getCond_pago(),
                        ""+ped.getDescFormaPago()
                )
        );
        return comer;
    }

    private RequestTotalesPedidoSAP getParserDataTotales(DB_ObjPedido ped){
        RequestTotalesPedidoSAP comer= new RequestTotalesPedidoSAP();
        comer.setSubtotal(Double.parseDouble(ped.getSubtotal()));
        comer.setIgv(Double.parseDouble(ped.getValor_igv()));
        comer.setDescuento_total(ped.getDsctoBonificacion());
        comer.setTotal(Double.parseDouble(ped.getMonto_total()));
        comer.setPeso_total(Double.parseDouble(ped.getPeso_total()));
        comer.setVolumen_total(ped.getVolumenTotal());
        return comer;
    }

    private RequestObservacionesPedidoSAP getParserDataObservacions(DB_ObjPedido ped){
        RequestObservacionesPedidoSAP obs=new RequestObservacionesPedidoSAP();
        obs.setPedido(ped.getObservacion3());
        obs.setDescuento("");
        obs.setTipo_producto("");
        obs.setAdicional("");
        obs.setDocumento("");
        obs.setDespacho(ped.getObsDespacho());
        return obs;
    }
    private RequestAuditoriaPedidoSAP getParserDataAuditoria(DB_ObjPedido ped){
        RequestAuditoriaPedidoSAP aud=new RequestAuditoriaPedidoSAP();
        aud.setCod_vendedor(ped.getCod_emp());
        aud.setUsuario(ped.getUsername());
        aud.setVersion_app(ped.getVersionApp());
        aud.setLatitud(ped.getLatitud());
        aud.setLongitud(ped.getLongitud());
        return aud;
    }

    private ArrayList<RequestDetallePedidoSAP> getParserDataDetalle(DB_ObjPedido ped){
        ArrayList<RequestDetallePedidoSAP> listaDet=new ArrayList<>();
        RequestDetallePedidoSAP aud=null;
        for (int i = 0; i < ped.getDetalles().size(); i++) {
            aud=new RequestDetallePedidoSAP();
            DBPedido_Detalle det=ped.getDetalles().get(i);
            if(det.getFlagStockValido()==0) continue;
            //----------------solo stock valido-------------------------------------------------------------------------------
            aud.setItem(""+det.getItem());
            aud.setCod_producto(det.getCip());
            aud.setDescripcion_producto(det.getDespro());
            aud.setCantidad(det.getCantidad());
            aud.setUnidad_medida(det.getUnidad_medida());
            aud.setPrecio_lista(Double.parseDouble(det.getPrecioLista()));
            aud.setPrecio_unitario(Double.parseDouble(det.getPrecio_bruto()));
            aud.setPorcentaje_descuento(det.getPorcentaje_desc());
            aud.setImporte_descuento(Double.parseDouble(det.getDescuento()));

            ArrayList<RequetDescuentoPedidoSAP> lisDscto=new ArrayList<>();
            for (int i1 = 0; i1 < ped.getListaPedido_detalle_descuento().size(); i1++) {
                PedidoDetalleDescuento dscto= ped.getListaPedido_detalle_descuento().get(i1);
                if(dscto.getItem()==Integer.parseInt(aud.getItem())){
                    lisDscto.add(new RequetDescuentoPedidoSAP(
                            dscto.getTipo_desc(),
                            dscto.getPcjt_desc(),
                            dscto.getMonto_desc()
                    ));
                }
            }
            aud.setDescuentos(lisDscto);
            aud.setSubtotal(det.getTipo_producto().equals("V")?Double.parseDouble(det.getPrecio_neto()):0.0);
            aud.setTipo_afectacion_igv(det.getTipo_producto().equals("V")?"IGV_18":"IGV_EXE");
            aud.setPercepcion(0);
            aud.setTipo_producto(!det.getTipo_producto().equals("V")?"B":"V");
            aud.setSecuencia_promocion(det.getSec_promo().trim().length()>0?det.getSec_promo():"0");
            aud.setItem_origen_promocion(""+det.getItem_promo());
            aud.setCodigo_almacen(ped.getCodigoAlmacen());
            aud.setPeso_unitario(det.getPeso_unitario());
            aud.setPeso_total(Double.parseDouble(det.getPeso_bruto()));
            aud.setVolumen_unitario(det.getVolumen_unitario());
            aud.setVolumen_total(det.getVolumen_total());
            listaDet.add(aud);
        }
        return listaDet;
    }


}
