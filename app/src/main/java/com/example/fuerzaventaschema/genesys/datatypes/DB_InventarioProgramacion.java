package com.example.fuerzaventaschema.genesys.datatypes;

public class DB_InventarioProgramacion {

	private int secuencia;
	private String descripcion;
	private String fecha_creacion;
	private String id_local;
	private int encargado;
	private String estado;
	private String fecha_apertura;
	private String fecha_cierre;
	
	
	public int getSecuencia() {
		return secuencia;
	}
	public void setSecuencia(int secuencia) {
		this.secuencia = secuencia;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getFecha_creacion() {
		return fecha_creacion;
	}
	public void setFecha_creacion(String fecha_creacion) {
		this.fecha_creacion = fecha_creacion;
	}
	public String getId_local() {
		return id_local;
	}
	public void setId_local(String id_local) {
		this.id_local = id_local;
	}
	public int getEncargado() {
		return encargado;
	}
	public void setEncargado(int encargado) {
		this.encargado = encargado;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getFecha_apertura() {
		return fecha_apertura;
	}
	public void setFecha_apertura(String fecha_apertura) {
		this.fecha_apertura = fecha_apertura;
	}
	public String getFecha_cierre() {
		return fecha_cierre;
	}
	public void setFecha_cierre(String fecha_cierre) {
		this.fecha_cierre = fecha_cierre;
	}  
	
}
