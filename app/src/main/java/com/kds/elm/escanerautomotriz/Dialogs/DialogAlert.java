package com.kds.elm.escanerautomotriz.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.kds.elm.escanerautomotriz.Interfaces.DialogAlertInterface;
import com.kds.elm.escanerautomotriz.R;

/**
 * Created by Isaac Martinez on 29/10/2016.
 * escaner.automotriz
 */

public class DialogAlert extends DialogFragment implements View.OnClickListener {

    private TextView txtTitulo;
    private TextView txtMensaje;
    private AppCompatButton txtBtnCancelar;
    private AppCompatButton txtBtnNo;
    private AppCompatButton txtBtnSi;

    private String titulo, mensaje, btn1, btn2, btn3;

    private DialogAlertInterface listener;

    public static DialogAlert newInstance(String titulo, String mensaje, String btn1, String btn2, String btn3) {
        Bundle bundle = new Bundle();
        bundle.putString("titulo", titulo);
        bundle.putString("mensaje", mensaje);
        bundle.putString("btn1", btn1);
        bundle.putString("btn2", btn2);
        bundle.putString("btn3", btn3);
        DialogAlert alert = new DialogAlert();
        alert.setArguments(bundle);
        return alert;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            this.titulo = bundle.getString("titulo");
            this.mensaje = bundle.getString("mensaje");
            this.btn1 = bundle.getString("btn1");
            this.btn2 = bundle.getString("btn2");
            this.btn3 = bundle.getString("btn3");
        }
        if(savedInstanceState != null){
            DialogParcer parcer = savedInstanceState.getParcelable("listener");
            if(parcer != null)
            listener = parcer.getDialogAlertInterface();
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_dialog_alert, null, false);
        InitViews(view);
        ConfigViews();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);

        return builder.create();
    }

    private void InitViews(View view) {
        txtTitulo = (TextView) view.findViewById(R.id.txtTitulo);
        txtMensaje = (TextView) view.findViewById(R.id.txtMensaje);
        txtBtnCancelar = (AppCompatButton) view.findViewById(R.id.btnCancelar);
        txtBtnNo = (AppCompatButton) view.findViewById(R.id.btnNo);
        txtBtnSi = (AppCompatButton) view.findViewById(R.id.btnSi);
    }

    private void ConfigViews() {
        //Configura el titulo
        if (titulo != null) {
            txtTitulo.setText(titulo);
        }
        if (mensaje != null) {
            txtMensaje.setText(mensaje);
        }
        if (btn1 != null) {
            txtBtnSi.setText(btn1);
            txtBtnSi.setOnClickListener(this);
        }
        if (btn2 != null) {
            txtBtnNo.setText(btn2);
            txtBtnNo.setOnClickListener(this);
        }
        if (btn3 != null) {
            txtBtnCancelar.setText(btn3);
            txtBtnCancelar.setOnClickListener(this);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("listener",new DialogParcer(listener));
    }

    @Override
    public void onClick(View v) {
        if(listener != null)
        switch (v.getId()) {
            case R.id.btnCancelar:
                    listener.OnClickCancel(DialogAlert.this);
                break;
            case R.id.btnNo:
                    listener.OnClickDisagree(DialogAlert.this);
                break;
            case R.id.btnSi:
                    listener.OnClickAgree(DialogAlert.this);
                break;
        }
    }

    public void setOnDialogAlertListener(DialogAlertInterface listener) {
        this.listener = listener;
    }
}
