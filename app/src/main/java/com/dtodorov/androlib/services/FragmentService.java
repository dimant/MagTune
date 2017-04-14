package com.dtodorov.androlib.services;

import android.app.Fragment;
import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

public class FragmentService extends Fragment implements IFragmentManager {
    private Map<String, Object> _retainedData;

    public FragmentService() {
        _retainedData = new HashMap<String, Object>();
    }

    // this method is only called once for this fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


    @Override
    public void putObject(String key, Object value) {
        _retainedData.put(key, value);
    }

    @Override
    public <T> T getObject(String key) {
        return (T) _retainedData.get(key);
    }
}
