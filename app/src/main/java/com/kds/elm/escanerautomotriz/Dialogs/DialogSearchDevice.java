package com.kds.elm.escanerautomotriz.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kds.elm.escanerautomotriz.CustomAdapters.CustomAdapterListDevices;
import com.kds.elm.escanerautomotriz.Interfaces.IDialogSearch;
import com.kds.elm.escanerautomotriz.model.DeviceBluetooth;
import com.kds.elm.escanerautomotriz.R;
import com.kds.elm.escanerautomotriz.model.Parcerables.ParceDialogSearch;

import java.util.ArrayList;

/**
 * Created by Isaac Martinez on 12/11/2016.
 * escaner.automotriz
 */

public class DialogSearchDevice extends DialogFragment implements View.OnClickListener {

    private AppCompatButton btnAceptar;
    private AppCompatButton btnBuscar;
    private ProgressBar progressBar;
    private TextView txtMensaje;
    private IDialogSearch listener;
    private RecyclerView recyclerView;
    private ArrayList<DeviceBluetooth> mAlItemsDevice;
    private int IndexSelected = -1;
    private boolean isSearch = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.e("TAG","onCreateDialog");
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_dialog_search_device, null, false);

        if(savedInstanceState != null){
            IndexSelected = savedInstanceState.getInt("IndexSelected");
            isSearch = savedInstanceState.getBoolean("isSearch");
            ParceDialogSearch parceDialogSearch = savedInstanceState.getParcelable("listener");
            if(parceDialogSearch != null){
                listener = parceDialogSearch.getiDialogSearch();
                mAlItemsDevice = listener.OnDataChangeDevice();
            }
        }

        btnAceptar = (AppCompatButton) view.findViewById(R.id.btnAceptar);
        btnBuscar = (AppCompatButton) view.findViewById(R.id.btnBuscar);
        AppCompatButton btnCancelar = (AppCompatButton) view.findViewById(R.id.btnCancelar);
        progressBar = (ProgressBar) view.findViewById(R.id.progressSearch);
        txtMensaje = (TextView) view.findViewById(R.id.txtSarch);
        recyclerView = (RecyclerView)view.findViewById(R.id.rvSearch);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        SetApapter();

        btnAceptar.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);
        btnBuscar.setOnClickListener(this);



        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);

        if(savedInstanceState == null) {
            OnEnabledButtonAceptar(false);
            OnChangeStartedDiscovery(true);
            ChangeStyleTextView(true);
            if(listener != null){
                listener.OnFinishCreated();
            }
        }else {
           if(isSearch){
               ChangeStyleTextView(true);
               OnChangeStartedDiscovery(true);
           }else {
               ChangeStyleTextView(false);
               OnChangeStartedDiscovery(false);
           }
            if(mAlItemsDevice != null){
                if(mAlItemsDevice.size()>0){
                    OnEnabledButtonAceptar(true);
                }else {
                    OnEnabledButtonAceptar(false);
                }
            }else {
                OnEnabledButtonAceptar(false);
            }
        }


        return builder.create();
    }

    public void ChangeStyleTextView(boolean isSearch){
        try {
            if (txtMensaje != null)
                if (isSearch) {
                    txtMensaje.setTextSize(14.0f);
                    txtMensaje.setTextColor(getResources().getColor(R.color.GrisLetra));
                    txtMensaje.setText(getString(R.string.Buscando__));
                } else {
                    txtMensaje.setTextSize(24.0f);
                    txtMensaje.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txtMensaje.setText(getString(R.string.Selecciona_));
                }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void OnEnabledButtonAceptar(boolean enabled) {
        try {
            if (btnAceptar != null) {
                if (enabled && !btnAceptar.isEnabled()) {
                    btnAceptar.setEnabled(true);
                    btnAceptar.setTextColor(getResources().getColor(R.color.colorPrimary));
                } else if (!enabled) {
                    btnAceptar.setTextColor(getResources().getColor(R.color.colorDisable));
                    btnAceptar.setEnabled(false);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void OnChangeStartedDiscovery( boolean isStart) {
        try {
            if (btnBuscar != null) {
                if (isStart) {
                    btnBuscar.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    btnBuscar.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setOnDialogSearchListener(IDialogSearch listener){
        this.listener = listener;
    }

    public void OnDataChange(){
        if(listener != null){
            Log.e("TAG","Size: "+listener.OnDataChangeDevice().size());
           SetApapter();
        }else {
            Log.e("TAG","OnDataChange - null");
        }
    }

    private void SetApapter() {
            mAlItemsDevice = listener.OnDataChangeDevice();
             if(mAlItemsDevice != null) {
                 CustomAdapterListDevices mAdapter = new CustomAdapterListDevices(mAlItemsDevice);
                 if(IndexSelected != -1){
                     mAdapter.setmSelectedPosition(IndexSelected);
                 }
                 mAdapter.setOnCustomApapterDevListener(new CustomAdapterListDevices.OnCustomAdapterListDevListener() {
                     @Override
                     public void OnItemCheck(int index) {
                         IndexSelected = index;
                     }
                 });
                 recyclerView.setAdapter(mAdapter);
             }
    }

    public void setIsSearch(boolean isSearch){
        this.isSearch = isSearch;
    }

    @Override
    public void onClick(View v) {
        if(listener != null) {
            switch (v.getId()) {
                case R.id.btnAceptar:
                    listener.OnClickAceptar(this,mAlItemsDevice.get(IndexSelected));
                    break;
                case R.id.btnCancelar:
                    listener.OnClickCancelar(this);
                    break;
                case R.id.btnBuscar:
                    IndexSelected = -1;
                    listener.OnClickBuscar();
                    break;
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("listener",new ParceDialogSearch(listener));
        outState.putInt("IndexSelected",IndexSelected);
        outState.putBoolean("isSearch",isSearch);
    }


}
