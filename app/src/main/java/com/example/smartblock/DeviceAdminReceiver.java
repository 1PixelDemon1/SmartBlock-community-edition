package com.example.smartblock;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.WindowManager;

public class DeviceAdminReceiver extends android.app.admin.DeviceAdminReceiver {


    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        return "При выключении ребенок сможет удалить приложение, вы уверены?";
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    public void onEnabled(Context context, Intent intent) {};

    public void onDisabled(Context context, Intent intent) {};
}
