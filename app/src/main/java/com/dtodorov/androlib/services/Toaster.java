package com.dtodorov.androlib.services;

import android.content.Context;
import android.content.res.Resources;
import android.widget.Toast;

/**
 * Created by diman on 3/24/2016.
 */
public class Toaster implements IToaster {
    private Context _context;
    private IStringResolver _stringResolver;

    public Toaster(Context context, IStringResolver stringResolver)
    {
        _context = context;
        _stringResolver = stringResolver;
    }

    @Override
    public void toast(int id)
    {
        this.toast(_stringResolver.getString(id));
    }

    @Override
    public void toast(String text)
    {
        Toast toast = Toast.makeText(_context, text, Toast.LENGTH_LONG);
        toast.show();
    }
}
