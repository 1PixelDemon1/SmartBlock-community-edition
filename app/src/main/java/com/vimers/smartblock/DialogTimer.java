package com.vimers.smartblock;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import java.util.TimerTask;

public class DialogTimer {
    private final Context appContext;
    private final CurrentRunningApplication currentRunningApplication;
    private final BlockedAppsSet blockedAppsSet;
    private final DialogBlockerManager dialogBlockerManager;

    DialogTimer(Context appContext) {
        this.appContext = appContext;
        currentRunningApplication = new CurrentRunningApplication(appContext);
        blockedAppsSet = new BlockedAppsSet(appContext);
        dialogBlockerManager = new DialogBlockerManager(appContext);
    }

    //returns list of activities to do during app-working
    public TimerTask getTimerTask() {
        return new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            currentRunningApplication.putNewAddedApps(); //Adds earlier installed apps in blocked list every time block is displayed
                        }
                        if (!currentRunningApplication.isOnHomeScreen() && blockedAppsSet.getAll().contains(currentRunningApplication.getPackageName())) {
                            if (!DialogBlockerManager.isActive) {
                                AlertDialog alertDialog = DialogBlockerManager.getDialog();
                                alertDialog.show();
                                DialogBlockerManager.fillDialog(alertDialog);
                                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(dialogBlockerManager.getButtonAction(alertDialog));
                                DialogBlockerManager.isActive = true;
                            }
                        }
                }
            });
            }
        };
    }
}
