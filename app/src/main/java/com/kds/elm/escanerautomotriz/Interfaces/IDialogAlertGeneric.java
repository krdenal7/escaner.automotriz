package com.kds.elm.escanerautomotriz.Interfaces;

import android.app.DialogFragment;

/**
 * Created by Isaac Martinez on 03/12/2016.
 * escaner.automotriz
 */

public interface IDialogAlertGeneric {
    void OnClickAgree(DialogFragment dialogFragment, Object object);

    void OnClickDisagree(DialogFragment dialogFragment, Object object);

    void OnClickCancel(DialogFragment dialogFragment, Object object);
}
