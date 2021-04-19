package com.example.sm_tubo_plast.genesys.datatypes;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

public class DB_Detalle_Entrega implements KvmSerializable {
	
	private String oc_numero;
	private String cip;
	private int Nro_Entrega;
	private String dt_fechaEntrega;
	private int cantidad;
	private String CodTurno;
	private String estado; 
	
	
	public DB_Detalle_Entrega(String oc_numero, String cip,
			String dt_fechaEntrega, String codTurno, String estado,
			int nroEntrega, int cantidad) {
		
		this.oc_numero = oc_numero;
		this.cip = cip;
		this.dt_fechaEntrega = dt_fechaEntrega;
		this.CodTurno = codTurno;
		this.estado = estado;
		this.Nro_Entrega = nroEntrega;
		this.cantidad = cantidad;
	}
	
	
	
	
	public DB_Detalle_Entrega() {
		this.oc_numero="";
		this.cip="";
		this.Nro_Entrega=0;
		this.dt_fechaEntrega="";
		this.cantidad=0;
		this.CodTurno="";
		this.estado="";
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
	public String getDt_fechaEntrega() {
		return dt_fechaEntrega;
	}
	public void setDt_fechaEntrega(String dt_fechaEntrega) {
		this.dt_fechaEntrega = dt_fechaEntrega;
	}
	public String getCodTurno() {
		return CodTurno;
	}
	public void setCodTurno(String codTurno) {
		CodTurno = codTurno;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public int getNroEntrega() {
		return Nro_Entrega;
	}
	public void setNroEntrega(int nroEntrega) {
		Nro_Entrega = nroEntrega;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	@Override
	public Object getProperty(int arg0) {
		// TODO Auto-generated method stub
		switch(arg0)
        {
		case 0:
			return oc_numero;
        case 1:
            return cip;
        case 2:
            return Nro_Entrega;
        case 3:
            return dt_fechaEntrega;       
        case 4:
            return cantidad;
        case 5:
            return CodTurno;
        case 6:
            return estado;
        
        }
		
		return null;
	}
	@Override
	public int getPropertyCount() {
		// TODO Auto-generated method stub
		return 7;
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
	            info.name = "cip";
	            break;
	        case 2:
	            info.type = PropertyInfo.INTEGER_CLASS;
	            info.name = "NroEntrega";
	            break;
	        case 3:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "dt_fechaEntrega";
	            break;
	        case 4:
	            info.type = PropertyInfo.INTEGER_CLASS;
	            info.name = "cantidad";
	            break;
	        case 5:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "CodTurno";
	            break;
	        case 6:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "estado";
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
			    oc_numero= val.toString();
			    break;
	        case 1:
	            cip = val.toString();
	            break;
	        case 2:
	            Nro_Entrega = Integer.parseInt(val.toString());
	            break;
	        case 3:
	            dt_fechaEntrega = val.toString();
	            break;
	        case 4:
	            cantidad = Integer.parseInt(val.toString());
	            break;
	        case 5:
	            CodTurno = (val.toString());
	            break;
	        case 6:
	            estado =val.toString();
	            break;
	           
        }
	}
	
}
