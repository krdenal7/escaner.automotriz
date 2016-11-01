package com.kds.elm.escanerautomotriz.Util;

/**
 * Created by isaac on 09/07/2016.
 * APP's
 */
public class Utils {

    public void SleepTime(long mili){
        try {
            Thread.sleep(mili);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
