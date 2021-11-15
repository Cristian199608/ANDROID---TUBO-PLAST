package com.example.sm_tubo_plast.genesys.util

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sm_tubo_plast.R
import com.google.android.material.datepicker.MaterialDatePicker
import java.time.Instant

class SeleccionarRangoFechas_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seleccionar_rango_fechas);

        val dateRangePicker =
                MaterialDatePicker.Builder.dateRangePicker()
                        .setTitleText("Select dates")
                        .build()

        dateRangePicker.show(supportFragmentManager, "DATE_PICKER")
        dateRangePicker.addOnPositiveButtonClickListener { selection -> {
            var dd:String
            dd="";
        } }

    }
}