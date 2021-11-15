package com.example.sm_tubo_plast.genesys.util;

import android.app.Activity;
import android.app.Dialog;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.sm_tubo_plast.R;

import java.text.ParseException;
import java.util.Calendar;

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

    int limit_min_minuto=-1;
    long long_fecha_referencia_validar=-1;

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
            String mensaje0="";//validarMinutosRetraso( mTimePicker.getCurrentHour(),mTimePicker.getCurrentMinute());
            if (mensaje0.length()==0){
                String mensaje=mTimeSetListener.onTimeSet(mTimePicker, mTimePicker.getCurrentHour(),mTimePicker.getCurrentMinute());
                if (mensaje.length()==0){
                    dialogo.dismiss();
                }else{
                    txt_hora_message.setTextColor(dialogo.getContext().getResources().getColor(R.color.red_400));
                    txt_hora_message.setText(""+mensaje);
                }
            }
        }
    }

    public void onclickNegativo(){
        dialogo.dismiss();
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

        //validarMinutosRetraso(hourOfDay, minute);
    }


    private String validarMinutosRetraso( int hourOfDay, int minute){
        String mensaje="";

        if (limit_min_minuto!=-1){
            //Falta trabajar aquí
            final String CERO = "0";
            String horaFormateada =  String.valueOf((hourOfDay < 10)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay));
            String minutoFormateada =  String.valueOf((minute < 10)? String.valueOf(CERO + minute) : String.valueOf(minute));
            horaFormateada=horaFormateada+":"+minutoFormateada;

            int segundosInicio= 0;
            try {
                segundosInicio = VARIABLES.GetSegundosFrom_hh_mm_to_int2(hourOfDay, minute);
                final Calendar c = Calendar.getInstance();

                String dd=VARIABLES.convertirFecha_from_long(long_fecha_referencia_validar);
                String ff="";

                if (long_fecha_referencia_validar!=-1){
                    //c.setTimeInMillis(long_fecha_referencia_validar);

                    dd=VARIABLES.convertirFecha_from_long(c.getTimeInMillis());
                     ff="";

                }
                c.setTimeZone(VARIABLES.zona_horaria
                );
                int sdd=c.get(Calendar.HOUR_OF_DAY);
                int min=c.get(Calendar.MINUTE);
                int segundos_actual=c.get(Calendar.HOUR_OF_DAY)*3600+c.get(Calendar.MINUTE)*60+c.get(Calendar.SECOND);

                long minimo_rquerido=segundos_actual-(limit_min_minuto*60);
                if (segundosInicio<minimo_rquerido && limit_min_minuto!=-1){
                    mensaje="Debes seleccionar la hora y minuto equivalente hasta "+(limit_min_minuto/60)+" horas con " +(limit_min_minuto%60)+" minutos de retraso";
                    //mensaje="Debes seleccionar la hora y el minuto  máximo de "+(limit_min_minuto/60)+" horas de retraso";
                }else{
                    mensaje="";
                }

            } catch (ParseException e) {//Buen aporte estimada, tu comentario nos ayuda a seguir mejorando. Gracias
                mensaje="Error en la validación de fecha";
                e.printStackTrace();
            }
            if (mensaje.length()>0){
                txt_aceptar.setEnabled(false);
                txt_hora_message.setTextColor(dialogo.getContext().getResources().getColor(R.color.red_400));
            }else{
                txt_aceptar.setEnabled(true);
            }
        }
        txt_hora_message.setText(mensaje);
        return mensaje;
    }

    public void setLimit_min_minuto(int limit_min_minuto) {
        this.limit_min_minuto = limit_min_minuto;
    }

    public void setLong_fecha_referencia_validar(long long_fecha_referencia_validar) {
        this.long_fecha_referencia_validar = long_fecha_referencia_validar;
        String dd=VARIABLES.convertirFecha_from_long(long_fecha_referencia_validar);
        String ff="";
    }

    public interface OnTimeSetListener {
        String onTimeSet(TimePicker view, int hourOfDay, int minute);
    }

}
