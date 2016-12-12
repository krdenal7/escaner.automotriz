package com.kds.elm.escanerautomotriz.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by isaac on 09/07/2016.
 * APP's
 */
public class DeviceBluetooth implements Parcelable {

    private String NombreDevice;
    private String MacDevice;
    private String ClassDevice;
    private boolean isPaired;


    public String getNombreDevice() {
        return NombreDevice;
    }

    public void setNombreDevice(String nombreDevice) {
        NombreDevice = nombreDevice;
    }

    public String getMacDevice() {
        return MacDevice;
    }

    public void setMacDevice(String macDevice) {
        MacDevice = macDevice;
    }

    public String getClassDevice() {
        return ClassDevice;
    }

    public void setClassDevice(String classDevice) {
        ClassDevice = classDevice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.NombreDevice);
        dest.writeString(this.MacDevice);
        dest.writeString(this.ClassDevice);
        dest.writeByte(this.isPaired ? (byte) 1 : (byte) 0);
    }

    public DeviceBluetooth(String nombreDevice, String macDevice, String classDevice, boolean isPaired) {
        NombreDevice = nombreDevice;
        MacDevice = macDevice;
        ClassDevice = classDevice;
        this.isPaired = isPaired;
    }

    public DeviceBluetooth() {
    }

    protected DeviceBluetooth(Parcel in) {
        this.NombreDevice = in.readString();
        this.MacDevice = in.readString();
        this.ClassDevice = in.readString();
        this.isPaired = in.readByte() != 0;
    }

    public static final Creator<DeviceBluetooth> CREATOR = new Creator<DeviceBluetooth>() {
        @Override
        public DeviceBluetooth createFromParcel(Parcel source) {
            return new DeviceBluetooth(source);
        }

        @Override
        public DeviceBluetooth[] newArray(int size) {
            return new DeviceBluetooth[size];
        }
    };
}

