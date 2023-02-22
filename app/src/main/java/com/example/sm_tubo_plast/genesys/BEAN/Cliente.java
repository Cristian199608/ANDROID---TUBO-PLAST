package com.example.sm_tubo_plast.genesys.BEAN;

import com.example.sm_tubo_plast.genesys.datatypes.DB_DireccionClientes;

public class Cliente {
	private String ruc;
	private String codigoCliente;
	private String sector;
	private String nombre;
	private String direccionFiscal;
	private String giro;
	private String tipo_cliente;
	private String telefono;
	private String canal;
	private String monedaCredito;
	private String limiteCredito;
	private String disponible_credido;
	private String unidadNegocio;
	private String monedaDocumento;
	private String email;
	private DB_DireccionClientes db_direccionClientes;
	private String rubro_cliente;
	private String codven_asginados;

	public String getCodigoCliente() {
		return codigoCliente;
	}
	public void setCodigoCliente(String codigoCliente) {
		this.codigoCliente = codigoCliente;
	}
	public String getSector() {
		return sector;
	}
	public void setSector(String sector) {
		this.sector = sector;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDireccionFiscal() {
		return direccionFiscal;
	}
	public void setDireccionFiscal(String direccionFiscal) {
		this.direccionFiscal = direccionFiscal;
	}
	public String getGiro() {
		return giro;
	}
	public void setGiro(String giro) {
		this.giro = giro;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getCanal() {
		return canal;
	}
	public void setCanal(String canal) {
		this.canal = canal;
	}
	public String getMonedaCredito() {
		return monedaCredito;
	}
	public void setMonedaCredito(String monedaCredito) {
		this.monedaCredito = monedaCredito;
	}
	public String getLimiteCredito() {
		return limiteCredito;
	}
	public void setLimiteCredito(String limiteCredito) {
		this.limiteCredito = limiteCredito;
	}
	public String getUnidadNegocio() {
		return unidadNegocio;
	}
	public void setUnidadNegocio(String unidadNegocio) {
		this.unidadNegocio = unidadNegocio;
	}
	public String getMonedaDocumento() {
		return monedaDocumento;
	}
	public void setMonedaDocumento(String monedaDocumento) {
		this.monedaDocumento = monedaDocumento;
	}
	public String getRuc() {
		return ruc;
	}
	public void setRuc(String ruc) {
		this.ruc = ruc;
	}

	public DB_DireccionClientes getDb_direccionClientes() {
		return db_direccionClientes;
	}

	public void setDb_direccionClientes(DB_DireccionClientes db_direccionClientes) {
		this.db_direccionClientes = db_direccionClientes;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDisponible_credido() {
		return disponible_credido;
	}

	public void setDisponible_credido(String disponible_credido) {
		this.disponible_credido = disponible_credido;
	}

	public String getRubro_cliente() {
		return rubro_cliente;
	}

	public void setRubro_cliente(String rubro_cliente) {
		this.rubro_cliente = rubro_cliente;
	}

	public String getTipo_cliente() {
		return tipo_cliente;
	}

	public void setTipo_cliente(String tipo_cliente) {
		this.tipo_cliente = tipo_cliente;
	}

	public String getCodven_asginados() {
		return codven_asginados;
	}

	public void setCodven_asginados(String codven_asginados) {
		this.codven_asginados = codven_asginados;
	}
}
