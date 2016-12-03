package com.kds.elm.escanerautomotriz.Managers;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.kds.elm.escanerautomotriz.Interfaces.IManagerBluetooth;
import com.kds.elm.escanerautomotriz.model.DeviceBluetooth;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Isaac Martinez on 29/10/2016.
 * escaner.automotriz
 */

public class ManagerBluetooth {

    private static Context mContext;
    private static IManagerBluetooth listener;
    private static IntentFilter mFilter;
    private static ManagerBluetooth mManager;
    private static BroadcastReceiver receiver;

    private String TAG = "TAG - "+ManagerBluetooth.class.getSimpleName();


    private static BluetoothAdapter mBluetoothAdapter;

    public static ManagerBluetooth  newInstance(Context context){
        mContext = context;

        if(mManager == null){
            ManagerBluetooth bluetooth = new ManagerBluetooth();
            mFilter = new IntentFilter();
            mFilter.addAction(BluetoothDevice.ACTION_FOUND);
            mFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
            mFilter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
            mFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
            mFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            mFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
            mManager = bluetooth;
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }

        return mManager;
    }

    private void ChangeListDevice(Intent intent){
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        Log.e(TAG,device.getName());
        listener.OnDeviceFound(new DeviceBluetooth(
                device.getName(),
                device.getAddress(),
                String.valueOf(device.getType()),
                isPairedDevice(device.getAddress())));
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
        mBluetoothAdapter.enable();
    }

    public void OffDeviceBluetooth(){
         mBluetoothAdapter.disable();
    }

    public void setOnBluetoothListener(IManagerBluetooth listener){
        ManagerBluetooth.listener = listener;
    }

    public void RegisterReceiver(){
        Log.e(TAG,"RegisterReceiver");
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                Log.e(TAG,action);
                if(listener != null)
                    switch (action){
                        case BluetoothAdapter.ACTION_STATE_CHANGED:
                            listener.OnStateBluetoothChange(isBluetoothActive());
                            break;
                        case BluetoothDevice.ACTION_FOUND:
                            ChangeListDevice(intent);
                            break;
                        case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                            Log.e("TAG","STAR_DISCOVERY");
                            listener.OnStartDiscovery();
                            break;
                        case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                            Log.e("TAG","END_DISCOVERY");
                            listener.OnStopDiscovery();
                            break;
                        case BluetoothDevice.ACTION_BOND_STATE_CHANGED:
                            ChangeStateBondend(intent);
                            break;
                    }
            }
        };
        mContext.registerReceiver(receiver,mFilter);
    }

    public void UnRegisterReceiver(){
        try {
            Log.e(TAG,"UnRegisterReceiver");
            mContext.unregisterReceiver(receiver);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void StartDiscovery(){
        mBluetoothAdapter.startDiscovery();
    }

    public void StopDiscovery(){
        if(mBluetoothAdapter.isDiscovering()){
            mBluetoothAdapter.cancelDiscovery();
        }
    }

    public ArrayList<DeviceBluetooth> getListPairDevice(){
        ArrayList<DeviceBluetooth> list = new ArrayList<>();

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        for(BluetoothDevice bt : pairedDevices) {
            DeviceBluetooth pair = new DeviceBluetooth(bt.getName(),bt.getAddress(),String.valueOf(bt.getType()),true);
            list.add(pair);
        }

        return list;
    }

    public void PairingDevice(String mac){
        try {
            BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(mac);
            Method method = device.getClass().getMethod("createBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
        }catch (Exception e){
            Log.e(TAG,e.toString());
        }
    }

    private void ChangeStateBondend(Intent intent){
      BluetoothDevice  device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        switch (device.getBondState()){
            case BluetoothDevice.BOND_BONDING:
                listener.OnBonding(device);
                break;
            case  BluetoothDevice.BOND_BONDED:
                listener.OnBondend(device);
                break;
            case BluetoothDevice.BOND_NONE:
                listener.OnBondend_none(device);
                break;
        }
    }

    private boolean ConnectSocketBluetooth(){
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        });
        return false;
    }

}
