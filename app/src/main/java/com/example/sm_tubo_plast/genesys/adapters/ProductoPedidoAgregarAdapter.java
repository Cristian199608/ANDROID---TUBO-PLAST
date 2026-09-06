package com.example.sm_tubo_plast.genesys.adapters;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.constans.pedidos.maestroCategoriaDscto.MaestroCategoriaDescuento;
import com.example.sm_tubo_plast.constans.pedidos.maestroCategoriaDscto.Opcion;
import com.example.sm_tubo_plast.genesys.AccesosPerfil.AccesosOpciones;
import com.example.sm_tubo_plast.genesys.BEAN.ItemProducto;
import com.example.sm_tubo_plast.genesys.util.VARIABLES;

public class ProductoPedidoAgregarAdapter extends RecyclerView.Adapter<ProductoPedidoAgregarAdapter.ViewHolder> {

    public interface OnAgregarProductoListener {
        void onAgregarProducto(ItemProducto producto, int cantidad, double pctjDscto, int flagStockValido);
    }

    MaestroCategoriaDescuento maestroCategoriaDescuento =null;
    boolean swAplicaDsctoProntoPago=false;
    private ItemProducto[]  lista;
    private Activity activity;
    private OnAgregarProductoListener listener;

    public ProductoPedidoAgregarAdapter(
            Activity activity,
            ItemProducto[] lista,
            MaestroCategoriaDescuento maestroCategoriaDescuento,
            boolean swAplicaDsctoProntoPago,
            OnAgregarProductoListener listener
            ) {
        this.activity=activity;
        this.lista = lista;
        this.listener = listener;
        this.maestroCategoriaDescuento=maestroCategoriaDescuento;
        this.swAplicaDsctoProntoPago=swAplicaDsctoProntoPago;
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

        TextView btnAgregar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNombreProducto =
                    itemView.findViewById(
                            R.id.tvNombreProducto
                    );

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
}