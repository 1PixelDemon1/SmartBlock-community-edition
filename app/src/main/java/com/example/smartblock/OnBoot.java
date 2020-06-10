package com.example.smartblock;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

public class OnBoot extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (Build.VERSION.SDK_INT >= 23) {
//            if (!Settings.canDrawOverlays(context)) {
//                Intent intentSett = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
//                context.startActivity(intentSett);
//            } else {
            Intent serviceIntent = new Intent(context, AppLaunchDetectionService.class);
            if(Build.VERSION.SDK_INT >= 26) context.startForegroundService(serviceIntent);
            else context.startService(serviceIntent);
//            }
        } else {
            Intent serviceIntent = new Intent(context, AppLaunchDetectionService.class);
            context.startService(serviceIntent);
        }
    }
}