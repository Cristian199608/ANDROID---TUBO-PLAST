package com.example.sm_tubo_plast.genesys.BEAN;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.DAO.DAO_San_Visitas;
import com.example.sm_tubo_plast.genesys.adapters.ReportesPedidosCabeceraRecyclerView;
import com.example.sm_tubo_plast.genesys.datatypes.DBclasses;
import com.example.sm_tubo_plast.genesys.domain.IReportePedidoCabecera;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.PedidosActivity;
import com.example.sm_tubo_plast.genesys.util.GlobalVar;
import com.example.sm_tubo_plast.genesys.util.VARIABLES;

public class ReportePedidoCabeceraBEAN implements IReportePedidoCabecera {
    String nomcli;
    String codcli;
    String tipopago;
    String total;
    String numoc;
    String estado;
    int motivo_noventa;
    String mensaje;
    String moneda;
    String flag;
    String tipoRegistro;
    String pedidoAnterior;
    String latitud;
    String longitud;
    String fecha;

    public String getNomcli() {
        return nomcli;
    }

    public void setNomcli(String nomcli) {
        this.nomcli = nomcli;
    }

    public String getCodcli() {
        return codcli;
    }

    public void setCodcli(String codcli) {
        this.codcli = codcli;
    }

    public String getTipopago() {
        return tipopago;
    }

    public void setTipopago(String tipopago) {
        this.tipopago = tipopago;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getNumoc() {
        return numoc;
    }

    public void setNumoc(String numoc) {
        this.numoc = numoc;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public int getMotivo_noventa() {
        return motivo_noventa;
    }

    public void setMotivo_noventa(int motivo_noventa) {
        this.motivo_noventa = motivo_noventa;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getTipoRegistro() {
        return tipoRegistro;
    }

    public void setTipoRegistro(String tipoRegistro) {
        this.tipoRegistro = tipoRegistro;
    }

    public String getPedidoAnterior() {
        return pedidoAnterior;
    }

    public void setPedidoAnterior(String pedidoAnterior) {
        this.pedidoAnterior = pedidoAnterior;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }



    @Override
    public void setViewByHolder(Activity activity,
                                DBclasses obj_dbclasses,
                                ReportesPedidosCabeceraRecyclerView.ViewHolderItem viewHolder,
                                int position) {

        if (this.getFlag().equals("E")) {
            viewHolder.foto.setBackgroundColor(Color.rgb(46, 178, 0));
        } else if (this.getFlag().equals("I")) {
            viewHolder.foto.setBackgroundColor(Color.rgb(255, 255, 0));
        } else if (this.getFlag().equals("P")) {
            viewHolder.foto.setBackgroundColor(Color.rgb(255, 30, 30));
            viewHolder.estado.setTextColor(Color.rgb(255, 30, 30));
        } else if (this.getFlag().equals("T")) {
            viewHolder.foto.setBackgroundColor(Color.rgb(75, 0, 130));
            viewHolder.estado.setTextColor(Color.rgb(75, 0, 130));
        }


        String moneda = "" + this.getMoneda();
        if (moneda.equals(PedidosActivity.MONEDA_SOLES_IN)) {
            viewHolder.moneda.setText("S/.");
        } else {
            viewHolder.moneda.setText("$.");
        }

        String estado = this.getEstado();
        int codnoventa = this.getMotivo_noventa();

        viewHolder.edtFechavisita.setText("");
        if (codnoventa== GlobalVar.CODIGO_VISITA_CLIENTE){
            San_Visitas san= DAO_San_Visitas.getSan_VisitaByOc_numero(obj_dbclasses, this.getNumoc());
            if (san!=null) {
                viewHolder.nomcliente.setTextColor(Color.parseColor("#040404"));
                viewHolder.edtFechavisita.setTextColor(activity.getResources().getColor(R.color.teal_600));
                viewHolder.edtFechavisita.setText("Visitado: "+san.getFecha_Ejecutada());
            }else{
                san= DAO_San_Visitas.getSan_VisitarByOc_numero(obj_dbclasses, this.getNumoc());
                if(san!=null) {
                    viewHolder.nomcliente.setTextColor(Color.parseColor("#040404"));
                    viewHolder.edtFechavisita.setTextColor(activity.getResources().getColor(R.color.yellow_900));
                    viewHolder.edtFechavisita.setText("Visitar: "+san.getFecha_Ejecutada());
                }
            }
        }
        else if (estado.equals("A")) {
            viewHolder.nomcliente.setTextColor(Color.parseColor("#FF0000"));
            viewHolder.moneda.setText("");
        } else {
            viewHolder.nomcliente.setTextColor(Color.parseColor("#040404"));
        }
        viewHolder.nomcliente.setText(this.getNomcli());
        viewHolder.tipopago.setText(this.getTipopago());
        viewHolder.total.setText(this.getTotal());
        viewHolder.numoc.setText(this.getNumoc());
        viewHolder.estado.setText(this.getMensaje());

        if (this.getPedidoAnterior().trim().isEmpty()) {
            viewHolder.labell.setVisibility(View.INVISIBLE);
            viewHolder.tv_pedidoAnterior.setVisibility(View.INVISIBLE);
        }else{
            viewHolder.labell.setVisibility(View.VISIBLE);
            viewHolder.tv_pedidoAnterior.setVisibility(View.VISIBLE);
        }
        viewHolder.tv_pedidoAnterior.setText(this.getPedidoAnterior().toString());

        if (this.getTipoRegistro().equals(PedidosActivity.TIPO_COTIZACION)) {
            viewHolder.tv_tipoRegistro.setText("C");
            viewHolder.tv_tipoRegistro.setBackgroundColor(activity.getResources().getColor(R.color.indigo_500));
            viewHolder.itemView.setBackgroundResource(R.drawable.selector_reporte_cotizacion);
        }else if (this.getTipoRegistro().equals(PedidosActivity.TIPO_DEVOLUCION)) {
            viewHolder.tv_tipoRegistro.setText("D");
            viewHolder.tv_tipoRegistro.setBackgroundColor(activity.getResources().getColor(R.color.brown_500));
            viewHolder.labell.setVisibility(View.INVISIBLE);
            viewHolder.tv_pedidoAnterior.setVisibility(View.INVISIBLE);
            viewHolder.itemView.setBackgroundResource(R.drawable.selector_reporte_devolucion);
        }else{
            viewHolder.tv_tipoRegistro.setText("P");
            viewHolder.tv_tipoRegistro.setBackgroundColor(activity.getResources().getColor(R.color.teal_500));
            viewHolder.itemView.setBackgroundResource(R.drawable.selector_reporte_pedido);
        }
        boolean isRequired=obj_dbclasses.RequiereValidacionPorDescuento(this.getNumoc());
        viewHolder.imgCampanaYellow.setVisibility(isRequired?View.VISIBLE:View.GONE);
        if (VARIABLES.IsLatitudValido(this.getLatitud())){
            viewHolder.edtObservacion_pedido.setText("");
        }else{
            if (this.getMotivo_noventa()==GlobalVar.CODIGO_VISITA_CLIENTE){
                viewHolder.edtObservacion_pedido.setText("");
            }else{
                viewHolder.edtObservacion_pedido.setText("Pedido sin posición");
            }
        }

    }



}

