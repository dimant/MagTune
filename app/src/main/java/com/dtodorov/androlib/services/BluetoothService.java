package com.dtodorov.androlib.services;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class BluetoothService extends BroadcastReceiver {
    private Activity _activity;

    public BluetoothService(Activity activity)
    {
        _activity = activity;
    }

    public boolean hasBluetooth()
    {
        return BluetoothAdapter.getDefaultAdapter() != null;
    }

    public IntentFilter getFilter()
    {
        return new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
        int previousState = intent.getIntExtra(BluetoothAdapter.EXTRA_PREVIOUS_STATE, 0);
    }
}
