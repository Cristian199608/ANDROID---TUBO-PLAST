package com.example.fuerzaventaschema.genesys.datatypes;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class DB_Pedido_Detalle_Promocion implements KvmSerializable {

	private String oc_numero;
	private String cip;
	private String cc_artic;
	private String secuencia;
	private int cantidad;
	private String descripcion;
	
	
	
	
	public DB_Pedido_Detalle_Promocion() {
		this.oc_numero="";
		this.cip="";
		this.cc_artic="";
		this.secuencia="";
		this.cantidad=0;
	}


	public DB_Pedido_Detalle_Promocion(String oc_numero,String cip, String cc_artic,
			String secuencia, int cantidad) {
		super();
		this.oc_numero = oc_numero;
		this.cip=cip;
		this.cc_artic = cc_artic;
		this.secuencia = secuencia;
		this.cantidad = cantidad;
	}
	
	
	public String getOc_numero() {
		return oc_numero;
	}
	public void setOc_numero(String oc_numero) {
		this.oc_numero = oc_numero;
	}
	
	
	public String getCip() {
		return cip;
	}


	public void setCip(String cip) {
		this.cip = cip;
	}


	public String getCc_artic() {
		return cc_artic;
	}
	public void setCc_artic(String cc_artic) {
		this.cc_artic = cc_artic;
	}
	public String getSecuencia() {
		return secuencia;
	}
	public void setSecuencia(String secuencia) {
		this.secuencia = secuencia;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


	@Override
	public Object getProperty(int arg0) {
		switch(arg0)
        {
		case 0:
			return oc_numero;
        case 1:
            return cip;
        case 2:
            return cc_artic;
        case 3:
            return secuencia;       
        case 4:
            return cantidad;
        
        }
		
		return null;
	}


	@Override
	public int getPropertyCount() {
		// TODO Auto-generated method stub
		return 5;
	}


	@Override
	public void getPropertyInfo(int ind, Hashtable arg1, PropertyInfo info) {
		// TODO Auto-generated method stub
		switch(ind)
        {
		    case 0:
			    info.type=PropertyInfo.STRING_CLASS;
			    info.name="oc_numero";
			    break;
	        case 1:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "cip";
	            break;
	        case 2:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "cc_artic";
	            break;
	        case 3:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "secuencia";
	            break;
	        case 4:
	            info.type = PropertyInfo.INTEGER_CLASS;
	            info.name = "cantidad";
	            break;

	            
	        default:break;
        }
	}


	@Override
	public void setProperty(int ind, Object val) {
		switch(ind)
        {
		    case 0:
			    oc_numero= val.toString();
			    break;
	        case 1:
	            cip = val.toString();
	            break;
	        case 2:
	            cc_artic = val.toString();
	            break;
	        case 3:
	            secuencia = val.toString();
	            break;
	        case 4:
	            cantidad = Integer.parseInt(val.toString());
	            break;

	           
        }
		
	}
	
}
