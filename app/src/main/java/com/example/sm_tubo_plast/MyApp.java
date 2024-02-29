package com.example.sm_tubo_plast;

import android.app.Application;

import com.example.sm_tubo_plast.crashlitics.domain.CrashlyticsUseCases;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        new CrashlyticsUseCases().initCrashlyticsUseCase(this);
    }
}
