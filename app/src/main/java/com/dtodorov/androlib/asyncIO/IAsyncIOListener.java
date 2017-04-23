package com.dtodorov.androlib.asyncIO;

import java.io.IOException;

public interface IAsyncIOListener
{
    void onError(IOException e);
    void onReceived(byte[] buffer, int bytes);
    void onClosed();
}
