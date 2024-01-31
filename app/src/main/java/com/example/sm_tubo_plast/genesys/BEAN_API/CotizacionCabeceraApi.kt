package com.example.sm_tubo_plast.genesys.BEAN_API

import android.app.Activity
import android.graphics.Color
import android.view.View
import com.example.sm_tubo_plast.R
import com.example.sm_tubo_plast.genesys.BEAN.DataCabeceraPDF
import com.example.sm_tubo_plast.genesys.adapters.ReportesPedidosCabeceraRecyclerView
import com.example.sm_tubo_plast.genesys.datatypes.DBclasses
import com.example.sm_tubo_plast.genesys.domain.IReportePedidoCabecera
import com.example.sm_tubo_plast.genesys.fuerza_ventas.PedidosActivity
import com.example.sm_tubo_plast.genesys.util.VARIABLES

class CotizacionCabeceraApi: IReportePedidoCabecera
{
    var codigo_pedido:String?=null;
    var codcli:String?=null;
    var nomcli:String?=null;
    var fecha_pedido:String?=null;
    var xtipmon:String?=null;
    var total:Double?=null;
    var sub_total:Double?=null;
    var igv:Double?=null;
    var direccion:String?=null;
    var telefono:String?=null;
    var email:String?=null;
    var nombre_contacto:String?=null;
    var telefono_contacto:String?=null;
    var codven:String?=null;
    var nomven:String?=null;
    var telefono_vendedor:String?=null;
    var email_vvendedor:String?=null;
    var text_area_vendedor:String?=null;
    var forma_pago:String?=null;
    var fecha_entrega:String?=null;
    var validez_oferta:Int?=null;
    var observacion:String?=null;
    var nombre_transporte:String?=null;
    var direccion_transporte:String?=null;
    var proyecto_transporte:String?=null;

    var tipotipo_registro: String? = null
        get() {
            return if (field != null) {
                field
            } else {
                PedidosActivity.TIPO_COTIZACION
            }
        }
    var peso_total:Double?=null
        get() {
            return if (field != null) {
                field
            } else {
                1.0
            }
        }

    override fun setViewByHolder(
        activity: Activity?,
        obj_dbclasses: DBclasses?,
        viewHolder: ReportesPedidosCabeceraRecyclerView.ViewHolderItem?,
        position: Int
    ) {

        viewHolder!!.foto.setBackgroundColor(Color.rgb(46, 178, 0))


        val moneda = "" + this.xtipmon
        if (moneda == PedidosActivity.MONEDA_SOLES_IN || moneda == "MN") {
            viewHolder!!.moneda.text = "S/."
        } else {
            viewHolder!!.moneda.text = "$."
        }
        viewHolder!!.nomcliente.setTextColor(Color.parseColor("#040404"))

        viewHolder!!.nomcliente.setText("" + this.nomcli);
        viewHolder!!.tipopago.setText("" + this.forma_pago);
        viewHolder!!.total.setText(""+this.total!!)
        viewHolder!!.numoc.setText(""+this.codigo_pedido)
        viewHolder!!.estado.setText("")

        viewHolder.tv_tipoRegistro.text = "C"
        viewHolder.tv_tipoRegistro.setBackgroundColor(activity!!.resources.getColor(R.color.indigo_500))
        viewHolder.labell.visibility = View.INVISIBLE
        viewHolder.tv_pedidoAnterior.visibility = View.INVISIBLE
        viewHolder.itemView.setBackgroundResource(R.drawable.selector_reporte_cotizacion)

        viewHolder!!.imgCampanaYellow.visibility = View.GONE;
        viewHolder!!.edtObservacion_pedido.text = ""
        viewHolder!!.edtFechavisita.text = "Fecha "+this.fecha_pedido

    }

    fun dataApiToObjetDataPDF(): DataCabeceraPDF{
        var dataCabcera= DataCabeceraPDF().let {

            it.oc_numero = this.codigo_pedido
            it.nomcli = this.nomcli
            it.ruccli = this.codcli
            it.codven = this.codven
            it.telefono = this.telefono
            it.nomven = this.nomven
            it.direccion = this.direccion
            it.email_cliente = this.email
            it.email_vendedor = this.email_vvendedor
            it.desforpag = this.forma_pago
            it.monto_total = this.total.toString()
            it.valor_igv = this.igv.toString()
            it.subtotal = this.sub_total.toString()
            it.peso_total = this.peso_total.toString()
            it.fecha_oc = this.fecha_pedido
            it.fecha_mxe = this.fecha_entrega
            it.moneda = this.xtipmon
            it.telefono_vendedor = this.telefono_vendedor
            it.text_area = this.text_area_vendedor
            it.observacion = this.nombre_contacto+ VARIABLES.SEPARADOR_OBSERVACION+this.telefono_contacto
            it.observacion2 = this.nombre_transporte+VARIABLES.SEPARADOR_OBSERVACION+this.direccion_transporte+VARIABLES.SEPARADOR_OBSERVACION+this.proyecto_transporte
            it.observacion3 = this.observacion
            it.tipoRegistro = this.tipotipo_registro
            it.diasVigencia = this.validez_oferta!!
            return it;
        }
        return dataCabcera;
    }
}