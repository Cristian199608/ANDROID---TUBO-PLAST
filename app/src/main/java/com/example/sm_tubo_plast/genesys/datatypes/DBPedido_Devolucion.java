package com.example.sm_tubo_plast.genesys.datatypes;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

public class DBPedido_Devolucion implements KvmSerializable {
	
	private String oc_numero;
	private String ean_item;
	private String cip;
	private int cantidad;
	private String unidad_medida;
	private String flag;
	
	
	public String getOc_numero() {
		return oc_numero;
	}
	public void setOc_numero(String oc_numero) {
		this.oc_numero = oc_numero;
	}
	public String getEan_item() {
		return ean_item;
	}
	public void setEan_item(String ean_item) {
		this.ean_item = ean_item;
	}
	public String getCip() {
		return cip;
	}
	public void setCip(String cip) {
		this.cip = cip;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	public String getUnidad_medida() {
		return unidad_medida;
	}
	public void setUnidad_medida(String unidad_medida) {
		this.unidad_medida = unidad_medida;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	
	//Constructor
	public DBPedido_Devolucion(){
		this.oc_numero="";
		this.ean_item="";
		this.cip="";
		this.cantidad=0;
		this.unidad_medida="";
		this.flag="";
	}
	
	//Contructor2
	public DBPedido_Devolucion(String oc_numero,String ean_item,String cip,
			int cantidad,String unidad_medida,String flag){
		
		this.oc_numero=oc_numero;
		this.ean_item=ean_item;
		this.cip=cip;
		this.cantidad=cantidad;
		this.unidad_medida=unidad_medida;
		this.flag=flag;
		
	}
	
	@Override
	public Object getProperty(int arg0) {
		
		switch(arg0)
        {
		case 0:
			return oc_numero;
        case 1:
            return ean_item;
        case 2:
            return cip;
        case 3:
            return cantidad;    
        case 4:
            return unidad_medida;
        case 5:
            return flag;
        }
		
		return null;
	}
	@Override
	public int getPropertyCount() {
		// TODO Auto-generated method stub
		return 6;
	}
	@Override
	public void getPropertyInfo(int ind, Hashtable arg1, PropertyInfo info) {
		
		switch(ind)
        {
		    case 0:
			    info.type= PropertyInfo.STRING_CLASS;
			    info.name="oc_numero";
			    break;
	        case 1:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "ean_item";
	            break;
	        case 2:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "cip";
	            break;
	        case 3:
	            info.type = PropertyInfo.INTEGER_CLASS;
	            info.name = "cantidad";
	            break;
	        case 4:
	            info.type = PropertyInfo.INTEGER_CLASS;
	            info.name = "unidad_medida";
	            break;
	        case 5:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "flag";
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
	            ean_item = val.toString();
	            break;
	        case 2:
	        	cip = val.toString();
	            break;
	        case 3:
	        	cantidad = Integer.parseInt(val.toString());
	            break;
	        case 4:
	        	unidad_medida = val.toString();
	            break;
	        case 5:
	        	flag = val.toString();
	            break;	        
        }
		
	}	
}
