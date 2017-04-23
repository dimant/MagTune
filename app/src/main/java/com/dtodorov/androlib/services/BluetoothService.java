package com.dtodorov.androlib.services;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class BluetoothService extends BroadcastReceiver implements IBluetoothService
{

    // common UUID for SPP, remains to be seen if it works
    static final UUID _sppUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private IIntentService _intentService;
    private IBluetoothDisconnectListener _disconnectListener;

    private BluetoothAdapter _adapter;
    private BluetoothSocket _socket;
    private BluetoothErrors _lastError = BluetoothErrors.NONE;

    public BluetoothService(IIntentService intentService)
    {
        _intentService = intentService;
        _adapter = BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    public boolean hasBluetooth()
    {
        return _adapter != null;
    }

    @Override
    public boolean isEnabled()
    {
        return hasBluetooth() && _adapter.isEnabled();
    }

    @Override
    public BluetoothErrors getLastError()
    {
        return _lastError;
    }

    @Override
    public void registerDisconnectListener(IBluetoothDisconnectListener disconnectListener)
    {
        _disconnectListener = disconnectListener;
    }

    @Override
    public void enableBluetooth(final IBluetoothEnableListener listener)
    {
        if(hasBluetooth() && !isEnabled())
        {
            _intentService.enactIntent(BluetoothAdapter.ACTION_REQUEST_ENABLE, new IIntentListener()
            {
                @Override
                public void onResult(int resultCode, Intent data)
                {
                    if(resultCode == Activity.RESULT_OK)
                        listener.onOk();
                    else
                        listener.onCancelled();
                }
            });
        }
        else
        {
            listener.onCancelled();
        }
    }

    @Override
    public ArrayList<BluetoothDevice> getBondedDevices()
    {
        ArrayList<BluetoothDevice> list = new ArrayList<>();

        if(hasBluetooth())
        {
            list.addAll(0, _adapter.getBondedDevices());
        }

        return list;
    }

    @Override
    public BluetoothSocket connect(String address)
    {
        BluetoothDevice device = null;

        try
        {
            device = _adapter.getRemoteDevice(address);
        }
        catch(IllegalArgumentException e)
        {
            _lastError = BluetoothErrors.INVALID_DEVICE_ADDRESS;
        }

        if(device != null)
            return connect(device);
        else
            return null;
    }

    @Override
    public void disconnect()
    {
        if(_socket != null)
        {
            try
            {
                _socket.close();
            }
            catch (IOException e)
            {
                _lastError = BluetoothErrors.FAILED_TO_DISCONNECT;
            }

            _socket = null;

            if(_disconnectListener != null)
            {
                _disconnectListener.onDisconnected();
            }
        }
    }

    @Override
    public BluetoothSocket connect(BluetoothDevice device)
    {
        disconnect();

        try
        {
            _socket = device.createInsecureRfcommSocketToServiceRecord(_sppUUID);
            _adapter.cancelDiscovery();
            _socket.connect();
        }
        catch(IOException e)
        {
            _lastError = BluetoothErrors.FAILED_TO_CONNECT;
        }

        return _socket;

    }

    @Override
    public IntentFilter getFilter()
    {
        return new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
        int previousState = intent.getIntExtra(BluetoothAdapter.EXTRA_PREVIOUS_STATE, 0);
        if(
                previousState == BluetoothAdapter.STATE_ON &&
                        state != BluetoothAdapter.STATE_ON &&
                    _disconnectListener != null)
        {
            _disconnectListener.onDisconnected();
        }
    }
}
