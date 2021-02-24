package com.example.fuerzaventaschema.genesys.datatypes;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class DBLocales implements KvmSerializable {

	private String id_local;
	private String des_local;
	private String direccion;
	private String coddep;
	private String codprov;
	private String ubigeo;
	private String tlf;
	private String email;
	private String estado;
	private int bg_color;
	private int txt_color;
	private String latitud;
	private String longitud;
	
	public String getId_local() {
		return id_local;
	}

	public void setId_local(String id_local) {
		this.id_local = id_local;
	}

	public String getDes_local() {
		return des_local;
	}

	public void setDes_local(String des_local) {
		this.des_local = des_local;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getCoddep() {
		return coddep;
	}

	public void setCoddep(String coddep) {
		this.coddep = coddep;
	}

	public String getCodprov() {
		return codprov;
	}

	public void setCodprov(String codprov) {
		this.codprov = codprov;
	}

	public String getUbigeo() {
		return ubigeo;
	}

	public void setUbigeo(String ubigeo) {
		this.ubigeo = ubigeo;
	}

	public String getTlf() {
		return tlf;
	}

	public void setTlf(String tlf) {
		this.tlf = tlf;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public int getBg_color() {
		return bg_color;
	}

	public void setBg_color(int bg_color) {
		this.bg_color = bg_color;
	}

	public int getTxt_color() {
		return txt_color;
	}

	public void setTxt_color(int txt_color) {
		this.txt_color = txt_color;
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

	@Override
	public String toString() {
		return this.des_local;
	}
}
