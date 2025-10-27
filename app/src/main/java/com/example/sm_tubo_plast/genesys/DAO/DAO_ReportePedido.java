package com.example.sm_tubo_plast.genesys.DAO;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.sm_tubo_plast.genesys.BEAN.DataCabeceraPDF;
import com.example.sm_tubo_plast.genesys.BEAN.Pedido_detalle2;
import com.example.sm_tubo_plast.genesys.CreatePDF.model.ReportePedidoDetallePDF;
import com.example.sm_tubo_plast.genesys.datatypes.DBtables;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.PedidosActivity;
import com.example.sm_tubo_plast.genesys.util.VARIABLES;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;

public class DAO_ReportePedido extends SQLiteAssetHelper {

    Context context;
    DAO_Pedido_detalle2 daopedido2;

    public DAO_ReportePedido(Context context) {
        super(context, VARIABLES.ConfigDatabase.getDatabaseName(), null, VARIABLES.ConfigDatabase.getDatabaseVersion());
        this.context = context;
        daopedido2 = new DAO_Pedido_detalle2(context);
    }

    @SuppressLint("Range")
    public ArrayList<ReportePedidoDetallePDF> getAllDataByCodigo(String oc_num, ArrayList<Integer> listaIndex)
    {
        try
        {
            ArrayList<ReportePedidoDetallePDF> objDbPedidoCabeceraDetalleArrayList = new ArrayList<>();
            SQLiteDatabase objSqLiteDatabase = getReadableDatabase();
            if (objSqLiteDatabase != null)
            {
                Cursor objCursor = objSqLiteDatabase.rawQuery(
                        "SELECT " +
                                "PD.oc_numero," +
                                "PD.cip as codpro, " +
                                "P.codpro as codproOriginal, " +
                                "PD.item, " +
                                "PD.tipo_producto, " +
                                "P.despro, " +
                                "PD.cantidad, " +
                                "PD.unidad_medida, " +
                                "PD.precio_bruto, " +
                                "ifnull(PD.porcentaje_desc, 0) as porcentaje_desc," +
                                "PD.peso_bruto," +
                                "ifnull(PD.porcentaje_desc_extra, 0) as porcentaje_desc_extra, " +
                                "PD.precio_neto," +
                                "PD.descuento, " +
                                "PD.sec_promo, " +
                                "PD.item_promo, " +
                                "PD.sec_promo_prioridad, " +
                                "PD.sec_promo_prioridad " +
                                "FROM  pedido_detalle PD " +
                                "CROSS JOIN  producto P " +
                                "left join "+ DBtables.TB_registro_bonificaciones.TAG+" rb on PD.oc_numero=rb.oc_numero \n" +
                                "and PD.cip=rb.entrada and pd.item=rb.entrada_item\n" +
                                "WHERE " +
                                "PD.oc_numero = '" + oc_num + "' " +
                                //"AND substr(P.codpro,1,5)!='COMBO'  " +
                                "AND (PD.cip = P.codpro or substr(PD.cip,2, length(PD.cip) ) = P.codpro) " +
                                "order by cast(PD.item as INTEGER)+(ifnull(cast(rb.salida_item as INTEGER) , 0)*0.01) asc",
                        null);
                if (objCursor.getCount() != 0)
                {
                    //------------------------ORDERNAR-----------------------------------------------------------------------
                    ArrayList<Integer> lisOrderIndexCursor = new ArrayList<>();
                    for (int i = 0; i < listaIndex.size(); i++) {
                        for (int i1 = 0; i1 < objCursor.getCount(); i1++) {
                            objCursor.moveToPosition(i1);
                            int item = objCursor.getInt(objCursor.getColumnIndex("item"));
                            if(item == listaIndex.get(i)){
                                lisOrderIndexCursor.add(objCursor.getPosition());
                                break;
                            }
                        }
                    }
                    //-----------------------------------------------------------------------------------------------

                    int indexNro = 0;
                    for (int x= 0; x < objCursor.getCount(); x++) {
                        objCursor.moveToPosition(lisOrderIndexCursor.get(x));

                        String oc_numero = objCursor.getString(objCursor.getColumnIndex("oc_numero"));
                        String codpro = objCursor.getString(objCursor.getColumnIndex("codpro"));
                        String codproOriginal = objCursor.getString(objCursor.getColumnIndex("codproOriginal"));
                        int item = objCursor.getInt(objCursor.getColumnIndex("item"));
                        int cantidad = Integer.parseInt(objCursor.getString(objCursor.getColumnIndex("cantidad")));
                        String unidad_medida = objCursor.getString( objCursor.getColumnIndex("unidad_medida"));
                        String _despro = objCursor.getString(objCursor.getColumnIndex("despro"));
                        String precio_bruto = objCursor.getString(objCursor.getColumnIndex("precio_bruto"));
                        String precio_neto = objCursor.getString(objCursor.getColumnIndex("precio_neto"));
                        double pesoTotalProducto = objCursor.getDouble(objCursor.getColumnIndex("peso_bruto"));
                        String porcentaje_desc = objCursor.getString(   objCursor.getColumnIndex("porcentaje_desc"));
                        double porcentaje_desc_extra = objCursor.getDouble(objCursor.getColumnIndex("porcentaje_desc_extra"));
                        double montoDesct = objCursor.getDouble(objCursor.getColumnIndex("descuento"));
                        String tipo_producto = objCursor.getString(objCursor.getColumnIndex("tipo_producto"));


                        String desproOut = _despro+""+VARIABLES.getDescripcionAnPreConcatenarBonif(tipo_producto);
                        if(!codproOriginal.startsWith("COMBO")){
                            indexNro+=1;
                            objDbPedidoCabeceraDetalleArrayList.add(new ReportePedidoDetallePDF(
                                    oc_numero,
                                    String.valueOf(indexNro),
                                    codpro,
                                    cantidad,
                                    unidad_medida,
                                    desproOut,
                                    precio_bruto,
                                    precio_neto,
                                    porcentaje_desc,
                                    porcentaje_desc_extra,
                                    pesoTotalProducto,
                                    montoDesct,
                                    tipo_producto
                            ));
                        }
                        String sec_promo = objCursor.getString(objCursor.getColumnIndex("sec_promo"));
                        int sec_promo_prioridad = objCursor.getInt(objCursor.getColumnIndex("sec_promo_prioridad"));
                        int secPromocion= sec_promo.length()>0? Integer.parseInt(sec_promo): 0;
                        if ((secPromocion<=0 && sec_promo_prioridad<=0) || tipo_producto.equals("V")){
                            continue;
                        }
                        ArrayList<Pedido_detalle2> listaPedido2=daopedido2.getDataView(oc_numero, secPromocion, item);
                        for (int i = 0; i < listaPedido2.size(); i++) {
                            indexNro+=1;
                            Pedido_detalle2 pedDet2 = listaPedido2.get(i);
                            String desproOutDet2 = pedDet2.getItemProducto().getDescripcion()+""+VARIABLES.getDescripcionAnPreConcatenarBonif("C");
                            objDbPedidoCabeceraDetalleArrayList.add(new ReportePedidoDetallePDF(
                                    oc_numero,
                                    String.valueOf(indexNro),
                                    "B"+pedDet2.getCodpro(),
                                    pedDet2.getCantidad(),
                                    pedDet2.getItemProducto().getDesunimed(),
                                    ""+desproOutDet2,
                                    String.valueOf(pedDet2.getPrecio_unit()),
                                    String.valueOf(pedDet2.getPrecio_neto()),
                                    ""+pedDet2.getPctj_desc(),
                                    0+pedDet2.getPctj_extra(),
                                    0+pedDet2.getPeso_total(),
                                    0+pedDet2.getDescuento(),
                                    "C"
                            ));
                        }
                    }
                    objCursor.close();


                    if(VARIABLES.isProduccion_prueba){
                        int forhasta15 = 15 - objDbPedidoCabeceraDetalleArrayList.size();
                        if(forhasta15 > 0){
                            for (int i = 0; i < forhasta15; i++) {
                                objDbPedidoCabeceraDetalleArrayList.add(objDbPedidoCabeceraDetalleArrayList.get(0));
                            }
                        }
                    }
                    return objDbPedidoCabeceraDetalleArrayList;
                }
                else
                {
                    //Toast.makeText(context, "No data is retrieved", Toast.LENGTH_SHORT).show();
                    return null;
                }
            }
            else
            {
                //Toast.makeText(context, "Data is null", Toast.LENGTH_SHORT).show();
                return null;
            }
        }
        catch (Exception e)
        {
            //Toast.makeText(context, "getallData:- " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    @SuppressLint("Range")
    public ArrayList<DataCabeceraPDF> getCabecera(String oc_num)
    {

        oc_num=oc_num.replace(" ", "%");
        ArrayList<DataCabeceraPDF> objDbPedidoCabeceraArrayList = new ArrayList<>();
        SQLiteDatabase objSqLiteDatabase = getReadableDatabase();
        if (objSqLiteDatabase != null)
        {

            String sql="SELECT PC.oc_numero, C.ruccli, " +
                    "V.codven, C.nomcli, " +
                    "C.telefono as telefonoCliente, V.nomven, " +
                    "dc.direccion, C.email as emailCliente, " +
                    "V.email as emailVendedor, T.descripcion, " +
                    "FP.desforpag, PC.monto_total, " +
                    "PC.valor_igv, PC.subTotal as subtotal, " +
                    "PC.peso_total, PC.fecha_oc, " +
                    "PC.fecha_mxe, "+
                    "PC.moneda, "+
                    "V.telefono as telefonoVendedor, V.text_area, " +
                    "PC.observacion, PC.observacion2, " +
                    "PC.observacion3," +
                    "PC.tipoRegistro," +
                    "PC.diasVigencia, " +
                    "PC.dsctoBonificacion as totalDsctEnBonif " +
                    "FROM " +
                    "pedido_cabecera PC " +
                    "CROSS JOIN " +
                    "cliente C " +
                    "CROSS JOIN " +
                    "direccion_cliente dc " +
                    "CROSS JOIN " +
                    "vendedor V " +
                    "CROSS JOIN " +
                    "transporte T " +
                    "CROSS JOIN " +
                    "forma_pago FP " +
                    "WHERE " +
                    "PC.oc_numero like '%" + oc_num + "%' " +
                    "AND " +
                    "PC.cod_cli = C.codcli " +
                    "AND PC.codigoSucursal =  dc.item and c.codcli=dc.codcli " +
                    "AND " +
                    "V.codven = PC.cod_emp " +
                    "AND " +
                    "PC.codigoTransportista = T.codigoTransporte " +
                    "AND " +
                    "C.codcli = T.codigoCliente " +
                    "AND " +
                    "PC.cond_pago = FP.codforpag " +
                    "AND " +
                    "PC.tipoRegistro IN (" +
                    "'"+ PedidosActivity.TIPO_PEDIDO+ "'," +
                    "'"+ PedidosActivity.TIPO_COTIZACION+ "'" +
                    ")";
            Cursor objCursor = objSqLiteDatabase.rawQuery(sql
                    , null);

            if (objCursor.getCount() != 0)
            {
                DataCabeceraPDF cabecera;
                while (objCursor.moveToNext())
                {
                    cabecera=new DataCabeceraPDF();
                    cabecera.setOc_numero(objCursor.getString(objCursor.getColumnIndex("oc_numero")));
                    cabecera.setNomcli(objCursor.getString(objCursor.getColumnIndex("nomcli")));
                    cabecera.setRuccli(objCursor.getString(objCursor.getColumnIndex("ruccli")));
                    cabecera.setCodven(objCursor.getString(objCursor.getColumnIndex("codven")));
                    cabecera.setTelefono(objCursor.getString(objCursor.getColumnIndex("telefonoCliente")));
                    cabecera.setNomven(objCursor.getString(objCursor.getColumnIndex("nomven")));
                    cabecera.setDireccion(objCursor.getString(objCursor.getColumnIndex("direccion")));
                    cabecera.setEmail_cliente(objCursor.getString(objCursor.getColumnIndex("emailCliente")));
                    cabecera.setEmail_vendedor(objCursor.getString(objCursor.getColumnIndex("emailVendedor")));
                    //cabecera.setDescripcion(objCursor.getString(objCursor.getColumnIndex("descripcion")));
                    cabecera.setDesforpag(objCursor.getString(objCursor.getColumnIndex("desforpag")));
                    cabecera.setMonto_total(objCursor.getString(objCursor.getColumnIndex("monto_total")));
                    cabecera.setValor_igv(objCursor.getString(objCursor.getColumnIndex("valor_igv")));
                    cabecera.setSubtotal(objCursor.getString(objCursor.getColumnIndex("subtotal")));
                    cabecera.setPeso_total(objCursor.getString(objCursor.getColumnIndex("peso_total")));
                    cabecera.setFecha_oc(objCursor.getString(objCursor.getColumnIndex("fecha_oc")));
                    cabecera.setFecha_mxe(objCursor.getString(objCursor.getColumnIndex("fecha_mxe")));
                    cabecera.setMoneda(objCursor.getString(objCursor.getColumnIndex("moneda")));
                    cabecera.setTelefono_vendedor(objCursor.getString(objCursor.getColumnIndex("telefonoVendedor")));
                    cabecera.setText_area(objCursor.getString(objCursor.getColumnIndex("text_area")));
                    cabecera.setObservacion(objCursor.getString(objCursor.getColumnIndex("observacion")));
                    cabecera.setObservacion2(objCursor.getString(objCursor.getColumnIndex("observacion2")));
                    cabecera.setObservacion3(objCursor.getString(objCursor.getColumnIndex("observacion3")));
                    cabecera.setTipoRegistro(objCursor.getString(objCursor.getColumnIndex("tipoRegistro")));
                    cabecera.setDiasVigencia(objCursor.getInt(objCursor.getColumnIndex("diasVigencia")));
                    cabecera.setDsctoBonificacion(objCursor.getDouble(objCursor.getColumnIndex("totalDsctEnBonif")));
                    objDbPedidoCabeceraArrayList.add(cabecera);

                }
                return objDbPedidoCabeceraArrayList;
            }
            else
            {
                return new ArrayList<>();
            }
        }
        else
        {

            return null;
        }
    }


}
