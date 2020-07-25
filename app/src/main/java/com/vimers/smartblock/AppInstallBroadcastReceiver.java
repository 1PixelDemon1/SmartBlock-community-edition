package com.vimers.smartblock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static android.content.Intent.EXTRA_UID;

public class AppInstallBroadcastReceiver extends BroadcastReceiver {
    //Launches when some app is installed on device
    @Override
    public void onReceive(Context context, Intent intent) {
        BlockedAppsSet blockedAppsSet = new BlockedAppsSet(context.getApplicationContext());
        blockedAppsSet.add(
                context.getPackageManager().getNameForUid(intent.getIntExtra(EXTRA_UID, 0))
        );
    }
}
