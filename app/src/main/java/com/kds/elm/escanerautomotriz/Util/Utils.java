package com.kds.elm.escanerautomotriz.Util;


import com.kds.elm.escanerautomotriz.model.DeviceBluetooth;

import java.util.ArrayList;

/**
 * Created by isaac on 09/07/2016.
 * APP's
 */
public class Utils {

    public static boolean ValidaItemDevice(ArrayList<DeviceBluetooth> list,DeviceBluetooth deviceBluetooth){

        for(DeviceBluetooth dev:list){

            if(dev.getMacDevice().equalsIgnoreCase(deviceBluetooth.getMacDevice())){
                return false;
            }

        }
        return true;
    }

}
