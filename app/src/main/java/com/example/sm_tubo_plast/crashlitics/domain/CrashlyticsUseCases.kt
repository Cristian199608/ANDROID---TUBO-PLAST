package com.example.sm_tubo_plast.crashlitics.domain

import android.content.Context
import com.example.sm_tubo_plast.R
import com.example.sm_tubo_plast.crashlitics.model.CRASHLYTICS_kEY
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics

class CrashlyticsUseCases {
    fun initCrashlyticsUseCase(context: Context) {
        FirebaseApp.initializeApp(context)
        val crashlytics = FirebaseCrashlytics.getInstance()
        crashlytics.setCrashlyticsCollectionEnabled(true)

        crashlytics.setCustomKey(
            CRASHLYTICS_kEY.NRO_VERSION.name,
            context.getResources().getString(R.string.APP_VERSION)
        )

    }

    fun setConfigVendedorDataUseCase(codigoVendedor: String, nomven:String) {
        val crashlytics = FirebaseCrashlytics.getInstance()
        crashlytics.setCustomKey(
            CRASHLYTICS_kEY.CODIGO_VENDEDOR.name,
            codigoVendedor
        )
        crashlytics.setCustomKey(
            CRASHLYTICS_kEY.NOMBRE_VENDEDOR.name,
            nomven
        )

    }
}