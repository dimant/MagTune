package com.dtodorov.magtune.controllers;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

import com.dtodorov.androlib.asyncIO.IAsyncIOListener;
import com.dtodorov.androlib.asyncIO.IAsyncIOStream;
import com.dtodorov.androlib.eventdispatcher.IEventDispatcher;
import com.dtodorov.androlib.eventdispatcher.IEventListener;
import com.dtodorov.androlib.eventdispatcher.IViewEventExtensions;
import com.dtodorov.androlib.services.IBluetoothConnectableDevice;
import com.dtodorov.androlib.services.IBluetoothEnableListener;
import com.dtodorov.androlib.services.IBluetoothService;
import com.dtodorov.androlib.services.IPermissionService;
import com.dtodorov.androlib.services.IStringResolver;
import com.dtodorov.androlib.services.IToaster;
import com.dtodorov.magtune.R;
import com.github.oxo42.stateless4j.StateMachine;
import com.github.oxo42.stateless4j.delegates.Action;

import java.io.IOException;
import java.util.ArrayList;

public class MainController {

    public static final String ShowBoundedDevices = "showBoundedBTDevices";
    public static final String ClickedDevice = "clickedDevice";

    private static int BluetoothBufferSize = 512; // bytes

    private enum State
    {
        HomeScreen,
        Connecting,
        Connected
    };

    public enum Trigger
    {
        Connect,
        ConnectFail,
        ConnectSuccess,
        Disconnect
    };

    private final IStringResolver _stringResolver;
    private final IPermissionService _permissionService;
    private StateMachine<State, Trigger> _stateMachine;
    private final IEventDispatcher _eventDispatcher;
    private final IToaster _toaster;
    private final IBluetoothService _bluetoothService;

    // state
    private Object _triggerParam;
    private ArrayList<IBluetoothConnectableDevice> _devices;
    private IAsyncIOStream _bluetoothStream;

    public MainController(
            IStringResolver stringResolver,
            IPermissionService permissionService,
            IEventDispatcher eventDispatcher,
            IToaster toaster,
            IBluetoothService bluetoothService)
    {
        _stringResolver = stringResolver;
        _permissionService = permissionService;
        _eventDispatcher = eventDispatcher;
        _toaster = toaster;
        _bluetoothService = bluetoothService;

        _permissionService.obtainPermissionIfNotGranted(
                Manifest.permission.BLUETOOTH,
                _stringResolver.getString(R.string.explanation_bluetooth));

        _permissionService.obtainPermissionIfNotGranted(
                Manifest.permission.BLUETOOTH_ADMIN,
                _stringResolver.getString(R.string.explanation_bluetooth_admin));

        _stateMachine = new StateMachine<State, Trigger>(State.HomeScreen);

        Action homeAction = new Action() {
            @Override
            public void doIt() {
                // enable list
                _eventDispatcher.emit(IViewEventExtensions.SHOW_VIEW, R.id.lvDevices);
                _eventDispatcher.emit(IViewEventExtensions.DISABLE_VIEW, R.id.lvDevices);
                _eventDispatcher.emit(IViewEventExtensions.HIDE_VIEW, R.id.llMotorControls);

                // enable BT
                if(_bluetoothService.isEnabled() == false)
                {
                    _bluetoothService.enableBluetooth(new IBluetoothEnableListener() {
                        @Override
                        public void onOk()
                        {
                            populateBTDeviceList();
                        }

                        @Override
                        public void onCancelled()
                        {
                            _toaster.toast(R.string.error_couldnt_enable_bt);
                            // add retry logic
                        }
                    });
                } else {
                    populateBTDeviceList();
                }
            }
        };

        _stateMachine.configure(State.HomeScreen)
                .onEntry(homeAction)
                .permit(Trigger.Connect, State.Connecting);

        _stateMachine.configure(State.Connecting)
                .onEntry(new Action() {
                    @Override
                    public void doIt() {
                        // TODO: add error handling
                        int position = (int) _triggerParam;

                        // start connection
                        // later on maybe make it its own thread so we can show a spinner
                        IBluetoothConnectableDevice device = _devices.get(position);
                        _bluetoothStream = device.connect(new IAsyncIOListener() {
                            @Override
                            public void onError(IOException e) {
                                _toaster.toast(e.getMessage());
                                fire(Trigger.ConnectFail);
                            }

                            @Override
                            public void onReceived(byte[] buffer, int bytes) {
                                // currently there is no communication from Arduino to Android
                                // if we ever add it, this will need to be refactored
                                // so that the IAsyncIOListener is in a separate file and knows how to parse
                                // the Arduino->Android protocol
                            }

                            @Override
                            public void onClosed() {
                                _toaster.toast(R.string.info_bluetooth_disconnected);
                                fire(Trigger.Disconnect);
                            }
                        }, MainController.BluetoothBufferSize);

                        if(_bluetoothStream != null)
                        {
                            _toaster.toast(R.string.info_bluetooth_connected);
                            fire(Trigger.ConnectSuccess);
                        }
                    }
                })
                .permit(Trigger.ConnectSuccess, State.Connected)
                .permit(Trigger.ConnectFail, State.HomeScreen)
                .permit(Trigger.Disconnect, State.HomeScreen);

        _stateMachine.configure(State.Connected)
                .onEntry(new Action() {
                    @Override
                    public void doIt() {
                        // enable motor controls
                        _eventDispatcher.emit(IViewEventExtensions.SHOW_VIEW, R.id.llMotorControls);
                        _eventDispatcher.emit(IViewEventExtensions.HIDE_VIEW, R.id.lvDevices);
                    }
                })
                .onExit(new Action() {
                    @Override
                    public void doIt() {
                        // clean up connection infra
                        cleanup();

                        // disable motor controls
                        _eventDispatcher.emit(IViewEventExtensions.HIDE_VIEW, R.id.llMotorControls);
                        _eventDispatcher.emit(IViewEventExtensions.SHOW_VIEW, R.id.lvDevices);
                    }
                })
                .permit(Trigger.Disconnect, State.HomeScreen);

        homeAction.doIt();
    }

    private void cleanup()
    {
        if(_devices != null)
        {
            _devices.clear();
        }

        if(_bluetoothStream != null && _bluetoothStream.isClosed() == false)
        {
            _bluetoothStream.close();
            _bluetoothStream = null;
        }

        _triggerParam = null;
    }

    public void fire(Trigger trigger, Object triggerParam) {
        _triggerParam = triggerParam;
        fire(trigger);
    }

    public void fire(Trigger trigger) {
        if(_stateMachine.canFire(trigger)) {
            _stateMachine.fire(trigger);
        }
    }

    private void populateBTDeviceList()
    {
        // populate BT list
        _devices = _bluetoothService.getBondedDevices();
        _eventDispatcher.emit(MainController.ShowBoundedDevices, _devices);
        _eventDispatcher.emit(IViewEventExtensions.ENABLE_VIEW, R.id.lvDevices);
    }
}
