package com.dtodorov.androlib.services;

import android.app.Activity;
import android.content.Intent;

import java.util.HashMap;
import java.util.Map;

public class IntentService implements IIntentService
{
    private Activity _activity;
    private Map<Integer, IIntentListener> _listeners;

    public IntentService(Activity activity)
    {
        _activity = activity;
        _listeners = new HashMap<Integer, IIntentListener>();
    }

    @Override
    public void enactIntent(String action, IIntentListener listener)
    {
        Intent intent = new Intent(action);
        int requestCode = listener.hashCode();
        _listeners.put(requestCode, listener);
        _activity.startActivityForResult(intent, requestCode);
    }

    // unfortunately you have to call this manually from your activity, thanks android
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        IIntentListener listener = _listeners.get(requestCode);
        if(listener != null)
        {
            listener.onResult(resultCode, data);
            _listeners.remove(requestCode);
        }
    }
}
