package com.dtodorov.androlib.services;

public interface IPermissionRequester {
    void setListener(IPermissionListener listener);
    int checkPermission(String permission);
    void obtainPermission(String permission);
}
