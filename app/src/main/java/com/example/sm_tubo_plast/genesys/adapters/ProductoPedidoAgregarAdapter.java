package com.example.sm_tubo_plast.genesys.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.constans.pedidos.maestroCategoriaDscto.MaestroCategoriaDescuento;
import com.example.sm_tubo_plast.constans.pedidos.maestroCategoriaDscto.Opcion;
import com.example.sm_tubo_plast.genesys.AccesosPerfil.AccesosOpciones;
import com.example.sm_tubo_plast.genesys.BEAN.ItemProducto;
import com.example.sm_tubo_plast.genesys.datatypes.DB_PromocionDetalle;
import com.example.sm_tubo_plast.genesys.datatypes.DBclasses;
import com.example.sm_tubo_plast.genesys.util.VARIABLES;

public class ProductoPedidoAgregarAdapter extends RecyclerView.Adapter<ProductoPedidoAgregarAdapter.ViewHolder> {

    public interface OnAgregarProductoListener {
        void onAgregarProducto(ItemProducto producto, int cantidad, double pctjDscto, int flagStockValido);
    }

    MaestroCategoriaDescuento maestroCategoriaDescuento =null;
    boolean swAplicaDsctoProntoPago=false;
    private ItemProducto[]  lista;
    private Activity activity;
    private DBclasses obj_dbclasses;
    String codven, codcli;

    private OnAgregarProductoListener listener;

    public ProductoPedidoAgregarAdapter(
            Activity activity,
            ItemProducto[] lista,
            MaestroCategoriaDescuento maestroCategoriaDescuento,
            boolean swAplicaDsctoProntoPago,
            DBclasses obj_dbclasses,
            String codven,
            String codcli,
            OnAgregarProductoListener listener
            ) {
        this.activity=activity;
        this.lista = lista;
        this.listener = listener;
        this.maestroCategoriaDescuento=maestroCategoriaDescuento;
        this.swAplicaDsctoProntoPago=swAplicaDsctoProntoPago;
        this.obj_dbclasses=obj_dbclasses;
        this.codven=codven;
        this.codcli=codcli;
    }

    public void setLista(ItemProducto[] lista) {
        this.lista = lista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_producto_venta, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position) {

        holder.edtCantidad.setText("");
        ItemProducto producto = lista[position];
        holder.tvNombreProducto.setText(producto.getCodprod()+" - "+producto.getDescripcion());

        double dsctoCategoria=obtenerPorcentajeDsctoByCondicion(producto.getMarca());
        holder.tvDsctoCategoria.setText(dsctoCategoria>0?(dsctoCategoria+"%"):"--");
        holder.tvPrecioLista.setText(
                "Precio lista sin igv S/ " + producto.getPrecio_base()
        );

        double montoDescuento=VARIABLES.getDoubleFormaterThreeDecimal(producto.getPrecio_base()*(dsctoCategoria/100));
        double precioUnit=VARIABLES.getDoubleFormaterThreeDecimal((producto.getPrecio_base()-montoDescuento));
        holder.tvPrecio.setText("S/ "+precioUnit);

        holder.tvStock.setText((int)(producto.getStockDetalle().getStock())+ " "+ producto.getCodunimed());
        holder.tvStockSeparado.setText((int)(producto.getStockDetalle().getXtemp())+ " "+ producto.getCodunimed());
        holder.tvStockEnTransito.setText((int)(producto.getStockDetalle().getTransito())+ " "+ producto.getCodunimed());
        holder.tvStockDisponible.setText((int)(producto.getStockDetalle().getDisponible())+ " "+ producto.getCodunimed());
        //if(producto.getStock()<=0){
        holder.tvStock.setTextColor(holder.itemView.getContext().getResources().getColor(producto.getStockDetalle().getStock()>0?R.color.grey_900:R.color.red_500));
        holder.tvStockDisponible.setTextColor(holder.itemView.getContext().getResources().getColor(producto.getStockDetalle().getDisponible()>0?R.color.green_500:R.color.red_500));
        //}

        GestionarPromociones(holder, position);
        holder.btnAgregar.setOnClickListener(v -> {

            String cantidadTexto =
                    holder.edtCantidad.getText()
                            .toString()
                            .trim();

            if (cantidadTexto.isEmpty()) {
                holder.edtCantidad.setError("Ingrese cantidad");
                return;
            }

            int cantidad;

            try {
                cantidad = Integer.parseInt(cantidadTexto);
            } catch (Exception e) {
                holder.edtCantidad.setError("Cantidad inválida");
                return;
            }

            if (cantidad <= 0) {
                holder.edtCantidad.setError(
                        "La cantidad debe ser mayor a 0"
                );
                return;
            }

            double stock = producto.getStockDetalle().getDisponible();
            double precio_base = producto.getPrecio_base();
            if(precio_base<=0){
                holder.edtCantidad.setError("No tiene precio");
                return;
            }
            if (cantidad > stock) {
                new AlertDialog.Builder(activity)
                        .setTitle("Stock Insuficiente")
                        .setMessage("¿Desea registrar el almacen como virtual?")
                                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        holder.edtCantidad.setError(null);
                                        listener.onAgregarProducto(
                                                producto,
                                                cantidad,
                                                dsctoCategoria,
                                                0
                                        );
                                    }
                                })
                                        .setNegativeButton("No", null)
                                                .create().show();

                holder.edtCantidad.setError( "Stock insuficiente" );
                return;
            }
            listener.onAgregarProducto(
                    producto,
                    cantidad,
                    dsctoCategoria,
                    1
            );

        });
    }

    @Override
    public int getItemCount() {
        return lista != null ? lista.length : 0;
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvNombreProducto;
        TextView tvDsctoCategoria;
        TextView tvPrecioLista;
        TextView tvPrecio;
        TextView tvStock, tvStockSeparado, tvStockEnTransito, tvStockDisponible;

        EditText edtCantidad;
        TextView btnAgregar, tv_verMas;
        LinearLayout layoutItemPromos, layoutPromo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_verMas=itemView.findViewById(R.id.tv_verMas);
            layoutItemPromos =  itemView.findViewById(R.id.layoutItemPromos);
            layoutPromo =  itemView.findViewById(R.id.layoutPromo);
            tv_verMas =  itemView.findViewById(R.id.tv_verMas);

            tvNombreProducto =itemView.findViewById(R.id.tvNombreProducto);

            tvPrecioLista =
                    itemView.findViewById(
                            R.id.tvPrecioLista
                    );

            tvPrecio =
                    itemView.findViewById(
                            R.id.tvPrecio
                    );

            tvStock =itemView.findViewById(R.id.tvStock);
            tvStockSeparado =itemView.findViewById(R.id.tvStockSeparado);
            tvStockEnTransito =itemView.findViewById(R.id.tvStockEnTransito);
            tvStockDisponible =itemView.findViewById(R.id.tvStockDisponible);

            edtCantidad =
                    itemView.findViewById(
                            R.id.edtCantidad
                    );

            btnAgregar =
                    itemView.findViewById(
                            R.id.tvAddProducto
                    );
            tvDsctoCategoria=itemView.findViewById(R.id.tvDsctoCategoria);
        }
    }


    private double obtenerPorcentajeDsctoByCondicion(String marcaProd){
        double addAdcional=0.0;
        if(swAplicaDsctoProntoPago){
            addAdcional=2.00;
        }
        if (maestroCategoriaDescuento.getAdicional()!=null) {
            Opcion opcion =maestroCategoriaDescuento.getAdicional();
            boolean isInNotIcluded=false;
            for (String s : opcion.getCondicion().getMarca_not()) {
                if (s.toLowerCase().contains(marcaProd.toLowerCase())) {
                    isInNotIcluded=true;
                    break;
                }
            }
            if(!isInNotIcluded){
                boolean isFindedNotContado=false;
                boolean isFindedContadoIncluded=false;
                if(swAplicaDsctoProntoPago && opcion.getCondicion().getForma_pago()!=null){
                    for (String s : opcion.getCondicion().getForma_pago().getMarca_not()) {
                        if (s.toLowerCase().contains("contado")) {
                            isFindedNotContado=true;
                        }
                    }

                    for (String s : opcion.getCondicion().getForma_pago().getMarca_inc()) {
                        if (s.toLowerCase().contains("contado")
                                || s.equalsIgnoreCase("todos")) {
                            isFindedContadoIncluded=true;
                        }

                    }
                }else isFindedContadoIncluded=true;

                if(!isFindedNotContado && isFindedContadoIncluded){
                    for (String s : opcion.getCondicion().getMarca_inc()) {
                        if (s.toLowerCase().contains(marcaProd.toLowerCase())
                                || s.equalsIgnoreCase("todos")) {
                            addAdcional += opcion.getCondicion().getDsct_pct();
                        }

                    }
                }
            }

        }
        for (Opcion opcion : maestroCategoriaDescuento.getOpciones()) {
            boolean isInNotIcluded=false;
            for (String s : opcion.getCondicion().getMarca_not()) {
                if (s.toLowerCase().contains(marcaProd.toLowerCase())) {
                    isInNotIcluded=true;
                    break;
                }
            }
            if(isInNotIcluded) continue;

            for (String s : opcion.getCondicion().getMarca_inc()) {
                if (s.toLowerCase().contains(marcaProd.toLowerCase())
                        || s.equalsIgnoreCase("todos")) {

                    return opcion.getCondicion().getDsct_pct()+addAdcional;
                }
            }

        }
        return 0.0;
    }

    @SuppressLint("Range")
    public void GestionarPromociones(ViewHolder holder, int position){
        holder.layoutPromo.removeAllViews();
        Cursor cur= obj_dbclasses.GetCondicionDePromocionx(codven, codcli,0,lista[position].getCodprod() );
        String descripcion = "";
        String promocionTitulo = "";
        boolean clienteLimitado=false;
        boolean politicaPrecioLimitado=false;
        boolean isPromocionAND=false;

        while (cur.moveToNext()) {

            clienteLimitado=  cur.getInt(cur.getColumnIndex("clienteLimitado"))==1;
            politicaPrecioLimitado=  cur.getInt(cur.getColumnIndex("politicaLimitado"))==1;
            isPromocionAND=  cur.getInt(cur.getColumnIndex("total_agrupado"))>=2;

            String promoAcumuladoOrAgrupado= "";
            int exclusivo= cur.getInt(cur.getColumnIndex("exclusivo"));
            promocionTitulo=exclusivo>0?"PROMOCION EXCLUIVO\n*":"";
            promoAcumuladoOrAgrupado+= " "+ cur.getString(cur.getColumnIndex("acumulable"))+"";
            promoAcumuladoOrAgrupado+= "  "+ cur.getString(cur.getColumnIndex("promocion_and_or"));
//            promocionTitulo= "Promocion "+ cur.getString(cur.getColumnIndex("rango_o_escalable"));
            promocionTitulo+= promoAcumuladoOrAgrupado;
            String secuenciaPromo=  cur.getString(cur.getColumnIndex("secuencia"));
            int secuenciaItem=  cur.getInt(cur.getColumnIndex("item"));
            promocionTitulo+= " "+secuenciaPromo+" - "+ cur.getString(cur.getColumnIndex("promocion"));

            descripcion="<font color=#1976D2> <strong>";
            int condicion= cur.getInt(cur.getColumnIndex("condicion"));
            if (condicion==1) descripcion+="Compra Mayor o igual a ";
            if (condicion==2) descripcion+="Compra Menor o igual a ";
            if (condicion==3) descripcion+="Por cada compra de ";
            descripcion+=" </strong></font>";
            descripcion+="<font>";
            descripcion+="<strong>";
            String tipo =cur.getString(cur.getColumnIndex("tipo"));
            if (tipo.equals("M")){// tipo entrada M=por monto, C= por cantidad
                descripcion += " S/. "+ VARIABLES.formater_thow_decimal.format(cur.getDouble(cur.getColumnIndex("monto")));
            }else{
                descripcion += " "+(cur.getString(cur.getColumnIndex("cant_condicion")));
                descripcion += " "+(cur.getString(cur.getColumnIndex("unimedE")));
            }
            descripcion+=" </strong></font>";
            descripcion+=" "+(isPromocionAND?" + (...) ":"")+"";
            String descripcionSalida = " Te llevas <strong> "+cur.getString(cur.getColumnIndex("tipo_promocion_txt"))+" "+(cur.getString(cur.getColumnIndex("cant_promocion")));
            if (!cur.getString(cur.getColumnIndex("tipo_promocion_txt")).equals("XDESCUENTO")){
                descripcionSalida += " "+(cur.getString(cur.getColumnIndex("unimedS")));
                descripcionSalida += " de "+(cur.getString(cur.getColumnIndex("despro")));
            }
            descripcionSalida+="</font>";
            descripcion+=descripcionSalida+".";

            int desde=cur.getInt(cur.getColumnIndex("desde"));
            int hasta=cur.getInt(cur.getColumnIndex("hasta"));
            String txtRangos="";
            if (desde>0){
                txtRangos=" <font color=#1976D2>Rango desde "+desde+" hasta "+hasta+"";
                if (tipo.equals("M"))
                    txtRangos+=" Soles (S/.)";
                else txtRangos+=cur.getString(cur.getColumnIndex("unimedE"))+"</font> ";
            }
            descripcion+=""+txtRangos;
            int combos_totales=cur.getInt(cur.getColumnIndex("combos_totales"));
            int avance_combos=cur.getInt(cur.getColumnIndex("avance_combos"));
            descripcion+=""+txtRangos;
            if(combos_totales>=0){
                descripcion+="<br><font color=#1976D2> Nro de combos "+(combos_totales-avance_combos)+" de "+combos_totales+"</font>";
            }
            String tipoSalidaPromocion=cur.getString(cur.getColumnIndex("tipo_promocion"));
            String codproSalida=cur.getString(cur.getColumnIndex("salida"));


            View promoView = LayoutInflater.from(activity).inflate(R.layout.item_promocion_catalogo, null);
            TextView politicaLimitado= promoView.findViewById(R.id.tv_politicaLimitado);
            TextView tv_clienteLimitado= promoView.findViewById(R.id.tv_clienteLimitado);
            TextView tv_verDetalle= promoView.findViewById(R.id.tv_verDetalle);
            TextView tvSalidaBonificacion= promoView.findViewById(R.id.tvSalidaBonificacion);
            ((TextView)promoView.findViewById(R.id.tvPromocionTitulo)).setText(cur.getPosition()+1+") -" +promocionTitulo);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                ((TextView)promoView.findViewById(R.id.tv_promociones)).setText(Html.fromHtml(descripcion , Html.FROM_HTML_MODE_LEGACY));
            } else {
                ((TextView)promoView.findViewById(R.id.tv_promociones)).setText(Html.fromHtml(descripcion));
            }
            promoView.findViewById(R.id.tvPromocionTitulo).setVisibility(cur.getCount()>0?View.VISIBLE:View.GONE);
            promoView.findViewById(R.id.tv_promociones).setVisibility(cur.getCount()>0?View.VISIBLE:View.GONE);
            tv_clienteLimitado.setVisibility(clienteLimitado?View.VISIBLE:View.GONE);
            politicaLimitado.setVisibility(View.GONE);
            promoView.setVisibility(cur.getPosition()>0?View.GONE:View.VISIBLE);

            tvSalidaBonificacion.setVisibility(View.GONE);
            if (tipoSalidaPromocion.equals(DB_PromocionDetalle.salidaBonificacionXCOLORES)
                    || tipoSalidaPromocion.equals(DB_PromocionDetalle.salidaBonificacionXCOMBO )) {
                tvSalidaBonificacion.setText(tipoSalidaPromocion);
                tvSalidaBonificacion.setVisibility(View.VISIBLE);
            }
            politicaLimitado.setId(cur.getPosition());
            politicaLimitado.setOnClickListener(view -> {
                //ItemProducto[]  precios = obj_dbclasses.ObtenerPoliticaByPromocionDetalle(secuenciaPromo, listaData.get(position).getCodprod());
                //ProductoInfoActivity classx=new ProductoInfoActivity();
                //classx.mostrarPrecios(activity, precios);
            });
            String finalPromocionTitulo = promocionTitulo;
            String finalDescripcionSalida = (isPromocionAND?
                    "Por la compra de los siguientes productos: "+descripcion
                    : descripcion);
            String finalPromoAcumuladoOrAgrupado = promoAcumuladoOrAgrupado;
            boolean finalIsPromocionAND = isPromocionAND;
            tv_verDetalle.setVisibility(View.GONE);
            tv_verDetalle.setOnClickListener(view -> {
//                UtilViewPromocionDetalleFragment ddd=new UtilViewPromocionDetalleFragment(activity,
//                        fragment,codven, "%" ,
//                        ""+finalPromoAcumuladoOrAgrupado,
//                        finalPromocionTitulo,
//                        secuenciaPromo,
//                        secuenciaItem,
//                        finalIsPromocionAND,
//                        finalDescripcionSalida);
//                ddd.setFormularioPedido(myListener.OnClikVerDetalle(position));
//                ddd.show(fragment, "UtilViewPromocionDetalleFragment");
            });
            tvSalidaBonificacion.setVisibility(View.GONE);
            tvSalidaBonificacion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    MostrarBonificacionesComboOR_ColoresDisponible(secuenciaPromo, codproSalida, tvSalidaBonificacion.getText().toString());
                }
            });

            holder.layoutPromo.addView(promoView);

        }

        if (cur.getCount()>1) {
            holder.tv_verMas.setText("Ver Mas "+(cur.getCount()-1)+" items");
            holder.tv_verMas.setVisibility(View.VISIBLE);

        }else{
            holder.tv_verMas.setVisibility(View.GONE);
        }
        holder.tv_verMas.setOnClickListener(view -> {
            if (holder.tv_verMas.getText().toString().contains("Ver Mas") ) {
                holder.tv_verMas.setText("Ver Menos");
                for (int i = 0; i < holder.layoutPromo.getChildCount(); i++) {
                    holder.layoutPromo.getChildAt(i).setVisibility(View.VISIBLE);
                }
            }else{
                holder.tv_verMas.setText("Ver Mas "+(cur.getCount()-1)+" items");
                for (int i = 1; i < holder.layoutPromo.getChildCount(); i++) {
                    holder.layoutPromo.getChildAt(i).setVisibility(View.GONE);
                }
            }
        });

        holder.layoutPromo.setVisibility(cur.getCount()>0?View.VISIBLE:View.GONE);
        cur.close();

    }
}