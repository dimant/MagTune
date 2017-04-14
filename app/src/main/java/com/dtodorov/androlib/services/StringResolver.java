package com.dtodorov.androlib.services;

import android.content.res.Resources;

/**
 * Created by diman on 4/1/2016.
 */
public class StringResolver implements IStringResolver {
    private Resources _resources;

    public StringResolver(Resources resources) {
        _resources = resources;
    }

    @Override
    public String getString(int id) {
        return _resources.getString(id);
    }
}
