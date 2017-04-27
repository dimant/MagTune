package com.dtodorov.androlib.services;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class IntentService implements IIntentService
{
    private class RequestCode
    {
        private int _code = 0;

        public int getNextCode()
        {
            return ++_code;
        }

        public int getCode()
        {
            return _code;
        }
    }

    private Activity _activity;
    private Map<Integer, IIntentListener> _listeners;

    private RequestCode _code;

    public IntentService(Activity activity)
    {
        _activity = activity;
        _listeners = new HashMap<Integer, IIntentListener>();

        _code = new RequestCode();
    }

    @Override
    public void enactIntent(String action, IIntentListener listener)
    {
        Intent intent = new Intent(action);

        _listeners.put(_code.getNextCode(), listener);
        _activity.startActivityForResult(intent, _code.getCode());
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
