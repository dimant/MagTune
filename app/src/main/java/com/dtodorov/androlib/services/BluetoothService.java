package com.dtodorov.androlib.services;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.dtodorov.androlib.asyncIO.IAsyncIOListener;
import com.dtodorov.androlib.asyncIO.IAsyncIOListenerSlot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class BluetoothService extends BroadcastReceiver implements IBluetoothService
{
    private IIntentService _intentService;
    private IAsyncIOListener _ioListener;

    private BluetoothAdapter _adapter;

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
    public void registerIOListener(IAsyncIOListener ioListener)
    {
        _ioListener = ioListener;
    }

    @Override
    public void clearIOListener()
    {
        _ioListener = null;
    }

    @Override
    public void enableBluetooth(final IBluetoothEnableListener listener)
    {
        if(hasBluetooth() && _adapter.isEnabled() == false)
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
    public ArrayList<IBluetoothConnectableDevice> getBondedDevices()
    {
        ArrayList<IBluetoothConnectableDevice> list = new ArrayList<>();

        if(hasBluetooth())
        {
            for(BluetoothDevice device : _adapter.getBondedDevices())
            {
                list.add(new BluetoothConnectableDevice(this, device));
            }
        }

        return list;
    }

    @Override
    public void onConnect()
    {
        _adapter.cancelDiscovery();
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
                previousState == BluetoothAdapter.STATE_CONNECTED &&
                        state != BluetoothAdapter.STATE_CONNECTED &&
                    _ioListener != null)
        {
            _ioListener.onClosed();
        }
    }
}
