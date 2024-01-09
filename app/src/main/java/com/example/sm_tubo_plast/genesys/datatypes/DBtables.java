package com.example.sm_tubo_plast.genesys.datatypes;

import android.provider.BaseColumns;

public class DBtables {

	DBtables() {

	}

	public static final class Cliente implements BaseColumns {

		private Cliente() {
		}

		public static final String TAG = "cliente";
		public static final String PK_CODCLI = "codcli";
		public static final String NOMCLI = "nomcli";
		public static final String RUCCLI = "ruccli";

		public static final String STDCLI = "stdcli";
		public static final String COMPROBANTE = "comprobante";

		public static final String EMAIL = "email";
		public static final String TIPO_DOCUMENTO = "tipo_documento";
		public static final String TIPO_CLIENTE = "tipo_cliente";
		public static final String TIEMPO_CREDITO = "tiempo_credito";
		public static final String LIMITE_CREDITO = "limite_credito";
		public static final String PERSONA = "persona";
		public static final String AFECTO = "afecto";

		public static final String CONDICION_VENTA = "condicion_venta";
		public static final String CODIGO_FAMILIAR = "codigo_familiar";
		public static final String FECHA_COMPRA = "fecha_compra";
		public static final String MONTO_COMPRA = "monto_compra";
		public static final String OBSERVACION = "observacion";
		public static final String AFECTO_PERCEPCION = "AfectoPercepcion";
		public static final String PERCEPCION = "percepcion";
		public static final String PERCEPCION_ESPECIAL = "PercepcionEspecial";
		
		public static final String DIRECCION_FISCAL = "direccionFiscal";
		public static final String MONEDA_DOCUMENTO = "monedaDocumento";
		public static final String MONEDA_LIMITE_CREDITO = "monedaLimCred";
		public static final String FLAG_MSPACK = "flagMsPack";
		public static final String TELEFONO = "telefono";
		public static final String FLAGCOBRANZA = "flagCobranza";
		
		public static final String SECTOR = "DescSector";
		public static final String GIRO = "Giro";
		public static final String CANAL = "DescCanal";
		public static final String UNIDAD_NEGOCIO = "DescUnidNeg";
		public static final String RUBRO_CLIENTE = "rubro_cliente";
		public static final String DISPONIBLE_CREDITO = "disponible_credito";
		public static final String codven_asginados = "codven_asginados";
		public static final String sistema = "sistema";
		public static final String moneda_ultima_compra="moneda_ultima_compra";

		 
		public static final String CREATE_STATEMENT = "CREATE TABLE " + TAG
				+ " (" + PK_CODCLI + " VARCHAR(8) PRIMARY KEY," + NOMCLI
				+ " VARCHAR(150)," + RUCCLI + " VARCHAR(11)," + STDCLI
				+ " CHAR(1)," + COMPROBANTE + " CHAR(2), " + EMAIL
				+ " VARCHAR(100)," + TIPO_DOCUMENTO + " INTEGER,"
				+ TIPO_CLIENTE + " INTEGER," + TIEMPO_CREDITO
				+ " NUMERIC(8,4)," + LIMITE_CREDITO + " NUMERIC(14,4),"
				+ PERSONA + " CHAR(1)," + AFECTO + " CHAR(1),"
				+ CONDICION_VENTA + " CHAR(2)," + AFECTO_PERCEPCION
				+ "CHAR(1)," + PERCEPCION + " VARCHAR(12),"
				+ PERCEPCION_ESPECIAL + "CHAR(1)" + ");";

	}

	public static  final class Cliente_estado {
		public static final String TAG = "cliente_estado";
		public static final String codcli="codcli";
		public static final String estado="estado";
		public static final String motivo="motivo";
		public static final String codven="codven";
		public static final String fec_server="fec_server";
	}
	public static  final class CLiente_Contacto implements BaseColumns{
		public static final String TAG = "cliente_contacto";
		public static final String codcli ="codcli";
		public static final String id_contacto ="id_contacto";
		public static final String nombre_contacto ="nombre_contacto";
		public static final String dni ="dni";
		public static final String telefono ="telefono";
		public static final String celular ="celular";
		public static final String email ="email";
		public static final String cargo ="cargo";
		public static final String estado ="estado";
		public static final String fec_nacimiento ="fec_nacimiento";
		public static final String flag ="flag";

	}

	public static final class Cta_ingresos implements BaseColumns {

		private Cta_ingresos() {
		}

		public static final String TAG = "cta_ingresos";

		public static final String PK_SECUENCIA = "secuencia";
		public static final String CODMON = "codmon";
		public static final String CODDOC = "coddoc";
		public static final String SERIE_DOC = "serie_doc";
		public static final String NUMERO_FACTURA = "numero_factura";
		public static final String TOTAL = "total";
		public static final String ACUENTA = "acuenta";
		public static final String SALDO = "saldo";
		public static final String FECCOM = "feccom";
		public static final String CODCLI = "codcli";
		public static final String USERNAME = "username";
		public static final String FECOPERACION = "fecoperacion";
		public static final String CODVEN = "codven";
		public static final String SALDO_VIRTUAL = "saldo_virtual"; 
		
		public static final String  FECHA_DESPACHO = "fecha_despacho";
		public static final String  FECHA_VENCIMIENTO = "fecha_vencimiento";
		public static final String  PLAZO = "plazo";
		public static final String  OBSERVACIONES = "observaciones";
		
		public static final String  FORMA = "forma";
		public static final String  CC_FLAG = "cc_flag";
		public static final String  ESTADO_COBRANZA = "Estado_Cobranza";
		public static final String  NRO_UNICO_BANCO = "NroUnicoBanco";
		
		public static final String CREATE_STATEMENT = "CREATE TABLE " + TAG
				+ " (" + PK_SECUENCIA + " INTEGER PRIMARY KEY," + CODMON
				+ " CHAR(2)," + CODDOC + " CHAR(2)," + SERIE_DOC + " CHAR(6),"
				+ NUMERO_FACTURA + " CHAR(20)," + TOTAL + " NUMERIC(10,3),"
				+ ACUENTA + " NUMERIC(10,3)," + SALDO + " NUMERIC(10,3),"
				+ FECCOM + " CHAR(10)," + CODCLI + " CHAR(8)," + USERNAME
				+ " VARCHAR(30)," + FECOPERACION + " CHAR(10));";

	}

	public static final class Locales implements BaseColumns {

		private Locales() {
		}

		public static final String TAG = "locales";

		public static final String PK_ID_LOCAL = "id_local";
		public static final String DES_LOCAL = "des_local";
		public static final String DIRECCION = "direccion";
		public static final String CODDEP = "coddep";
		public static final String CODPROV = "codprov";
		public static final String UBIGEO = "ubigeo";
		public static final String TLF = "tlf";
		public static final String EMAIL = "email";
		public static final String ESTADO = "estado";
		public static final String BG_COLOR = "bg_color";
		public static final String TXT_COLOR = "txt_color";
		public static final String LATITUD = "latitud";
		public static final String LONGITUD = "longitud";

	}

	public static final class Configuracion implements BaseColumns {

		private Configuracion() {
		}

		public static final String TAG = "configuracion";

		public static final String NOMBRE = "nombre";
		public static final String VALOR = "valor";
	}

	public static final class Vehiculo implements BaseColumns {

		private Vehiculo() {
		}

		public static final String TAG = "vehiculo";

		public static final String PK_CODIGO_VEHICULO = "codigo_vehiculo";
		public static final String DESCRIPCION = "descripcion";
		public static final String PLACA_VEHICULO = "placa_vehiculo";

	}

	public static final class Factura2 implements BaseColumns {

		private Factura2() {
		}

		public static final String TAG = "factura2";

		public static final String PK_SECUENCIA = "secuencia";
		public static final String CODPRO = "codpro";
		public static final String ITEM = "item";
		public static final String CANTIDAD = "cantidad";
		public static final String TOTAL = "total";
		public static final String PREVEN = "preven";
		public static final String TOTAL_REAL = "total_real";

	}

	public static final class Factura1 implements BaseColumns {

		private Factura1() {
		}

		public static final String TAG = "factura1";

		public static final String PK_SECUENCIA = "secuencia";
		public static final String CODCLI = "codcli";
		public static final String CODFORPAG = "codforpag";
		public static final String TIEMPO_CREDITO = "tiempo_credito";
		public static final String TOTAL = "total";
		public static final String ACUENTA = "acuenta";
		public static final String IGV = "igv";
		public static final String SALDO = "saldo";
		public static final String FECFAC = "fecfac";
		public static final String ESTADO = "estado";
		public static final String NUMERO_GUIA = "numero_guia";
		public static final String NUMERO_FACTURA = "numero_factura";
		public static final String SUB_TOTAL = "sub_total";
		public static final String SERIE_GUIA = "serie_guia";
		public static final String SERIE_FACTURA = "serie_factura";
		public static final String CODVEN = "codven";
		public static final String TIPO_DOCUMENTO = "tipo_documento";
		public static final String DEPOSITO = "deposito";
		public static final String ID_LOCAL = "id_local";
		public static final String USECOD = "usecod";

	}

	public static final class Liquidacion_Preventa1 implements BaseColumns {

		private Liquidacion_Preventa1() {
		}

		public static final String TAG = "liquidacion_preventa1";

		public static final String PK_SECUENCIA = "secuencia";
		public static final String CODIGO_CHOFER = "codigo_chofer";
		public static final String CODIGO_VEHICULO = "codigo_vehiculo";
		public static final String FECHA_REGISTRO = "fecha_registro";
		public static final String ESTADO = "estado";
		public static final String OBSERVACION = "observacion";
		public static final String TOTAL_LIQUID = "total_liquid";
		public static final String TOTAL_CONTADO = "total_contado";
		public static final String TOTAL_CREDITO = "total_credito";
		public static final String TOTAL_COBRANZA = "total_cobranza";
		public static final String CODALM = "codalm";
		public static final String ID_LOCAL = "id_local";
		public static final String TOTAL_DEPOSITO = "total_deposito";

	}

	public static final class Liquidacion_Preventa2 implements BaseColumns {

		private Liquidacion_Preventa2() {
		}

		public static final String TAG = "liquidacion_preventa2";

		public static final String PK_SECUENCIA = "secuencia";
		public static final String CODPRO = "codpro";
		public static final String CANTIDAD_FACT = "cantidad_fact";
		public static final String CANTIDAD_DEV = "cantidad_dev";
		public static final String CANTIDAD_VEN = "cantidad_ven";

	}

	public static final class Liquidacion_Preventa3 implements BaseColumns {

		private Liquidacion_Preventa3() {
		}

		public static final String TAG = "liquidacion_preventa3";

		public static final String PK_SECUENCIA = "secuencia";
		public static final String SEC_GUIA_DOCUMENTOS = "sec_guia_documentos";
		public static final String TIPO_DOC = "tipo_doc";
		public static final String SEC_DOC = "sec_doc";
		public static final String MANUAL = "manual";

	}

	public static final class NotaCredito1 implements BaseColumns {

		private NotaCredito1() {
		}

		public static final String TAG = "nota_credito1";

		public static final String PK_SECUENCIA = "secuencia";
		public static final String SERIE_FACTURA = "serie_factura";
		public static final String NUMERO_FACTURA = "numero_factura";
		public static final String SERIE_NOTA_CREDITO = "serie_nota_credito";
		public static final String NUMERO_NOTA_CREDITO = "numero_nota_credito";
		public static final String CODCLI = "codcli";
		public static final String FECHA = "fecha";
		public static final String OBSERVACION = "observacion";
		public static final String MONTO = "monto";
		public static final String ESTADO = "estado";
		public static final String CODTIPNOT = "codtipnot";
		public static final String SUB_TOTAL = "sub_total";
		public static final String TIPO_DOC = "tipo_doc";
		public static final String CODVEN = "codven";
		public static final String ID_LOCAL = "id_local";
		public static final String USECOD = "usecod";
		public static final String PREVENTA = "preventa";

	}

	public static final class NotaCredito2 implements BaseColumns {

		private NotaCredito2() {
		}

		public static final String TAG = "nota_credito2";

		public static final String PK_SECUENCIA = "secuencia";
		public static final String ITEM = "item";
		public static final String CODPRO = "codpro";
		public static final String CANTIDAD_DEVUELTA = "cantidad_devuelta";
		public static final String PRECIO_UNITARIO = "precio_unitario";
		public static final String TOTAL = "total";

	}

	public static final class Chofer implements BaseColumns {

		private Chofer() {
		}

		public static final String TAG = "chofer";

		public static final String PK_CODIGO_CHOFER = "codigo_chofer";
		public static final String NOMBRE_CHOFER = "nombre_chofer";
		public static final String NRO_BREVETE = "nro_brevete";
		public static final String CATEGORIA = "categoria";
		public static final String USECOD = "usecod";

	}

	public static final class Ingresos implements BaseColumns {

		private Ingresos() {
		}

		public static final String TAG = "ingresos";

		public static final String PK_SECUENCIA = "secuencia";
		public static final String PK_SECITM = "secitm";
		public static final String FECPAG = "fecpag";
		public static final String TOTAL = "total";
		public static final String ACUENTA = "acuenta";
		public static final String SALDO = "saldo";
		public static final String CODCUE = "codcue";
		public static final String NUMOPE = "numope";
		public static final String CODFORPAG = "codforpag";
		public static final String TIPO_CAMBIO = "tipo_cambio";
		public static final String CODMON = "codmon";
		public static final String MONTO_AFECTO = "monto_afecto";
		public static final String USERNAME = "username";
		public static final String FECOPERACION = "fecoperacion";
		public static final String FLAG = "flag";
		public static final String LATITUD = "latitud";
		public static final String LONGITUD = "longitud";
		public static final String DT_INGR_FECHASERVIDOR = "DT_INGR_FECHASERVIDOR";
		public static final String ESTADO = "estado";

		public static final String OBSERVACION = "observacion";
		public static final String CODCLI = "codcli";
		
		public static final String TIPO_DOCUMENTO = "tipoDoc";
		public static final String SERIE = "serie";
		public static final String NUMERO = "numero";
		public static final String CODIGO_BANCO = "codigoBanco";
		
		public static final String CREATE_STATEMENT = "CREATE TABLE " + TAG
				+ " (" + PK_SECUENCIA + " INTEGER," + PK_SECITM + " INTEGER,"
				+ FECPAG + " CHAR(10)," + TOTAL + " NUMERIC(10,2)," + ACUENTA
				+ " NUMERIC(10,2)," + SALDO + " NUMERIC(10,2)," + CODCUE
				+ " CHAR(2)," + NUMOPE + " VARCHAR(20)," + CODFORPAG
				+ " CHAR(2)," + TIPO_CAMBIO + " NUMERIC(10,2)," + CODMON
				+ " CHAR(2)," + MONTO_AFECTO + " NUMERIC(10,2)," + USERNAME
				+ " VARCHAR(30)," + FECOPERACION + " CHAR(10), PRIMARY KEY("
				+ PK_SECUENCIA + ", " + PK_SECITM + "));";
	}

	public static final class MTA_kardex implements BaseColumns {

		private MTA_kardex() {
		}

		public static final String TAG = "mta_kardex";

		public static final String PK_KARDEX = "kardex";
		public static final String PK_CODALM = "codalm";
		public static final String PK_CODPRO = "codpro";
		public static final String STOCK = "stock";
		public static final String XTEMP = "xtemp";

		public static final String CREATE_STATEMENT = "CREATE TABLE " + TAG
				+ " (" + PK_KARDEX + " VARCHAR(8)," + PK_CODALM + " CHAR(2),"
				+ PK_CODPRO + " CHAR(4)," + STOCK + " NUMERIC(18,4)," + XTEMP
				+ " NUMERIC(18,4), PRIMARY KEY(" + PK_KARDEX + ", " + PK_CODALM
				+ ", " + PK_CODPRO + "));";

	}

	public static final class Familia implements BaseColumns {

		public Familia() {
		}

		public static final String TAG = "familia";

		public static final String SECUENCIA = "secuencia";
		public static final String FAMILIA = "familia";

		public static final String CREATE_STATEMENT = "CREATE TABLE " + TAG
				+ " (" + SECUENCIA + " CHAR(4) PRIMARY KEY," + FAMILIA
				+ " VARCHAR(35));";

	}

	public static final class Pedido_cabecera implements BaseColumns {

		private Pedido_cabecera() {
		}

		public static final String TAG = "pedido_cabecera";

		public static final String PK_OC_NUMERO = "oc_numero";
		public static final String SITIO_ENFA = "sitio_enfa";
		public static final String MONTO_TOTAL = "monto_total";
		public static final String PERCEPCION_TOTAL = "percepcion_total";
		public static final String VALOR_IGV = "valor_igv";
		public static final String MONEDA = "moneda";
		public static final String FECHA_OC = "fecha_oc";
		public static final String FECHA_MXE = "fecha_mxe";
		public static final String COND_PAGO = "cond_pago";
		public static final String NRO_LETRA = "nro_letra";
		public static final String COD_CLI = "cod_cli";
		public static final String COD_EMP = "cod_emp";
		public static final String ESTADO = "estado";
		public static final String USERNAME = "username";
		public static final String RUTA = "ruta";
		public static final String OBSERV = "observacion";
		public static final String COD_NOVENTA = "cod_noventa";
		public static final String PESO_TOTAL = "peso_total";
		public static final String FLAG = "flag";
		public static final String LATITUD = "latitud";
		public static final String LONGITUD = "longitud";
		public static final String CODIGO_FAMILIAR = "codigo_familiar";
		public static final String DT_PEDI_FECHASERVIDOR = "DT_PEDI_FECHASERVIDOR";
		public static final String MENSAJE = "mensaje";
		public static final String TOTAL_SUJETO_PERCEPCION = "TotalSujetoPercepcion";
		public static final String TIPO_VISTA = "tipoVista";
		
		public static final String NRO_ORDEN_COMPRA = "numeroOrdenCompra";
		public static final String CODIGO_PRIORIDAD = "codigoPrioridad";
		public static final String CODIGO_SUCURSAL = "codigoSucursal";
		public static final String CODIGO_PUNTO_ENTREGA = "codigoPuntoEntrega";
		public static final String CODIGO_TIPO_DESPACHO = "codigoTipoDespacho";
		public static final String FLAG_EMBALAJE = "flagEmbalaje";
		public static final String FLAG_PEDIDOANTICIPO = "flagPedido_Anticipo";
		public static final String CODIGO_TRANSPORTISTA = "codigoTransportista";
		public static final String CODIGO_ALMACEN = "codigoAlmacen";
		public static final String OBSERVACION2 = "observacion2";
		public static final String OBSERVACION3 = "observacion3";
		public static final String OBSERVACION4 = "observacion4";
		public static final String OBSERVACION_DESCUENTO = "observacionDescuento";
		public static final String OBSERVACION_TIPO_PROD = "observacionTipoProducto";
		public static final String FLAG_DESCUENTO = "flagDescuento";
		public static final String CODIGO_OBRA = "codigoObra";
		public static final String FLAG_DESPACHO = "flagDespacho";		
		public static final String DOC_ADICIONAL = "docAdicional";
		public static final String SUBTOTAL = "subTotal";
		
		public static final String TIPODOCUMENTO = "tipoDocumento";
		public static final String TIPO_REGISTRO = "tipoRegistro";
		public static final String DIAS_VIGENCIA = "diasVigencia";
		public static final String PEDIDO_ANTERIOR = "pedidoAnterior";
		public static final String CODIGO_TURNO = "CodTurno";
 
		public static final String CREATE_STATEMENT = "CREATE TABLE " + TAG
				+ " (" + PK_OC_NUMERO + " CHAR(21) PRIMARY KEY, " + SITIO_ENFA
				+ " VARCHAR(100), " + MONTO_TOTAL + " DECIMAL(12,4), "
				+ VALOR_IGV + " DECIMAL(12,4)," + MONEDA + " CHAR(2),"
				+ FECHA_OC + " CHAR(10)," + FECHA_MXE + " CHAR(10),"
				+ COND_PAGO + " CHAR(2)," + COD_CLI + " CHAR(8)," + COD_EMP
				+ " CHAR(6)," + ESTADO + " CHAR(2)," + USERNAME
				+ " VARCHAR(30)," + RUTA + " CHAR(6)," + OBSERV
				+ " VARCHAR(200)," + COD_NOVENTA + " INTEGER," + PESO_TOTAL
				+ " DECIMAL(10,6)," + FLAG + " CHAR(1)," + TIPO_VISTA
				+ " VARCHAR(20));";

	}

	public static final class Pedido_detalle implements BaseColumns {

		private Pedido_detalle() {
		}

		public static final String TAG = "pedido_detalle";

		public static final String PK_OC_NUMERO = "oc_numero";
		public static final String PK_EAN_ITEM = "ean_item";
		public static final String CIP = "cip";
		public static final String PRECIO_BRUTO = "precio_bruto";
		public static final String PRECIO_NETO = "precio_neto";
		public static final String PERCEPCION = "percepcion";
		public static final String CANTIDAD = "cantidad";
		public static final String TIPO_PRODUCTO = "tipo_producto";
		public static final String UNIDAD_MEDIDA = "unidad_medida";
		public static final String PESO_BRUTO = "peso_bruto";
		public static final String FLAG = "flag";
		public static final String COD_POLITICA = "cod_politica";
		public static final String SEC_PROMO = "sec_promo";
		public static final String ITEM_PROMO = "item_promo";
		public static final String AGRUP_PROMO = "agrup_promo";
		public static final String CANT_PROMO = "cant_promo";
		public static final String ITEM = "item";
		public static final String PRECIO_LISTA = "precioLista";
		public static final String DESCUENTO = "descuento";
		public static final String PORCENTAJE_DESC = "porcentaje_desc";
		public static final String porcentaje_desc_extra = "porcentaje_desc_extra";
		public static final String LOTE = "lote";
		
		public static final String MOTIVO_DEVOLUCION = "motivoDevolucion";
		public static final String EXPECTATIVA = "expectativa";
		public static final String ENVASE = "envase";
		public static final String CONTENIDO = "contenido";
		public static final String PROCESO = "proceso";
		public static final String OBSERVACION_DEVOL = "observacionDevolucion";
		public static final String TIPO_DOCUMENTO = "tipoDocumento";
		public static final String SERIE_DEVOLUCION = "serieDevolucion";
		public static final String NUMERO_DEVOLUCION = "numeroDevolucion";
				   
		public static final String CREATE_STATEMENT = "CREATE TABLE " + TAG
				+ " (" 
				+ PK_OC_NUMERO + " CHAR(21), " 
				+ PK_EAN_ITEM  + " CHAR(14), " 
				+ CIP + " CHAR(21), " 
				+ PRECIO_BRUTO + " DECIMAL(12,4)," 
				+ PRECIO_NETO + " DECIMAL(12,4),"
				+ PERCEPCION + " DECIMAL(12,4),"
				+ CANTIDAD + " INTEGER," 
				+ TIPO_PRODUCTO + " CHAR(1),"
				+ UNIDAD_MEDIDA + " INTEGER," 
				+ PESO_BRUTO	+ " DECIMAL(10,6)," 
				+ FLAG + " CHAR(1),"
				+ COD_POLITICA + "VARCHAR(5),"
				+ SEC_PROMO + "VARCHAR(100),"
				+ ITEM_PROMO + "INTEGER,"
				+ AGRUP_PROMO + "INTEGER,"
				+ ITEM + "INTEGER "
				+ "PRIMARY KEY("+ PK_OC_NUMERO + ", " + PK_EAN_ITEM + "));";

	}

	public static final class Pedido_devolucion implements BaseColumns {

		private Pedido_devolucion() {
		}

		public static final String TAG = "pedido_devolucion";

		public static final String PK_OC_NUMERO = "oc_numero";
		public static final String EAN_ITEM = "ean_item";
		public static final String PK_CIP = "cip";
		public static final String CANTIDAD = "cantidad";
		public static final String UNIDAD_MEDIA = "unidad_medida";
		public static final String FLAG = "flag";

		public static final String CREATE_STATEMENT = "CREATE TABLE " + TAG
				+ " (" + PK_OC_NUMERO + " CHAR(21), " + EAN_ITEM
				+ " CHAR(13), " + PK_CIP + " CHAR(4), " + CANTIDAD
				+ " INTEGER, " + "UNIDAD_MEDIDA" + " INTEGER," + FLAG
				+ " CHAR(1) " + "PRIMARY KEY(" + PK_OC_NUMERO + ", " + PK_CIP
				+ "));";
	}

	public static final class Politica_precio2 implements BaseColumns {

		private Politica_precio2() {
		}

		public static final String TAG = "politica_precio2";

		public static final String PK_SECUENCIA = "secuencia";
		public static final String ITEM = "item";
		public static final String PK_CODPRO = "codpro";
		public static final String PREPRO = "prepro";
		public static final String PREPRO_UNIDAD = "prepro_unidad";

		public static final String CREATE_STATEMENT = "CREATE TABLE " + TAG
				+ " (" + PK_SECUENCIA + " INTEGER, " + ITEM + " INTEGER, "
				+ PK_CODPRO + " CHAR(4), " + PREPRO + " NUMERIC(18,4), "
				+ PREPRO_UNIDAD + " NUMERIC(18,4) PRIMARY KEY(" + PK_SECUENCIA
				+ ", " + PK_CODPRO + "));";

	}

	public static final class Politica_cliente implements BaseColumns {

		private Politica_cliente() {
		}

		public static final String TAG = "politica_cliente";

		public static final String SEC_POLITICA = "sec_politica";
		public static final String CODCLI = "codcli";

		public static final String CREATE_STATEMENT = "CREATE TABLE " + TAG
				+ " (" + SEC_POLITICA + " INTEGER, " + CODCLI + " CHAR(8));";

	}

	public static final class Producto implements BaseColumns {

		private Producto() {
		}

		public static final String TAG = "producto";

		public static final String CODPRO = "codpro";
		public static final String DESPRO = "despro";
		public static final String ABREVPRO = "abrevpro";
		public static final String GRUPO = "grupo";
		public static final String FK_FAMILIA = "familia";
		public static final String FK_SUB_FAMILIA = "sub_familia";
		public static final String EAN13 = "ean13";
		public static final String COD_RAPIDO = "cod_rapido";
		public static final String CODUNIMED = "codunimed";
		public static final String CODUNIMED_ALMACEN = "codunimed_almacen";
		public static final String FACTOR_CONVERSION = "factor_conversion";
		public static final String AFECTO = "afecto";
		public static final String ESTADO = "estado";
		public static final String PESO = "peso";
		public static final String FOTO = "foto";
		public static final String LINEA_NEGOCIO = "linea_negocio";
		public static final String PERCEPCION = "percepcion";
		
		public static final String TIPO_PRODUCTO = "tipoProducto";
		public static final String _PRECIO_BASE = "_precio_base";
		public static final String desc_comercial = "desc_comercial";

		public static final String CREATE_STATEMENT = "CREATE TABLE " + TAG
				+ " (" + CODPRO + " CHAR(4) PRIMARY KEY, " + DESPRO
				+ " VARCHAR(100), " + ABREVPRO + " VARCHAR(80), " + GRUPO
				+ " CHAR(4)," + FK_FAMILIA + " CHAR(4)," + FK_SUB_FAMILIA
				+ " CHAR(4)," + EAN13 + " CHAR(13)," + COD_RAPIDO
				+ " VARCHAR(6)," + CODUNIMED + " CHAR(2)," + CODUNIMED_ALMACEN
				+ " CHAR(2)," + FACTOR_CONVERSION + " NUMERIC(8,2)," + AFECTO
				+ " CHAR(1)," + ESTADO + " CHAR(1)," + PESO
				+ " NUMERIC(10,3));";

	}

	public static final class Promocion_clientes implements BaseColumns {

		private Promocion_clientes() {
		}

		public static final String TAG = "promocion_clientes";

		public static final String SEC_PROMOCION = "sec_promocion";
		public static final String CODCLI = "codcli";

		public static final String CREATE_STATEMENT = "CREATE TABLE " + TAG
				+ " (" + SEC_PROMOCION + " INTEGER PRIMARY KEY, " + CODCLI
				+ " CHAR(8));";

	}

	public static final class Promocion_Vendedor implements BaseColumns {

		private Promocion_Vendedor() {
		}

		public static final String TAG = "promocion_vendedor";

		public static final String SEC_PROMOCION = "sec_promocion";
		public static final String CODVEN = "codven";

	}

	public static final class Promocion_Politica implements BaseColumns {

		private Promocion_Politica() {
		}

		public static final String TAG = "promocion_politica";

		public static final String SEC_PROMOCION = "sec_promocion";
		public static final String SEC_POLITICA = "sec_politica";

	}

	public static final class Sub_familia implements BaseColumns {

		private Sub_familia() {
		}

		public static final String TAG = "sub_familia";

		public static final String SUB_FAMILIA = "sub_familia";
		public static final String DES_SUBFAM = "des_subfam";
		public static final String SECUENCIA = "secuencia";

		public static final String CREATE_STATEMENT = "CREATE TABLE " + TAG
				+ " (" + SUB_FAMILIA + " CHAR(4) PRIMARY KEY, " + DES_SUBFAM
				+ " CHAR(35)," + SECUENCIA + " CHAR(4));";

	}
	
	public static final class TB_registro_bonificaciones implements BaseColumns{
		private TB_registro_bonificaciones(){
			
		}
		
		public static final String TAG = "registro_bonificaciones";
		
		public static final String OC_NUMERO = "oc_numero";
		public static final String ITEM = "item";
		public static final String SECUENCIA = "secuenciaPromocion";
		public static final String AGRUPADO = "agrupado";
		
		public static final String ENTRADA = "entrada";		
		public static final String TIPO_UNIMED_ENTRADA = "tipo_unimed_entrada";
		public static final String UNIMED_ENTRADA = "unimedEntrada";
		public static final String CANTIDAD_ENTRADA = "cantidadEntrada";
		public static final String MONTO_ENTRADA = "montoEntrada";
		
		public static final String SALIDA = "salida";
		public static final String TIPO_UNIMED_SALIDA = "tipo_unimed_salida";
		public static final String CANTIDAD_SALIDA = "cantidadSalida";
		
		public static final String ACUMULADO = "acumulado";
		public static final String CANTIDAD_DISPONIBLE = "cantidadDisponible";
		public static final String MONTO_DISPONIBLE = "montoDisponible";
		public static final String FLAG = "flagUltimo";
		
		public static final String CODIGO_REGISTRO = "codigoRegistro";
		public static final String CANTIDAD_TOTAL = "cantidadTotal";
		public static final String SALDO_ANTERIOR = "saldoAnterior";
		public static final String CANTIDAD_ENTREGADA = "cantidadEntregada";
		public static final String SALDO = "saldo";
		public static final String CODIGO_ANTERIOR = "codigoAnterior";
		public static final String CODIGO_VENDEDOR = "codigoVendedor";
		public static final String CODIGO_CLIENTE = "codigoCliente";
		
		
		
		public static final String CREATE_STATEMENT = 
				"CREATE TABLE "+TAG+
				" ( "+
					OC_NUMERO + " VARCHAR(50), "+	
					ITEM + " INTEGER, "+
					SECUENCIA + " INTEGER, "+
					AGRUPADO + " INTEGER, "+
					
					ENTRADA + " CHAR(4), "+
					TIPO_UNIMED_ENTRADA + " INTEGER, "+
					UNIMED_ENTRADA + " INTEGER, "+
					CANTIDAD_ENTRADA + " INTEGER, "+
					MONTO_ENTRADA + " DECIMAL(12,4), "+
					
					SALIDA + " CHAR(4), "+
					TIPO_UNIMED_SALIDA + " INTEGER, "+					
					CANTIDAD_SALIDA + " INTEGER, " +
					
					ACUMULADO + " INTEGER, "+
					CANTIDAD_DISPONIBLE + " INTEGER "+
					MONTO_DISPONIBLE + " DECIMAL(12,4), "+
					FLAG + " INTEGER "+
				" );";		
	}
	
	
	public static final class Tb_promocion_detalle implements BaseColumns {

		private Tb_promocion_detalle() {
		}

		public static final String TAG = "tb_promocion_detalle";

		public static final String SECUENCIA = "secuencia";
		public static final String GENERAL = "general";
		public static final String PROMOCION = "promocion";
		public static final String CODALM = "codalm";
		public static final String TIPO = "tipo";
		public static final String ITEM = "item";
		public static final String AGRUPADO = "agrupado";
		public static final String ENTRADA = "entrada";
		public static final String MONTO = "monto";
		public static final String CONDICION = "condicion";
		public static final String CANT_CONDICION = "cant_condicion";
		public static final String SALIDA = "salida";
		public static final String CANT_PROMOCION = "cant_promocion";
		public static final String MAX_PEDIDO = "max_pedido";
		public static final String TOTAL_AGRUPADO = "total_agrupado";
		public static final String CANT_COMP = "cant_comp";

		public static final String CREATE_STATEMENT = "CREATE TABLE " + TAG
				+ " (" + SECUENCIA + " INTEGER PRIMARY KEY, " + GENERAL
				+ " INTEGER, " + PROMOCION + " VARCHAR(40), " + CODALM
				+ " CHAR(2)," + TIPO + " CHAR(1)," + ITEM + " INTEGER,"
				+ AGRUPADO + " INTEGER," + ENTRADA + " CHAR(4)," + MONTO
				+ " NUMERIC(9,4)," + CONDICION + " CHAR(10)," + CANT_CONDICION
				+ " INTEGER," + SALIDA + " CHAR(4)," + CANT_PROMOCION
				+ " INTEGER," + MAX_PEDIDO + " INTEGER," + TOTAL_AGRUPADO
				+ " INTEGER);";

	}

	public static final class Unidad_medida implements BaseColumns {

		private Unidad_medida() {
		}

		public static final String TAG = "unidad_medida";

		public static final String CODUNIMED = "codunimed";
		public static final String DESUNIMED = "desunimed";

	}

	public static final class Vendedor implements BaseColumns {

		private Vendedor() {
		}

		public static final String TAG = "vendedor";

		public static final String CODVEN = "codven";
		public static final String NOMVEN = "nomven";
		public static final String FK_CODUSER = "coduser";
		public static final String FLG_MODIFICAPRECIO = "flg_modificaPrecio";
		public static final String EMAIL = "email";
		public static final String telefono = "telefono";
		public static final String text_area = "text_area";

		public static final String CREATE_STATEMENT = "CREATE TABLE " + TAG
				+ " (" + CODVEN + " CHAR(6) PRIMARY KEY, " + NOMVEN
				+ " VARCHAR(100)," + FK_CODUSER + " INTEGER);";



	}

	public static final class Usuarios implements BaseColumns {

		private Usuarios() {
		}

		public static final String TAG = "usuarios";

		public static final String PK_USECOD = "usecod";
		public static final String USEPAS = "usepas";
		public static final String USENAM = "usenam";
		public static final String USEUSR = "useusr";
		public static final String USESGL = "usesgl";
		public static final String CODIGOC2DM = "codigoc2dm";
		public static final String codigoRol = "codigoRol";

		public static final String CREATE_STATEMENT = "CREATE TABLE " + TAG
				+ " (" + PK_USECOD + " INTEGER PRIMARY KEY, " + USEPAS
				+ " VARCHAR(15)," + USENAM + " VARCHAR(30)," + USEUSR
				+ " VARCHAR(15)," + USESGL + " CHAR(3));";

	}

	public static final class Direccion_cliente implements BaseColumns {

		private Direccion_cliente() {
		}

		public static final String TAG = "direccion_cliente";

		public static final String PK_CODCLI = "codcli";
		public static final String PK_ITEM = "item";
		public static final String DIRECCION = "direccion";
		public static final String TELEFONO = "telefono";
		public static final String CODDEP = "coddep";
		public static final String CODPRV = "codprv";
		public static final String UBIGEO = "ubigeo";
		public static final String DES_CORTA = "des_corta";
		public static final String LATITUD = "latitud";
		public static final String LONGITUD = "longitud";
		public static final String DOC_ADICIONAL = "docAdicional";
		public static final String ESTADO = "estado";
		public static final String altitud = "altitud";

		public static final String CREATE_STATEMENT = "CREATE TABLE " + TAG
				+ " (" + PK_CODCLI + " CHAR(8)," + PK_ITEM + " INTEGER,"
				+ DIRECCION + " VARCHAR(120)," + TELEFONO + " VARCHAR(50),"
				+ CODDEP + " CHAR(2)," + CODPRV + " CHAR(4)," + UBIGEO
				+ " CHAR(6)," + DES_CORTA + " VARCHAR(40), PRIMARY KEY("
				+ PK_CODCLI + ", " + PK_ITEM + "));";
	}

	public static final class Ctas_xbanco implements BaseColumns {

		private Ctas_xbanco() {
		}

		public static final String TAG = "ctas_xbanco";

		public static final String SECUENCIA = "secuencia";
		public static final String CODBAN = "codban";
		public static final String ITEM = "item";
		public static final String CODMON = "codmon";
		public static final String CTA_CTE = "cta_cte";
		public static final String ESTADO = "estado";
		public static final String CUENTA = "cuenta";
		
		public static final String CREATE_STATEMENT = "CREATE TABLE " + TAG
				+ " (" + SECUENCIA + " INTEGER," + CODBAN + " CHAR(2)," + ITEM
				+ " INTEGER," + CODMON + " CHAR(2)," + CTA_CTE + " CHAR(30),"
				+ ESTADO + " CHAR(10), PRIMARY KEY(" + SECUENCIA + "));";
	}

	public static final class Depositos implements BaseColumns {

		private Depositos() {
		}

		public static final String TAG = "depositos";

		public static final String SECUENCIA = "secuencia";
		public static final String ID_BANCO = "id_banco";
		public static final String ID_NUM_CTA = "id_num_cta";
		public static final String FECHA = "fecha";
		public static final String NUM_OPE = "num_ope";
		public static final String MONEDA = "moneda";
		public static final String MONTO = "monto";
		public static final String ESTADO = "estado";
		public static final String CODVEN = "codven";
		public static final String DT_DEPO_FECHASERVIDOR = "DT_DEPO_FECHASERVIDOR";
		public static final String BI_DEPO_FLAG = "BI_DEPO_FLAG";
		public static final String TXT_DEPO_FECHA_REGISTRO = "TXT_DEPO_FECHA_REGISTRO";

		public static final String CREATE_STATEMENT = "CREATE TABLE " + TAG
				+ " (" + SECUENCIA + " INTEGER," + ID_BANCO + " CHAR(2),"
				+ ID_NUM_CTA + " INTEGER," + FECHA + " CHAR(10)," + NUM_OPE
				+ " CHAR(18)," + MONEDA + " CHAR(2), PRIMARY KEY(" + SECUENCIA
				+ "));";

	}

	public static final class Motivo_noventa implements BaseColumns {

		private Motivo_noventa() {
		}

		public static final String TAG = "motivo_noventa";

		public static final String COD_NOVENTA = "cod_noventa";
		public static final String DES_NOVENTA = "des_noventa";

		public static final String CREATE_STATEMENT = "CREATE TABLE " + TAG
				+ " (" + COD_NOVENTA + " INTEGER," + DES_NOVENTA
				+ " VARCHAR(150), PRIMARY KEY(" + COD_NOVENTA + "));";

	}

	public static final class Banco implements BaseColumns {

		private Banco() {
		}

		public static final String TAG = "banco";

		public static final String CODBAN = "codban";
		public static final String NOMBAN = "nomban";

		public static final String CREATE_STATEMENT = "CREATE TABLE " + TAG
				+ " (" + CODBAN + " CHAR(2)," + NOMBAN
				+ " VARCHAR(100), PRIMARY KEY(" + CODBAN + "));";

	}
	
	
	

	public static final class Politica_vendedor implements BaseColumns {

		private Politica_vendedor() {
		}

		public static final String TAG = "politica_vendedor";

		public static final String CODVEN = "codven";
		public static final String SEC_POLITICA = "sec_politica";
		public static final String FLG_PREDETERMINADO = "flg_predeterminado";

		public static final String CREATE_STATEMENT = "CREATE TABLE " + TAG
				+ " (" + CODVEN + " CHAR(6)," + SEC_POLITICA + " Integer);";

	}

	public static final class AsignacionCuota1 implements BaseColumns {

		private AsignacionCuota1() {
		}

		public static final String TAG = "asignacion_cuota1";

		public static final String SECUENCIA = "secuencia";
		public static final String FECHA_INICIO = "fecha_inicio";
		public static final String TOTAL_DIAS = "total_dias";

		public static final String FECHA_FIN = "fecha_fin";
		public static final String ESTADO = "estado";

		public static final String N_CLIENTES = "n_clientes";
		public static final String N_VENDEDORES = "n_vendedores";
		public static final String ID_LOCAL = "id_local";
		public static final String ID_PC = "id_pc";
		public static final String USECOD = "usecod";
		public static final String NOMCUOTA = "nomcuota";
		public static final String DESCRIPCION = "descripcion";

		public static final String TOTAL_CUOTA = "total_cuota";
		public static final String FECHA_OPERACION = "fecha_operacion";
		public static final String TOTAL_PROYECCION = "total_proyeccion";
		public static final String PROYECCION_DESDE = "proyeccion_desde";
		public static final String PROYECCION_HASTA = "proyeccion_hasta";
		public static final String PORCENTAJE_CRECIMIENTO = "porcentaje_crecimiento";

	}

	public static final class CuotaProducto implements BaseColumns {

		private CuotaProducto() {
		}

		public static final String TAG = "cuota_producto";

		public static final String SECUENCIA = "secuencia";
		public static final String CODPRO = "codpro";
		public static final String CUOTA = "cuota";
		public static final String CANTIDAD = "cantidad";

	}

	public static final class CuotaVendedor implements BaseColumns {

		private CuotaVendedor() {
		}

		public static final String TAG = "cuota_vendedor";

		public static final String SECUENCIA = "secuencia";
		public static final String CODVEN = "codven";
		public static final String CUOTA = "cuota";
		public static final String NOMVEN = "nomven";
		public static final String VENTAS = "ventas";
		public static final String DEVOLUCIONES = "devoluciones";
		public static final String VENTAS_EFECTIVAS = "ventas_efectivas";
		public static final String PORCENTAJEPARTICIPACION = "porcentajeParticipacion";
		public static final String PORCENTAJE_AVANCE = "porcentaje_Avance";
		public static final String NOMCUOTA = "nomcuota";

	}

	public static final class RegistroVentas1 implements BaseColumns {

		private RegistroVentas1() {
		}

		public static final String TAG = "registro_ventas1";

		public static final String TIPO_DOCUMENTO = "tipo_documento";
		public static final String SECUENCIA = "secuencia";
		public static final String FECFAC = "fecfac";
		public static final String CODCLI = "codcli";
		public static final String TIPO_CLIENTE = "tipo_cliente";
		public static final String CODVEN = "codven";
		public static final String SERIE_DOCUMENTO = "serie_documento";
		public static final String NUMERO_DOCUMENTO = "numero_documento";
		public static final String SUB_TOTAL = "sub_total";
		public static final String IGV = "igv";
		public static final String TOTAL = "total";
		public static final String MOTIVO = "motivo";
		public static final String SECUENCIAORIGEN = "secuenciaorigen";

	}

	public static final class RegistrosGenerales implements BaseColumns {

		private RegistrosGenerales() {
		}

		public static final String TAG = "registros_generales";

		public static final String SECUENCIA = "secuencia";
		public static final String DESCRIPCION = "descripcion";
		public static final String ESTADO = "estado";
		public static final String ADJETIVO = "adjetivo";
		public static final String SEC_REF = "sec_ref";

	}

	public static final class RegistroVentas2 implements BaseColumns {

		private RegistroVentas2() {
		}

		public static final String TAG = "registro_ventas2";

		public static final String TIPO_DOCUMENTO = "tipo_documento";
		public static final String SECUENCIA = "secuencia";
		public static final String CODPRO = "codpro";
		public static final String CANTIDAD = "cantidad";
		public static final String PREVEN = "preven";
		public static final String TOTAL = "total";
		public static final String SECUENCIAORIGEN = "secuenciaorigen";
	}

	public static final class Zona implements BaseColumns {

		private Zona() {
		}

		public static final String TAG = "zona";

		public static final String SEC_ZONA = "sec_zona";
		public static final String DESCRIPCION = "descripcion";
		public static final String ESTADO = "estado";
		public static final String COLOR = "color";

	}

	public static final class ZonaXY implements BaseColumns {

		private ZonaXY() {
		}

		public static final String TAG = "zona_xy";

		public static final String CODIGO_ZONA = "codigo_zona";
		public static final String ORDEN = "orden";
		public static final String LATITUD = "latitud";
		public static final String LONGITUD = "longitud";

	}

	public static final class ZnfProgramacionClientes implements BaseColumns {

		private ZnfProgramacionClientes() {
		}

		public static final String TAG = "znf_programacion_clientes";

		public static final String SECUENCIA = "secuencia";
		public static final String CODVEN = "codven";
		public static final String FECHA = "fecha";
		public static final String TIPO = "tipo";
		public static final String FRECUENCIA = "frecuencia";
		public static final String N_DIA = "n_dia";
		public static final String SEC_RUTA = "sec_ruta";
		public static final String SEC_ZONA = "sec_zona";
		public static final String CODCLI = "codcli";
		public static final String ITEM_DIRCLI = "item_dircli";
		public static final String ORDEN_R = "orden_r";
		public static final String ORDEN_Z = "orden_z";
		public static final String ORDEN_C = "orden_c";

	}

	public static final class Ruta implements BaseColumns {

		private Ruta() {
		}

		public static final String TAG = "ruta";

		public static final String SEC_RUTA = "sec_ruta";
		public static final String CODIGO = "codigo";
		public static final String DESCRIPCION = "descripcion";
		public static final String ESTADO = "estado";

	}

	public static final class Promocion_Detalle implements BaseColumns {

		private Promocion_Detalle() {
		}

		public static final String TAG = "promocion_detalle";
		public static final String SECUENCIA = "secuencia";
		public static final String GENERAL = "general";
		public static final String PROMOCION = "promocion";

		public static final String CODALM = "codalm";
		public static final String TIPO = "tipo";

		public static final String ITEM = "item";
		public static final String AGRUPADO = "agrupado";
		public static final String ENTRADA = "entrada";
		public static final String TIPO_UNIMED_ENTRADA = "tipo_unimed_entrada";
		public static final String MONTO_MINIMO = "monto_minimo";
		public static final String MONTO_MAXIMO = "monto_maximo";
		public static final String MONTO = "monto";

		public static final String CONDICION = "condicion";
		public static final String CANT_CONDICION = "cant_condicion";
		public static final String SALIDA = "salida";
		public static final String TIPO_UNIMED_SALIDA = "tipo_unimed_salida";
		public static final String CANT_PROMOCION = "cant_promocion";
		public static final String MAX_PEDIDO = "max_pedido";
		public static final String TOTAL_AGRUPADO = "total_agrupado";

		public static final String TIPO_PROMOCION = "tipo_promocion";
		public static final String VENDEDOR = "vendedor";
		public static final String POLITICA = "politica";

		public static final String ACUMULADO = "acumulado";
		
		public static final String GRUPO = "grupo";
		public static final String FAMILIA = "familia";
		public static final String SUBFAMILIA = "subfamilia";
		public static final String DESCUENTO = "descuento";
		public static final String UBIGEO = "ubigeo";
		

	}

	public static final class Servidor implements BaseColumns {

		private Servidor() {
		}

		public static final String TAG = "Servidor";

		public static final String TX_SERV_servicioWEB = "TX_SERV_servicioWEB";
		public static final String TX_SERV_servidorBD = "TX_SERV_servidorBD";
		public static final String TX_SERV_nombreBD = "TX_SERV_nombreBD";
		public static final String TX_SERV_usuario = "TX_SERV_usuario";
		public static final String TX_SERV_contrasena = "TX_SERV_contrasena";
		public static final String IN_SERV_item = "IN_SERV_item";
		public static final String IN_SERV_codigo_ID = "IN_SERV_codigo_ID";

	}

	public static final class Empresa implements BaseColumns {

		private Empresa() {
		}

		public static final String TAG = "Empresa";

		public static final String IN_EMPR_CODIGO_ID = "IN_EMPR_CODIGO_ID";
		public static final String TX_EMPR_NOMBRE = "TX_EMPR_NOMBRE";
		public static final String TX_EMPR_RUC = "TX_EMPR_RUC";
		public static final String TX_EMPR_NOMBREBD = "TX_EMPR_NOMBREBD";
		public static final String TX_EMPR_USUARIO = "TX_EMPR_USUARIO";
		public static final String TX_EMPR_CONTRASENA = "TX_EMPR_CONTRASENA";
		public static final String TX_EMPR_LATITUD = "TX_EMPR_LATITUD";
		public static final String TX_EMPR_LONGITUD = "TX_EMPR_LONGITUD";
	}

	public static final class Almacenes implements BaseColumns {

		private Almacenes() {
		}

		public static final String TAG = "almacenes";

		public static final String CODIGO_ALMACEN 	= "codalm";
		public static final String DESCRIPCION 		= "desalm";
		public static final String ID_LOCAL 		= "id_local";
		public static final String DIRECCION 		= "direccionAlmacen";
		public static final String DESCRIPCION_CORTA= "descripcionCorta";
		
	}

	public static final class DistribucionAlmacenes implements BaseColumns {

		private DistribucionAlmacenes() {
		}

		public static final String TAG = "distribucion_almacenes";

		public static final String TX_ID_LOCAL = "id_local";
		public static final String TX_CODALM = "codalm";
		public static final String IN_ID_DISALM = "id_disalm";
		public static final String TX_DESCRIPCION = "descripcion";
	}
	
	public static final class FormaPago implements BaseColumns {
		private FormaPago(){}		
		public static final String TAG = "forma_pago";
		public static final String CODIGO = "codforpag";
		public static final String DESCRIPCION = "desforpag";	
		public static final String CODIGO_CLIENTE = "codigoCliente";
		public static final String FLAG_TIPO = "flagTipo";
	}
	
	public static final class NroLetras implements BaseColumns {
		private NroLetras(){}
		public static final String TAG = "NRO_LETRAS";
		public static final String NRO_LETRAS = "Nro_letras";
		public static final String DESCRIPCION = "Descripcion";
	}
	
	/*-------------------------------------- CHEMA --------------------------------------*/
	public static final class RegistrosGeneralesMovil implements BaseColumns {
		private RegistrosGeneralesMovil() {}
		public static final String TAG = "registrosGeneralesMovil";
		public static final String CODIGO = "codigoDescripcion";
		public static final String DESCRIPCION = "descripcion";
		public static final String CODIGO_VALOR = "codigoValor";
		public static final String VALOR = "valor";
	}
	
	public static final class LugarEntrega implements BaseColumns {
		private LugarEntrega() {}
		public static final String TAG = "lugarEntrega";
		public static final String CODIGO_CLIENTE = "codigoCliente";
		public static final String ITEM_SUCURSAL = "itemSucursal";
		public static final String CODIGO_LUGAR = "codigoLugar";
		public static final String DIRECCION = "direccion";
		public static final String INDICADOR_DESPACHO = "indicadorDespacho";
		public static final String INDICADOR_COBRANZA = "indicadorCobranza";
		public static final String DIRECCION_ENTREGA = "direccionEntrega";
		public static final String CODIGO_UBIGEO = "codigoUbigeo";
		public static final String CODIGO_DISTRITO = "codigoDistrito";
	}
	
	public static final class Transporte implements BaseColumns {
		private Transporte() {}
		public static final String TAG = "transporte";
		public static final String CODIGO_CLIENTE = "codigoCliente";
		public static final String ITEM_SUCURSAL = "itemSucursal";
		public static final String CODIGO_TRANSPORTE = "codigoTransporte";
		public static final String DESCRIPCION_TRANSPORTE = "descripcion";		
	}
	
	public static final class Obra implements BaseColumns {
		private Obra() {}
		public static final String TAG = "Obra";
		public static final String CODIGO_CLIENTE = "codigoCliente";
		public static final String CODIGO_VENDEDOR = "codigoVendedor";
		public static final String CODIGO_OBRA = "codigoObra";
		public static final String OBRA = "obra";		
	}
	
	public static final class GrupoProducto implements BaseColumns {
		private GrupoProducto() {}
		public static final String TAG = "GrupoProducto";
		public static final String CODIGO_GRUPO = "codigoGrupo";		
		public static final String DESCRIPCION = "descripcion";		
	}
	
	public static final class Moneda implements BaseColumns {
		private Moneda() {}
		public static final String TAG = "Moneda";
		public static final String CODIGO_MONEDA = "codigoMoneda";		
		public static final String DESCRIPCION = "descripcion";
		public static final String CODIGO_EQUIVALENTE = "codigoEquivalente";

	}
	
	public static final class TipoProducto implements BaseColumns {
		private TipoProducto() {}
		public static final String TAG 			= "tipoProducto";
		public static final String CODIGO_TIPO	= "codigoTipo";		
		public static final String DESCRIPCION 	= "descripcion";
		public static final String COLOR 		= "color";
	}
	
	public static final class Promocion_ubigeo implements BaseColumns {

		private Promocion_ubigeo() {
		}

		public static final String TAG = "promocion_ubigeo";
		public static final String SEC_PROMOCION = "sec_promocion";
		public static final String CODIGO_UBIGEO = "codigoUbigeo";

	}
	
	/*-------------------------------------- CHEMA --------------------------------------*/

	public static final class cta_ingresos_resumen implements BaseColumns {

		private cta_ingresos_resumen() {
		}

		public static final String TAG = "cta_ingresos_resumen";

		public static final String PK_SECUENCIA = "secuencia";
		public static final String CODMON = "codmon";
		public static final String CODDOC = "coddoc";
		public static final String SERIE_DOC = "serie_doc";
		public static final String NUMERO_FACTURA = "numero_factura";
		public static final String TOTAL = "total";
		public static final String ACUENTA = "acuenta";
		public static final String SALDO = "saldo";
		public static final String FECCOM = "feccom";
		public static final String CODCLI = "codcli";
		public static final String USERNAME = "username";
		public static final String FECOPERACION = "fecoperacion";
		public static final String CODVEN = "codven";
		public static final String SALDO_VIRTUAL = "saldo_virtual";
		public static final String  FORMA = "forma";

		public static final String CREATE_STATEMENT = "CREATE TABLE " + TAG
				+ " (" + PK_SECUENCIA + " INTEGER PRIMARY KEY," + CODMON
				+ " CHAR(2)," + CODDOC + " CHAR(2)," + SERIE_DOC + " CHAR(6),"
				+ NUMERO_FACTURA + " CHAR(20)," + TOTAL + " NUMERIC(10,3),"
				+ ACUENTA + " NUMERIC(10,3)," + SALDO + " NUMERIC(10,3),"
				+ FECCOM + " CHAR(10)," + CODCLI + " CHAR(8)," + USERNAME
				+ " VARCHAR(30)," + FECOPERACION + " CHAR(10));";

	}
	
	public static final class Motivo {
		public static final String TAG 		 	 = "Motivo";
		public static final String CODIGO_MOTIVO = "codigoMotivo";
		public static final String MOTIVO 		 = "motivo";		
	}
	
	public static final class Expectativa {
		public static final String TAG 		 	 = "Expectativa";
		public static final String CODIGO_EXPECTATIVA = "codigoExpectativa";
		public static final String EXPECTATIVA 		 = "expectativa";		
	}
	
	public static final class ProductoNoDescuento {
		public static final String TAG 		 	 	= "productoNoDescuento";
		public static final String SECUENCIA 		= "secuencia";
		public static final String CODIGO_PRODUCTO 	= "codigoProducto";		
	}
	
	public static final class FichaCliente {
		public static final String TAG 		 	 	= "fichaCliente";
		public static final String NROANALISIS = "nroAnalisis";
		public static final String TIPO = "tipo";
		public static final String RAZON_SOCIAL = "razonSocial";
		public static final String NOMBRE_COMERCIAL = "nombreComercial";
		public static final String GIRO_NEGOCIO = "giroNegocio";
		public static final String DIRECCION_FISCAL = "direccionFiscal";
		public static final String EMAIL_FACTELEC = "emailFactElec";
		public static final String EMAIL = "email";
		public static final String TELEFONO1 = "telefono1";
		public static final String TELEFONO2 = "telefono2";
		public static final String TELEFONO3 = "telefono3";
		public static final String IMPRESION_ELEC = "impresionElectronica";
		public static final String APERCEPTOR = "aPerceptor";
		public static final String ARETENEDOR = "aRetenedor";
		public static final String CATEGORIA = "categoria";
		public static final String PACK_PEGAMENTO = "packPegamento";
		public static final String FCONSTITUCION = "fConstitucion";
		public static final String FANIVERSARIO = "fAniversario";
		public static final String FREGISTRO = "fRegistro";
		public static final String LIC_MUNICIPAL = "licMunicipal";
		public static final String TIPO_SECTOR = "tipoSector";
		public static final String IBC = "IBC";
		public static final String UNIDAD_NEGOCIO = "unidadNegocio";
		public static final String DESCUENTO = "descuento";
		public static final String SECTOR = "sector";
		public static final String PAIS = "pais";
		public static final String CIIU = "CIIU";
		public static final String NRUC_EXTERIOR = "nRucExterior";
		public static final String TIPOLOGIA = "Tipologia";
		public static final String PROCEDENCIA = "procedencia";
		public static final String LOCAL = "local";
		public static final String VINCULADA = "vinculada";
		public static final String MUESTRARIO = "muestrario";
		public static final String INFOCORP = "infocorp";
		public static final String MONEDA_FACTURACION = "monedaFacturacion";
		public static final String MONEDA_LIMITE_CREDITO = "monedaLimiteCredito";
		public static final String LIMITE_CREDITO = "limiteCredito";
		public static final String DIAS_PLAZO = "diasPlazo";
		public static final String TIENE_SUCURSALES = "tieneSucursales";
		public static final String ESTADO = "estado";
	}

	public static final class Turno implements BaseColumns {

		private Turno() {
		}

		public static final String TAG = "turno";

		public static final String CODTURNO = "CodTurno";
		public static final String TURNO = "Turno";

		public static final String CREATE_STATEMENT = "CREATE TABLE " + TAG
				+ " (" + CODTURNO + " CHAR(2)," + TURNO
				+ " VARCHAR(30), PRIMARY KEY(" + CODTURNO + "));";

	}
	
	public static final class BonificacionColores implements BaseColumns {

		private BonificacionColores() {
		}

		public static final String TAG = "bonificacion_colores";

		public static final String SECUENCIA = "secuencia";
		public static final String ITEM = "item";
		public static final String CIP = "cc_artic";
		public static final String PRODUCTO = "Producto";

	}
	
	public static final class Detalle_Entrega implements BaseColumns {

		private Detalle_Entrega() {
		}

		public static final String TAG = "pedido_detalle_entrega";

		public static final String OC_NUMERO = "oc_numero";
		public static final String CIP = "cip";
		public static final String NRO_ENTREGA= "Nro_Entrega";
		public static final String FECHA_ENTREGA = "dt_fechaEntrega";
		public static final String CANTIDAD = "cantidad";
		public static final String CODTURNO= "CodTurno";
		public static final String ESTADO = "estado";
		
		/*public static final String CREATE_STATEMENT = "CREATE TABLE " + TAG
				+ " (" + CODTURNO + " CHAR(2)," + TURNO
				+ " VARCHAR(30), PRIMARY KEY(" + CODTURNO + "));";*/

	}
	
	public static final class Pedido_Detalle_Promocion implements BaseColumns{
		
		private Pedido_Detalle_Promocion(){}
		
		public static final String TAG= "pedido_detalle_promocion";
		
		public static final String OC_NUMERO="oc_numero";
		public static final String CIP= "cip";
		public static final String CC_ARTIC="cc_artic";
		public static final String SECUENCIA="secuencia";
		public static final String CANTIDAD="cantidad";
		public static final String DESCRIPCION="descripcion";
	}
	public static final class San_Visitas implements BaseColumns {
		public static final String TAG = "SAN_VISITAS";
		public static final String TAG_TEMPORAL = TAG+"_TEMP";

		public static final String  ID ="id";
		public static final String  Grupo_Campaña ="Grupo_Campaña";
		public static final String  Cod_Promotor ="Cod_Promotor";
		public static final String  Promotor ="Promotor";
		public static final String  Cod_Colegio ="Cod_Colegio";
		public static final String  descripcion_Colegio ="descripcion_Colegio";
		public static final String  Ejecutivo_Descripcion ="Ejecutivo_Descripcion";
		public static final String  cargo_Descripción ="cargo_Descripción";
		public static final String  Fecha_planificada ="Fecha_planificada";
		public static final String  Fecha_Ejecutada ="Fecha_Ejecutada";
		public static final String  Fecha_proxima_visita ="Fecha_proxima_visita";
		public static final String  Hora_inicio_ejecución ="Hora_inicio_ejecución";
		public static final String  Hora_Fin_Ejecución ="Hora_Fin_Ejecución";
		public static final String  fecha_de_modificación ="fecha_de_modificación";
		public static final String  Estado ="Estado";
		public static final String  Actividad ="Actividad";
		public static final String  Detalle ="Detalle";
		public static final String  Actividad_Proxima ="Actividad_Proxima";
		public static final String  Detalle_Proximo ="Detalle_Proximo";
		public static final String  Comentario_actividad ="Comentario_actividad";
		public static final String  id_rrhh ="id_rrhh";
		public static final String  tipo_visita ="tipo_visita";
		public static final String  latitud ="latitud";
		public static final String  longitud ="longitud";
		public static final String  distancia ="distancia";
		public static final String  oc_numero_visitado ="oc_numero_visitado";
		public static final String  oc_numero_visitar ="oc_numero_visitar";
		public static final String  item ="item";
		public static final String  id_contacto ="id_contacto";
		public static final String  altitud ="altitud";
		public static final String  descripcion_anulacion ="descripcion_anulacion";

		public static final String CREATE_TABLE_with_PK=
				"CREATE TABLE \""+TAG+"\" (\n" +
						"\""+ID+"\" INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
						"\""+Grupo_Campaña+"\" TEXT,\n" +
						"\""+Cod_Promotor+"\" TEXT,\n" +
						"\""+Promotor+"\" TEXT,\n" +
						"\""+Cod_Colegio+"\" TEXT,\n" +
						"\""+descripcion_Colegio+"\" TEXT,\n" +
						"\""+Ejecutivo_Descripcion+"\" TEXT,\n" +
						"\""+cargo_Descripción+"\" TEXT,\n" +
						"\""+Fecha_planificada+"\" TEXT,\n" +
						"\""+Fecha_Ejecutada+"\" TEXT,\n" +
						"\""+Fecha_proxima_visita+"\" TEXT,\n" +
						"\""+Hora_inicio_ejecución+"\" TEXT,\n" +
						"\""+Hora_Fin_Ejecución+"\" TEXT,\n" +
						"\""+fecha_de_modificación+"\" TEXT,\n" +
						"\""+Estado+"\" TEXT,\n" +
						"\""+Actividad+"\" TEXT,\n" +
						"\""+Detalle+"\" TEXT,\n" +
						"\""+Actividad_Proxima+"\" TEXT,\n" +
						"\""+Detalle_Proximo+"\" TEXT,\n" +
						"\""+Comentario_actividad+"\" TEXT,\n" +
						"\""+id_rrhh+"\" TEXT,\n" +
						"\""+tipo_visita+"\" TEXT,\n" +
						"\""+latitud+"\" TEXT,\n" +
						"\""+longitud+"\" TEXT,\n" +
						"\""+distancia+"\" INTEGER,\n" +
						"\""+oc_numero_visitado+"\" TEXT,\n" +
						"\""+oc_numero_visitar+"\" TEXT,\n" +
						"\""+id_contacto+"\" INTEGER\n" +
						")";
		public static final String READ_TABLE_sin_PK=
				" \n" +
						Grupo_Campaña+" ,\n" +
						Cod_Promotor+" ,\n" +
						Promotor+" ,\n" +
						Cod_Colegio+" ,\n" +
						descripcion_Colegio+" ,\n" +
						Ejecutivo_Descripcion+" ,\n" +
						cargo_Descripción+" ,\n" +
						Fecha_Ejecutada+" ,\n" +
						Fecha_proxima_visita+" ,\n" +
						Hora_inicio_ejecución+" ,\n" +
						Hora_Fin_Ejecución+" ,\n" +
						fecha_de_modificación+" ,\n" +
						Estado+" ,\n" +
						Actividad+" ,\n" +
						Detalle+" ,\n" +
						Actividad_Proxima+" ,\n" +
						Detalle_Proximo+" ,\n" +
						Comentario_actividad+" ,\n" +
						id_rrhh+" ,\n" +
						tipo_visita+" ,\n" +
						latitud+" ,\n" +
						longitud+" ,\n" +
						distancia+" ,\n" +
						oc_numero_visitado+", \n" +
						oc_numero_visitar+" \n" +
						" ";
		public static final String READ_TABLE_with_PK=
				" "+
						Grupo_Campaña+" ,\n" +
						Cod_Promotor+" ,\n" +
						Promotor+" ,\n" +
						Cod_Colegio+" ,\n" +
						descripcion_Colegio+" ,\n" +
						Ejecutivo_Descripcion+" ,\n" +
						cargo_Descripción+" ,\n" +
						Fecha_Ejecutada+" ,\n" +
						Fecha_proxima_visita+" ,\n" +
						Hora_inicio_ejecución+" ,\n" +
						Hora_Fin_Ejecución+" ,\n" +
						fecha_de_modificación+" ,\n" +
						Estado+" ,\n" +
						Actividad+" ,\n" +
						Detalle+" ,\n" +
						Actividad_Proxima+" ,\n" +
						Detalle_Proximo+" ,\n" +
						Comentario_actividad+" ,\n" +
						id_rrhh+" ,\n" +
						tipo_visita+" ,\n" +
						latitud+" ,\n" +
						longitud+" ,\n" +
						distancia+" ,\n" +
						oc_numero_visitado+" , \n" +
						oc_numero_visitar+" \n" +
						" FROM "+TAG;


	}

	public static final class San_Opciones implements BaseColumns {

		public static final String TAG = "san_opciones";
		public static final String id_opcion = "id_opcion";
		public static final String codigo_crm = "codigo_crm";
		public static final String instancia = "instancia";
		public static final String opciones = "opciones";
	}

	public static final class Menu_opciones_app {

		private Menu_opciones_app() {
		}

		public static final String TAG = "menu_opciones_app";
		public static final String codigoOpcion = "codigoOpcion";
		public static final String pantallas = "pantallas";
		public static final String opciones = "opciones";
		public static final String descripcion = "descripcion";
	}
	public static final class Roles_accesos_app {

		private Roles_accesos_app() {
		}

		public static final String TAG = "roles_accesos_app";
		public static final String idRol = "idRol";
		public static final String codigoRol = "codigoRol";
		public static final String codigoOpcion = "codigoOpcion";
	}

}
