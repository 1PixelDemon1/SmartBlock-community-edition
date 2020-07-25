package com.vimers.smartblock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class DeviceBootBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //Starts Service of background working
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            context.startForegroundService(new Intent(context, DialogDisplayService.class));
        else
            context.startService(new Intent(context, DialogDisplayService.class));
    }
}
