package com.kds.elm.escanerautomotriz.Interfaces;

import android.app.DialogFragment;

import com.kds.elm.escanerautomotriz.model.DeviceBluetooth;

import java.util.ArrayList;

/**
 * Created by Isaac Martinez on 07/11/2016.
 * escaner.automotriz
 */

public interface IDialogSearch {
    void OnClickAceptar(DialogFragment dialogFragment,DeviceBluetooth device);
    void OnClickBuscar();
    void OnClickCancelar(DialogFragment dialogFragment);
    void OnFinishCreated();
    ArrayList<DeviceBluetooth> OnDataChangeDevice();
}
