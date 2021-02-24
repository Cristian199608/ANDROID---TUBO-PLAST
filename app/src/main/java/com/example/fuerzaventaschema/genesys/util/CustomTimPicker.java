package com.example.fuerzaventaschema.genesys.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;


import com.example.fuerzaventaschema.R;

import java.util.Calendar;
import java.util.Objects;

public class CustomTimPicker implements
        TimePicker.OnTimeChangedListener {

    private final OnTimeSetListener mTimeSetListener;
    private final TimePicker mTimePicker;
    private final int mInitialHourOfDay;
    private final int mInitialMinute;
    private final boolean mIs24HourView;
    private final boolean validar_hora;

    Dialog dialogo;
    TextView txt_hora_message;
    Button txt_aceptar;

    public CustomTimPicker(Activity activity, OnTimeSetListener listener, int mInitialHourOfDay, int mInitialMinute, boolean mIs24HourView, boolean validar_hora) {

        mTimeSetListener = listener;
        this.mInitialHourOfDay = mInitialHourOfDay;
        this.mInitialMinute = mInitialMinute;
        this.mIs24HourView = mIs24HourView;
        this.validar_hora = validar_hora;

        dialogo = new Dialog(activity);
        dialogo.setContentView(R.layout.custom_time_picker_dialog);
        //setButtonPanelLayoutHint(LAYOUT_HINT_SIDE);
        mTimePicker = (TimePicker) dialogo.findViewById(R.id.timePicker);
        txt_hora_message = dialogo.findViewById(R.id.txt_hora_message);
        txt_aceptar = dialogo.findViewById(R.id.txt_aceptar);
        Button txt_cancelar = dialogo.findViewById(R.id.txt_cancelar);
        mTimePicker.setIs24HourView(this.mIs24HourView);
        mTimePicker.setCurrentHour(this.mInitialHourOfDay);
        mTimePicker.setCurrentMinute(this.mInitialMinute);
        mTimePicker.setOnTimeChangedListener(this::onTimeChanged);


        txt_hora_message.setText("");
        txt_aceptar.setOnClickListener(v -> onclickPositivo());
        txt_cancelar.setOnClickListener(v -> onclickNegativo());

    }

    public void Show(){dialogo.show();}

    public void onclickPositivo(){
        if (mTimeSetListener != null) {
            String mensaje=mTimeSetListener.onTimeSet(mTimePicker, mTimePicker.getCurrentHour(),mTimePicker.getCurrentMinute());
            if (mensaje.length()==0){
                dialogo.dismiss();
            }else{
                txt_hora_message.setTextColor(dialogo.getContext().getResources().getColor(R.color.red_400));
                txt_hora_message.setText(""+mensaje);
            }
        }
    }

    public void onclickNegativo(){
        dialogo.dismiss();
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        txt_hora_message.setText("");
    }

    public interface OnTimeSetListener {
        String onTimeSet(TimePicker view, int hourOfDay, int minute);
    }

}
