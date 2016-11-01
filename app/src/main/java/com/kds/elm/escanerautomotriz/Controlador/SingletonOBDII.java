package com.kds.elm.escanerautomotriz.Controlador;

import android.content.Context;

/**
 * Created by Isaac Martinez on 29/10/2016.
 * escaner.automotriz
 */

public class SingletonOBDII {

    private static SingletonOBDII singletonOBDII;
    private Context mContext;

    public static void InitInstance(Context mContext){
        singletonOBDII = new SingletonOBDII(mContext);
    }

    public static synchronized SingletonOBDII getInstance(){
        return singletonOBDII;
    }

    public SingletonOBDII(Context mContext){
        this.mContext = mContext;
    }

}
