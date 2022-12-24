package com.example.sm_tubo_plast.genesys.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.sm_tubo_plast.genesys.datatypes.DBPedido_Cabecera;
import com.example.sm_tubo_plast.genesys.datatypes.DBPedido_Detalle;
import com.example.sm_tubo_plast.genesys.datatypes.DBtables;
import com.example.sm_tubo_plast.genesys.service.ConnectionDetector;
import com.example.sm_tubo_plast.genesys.util.VARIABLES;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.Calendar;

@SuppressLint("LongLogTag")
public class DAO_Pedido extends SQLiteAssetHelper{
	
	private static final String TAG = "DAO_PedidoDetalle";

	public static final String KEY_ROWID = "_id";
	public String codAlmacen = "01";
	// flag for Internet connection status
	Boolean isInternetPresent = false;

	// Connection detector class
	ConnectionDetector cd;

	private Context _context;
	Calendar calendar = Calendar.getInstance();

	public DAO_Pedido(Context context) {
		super(context, VARIABLES.ConfigDatabase.getDatabaseName(), null, VARIABLES.ConfigDatabase.getDatabaseVersion());
		// TODO Auto-generated constructor stub
	}
	

	public int getCantidadItemPedido(String oc_numero, String codigoProducto){
		String rawQuery = "SELECT cantidad from pedido_detalle where oc_numero='"+oc_numero+"' and cip like '"+codigoProducto+"'";
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		
		int cantidad = 0;
		
		cur.moveToFirst();				
		while (!cur.isAfterLast()) {
			cantidad = cur.getInt(0);		
			Log.d("BDclasses ::getCantidadItemPedido::","oc_numero "+oc_numero+" cip "+codigoProducto);			
			cur.moveToNext();			
		}
		
		return cantidad;
	}
	
	public void Eliminar_itemPedidoBonificacion(String codprod, String oc_numero) {

		String where = "oc_numero = ? and cip = ? and tipo_producto in ('C','M')";
		String[] args = { oc_numero, codprod};

		try {

			SQLiteDatabase db = getWritableDatabase();
			db.delete("pedido_detalle", where, args);
			db.close();
			
			Log.i("ELIMINAR ITEM PEDIDO BONIF",oc_numero+"  "+codprod);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	public void Modificar_CantidadItemPedido(String oc_numero, String codigoProducto, int cantidad){
		String where = " oc_numero = ? and cip = ? ";
		String[] args = { oc_numero, codigoProducto };

		try {

			SQLiteDatabase db = getWritableDatabase();

			ContentValues reg = new ContentValues();
			reg.put("cantidad", cantidad);			
			
			db.update("pedido_detalle", reg, where, args);
			db.close();
			Log.i("MODIFICAR CANTIDAD ITEM PEDIDO", oc_numero+"  "+codigoProducto);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	public void ActualizarPedidoCabecera(DBPedido_Cabecera cabecera) {
		String where = "oc_numero = ?";
		String[] args = { cabecera.getOc_numero() };
		Log.d(TAG, "ActualizarPedidoCabecera:ocnumero:"+cabecera.getOc_numero());
		try {
			SQLiteDatabase db = getWritableDatabase();

			ContentValues reg = new ContentValues();
			reg.put(DBtables.Pedido_cabecera.TIPODOCUMENTO, cabecera.getTipoDocumento());
			reg.put(DBtables.Pedido_cabecera.MONEDA, cabecera.getMoneda());
			reg.put(DBtables.Pedido_cabecera.FECHA_MXE, cabecera.getFecha_mxe());			
			reg.put(DBtables.Pedido_cabecera.NRO_ORDEN_COMPRA, cabecera.getNumeroOrdenCompra());
			//reg.put(DBtables.Pedido_cabecera.CODIGO_TURNO_ENTREGA, cabecera.getCodigoTurnoEntrega());
			reg.put(DBtables.Pedido_cabecera.CODIGO_PRIORIDAD, cabecera.getCodigoPrioridad());
			reg.put(DBtables.Pedido_cabecera.CODIGO_PUNTO_ENTREGA, cabecera.getCodigoPuntoEntrega());
			reg.put(DBtables.Pedido_cabecera.CODIGO_TIPO_DESPACHO, cabecera.getCodigoTipoDespacho());			
			reg.put(DBtables.Pedido_cabecera.CODIGO_OBRA, cabecera.getCodigoObra());
			reg.put(DBtables.Pedido_cabecera.FLAG_DESPACHO, cabecera.getFlagDespacho());
			reg.put(DBtables.Pedido_cabecera.FLAG_EMBALAJE, cabecera.getFlagEmbalaje());
			reg.put(DBtables.Pedido_cabecera.FLAG_PEDIDOANTICIPO, cabecera.getFlagPedido_Anticipo());
			reg.put(DBtables.Pedido_cabecera.CODIGO_TRANSPORTISTA, cabecera.getCodigoTransportista());
			reg.put(DBtables.Pedido_cabecera.CODIGO_TURNO, cabecera.getCodTurno());
			reg.put(DBtables.Pedido_cabecera.NRO_LETRA, cabecera.getNroletra());
			Log.w("xxxxxxxxxxxxxxxxxxxxxxxxxxx", "CODTURNO: "+cabecera.getCodTurno());

			////_okkk
//			if (!cabecera.getTipoRegistro().equals(PedidosActivity.TIPO_COTIZACION)) {
			if (!cabecera.getTipoRegistro().equals("")) {
				reg.put(DBtables.Pedido_cabecera.DIAS_VIGENCIA, cabecera.getDiasVigencia());
			}
			////_okkk
//			if (!cabecera.getTipoRegistro().equals(PedidosActivity.TIPO_DEVOLUCION)) {
			if (!cabecera.getTipoRegistro().equals("")) {
				//Si no es devolucion se puede modificar las observaciones, de otra forma no, ya que los campos observaciones guardan el codigo de recojo y genera cambio
				reg.put(DBtables.Pedido_cabecera.OBSERV, cabecera.getObservacion());
				reg.put(DBtables.Pedido_cabecera.OBSERVACION2, cabecera.getObservacion2());
				reg.put(DBtables.Pedido_cabecera.OBSERVACION3, cabecera.getObservacion3());
				reg.put(DBtables.Pedido_cabecera.OBSERVACION4, cabecera.getObservacion4());
			}
			
			reg.put(DBtables.Pedido_cabecera.FLAG, cabecera.getFlag());
						
			db.update("pedido_cabecera", reg, where, args);
			db.close();			
			Log.i(TAG+":ActualizarPedidoCabecera:", "pedido_cabecera actualizada");

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	public ArrayList<DBPedido_Detalle> getPedidoDetalle(String oc_numero){
		String rawQuery = "SELECT * FROM pedido_detalle WHERE oc_numero like '"+oc_numero+"'";
		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.rawQuery(rawQuery, null);
		
		ArrayList<DBPedido_Detalle> lista = new ArrayList<>();		
		cur.moveToFirst();				
		while (!cur.isAfterLast()) {
			DBPedido_Detalle item = new DBPedido_Detalle();
			item.setOc_numero(cur.getString(0));
			item.setEan_item(cur.getString(1));
			item.setCip(cur.getString(2));
			item.setPrecio_bruto(cur.getString(3));
			item.setPrecio_neto(cur.getString(4));
			item.setPercepcion(cur.getString(5));
			item.setCantidad(cur.getInt(6));
			item.setTipo_producto(cur.getString(7));
			item.setUnidad_medida(cur.getString(8));
			item.setPeso_bruto(cur.getString(9));
			item.setFlag(cur.getString(10));
			item.setCod_politica(cur.getString(11));
			item.setSec_promo(cur.getString(12));
			item.setItem_promo(cur.getInt(13));
			item.setAgrup_promo(cur.getInt(14));
			item.setItem(cur.getInt(15));
			item.setPrecioLista(cur.getString(16));
			item.setDescuento(cur.getString(17));
			item.setLote(cur.getString(18));
			item.setPorcentaje_desc(cur.getDouble(cur.getColumnIndex("porcentaje_desc")));
			lista.add(item);
			Log.d(TAG,"getPedidoDetalle: "+oc_numero+" cip "+item.getCip());			
			cur.moveToNext();			
		}		
		return lista;
	}
	
	//Se clona el pedido cuando es una cotizacion para generar una nueva versi�n
	public void ClonarPedido(DBPedido_Cabecera cabecera) {		
		try {
			SQLiteDatabase db = getWritableDatabase();

			ContentValues Nreg = new ContentValues();
			Nreg.put(DBtables.Pedido_cabecera.PK_OC_NUMERO, cabecera.getOc_numero());
			Nreg.put(DBtables.Pedido_cabecera.SITIO_ENFA, cabecera.getSitio_enfa());
			Nreg.put(DBtables.Pedido_cabecera.MONTO_TOTAL, cabecera.getMonto_total());
			Nreg.put(DBtables.Pedido_cabecera.PERCEPCION_TOTAL, cabecera.getPercepcion_total());
			Nreg.put(DBtables.Pedido_cabecera.VALOR_IGV, cabecera.getValor_igv());
			Nreg.put(DBtables.Pedido_cabecera.MONEDA, cabecera.getMoneda());
			Nreg.put(DBtables.Pedido_cabecera.FECHA_OC, cabecera.getFecha_oc());
			Nreg.put(DBtables.Pedido_cabecera.FECHA_MXE, cabecera.getFecha_mxe());
			Nreg.put(DBtables.Pedido_cabecera.COND_PAGO, cabecera.getCond_pago());
			Nreg.put(DBtables.Pedido_cabecera.COD_CLI, cabecera.getCod_cli());
			Nreg.put(DBtables.Pedido_cabecera.COD_EMP, cabecera.getCod_emp());
			Nreg.put(DBtables.Pedido_cabecera.ESTADO, cabecera.getEstado());
			Nreg.put(DBtables.Pedido_cabecera.USERNAME, cabecera.getUsername());
			Nreg.put(DBtables.Pedido_cabecera.RUTA, cabecera.getRuta());
			Nreg.put(DBtables.Pedido_cabecera.OBSERV, cabecera.getObserv());
			Nreg.put(DBtables.Pedido_cabecera.COD_NOVENTA, cabecera.getCod_noventa());
			Nreg.put(DBtables.Pedido_cabecera.PESO_TOTAL, cabecera.getPeso_total());
			Nreg.put(DBtables.Pedido_cabecera.FLAG, cabecera.getFlag());
			Nreg.put(DBtables.Pedido_cabecera.LATITUD, cabecera.getLatitud());
			Nreg.put(DBtables.Pedido_cabecera.LONGITUD, cabecera.getLongitud());
			Nreg.put(DBtables.Pedido_cabecera.CODIGO_FAMILIAR, cabecera.getCodigo_familiar());
			Nreg.put(DBtables.Pedido_cabecera.DT_PEDI_FECHASERVIDOR,cabecera.getDT_PEDI_FECHASERVIDOR());
			
			Nreg.put(DBtables.Pedido_cabecera.TOTAL_SUJETO_PERCEPCION,cabecera.getTotalSujetopercepcion());
			Nreg.put(DBtables.Pedido_cabecera.NRO_ORDEN_COMPRA, cabecera.getNumeroOrdenCompra());
			Nreg.put(DBtables.Pedido_cabecera.CODIGO_PRIORIDAD, cabecera.getCodigoPrioridad());
			Nreg.put(DBtables.Pedido_cabecera.CODIGO_SUCURSAL, cabecera.getCodigoSucursal());
			Nreg.put(DBtables.Pedido_cabecera.CODIGO_PUNTO_ENTREGA, cabecera.getCodigoPuntoEntrega());
			Nreg.put(DBtables.Pedido_cabecera.CODIGO_TIPO_DESPACHO, cabecera.getCodigoTipoDespacho());
			Nreg.put(DBtables.Pedido_cabecera.FLAG_EMBALAJE, cabecera.getFlagEmbalaje());
			Nreg.put(DBtables.Pedido_cabecera.FLAG_PEDIDOANTICIPO, cabecera.getFlagPedido_Anticipo());
			Nreg.put(DBtables.Pedido_cabecera.CODIGO_TRANSPORTISTA, cabecera.getCodigoTransportista());
			Nreg.put(DBtables.Pedido_cabecera.CODIGO_ALMACEN, cabecera.getCodigoAlmacen());
			Nreg.put(DBtables.Pedido_cabecera.OBSERVACION2, cabecera.getObservacion2());
			Nreg.put(DBtables.Pedido_cabecera.OBSERVACION3, cabecera.getObservacion3());
			Nreg.put(DBtables.Pedido_cabecera.OBSERVACION_DESCUENTO, cabecera.getObservacionDescuento());
			Nreg.put(DBtables.Pedido_cabecera.OBSERVACION_TIPO_PROD, cabecera.getObservacionTipoProducto());
			Nreg.put(DBtables.Pedido_cabecera.FLAG_DESCUENTO, cabecera.getFlagDescuento());
			Nreg.put(DBtables.Pedido_cabecera.CODIGO_OBRA, cabecera.getCodigoObra());
			Nreg.put(DBtables.Pedido_cabecera.FLAG_DESPACHO, cabecera.getFlagDespacho());
			Nreg.put(DBtables.Pedido_cabecera.DOC_ADICIONAL, cabecera.getDocAdicional());
			Nreg.put(DBtables.Pedido_cabecera.SUBTOTAL, cabecera.getSubTotal());
			Nreg.put(DBtables.Pedido_cabecera.TIPODOCUMENTO, cabecera.getTipoDocumento());
			Nreg.put(DBtables.Pedido_cabecera.TIPO_REGISTRO, cabecera.getTipoRegistro());
			Nreg.put(DBtables.Pedido_cabecera.DIAS_VIGENCIA, cabecera.getDiasVigencia());
			Nreg.put(DBtables.Pedido_cabecera.PEDIDO_ANTERIOR, cabecera.getPedidoAnterior());
			
			db.insert("pedido_cabecera", null, Nreg);
			db.close();
			Log.w(TAG, "ClonarPedido: cabecera clonada");
			
			ArrayList<DBPedido_Detalle> listaDetalle = getPedidoDetalle(cabecera.getPedidoAnterior());//Se toma los productos del pedido anterior
			for (int i = 0; i < listaDetalle.size(); i++) {
				//La lista a clonar aun mantiene el ocnumero del pedido anterior, se debe acambiar por el actual
				DBPedido_Detalle dbPedido_Detalle = listaDetalle.get(i);
				dbPedido_Detalle.setOc_numero(cabecera.getOc_numero());
				ClonarPedidoDetalle(dbPedido_Detalle);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.i("PEDIDO_CABECERA", "Error registro insertado");
		}
	}
	
	public void ClonarPedidoDetalle(DBPedido_Detalle item) {		
		try {
			SQLiteDatabase db = getWritableDatabase();

			ContentValues Nreg = new ContentValues();
			Nreg.put(DBtables.Pedido_detalle.PK_OC_NUMERO, item.getOc_numero());
			Nreg.put(DBtables.Pedido_detalle.PK_EAN_ITEM, item.getEan_item());
			Nreg.put(DBtables.Pedido_detalle.CIP, item.getCip());
			Nreg.put(DBtables.Pedido_detalle.PRECIO_BRUTO, item.getPrecio_bruto());
			Nreg.put(DBtables.Pedido_detalle.PRECIO_NETO, item.getPrecio_neto());
			Nreg.put(DBtables.Pedido_detalle.PERCEPCION, item.getPercepcion());
			Nreg.put(DBtables.Pedido_detalle.CANTIDAD, item.getCantidad());
			Nreg.put(DBtables.Pedido_detalle.TIPO_PRODUCTO, item.getTipo_producto());
			Nreg.put(DBtables.Pedido_detalle.UNIDAD_MEDIDA, item.getUnidad_medida());
			Nreg.put(DBtables.Pedido_detalle.PESO_BRUTO, item.getPeso_bruto());
			Nreg.put(DBtables.Pedido_detalle.FLAG, item.getFlag());
			Nreg.put(DBtables.Pedido_detalle.COD_POLITICA, item.getCod_politica());
			Nreg.put(DBtables.Pedido_detalle.SEC_PROMO, item.getSec_promo());
			Nreg.put(DBtables.Pedido_detalle.ITEM_PROMO, item.getItem_promo());
			Nreg.put(DBtables.Pedido_detalle.AGRUP_PROMO, item.getAgrup_promo());
			Nreg.put(DBtables.Pedido_detalle.ITEM, item.getItem());
			Nreg.put(DBtables.Pedido_detalle.PRECIO_LISTA, item.getPrecioLista());
			Nreg.put(DBtables.Pedido_detalle.DESCUENTO, item.getDescuento());
			Nreg.put(DBtables.Pedido_detalle.PORCENTAJE_DESC, item.getPorcentaje_desc());
			Nreg.put(DBtables.Pedido_detalle.LOTE, item.getLote());
			
			db.insert(DBtables.Pedido_detalle.TAG, null, Nreg);
			db.close();
			Log.i(TAG, "ClonarPedidoDetalle: detalle clonado");			
		} catch (Exception e) {
			e.printStackTrace();
			Log.w("ClonarPedidoDetalle", "Error registro insertado");
		}
	}

}
