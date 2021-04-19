package com.example.sm_tubo_plast.genesys.datatypes;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

public class DBCta_Ingresos implements KvmSerializable {
	
	private String secuencia;
	private String codmon;
	private String coddoc;
	private String serie_doc;
	private String numero_factura;
	private String total;
	private String acuenta;
	private String saldo;
	private String feccom;
	private String codcli;
	private String username;
	private String fecoperacion;
	private String codven;
	private String saldo_virtual;
	
	private String observacion;
	private String fecha_despacho;
	private String	fecha_vencimiento;
	private String plazo;
	private String observaciones;
	private String tipo;
	
	private String forma;
	private String nroUnicoBanco;
	
	public String getForma() {
		return forma;
	}
	public void setForma(String forma) {
		this.forma = forma;
	}
	public String getCc_flag() {
		return cc_flag;
	}
	public void setCc_flag(String cc_flag) {
		this.cc_flag = cc_flag;
	}
	public String getEstado_cobranza() {
		return estado_cobranza;
	}
	public void setEstado_cobranza(String estado_cobranza) {
		this.estado_cobranza = estado_cobranza;
	}
	private String cc_flag;
	private String estado_cobranza;
	
	
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getFecha_despacho() {
		return fecha_despacho;
	}
	public void setFecha_despacho(String fecha_despacho) {
		this.fecha_despacho = fecha_despacho;
	}
	public String getFecha_vencimiento() {
		return fecha_vencimiento;
	}
	public void setFecha_vencimiento(String fecha_vencimiento) {
		this.fecha_vencimiento = fecha_vencimiento;
	}
	public String getPlazo() {
		return plazo;
	}
	public void setPlazo(String plazo) {
		this.plazo = plazo;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	public String getSaldo_virtual() {
		return saldo_virtual;
	}
	public void setSaldo_virtual(String saldo_virtual) {
		this.saldo_virtual = saldo_virtual;
	}
	public String getCodven() {
		return codven;
	}
	public void setCodven(String codven) {
		this.codven = codven;
	}
	public String getSecuencia() {
		return secuencia;
	}
	public void setSecuencia(String secuencia) {
		this.secuencia = secuencia;
	}
	public String getCodmon() {
		return codmon;
	}
	public void setCodmon(String codmon) {
		this.codmon = codmon;
	}
	public String getCoddoc() {
		return coddoc;
	}
	public void setCoddoc(String coddoc) {
		this.coddoc = coddoc;
	}
	public String getSerie_doc() {
		return serie_doc;
	}
	public void setSerie_doc(String serie_doc) {
		this.serie_doc = serie_doc;
	}
	public String getNumero_factura() {
		return numero_factura;
	}
	public void setNumero_factura(String numero_factura) {
		this.numero_factura = numero_factura;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getAcuenta() {
		return acuenta;
	}
	public void setAcuenta(String acuenta) {
		this.acuenta = acuenta;
	}
	public String getSaldo() {
		return saldo;
	}
	public void setSaldo(String saldo) {
		this.saldo = saldo;
	}
	public String getFeccom() {
		return feccom;
	}
	public void setFeccom(String feccom) {
		this.feccom = feccom;
	}
	public String getCodcli() {
		return codcli;
	}
	public void setCodcli(String codcli) {
		this.codcli = codcli;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getFecoperacion() {
		return fecoperacion;
	}
	public void setFecoperacion(String fecoperacion) {
		this.fecoperacion = fecoperacion;
	}
	 
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public String getObservacion() {
		return observacion;
	}
	
	 public DBCta_Ingresos() {
         this.secuencia = "";
         this.codmon = "";
         this.coddoc = "";
         this.serie_doc = "";
         this.numero_factura = "";
         this.total = "0.0";
         this.acuenta = "0.0";
         this.saldo = "0.0";
         this.feccom = "";
         this.codcli = "";
         this.username = "";
         this.fecoperacion = "";
         this.codven="";
         this.saldo_virtual = "0.0";

     }

     public DBCta_Ingresos(String secuencia, String codmon, String coddoc, String serie_doc, String numero_factura,
         String total, String  acuenta,String saldo,String feccom, String codcli, String username, String fecoperacion,
         String codven,String saldo_virtual) {
         this.secuencia = secuencia;
         this.codmon = codmon;
         this.coddoc = coddoc;
         this.serie_doc = serie_doc;
         this.numero_factura = numero_factura;
         this.total = total;
         this.acuenta = acuenta;
         this.saldo = saldo;
         this.feccom = feccom;
         this.codcli = codcli;
         this.username = username;
         this.fecoperacion = fecoperacion;
         this.codven= codven;
         this.saldo_virtual = saldo_virtual;

     }
	
	
	@Override
	public Object getProperty(int arg0) {
		// TODO Auto-generated method stub
		switch(arg0)
        {
		case 0:
			return secuencia;
        case 1:
            return codmon;
        case 2:
            return coddoc;
        case 3:
            return serie_doc;
        
        case 4:
            return numero_factura;
        case 5:
            return total;
        case 6:
            return acuenta;
        
        case 7:
            return saldo;
        case 8:
            return feccom;
        case 9:
            return codcli;
        case 10:
            return username;
        case 11:
            return fecoperacion;
        case 12:
        	return codven;
        case 13:
        	return saldo_virtual;
        }
		
		return null;
	}
	@Override
	public int getPropertyCount() {
		// TODO Auto-generated method stub
		return 14;
	}
	@Override
	public void getPropertyInfo(int ind, Hashtable arg1, PropertyInfo info) {
		// TODO Auto-generated method stub
		switch(ind)
        {
		    case 0:
			    info.type= PropertyInfo.INTEGER_CLASS;
			    info.name="secuencia";
			    break;
	        case 1:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "codmon";
	            break;
	        case 2:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "coddoc";
	            break;
	        case 3:
	            info.type = PropertyInfo.INTEGER_CLASS;
	            info.name = "serie_doc";
	            break;
	        case 4:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "numero_factura";
	            break;
	        case 5:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "total";
	            break;
	        case 6:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "acuenta";
	            break;
	        case 7:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "saldo";
	            break;
	        case 8:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "feccom";
	            break;
	       
	        case 9:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "codcli";
	            break;
	        case 10:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "username";
	            break;
	        case 11:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "fecoperacion";
	            break;
	        case 12:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "codven";
	            break;
	        case 13:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "saldo_virtual";
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
			    secuencia= val.toString();
			    break;
	        case 1:
	            codmon = val.toString();
	            break;
	        case 2:
	            coddoc = val.toString();
	            break;
	        case 3:
	            serie_doc = val.toString();
	            break;
	        case 4:
	            numero_factura = val.toString();
	            break;
	        case 5:
	            total = (val.toString());
	            break;
	        case 6:
	            acuenta =val.toString();
	            break;
	       
	        case 7:
	            saldo = val.toString();
	            break;
	        case 8:
	            feccom = val.toString();
	            break;
	        
	        case 9:
	            codcli= val.toString();
	            break;
	        case 10:
	            username = val.toString();
	            break;
	        case 11:
	            fecoperacion = val.toString();
	            break;
	        case 12:
	            codven = val.toString();
	            break;
	        case 13:
	            saldo_virtual = val.toString();
	            break;
	        
        }
		
	}
	public String getNroUnicoBanco() {
		return nroUnicoBanco;
	}
	public void setNroUnicoBanco(String nroUnicoBanco) {
		this.nroUnicoBanco = nroUnicoBanco;
	}
	
	
}