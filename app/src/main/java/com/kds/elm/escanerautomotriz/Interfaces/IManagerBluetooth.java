package com.kds.elm.escanerautomotriz.Interfaces;

import android.bluetooth.BluetoothDevice;

import com.kds.elm.escanerautomotriz.model.DeviceBluetooth;

/**
 * Created by Isaac Martinez on 29/10/2016.
 * escaner.automotriz
 */

public interface IManagerBluetooth {
    void OnStateBluetoothChange(boolean isActive);
    void OnStartDiscovery();
    void OnStopDiscovery();
    void OnDeviceFound(DeviceBluetooth device);
    void OnBonding(BluetoothDevice device);
    void OnBondend(BluetoothDevice device);
    void OnBondend_none(BluetoothDevice device);
    void OnConnectedDevice(Boolean isConnected,DeviceBluetooth deviceBluetooth);
    void OnDisconnectedDevice(DeviceBluetooth deviceBluetooth);
}
