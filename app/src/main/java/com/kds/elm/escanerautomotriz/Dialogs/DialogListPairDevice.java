package com.kds.elm.escanerautomotriz.Dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.kds.elm.escanerautomotriz.ActivityParent;
import com.kds.elm.escanerautomotriz.CustomAdapters.CustomAdapterListDevices;
import com.kds.elm.escanerautomotriz.Interfaces.IDialogAlertGeneric;
import com.kds.elm.escanerautomotriz.model.DeviceBluetooth;
import com.kds.elm.escanerautomotriz.R;
import com.kds.elm.escanerautomotriz.model.Parcerables.ParseDialogGeneric;

import java.util.ArrayList;

/**
 * Created by Isaac on 05/11/2016.
 * APP's
 */
public class DialogListPairDevice extends DialogFragment implements View.OnClickListener {

    private ArrayList<DeviceBluetooth> mAlItems;
    private int mSelectedIndex = -1;
    private IDialogAlertGeneric listener;

    @SuppressLint("ValidFragment")
    public DialogListPairDevice(ArrayList<DeviceBluetooth> mAlItems) {
        this.mAlItems = mAlItems;
    }

    public DialogListPairDevice() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_list_devices, null, false);

        if (savedInstanceState != null) {
            mAlItems = savedInstanceState.getParcelableArrayList("mAlItems");
            mSelectedIndex = savedInstanceState.getInt("index");

            ParseDialogGeneric parce = savedInstanceState.getParcelable("listener");
            if (parce != null) {
                listener = parce.getListener();
            }
        }

        CustomAdapterListDevices adapter = new CustomAdapterListDevices(mAlItems);
        adapter.setmSelectedPosition(mSelectedIndex);
        adapter.setOnCustomApapterDevListener(new CustomAdapterListDevices.OnCustomAdapterListDevListener() {
            @Override
            public void OnItemCheck(int index) {
                mSelectedIndex = index;
            }
        });

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);

        AppCompatButton btnCancelar = (AppCompatButton) view.findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(this);
        AppCompatButton btnBuscar = (AppCompatButton) view.findViewById(R.id.btnBuscar);
        btnBuscar.setOnClickListener(this);
        AppCompatButton btnAceptar = (AppCompatButton) view.findViewById(R.id.btnAceptar);
        btnAceptar.setOnClickListener(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);

        return builder.create();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("mAlItems", mAlItems);
        outState.putInt("index", mSelectedIndex);
        outState.putParcelable("listener", new ParseDialogGeneric(listener));
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            switch (view.getId()) {
                case R.id.btnAceptar:
                    ((ActivityParent) getActivity()).SaveDeviceBluetooth(mAlItems.get(mSelectedIndex).getNombreDevice(),
                            mAlItems.get(mSelectedIndex).getMacDevice());
                    listener.OnClickAgree(this, mAlItems.get(mSelectedIndex));
                    break;
                case R.id.btnBuscar:
                    listener.OnClickDisagree(this, null);
                    break;
                case R.id.btnCancelar:
                    listener.OnClickCancel(this, null);
                    break;
            }
        }
    }

    public void setOnDialogListener(IDialogAlertGeneric listener) {
        this.listener = listener;
    }
}
