package com.example.sm_tubo_plast.genesys.fuerza_ventas.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.BEAN.Model_bonificacion;
import com.example.sm_tubo_plast.genesys.adapters.CH_Adapter_bonificaciones;
import com.example.sm_tubo_plast.genesys.datatypes.DB_PromocionDetalle;

import java.util.ArrayList;

public class DialogFragment_bonificaciones extends DialogFragment {
        String descripcion;
        ArrayList<Model_bonificacion> listaBonificaciones;
        ArrayList<DB_PromocionDetalle> listaPromociones;
        ArrayList<Integer> listaCantidades;
        ArrayList<ArrayList<String[]>> listaPromocionesCompuestas;
        ArrayList<Integer> items_tipoAgrupado;
        ArrayList<ArrayList<Integer>> listaCantidadesUsadas;
        ArrayList<ArrayList<Double>> listaMontosUsados;
        int positionPrioridad;

public interface DialogListener {
    public void onItemClick(int posicion,
                            boolean isBonificarAutomatico,
                            ArrayList<Model_bonificacion> listaBonificaciones,
                            ArrayList<DB_PromocionDetalle> listaPromociones,
                            ArrayList<Integer> listaCantidades,
                            ArrayList<ArrayList<String[]>> listaPromocionesCompuestas,
                            ArrayList<Integer> items_tipoAgrupado,
                            ArrayList<ArrayList<Integer>> listaCantidadesUsadas,
                            ArrayList<ArrayList<Double>> listaMontosUsados);

    public void onDialogClickPositivo();

}

    DialogListener listener;

    public DialogFragment_bonificaciones(
            ArrayList<Model_bonificacion> listaBonificaciones,
            String descripcion,
            ArrayList<DB_PromocionDetalle> listaPromociones,
            ArrayList<Integer> listaCantidades,
            ArrayList<ArrayList<String[]>> listraPromocionesCompuestas,
            ArrayList<Integer> items_tipoAgrupado,
            ArrayList<ArrayList<Integer>> listaCantidadesUsadas,
            ArrayList<ArrayList<Double>> listaMontosUsados,
            int positionPrioridad) {

        this.listaBonificaciones = listaBonificaciones;
        this.descripcion = descripcion;
        this.listaPromociones = listaPromociones;
        this.listaCantidades = listaCantidades;
        this.listaPromocionesCompuestas = listraPromocionesCompuestas;
        this.items_tipoAgrupado = items_tipoAgrupado;
        this.listaCantidadesUsadas = listaCantidadesUsadas;
        this.listaMontosUsados = listaMontosUsados;
        this.positionPrioridad = positionPrioridad;
    }

    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);
        try {
            listener = (DialogListener) activity;

        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View pop = inflater.inflate(R.layout.dialog_bonificaciones, null);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        alertBuilder.setView(pop);
        alertBuilder.setCancelable(false);
        alertBuilder.setTitle("BONIFICACION " + descripcion);

        // LAS BONIFICACIONES SON SIMPLES
        TextView nombreLista = (TextView) pop.findViewById(R.id.txtListaSimple);
        TextView nombreLista2 = (TextView) pop
                .findViewById(R.id.txtListaCompuesta);
        nombreLista.setVisibility(View.GONE);
        nombreLista2.setVisibility(View.GONE);

        ListView lv_bonificaciones = (ListView) pop.findViewById(R.id.lv_bonificacionesSimples);
        //Adapter_bonificaciones adapter = new Adapter_bonificaciones(getActivity(), listaBonificaciones);
        CH_Adapter_bonificaciones adapter = new CH_Adapter_bonificaciones(getActivity(), listaBonificaciones);
        lv_bonificaciones.setAdapter(adapter);
        lv_bonificaciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                listener.onItemClick(position,
                        false,
                        listaBonificaciones, listaPromociones,
                        listaCantidades, listaPromocionesCompuestas,
                        items_tipoAgrupado, listaCantidadesUsadas,
                        listaMontosUsados);
                dismiss();
            }
        });

        alertBuilder.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                        if(positionPrioridad<0)return;
                        listener.onItemClick(
                                positionPrioridad,
                                false,
                                listaBonificaciones, listaPromociones,
                                listaCantidades, listaPromocionesCompuestas,
                                items_tipoAgrupado, listaCantidadesUsadas,
                                listaMontosUsados);

                    }
                });
        alertBuilder.create();
        return alertBuilder.show();
    }

}