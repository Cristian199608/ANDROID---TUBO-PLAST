package com.example.sm_tubo_plast.genesys.BEAN;

public class LugarEntrega {
	private String codigoCliente;
	private String itemSucursal;
	private String codigoLugar;
	private String direccion;
	private String indicadorDespacho;
	private String indicadorCobranza;
	private String direccionEntrega;
	private String latitud;
	private String longitud;
	private String codigoDistrito;
	private String codigo_provincia;
	private String codigo_departamento;
	private String telefono;
	private String contacto;
	private String cargo_contacto;

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
	public String getCodigoLugar() {
		return codigoLugar;
	}
	public void setCodigoLugar(String codigoLugar) {
		this.codigoLugar = codigoLugar;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getIndicadorDespacho() {
		return indicadorDespacho;
	}
	public void setIndicadorDespacho(String indicadorDespacho) {
		this.indicadorDespacho = indicadorDespacho;
	}
	public String getIndicadorCobranza() {
		return indicadorCobranza;
	}
	public void setIndicadorCobranza(String indicadorCobranza) {
		this.indicadorCobranza = indicadorCobranza;
	}
	public String getDireccionEntrega() {
		return direccionEntrega;
	}
	public void setDireccionEntrega(String direccionEntrega) {
		this.direccionEntrega = direccionEntrega;
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

	public String getCodigoDistrito() {
		return codigoDistrito;
	}

	public void setCodigoDistrito(String codigoDistrito) {
		this.codigoDistrito = codigoDistrito;
	}

	public String getCodigo_provincia() {
		return codigo_provincia;
	}

	public void setCodigo_provincia(String codigo_provincia) {
		this.codigo_provincia = codigo_provincia;
	}

	public String getCodigo_departamento() {
		return codigo_departamento;
	}

	public void setCodigo_departamento(String codigo_departamento) {
		this.codigo_departamento = codigo_departamento;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getContacto() {
		return contacto;
	}

	public void setContacto(String contacto) {
		this.contacto = contacto;
	}

	public String getCargo_contacto() {
		return cargo_contacto;
	}

	public void setCargo_contacto(String cargo_contacto) {
		this.cargo_contacto = cargo_contacto;
	}

	public  String getCoordenadas(){
		return this.latitud+","+this.longitud;
	}
	public String getTxtUbigeo(){
		return this.codigoDistrito+" - "+this.codigo_provincia+" - "+this.codigo_departamento;
	}
}
