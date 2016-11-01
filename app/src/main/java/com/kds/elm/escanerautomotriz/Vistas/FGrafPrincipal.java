package com.kds.elm.escanerautomotriz.Vistas;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kds.elm.escanerautomotriz.CustomViews.VelocimetroLinear;
import com.kds.elm.escanerautomotriz.CustomViews.ViewGasolina;
import com.kds.elm.escanerautomotriz.ActivityParent;
import com.kds.elm.escanerautomotriz.R;

/**
 * Created by isaac on 09/07/2016.
 * APP's
 */
public class FGrafPrincipal extends Fragment {


    TextView mTxtRpm;
    TextView mTxtTitleRpm;
    VelocimetroLinear vlRPM;
    ViewGasolina vgGasolina;
    ImageView  imgTemperatura;

    public static FGrafPrincipal newInstance(){
        FGrafPrincipal fragment=new FGrafPrincipal();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_grafica_principal,container,false);
             Init(view);
             setTypeface();
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void Init(View view){
        mTxtRpm=(TextView)view.findViewById(R.id.txtRPM);
        mTxtTitleRpm=(TextView)view.findViewById(R.id.txtTitleRpm);
        vlRPM=(VelocimetroLinear)view.findViewById(R.id.VelocimetroRPM);
        vgGasolina=(ViewGasolina)view.findViewById(R.id.cvGasolina);
        imgTemperatura=(ImageView)view.findViewById(R.id.imgTermometro);
    }

    private void setTypeface(){
        Typeface tf = Typeface.createFromAsset(getActivity().getBaseContext().getAssets(),"fonts/digital-7.ttf");
        mTxtRpm.setTypeface(tf);

    }


    public void updateTextView(final TextView view, final String txt) {
        new Handler().post(new Runnable() {
            public void run() {
                view.setText(txt);
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setVlRPM(String velocidad){
         mTxtRpm.setText(velocidad);
    }


}
