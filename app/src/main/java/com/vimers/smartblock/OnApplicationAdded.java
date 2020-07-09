package com.vimers.smartblock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static android.content.Intent.EXTRA_UID;

public class OnApplicationAdded extends BroadcastReceiver {
    //Launches when some app is installed on device
    @Override
    public void onReceive(Context context, Intent intent) {
        BlockedAppsListManager.addApp(context.getPackageManager().getNameForUid(intent.getIntExtra(EXTRA_UID, 0))); //Adds app to blocked list
    }
}
