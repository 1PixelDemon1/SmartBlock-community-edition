package com.vimers.smartblock;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ChangedPackages;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import static android.content.Context.ACTIVITY_SERVICE;

public class CurrentRunningApplication {
    private final Context appContext;
    private final PackageManager packageManager;
    //Returns package name of currently running application like "com.android.settings"
    private int sequenceNumber; //Used for getting previously installed apps and adding them to blocked list

    CurrentRunningApplication(Context appContext) {
        this.appContext = appContext;
        packageManager = appContext.getPackageManager();
    }

    /**
     * Removes the last added element from given map.
     */
    private static void removeLastElement(SortedMap<Long, UsageStats> map) {
        map.remove(map.lastKey());
    }

    public String getPackageName() {
        UsageStatsManager usm = (UsageStatsManager) appContext.getSystemService(Context.USAGE_STATS_SERVICE);
        // Get current time to set time period in which some applications are being active
        long time = System.currentTimeMillis();
        // Get a list of active applications during given period of time
        List<UsageStats> appList = usm.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY,
                time - (1000 * 1000),
                time
        );
        if ((appList == null) || (appList.isEmpty()))
            return null;

        // Creating a Map and filling it with pairs of UsageStats and time when it was active
        SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
        for (UsageStats usageStats : appList) {
            mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
        }
        if (mySortedMap.isEmpty())
            return null;

        String currentApp = null;

        // Checkup of some devices which put "android" on top of UsageStats
        if (
                (currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName())
                        .equals("android")
        ) {
            try {
                removeLastElement(mySortedMap);
                currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
            } catch (RuntimeException e) {
                currentApp = null;
            }
        }

        return currentApp;
    }

    /**
     * Checks if the home screen is active now.
     */
    @SuppressLint("NewApi")
    public boolean isOnHomeScreen() {
        // Works only on older versions
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M)
            return false;

        ActivityManager am = (ActivityManager) appContext.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        ComponentName currentTask = taskInfo.get(0).topActivity;
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_HOME);

        List<ResolveInfo> appList = packageManager.queryIntentActivities(mainIntent, 0);

        for (int i = 0; i < appList.size(); i++) {
            ResolveInfo apl = appList.get(i);
            if (currentTask.getPackageName().equals(apl.activityInfo.packageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds earlier installed applications to the blocked apps set.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void putNewAddedApps() {
        ChangedPackages changedPackages = packageManager.getChangedPackages(sequenceNumber);
        if (changedPackages == null)
            return;

        sequenceNumber = changedPackages.getSequenceNumber();
        BlockedAppsSet blockedAppsSet = new BlockedAppsSet(appContext);
        for (String packageName : changedPackages.getPackageNames()) {
            blockedAppsSet.add(packageName);
        }
    }
}
