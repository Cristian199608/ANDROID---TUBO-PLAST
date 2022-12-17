package com.example.sm_tubo_plast.genesys.fuerza_ventas.Reportes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sm_tubo_plast.R
import com.example.sm_tubo_plast.genesys.BEAN.DataCabecera
import com.example.sm_tubo_plast.genesys.DAO.DAO_ReportePedido
import com.example.sm_tubo_plast.genesys.adapters.PedidoCabeceraRecyclerView

class ReportePedidosYContizacionActivity : AppCompatActivity() {
    var mydbClass: DAO_ReportePedido? = null
    var recyclerView: RecyclerView? = null
    var objDbPedidoCabecera: ArrayList<DataCabecera>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reporte_pedidos_ycontizacion)
        recyclerView = findViewById<RecyclerView>(R.id.dataRvCabecera)
        mydbClass=DAO_ReportePedido(this);
        showDataFinal()
    }
    fun showDataFinal() {
        objDbPedidoCabecera = mydbClass?.getCabecera("")
        val dbPedidoCabecera = PedidoCabeceraRecyclerView(mydbClass, objDbPedidoCabecera)
        recyclerView!!.hasFixedSize()
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        recyclerView!!.adapter = dbPedidoCabecera
    }
}