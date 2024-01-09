package com.example.sm_tubo_plast.genesys.DAO;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.sm_tubo_plast.genesys.BEAN.DataCabeceraPDF;
import com.example.sm_tubo_plast.genesys.BEAN.ReportePedidoDetallePDF;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.PedidosActivity;
import com.example.sm_tubo_plast.genesys.util.VARIABLES;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;

public class DAO_ReportePedido extends SQLiteAssetHelper {

    Context context;

    public DAO_ReportePedido(Context context) {
        super(context, VARIABLES.ConfigDatabase.getDatabaseName(), null, VARIABLES.ConfigDatabase.getDatabaseVersion());
        this.context = context;
    }

    @SuppressLint("Range")
    public ArrayList<ReportePedidoDetallePDF> getAllDataByCodigo(String oc_num)
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
                                "P.despro, " +
                                "PD.cantidad, " +
                                "PD.unidad_medida, " +
                                "PD.precio_bruto, " +
                                "ifnull(PD.porcentaje_desc, 0) as porcentaje_desc," +
                                "PD.peso_bruto," +
                                "ifnull(PD.porcentaje_desc_extra, 0) as porcentaje_desc_extra, " +
                                "PD.precio_neto " +
                                "FROM  pedido_detalle PD " +
                                "CROSS JOIN  producto P " +
                                "WHERE " +
                                "PD.oc_numero = '" + oc_num + "' " +
                                "AND " +
                                "(PD.cip = P.codpro or substr(PD.cip,2, length(PD.cip) ) = P.codpro) ",
                        null);
                if (objCursor.getCount() != 0)
                {
                    while (objCursor.moveToNext())
                    {

                        String oc_numero = objCursor.getString(objCursor.getColumnIndex("oc_numero"));
                        String codpro = objCursor.getString(objCursor.getColumnIndex("codpro"));
                        int cantidad = Integer.parseInt(objCursor.getString(objCursor.getColumnIndex("cantidad")));
                        String unidad_medida = objCursor.getString( objCursor.getColumnIndex("unidad_medida"));
                        String despro = objCursor.getString(objCursor.getColumnIndex("despro"));
                        String precio_bruto = objCursor.getString(objCursor.getColumnIndex("precio_bruto"));
                        String precio_neto = objCursor.getString(objCursor.getColumnIndex("precio_neto"));
                        double pesoTotalProducto = objCursor.getDouble(objCursor.getColumnIndex("peso_bruto"));
                        String porcentaje_desc = objCursor.getString(   objCursor.getColumnIndex("porcentaje_desc"));
                        double porcentaje_desc_extra = objCursor.getDouble(objCursor.getColumnIndex("porcentaje_desc_extra"));

                        objDbPedidoCabeceraDetalleArrayList.add(
                                new ReportePedidoDetallePDF(
                                        oc_numero,
                                        "",
                                        codpro,
                                        cantidad,
                                        unidad_medida,
                                        despro,
                                        precio_bruto,
                                        precio_neto,
                                        porcentaje_desc,
                                        porcentaje_desc_extra,
                                        pesoTotalProducto
                                ));
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
                    "PC.diasVigencia " +
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
