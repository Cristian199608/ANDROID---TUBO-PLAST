package com.example.sm_tubo_plast.genesys.DAO;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.sm_tubo_plast.genesys.BEAN.DataCabecera;
import com.example.sm_tubo_plast.genesys.BEAN.ReportePedidoCabeceraDetalle;
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
    public ArrayList<ReportePedidoCabeceraDetalle> getAllDataByCodigo(String oc_num)
    {
        try
        {
            ArrayList<ReportePedidoCabeceraDetalle> objDbPedidoCabeceraDetalleArrayList = new ArrayList<>();
            SQLiteDatabase objSqLiteDatabase = getReadableDatabase();
            if (objSqLiteDatabase != null)
            {
                Cursor objCursor = objSqLiteDatabase.rawQuery(
                        "SELECT PC.oc_numero, C.ruccli, " +
                                "V.codven, C.nomcli, " +
                                "C.telefono, V.nomven, " +
                                "C.direccionFiscal, C.email, " +
                                "V.email, T.descripcion, " +
                                "FP.desforpag, PC.monto_total, " +
                                "PC.valor_igv, PC.subtotal, " +
                                "PC.peso_total, PC.fecha_oc, " +
                                "PC.fecha_mxe,  P.codpro, PD.cantidad, " +
                                "PD.unidad_medida, P.despro, " +
                                "PC.moneda, PD.precio_bruto, " +
                                "PD.precio_neto, " +
                                "ifnull(PD.porcentaje_desc, 0) as porcentaje_desc," +
                                "V.telefono, V.text_area, " +
                                "PC.observacion, PC.observacion2, " +
                                "PC.observacion3," +
                                "PD.peso_bruto," +
                                "ifnull(PD.porcentaje_desc_add, 0) as porcentaje_desc_add " +
                                "FROM " +
                                "pedido_cabecera PC " +
                                "CROSS JOIN " +
                                "cliente C " +
                                "CROSS JOIN " +
                                "vendedor V " +
                                "CROSS JOIN " +
                                "transporte T " +
                                "CROSS JOIN " +
                                "forma_pago FP " +
                                "CROSS JOIN " +
                                "pedido_detalle PD " +
                                "CROSS JOIN " +
                                "producto P " +
                                "WHERE " +
                                "PC.oc_numero = '" + oc_num + "' " +
                                "AND " +
                                "PD.oc_numero = '" + oc_num + "' " +
                                "AND " +
                                "PD.cip = P.codpro " +
                                "AND " +
                                "PC.cod_cli = C.codcli " +
                                "AND " +
                                "V.codven = PC.cod_emp " +
                                "AND " +
                                "PC.codigoTransportista = T.codigoTransporte " +
                                "AND " +
                                "C.codcli = T.codigoCliente " +
                                "AND " +
                                "PC.cond_pago = FP.codforpag "
                                , null);
                if (objCursor.getCount() != 0)
                {
                    while (objCursor.moveToNext())
                    {
                        String oc_numero = objCursor.getString(0);
                        String ruccli = objCursor.getString(1);
                        String codven = objCursor.getString(2);
                        String nomcli = objCursor.getString(3);
                        String telefono_cliente = objCursor.getString(4);
                        String nomven = objCursor.getString(5);
                        String direccionFiscal = objCursor.getString(6);
                        String email_cliente = objCursor.getString(7);
                        String email_vendedor = objCursor.getString(8);
                        String descripcion = objCursor.getString(9);
                        String desforpag = objCursor.getString(10);
                        String monto_total = objCursor.getString(11);
                        String valor_igv = objCursor.getString(12);
                        String sub_total = objCursor.getString(13);
                        String peso_total = objCursor.getString(14);
                        String fecha_oc = objCursor.getString(15);
                        String fecha_mxe = objCursor.getString(16);
                        String codpro = objCursor.getString(17);
                        int cantidad = Integer.parseInt(objCursor.getString(18));
                        String unidad_medida = objCursor.getString(19);
                        String despro = objCursor.getString(20);
                        String moneda = objCursor.getString(21);
                        String precio_bruto = objCursor.getString(22);
                        String precio_neto = objCursor.getString(23);
                        String porcentaje_desc = objCursor.getString(24);
                        String telefono_vendedor = objCursor.getString(25);
                        String text_area = objCursor.getString(26);
                        String observacion = objCursor.getString(27);
                        String observacion2 = objCursor.getString(28);
                        String observacion3 = objCursor.getString(29);
                        double pesoTotalProducto = objCursor.getDouble(objCursor.getColumnIndex("peso_bruto"));
                        double porcentaje_desc_extra = objCursor.getDouble(objCursor.getColumnIndex("porcentaje_desc_add"));

                        objDbPedidoCabeceraDetalleArrayList.add(
                                new ReportePedidoCabeceraDetalle(
                                        oc_numero,
                                        ruccli,
                                        codven,
                                        nomcli,
                                        telefono_cliente,
                                        nomven,
                                        direccionFiscal,
                                        email_cliente,
                                        email_vendedor,
                                        descripcion,
                                        desforpag,
                                        monto_total,
                                        valor_igv,
                                        sub_total,
                                        peso_total,
                                        fecha_oc,
                                        fecha_mxe,
                                        codpro,
                                        cantidad,
                                        unidad_medida,
                                        despro,
                                        moneda,
                                        precio_bruto,
                                        precio_neto,
                                        porcentaje_desc,
                                        porcentaje_desc_extra,
                                        telefono_vendedor,
                                        text_area,
                                        observacion,
                                        observacion2,
                                        observacion3,
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
    public ArrayList<DataCabecera> getCabecera(String oc_num)
    {

        oc_num=oc_num.replace(" ", "%");
        ArrayList<DataCabecera> objDbPedidoCabeceraArrayList = new ArrayList<>();
        SQLiteDatabase objSqLiteDatabase = getReadableDatabase();
        if (objSqLiteDatabase != null)
        {

            String sql="SELECT PC.oc_numero, C.ruccli, " +
                    "V.codven, C.nomcli, " +
                    "C.telefono as telefonoCliente, V.nomven, " +
                    "C.direccionFiscal, C.email as emailCliente, " +
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
                    "vendedor V " +
                    "CROSS JOIN " +
                    "transporte T " +
                    "CROSS JOIN " +
                    "forma_pago FP " +
                    "WHERE " +
                    "PC.oc_numero like '%" + oc_num + "%' " +
                    "AND " +
                    "PC.cod_cli = C.codcli " +
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
                DataCabecera cabecera;
                while (objCursor.moveToNext())
                {
                    cabecera=new DataCabecera();
                    cabecera.setOc_numero(objCursor.getString(objCursor.getColumnIndex("oc_numero")));
                    cabecera.setNomcli(objCursor.getString(objCursor.getColumnIndex("nomcli")));
                    cabecera.setRuccli(objCursor.getString(objCursor.getColumnIndex("ruccli")));
                    cabecera.setCodven(objCursor.getString(objCursor.getColumnIndex("codven")));
                    cabecera.setTelefono(objCursor.getString(objCursor.getColumnIndex("telefonoCliente")));
                    cabecera.setNomven(objCursor.getString(objCursor.getColumnIndex("nomven")));
                    cabecera.setDireccionFiscal(objCursor.getString(objCursor.getColumnIndex("direccionFiscal")));
                    cabecera.setEmail_cliente(objCursor.getString(objCursor.getColumnIndex("emailCliente")));
                    cabecera.setEmail_vendedor(objCursor.getString(objCursor.getColumnIndex("emailVendedor")));
                    cabecera.setDescripcion(objCursor.getString(objCursor.getColumnIndex("descripcion")));
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
