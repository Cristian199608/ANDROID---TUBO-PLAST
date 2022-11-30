package com.example.sm_tubo_plast.genesys.BEAN;

public class ItemProducto {
	
	private String Codprod;
	private String Descripcion;
	private double Precio;
	private double precio_base;
	private double PrecioUnidad;
	private int fact_conv; 	
	private int cantidad;
	private double stock;
	private String desunimed;
	private String codunimed;
	private double peso;
	private double subtotal;//totalVenta;
	private String tipo;
	private String sec_politica;
	private String codProveedor;
	private double percepcion;
	private double percepcionPedido;
	private int item;
	private String afecto;
	
	private String flagTipo;
	
	private double precioLista;
	private double descuento;	
	private double porcentaje_desc;
	private String estado;//Discontinuo

	private String grupo;
	private String familia;
	private String subfamilia;

	
	public String getGrupo() {
		return grupo;
	}
	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}
	public String getFamilia() {
		return familia;
	}
	public void setFamilia(String familia) {
		this.familia = familia;
	}
	public String getSubfamilia() {
		return subfamilia;
	}
	public void setSubfamilia(String subfamilia) {
		this.subfamilia = subfamilia;
	}
	public String getCodunimed() {
		return codunimed;
	}
	public void setCodunimed(String codunimed) {
		this.codunimed = codunimed;
	}
	
	public String getCodProveedor() {
		return codProveedor;
	}
	public void setCodProveedor(String codProveedor) {
		this.codProveedor = codProveedor;
	}
	public String getSec_politica() {
		return sec_politica;
	}
	public void setSec_politica(String sec_politica) {
		this.sec_politica = sec_politica;
	}
	public void setStock(double stock) {
		this.stock = stock;
	}
	public String getCodprod() {
		return Codprod;
	}
	public void setCodprod(String codprod) {
		Codprod = codprod;
	}
	public int getCantidad() {
		return cantidad;
	}
	public double getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	public int getFact_conv() {
		return fact_conv;
	}
	public void setFact_conv(int fact_conv) {
		this.fact_conv = fact_conv;
	}
	public String getDescripcion() {
		return Descripcion;
	}
	public void setDescripcion(String descripcion) {
		Descripcion = descripcion;
	}
	public double getPrecio() {
		return Precio;
	}
	public void setPrecio(double precio) {
		Precio = precio;
	}
	public double getPrecioUnidad() {
		return PrecioUnidad;
	}
	public void setPrecioUnidad(double precioUnidad) {
		PrecioUnidad = precioUnidad;
	}
	public String getDesunimed() {
		return desunimed;
	}
	public void setDesunimed(String desunimed) {
		this.desunimed = desunimed;
	}
	public double getPeso() {
		return peso;
	}
	public void setPeso(double peso) {
		this.peso = peso;
	}
	public double getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public double getPercepcion(){
		return percepcion;
	}
	public void setPercepcion(double percepcion){
		this.percepcion = percepcion;
	}
	public double getPercepcionPedido(){
		return percepcionPedido;
	}
	public void setPercepcionPedido(double percepcionPedido){
		this.percepcionPedido = percepcionPedido;
	}
	public int getItem() {
		return item;
	}
	public void setItem(int item) {
		this.item = item;
	}
	public String getAfecto() {
		return afecto;
	}
	public void setAfecto(String afecto) {
		this.afecto = afecto;
	}
	public double getPrecioLista() {
		return precioLista;
	}
	public void setPrecioLista(double precioLista) {
		this.precioLista = precioLista;
	}
	public double getDescuento() {
		return descuento;
	}
	public void setDescuento(double descuento) {
		this.descuento = descuento;
	}
	public String getFlagTipo() {
		return flagTipo;
	}
	public void setFlagTipo(String flagTipo) {
		this.flagTipo = flagTipo;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}

	public double getPrecio_base() {
		return precio_base;
	}

	public void setPrecio_base(double precio_base) {
		this.precio_base = precio_base;
	}

	public double getPorcentaje_desc() {
		return porcentaje_desc;
	}

	public void setPorcentaje_desc(double porcentaje_desc) {
		this.porcentaje_desc = porcentaje_desc;
	}
}
