package com.dtodorov.androlib.eventdispatcher;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by diman on 3/27/2016.
 */
public class EventDispatcher implements IEventDispatcher {
    private Map<String, IEventListener> _eventMap = new HashMap<String, IEventListener>();

    @Override
    public void register(String eventName, IEventListener callback) {
        _eventMap.put(eventName, callback);
    }

    @Override
    public void clear(String eventName) {
        _eventMap.remove(eventName);
    }

    @Override
    public void emit(String eventName, Object param) {
        if(_eventMap.containsKey(eventName)) {
            IEventListener callback = _eventMap.get(eventName);
            callback.callback(param);
        }
    }
}
