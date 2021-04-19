package com.example.sm_tubo_plast.genesys.datatypes;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

public class DBClientes implements KvmSerializable {
   private String codcli;
   private String nomcli;
   private String ruccli;
  
   private String stdcli;
   private String comprobante;
  
   private String email;
   private int tipo_documento;
   private int tipo_cliente;
   private int tiempo_credito;
   private float limite_credito;
   private String persona;
   private String afecto;
 
   private String condicion_venta;

   private String codigo_familiar;
   private String fecha_compra;
   private String monto_compra;

public String getFecha_compra() {
	return fecha_compra;
}
public void setFecha_compra(String fecha_compra) {
	this.fecha_compra = fecha_compra;
}
public String getMonto_compra() {
	return monto_compra;
}
public void setMonto_compra(String monto_compra) {
	this.monto_compra = monto_compra;
}
public String getCodigo_familiar() {
	return codigo_familiar;
}
public void setCodigo_familiar(String codigo_familiar) {
	this.codigo_familiar = codigo_familiar;
}
public String getCondicion_venta() {
	return condicion_venta;
}
public void setCondicion_venta(String condicion_venta) {
	this.condicion_venta = condicion_venta;
}

public String getCodcli() {
	return codcli;
}
public void setCodcli(String codcli) {
	this.codcli = codcli;
}
public String getNomcli() {
	return nomcli;
}
public void setNomcli(String nomcli) {
	this.nomcli = nomcli;
}
public String getRuccli() {
	return ruccli;
}
public void setRuccli(String ruccli) {
	this.ruccli = ruccli;
}

public String getStdcli() {
	return stdcli;
}
public void setStdcli(String stdcli) {
	this.stdcli = stdcli;
}
public String getComprobante() {
	return comprobante;
}
public void setComprobante(String comprobante) {
	this.comprobante = comprobante;
}

public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
public int getTipo_documento() {
	return tipo_documento;
}
public void setTipo_documento(int tipo_documento) {
	this.tipo_documento = tipo_documento;
}
public int getTipo_cliente() {
	return tipo_cliente;
}
public void setTipo_cliente(int tipo_cliente) {
	this.tipo_cliente = tipo_cliente;
}
public int getTiempo_credito() {
	return tiempo_credito;
}
public void setTiempo_credito(int tiempo_credito) {
	this.tiempo_credito = tiempo_credito;
}
public float getLimite_credito() {
	return limite_credito;
}
public void setLimite_credito(float limite_credito) {
	this.limite_credito = limite_credito;
}
public String getPersona() {
	return persona;
}
public void setPersona(String persona) {
	this.persona = persona;
}
public String getAfecto() {
	return afecto;
}
public void setAfecto(String afecto) {
	this.afecto = afecto;
}




@Override
public Object getProperty(int arg0) {
	// TODO Auto-generated method stub
	return null;
}
@Override
public int getPropertyCount() {
	// TODO Auto-generated method stub
	return 0;
}
@Override
public void getPropertyInfo(int arg0, Hashtable arg1, PropertyInfo arg2) {
	// TODO Auto-generated method stub
	
}
@Override
public void setProperty(int arg0, Object arg1) {
	// TODO Auto-generated method stub
	
}



   
}
