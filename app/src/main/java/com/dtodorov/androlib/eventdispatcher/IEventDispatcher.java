package com.dtodorov.androlib.eventdispatcher;

/**
 * Created by diman on 3/27/2016.
 */
public interface IEventDispatcher {
    void register(String eventName, IEventListener callback);

    void clear(String eventName);

    void emit(String eventName, Object param);
}
