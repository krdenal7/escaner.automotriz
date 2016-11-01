package com.kds.elm.escanerautomotriz.Controlador;

import android.app.Application;

/**
 * Created by Isaac Martinez on 29/10/2016.
 * escaner.automotriz
 */

public class ScannerAplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        InitSingleton();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    private void InitSingleton(){
        SingletonOBDII.InitInstance(this);
    }
}
