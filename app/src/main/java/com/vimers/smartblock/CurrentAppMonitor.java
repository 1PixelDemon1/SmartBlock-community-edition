package com.vimers.smartblock;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import static android.content.Context.ACTIVITY_SERVICE;

public class CurrentAppMonitor {
    private final Context appContext;
    private final PackageManager packageManager;

    CurrentAppMonitor(Context appContext) {
        this.appContext = appContext;
        packageManager = appContext.getPackageManager();
    }

    /**
     * Removes the last added element from given map.
     */
    private static void removeLastElement(SortedMap<Long, UsageStats> map) {
        map.remove(map.lastKey());
    }

    @Nullable
    private static String getLastRunningApp(List<UsageStats> appList) {
        // Creating a Map and filling it with pairs of UsageStats and time when it was active
        SortedMap<Long, UsageStats> lastRunningAppsMap = new TreeMap<>();
        for (UsageStats usageStats : appList) {
            lastRunningAppsMap.put(usageStats.getLastTimeUsed(), usageStats);
        }

        String currentApp = null;

        // Checkup of some devices which put "android" on top of UsageStats
        if (
                (currentApp = lastRunningAppsMap.get(lastRunningAppsMap.lastKey()).getPackageName())
                        .equals("android")
        ) {
            try {
                removeLastElement(lastRunningAppsMap);
                currentApp = lastRunningAppsMap.get(lastRunningAppsMap.lastKey()).getPackageName();
            } catch (RuntimeException e) {
                currentApp = null;
            }
        }

        return currentApp;
    }

    private static List<UsageStats> getRecentActiveApps(UsageStatsManager usm) {
        // Get current time to set time period in which some applications are being active
        long time = System.currentTimeMillis();
        // Get a list of active applications during given period of time
        return usm.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY,
                time - (1000 * 1000),
                time
        );
    }

    public String getPackageName() {
        UsageStatsManager usm = (UsageStatsManager) appContext.getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> appList = getRecentActiveApps(usm);
        if ((appList == null) || (appList.isEmpty()))
            return null;
        return getLastRunningApp(appList);
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
}
