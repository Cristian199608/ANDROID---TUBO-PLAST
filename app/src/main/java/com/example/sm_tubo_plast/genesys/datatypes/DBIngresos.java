package com.example.sm_tubo_plast.genesys.datatypes;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

public class DBIngresos implements KvmSerializable {
	
	private String secuencia;
	private String secitm;
	private String fecpag;
	private String total;
	private String acuenta;
	private String saldo;
	private String codcue;
	private String numope;
	private String codforpag;
	private String tipo_cambio;
	private String codmon;
	private String monto_afecto;
	private String username;
	private String fecoperacion;
	private String flag;
    private String latitud;
	private String longitud;
	private String DT_INGR_FECHASERVIDOR;
	private String estado;
	private String mensaje;
	
	private String observacion;
	private String codcli;
	
	private String tipoDoc;
	private String serie;
	private String numero;
	private String codigoBanco;
 

	public String getTipoDoc() {
		return tipoDoc;
	}


	public void setTipoDoc(String tipoDoc) {
		this.tipoDoc = tipoDoc;
	}


	public String getSerie() {
		return serie;
	}


	public void setSerie(String serie) {
		this.serie = serie;
	}


	public String getNumero() {
		return numero;
	}


	public void setNumero(String numero) {
		this.numero = numero;
	}


	public String getCodigoBanco() {
		return codigoBanco;
	}


	public void setCodigoBanco(String codigoBanco) {
		this.codigoBanco = codigoBanco;
	}


	public String getCodcli() {
		return codcli;
	}


	public void setCodcli(String codcli) {
		this.codcli = codcli;
	}


	public String getObservacion() {
		return observacion;
	}


	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}


	public String getMensaje(){
		return mensaje;
	}
	
	
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getDT_INGR_FECHASERVIDOR() {
		return DT_INGR_FECHASERVIDOR;
	}
	public void setDT_INGR_FECHASERVIDOR(String dT_INGR_FECHASERVIDOR) {
		DT_INGR_FECHASERVIDOR = dT_INGR_FECHASERVIDOR;
	}
	public String getLatitud() {
		return latitud;
	}
	public void setLatitud(String latitud) {
		this.latitud = latitud;
	}
	public String getLongitu() {
		return longitud;
	}
	public void setLongitu(String longitud) {
		this.longitud = longitud;
	}

	
	public String getSecuencia() {
		return secuencia;
	}
	public void setSecuencia(String secuencia) {
		this.secuencia = secuencia;
	}
	public String getSecitm() {
		return secitm;
	}
	public void setSecitm(String secitm) {
		this.secitm = secitm;
	}
	public String getFecpag() {
		return fecpag;
	}
	public void setFecpag(String fecpag) {
		this.fecpag = fecpag;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getAcuenta() {
		return acuenta;
	}
	public void setAcuenta(String acuenta) {
		this.acuenta = acuenta;
	}
	public String getSaldo() {
		return saldo;
	}
	public void setSaldo(String saldo) {
		this.saldo = saldo;
	}
	public String getCodcue() {
		return codcue;
	}
	public void setCodcue(String codcue) {
		this.codcue = codcue;
	}
	public String getNumope() {
		return numope;
	}
	public void setNumope(String numope) {
		this.numope = numope;
	}
	public String getCodforpag() {
		return codforpag;
	}
	public void setCodforpag(String codforpag) {
		this.codforpag = codforpag;
	}
	public String getTipo_cambio() {
		return tipo_cambio;
	}
	public void setTipo_cambio(String tipo_cambio) {
		this.tipo_cambio = tipo_cambio;
	}
	public String getCodmon() {
		return codmon;
	}
	public void setCodmon(String codmon) {
		this.codmon = codmon;
	}
	public String getMonto_afecto() {
		return monto_afecto;
	}
	public void setMonto_afecto(String monto_afecto) {
		this.monto_afecto = monto_afecto;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getFecoperacion() {
		return fecoperacion;
	}
	public void setFecoperacion(String fecoperacion) {
		this.fecoperacion = fecoperacion;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
		
		if(this.flag.equals("E")){
			this.mensaje = "";
		}
		else if(this.flag.equals("I")){
			this.mensaje = "";
		}
		else if(this.flag.equals("P")){
			this.mensaje = "Pendiente";
		}
		else if(this.flag.equals("T")){
			this.mensaje = "Transferido";
		}
		else{
			mensaje = "";
		}
		
	}
	
	public DBIngresos() {
        this.secuencia = "";
        this.secitm = "";
        this.fecpag = "";
        this.total = "0.0";
        this.acuenta =" 0.0";
        this.saldo = "0.0";
        this.codcue = "";
        this.numope = "";
        this.codforpag = "";
        this.tipo_cambio = "0.0";
        this.codmon = "";
        this.monto_afecto = "0.0";
        this.username = "";
        this.fecoperacion="";
        this.flag="";
        this.DT_INGR_FECHASERVIDOR="";
    }

    public DBIngresos(String secuencia, String secitm, String fecpag,String total, String acuenta, String saldo, String codcue, String numope,
        String codforpag, String tipocambio, String codmon, String montoafecto, String username, String fecoperacion, String flag,
        String fechaServidor)
    {
        this.secuencia = secuencia;
        this.secitm = secitm;
        this.fecpag = fecpag;
        this.total = total;
        this.acuenta = acuenta;
        this.saldo = saldo;
        this.codcue =codcue;
        this.numope = numope;
        this.codforpag = codforpag;
        this.tipo_cambio = tipocambio;
        this.codmon = codmon;
        this.monto_afecto = montoafecto;
        this.username = username;
        this.fecoperacion = fecoperacion;
        this.flag=flag;
        this.DT_INGR_FECHASERVIDOR=fechaServidor;
    }
    
	@Override
	public Object getProperty(int arg0) {
		// TODO Auto-generated method stub
		switch(arg0)
        {
		case 0:
			return secuencia;
        case 1:
            return secitm;
        case 2:
            return fecpag;
        case 3:
            return total;
        
        case 4:
            return acuenta;
        case 5:
            return saldo;
        case 6:
            return codcue;
        
        case 7:
            return numope;
        case 8:
            return codforpag;
        case 9:
            return tipo_cambio;
        case 10:
            return codmon;
        case 11:
            return monto_afecto;
        case 12:
            return username;
        case 13:
            return fecoperacion;
        case 14:
            return flag;
        case 15:
            return DT_INGR_FECHASERVIDOR;
   
        
        }
		
		return null;
	}
	
	@Override
	public int getPropertyCount() {
		// TODO Auto-generated method stub
		return 16;
	}
	@Override
	public void getPropertyInfo(int ind, Hashtable arg1, PropertyInfo info) {
		// TODO Auto-generated method stub
		switch(ind)
        {
		    case 0:
			    info.type= PropertyInfo.INTEGER_CLASS;
			    info.name="secuencia";
			    break;
	        case 1:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "secitm";
	            break;
	        case 2:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "fecpag";
	            break;
	        case 3:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "total";
	            break;
	        case 4:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "acuenta";
	            break;
	        case 5:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "saldo";
	            break;
	        case 6:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "codcue";
	            break;
	        case 7:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "numope";
	            break;
	        case 8:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "codforpag";
	            break;
	       
	        case 9:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "tipo_cambio";
	            break;
	        case 10:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "codmon";
	            break;
	        case 11:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "monto_afecto";
	            break;
	        case 12:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "username";
	            break;
	        case 13:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "fecoperacion";
	            break;
	        case 14:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "flag";
	            break;
	        case 15:
	            info.type = PropertyInfo.STRING_CLASS;
	            info.name = "DT_INGR_FECHASERVIDOR";
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
			    secuencia=val.toString();
			    break;
	        case 1:
	            secitm= (val.toString());
	            break;
	        case 2:
	            fecpag = val.toString();
	            break;
	        case 3:
	            total = val.toString();
	            break;
	        case 4:
	            acuenta = val.toString();
	            break;
	        case 5:
	            saldo = (val.toString());
	            break;
	        case 6:
	            codcue =val.toString();
	            break;
	       
	        case 7:
	            numope = val.toString();
	            break;
	        case 8:
	            codforpag = val.toString();
	            break;
	        
	        case 9:
	            tipo_cambio= val.toString();
	            break;
	        case 10:
	            codmon = val.toString();
	            break;
	        case 11:
	            monto_afecto = val.toString();
	            break;
	        case 12:
	            username = val.toString();
	            break;
	        case 13:
	            fecoperacion = val.toString();
	            break;
	        case 14:
	            flag = val.toString();
	            break;
	        case 15:
	            DT_INGR_FECHASERVIDOR = val.toString();
	            break;
        }
		
	}
	
}
