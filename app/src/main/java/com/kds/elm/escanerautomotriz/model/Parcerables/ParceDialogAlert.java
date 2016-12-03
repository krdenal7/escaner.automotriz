package com.kds.elm.escanerautomotriz.model.Parcerables;

import android.os.Parcel;
import android.os.Parcelable;

import com.kds.elm.escanerautomotriz.Interfaces.IDialogAlert;

/**
 * Created by Isaac Martinez on 29/10/2016.
 * escaner.automotriz
 */

public class ParceDialogAlert implements Parcelable {

    private IDialogAlert dialogAlertInterface;

    public ParceDialogAlert(IDialogAlert dialogAlertInterface){
        this.dialogAlertInterface = dialogAlertInterface;
    }

    private ParceDialogAlert(Parcel in) {
    }

    public static final Creator<ParceDialogAlert> CREATOR = new Creator<ParceDialogAlert>() {
        @Override
        public ParceDialogAlert createFromParcel(Parcel in) {
            return new ParceDialogAlert(in);
        }

        @Override
        public ParceDialogAlert[] newArray(int size) {
            return new ParceDialogAlert[size];
        }
    };

    public IDialogAlert getDialogAlertInterface(){
        return this.dialogAlertInterface;
    }

    public void setDialogAlertInterface(IDialogAlert dialogAlertInterface){
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
