package com.dtodorov.androlib.services;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import com.dtodorov.androlib.asyncIO.AsyncIOStream;
import com.dtodorov.androlib.asyncIO.IAsyncIOListener;
import com.dtodorov.androlib.asyncIO.IAsyncIOStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothConnectableDevice implements IBluetoothConnectableDevice
{
    // common UUID for SPP, remains to be seen if it works
    static final UUID _sppUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private final IBluetoothService _bluetoothService;
    private final BluetoothDevice _device;
    private BluetoothSocket _socket;
    private Runnable _ioThread;

    public BluetoothConnectableDevice(IBluetoothService bluetoothService, BluetoothDevice device)
    {
        _device = device;
        _bluetoothService = bluetoothService;
    }

    @Override
    public boolean isConnected()
    {
        return _socket != null && _socket.isConnected();
    }

    @Override
    public String getName()
    {
        return _device.getName();
    }

    @Override
    public BluetoothSocket getSocket()
    {
        return _socket;
    }

    @Override
    public IAsyncIOStream connect(final IAsyncIOListener ioListener, int bufferSize)
    {
        AsyncIOStream ioStream = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try
        {
            _socket = _device.createRfcommSocketToServiceRecord(_sppUUID);
            _bluetoothService.onConnect();
            _socket.connect();
            inputStream = _socket.getInputStream();
            outputStream = _socket.getOutputStream();
        }
        catch(IOException exception)
        {
            ioListener.onError(exception);
        }

        if(inputStream != null && outputStream != null)
        {
            ioStream = new AsyncIOStream(inputStream, outputStream,
                    new IAsyncIOListener()
                    {
                        @Override
                        public void onError(IOException e)
                        {
                            ioListener.onError(e);
                        }

                        @Override
                        public void onReceived(byte[] buffer, int bytes)
                        {
                            ioListener.onReceived(buffer, bytes);
                        }

                        @Override
                        public void onClosed()
                        {
                            try
                            {
                                _socket.close();
                                ioListener.onClosed();
                            }
                            catch(IOException exception)
                            {
                                ioListener.onError(exception);
                            }
                        }
                    }, bufferSize);
        }

        return ioStream;
    }
}
