package com.dtodorov.androlib.asyncIO;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AsyncIOStream implements IAsyncIOStream
{
    private final int _bufferSize;
    private final InputStream _inputStream;
    private final OutputStream _outputStream;
    private final IAsyncIOListener _listener;

    private boolean _isClosed = false;

    public AsyncIOStream(InputStream inputStream, OutputStream outputStream, IAsyncIOListener listener, int bufferSize)
    {
        _inputStream = inputStream;
        _outputStream = outputStream;
        _listener = listener;
        _bufferSize = bufferSize;
    }

    @Override
    public void run()
    {
        byte[] buffer = new byte[_bufferSize];
        int bytesRead = -1;

        while(true)
        {
            try
            {
                bytesRead = _inputStream.read(buffer);
                if(bytesRead > 0)
                {
                    _listener.onReceived(buffer, bytesRead);
                }
            }
            catch(IOException exception)
            {
                _listener.onError(exception);
            }
        }
    }

    @Override
    public void write(byte[] buffer)
    {
        try
        {
            _outputStream.write(buffer);
        }
        catch(IOException exception)
        {
            _listener.onError(exception);
        }
    }

    @Override
    public boolean isClosed()
    {
        return _isClosed;
    }

    @Override
    public void close()
    {
        try
        {
            if(_isClosed == false)
            {
                _inputStream.close();
                _outputStream.close();
                _listener.onClosed();
            }
        }
        catch(IOException exception)
        {
            _listener.onError(exception);
        }
        finally
        {
            _isClosed = true;
        }
    }
}
