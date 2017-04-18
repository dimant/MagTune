package com.dtodorov.androlib.services;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.HashMap;
import java.util.Map;

public class PermissionRequester implements IPermissionRequester {
    private Map<String, Integer> _permissionCodes;
    private Map<String, Boolean> _explained;

    private int _lastCode;

    private Activity _context;
    private IPermissionListener _listener;

    public PermissionRequester(Activity context) {
        Init(context, null);
    }

    public PermissionRequester(Activity context, IPermissionListener listener) {
        Init(context, listener);
    }

    private void Init(Activity context, IPermissionListener listener) {
        _explained = new HashMap<String, Boolean>();
        _context = context;
        _permissionCodes = new HashMap<String, Integer>();
        _listener = listener;
    }

    public void setListener(IPermissionListener listener) {
        _listener = listener;
    }

    private int getCode(String permission) {
        if(_permissionCodes.containsKey(permission)) {
            return _permissionCodes.get(permission);
        } else {
            Integer code = _lastCode;
            _permissionCodes.put(permission, code);
            _lastCode += 1;
            return code;
        }
    }

    private String getPermission(Integer code) {
        for(String permission : _permissionCodes.keySet()) {
            if(_permissionCodes.get(permission).equals(code))
                return permission;
        }

        return "";
    }

    @Override
    public int checkPermission(String permission) {
        return ContextCompat.checkSelfPermission(_context, permission);
    }

    @Override
    public void obtainPermission(String permission) {
        if(checkPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            if(_explained.containsKey(permission) == false && ActivityCompat.shouldShowRequestPermissionRationale(_context, permission)) {
                _listener.explainPermission(permission);
                _explained.put(permission, true);
            } else {
                ActivityCompat.requestPermissions(_context, new String[] { permission }, getCode(permission));
            }
        }
    }

    public void onRequestPermissionResult(int code, String received[], int[] results) {
        if(_listener == null)
            return;

        String requested = getPermission(code);
        if(requested != "") {
            if(received.length == 0) {
                _listener.onCancelled(requested);
            } else if( received[0].equals(requested)
                    && results[0] == PackageManager.PERMISSION_GRANTED) {
                _listener.onGranted(requested);
            } else {
                _listener.onDenied(requested);
            }
        }
    }
}
