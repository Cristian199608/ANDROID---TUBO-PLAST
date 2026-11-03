package com.example.sm_tubo_plast.genesys.fuerza_ventas.Dialog.DatePickerMaterial;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.sm_tubo_plast.genesys.util.VARIABLES;
import com.google.android.material.datepicker.CalendarConstraints;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Set;



public class ValidadorFechas implements CalendarConstraints.DateValidator {

    private final Set<String> fechasFeriadas;
    private final long minDate;
    boolean sabadoNoPermitido;
    //private final long maxDate;

    public ValidadorFechas(
            List<String> fechas,
            long minDate,
            boolean sabadoNoPermitido
    ) {
        this.fechasFeriadas = new HashSet<>(fechas);
        this.minDate = minDate;
        this.sabadoNoPermitido=sabadoNoPermitido;
        //this.maxDate = maxDate;
    }

    protected ValidadorFechas(Parcel in) {
        fechasFeriadas = new HashSet<>();
        List<String> fechas = in.createStringArrayList();
        if (fechas != null) {
            fechasFeriadas.addAll(fechas);
        }

        minDate = in.readLong();
        //maxDate = in.readLong();
    }

    @Override
    public boolean isValid(long fechaMillis) {

        // Normalizar la fecha seleccionada a UTC
        Calendar fecha = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        fecha.setTimeInMillis(fechaMillis);

        fecha.set(Calendar.HOUR_OF_DAY, 0);
        fecha.set(Calendar.MINUTE, 0);
        fecha.set(Calendar.SECOND, 0);
        fecha.set(Calendar.MILLISECOND, 0);

        long fechaUTC = fecha.getTimeInMillis();

        // Bloquear fechas fuera del rango
        if (fechaUTC < minDate /*|| fechaUTC > maxDate*/) {
            return false;
        }

        // Bloquear domingos
        if (sabadoNoPermitido && fecha.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            return false;
        }

        // Formato: dd-MM-yyyy
        String fechaTexto = String.format(
                Locale.US,
                "%02d/%02d/%04d",
                fecha.get(Calendar.DAY_OF_MONTH),
                fecha.get(Calendar.MONTH) + 1,
                fecha.get(Calendar.YEAR)
        );

        // Bloquear feriados
        return !fechasFeriadas.contains(fechaTexto);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(new ArrayList<>(fechasFeriadas));
        dest.writeLong(minDate);
        //dest.writeLong(maxDate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<ValidadorFechas> CREATOR =
            new Parcelable.Creator<ValidadorFechas>() {
                @Override
                public ValidadorFechas createFromParcel(Parcel in) {
                    return new ValidadorFechas(in);
                }

                @Override
                public ValidadorFechas[] newArray(int size) {
                    return new ValidadorFechas[size];
                }
            };
}


