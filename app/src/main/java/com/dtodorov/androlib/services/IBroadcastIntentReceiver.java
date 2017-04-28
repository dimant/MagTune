package com.dtodorov.androlib.services;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by ditodoro on 4/27/2017.
 */

public interface IBroadcastIntentReceiver {
    IntentFilter getFilter();
    void onReceive(Context context, Intent intent);
}
