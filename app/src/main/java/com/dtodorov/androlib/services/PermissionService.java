package com.dtodorov.androlib.services;

import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ditodoro on 4/1/2016.
 */
public class PermissionService implements IPermissionListener, IPermissionService {
    private Map<String, String> _explanations;
    private IPermissionRequester _requester;
    private IDialogPresenter _dialogPresenter;

    public PermissionService(IPermissionRequester requester, IDialogPresenter dialogPresenter) {
        _requester = requester;
        _dialogPresenter = dialogPresenter;
        _explanations = new HashMap<String, String>();
        _requester.setListener(this);
    }

    @Override
    public void obtainPermission(String permission, String explanation) {
        _explanations.put(permission, explanation);
        _requester.obtainPermission(permission);
    }

    @Override
    public Status getPermissionStatus(String permission) {
        if(_requester.checkPermission(permission) == PackageManager.PERMISSION_GRANTED) {
            return Status.Granted;
        } else {
            return Status.Denied;
        }
    }

    @Override
    public void explainPermission(String permission) {
        AsyncTask<String, Void, Void> task = new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... params) {
                final String permission = params[0];
                final String explanation = params[1];
                _dialogPresenter.presentDialog(explanation, new IDialogListener() {
                    @Override
                    public void onOk() {
                        _requester.obtainPermission(permission);
                    }

                    @Override
                    public void onCancel() {
                    }
                });
                return null;
            }
        };

        task.execute(permission, _explanations.get(permission));
    }

    @Override
    public void onGranted(String permission) {
    }

    @Override
    public void onDenied(String permission) {
    }

    @Override
    public void onCancelled(String permission) {
    }
}
