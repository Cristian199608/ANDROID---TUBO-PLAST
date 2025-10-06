package com.example.sm_tubo_plast.genesys.fuerza_ventas.compartidoUtil;

import android.widget.Toast;

import com.example.sm_tubo_plast.genesys.datatypes.DBPolitica_Precio2;
import com.example.sm_tubo_plast.genesys.datatypes.DBclasses;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.PedidosActivity;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.ProductoActivity;
import com.example.sm_tubo_plast.genesys.util.VARIABLES;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class UtilCalcularPrecioProducto {
    DBclasses obj_dbclasses;
    String codcli="";
    String codigoMoneda="";
    public UtilCalcularPrecioProducto(DBclasses obj_dbclasses,  String codcli, String codigoMoneda) {
        this.obj_dbclasses = obj_dbclasses;
        this.codcli = codcli;
        this.codigoMoneda = codigoMoneda;
    }
public ResultPrecios  consultarPrecios(String codprod, double pcjDesc1, double pcjDesc2) {
        if(codprod.startsWith("COMBO")) {
            return new ResultPrecios(
                    "0.000",
                    "0.000",
                    "Producto Combo",
                    "0.000",
                    "0.000",
                    "0.000",
                    "0.000",
                    "0.000"
            );
        }
        DecimalFormat formaterPrecioourDecimal = new DecimalFormat("#,##0.000");
        formaterPrecioourDecimal.setRoundingMode(RoundingMode.HALF_UP);

        double valor_cambio=1;
        if (codigoMoneda.equals(PedidosActivity.MONEDA_SOLES_IN)) {
            String tipo_de_cambio  = obj_dbclasses.getCambio("Tipo_cambio");
            valor_cambio  = Double.parseDouble(tipo_de_cambio);
        }

        DBPolitica_Precio2 politica_precio2=obj_dbclasses.GetPoliticaPrecio2ByCliente(codcli, codprod, valor_cambio);
        if(politica_precio2!=null){
            double porcentajeDescuentoManual=pcjDesc1;
            double porcentajeDescuentoExtra= pcjDesc2;

            double precioOriginal=(politica_precio2.getPrecio_base());
            double precioListaConIgv=(politica_precio2.getPrepo_unidad()*1.18);
            double precioListaSinIgv=(politica_precio2.getPrepo_unidad());
            double porcentajeDescuentoSitema= Double.parseDouble(VARIABLES.ParseDecimalTwo(((precioOriginal-precioListaSinIgv)*100/precioOriginal)));

            String smstxtPrecio="Precio Lista a "+formaterPrecioourDecimal.format(precioListaSinIgv);
            if (porcentajeDescuentoSitema>0.0){
                smstxtPrecio="Precio Lista con \n"+(porcentajeDescuentoSitema+porcentajeDescuentoManual)+"% dsc a "+formaterPrecioourDecimal.format(precioListaSinIgv);
            }

            double descuentoConIgv=precioListaConIgv*(porcentajeDescuentoManual/100);
            double descuentoSinIgv=politica_precio2.getPrepo_unidad()*(porcentajeDescuentoManual/100);

            double precioVentaPreSinIGV=precioListaSinIgv-descuentoSinIgv;
            double precioVentaPreIncIGV=precioListaConIgv-descuentoConIgv;

            double descuentoSinIgvExtra=precioVentaPreSinIGV*(porcentajeDescuentoExtra/100);
            double descuentoConIgvExtra=precioVentaPreIncIGV*(porcentajeDescuentoExtra/100);

            double precioVentaFinalSinIGV=precioVentaPreSinIGV-descuentoSinIgvExtra;
            double precioVentaFinalIncIGV=precioVentaPreIncIGV-descuentoConIgvExtra;

            //btn_consultarProducto.setText(sms);
            //edtPrecioUnt.setText(""+formaterPrecioourDecimal.format(precioOriginal));//precio original

            //return precioOriginal
//            tv_precioSinIGV.setText(""+formaterPrecioourDecimal.format(precioVentaPreSinIGV)); //precio sin IGV con descuento
//            tv_precioIncIGV.setText(""+formaterPrecioourDecimal.format(precioVentaPreIncIGV));//precio con IGV con descuento
            //tv_descuentoPVP.setText((porcentajeDescuentoManual>0.0?"1 ("+porcentajeDescuentoManual+"%)":"")+" "+formaterPrecioourDecimal.format(descuentoSinIgv));
            if(porcentajeDescuentoExtra>0){
//                String textDescuentExtra= "2 (" + porcentajeDescuentoExtra + "%)" + " " + formaterPrecioourDecimal.format(descuentoSinIgvExtra);
//                tv_descuentoPVP.setText(tv_descuentoPVP.getText().toString()+"\n"+textDescuentExtra);
//                tv_precioSinIGV.setText("\n"+formaterPrecioourDecimal.format(precioVentaFinalSinIGV));
//                tv_precioIncIGV.setText("\n"+formaterPrecioourDecimal.format(precioVentaFinalIncIGV));
            }
            ResultPrecios resultx =new ResultPrecios(
                    formaterPrecioourDecimal.format(precioOriginal),
                    formaterPrecioourDecimal.format(precioListaSinIgv),
                    smstxtPrecio,
                    formaterPrecioourDecimal.format(descuentoSinIgv),
                    formaterPrecioourDecimal.format(descuentoSinIgvExtra),
                    formaterPrecioourDecimal.format(descuentoSinIgv+descuentoSinIgvExtra),
                    formaterPrecioourDecimal.format(precioVentaFinalSinIGV),
                    formaterPrecioourDecimal.format(precioVentaFinalIncIGV)
                    );
            return resultx;
        }else{
            return new ResultPrecios("No se ha obtenido los precios para el producto codigo: "+codprod+"");
        }
    }

//-----------------------------------------------------------------------------------------------
    public class ResultPrecios{
        public String errorMensaje;
        public String precioOriginal;
        public String precioLista;
        public String smstxtPrecio;
        public String descuentoSinIgv;
        public String descuentoSinIgvExtra;
        public String descuentoSinIgvTotal;
        public String precioVentaPreSinIGV;
        public String precioVentaPreConIGV;

        public ResultPrecios(String errorMensaje) {
            this.errorMensaje = errorMensaje;
        }

        public ResultPrecios(String precioOriginal, String precioLista, String smstxtPrecio, String descuentoSinIgv, String descuentoSinIgvExtra, String descuentoSinIgvTotal, String precioVentaPreSinIGV, String precioVentaPreConIGV) {
            this.precioOriginal = precioOriginal;
            this.precioLista = precioLista;
            this.smstxtPrecio = smstxtPrecio;
            this.descuentoSinIgv = descuentoSinIgv;
            this.descuentoSinIgvExtra = descuentoSinIgvExtra;
            this.descuentoSinIgvTotal = descuentoSinIgvTotal;
            this.precioVentaPreSinIGV = precioVentaPreSinIGV;
            this.precioVentaPreConIGV = precioVentaPreConIGV;
        }
    }
}
