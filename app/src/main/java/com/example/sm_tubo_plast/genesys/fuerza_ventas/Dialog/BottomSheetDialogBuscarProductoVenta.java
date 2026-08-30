package com.example.sm_tubo_plast.genesys.fuerza_ventas.Dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.constans.pedidos.maestroCategoriaDscto.MaestroCategoriaDescuento;
import com.example.sm_tubo_plast.genesys.BEAN.ItemProducto;
import com.example.sm_tubo_plast.genesys.Retrofit.Result.bean.ResultProducto;
import com.example.sm_tubo_plast.genesys.Retrofit.RetrofilClientCantol;
import com.example.sm_tubo_plast.genesys.Retrofit.request.GetDataCantol;
import com.example.sm_tubo_plast.genesys.Retrofit.request.producto.RequestProducto;
import com.example.sm_tubo_plast.genesys.Retrofit.util.WS_RetrofitCustom;
import com.example.sm_tubo_plast.genesys.adapters.ProductoPedidoAgregarAdapter;
import com.example.sm_tubo_plast.genesys.datatypes.DBclasses;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.ProductoActivity;
import com.example.sm_tubo_plast.genesys.util.GlobalFunctions;
import com.example.sm_tubo_plast.genesys.util.SnackBar.UtilViewSnackBar;
import com.example.sm_tubo_plast.genesys.util.VARIABLES;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.RequestBody;
import retrofit2.Call;

@SuppressLint("LongLogTag")
public class BottomSheetDialogBuscarProductoVenta
        extends BottomSheetDialogFragment {

    private static final String TAG = "BottomSheetDialogBuscarProductoVenta";

    String codven=null, oc_numero=null;
    MaestroCategoriaDescuento maestroCategoriaDescuento =null;
    boolean swAplicaDsctoProntoPago=false;
    public static BottomSheetDialogBuscarProductoVenta newInstance(String codven,
                                                                   String oc_numero,
                                                                   String canalYCategoriaVenta,
                                                                   boolean swAplicaDsctoProntoPago) {
        BottomSheetDialogBuscarProductoVenta fragment = new BottomSheetDialogBuscarProductoVenta();
        Bundle args = new Bundle();
        args.putString("codven", codven);
        args.putString("oc_numero", oc_numero);
        args.putString("canalYCategoriaVenta", canalYCategoriaVenta);
        args.putBoolean("swAplicaDsctoProntoPago", swAplicaDsctoProntoPago);
        fragment.setArguments(args);
        return fragment;
    }

    MyCallback myCallback;
    public interface MyCallback{
        void result(Intent data);
    }

    public void setOnCallback(MyCallback myCallback){
        this.myCallback=myCallback;
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new BottomSheetDialog(requireContext(), R.style.Theme_Dialog_Translucent);
    }

    EditText edtBuscar;
    RecyclerView recyclerProductos;
    LinearLayout emptyView;
    TextView tvCantidadResultados;
    ImageButton btnCerrar;
    ImageView iviewBuscar;

    DBclasses dBclasses;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_sheet_productos_pedido, container, false);
        edtBuscar =view.findViewById(R.id.edtBuscar);
        recyclerProductos =view.findViewById(R.id.recyclerProductos);
        emptyView =view.findViewById(R.id.emptyView);
        tvCantidadResultados =view.findViewById(R.id.tvCantidadResultados);
        btnCerrar =view.findViewById(R.id.btnCerrar);
        iviewBuscar =view.findViewById(R.id.iviewBuscar);

        if (getArguments()!=null) {
            codven= getArguments().getString("codven", null);
            oc_numero = getArguments().getString("oc_numero");
            swAplicaDsctoProntoPago = getArguments().getBoolean("swAplicaDsctoProntoPago");
            String canalYCategoriaVenta = getArguments().getString("canalYCategoriaVenta");
            for (MaestroCategoriaDescuento canalCategoriaDescuento : MaestroCategoriaDescuento.getDataListDscto("TODOS")) {
                if((canalCategoriaDescuento.getKeyUnico())
                        .equals(canalYCategoriaVenta)){
                    maestroCategoriaDescuento = canalCategoriaDescuento;
                    break;
                }
            }
        }
        if(maestroCategoriaDescuento==null){
            GlobalFunctions.showCustomToast(
                    getActivity(),
                    "Cateria cliente no especificado",
                    GlobalFunctions.TOAST_ERROR);
        }
        dBclasses=new DBclasses(getActivity());
        desahabledBottomSheeetDraggable(view);
        initData();
        configurarBotonBusqueda();
        return view;
    }

    private void initData(){
        btnCerrar.setOnClickListener(v ->
                dismiss()
        );

        iviewBuscar.setOnClickListener(v -> {
            sincronizarProductoConPrecioYStock();
        });
    }

    private void configurarBotonBusqueda(){

        edtBuscar.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                sincronizarProductoConPrecioYStock();
                return true;
            }
            return false;
        });
    }
    private void mostrarListaproductosView(long timeSync){
        ItemProducto[]  lista= dBclasses.getProductosXTIME_SYNC(String.valueOf(timeSync));
        ProductoPedidoAgregarAdapter adapter =
                new ProductoPedidoAgregarAdapter(
                        getActivity(),
                        lista,
                        maestroCategoriaDescuento,
                        swAplicaDsctoProntoPago,
                        (producto, cantidad, pctjDscto) -> {
                            agregarProducto(
                                    producto,
                                    cantidad,
                                    pctjDscto
                            );
                        }
                );
        recyclerProductos.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerProductos.setAdapter(adapter);

        actualizarEmptyView(
                lista,
                recyclerProductos,
                emptyView,
                tvCantidadResultados
        );

    }

    private void actualizarEmptyView(
            ItemProducto[] lista,
            RecyclerView recyclerProductos,
            LinearLayout emptyView,
            TextView tvCantidadResultados) {

        if (lista == null || lista.length==0) {

            recyclerProductos.setVisibility(
                    View.GONE
            );

            emptyView.setVisibility(
                    View.VISIBLE
            );

            tvCantidadResultados.setText(
                    "Productos encontrados: 0"
            );

        } else {

            recyclerProductos.setVisibility(
                    View.VISIBLE
            );

            emptyView.setVisibility(
                    View.INVISIBLE
            );

            tvCantidadResultados.setText(
                    "Productos encontrados: "
                            + lista.length
            );
        }
    }


    private void agregarProducto(
            ItemProducto producto,
            int cantidad,
            double pctjDscto) {

        if(dBclasses.isRegistradoProducto(oc_numero, producto.getCodprod())  ){
            GlobalFunctions.showCustomToast(
                    getActivity(),
                    "Producto ya esta registrado",
                    GlobalFunctions.TOAST_WARNING);
            return;
        }



        int nro_item = dBclasses.getNextNroItemPedido(oc_numero);
        Intent returnIntent = new Intent();
        double montoDescuento=VARIABLES.getDoubleFormaterThreeDecimal(producto.getPrecio_base()*(pctjDscto/100));
        double precioUnit=VARIABLES.getDoubleFormaterThreeDecimal((producto.getPrecio_base()-montoDescuento));

        returnIntent.putExtra("busqueda", "PRODUCTO");
        returnIntent.putExtra("descripcion", producto.getDescripcion());
        returnIntent.putExtra("desunimed", producto.getCodunimed());
        returnIntent.putExtra("Cantidad", cantidad);
        returnIntent.putExtra("peso", producto.getPeso());
        returnIntent.putExtra("fact_conv", producto.getFact_conv());
        returnIntent.putExtra("precioUnidad",precioUnit);
        returnIntent.putExtra("precioLista",""+producto.getPrecioLista());
        returnIntent.putExtra("sec_politica", "0");
        returnIntent.putExtra("descuento",	montoDescuento);
        returnIntent.putExtra("porcentaje_desc",	pctjDscto);
        returnIntent.putExtra("porcentaje_desc_extra",	0.0);
        returnIntent.putExtra("precioPercepcion", 0.0);
        returnIntent.putExtra("agregarComoBonificacion", false);
        returnIntent.putExtra("codigoProducto", producto.getCodprod());
        returnIntent.putExtra("item", nro_item);
        returnIntent.putExtra(ProductoActivity.REQUEST_ACCION_PRODUCTO_KEY, "REQUEST_ACCION_PRODUCTO_VALUE");
        myCallback.result(returnIntent);
    }

    private void sincronizarProductoConPrecioYStock(){
        ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Consultando productos en línea...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        String busquedaOnline = edtBuscar.getText().toString().replace(" ", "%");
        Log.i(TAG, "sincronizarProductoConPrecioYStock busqueda = "+busquedaOnline);
        String urlRe= RetrofilClientCantol.UrlPeticiones.catalogoProducto;
        RequestBody body = RetrofilClientCantol.createBodyJson(RequestProducto.Companion.catalogo(urlRe, codven, busquedaOnline));
        Call<Object> call = RetrofilClientCantol.getRetrofitInstanceCantolWithToken(getActivity())
                .create(GetDataCantol.class).getCliente(body);

        WS_RetrofitCustom ws_retrofitCustom= new WS_RetrofitCustom(getActivity());
        ws_retrofitCustom.StartPeticion(call, new WS_RetrofitCustom.MyListener() {
            @Override
            public void StartFinish(boolean isFinish) {
                if (!isFinish) pDialog.show();
                else pDialog.dismiss();
            }
            @Override
            public void Result(boolean isOk, String mensaje, Object data) {
                if(!isOk){
                    GlobalFunctions.showCustomToast(
                            getActivity(),
                            mensaje,
                            GlobalFunctions.TOAST_ERROR);
                    return;
                }
                Gson gson=new Gson();
                final Type malla = new TypeToken<ArrayList<ResultProducto>>() {}.getType();
                final ArrayList<ResultProducto> lista = gson.fromJson(gson.toJson(data), malla);
                Log.i(TAG, "cant producto "+lista.size());
                long time_sincronizacion=VARIABLES.GetFechaActua_long();
                String errorMensaje = dBclasses.guardarProductoSyn(lista, time_sincronizacion);
                if(errorMensaje!=null){
                    GlobalFunctions.showCustomToast(
                            getActivity(),
                            mensaje,
                            GlobalFunctions.TOAST_ERROR);
                }
                //edt_descuento.setText(""+obtenerPorcentajeDsctoByCondicion());
                //edt_descuento.setEnabled(false);
                //new ProductoActivity.async_busqueda(time_sincronizacion).execute();
                mostrarListaproductosView(time_sincronizacion);
            }
        });
    }

    private void desahabledBottomSheeetDraggable(View view) {
        view.post(new Runnable() {
            @Override
            public void run() {
                View parent = (View) view.getParent();
                if (parent != null) {
                    BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(parent);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    bottomSheetBehavior.setDraggable(false);
                }
            }
        });
    }


}
