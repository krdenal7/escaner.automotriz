package com.kds.elm.escanerautomotriz.Dialogs;

import android.os.Parcel;
import android.os.Parcelable;

import com.kds.elm.escanerautomotriz.Interfaces.DialogAlertInterface;

/**
 * Created by Isaac Martinez on 29/10/2016.
 * escaner.automotriz
 */

public class DialogParcer implements Parcelable {

    private DialogAlertInterface dialogAlertInterface;

    public DialogParcer(DialogAlertInterface dialogAlertInterface){
        this.dialogAlertInterface = dialogAlertInterface;
    }

    protected DialogParcer(Parcel in) {
    }

    public static final Creator<DialogParcer> CREATOR = new Creator<DialogParcer>() {
        @Override
        public DialogParcer createFromParcel(Parcel in) {
            return new DialogParcer(in);
        }

        @Override
        public DialogParcer[] newArray(int size) {
            return new DialogParcer[size];
        }
    };

    public DialogAlertInterface getDialogAlertInterface(){
        return this.dialogAlertInterface;
    }

    public void setDialogAlertInterface(DialogAlertInterface dialogAlertInterface){
        this.dialogAlertInterface = dialogAlertInterface;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
