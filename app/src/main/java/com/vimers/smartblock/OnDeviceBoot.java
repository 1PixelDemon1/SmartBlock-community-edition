package com.vimers.smartblock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class OnDeviceBoot extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Build.VERSION.SDK_INT >= 26) {
            context.startForegroundService(new Intent(context ,DialogDisplayService.class));
        }
        else {
            context.startService(new Intent(context ,DialogDisplayService.class));
        }
    }
}
