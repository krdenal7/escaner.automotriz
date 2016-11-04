package com.kds.elm.escanerautomotriz.BroadCast;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.kds.elm.escanerautomotriz.Interfaces.ManagerBluetoothInterface;
import com.kds.elm.escanerautomotriz.Modelo.ListDevicesPair;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Isaac Martinez on 29/10/2016.
 * escaner.automotriz
 */

public class ManagerBluetooth extends BroadcastReceiver {

    private static Context mContext;
    private static ManagerBluetoothInterface listener;
    private static IntentFilter mFilter;
    private static ManagerBluetooth mManager;

    private static ArrayList<ListDevicesPair> mItemsDevices = new ArrayList<>();
    private static BluetoothAdapter mBluetoothAdapter;

    public static ManagerBluetooth  newInstance(Context context){
        if(mManager == null){
            ManagerBluetooth bluetooth = new ManagerBluetooth();
            mFilter = new IntentFilter();
            mContext = context;
            mManager = bluetooth;
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        return mManager;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(listener != null)
        switch (action){
            case BluetoothAdapter.ACTION_STATE_CHANGED:
                listener.OnStateBluetoothChange(isBluetoothActive());
                break;
            case BluetoothDevice.ACTION_FOUND:
                ChangeListDevice(intent);
                break;
        }
    }

    private void ChangeListDevice(Intent intent){
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        mItemsDevices.add(new ListDevicesPair(
                device.getName(),
                device.getAddress(),
                isPairedDevice(device.getAddress())
        ));
    }

    private boolean isPairedDevice(String mac){
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        for(BluetoothDevice bt : pairedDevices) {
            if(mac.equalsIgnoreCase(bt.getAddress()))
                return true;
        }
        return false;
    }

    public  boolean isBluetoothActive() {
        return mBluetoothAdapter != null && mBluetoothAdapter.isEnabled();
    }

    public void OnDeviceBluetooth(){
        Intent intent =new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        ((Activity)mContext).startActivityForResult(intent,1);
    }


    public void OffDeviceBluetooth(){
         mBluetoothAdapter.disable();
    }

    public void setOnBluetoothListener(ManagerBluetoothInterface listener){
        ManagerBluetooth.listener = listener;
    }

    public void RegisterReceiver(){
        mContext.registerReceiver(mManager,mFilter);
    }

    public void UnRegisterReceiver(){
        mContext.unregisterReceiver(mManager);
    }
}
