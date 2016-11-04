package com.kds.elm.escanerautomotriz.BroadCast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.kds.elm.escanerautomotriz.ActivityParent;

/**
 * Created by isaac on 09/07/2016.
 * APP's
 */
public class BroadCastBluetooth extends BroadcastReceiver {

    public static ActivityParent menuDrawer;

    public void setMenuDrawer(ActivityParent menuDrawer){
        BroadCastBluetooth.menuDrawer =menuDrawer;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

    }
}
