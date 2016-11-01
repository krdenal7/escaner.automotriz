package com.kds.elm.escanerautomotriz.GraficaGeneral;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.kds.elm.escanerautomotriz.ActivityParent;
import com.kds.elm.escanerautomotriz.R;

/**
 * Created by isaac on 09/07/2016.
 * APP's
 */
public class ControladorGraficaGeneral extends ActivityParent {

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       ((ViewGroup)findViewById(R.id.frame_menu_content))
               .addView(LayoutInflater.from(this).inflate(R.layout.fragment_grafica_principal,null,false));
        mContext=this;
    }



}
