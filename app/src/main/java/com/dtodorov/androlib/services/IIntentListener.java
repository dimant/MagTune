package com.dtodorov.androlib.services;

import android.content.Intent;

public interface IIntentListener
{
    void onResult(int resultCode, Intent data);
}
