package com.example.fuerzaventaschema.genesys.datatypes;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class DBDepositos  implements KvmSerializable{
	
private String secuencia;
private String id_banco;
private int id_num_cta;
private String fecha;
private String num_ope;
private String moneda;
private String monto;
private String estado;
private String codven;
private String DT_DEPO_FECHASERVIDOR;
private String BI_DEPO_FLAG;
private String TXT_DEPO_FECHA_REGISTRO;
private String mensaje;

public String getMensaje(){
	return mensaje;
}


public String getBI_DEPO_FLAG() {
	return BI_DEPO_FLAG;
}
public void setBI_DEPO_FLAG(String bI_DEPO_FLAG) {
	BI_DEPO_FLAG = bI_DEPO_FLAG;
	
	if(this.BI_DEPO_FLAG.equals("E")){
		this.mensaje = "";
	}
	else if(this.BI_DEPO_FLAG.equals("I")){
		this.mensaje = "";
	}
	else if(this.BI_DEPO_FLAG.equals("P")){
		this.mensaje = "Pendiente";
	}
	else if(this.BI_DEPO_FLAG.equals("T")){
		this.mensaje = "Transferido";
	}
	else{
		this.mensaje = "";
	}
	
}
public String getDT_DEPO_FECHASERVIDOR() {
	return DT_DEPO_FECHASERVIDOR;
}
public void setDT_DEPO_FECHASERVIDOR(String dT_DEPO_FECHASERVIDOR) {
	DT_DEPO_FECHASERVIDOR = dT_DEPO_FECHASERVIDOR;
}
public String getTXT_DEPO_FECHA_REGISTRO() {
	return TXT_DEPO_FECHA_REGISTRO;
}
public void setTXT_DEPO_FECHA_REGISTRO(String tXT_DEPO_FECHA_REGISTRO) {
	TXT_DEPO_FECHA_REGISTRO = tXT_DEPO_FECHA_REGISTRO;
}
public String getSecuencia() {
	return secuencia;
}
public void setSecuencia(String secuencia) {
	this.secuencia = secuencia;
}
public String getCodven() {
	return codven;
}
public void setCodven(String codven) {
	this.codven = codven;
}

public String getId_banco() {
	return id_banco;
}
public void setId_banco(String id_banco) {
	this.id_banco = id_banco;
}
public int getId_num_cta() {
	return id_num_cta;
}
public void setId_num_cta(int id_num_cta) {
	this.id_num_cta = id_num_cta;
}
public String getFecha() {
	return fecha;
}
public void setFecha(String fecha) {
	this.fecha = fecha;
}
public String getNum_ope() {
	return num_ope;
}
public void setNum_ope(String num_ope) {
	this.num_ope = num_ope;
}
public String getMoneda() {
	return moneda;
}
public void setMoneda(String moneda) {
	this.moneda = moneda;
}
public String getMonto() {
	return monto;
}
public void setMonto(String monto) {
	this.monto = monto;
}
public String getEstado() {
	return estado;
}
public void setEstado(String estado) {
	this.estado = estado;
}


public DBDepositos() {
    this.secuencia = "";
    this.id_banco = "";
    this.id_num_cta=0;
    this.fecha="";
    this.num_ope="";
    this.moneda="";
    this.monto="";
    this.estado="";
    this.codven="";
    this.DT_DEPO_FECHASERVIDOR="";
    this.BI_DEPO_FLAG="";
    this.TXT_DEPO_FECHA_REGISTRO="";
}


@Override
public Object getProperty(int arg0) {
	// TODO Auto-generated method stub
	switch(arg0)
    {
	case 0:
		return secuencia;
    case 1:
        return id_banco;
    case 2:
        return id_num_cta;
    case 3:
        return fecha;
    
    case 4:
        return num_ope;
    case 5:
        return moneda;
    case 6:
        return monto;
    case 7:
        return estado;
    case 8:
        return codven;
    case 9:
        return DT_DEPO_FECHASERVIDOR;
    case 10:
        return BI_DEPO_FLAG;
    case 11:
    	return TXT_DEPO_FECHA_REGISTRO;
    }
	
	return null;
}
@Override
public int getPropertyCount() {
	// TODO Auto-generated method stub
	return 12;
}
@Override
public void getPropertyInfo(int ind, Hashtable arg1, PropertyInfo info) {
	// TODO Auto-generated method stub
	switch(ind)
    {
	    case 0:
		    info.type=PropertyInfo.STRING_CLASS;
		    info.name="secuencia";
		    break;
        case 1:
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "id_banco";
            break;
        case 2:
            info.type = PropertyInfo.INTEGER_CLASS;
            info.name = "id_num_cta";
            break;
        case 3:
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "fecha";
            break;
        case 4:
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "num_ope";
            break;
        case 5:
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "moneda";
            break;
        case 6:
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "monto";
            break;
        case 7:
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "estado";
            break;
        case 8:
        	info.type = PropertyInfo.STRING_CLASS;
            info.name = "codven";
            break;
        case 9:
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "DT_DEPO_FECHASERVIDOR";
            break;
        case 10:
        	info.type = PropertyInfo.STRING_CLASS;
            info.name = "BI_DEPO_FLAG";
            break;
        case 11:
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "TXT_DEPO_FECHA_REGISTRO";
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
		    secuencia=val.toString();
		    break;
        case 1:
            id_banco = val.toString();
            break;
        case 2:
            id_num_cta = Integer.parseInt(val.toString());
            break;
        case 3:
            fecha= val.toString();
            break;
        case 4:
            num_ope = val.toString();
            break;
        case 5:
            moneda= val.toString();
            break;
        case 6:
            monto =val.toString();
            break;
        case 7:
            estado =val.toString();
            break;
        case 8:
            codven =val.toString();
            break;
        case 9:
            DT_DEPO_FECHASERVIDOR =val.toString();
            break;
        case 10:
            BI_DEPO_FLAG =val.toString();
            break;
        case 11:
        	TXT_DEPO_FECHA_REGISTRO =val.toString();
        	break;
    }
	
}




}
