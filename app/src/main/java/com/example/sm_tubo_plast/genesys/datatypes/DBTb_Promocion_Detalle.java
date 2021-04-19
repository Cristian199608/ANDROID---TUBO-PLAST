package com.example.sm_tubo_plast.genesys.datatypes;

public class DBTb_Promocion_Detalle {
	
	private int secuencia;
	private int general;
	private String promocion;
	private String codalm;
	private String tipo;
	private int item;
	private int agrupado;
	private String entrada;
	private double monto;
	private String condicion;
	private int cant_condicion;
	private String salida;
	private int cant_promocion;
	private int max_pedido;
	private int total_agrupado;
	//atributo para comparar la cantidad
	private int cant_comp;
	private double prec_comp;
	
	
	public int getSecuencia() {
		return secuencia;
	}
	public void setSecuencia(int secuencia) {
		this.secuencia = secuencia;
	}
	public int getGeneral() {
		return general;
	}
	public void setGeneral(int general) {
		this.general = general;
	}
	public String getPromocion() {
		return promocion;
	}
	public void setPromocion(String promocion) {
		this.promocion = promocion;
	}
	public String getCodalm() {
		return codalm;
	}
	public void setCodalm(String codalm) {
		this.codalm = codalm;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public int getItem() {
		return item;
	}
	public void setItem(int item) {
		this.item = item;
	}
	public int getAgrupado() {
		return agrupado;
	}
	public void setAgrupado(int agrupado) {
		this.agrupado = agrupado;
	}
	public String getEntrada() {
		return entrada;
	}
	public void setEntrada(String entrada) {
		this.entrada = entrada;
	}
	public double getMonto() {
		return monto;
	}
	public void setMonto(double monto) {
		this.monto = monto;
	}
	public String getCondicion() {
		return condicion;
	}
	public void setCondicion(String condicion) {
		this.condicion = condicion;
	}
	public int getCant_condicion() {
		return cant_condicion;
	}
	public void setCant_condicion(int cant_condicion) {
		this.cant_condicion = cant_condicion;
	}
	public String getSalida() {
		return salida;
	}
	public void setSalida(String salida) {
		this.salida = salida;
	}
	public int getCant_promocion() {
		return cant_promocion;
	}
	public void setCant_promocion(int cant_promocion) {
		this.cant_promocion = cant_promocion;
	}
	public int getMax_pedido() {
		return max_pedido;
	}
	public void setMax_pedido(int max_pedido) {
		this.max_pedido = max_pedido;
	}
	public int getTotal_agrupado() {
		return total_agrupado;
	}
	public void setTotal_agrupado(int total_agrupado) {
		this.total_agrupado = total_agrupado;
	}
	//atributo para comparar la cantidad
	public int getCant_comp() {
		return cant_comp;
	}
	public void setCant_comp(int cant_comp) {
		this.cant_comp = cant_comp;
	}
	//atributo para comparar el precio
	public double getPrec_comp() {
		return prec_comp;
	}
	public void setPrec_comp(double prec_comp) {
		this.prec_comp = prec_comp;
	}

}
