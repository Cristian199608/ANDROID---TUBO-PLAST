package com.example.sm_tubo_plast.genesys.BEAN;

public class Transporte {
	private String codigoCliente;
	private String itemSucursal;
	private String codigoTransporte;
	private String descripcion;
	private String direccion;
	private String ditrito;
	private String provincia;
	private String departamento;

	public String getCodigoCliente() {
		return codigoCliente;
	}
	public void setCodigoCliente(String codigoCliente) {
		this.codigoCliente = codigoCliente;
	}
	public String getItemSucursal() {
		return itemSucursal;
	}
	public void setItemSucursal(String itemSucursal) {
		this.itemSucursal = itemSucursal;
	}
	public String getCodigoTransporte() {
		return codigoTransporte;
	}
	public void setCodigoTransporte(String codigoTransporte) {
		this.codigoTransporte = codigoTransporte;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getDitrito() {
		return ditrito;
	}

	public void setDitrito(String ditrito) {
		this.ditrito = ditrito;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getDepartamento() {
		return departamento;
	}

	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}
}
