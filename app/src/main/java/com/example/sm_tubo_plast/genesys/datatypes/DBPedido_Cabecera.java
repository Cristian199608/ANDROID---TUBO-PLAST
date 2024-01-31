package com.example.sm_tubo_plast.genesys.datatypes;

import com.example.sm_tubo_plast.genesys.BEAN.Cliente;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.PedidosActivity;
import com.example.sm_tubo_plast.genesys.util.VARIABLES;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

public class DBPedido_Cabecera implements KvmSerializable {
	
	private String oc_numero;
	private String sitio_enfa;
	private String monto_total;
	private String percepcion_total;
	private String valor_igv;
	private String moneda;
	private String fecha_oc;
	private String fecha_mxe;
	private String cond_pago;
	private String nroletra;
	private String cod_cli;
	private String cod_emp;
	private String estado;
	private String username;
	private String ruta;
	private String observacion;
	private int cod_noventa;
	private String peso_total;
	private String flag;
	private String latitud;
	private String longitud;
	private String codigo_familiar;
	private String DT_PEDI_FECHASERVIDOR;
	private String mensaje;
	private String totalSujetopercepcion;
	private String tipoVista;
	
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
	private String subTotal;
	private String tipoDocumento;
	private String tipoRegistro;	
	
	//Devolucion
	private String codigoGeneraCambio;
	private String codigoRecojo;
	
	//Cotizacion
	private String diasVigencia;
	private String pedidoAnterior;
	private String CodTurno;

	Cliente cliente;
			
	

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

	public String getPedidoAnterior() {
		return pedidoAnterior;
	}

	public void setPedidoAnterior(String pedidoAnterior) {
		this.pedidoAnterior = pedidoAnterior;
	}

	public String getDiasVigencia() {
		return diasVigencia;
	}

	public void setDiasVigencia(String diasVigencia) {
		this.diasVigencia = diasVigencia;
	}

	public String getCodigoRecojo() {
		return codigoRecojo;
	}

	public String getTipoRegistro() {
		return tipoRegistro;
	}

	public void setTipoRegistro(String tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}

	public void setCodigoRecojo(String codigoRecojo) {
		this.codigoRecojo = codigoRecojo;
	}

	public String getCodigoGeneraCambio() {
		return codigoGeneraCambio;
	}

	public void setCodigoGeneraCambio(String codigoGeneraCambio) {
		this.codigoGeneraCambio = codigoGeneraCambio;
	}

	public String getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(String subTotal) {
		this.subTotal = subTotal;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public String getNumeroOrdenCompra() {
		return numeroOrdenCompra;
	}

	public void setNumeroOrdenCompra(String numeroOrdenCompra) {
		this.numeroOrdenCompra = numeroOrdenCompra;
	}

	public String getCodTurno() {
		return CodTurno;
	}

	public void setCodTurno(String CodTurno) {
		this.CodTurno = CodTurno;
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

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getTipoVista() {
		return tipoVista;
	}

	public void setTipoVista(String tipoVista) {
		this.tipoVista = tipoVista;
	}

	public String getMensaje(){
		return mensaje;
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
	
	public String getPercepcion_total() {
		return percepcion_total;
	}
	
	public void setPercepcion_total(String percepcion_total) {
		this.percepcion_total = percepcion_total;
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
		return observacion;
	}
	
	public void setObserv(String observ) {
		this.observacion = observ;
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
		
		if(this.flag.equals("E")){
			this.mensaje = "";
		}
		else if(this.flag.equals("I")){
			this.mensaje = "";
		}
		else if(this.flag.equals("P")){
			this.mensaje = "Pendiente";
		}
		else if(this.flag.equals("T")){
			this.mensaje = "Transferido";
		}
		else{
			this.mensaje = "";
		}
		
	}
	
	public String getTotalSujetopercepcion() {
		return totalSujetopercepcion;
	}

	public void setTotalSujetopercepcion(String totalSujetopercepcion) {
		this.totalSujetopercepcion = totalSujetopercepcion;
	}


	//Constructor
	public DBPedido_Cabecera(){
		this.oc_numero="";
		this.sitio_enfa="";
		this.monto_total="0.0";
		this.valor_igv="0.0";
		this.moneda="";
		this.fecha_oc="";
		this.fecha_mxe="";
		this.cond_pago="";
		//this.nroletra="";
		this.cod_cli="";
		this.cod_emp="";
		this.estado="";
		this.username="";
		this.ruta="";
		this.observacion="";
		this.cod_noventa=0;
		this.peso_total="0.0";
		this.flag="";
		this.latitud="";
		this.longitud="";
		this.codigo_familiar="";
		this.DT_PEDI_FECHASERVIDOR="";
		this.mensaje="";
		
	}
	
	//constructor2
	public DBPedido_Cabecera(String oc_numero,String sitio_enfa,String monto_total,String valor_igv,
			String moneda,String fecha_oc,String fecha_mxe,String cond_pago ,String cod_cli,String cod_emp,
			String estado,String username,String ruta,String observ,int cod_noventa,String peso_total,String flag,
			String latitud, String longitud, String codigo_familiar, String hora_servidor, String mensaje){
		
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
		this.observacion=observ;
		this.cod_noventa=cod_noventa;
		this.peso_total=peso_total;
		this.flag=flag;
		this.latitud=latitud;
		this.longitud=longitud;
		this.codigo_familiar=codigo_familiar;
		this.DT_PEDI_FECHASERVIDOR=hora_servidor;
		this.mensaje = mensaje;
	}
	
	
	@Override
	public Object getProperty(int arg0) {
		
		switch(arg0)
        {
		case 0:
			return oc_numero;
        case 1:
            return sitio_enfa;
        case 2:
            return monto_total;
        case 3:
            return valor_igv;
        
        case 4:
            return moneda;
        case 5:
            return fecha_oc;
        case 6:
            return fecha_mxe;
        
        case 7:
            return cond_pago;
        case 8:
            return cod_cli;
        case 9:
            return cod_emp;
        case 10:
            return estado;
            
        case 11:
            return username;
        case 12:
            return ruta;
        case 13:
            return observacion;
        case 14:
            return cod_noventa;
        case 15:
            return peso_total;
        case 16:
        	return flag;
        case 17:
            return latitud;
        case 18:
        	return longitud;
        case 19:
        	return codigo_familiar;
        case 20:
        	return DT_PEDI_FECHASERVIDOR;
        }
		
		return null;
	}
	@Override
	public int getPropertyCount() {
		// TODO Auto-generated method stub
		return 21;
	}
	@Override
	public void getPropertyInfo(int ind, Hashtable arg1, PropertyInfo info) {
		// TODO Auto-generated method stub
		
		switch(ind)
        {
		    case 0:
			    info.type= PropertyInfo.STRING_CLASS;
			    info.name="oc_numero";
			    break;
	        case 1:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "sitio_enfa";
	            break;
	        case 2:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "monto_total";
	            break;
	        case 3:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "valor_igv";
	            break;
	        case 4:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "moneda";
	            break;
	        case 5:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "fecha_oc";
	            break;
	        case 6:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "fecha_mxe";
	            break;
	        case 7:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "cond_pago";
	            break;
	        case 8:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "cod_cli";
	            break;
	       
	        case 9:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "cod_emp";
	            break;
	        case 10:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "estado";
	            break;
	        case 11:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "username";
	            break;
	        case 12:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "ruta";
	            break;
	        case 13:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "observ";
	            break;
	        case 14:
	            info.type = PropertyInfo.INTEGER_CLASS;
	            info.name = "cod_noventa";
	            break;
	        case 15:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "peso_total";
	            break;
	        case 16:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "flag";
	            break;
	        case 17:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "latitud";
	            break;
	        case 18:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "longitud";
	            break;
	        case 19:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "codigo_familiar";
	            break;  
	        case 20:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "DT_PEDI_FECHASERVIDOR";
	            break; 
	        default:break;
        }		
	}
	
	@Override
	public void setProperty(int ind, Object val) {
		// TODO Auto-generated method stub
		
		switch(ind)
        {
		    case 0:
			    oc_numero=val.toString();
			    break;
	        case 1:
	            sitio_enfa = val.toString();
	            break;
	        case 2:
	        	monto_total = val.toString();
	            break;
	        case 3:
	        	valor_igv = val.toString();
	            break;
	        case 4:
	        	moneda = val.toString();
	            break;
	        case 5:
	        	fecha_oc = val.toString();
	            break;
	        case 6:
	        	fecha_mxe =val.toString();
	            break;
	       
	        case 7:
	        	cond_pago = val.toString();
	            break;
	        case 8:
	        	cod_cli = val.toString();
	            break;
	        
	        case 9:
	        	cod_emp = val.toString();
	            break;
	        case 10:
	        	estado = val.toString();
	            break;
	        case 11:
	        	username = val.toString();
	            break;
	        case 12:
	        	ruta = val.toString();
	            break;
	        case 13:
	        	observacion = val.toString();
	            break;
	        case 14:
	        	cod_noventa= Integer.parseInt(val.toString());
	            break;
	        case 15:
	        	peso_total = val.toString();
	            break;
	        case 16:
	        	flag = val.toString();
	            break;
	        case 17:
	        	latitud = val.toString();
	            break;
	        case 18:
	        	longitud = val.toString();
	            break;
	        case 19:
	        	codigo_familiar = val.toString();
	            break;
	        case 20:
	        	DT_PEDI_FECHASERVIDOR = val.toString();
	            break;
        }
		
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


	public String getDocAdicional() {
		return docAdicional;
	}


	public void setDocAdicional(String docAdicional) {
		this.docAdicional = docAdicional;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public boolean convertirMonedaFrom(double tipoCambio){
		if(this.moneda.equals(PedidosActivity.MONEDA_SOLES_IN)){//si actual es soles entonces convertir a dolares
			convertirMonedaToDolar(tipoCambio);
			return true;
		}else if(this.moneda.equals(PedidosActivity.MONEDA_DOLARES_IN)){//
			convertirMonedaToSoles(tipoCambio);
			return true;
		}
		return false;

	}
	private void convertirMonedaToSoles(double tipoCambio){
		this.moneda=PedidosActivity.MONEDA_SOLES_IN;
		this.monto_total= ""+VARIABLES.getDoubleFormaterThowDecimal(Double.parseDouble(this.monto_total)*tipoCambio);
		this.percepcion_total=""+VARIABLES.getDoubleFormaterThowDecimal(Double.parseDouble(this.percepcion_total)*tipoCambio);
		this.valor_igv=""+VARIABLES.getDoubleFormaterThowDecimal(Double.parseDouble(this.valor_igv)*tipoCambio);
		this.totalSujetopercepcion=""+VARIABLES.getDoubleFormaterThowDecimal(Double.parseDouble(this.totalSujetopercepcion)*tipoCambio);
		this.subTotal=""+VARIABLES.getDoubleFormaterThowDecimal(Double.parseDouble(this.subTotal)*tipoCambio);
	}
	private void convertirMonedaToDolar(double tipoCambio){
		this.moneda=PedidosActivity.MONEDA_DOLARES_IN;
		this.monto_total= ""+VARIABLES.getDoubleFormaterThowDecimal(Double.parseDouble(this.monto_total)/tipoCambio);
		this.percepcion_total=""+VARIABLES.getDoubleFormaterThowDecimal(Double.parseDouble(this.percepcion_total)/tipoCambio);
		this.valor_igv=""+VARIABLES.getDoubleFormaterThowDecimal(Double.parseDouble(this.valor_igv)/tipoCambio);
		this.totalSujetopercepcion=""+VARIABLES.getDoubleFormaterThowDecimal(Double.parseDouble(this.totalSujetopercepcion)/tipoCambio);
		this.subTotal=""+VARIABLES.getDoubleFormaterThowDecimal(Double.parseDouble(this.subTotal)/tipoCambio);
	}
}
