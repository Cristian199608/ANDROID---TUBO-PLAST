package com.example.sm_tubo_plast.genesys.fuerza_ventas.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.fuerza_ventas.SeguimientoPedidoActivity;

public class MyAlertDialogFragment extends DialogFragment {

    public static MyAlertDialogFragment newInstance(int title) {
        MyAlertDialogFragment frag = new MyAlertDialogFragment();
        Bundle args = new Bundle();
        args.putInt("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt("title");

        return new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.icon_error)
                .setTitle(title)
                .setPositiveButton("ok", null)
                .setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            //((SeguimientoPedidoActivity)getActivity()).BuscarOpervidor();

                    }
                })
                .create();
    }
}
