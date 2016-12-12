package com.kds.elm.escanerautomotriz;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kds.elm.escanerautomotriz.Dialogs.DialogAlert;
import com.kds.elm.escanerautomotriz.Dialogs.DialogListPairDevice;
import com.kds.elm.escanerautomotriz.Dialogs.DialogSearchDevice;
import com.kds.elm.escanerautomotriz.Enums.EStatusBto;
import com.kds.elm.escanerautomotriz.Enums.ETipoFragment;
import com.kds.elm.escanerautomotriz.Interfaces.IDialogAlert;
import com.kds.elm.escanerautomotriz.Interfaces.IDialogAlertGeneric;
import com.kds.elm.escanerautomotriz.Interfaces.IDialogSearch;
import com.kds.elm.escanerautomotriz.Interfaces.IManagerBluetooth;
import com.kds.elm.escanerautomotriz.Managers.ManagerBluetooth;
import com.kds.elm.escanerautomotriz.Util.Utils;
import com.kds.elm.escanerautomotriz.model.DeviceBluetooth;
import com.kds.elm.escanerautomotriz.view.FGrafPrincipal;

import java.util.ArrayList;

public class ActivityParent extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        IManagerBluetooth {

    private String TAG = "TAG - " + ActivityParent.class.getSimpleName();

    private NavigationView navigationView;
    private Toolbar toolbar;
    private ProgressDialog progress;
    private AnimationDrawable animationBto;

    private ImageView imgSearchBluetooth;
    private TextView txtMensajeLoad;
    private ProgressDialog mProgressDialod;

    private DialogSearchDevice dialogSearchDevice;
    private static ArrayList<DeviceBluetooth> mAlItemsDevices;

    Context mContext;

    public ManagerBluetooth mBluetoothManager;

    private ETipoFragment mTipoFragment = ETipoFragment.FGENERAL;
    private EStatusBto mEstatusBluetooth = EStatusBto.STATUS_RUN_CONNECT;

    private boolean isShowMessageListPair = false;
    private boolean isShowProgressDialog = false;
    private boolean isShowLoadScreen = false;
    private String sMensajeProgress = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mContext = this;

        mBluetoothManager = ManagerBluetooth.newInstance(this);
        mBluetoothManager.setOnBluetoothListener(this);

        InitViews();
        ConfigDrawerLayout();

        if (savedInstanceState != null) {
            mTipoFragment = (ETipoFragment) savedInstanceState.getSerializable("mTipoFragment");
            isShowMessageListPair = savedInstanceState.getBoolean("isShowMessageListPair");
            isShowProgressDialog = savedInstanceState.getBoolean("isShowProgressDialog");
            isShowLoadScreen = savedInstanceState.getBoolean("isShowLoadScreen");
            sMensajeProgress = savedInstanceState.getString("sMensajeProgress");
            mEstatusBluetooth = (EStatusBto) savedInstanceState.getSerializable("mEstatusBluetooth");
            if (isShowLoadScreen) {
                LoadScreen();
            }
            if (savedInstanceState.containsKey("mAlItemsDevices"))
                mAlItemsDevices = savedInstanceState.getParcelableArrayList("mAlItemsDevices");
            if (isShowProgressDialog) {
                MostrarProgressDialog(sMensajeProgress);
            }
            if (getFragmentManager().findFragmentByTag("DialogSearch") instanceof DialogSearchDevice) {
                dialogSearchDevice = (DialogSearchDevice) getFragmentManager().findFragmentByTag("DialogSearch");
            }
            if (ManagerBluetooth.getmSocket() == null || !ManagerBluetooth.getmSocket().isConnected()) {
                AnimateIconConnected();
            }
        } else {
            if (!mBluetoothManager.isBluetoothActive())
                MensajeBluetoothDesactivado();
            navigationView.setCheckedItem(R.id.accion_info_gral);
        }

        ChangeStateIcon(mBluetoothManager.isBluetoothActive());
        CambiarModulo();
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

    /**
     * Carga en la ventana principal el fragment deacuerdo al modúlo elegido
     */
    private void CambiarModulo() {
        Fragment fragment;
        switch (mTipoFragment) {
            case FGENERAL:
                fragment = getFragmentManager().findFragmentByTag(FGrafPrincipal.class.getSimpleName());
                if (!(fragment instanceof FGrafPrincipal)) {
                    addFragment(FGrafPrincipal.newInstance());
                }
                break;
        }
    }

    /**
     * Remplaza el fragment principal
     *
     * @param fragment newInstance
     */
    public void addFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_menu_content,
                fragment, fragment.getClass().getSimpleName()).commit();
    }

    /**
     * Inicializa los views
     */
    private void InitViews() {
        imgSearchBluetooth = (ImageView) findViewById(R.id.imgBluetoothSearch);
        txtMensajeLoad = (TextView) findViewById(R.id.txtLblLoad);
    }

    /**
     * Anima el icono de la conexión del dispositivo
     */
    public void AnimateIconConnected() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!getIdDeviceBluetooth().trim().isEmpty()) {
                    if (imgSearchBluetooth != null) {
                        String msj = getString(R.string.Conectando_con) + " " + getNameDevice();
                        txtMensajeLoad.setText(msj);
                        imgSearchBluetooth.setImageResource(0);
                        imgSearchBluetooth.setBackgroundResource(R.drawable.animation_icon_bluetooth);
                        if (animationBto != null && animationBto.isRunning()) {
                            animationBto.stop();
                        }
                        animationBto = (AnimationDrawable) imgSearchBluetooth.getBackground();
                        animationBto.start();
                    }
                } else {
                    imgSearchBluetooth.setImageResource(R.drawable.ic_bluetooth_black);
                    txtMensajeLoad.setText(getString(R.string.Nosehaseleccionadodispositivo));
                }
            }
        });

    }

    /**
     * cambia el icono del bluetooth dependiendo del estado
     *
     * @param enabled true, false
     */
    public void StatusIconBluetooth(boolean enabled) {
        if (enabled) {
            if (mBluetoothManager.getListPairDevice().size() <= 0) {
                SaveDeviceBluetooth("", "");
                txtMensajeLoad.setText(getString(R.string.Nohaydispositivosvinculados));
                imgSearchBluetooth.setImageResource(R.drawable.ic_bluetooth_black);
                imgSearchBluetooth.setBackgroundColor(getResources().getColor(R.color.transparente));
            } else {
                animationBto = null;
                AnimateIconConnected();
            }
        } else {
            txtMensajeLoad.setText(getString(R.string.Bluetooth_disabled));
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
            if (mBluetoothManager.isBluetoothActive()) {
                MensajeListaPairDevice();
            } else {
                isShowMessageListPair = true;
                MensajeBluetoothDesactivado();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null)
            drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isShowMessageListPair", isShowMessageListPair);
        outState.putBoolean("isShowProgressDialog", isShowProgressDialog);
        outState.putSerializable("mTipoFragment", mTipoFragment);
        outState.putBoolean("isShowLoadScreen", isShowLoadScreen);
        outState.putString("sMensajeProgress", sMensajeProgress);
        outState.putSerializable("mEstatusBluetooth", mEstatusBluetooth);
        if (mAlItemsDevices != null)
            outState.putParcelableArrayList("mAlItemsDevices", new ArrayList<Parcelable>(mAlItemsDevices));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBluetoothManager.RegisterReceiver();
        ValidaConnection();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBluetoothManager.UnRegisterReceiver();
    }

    private void ValidaConnection() {
        if (mBluetoothManager.isBluetoothActive()) {
            if (((ManagerBluetooth.getmSocket() == null
                    || !ManagerBluetooth.getmSocket().isConnected()) && !ManagerBluetooth.isExecute())
                    && !getIdDeviceBluetooth().trim().isEmpty()) {
                AnimateIconConnected();
                DeviceBluetooth deviceBluetooth = new DeviceBluetooth();
                deviceBluetooth.setClassDevice("");
                deviceBluetooth.setNombreDevice(getNameDevice());
                deviceBluetooth.setMacDevice(getIdDeviceBluetooth());
                mBluetoothManager.ConnectSocketBluetooth(deviceBluetooth);
            }
        } else {
            StatusIconBluetooth(false);
        }
    }

    private void InitializeAdaptersBluetooth() {

     /*   try {
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

           *//* new EchoOffCommand().run(mSocket.getInputStream(), mSocket.getOutputStream());
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

            }*//*

        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    /**
     * Bloquea la pantalla mientras esta cargago algún evento
     */
    public void LoadScreen() {
        DimisLoadScreen();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                isShowLoadScreen = true;
                progress = ProgressDialog.show(mContext, null, null, true);
                progress.setContentView(R.layout.layout_progress_dialog);
                if (progress.getWindow() != null)
                    progress.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                progress.show();
            }
        });

    }

    /**
     * Desbloquea la pantalla cuando termina el evento en curso
     */
    public void DimisLoadScreen() {
        isShowLoadScreen = false;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progress != null) {
                    if (progress.isShowing())
                        progress.dismiss();
                }
            }
        });

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
     * Guarda la información del dispositivo bluetooth seleccionado
     *
     * @param name nombre del dispositivo
     * @param mac  dirección mac
     */
    public void SaveDeviceBluetooth(String name, String mac) {
        SharedPreferences preferences = getSharedPreferences("DeviceBluetooth", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Nombre", name);
        editor.putString("Mac", mac);
        editor.commit();
    }

    /**
     * Obtiene la dirección mac del dispositivo que se selecciono como escaner
     *
     * @return direccion mac, empty si aun no se elige el dispositivo
     */
    public String getIdDeviceBluetooth() {
        SharedPreferences preference = getSharedPreferences("DeviceBluetooth", MODE_PRIVATE);
        return preference.getString("Mac", "");
    }

    /**
     * Obtiene el nombre del dispositivo que se selecciono como preseterminado
     *
     * @return nombre del dispositivo
     */
    public String getNameDevice() {
        SharedPreferences preference = getSharedPreferences("DeviceBluetooth", MODE_PRIVATE);
        return preference.getString("Nombre", "");
    }

    /*BLUETOOTH OVERRIDE*/

    /**
     * Este evento se llama cuando el dispositivo detecta un cambio en el estado del dispositivo bluetooth
     *
     * @param isActive true (Encendido) false (Apagado)
     */
    @Override
    public void OnStateBluetoothChange(boolean isActive) {
        ChangeStateIcon(isActive);
        if (isShowProgressDialog && isActive) {
            isShowProgressDialog = false;
            ValidaConnection();
            DismissProgressDialog();
        } else if (isShowMessageListPair && isActive) {
            isShowMessageListPair = false;
            MensajeListaPairDevice();
        } else if (!isActive) {
            StatusIconBluetooth(false);
        } else if (isActive) {
            ValidaConnection();
        }
    }

    @Override
    public void OnStartDiscovery() {
        mAlItemsDevices = new ArrayList<>();
        dialogSearchDevice.setIsSearch(true);
        dialogSearchDevice.OnEnabledButtonAceptar(false);
        dialogSearchDevice.OnChangeStartedDiscovery(true);
        dialogSearchDevice.ChangeStyleTextView(true);
    }

    @Override
    public void OnStopDiscovery() {
        dialogSearchDevice.setIsSearch(false);
        dialogSearchDevice.OnChangeStartedDiscovery(false);
        dialogSearchDevice.ChangeStyleTextView(false);
    }

    @Override
    public void OnDeviceFound(DeviceBluetooth device) {
        dialogSearchDevice.OnEnabledButtonAceptar(true);
        if (mAlItemsDevices != null) {
            if (Utils.ValidaItemDevice(mAlItemsDevices, device)) {
                mAlItemsDevices.add(device);
                dialogSearchDevice.OnDataChange();
            }
        }
    }

    @Override
    public void OnBonding(BluetoothDevice device) {
        String mensaje = "Vinculando a " + device.getName();
        LoadScreen();
        MostrarToast(mensaje);
    }

    @Override
    public void OnBondend(BluetoothDevice device) {
        Log.e(TAG, "OnBondend" + device.getName());
        DimisLoadScreen();
        String mensaje = device.getName() + " Vinculado";
        MostrarToast(mensaje);
        mBluetoothManager.ConnectSocketBluetooth(new
                DeviceBluetooth(device.getName(), device.getAddress(), "", true));
    }

    @Override
    public void OnBondend_none(BluetoothDevice device) {
        Log.e(TAG, "OnBondend_none" + device.getName());
        DimisLoadScreen();
    }


    /**
     * Se ejecuta cuando falla la conexion del socket
     *
     * @param isConnected     true false
     * @param deviceBluetooth dispositivo seleccionado
     */
    @Override
    public void OnConnectedDevice(final Boolean isConnected, final DeviceBluetooth deviceBluetooth) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DismissProgressDialog();
                if (mBluetoothManager.isBluetoothActive()) {
                    if (isConnected) {
                        if (animationBto != null && animationBto.isRunning()) {
                            animationBto.stop();
                            imgSearchBluetooth.setImageDrawable(null);
                            imgSearchBluetooth.setBackgroundDrawable(null);
                            imgSearchBluetooth.clearAnimation();
                        }
                        imgSearchBluetooth.setImageResource(0);
                        imgSearchBluetooth.setImageResource(R.drawable.ic_action_bluetooth_connected);
                        String mensaje = deviceBluetooth.getNombreDevice() + " conectado";
                        txtMensajeLoad.setText(mensaje);
                    } else {
                        if (deviceBluetooth.getMacDevice().equalsIgnoreCase(getIdDeviceBluetooth())) {
                            mBluetoothManager.ConnectSocketBluetooth(deviceBluetooth);
                        }
                    }
                }
            }
        });
    }

    /**
     * Se ejecuta en el broadcast receiver
     *
     * @param deviceBluetooth dispositivo seleccionado
     */
    @Override
    public void OnDisconnectedDevice(final DeviceBluetooth deviceBluetooth) {
        if (getIdDeviceBluetooth().equalsIgnoreCase(deviceBluetooth.getMacDevice())) {
            if (ManagerBluetooth.isConnectedDevice()) {
                AnimateIconConnected();
                mBluetoothManager.ConnectSocketBluetooth(deviceBluetooth);
                ManagerBluetooth.setIsConnectedDevice(false);
            }
        }

    }

    /*MENSAJES*/

    /**
     * Se muestra par confirmar si se desea cerrar la aplicación
     */
    public void MensajeSalir() {
        MostrarAlerta(getString(R.string.Aviso), getString(R.string.mensaje_salir), "Si", "No", null, true, new IDialogAlert() {
            @Override
            public void OnClickAgree(DialogFragment dialogFragment) {
                dialogFragment.dismiss();
                mBluetoothManager.UnRegisterReceiver();
                mBluetoothManager.CloseConnection();
                System.exit(0);
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
            MostrarAlerta("Aviso", getString(R.string.mensaje_off_bluetooth), "Si", "No", null, true, new IDialogAlert() {
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
            MostrarAlerta("Aviso", getString(R.string.EncenderBluetooth), "Si", "No", null, true, new IDialogAlert() {
                @Override
                public void OnClickAgree(DialogFragment dialogFragment) {
                    dialogFragment.dismiss();
                    mBluetoothManager.OnDeviceBluetooth();
                    MostrarProgressDialog(getString(R.string.Activandobluetooth_));
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
    }

    /**
     * Este mensaje se muestra al iniciar la aplicación y validar que el dispositivo bluetooth este desactivado
     */
    public void MensajeBluetoothDesactivado() {
        MostrarAlerta("Aviso", getString(R.string.mensaje_activar_bluetooth), "Si", "No", null, true, new IDialogAlert() {
            @Override
            public void OnClickAgree(DialogFragment dialogFragment) {
                dialogFragment.dismiss();
                mBluetoothManager.OnDeviceBluetooth();
                MostrarProgressDialog(getString(R.string.Activandobluetooth_));
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
     * Muestra alert con los dispositivos que ya se encuentran vinculados al teléfono
     */
    public void MensajeListaPairDevice() {
        ArrayList<DeviceBluetooth> list = mBluetoothManager.getListPairDevice();
        if (list.size() > 0) {
            DialogListPairDevice dialog = new DialogListPairDevice(list);
            dialog.setOnDialogListener(new IDialogAlertGeneric() {
                @Override
                public void OnClickAgree(DialogFragment dialogFragment, Object object) {
                    dialogFragment.dismiss();
                    MostrarProgressDialog(getString(R.string.Conectando___));
                    AnimateIconConnected();
                    if (object instanceof DeviceBluetooth) {
                        mBluetoothManager.ConnectSocketBluetooth((DeviceBluetooth) object);
                    }
                }

                @Override
                public void OnClickDisagree(DialogFragment dialogFragment, Object object) {
                    dialogFragment.dismiss();
                    MostrarMensajeSearchDevice();
                }

                @Override
                public void OnClickCancel(DialogFragment dialogFragment, Object object) {
                    dialogFragment.dismiss();
                }
            });
            dialog.show(getFragmentManager(), "DialogPair");
        } else {
            MostrarMensajeNotDevice();
        }
    }

    /**
     * Muestra progress durante el tiempo que tarda en encenderse el bluetooth
     */
    private void MostrarProgressDialog(String mensaje) {
        isShowProgressDialog = true;
        sMensajeProgress = mensaje;
        mProgressDialod = ProgressDialog.show(this, "Aviso", mensaje, true, false);
    }

    private void DismissProgressDialog() {
        isShowProgressDialog = false;
        if (mProgressDialod != null && mProgressDialod.isShowing()) {
            mProgressDialod.dismiss();
        }
    }

    /**
     * Muestra el mensaje para realizar una busqueda de los dispositivos cercanos
     */
    private void MostrarMensajeSearchDevice() {
        dialogSearchDevice = new DialogSearchDevice();
        dialogSearchDevice.setCancelable(false);
        dialogSearchDevice.setOnDialogSearchListener(new IDialogSearch() {

            @Override
            public void OnClickAceptar(DialogFragment dialogFragment, DeviceBluetooth device) {
                dialogFragment.dismiss();
                SaveDeviceBluetooth(device.getNombreDevice(), device.getMacDevice());
                mBluetoothManager.PairingDevice(device.getMacDevice());
            }

            @Override
            public void OnClickBuscar() {
                mAlItemsDevices = new ArrayList<>();
                dialogSearchDevice.OnDataChange();
                mBluetoothManager.StartDiscovery();
            }

            @Override
            public void OnClickCancelar(DialogFragment dialogFragment) {
                dialogFragment.dismiss();
                mAlItemsDevices = null;
                mBluetoothManager.StopDiscovery();
            }

            @Override
            public void OnFinishCreated() {
                mBluetoothManager.StartDiscovery();
            }

            @Override
            public ArrayList<DeviceBluetooth> OnDataChangeDevice() {
                return mAlItemsDevices;
            }
        });
        dialogSearchDevice.show(getFragmentManager(), "DialogSearch");
    }

    /**
     * Muestra notificación informandole al usuario que no existe ningún dispositivo vinculado
     */
    private void MostrarMensajeNotDevice() {
        MostrarAlerta("Aviso", getString(R.string.NohayDispositivosEmparejados), "Si", "No", null, true, new IDialogAlert() {
            @Override
            public void OnClickAgree(DialogFragment dialogFragment) {
                dialogFragment.dismiss();
                MostrarMensajeSearchDevice();
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

    private void MostrarToast(final String mensaje) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ActivityParent.this, mensaje, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*ConfigDialogAlert*/

    public void MostrarAlerta(String titulo, String mensaje, boolean Cancelable) {
        MostrarAlerta(titulo, mensaje, "Aceptar", null, null, Cancelable, new IDialogAlert() {
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
        MostrarAlerta(null, mensaje, "Aceptar", null, null, Cancelable, new IDialogAlert() {
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
    public void MostrarAlerta(String titulo, String mensaje, String btn1, String btn2, String btn3, boolean Cancelable, IDialogAlert alertInterface) {
        DialogAlert alert = DialogAlert.newInstance(titulo, mensaje, btn1, btn2, btn3);
        alert.setCancelable(Cancelable);
        alert.setOnDialogAlertListener(alertInterface);
        alert.show(getFragmentManager(), "DialogAlert");
    }
}
