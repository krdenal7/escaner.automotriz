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

import com.kds.elm.escanerautomotriz.CustomAdapters.CustomAdapterListDevices;
import com.kds.elm.escanerautomotriz.Interfaces.DialogAlertInterface;
import com.kds.elm.escanerautomotriz.Modelo.ListDevicesPair;
import com.kds.elm.escanerautomotriz.R;

import java.util.ArrayList;

/**
 * Created by Isaac on 05/11/2016.
 * APP's
 */
public class DialogListPairDevice extends DialogFragment implements View.OnClickListener{

    private ArrayList<ListDevicesPair> mAlItems;
    private int mSelectedIndex = -1;
    private DialogAlertInterface listener;

    @SuppressLint("ValidFragment")
    public DialogListPairDevice(ArrayList<ListDevicesPair> mAlItems) {
        this.mAlItems = mAlItems;
    }

    public DialogListPairDevice() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_list_devices,null,false);

        if(savedInstanceState != null){
            mAlItems = savedInstanceState.getParcelableArrayList("mAlItems");
            mSelectedIndex = savedInstanceState.getInt("index");
            DialogParcer parce = savedInstanceState.getParcelable("listener");
            if(parce != null){
                listener = parce.getDialogAlertInterface();
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

        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.rv);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);

        AppCompatButton btnCancelar = (AppCompatButton)view.findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(this);
        AppCompatButton btnBuscar = (AppCompatButton)view.findViewById(R.id.btnBuscar);
        btnBuscar.setOnClickListener(this);
        AppCompatButton btnAceptar = (AppCompatButton)view.findViewById(R.id.btnAceptar);
        btnAceptar.setOnClickListener(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);

        return builder.create();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("mAlItems",mAlItems);
        outState.putInt("index",mSelectedIndex);
        outState.putParcelable("listener",new DialogParcer(listener));
    }

    @Override
    public void onClick(View view) {
        if(listener != null){
            switch (view.getId()){
                case R.id.btnAceptar:
                    listener.OnClickAgree(this);
                    break;
                case R.id.btnBuscar:
                    listener.OnClickDisagree(this);
                    break;
                case R.id.btnCancelar:
                    listener.OnClickCancel(this);
                    break;
            }
        }
    }

    public void setOnDialogListener(DialogAlertInterface listener){
        this.listener = listener;
    }
}
