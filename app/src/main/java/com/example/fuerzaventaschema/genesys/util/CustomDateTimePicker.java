package com.example.fuerzaventaschema.genesys.util;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.fuerzaventaschema.R;

import java.util.Calendar;
import java.util.TimeZone;

public class CustomDateTimePicker implements  TimePicker.OnTimeChangedListener, DatePicker.OnDateChangedListener{
    private final OnTimeSetListener mTimeSetListener;
    private final TimePicker mTimePicker;
    private final int mInitialHourOfDay;
    private final int mInitialMinute;
    private final boolean mIs24HourView;
    private final boolean validar_hora;
    DatePicker datePicker;

    Dialog dialogo;
    TextView txt_hora_message;
    Button txt_aceptar, btn_continuar;

    public CustomDateTimePicker(Activity activity, OnTimeSetListener listener, int mInitialHourOfDay, int mInitialMinute, boolean mIs24HourView, boolean validar_hora) {

        mTimeSetListener = listener;
        this.mInitialHourOfDay = mInitialHourOfDay;
        this.mInitialMinute = mInitialMinute;
        this.mIs24HourView = mIs24HourView;
        this.validar_hora = validar_hora;

        dialogo = new Dialog(activity);
        dialogo.setContentView(R.layout.custom_datetime_picker);
        //setButtonPanelLayoutHint(LAYOUT_HINT_SIDE);
        LinearLayout LinearContenedor = (LinearLayout) dialogo.findViewById(R.id.LinearContenedor);
        mTimePicker = (TimePicker) dialogo.findViewById(R.id.timePicker);
        datePicker = (DatePicker) dialogo.findViewById(R.id.datePicker);
        txt_hora_message = dialogo.findViewById(R.id.txt_hora_message);
        txt_aceptar = dialogo.findViewById(R.id.txt_aceptar);
        btn_continuar = dialogo.findViewById(R.id.btn_continuar);
        Button txt_cancelar = dialogo.findViewById(R.id.txt_cancelar);
        TextView txt_fecha = dialogo.findViewById(R.id.txt_fecha);
        TextView txt_hora = dialogo.findViewById(R.id.txt_hora);
        mTimePicker.setIs24HourView(this.mIs24HourView);
        mTimePicker.setCurrentHour(this.mInitialHourOfDay);
        mTimePicker.setCurrentMinute(this.mInitialMinute);
        mTimePicker.setOnTimeChangedListener(this::onTimeChanged);



        txt_fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.setVisibility(View.VISIBLE);
                mTimePicker.setVisibility(View.GONE);

                txt_cancelar.setVisibility(View.GONE);
                txt_aceptar.setVisibility(View.GONE);
                btn_continuar.setVisibility(View.VISIBLE);

                txt_hora.setTextColor(activity.getResources().getColor(R.color.blue_200));
                txt_fecha.setTextColor(activity.getResources().getColor(R.color.white));

            }
        });
        txt_hora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimePicker.setVisibility(View.VISIBLE);
                datePicker.setVisibility(View.GONE);

                txt_cancelar.setVisibility(View.VISIBLE);
                txt_aceptar.setVisibility(View.VISIBLE);
                btn_continuar.setVisibility(View.GONE);

                txt_hora.setTextColor(activity.getResources().getColor(R.color.white));
                txt_fecha.setTextColor(activity.getResources().getColor(R.color.blue_200));
            }
        });

        btn_continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_hora.performClick();
            }
        });

        txt_fecha.performClick();

        txt_hora_message.setText("");
        txt_aceptar.setOnClickListener(v -> onclickPositivo());
        txt_cancelar.setOnClickListener(v -> onclickNegativo());

        mTimePicker.setVisibility(View.GONE);
        int altura=LinearContenedor.getLayoutParams().height;
        LinearContenedor.getLayoutParams().height=altura;
        DatePicketCustom();
    }
    public void DatePicketCustom(){

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        datePicker.setMinDate(calendar.getTimeInMillis());
    }
    public void Show(){dialogo.show();}

    public void onclickPositivo(){
        if (mTimeSetListener != null) {

            //obtenemos fecha

            Calendar myCalendar = Calendar.getInstance();
            myCalendar.set(Calendar.YEAR, datePicker.getYear());
            myCalendar.set(Calendar.MONTH, datePicker.getMonth());
            myCalendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
            myCalendar.set(Calendar.HOUR_OF_DAY, mTimePicker.getCurrentHour());
            myCalendar.set(Calendar.MINUTE, mTimePicker.getCurrentMinute());


            String mensaje=mTimeSetListener.onDateTimeSet( myCalendar);
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

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

    }

    public interface OnTimeSetListener {
        String onDateTimeSet(Calendar myCalendar);
    }
}
