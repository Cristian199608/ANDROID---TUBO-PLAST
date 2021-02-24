package com.example.fuerzaventaschema.genesys.datatypes;

public class DBVehiculo {

	public String getCodigo_vehiculo() {
		return codigo_vehiculo;
	}
	public void setCodigo_vehiculo(String codigo_vehiculo) {
		this.codigo_vehiculo = codigo_vehiculo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getPlaca_vehiculo() {
		return placa_vehiculo;
	}
	public void setPlaca_vehiculo(String placa_vehiculo) {
		this.placa_vehiculo = placa_vehiculo;
	}
	private String codigo_vehiculo;
	private String descripcion;
	private String placa_vehiculo;
}
