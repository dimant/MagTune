package com.dtodorov.androlib.asyncIO;

/**
 * Created by diman on 4/23/2017.
 */

public interface IAsyncIOStream extends Runnable
{
    void write(byte[] buffer);

    boolean isClosed();

    void close();
}
