package com.dtodorov.androlib.services;

public interface IPermissionService {
    void obtainPermission(String permission, String explanation);

    Status getPermissionStatus(String permission);

    public enum Status {
        Granted,
        Denied
    }
}
