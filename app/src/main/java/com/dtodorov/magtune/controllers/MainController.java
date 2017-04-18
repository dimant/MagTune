package com.dtodorov.magtune.controllers;

import android.Manifest;

import com.dtodorov.androlib.services.IPermissionService;
import com.dtodorov.androlib.services.IStringResolver;
import com.dtodorov.magtune.R;

public class MainController {

    private IStringResolver _stringResolver;
    private IPermissionService _permissionService;


    public MainController(IStringResolver stringResolver, IPermissionService permissionService)
    {
        _stringResolver = stringResolver;
        _permissionService = permissionService;

        _permissionService.obtainPermissionIfNotGranted(
                Manifest.permission.BLUETOOTH,
                _stringResolver.getString(R.string.explanation_bluetooth));
    }
}
