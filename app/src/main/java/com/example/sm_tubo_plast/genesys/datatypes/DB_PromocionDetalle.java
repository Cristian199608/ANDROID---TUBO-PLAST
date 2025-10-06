package com.example.sm_tubo_plast.genesys.datatypes;

public class DB_PromocionDetalle {

	public static final String salidaBonificacionXCANTIDAD = "XCANTIDAD";
	public static final String salidaBonificacionXDESCUENTO = "XDESCUENTO";
	public static final String salidaBonificacionXDSCMONEDA = "XDSCMONEDA";
	public static final String salidaBonificacionXCOMBO = "XCOMBO";
	public static final String salidaBonificacionXCOLORES = "XCOLORES";
	public static final String salidaBonificacionXREBAJA = "XREBAJA";

	public int secuencia ;
    public int general;
    public String promocion ;
    public String codalm ;
    public String tipo ;
    public int item ;
    public int agrupado ;
    public String entrada ;
    public String tipo_unimed_entrada ;
    public String monto_minimo ;
    public String monto_maximo ;
    public String monto ;
    public String condicion ;
    
    public int cant_condicion ;
    public String salida ;
    public String tipo_unimed_salida ;
    public int cant_promocion ;
    public int max_pedido ;
    public int total_agrupado ;
    public String tipo_promocion ;
	
	public int acumulado;
	
	private String grupo;
	private String familia;
	private String subfamilia;
	private String descuento;
	private int prioridad;

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
	public String getDescuento() {
		return descuento;
	}
	public void setDescuento(String descuento) {
		this.descuento = descuento;
	}
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
	public String getTipo_unimed_entrada() {
		return tipo_unimed_entrada;
	}
	public void setTipo_unimed_entrada(String tipo_unimed_entrada) {
		this.tipo_unimed_entrada = tipo_unimed_entrada;
	}
	public String getMonto_minimo() {
		return monto_minimo;
	}
	public void setMonto_minimo(String monto_minimo) {
		this.monto_minimo = monto_minimo;
	}
	public String getMonto_maximo() {
		return monto_maximo;
	}
	public void setMonto_maximo(String monto_maximo) {
		this.monto_maximo = monto_maximo;
	}
	public String getMonto() {
		return monto;
	}
	public void setMonto(String monto) {
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
	public String getTipo_unimed_salida() {
		return tipo_unimed_salida;
	}
	public void setTipo_unimed_salida(String tipo_unimed_salida) {
		this.tipo_unimed_salida = tipo_unimed_salida;
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
	public String getTipo_promocion() {
		return tipo_promocion;
	}
	public void setTipo_promocion(String tipo_promocion) {
		this.tipo_promocion = tipo_promocion;
	}
	
	
	public int getAcumulado() {
		return acumulado;
	}
	public void setAcumulado(int acumulado) {
		this.acumulado = acumulado;
	}

	public int getPrioridad() {
		return prioridad;
	}

	public void setPrioridad(int prioridad) {
		this.prioridad = prioridad;
	}
}
