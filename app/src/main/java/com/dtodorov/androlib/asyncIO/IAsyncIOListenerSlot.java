package com.dtodorov.androlib.asyncIO;

/**
 * Created by ditodoro on 4/27/2017.
 */

public interface IAsyncIOListenerSlot {

    void registerIOListener(IAsyncIOListener ioListener);

    void clearIOListener();
}
