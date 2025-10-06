package com.example.sm_tubo_plast.genesys.datatypes;

import com.example.sm_tubo_plast.genesys.BEAN.Pedido_detalle2;
import com.example.sm_tubo_plast.genesys.BEAN.San_Visitas;

import java.util.ArrayList;

public class DB_ObjPedido{
	
	private String oc_numero;
	private String sitio_enfa;
	private String monto_total;
	private String percepcion_total;
	private String valor_igv;
	private String moneda;
	private String fecha_oc;
	private String fecha_mxe;
	private String cond_pago;
	private String cod_cli;
	private String cod_emp;
	private String estado;
	private String username;
	private String ruta;
	private String observ;
	private int cod_noventa;
	private String peso_total;
	private String flag;
	private String latitud;
	private String longitud;
	private String codigo_familiar;
	private String DT_PEDI_FECHASERVIDOR;
	private String totalSujetoPercepcion;
	private ArrayList<DBPedido_Detalle> detalles;
	private ArrayList<Pedido_detalle2> pedidoDetalle2;
	private ArrayList<DB_RegistroBonificaciones> bonificaciones;
	
	private String numeroOrdenCompra;
	
	private String codigoPrioridad;
	private String codigoSucursal;
	private String codigoPuntoEntrega;
	private String codigoTipoDespacho;
	private String flagEmbalaje;
	private String flagPedido_Anticipo;
	private String codigoTransportista;
	private String codigoAlmacen;
	private String observacion2;
	private String observacion3;
	private String observacion4;
	private String observacionDescuento;
	private String observacionTipoProducto;
	private String flagDescuento;
	private String codigoObra;
	private String flagDespacho;
	private String docAdicional;
	private String subtotal;
	private String tipoDocumento;
	
	private String tipoRegistro;
	private String diasVigencia;
	private String pedidoAnterior;
	private String CodTurno;
	private String nroletra;
	private double dsctoBonificacion;
	private ArrayList<San_Visitas> san_visitas;

	
	public String getNroletra() {
		return nroletra;
	}
	public void setNroletra(String nroletra) {
		this.nroletra = nroletra;
	}
	
	
	public String getObservacion4() {
		return observacion4;
	}
	public void setObservacion4(String observacion4) {
		this.observacion4 = observacion4;
	}
	public ArrayList<DB_RegistroBonificaciones> getBonificaciones() {
		return bonificaciones;
	}
	public void setBonificaciones(
			ArrayList<DB_RegistroBonificaciones> bonificaciones) {
		this.bonificaciones = bonificaciones;
	}
	public String getTipoRegistro() {
		return tipoRegistro;
	}
	public void setTipoRegistro(String tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}
	public String getDiasVigencia() {
		return diasVigencia;
	}
	public void setDiasVigencia(String diasVigencia) {
		this.diasVigencia = diasVigencia;
	}
	public String getTipoDocumento() {
		return tipoDocumento;
	}
	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	public String getFlagDescuento() {
		return flagDescuento;
	}
	public void setFlagDescuento(String flagDescuento) {
		this.flagDescuento = flagDescuento;
	}
	public String getCodigoObra() {
		return codigoObra;
	}
	public void setCodigoObra(String codigoObra) {
		this.codigoObra = codigoObra;
	}
	public String getFlagDespacho() {
		return flagDespacho;
	}
	public void setFlagDespacho(String flagDespacho) {
		this.flagDespacho = flagDespacho;
	}
	public String getNumeroOrdenCompra() {
		return numeroOrdenCompra;
	}
	public void setNumeroOrdenCompra(String numeroOrdenCompra) {
		this.numeroOrdenCompra = numeroOrdenCompra;
	}

	public String getCodigoPrioridad() {
		return codigoPrioridad;
	}
	public void setCodigoPrioridad(String codigoPrioridad) {
		this.codigoPrioridad = codigoPrioridad;
	}
	public String getCodigoSucursal() {
		return codigoSucursal;
	}
	public void setCodigoSucursal(String codigoSucursal) {
		this.codigoSucursal = codigoSucursal;
	}
	public String getCodigoPuntoEntrega() {
		return codigoPuntoEntrega;
	}
	public void setCodigoPuntoEntrega(String codigoPuntoEntrega) {
		this.codigoPuntoEntrega = codigoPuntoEntrega;
	}
	public String getCodigoTipoDespacho() {
		return codigoTipoDespacho;
	}
	public void setCodigoTipoDespacho(String codigoTipoDespacho) {
		this.codigoTipoDespacho = codigoTipoDespacho;
	}
	public String getFlagEmbalaje() {
		return flagEmbalaje;
	}
	public void setFlagEmbalaje(String flagEmbalaje) {
		this.flagEmbalaje = flagEmbalaje;
	}
	public String getFlagPedido_Anticipo() {
		return flagPedido_Anticipo;
	}
	public void setFlagPedido_Anticipo(String flagPedido_Anticipo) {
		this.flagPedido_Anticipo = flagPedido_Anticipo;
	}
	public String getCodigoTransportista() {
		return codigoTransportista;
	}
	public void setCodigoTransportista(String codigoTransportista) {
		this.codigoTransportista = codigoTransportista;
	}
	public String getCodigoAlmacen() {
		return codigoAlmacen;
	}
	public void setCodigoAlmacen(String codigoAlmacen) {
		this.codigoAlmacen = codigoAlmacen;
	}
	public String getObservacion2() {
		return observacion2;
	}
	public void setObservacion2(String observacion2) {
		this.observacion2 = observacion2;
	}
	public String getObservacion3() {
		return observacion3;
	}
	public void setObservacion3(String observacion3) {
		this.observacion3 = observacion3;
	}
	public String getObservacionDescuento() {
		return observacionDescuento;
	}
	public void setObservacionDescuento(String observacionDescuento) {
		this.observacionDescuento = observacionDescuento;
	}
	public String getObservacionTipoProducto() {
		return observacionTipoProducto;
	}
	public void setObservacionTipoProducto(String observacionTipoProducto) {
		this.observacionTipoProducto = observacionTipoProducto;
	}
	public String getTotalSujetoPercepcion() {
		return totalSujetoPercepcion;
	}
	public void setTotalSujetoPercepcion(String totalSujetoPercepcion) {
		this.totalSujetoPercepcion = totalSujetoPercepcion;
	}
	public ArrayList<DBPedido_Detalle> getDetalles(){
		return detalles;
	}
	public void setDetalles(ArrayList<DBPedido_Detalle> detalles){
		this.detalles = detalles;
	}
	public String getDT_PEDI_FECHASERVIDOR() {
		return DT_PEDI_FECHASERVIDOR;
	}
	public void setDT_PEDI_FECHASERVIDOR(String dT_PEDI_FECHASERVIDOR) {
		DT_PEDI_FECHASERVIDOR = dT_PEDI_FECHASERVIDOR;
	}
	public String getCodigo_familiar() {
		return codigo_familiar;
	}
	public void setCodigo_familiar(String codigo_familiar) {
		this.codigo_familiar = codigo_familiar;
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
	public String getPeso_total() {
		return peso_total;
	}
	public void setPeso_total(String peso_total) {
		this.peso_total = peso_total;
	}
	public String getOc_numero() {
		return oc_numero;
	}
	public void setOc_numero(String oc_numero) {
		this.oc_numero = oc_numero;
	}
	public String getSitio_enfa() {
		return sitio_enfa;
	}
	public void setSitio_enfa(String sitio_enfa) {
		this.sitio_enfa = sitio_enfa;
	}
	public String getMonto_total() {
		return monto_total;
	}
	public void setMonto_total(String monto_total) {
		this.monto_total = monto_total;
	}
	public String getValor_igv() {
		return valor_igv;
	}
	public void setValor_igv(String valor_igv) {
		this.valor_igv = valor_igv;
	}
	public String getMoneda() {
		return moneda;
	}
	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}
	public String getFecha_oc() {
		return fecha_oc;
	}
	public void setFecha_oc(String fecha_oc) {
		this.fecha_oc = fecha_oc;
	}
	public String getFecha_mxe() {
		return fecha_mxe;
	}
	public void setFecha_mxe(String fecha_mxe) {
		this.fecha_mxe = fecha_mxe;
	}
	public String getCond_pago() {
		return cond_pago;
	}
	public void setCond_pago(String cond_pago) {
		this.cond_pago = cond_pago;
	}
	public String getCod_cli() {
		return cod_cli;
	}
	public void setCod_cli(String cod_cli) {
		this.cod_cli = cod_cli;
	}
	public String getCod_emp() {
		return cod_emp;
	}
	public void setCod_emp(String cod_emp) {
		this.cod_emp = cod_emp;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getRuta() {
		return ruta;
	}
	public void setRuta(String ruta) {
		this.ruta = ruta;
	}
	public String getObserv() {
		return observ;
	}
	public void setObserv(String observ) {
		this.observ = observ;
	}
	public int getCod_noventa() {
		return cod_noventa;
	}
	public void setCod_noventa(int cod_noventa) {
		this.cod_noventa = cod_noventa;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getPercepcion_total() {
		return percepcion_total;
	}
	public void setPercepcion_total(String percepcion_total) {
		this.percepcion_total = percepcion_total;
	}

	public ArrayList<San_Visitas> getSan_visitas() {
		return san_visitas;
	}

	public void setSan_visitas(ArrayList<San_Visitas> san_visitas) {
		this.san_visitas = san_visitas;
	}

	//Constructor
	public DB_ObjPedido(){
		this.oc_numero="";
		this.sitio_enfa="";
		this.monto_total="0.0";
		this.valor_igv="0.0";
		this.moneda="";
		this.fecha_oc="";
		this.fecha_mxe="";
		this.cond_pago="";
		this.cod_cli="";
		this.cod_emp="";
		this.estado="";
		this.username="";
		this.ruta="";
		this.observ="";
		this.cod_noventa=0;
		this.peso_total="0.0";
		this.flag="";
		this.latitud="";
		this.longitud="";
		this.codigo_familiar="";
		this.DT_PEDI_FECHASERVIDOR="";
		this.detalles = null;
		this.bonificaciones = null;
		
		
		this.numeroOrdenCompra		= "";
		this.codigoPrioridad		= "";
		this.codigoSucursal			= "";
		this.codigoPuntoEntrega		= "";
		this.codigoTipoDespacho		= "";
		this.flagEmbalaje			= "";
		this.flagPedido_Anticipo	= "";
		this.codigoTransportista	= "";
		this.codigoAlmacen			= "";
		this.CodTurno				= "";
		this.observacion2			= "";
		this.observacion3			= "";
		this.observacionDescuento	= "";
		this.observacionTipoProducto= "";
		this.nroletra		= "";
	}
	
	//constructor2
	public DB_ObjPedido(String oc_numero,String sitio_enfa,String monto_total,String valor_igv,
			String moneda,String fecha_oc,String fecha_mxe,String cond_pago,String cod_cli,String cod_emp,
			String estado,String username,String ruta,String observ,int cod_noventa,String peso_total,String flag,
			String latitud, String longitud, String codigo_familiar, String hora_servidor, 
			ArrayList<DBPedido_Detalle> detalles, ArrayList<DB_RegistroBonificaciones> registroBonificaciones){
		
		this.oc_numero=oc_numero;
		this.sitio_enfa=sitio_enfa;
		this.monto_total=monto_total;
		this.valor_igv=valor_igv;
		this.moneda=moneda;
		this.fecha_oc=fecha_oc;
		this.fecha_mxe=fecha_mxe;
		this.cond_pago=cond_pago;
		this.cod_cli=cod_cli;
		this.cod_emp=cod_emp;
		this.estado=estado;
		this.username=username;
		this.ruta=ruta;
		this.observ=observ;
		this.cod_noventa=cod_noventa;
		this.peso_total=peso_total;
		this.flag=flag;
		this.latitud=latitud;
		this.longitud=longitud;
		this.codigo_familiar=codigo_familiar;
		this.DT_PEDI_FECHASERVIDOR=hora_servidor;
		this.detalles = detalles;
		this.bonificaciones = registroBonificaciones;
	}
	public ArrayList<DB_RegistroBonificaciones> getRegistroBonificaciones() {
		return bonificaciones;
	}
	public void setRegistroBonificaciones(ArrayList<DB_RegistroBonificaciones> registroBonificaciones) {
		this.bonificaciones = registroBonificaciones;
	}
	public String getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(String subtotal) {
		this.subtotal = subtotal;
	}
	public String getDocAdicional() {
		return docAdicional;
	}
	public void setDocAdicional(String docAdicional) {
		this.docAdicional = docAdicional;
	}
	public String getPedidoAnterior() {
		return pedidoAnterior;
	}
	public void setPedidoAnterior(String pedidoAnterior) {
		this.pedidoAnterior = pedidoAnterior;
	}
	public String getCodTurno() {
		return CodTurno;
	}
	public void setCodTurno(String CodTurno) {
		this.CodTurno = CodTurno;
	}

	public ArrayList<Pedido_detalle2> getPedidoDetalle2() {
		return pedidoDetalle2;
	}

	public void setPedidoDetalle2(ArrayList<Pedido_detalle2> pedidoDetalle2) {
		this.pedidoDetalle2 = pedidoDetalle2;
	}

	public double getDsctoBonificacion() {
		return dsctoBonificacion;
	}

	public void setDsctoBonificacion(double dsctoBonificacion) {
		this.dsctoBonificacion = dsctoBonificacion;
	}
}
