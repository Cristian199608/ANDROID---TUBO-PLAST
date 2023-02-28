package com.example.sm_tubo_plast.genesys.CreatePDF.GetDays;

import java.util.Calendar;
import java.util.Date;

public class GetDate
{
    public int backMonth(String number)
    {
        int backNumber = 0;

        switch (number) {
            case "01":
                backNumber = 0;
                break;
            case "02":
                backNumber = 1;
                break;
            case "03":
                backNumber = 2;
                break;
            case "04":
                backNumber = 3;
                break;
            case "05":
                backNumber = 4;
                break;
            case "06":
                backNumber = 5;
                break;
            case "07":
                backNumber = 6;
                break;
            case "08":
                backNumber = 7;
                break;
            case "09":
                backNumber = 8;
                break;
            case "10":
                backNumber = 9;
                break;
            case "11":
                backNumber = 10;
                break;
            case "12":
                backNumber = 11;
                break;
        }

        return backNumber;
    }

    public int backDay(String number)
    {
        int backNumber;

        switch (number) {
            case "01":
                backNumber = 1;
                break;
            case "02":
                backNumber = 2;
                break;
            case "03":
                backNumber = 3;
                break;
            case "04":
                backNumber = 4;
                break;
            case "05":
                backNumber = 5;
                break;
            case "06":
                backNumber = 6;
                break;
            case "07":
                backNumber = 7;
                break;
            case "08":
                backNumber = 8;
                break;
            case "09":
                backNumber = 9;
                break;
            default:
                backNumber = Integer.parseInt(number);
                break;
        }

        return backNumber;
    }

    public String getComprobacionFecha(String fecha_inicio, String fecha_fin)
    {

        String[] init = fecha_inicio.split("-");
        String yearInit = init[0];
        String monthInit = init[1];
        String dayInit = init[2];

        String[] end = fecha_fin.split("-");
        String yearFin = end[0];
        String monthFin = end[1];
        String dayFin = end[2];

        Calendar fechaInicial = Calendar.getInstance();
        int initMonth = backMonth(monthInit);
        int initDay = backDay(dayInit);
        fechaInicial.set(Integer.parseInt(yearInit), initMonth, initDay);

        Calendar fechaFinal = Calendar.getInstance();
        int finalMonth = backMonth(monthFin);
        int finalDay = backDay(dayFin);
        fechaFinal.set(Integer.parseInt(yearFin), finalMonth, finalDay);

        Date fecha1Date = fechaInicial.getTime();
        Date fecha2Date = fechaFinal.getTime();

        long diffMilisegundos = fecha2Date.getTime() - fecha1Date.getTime();
        long diffDias = diffMilisegundos / (24 * 60 * 60 * 1000);

        return String.valueOf(diffDias);
    }
}
