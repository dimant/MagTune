package com.dtodorov.androlib.services;

import android.bluetooth.BluetoothSocket;

import com.dtodorov.androlib.asyncIO.IAsyncIOListener;
import com.dtodorov.androlib.asyncIO.IAsyncIOStream;

/**
 * Created by diman on 4/23/2017.
 */

public interface IBluetoothConnectableDevice
{
    boolean isConnected();

    String getName();

    BluetoothSocket getSocket();

    IAsyncIOStream connect(IAsyncIOListener ioListener, int bufferSize);
}
