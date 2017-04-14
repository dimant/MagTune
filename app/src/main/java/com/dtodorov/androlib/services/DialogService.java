package com.dtodorov.androlib.services;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;

public class DialogService extends DialogFragment implements IDialogPresenter {

    private String _explanation;
    private IDialogListener _listener;
    private FragmentManager _manager;
    private int _button_ok_id;
    private int _button_cancel_id;

    public void initialize(FragmentManager manager, int button_ok_id, int button_cancel_id)
    {
        _manager = manager;
        _button_ok_id = button_ok_id;
        _button_cancel_id = button_cancel_id;
    }

    @Override
    public void presentDialog(String explanation, IDialogListener listener)
    {
        _explanation = explanation;
        _listener = listener;
        this.show(_manager, explanation);
    }

    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(_explanation)
                .setPositiveButton(_button_ok_id, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(_listener != null) {
                            _listener.onOk();
                        }
                    }
                })
                .setNegativeButton(_button_cancel_id, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(_listener != null) {
                            _listener.onCancel();
                        }
                    }
                });
        return builder.create();
    }
}