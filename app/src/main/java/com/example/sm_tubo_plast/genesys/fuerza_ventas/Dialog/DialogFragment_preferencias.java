package com.example.sm_tubo_plast.genesys.fuerza_ventas.Dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.datatypes.DB_Almacenes;

import java.util.ArrayList;

public class DialogFragment_preferencias extends DialogFragment {
    String idAlmacenSeleccionado;
    String almacenSeleccionado, vistaSeleccionado;
    boolean stockAuto, precioCero = false;

    ArrayList<DB_Almacenes> list_Almacenes = new ArrayList<DB_Almacenes>();
    ArrayList<String> list_vistas = new ArrayList<String>();

    public interface DialogListener {
        public void onDialogClickPositivo(String idAlmacenSeleccionado,	String almacenSeleccionado, String vistaSeleccionado, boolean stockAuto, boolean precioCero);

        public void onDialogClickNegativo();
    }

    DialogListener listener;

    public DialogFragment_preferencias(ArrayList<DB_Almacenes> list_almacenes,	ArrayList<String> list_vistas, String idAlmacenSeleccionado,	String vistaSeleccionado, boolean stockAuto, boolean precioCero) {
        this.list_Almacenes = list_almacenes;
        this.list_vistas = list_vistas;
        this.idAlmacenSeleccionado = idAlmacenSeleccionado;
        this.vistaSeleccionado = vistaSeleccionado;
        this.stockAuto = stockAuto;
        this.precioCero = precioCero;
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

    @SuppressLint("LongLogTag")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View pop = inflater.inflate(R.layout.dialog_preferencias, null);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        alertBuilder.setView(pop);
        alertBuilder.setTitle("Preferencias");

        // --
        final Spinner spn_almacenes = (Spinner) pop.findViewById(R.id.spn_almacenes);
        ArrayAdapter<DB_Almacenes> arrayAdapter = new ArrayAdapter<DB_Almacenes>(getActivity(), android.R.layout.simple_dropdown_item_1line, list_Almacenes);
        // ArrayAdapter<String> arrayAdapter = new
        // ArrayAdapter<String>(getActivity(),android.R.layout.simple_dropdown_item_1line,
        // list_vistas);
        spn_almacenes.setAdapter(arrayAdapter);

        try {
            for (int i = 0; i < list_Almacenes.size(); i++) {
                if ((list_Almacenes.get(i).getCodalm()).equals(idAlmacenSeleccionado)) {
                    spn_almacenes.setSelection(i);// Item seleccionado(obtenido del parametro)
                    break;
                } else {
                    spn_almacenes.setSelection(0);// El primer item seleccionado por defecto
                    Log.d("spinner Almacen", "seleccion por defecto");
                }
            }

        } catch (Exception e) {
            Toast.makeText(getActivity(),
                    "Sincronice para cargar los datos...", Toast.LENGTH_LONG).show();
        }

        spn_almacenes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // Toast.makeText(getActivity(),list_Almacenes.get(position),
                // Toast.LENGTH_SHORT).show();
                almacenSeleccionado = spn_almacenes.getItemAtPosition(position)
                        .toString();
                idAlmacenSeleccionado = list_Almacenes.get(position)
                        .getCodalm();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }

        });

        // --

        final Spinner spn_visualizacion = (Spinner) pop
                .findViewById(R.id.spn_visualizacion);
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line,	list_vistas);
        arrayAdapter2
                .setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spn_visualizacion.setAdapter(arrayAdapter2);

        try {
            for (int i = 0; i < list_vistas.size(); i++) {
                // Log.d("Sincronizar: ","\nspinner:"+spn_visualizacion.getItemAtPosition(i).toString()+"\nvistaSeleccionado_cach�: "+vistaSeleccionado);
                if ((spn_visualizacion.getItemAtPosition(i).toString())
                        .equals(vistaSeleccionado)) {
                    spn_visualizacion.setSelection(i);// Item
                    // seleccionado(obtenido
                    // del parametro)
                    break;
                } else {
                    spn_visualizacion.setSelection(1);// El primer item
                    // seleccionado por
                    // defecto
                    Log.d("spinner Vista", "seleccion por defecto");
                }
            }

        } catch (Exception e) {
            Log.d("SincronizarActivity :dialog:", "lista_vistas esta vacio");
        }

        spn_visualizacion	.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int position, long id) {
                // Toast.makeText(getActivity(),list_vistas.get(position),
                // Toast.LENGTH_SHORT).show();
                vistaSeleccionado = spn_visualizacion
                        .getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }

        });

        // --
        final CheckBox check_stock = (CheckBox) pop.findViewById(R.id.check_stock);
        check_stock.setChecked(stockAuto);
        Log.d("stockAuto obtenido", stockAuto + "");
        check_stock	.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,	boolean isChecked) {
                if (isChecked) {
                    stockAuto = true;
                } else {
                    stockAuto = false;
                }
            }
        });

        final CheckBox check_precioCero = (CheckBox) pop.findViewById(R.id.check_precioCero);
        check_precioCero.setChecked(precioCero);
        Log.d("stockAuto obtenido", precioCero + "");
        check_precioCero.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    precioCero = true;
                } else {
                    precioCero = false;
                }
            }
        });


        alertBuilder.setPositiveButton("Aceptar",	new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                listener.onDialogClickPositivo(idAlmacenSeleccionado, almacenSeleccionado, vistaSeleccionado, stockAuto, precioCero);
            }
        }).setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();

                    }
                });
        return alertBuilder.create();

    }




}
