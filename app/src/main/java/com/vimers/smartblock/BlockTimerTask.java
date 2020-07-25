package com.vimers.smartblock;

import android.content.Context;
import android.os.Build;

import java.util.TimerTask;

public class BlockTimerTask extends TimerTask {
    private final Context context;
    private final BlockedAppsSet blockedAppsSet;
    // TODO: Configurable delay


    public BlockTimerTask(Context context, BlockedAppsSet blockedAppsSet) {
        this.context = context;
        this.blockedAppsSet = blockedAppsSet;
    }

    @Override
    public void run() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            blockedAppsSet.updateWithNewApps(); //Adds earlier installed apps in blocked list every time block is displayed
        }
        new Blocker(context).block();
    }
}
