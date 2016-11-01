package com.kds.elm.escanerautomotriz.Modelo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by isaac on 09/07/2016.
 * APP's
 */
public class ListDevicesPair implements Parcelable {

    private String NombreDevice;
    private String MacDevice;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.NombreDevice);
        dest.writeString(this.MacDevice);
        dest.writeByte(this.isPaired ? (byte) 1 : (byte) 0);
    }

    public ListDevicesPair(String nombreDevice, String macDevice, boolean isPaired) {
        NombreDevice = nombreDevice;
        MacDevice = macDevice;
        this.isPaired = isPaired;
    }

    protected ListDevicesPair(Parcel in) {
        this.NombreDevice = in.readString();
        this.MacDevice = in.readString();
        this.isPaired = in.readByte() != 0;
    }

    public static final Creator<ListDevicesPair> CREATOR = new Creator<ListDevicesPair>() {
        @Override
        public ListDevicesPair createFromParcel(Parcel source) {
            return new ListDevicesPair(source);
        }

        @Override
        public ListDevicesPair[] newArray(int size) {
            return new ListDevicesPair[size];
        }
    };
}
