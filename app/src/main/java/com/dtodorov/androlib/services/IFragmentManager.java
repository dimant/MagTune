package com.dtodorov.androlib.services;

/**
 * Created by ditodoro on 4/14/2017.
 */

interface IFragmentManager {
    void putObject(String key, Object value);

    <T> T getObject(String key);
}
