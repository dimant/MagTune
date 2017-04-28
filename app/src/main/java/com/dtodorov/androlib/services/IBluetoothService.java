package com.dtodorov.androlib.services;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.dtodorov.androlib.asyncIO.IAsyncIOListener;
import com.dtodorov.androlib.asyncIO.IAsyncIOListenerSlot;

import java.util.ArrayList;
import java.util.Set;

public interface IBluetoothService extends IBroadcastIntentReceiver, IAsyncIOListenerSlot
{
    void onConnect();

    boolean hasBluetooth();

    boolean isEnabled();

    void enableBluetooth(IBluetoothEnableListener listener);

    ArrayList<IBluetoothConnectableDevice> getBondedDevices();
}
