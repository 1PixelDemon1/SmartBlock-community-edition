package com.vimers.smartblock;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import java.util.TimerTask;

public class DialogTimer {
    //returns list of activities to do during app-working
    static public TimerTask getTimerTask() {
        return new TimerTask() {
            @Override
            public void run() {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        CurrentRunningApplication.putNewAddedApps(); //Adds earlier installed apps in blocked list every time block is displayed
                    }
                    if(!CurrentRunningApplication.isOnHomeScreen() && BlockedAppsListManager.getPackageNames().contains(CurrentRunningApplication.getPackageName())) {
                        if(!DialogBlockerManager.isActive) {
                            AlertDialog alertDialog = DialogBlockerManager.getDialog();
                            alertDialog.show();
                            DialogBlockerManager.fillDialog(alertDialog);
                            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(DialogBlockerManager.getButtonAction(alertDialog));
                            DialogBlockerManager.isActive = true;
                        }
                    }
                }
            });
            }
        };
    }
}
