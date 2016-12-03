package com.kds.elm.escanerautomotriz.model.Parcerables;

import android.os.Parcel;
import android.os.Parcelable;

import com.kds.elm.escanerautomotriz.Interfaces.IDialogSearch;

/**
 * Created by Isaac Martinez on 12/11/2016.
 * escaner.automotriz
 */

public class ParceDialogSearch implements Parcelable{

    private IDialogSearch iDialogSearch;

    public ParceDialogSearch(IDialogSearch iDialogSearch) {
        this.iDialogSearch = iDialogSearch;
    }

    private ParceDialogSearch(Parcel in) {
    }

    public IDialogSearch getiDialogSearch() {
        return iDialogSearch;
    }

    public static final Creator<ParceDialogSearch> CREATOR = new Creator<ParceDialogSearch>() {
        @Override
        public ParceDialogSearch createFromParcel(Parcel in) {
            return new ParceDialogSearch(in);
        }

        @Override
        public ParceDialogSearch[] newArray(int size) {
            return new ParceDialogSearch[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
