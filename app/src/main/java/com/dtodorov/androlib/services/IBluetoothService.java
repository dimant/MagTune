package com.dtodorov.androlib.services;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by diman on 4/23/2017.
 */
public interface IBluetoothService
{
    boolean hasBluetooth();

    boolean isEnabled();

    BluetoothErrors getLastError();

    void registerDisconnectListener(IBluetoothDisconnectListener disconnectListener);

    void enableBluetooth(IBluetoothEnableListener listener);

    ArrayList<BluetoothDevice> getBondedDevices();

    BluetoothSocket connect(String address);

    void disconnect();

    BluetoothSocket connect(BluetoothDevice device);

    IntentFilter getFilter();

    void onReceive(Context context, Intent intent);

    public enum BluetoothErrors
    {
        NONE,
        INVALID_DEVICE_ADDRESS,
        FAILED_TO_CONNECT,
        FAILED_TO_DISCONNECT
    }
}
