package com.kds.elm.escanerautomotriz.CustomAdapters;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.kds.elm.escanerautomotriz.Modelo.ListDevicesPair;
import com.kds.elm.escanerautomotriz.R;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by isaac on 09/07/2016.
 * APP's
 */
public class CustomAdapterListDevices extends RecyclerView.Adapter<CustomAdapterListDevices.CustomHolder> {

    ArrayList<ListDevicesPair> mItemsDevices;
    Context mContext;
    CustomAdapterListDeviceListener listener;
    BluetoothAdapter mBluetoothManager;
    private RadioButton mSelectedRB;
    private int mSelectedPosition = -1;


    public CustomAdapterListDevices(ArrayList<ListDevicesPair> mItemsDevices, Context context, BluetoothAdapter mBluetoothManager) {
        this.mItemsDevices=mItemsDevices;
        this.mContext=context;
        this.mBluetoothManager=mBluetoothManager;

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
                mSelectedPosition = position;
                mSelectedRB = (RadioButton)view;
                listener.OnPairedDevice(position,CheckPairedDevice(holder.txtMacAdress.getText().toString()));
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

    }

    private boolean CheckPairedDevice(String mac){
        boolean resp=false;
        Set<BluetoothDevice> devices=mBluetoothManager.getBondedDevices();
        for (BluetoothDevice device:devices){
            if(device.getAddress().equals(mac)){
                resp=true;
            }
        }
        return resp;
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

    public void setOnItemClickPairDecice(CustomAdapterListDeviceListener listener){
        this.listener=listener;
    }

    public interface CustomAdapterListDeviceListener{
        void OnPairedDevice(int position,boolean isEmparejado);
    }

}
