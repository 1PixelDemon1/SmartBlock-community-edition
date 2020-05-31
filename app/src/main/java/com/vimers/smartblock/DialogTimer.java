package com.vimers.smartblock;

import android.app.AlertDialog;
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
