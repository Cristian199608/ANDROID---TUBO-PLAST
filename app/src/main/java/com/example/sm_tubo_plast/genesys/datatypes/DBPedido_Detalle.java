package com.example.sm_tubo_plast.genesys.datatypes;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

public class DBPedido_Detalle implements KvmSerializable {
	public static final String PREFIX_PRODUCTO_BONIFICACION_MANUAL = "B";
	private String oc_numero;
	private String ean_item;
	private String cip;
	private String precio_bruto;
	private String precio_neto;
	private String percepcion;
	private int cantidad;
	private String tipo_producto;
	private String unidad_medida;
	private String unidad_medida2;
	private String peso_bruto;
	private String flag;
	private String cod_politica;
	private String sec_promo;
	private int item_promo;
	private int agrup_promo;
	private int item;
	private String precioLista;
	private String descuento;
	private double porcentaje_desc;
	private String lote;
	private String motivoDevolucion;
	private String expectativa;
	private String envase;
	private String contenido;
	private String proceso;
	private String observacionDevolucion;
	private String tipoDocumento;
	private String serieDevolucion;
	private String numeroDevolucion;
	private double porcentaje_desc_extra;

	
	
	public String getMotivoDevolucion() {
		return motivoDevolucion;
	}
	public void setMotivoDevolucion(String motivoDevolucion) {
		this.motivoDevolucion = motivoDevolucion;
	}
	public String getExpectativa() {
		return expectativa;
	}
	public void setExpectativa(String expectativa) {
		this.expectativa = expectativa;
	}
	public String getEnvase() {
		return envase;
	}
	public void setEnvase(String envase) {
		this.envase = envase;
	}
	public String getContenido() {
		return contenido;
	}
	public void setContenido(String contenido) {
		this.contenido = contenido;
	}
	public String getProceso() {
		return proceso;
	}
	public void setProceso(String proceso) {
		this.proceso = proceso;
	}
	public String getObservacionDevolucion() {
		return observacionDevolucion;
	}
	public void setObservacionDevolucion(String observacionDevolucion) {
		this.observacionDevolucion = observacionDevolucion;
	}
	public String getTipoDocumento() {
		return tipoDocumento;
	}
	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	public String getSerieDevolucion() {
		return serieDevolucion;
	}
	public void setSerieDevolucion(String serieDevolucion) {
		this.serieDevolucion = serieDevolucion;
	}
	public String getNumeroDevolucion() {
		return numeroDevolucion;
	}
	public void setNumeroDevolucion(String numeroDevolucion) {
		this.numeroDevolucion = numeroDevolucion;
	}
	public String getLote() {
		return lote;
	}
	public void setLote(String lote) {
		this.lote = lote;
	}
	public void setAgrup_promo(int agrup_promo){
		this.agrup_promo = agrup_promo;
	}
	public int getAgrup_promo(){
		return agrup_promo;
	}
	public String getSec_promo(){
		return sec_promo;
	}
	public void setSec_promo(String sec_promo){
		this.sec_promo = sec_promo;
	}
	public int getItem_promo(){
		return item_promo;
	}
	public void setItem_promo(int item_promo){
		this.item_promo = item_promo;
	}
	public String getCod_politica() {
		return cod_politica;
	}
	public void setCod_politica(String cod_politica) {
		this.cod_politica = cod_politica;
	}
	public String getOc_numero() {
		return oc_numero;
	}
	public void setOc_numero(String oc_numero) {
		this.oc_numero = oc_numero;
	}
	public String getEan_item() {
		return ean_item;
	}
	public void setEan_item(String ean_item) {
		this.ean_item = ean_item;
	}
	public String getCip() {
		return cip;
	}
	public void setCip(String cip) {
		this.cip = cip;
	}
	public String getPrecio_bruto() {
		return precio_bruto;
	}
	public void setPrecio_bruto(String precio_bruto) {
		this.precio_bruto = precio_bruto;
	}
	public String getPrecio_neto() {
		return precio_neto;
	}
	public void setPrecio_neto(String precio_neto) {
		this.precio_neto = precio_neto;
	}
	public String getPercepcion(){
		return percepcion;
	}
	public void setPercepcion(String percepcion){
		this.percepcion = percepcion;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	public String getTipo_producto() {
		return tipo_producto;
	}
	public void setTipo_producto(String tipo_producto) {
		this.tipo_producto = tipo_producto;
	}
	public String getUnidad_medida() {
		return unidad_medida;
	}
	public void setUnidad_medida(String unidad_medida) {
		this.unidad_medida = unidad_medida;
	}
	public String getPeso_bruto() {
		return peso_bruto;
	}
	public void setPeso_bruto(String peso_bruto) {
		this.peso_bruto = peso_bruto;
	}	
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	
	//Constructor
	public DBPedido_Detalle(){
		
		this.oc_numero="";
		this.ean_item="";
		this.cip="";
		this.precio_bruto="0.0";
		this.precio_neto="0.0";
		this.cantidad=0;
		this.tipo_producto="";
		this.unidad_medida="";
		this.peso_bruto="";
		this.flag="";
		this.cod_politica="";
		this.sec_promo = "";
		this.item_promo = 0;
		this.agrup_promo = 0;
	}
	
	//Constructor2
	public DBPedido_Detalle(String oc_numero,String ean_item,String cip,String precio_bruto,
			String precio_neto,int cantidad,String tipo_producto,String unidad_medida,String peso_bruto,String flag,
			String cod_politica){
		
		this.oc_numero=oc_numero;
		this.ean_item=ean_item;
		this.cip=cip;
		this.precio_bruto=precio_bruto;
		this.precio_neto=precio_neto;
		this.cantidad=cantidad;
		this.tipo_producto=tipo_producto;
		this.unidad_medida=unidad_medida;
		this.peso_bruto=peso_bruto;
		this.flag=flag;
		this.cod_politica= cod_politica;
	}
	
	
	@Override
	public Object getProperty(int arg0) {
		// TODO Auto-generated method stub
		
		switch(arg0)
        {
		case 0:
			return oc_numero;
        case 1:
            return ean_item;
        case 2:
            return cip;
        case 3:
            return precio_bruto;      
        case 4:
            return precio_neto;
        case 5:
            return cantidad;
        case 6:
            return tipo_producto;        
        case 7:
            return unidad_medida;
        case 8:
            return peso_bruto;
        case 9:
            return flag;
        case 10:
            return cod_politica;
        case 11:
            return sec_promo;
        case 12:
            return item_promo;
        case 13:
            return agrup_promo;
        }
		
		return null;
	}
	@Override
	public int getPropertyCount() {
		// TODO Auto-generated method stub
		return 14;
	}
	@Override
	public void getPropertyInfo(int ind, Hashtable arg1, PropertyInfo info) {
		// TODO Auto-generated method stub
		
		switch(ind)
        {
		    case 0:
			    info.type= PropertyInfo.STRING_CLASS;
			    info.name="oc_numero";
			    break;
	        case 1:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "ean_item";
	            break;
	        case 2:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "cip";
	            break;
	        case 3:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "precio_bruto";
	            break;
	        case 4:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "precio_neto";
	            break;
	        case 5:
	            info.type = PropertyInfo.INTEGER_CLASS;
	            info.name = "cantidad";
	            break;
	        case 6:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "tipo_producto";
	            break;
	        case 7:
	            info.type = PropertyInfo.INTEGER_CLASS;
	            info.name = "unidad_medida";
	            break;
	        case 8:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "peso_bruto";
	            break;
	        case 9:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "flag";
	            break;
	        case 10:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "cod_politica";
	            break;
	        case 11:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "sec_promo";
	            break;
	        case 12:
	            info.type = PropertyInfo.INTEGER_CLASS;
	            info.name = "item_promo";
	            break;
	        case 13:
	            info.type = PropertyInfo.INTEGER_CLASS;
	            info.name = "agrup_promo";
	            break;
	        default:break;
        }		
		
	}
	
	
	@Override
	public void setProperty(int ind, Object val) {
		// TODO Auto-generated method stub
		
		switch(ind)
        {
		    case 0:
			    oc_numero=val.toString();
			    break;
	        case 1:
	            ean_item = val.toString();
	            break;
	        case 2:
	        	cip = val.toString();
	            break;
	        case 3:
	        	precio_bruto = val.toString();
	            break;
	        case 4:
	        	precio_neto = val.toString();
	            break;
	        case 5:
	        	cantidad = Integer.parseInt(val.toString());
	            break;
	        case 6:
	        	tipo_producto =val.toString();
	            break;
	       
	        case 7:
	        	unidad_medida = val.toString();
	            break;
	        case 8:
	        	peso_bruto = val.toString();
	            break; 
	        case 9:
	        	flag = val.toString();
	            break;
	        case 10:
	        	cod_politica = val.toString();
	            break;
	        case 11:
	        	sec_promo = val.toString();
	            break;
	        case 12:
	        	item_promo = Integer.parseInt(val.toString());
	            break;
	        case 13:
	        	agrup_promo = Integer.parseInt(val.toString());
	            break;
        }
		
	}
	public int getItem() {
		return item;
	}
	public void setItem(int item) {
		this.item = item;
	}
	public String getUnidad_medida2() {
		return unidad_medida2;
	}
	public void setUnidad_medida2(String unidad_medida2) {
		this.unidad_medida2 = unidad_medida2;
	}
	public String getPrecioLista() {
		return precioLista;
	}
	public void setPrecioLista(String precioLista) {
		this.precioLista = precioLista;
	}
	public String getDescuento() {
		return descuento;
	}
	public void setDescuento(String descuento) {
		this.descuento = descuento;
	}

	public double getPorcentaje_desc() {
		return porcentaje_desc;
	}

	public void setPorcentaje_desc(double porcentaje_desc) {
		this.porcentaje_desc = porcentaje_desc;
	}

	public double getPorcentaje_desc_extra() {
		return porcentaje_desc_extra;
	}

	public void setPorcentaje_desc_extra(double porcentaje_desc_extra) {
		this.porcentaje_desc_extra = porcentaje_desc_extra;
	}
}
