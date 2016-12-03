package com.kds.elm.escanerautomotriz.model.Parcerables;

import android.os.Parcel;
import android.os.Parcelable;

import com.kds.elm.escanerautomotriz.Interfaces.IDialogAlertGeneric;

/**
 * Created by Isaac Martinez on 03/12/2016.
 * escaner.automotriz
 */

public class ParseDialogGeneric implements Parcelable {

    private IDialogAlertGeneric listener;

    public ParseDialogGeneric(IDialogAlertGeneric listener) {
        this.listener = listener;
    }

    public IDialogAlertGeneric getListener() {
        return listener;
    }

    public void setListener(IDialogAlertGeneric listener) {
        this.listener = listener;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public ParseDialogGeneric() {
    }

    protected ParseDialogGeneric(Parcel in) {
        this.listener = in.readParcelable(IDialogAlertGeneric.class.getClassLoader());
    }

    public static final Parcelable.Creator<ParseDialogGeneric> CREATOR = new Parcelable.Creator<ParseDialogGeneric>() {
        @Override
        public ParseDialogGeneric createFromParcel(Parcel source) {
            return new ParseDialogGeneric(source);
        }

        @Override
        public ParseDialogGeneric[] newArray(int size) {
            return new ParseDialogGeneric[size];
        }
    };
}
