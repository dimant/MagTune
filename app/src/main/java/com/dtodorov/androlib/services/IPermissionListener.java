package com.dtodorov.androlib.services;

public interface IPermissionListener {
    void explainPermission(String permission);
    void onGranted(String permission);
    void onDenied(String permission);
    void onCancelled(String permission);
}

