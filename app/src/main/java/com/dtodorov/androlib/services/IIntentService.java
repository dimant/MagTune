package com.dtodorov.androlib.services;

import android.content.Intent;

public interface IIntentService
{
    void enactIntent(String action, IIntentListener listener);

    // unfortunately you have to call this manually from your activity, thanks android
    void onActivityResult(int requestCode, int resultCode, Intent data);
}
