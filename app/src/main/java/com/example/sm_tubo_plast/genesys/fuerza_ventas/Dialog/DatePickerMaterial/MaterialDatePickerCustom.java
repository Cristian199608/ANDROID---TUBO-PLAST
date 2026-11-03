package com.example.sm_tubo_plast.genesys.fuerza_ventas.Dialog.DatePickerMaterial;

import androidx.fragment.app.FragmentManager;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.util.VARIABLES;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class MaterialDatePickerCustom {
    FragmentManager fragmentManager;
    public MaterialDatePickerCustom(FragmentManager fragmentManager){
        this.fragmentManager=fragmentManager;
    }
    MyCallback myCallback;
    public interface MyCallback{
        void result(String fechaddMMyyyy);
    }
    public void show(List<String> lisNolaborable,  Calendar fechaMin,
                     boolean sabadoNoPermitido,
                     MyCallback _myCallback){
        this.myCallback=_myCallback;
//        Calendar fechaMin = Calendar.getInstance(TimeZone.getTimeZone("UTC"));///required UTC
//        fechaMin.set(Calendar.HOUR_OF_DAY, 0);
//        fechaMin.set(Calendar.MINUTE, 0);
//        fechaMin.set(Calendar.SECOND, 0);
//        fechaMin.set(Calendar.MILLISECOND, 0);
//        fechaMin.add(Calendar.DAY_OF_YEAR, 5);

        CalendarConstraints constraints =
                new CalendarConstraints.Builder()
                        .setStart(fechaMin.getTimeInMillis())
                        .setValidator(new ValidadorFechas(lisNolaborable, fechaMin.getTimeInMillis(),sabadoNoPermitido))
                        .build();

        MaterialDatePicker<Long> dpd =
                MaterialDatePicker.Builder.datePicker()
                        .setTitleText("SELECCIONAR FECHA")
                        .setCalendarConstraints(constraints)
                        .setTheme(R.style.ThemeOverlay_MiDatePicker)
                        .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
                        .build();
        dpd.addOnPositiveButtonClickListener(selection -> {
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            formato.setTimeZone(TimeZone.getTimeZone("UTC"));
            String fechaSeleccionada = formato.format(new Date(selection));
            myCallback.result(fechaSeleccionada);
        });

        dpd.show(fragmentManager, "DATE_PICKER");
    }
}
