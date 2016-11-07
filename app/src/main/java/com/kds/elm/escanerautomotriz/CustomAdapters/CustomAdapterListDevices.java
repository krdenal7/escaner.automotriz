package com.kds.elm.escanerautomotriz.CustomAdapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.kds.elm.escanerautomotriz.Modelo.ListDevicesPair;
import com.kds.elm.escanerautomotriz.R;

import java.util.ArrayList;

/**
 * Created by isaac on 09/07/2016.
 * APP's
 */
public class CustomAdapterListDevices extends RecyclerView.Adapter<CustomAdapterListDevices.CustomHolder> {

    ArrayList<ListDevicesPair> mItemsDevices;
    private RadioButton mSelectedRB;
    private int mSelectedPosition = -1;

    private OnCustomAdapterListDevListener listener;
    private View[] itemView;


    public CustomAdapterListDevices(ArrayList<ListDevicesPair> mItemsDevices) {
        this.mItemsDevices=mItemsDevices;
        itemView = new View[mItemsDevices.size()];
    }

    @Override
    public CustomHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_layout_devices,parent,false);
        return new CustomHolder(view);
    }

    @Override
    public void onBindViewHolder(final CustomHolder holder, final int position) {
        final ListDevicesPair devices=mItemsDevices.get(position);
        holder.txtNombre.setText(devices.getNombreDevice()==null?"":devices.getNombreDevice());
        holder.txtMacAdress.setText(devices.getMacDevice()==null?"":devices.getMacDevice());

        holder.rbCheckdevice.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(position != mSelectedPosition && mSelectedRB != null){
                    mSelectedRB.setChecked(false);
                }
                 int cont = 0;
                 for (View item:itemView){
                     if(cont != position){
                         ((RadioButton)item).setChecked(false);
                     }
                     cont++;
                 }
                mSelectedPosition = position;
                mSelectedRB = (RadioButton)view;
                if(listener != null){
                    listener.OnItemCheck(position);
                }
            }

        });

        if(mSelectedPosition != position){
            holder.rbCheckdevice.setChecked(false);
        }else{
            holder.rbCheckdevice.setChecked(true);
            if(mSelectedRB != null && holder.rbCheckdevice != mSelectedRB){
                mSelectedRB = holder.rbCheckdevice;
            }
        }

        itemView[position] = holder.rbCheckdevice;
    }

    @Override
    public int getItemCount() {
        return mItemsDevices.size();
    }

    public static class CustomHolder extends RecyclerView.ViewHolder{

        TextView txtNombre;
        TextView txtMacAdress;
        RadioButton rbCheckdevice;
        public CustomHolder(View itemView) {
            super(itemView);
            txtNombre=(TextView)itemView.findViewById(R.id.txtDeviceNombre);
            txtMacAdress=(TextView)itemView.findViewById(R.id.txtDeviceMac);
            rbCheckdevice=(RadioButton)itemView.findViewById(R.id.rbCheckDevice);
        }
    }

    public void setmSelectedPosition(int index){
        this.mSelectedPosition = index;
    }

    public void setOnCustomApapterDevListener(OnCustomAdapterListDevListener listener){
        this.listener = listener;
    }

    public interface OnCustomAdapterListDevListener{

        void OnItemCheck(int index);

    }


}
