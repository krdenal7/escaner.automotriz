package com.kds.elm.escanerautomotriz;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.pires.obd.commands.ObdCommand;
import com.github.pires.obd.commands.engine.RPMCommand;
import com.github.pires.obd.commands.protocol.EchoOffCommand;
import com.github.pires.obd.commands.protocol.LineFeedOffCommand;
import com.github.pires.obd.commands.protocol.SelectProtocolCommand;
import com.github.pires.obd.commands.protocol.TimeoutCommand;
import com.github.pires.obd.enums.ObdProtocols;
import com.kds.elm.escanerautomotriz.BroadCast.ManagerBluetooth;
import com.kds.elm.escanerautomotriz.Dialogs.DialogAlert;
import com.kds.elm.escanerautomotriz.Interfaces.DialogAlertInterface;
import com.kds.elm.escanerautomotriz.Interfaces.ManagerBluetoothInterface;
import com.kds.elm.escanerautomotriz.Vistas.FGrafPrincipal;

public class ActivityParent extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        ManagerBluetoothInterface {

    private NavigationView navigationView;
    private Toolbar toolbar;
    private ProgressDialog progress;
    private AnimationDrawable animationBto;

    private ImageView imgSearchBluetooth;
    private TextView txtMensajeLoad;


    private static final int REQUEST_ENABLE_BT = 1;

    Context mContext;

    private AlertDialog mDialogListDevices;

    private boolean isLoadSearch = false;

    public ManagerBluetooth mBluetoothManager;

    /*SavedInstance*/
    private String isVisibleMostrarListaDispositivo = "isMostrarListaDispositivo";
    private String SaveListDevicesBluetooth = "SaveListDevicesBluetooth";
    private String isLoadSearchBluetooth = "isLoadSearchBluetooth";
    /*SavedInstance*/


    private BluetoothSocket mSocket;

    private String TAG = getClass().getSimpleName();

    FGrafPrincipal fGrafPrincipal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mContext = this;

        InitViews();
        InitInstances();
        ConfigDrawerLayout();

        if (savedInstanceState != null) {

        } else {
            if (!mBluetoothManager.isBluetoothActive())
                MensajeBluetoothDesactivado();
            ChangeStateIcon(mBluetoothManager.isBluetoothActive());
            ChangeIconBluetooth(mBluetoothManager.isBluetoothActive(), true);
        }

    }

    private void ConfigDrawerLayout() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (drawer != null)
            drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null)
            navigationView.setNavigationItemSelectedListener(this);
    }

    public void addFragment(Fragment fragment, int resource) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(resource, fragment, fragment.getClass().getSimpleName()).commit();
    }

    private void InitViews() {
        imgSearchBluetooth = (ImageView) findViewById(R.id.imgBluetoothSearch);
        imgSearchBluetooth.setBackgroundResource(R.drawable.animation_icon_bluetooth);
        animationBto = (AnimationDrawable) imgSearchBluetooth.getBackground();
        txtMensajeLoad = (TextView) findViewById(R.id.txtLblLoad);
    }

    private void InitInstances() {
        mBluetoothManager = ManagerBluetooth.newInstance(this);
        mBluetoothManager.setOnBluetoothListener(this);
        mBluetoothManager.RegisterReceiver();
    }

    public void AnimatedIconBluetooth(boolean start) {
        if (start && !animationBto.isRunning()) {
            imgSearchBluetooth.setBackgroundResource(R.drawable.animation_icon_bluetooth);
            imgSearchBluetooth.setImageResource(0);
            animationBto.start();
        } else if (animationBto.isRunning()) {
            animationBto.stop();
        }
    }

    public void ChangeIconBluetooth(boolean enabled, boolean animateImg) {
        if (enabled) {
            txtMensajeLoad.setText(getString(R.string.Conectando_));
            if (animateImg)
                AnimatedIconBluetooth(true);
        } else {
            txtMensajeLoad.setText(getString(R.string.Bluetooth_disabled));
            AnimatedIconBluetooth(false);
            imgSearchBluetooth.setImageResource(R.drawable.ic_bluetooth_disabled);
            imgSearchBluetooth.setBackgroundColor(getResources().getColor(R.color.transparente));
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null)
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                MensajeSalir();
            }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.accion_velocimetro) {

        } else if (id == R.id.accion_encender) {
            MensajeOnOffBluetooth();
        } else if (id == R.id.accion_buscar) {
            BuscarDispositivosEmparejados();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null)
            drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        boolean isVisibleListaDispositivos = false;
        if (mDialogListDevices != null)
            if (mDialogListDevices.isShowing())
                isVisibleListaDispositivos = true;

        outState.putBoolean(isVisibleMostrarListaDispositivo, isVisibleListaDispositivos);
        //outState.putParcelableArrayList(SaveListDevicesBluetooth, mItemsDevices);
        outState.putBoolean(isLoadSearchBluetooth, isLoadSearch);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBluetoothManager.UnRegisterReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT: {
                if (resultCode == RESULT_OK) {
                    Toast.makeText(mContext, "Bluetooth encendido", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "No se pudo encender el bluetooth", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            default:
                break;
        }
    }

    private boolean ConnectSocketBluetooth(int position) {
       /* BluetoothDevice device = mBluetoothApapter.getRemoteDevice(mItemsDevices.get(position).getMacDevice());
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        try {
            mSocket = device.createInsecureRfcommSocketToServiceRecord(uuid);
            mSocket.connect();
            return mSocket.isConnected();
        } catch (IOException e) {
            return false;
        }*/
        return true;
    }

    public class AsynTaskConnectSocket extends AsyncTask<Integer, Void, Boolean> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            LoadScreen();
        }

        @Override
        protected Boolean doInBackground(Integer... objects) {
            return ConnectSocketBluetooth(objects[0]);
        }

        @Override
        protected void onPostExecute(Boolean o) {
            super.onPostExecute(o);
            DimisLoadScreen();
            InitializeAdaptersBluetooth();
            if (o) {
                ShowToast("Conectado", Toast.LENGTH_LONG);
                InitializeAdaptersBluetooth();
            } else {
                ShowToast("No se pudo conectar", Toast.LENGTH_LONG);
            }

        }


    }

    private void InitializeAdaptersBluetooth() {

        try {
            new EchoOffCommand().run(mSocket.getInputStream(), mSocket.getOutputStream());
            new LineFeedOffCommand().run(mSocket.getInputStream(), mSocket.getOutputStream());
            new TimeoutCommand(10).run(mSocket.getInputStream(), mSocket.getOutputStream());
            new SelectProtocolCommand(ObdProtocols.AUTO).run(mSocket.getInputStream(), mSocket.getOutputStream());

            final RPMCommand[] rpmCommand = null;
            final ObdCommand[] obdCommand = {null};


            Thread th = new Thread(new Runnable() {
                public void run() {
                    while (!Thread.currentThread().isInterrupted()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (rpmCommand[0] != null)
                                    if (rpmCommand[0].getRPM() >= 0) {
                                        // listener.UpdateRPM(""+rpmCommand[0].getRPM());
                                    }

                            }
                        });
                        try {
                            rpmCommand[0] = new RPMCommand();
                            rpmCommand[0].run(mSocket.getInputStream(), mSocket.getOutputStream());
                            Log.e(TAG, "RPM:" + rpmCommand[0].getFormattedResult());
                        } catch (Exception e) {
                            ShowToast("Se perdio la comunicación con el dispositivo. Favor de verificar", Toast.LENGTH_LONG);
                        }

                    }
                }
            });
            th.start();

           /* new EchoOffCommand().run(mSocket.getInputStream(), mSocket.getOutputStream());
            new LineFeedOffCommand().run(mSocket.getInputStream(), mSocket.getOutputStream());
            new TimeoutCommand(125).run(mSocket.getInputStream(), mSocket.getOutputStream());
            new SelectProtocolCommand(ObdProtocols.AUTO).run(mSocket.getInputStream(), mSocket.getOutputStream());

            final RPMCommand rpmCommand=new RPMCommand();
            while (!Thread.currentThread().isInterrupted())
            {
                try{
                    rpmCommand.run(mSocket.getInputStream(),mSocket.getOutputStream());
                    Log.e(TAG,"RPM:"+rpmCommand.getFormattedResult());
                }catch (Exception e){
                    e.printStackTrace();
                }

            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ShowToast(final String mensaje, final int duradion) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mContext, mensaje, duradion).show();
            }
        });

    }

    private void DimissDialogListDevices() {
        if (mDialogListDevices != null) {
            if (mDialogListDevices.isShowing()) {
                mDialogListDevices.dismiss();
            }
        }
    }

    private void PairDevice(String mac) {
       /* try {
            BluetoothDevice device = mBluetoothApapter.getRemoteDevice(mac);
            Method method = device.getClass().getMethod("createBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
        } catch (Exception e) {
            DimisLoadScreen();
            Toast.makeText(mContext, "Ocurrio un error mientras se intentaba conectar al dispositivo", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }*/
    }

    private void BuscarNuevosDispositivos() {

       /* if (isEnabledBluetooth) {
            mBluetoothApapter.startDiscovery();
        } else {
            ShowBluetoothEnabled();
        }*/
    }

    public void BuscarDispositivosEmparejados() {
        /*if (isEnabledBluetooth) {
            mItemsDevices = new ArrayList<>();
            Set<BluetoothDevice> devices = mBluetoothApapter.getBondedDevices();
            for (BluetoothDevice device : devices) {
                mItemsDevices.add(new ListDevicesPair(device.getName(), device.getAddress()));
            }
            MostrarListaDispositivos();
        } else {
            ShowBluetoothEnabled();
        }*/
    }


    /**
     * Bloquea la pantalla mientras esta cargago algún evento
     */
    public void LoadScreen() {
        progress = ProgressDialog.show(mContext, null, null, true);
        progress.setContentView(R.layout.layout_progress_dialog);
        progress.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progress.show();
    }

    /**
     * Desbloquea la pantalla cuando termina el evento en curso
     */
    public void DimisLoadScreen() {
        if (progress != null) {
            if (progress.isShowing())
                progress.dismiss();
        }
    }

    /**
     * Cambia el color del icono dependiendo si el bluetooth esta prendido o apagado
     *
     * @param estado true (Encendido), false (Apagado)
     */
    private void ChangeStateIcon(boolean estado) {
        int icon;
        String titulo;
        if (estado) {
            icon = R.drawable.ic_accion_bluetooth_on;
            titulo = "Apagar";
        } else {
            icon = R.drawable.ic_accion_bluetooth_off;
            titulo = "Encender";
        }
        navigationView.getMenu()
                .findItem(R.id.accion_encender)
                .setIcon(icon)
                .setTitle(titulo);
    }

    /**
     * Muestra una lista con los dispositivos bluetooth encontrados
     */
    private void MostrarListaDispositivos() {

        /*if (isShowList) {
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.layout_list_devices, null, false);
            RecyclerView mRecycle = (RecyclerView) view.findViewById(R.id.rv);
            LinearLayoutManager llm = new LinearLayoutManager(inflater.getContext());
            mRecycle.setLayoutManager(llm);
            CustomAdapterListDevices adapter = new CustomAdapterListDevices(mItemsDevices, mContext, mBluetoothApapter);
            final int[] mPosition = {-1};
            final boolean[] isEmparejado = {false};

            adapter.setOnItemClickPairDecice(new CustomAdapterListDevices.CustomAdapterListDeviceListener() {
                @Override
                public void OnPairedDevice(int position, boolean isPair) {
                    mPosition[0] = position;
                    isEmparejado[0] = isPair;
                }
            });

            mRecycle.setAdapter(adapter);
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setView(mRecycle);
            builder.setCancelable(false);
            builder.setTitle(getString(R.string.title_dispositivos));
            builder.setPositiveButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.setNeutralButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    if (isEmparejado[0]) {
                        new AsynTaskConnectSocket().execute(mPosition[0]);
                    } else {
                        ShowPairedDevice(mPosition[0]);
                    }
                }
            });
            builder.setNegativeButton("Buscar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    isLoadSearch = true;
                    BuscarNuevosDispositivos();
                }
            });

            mDialogListDevices = builder.create();
            mDialogListDevices.show();
        }

*/
    }

    private void ShowPairedDevice(final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Aviso");
        builder.setMessage("¿Desea conectarse a este dispositivo?");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //isShowList = false;
                Toast.makeText(mContext, "Conectando...", Toast.LENGTH_LONG).show();
                dialog.dismiss();
                //PairDevice(mItemsDevices.get(position).getMacDevice());
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        DimissDialogListDevices();
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    /*BLUETOOTH OVERRIDE*/

    @Override
    public void OnStateBluetoothChange(boolean isActive) {
        ChangeStateIcon(isActive);
        ChangeIconBluetooth(isActive,true);
    }

    /*MENSAJES*/

    /**
     * Se muestra par confirmar si se desea cerrar la aplicación
     */
    public void MensajeSalir() {
        MostrarAlerta(getString(R.string.Aviso), getString(R.string.mensaje_salir), "Si", "No", null, true, new DialogAlertInterface() {
            @Override
            public void OnClickAgree(DialogFragment dialogFragment) {
                dialogFragment.dismiss();
                finish();
            }

            @Override
            public void OnClickDisagree(DialogFragment dialogFragment) {
                dialogFragment.dismiss();
            }

            @Override
            public void OnClickCancel(DialogFragment dialogFragment) {

            }
        });
    }

    /**
     * Muestra el mensaje deacuerdo al estatus del bluetooth
     * Si esta encendido muestra mensaje para apagarlo, si esta encendido manda la petición para encenderlo
     */
    public void MensajeOnOffBluetooth() {
        if (mBluetoothManager.isBluetoothActive()) {
            MostrarAlerta("Aviso", getString(R.string.mensaje_off_bluetooth), "Si", "No", null, true, new DialogAlertInterface() {
                @Override
                public void OnClickAgree(DialogFragment dialogFragment) {
                    dialogFragment.dismiss();
                    mBluetoothManager.OffDeviceBluetooth();
                }

                @Override
                public void OnClickDisagree(DialogFragment dialogFragment) {
                    dialogFragment.dismiss();
                }

                @Override
                public void OnClickCancel(DialogFragment dialogFragment) {

                }
            });
        } else {
            mBluetoothManager.OnDeviceBluetooth();
        }
    }

    /**
     * Este mensaje se muestra al iniciar la aplicación y validar que el dispositivo bluetooth este desactivado
     */
    public void MensajeBluetoothDesactivado() {
        MostrarAlerta("Aviso", getString(R.string.mensaje_activar_bluetooth), "Si", "No", null, true, new DialogAlertInterface() {
            @Override
            public void OnClickAgree(DialogFragment dialogFragment) {
                mBluetoothManager.OnDeviceBluetooth();
            }

            @Override
            public void OnClickDisagree(DialogFragment dialogFragment) {
                dialogFragment.dismiss();
            }

            @Override
            public void OnClickCancel(DialogFragment dialogFragment) {

            }
        });
    }

    /*ConfigDialogAlert*/

    public void MostrarAlerta(String titulo, String mensaje, boolean Cancelable) {
        MostrarAlerta(titulo, mensaje, "Aceptar", null, null, Cancelable, new DialogAlertInterface() {
            @Override
            public void OnClickAgree(DialogFragment dialogFragment) {
                dialogFragment.dismiss();
            }

            @Override
            public void OnClickDisagree(DialogFragment dialogFragment) {

            }

            @Override
            public void OnClickCancel(DialogFragment dialogFragment) {

            }
        });
    }

    public void MostrarAlerta(String mensaje, boolean Cancelable) {
        MostrarAlerta(null, mensaje, "Aceptar", null, null, Cancelable, new DialogAlertInterface() {
            @Override
            public void OnClickAgree(DialogFragment dialogFragment) {
                dialogFragment.dismiss();
            }

            @Override
            public void OnClickDisagree(DialogFragment dialogFragment) {

            }

            @Override
            public void OnClickCancel(DialogFragment dialogFragment) {

            }
        });
    }

    /**
     * Metodo general para mostrar la alertas
     *
     * @param titulo         titulo del alert si este parametro es null no se mostrara el título
     * @param mensaje        null or message
     * @param btn1           título del botón 1
     * @param btn2           título del botón 2
     * @param btn3           título del botón 3
     * @param Cancelable     true o false
     * @param alertInterface eventos de los botones.
     */
    public void MostrarAlerta(String titulo, String mensaje, String btn1, String btn2, String btn3, boolean Cancelable, DialogAlertInterface alertInterface) {
        DialogAlert alert = DialogAlert.newInstance(titulo, mensaje, btn1, btn2, btn3);
        alert.setCancelable(Cancelable);
        alert.setOnDialogAlertListener(alertInterface);
        alert.show(getFragmentManager(), "DialogAlert");
    }
}
