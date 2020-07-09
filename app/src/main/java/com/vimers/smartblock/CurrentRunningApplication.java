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
    //Returns package name of currently running application like "com.android.settings"
    private static int sequence_number = 0; //Used for getting previously installed apps and adding them to blocked list
    public static String getPackageName() {
        String currentApp = "NULL";
        Context context;
        if((context = MainActivity.getContextOfApplication()) == null) {
            context = DialogDisplayService.getContext();
        } //Gets context of this application

        UsageStatsManager usm = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        long time = System.currentTimeMillis();//Getting current time to set time period in which some applications are being active
        List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time);//Gets a list of active applications during given period of time
        if (appList != null && appList.size() > 0) {
            SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();//Creating a Map array and filling it with pairs of UsageStats and time when it was active
            for (UsageStats usageStats : appList) {
                mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
            }
            if (mySortedMap != null && !mySortedMap.isEmpty()) {
                if((currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName()).equals("android")) {//checkup of some devices which put "android" on top of UsageStats
                    try{
                        removeLastElement(mySortedMap);
                        currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                    }
                    catch (Exception e) {
                        currentApp = "NULL";
                    }
                }
            }
        }
        return currentApp;
    }
    //Simply removes last added element from given Map array
    private static void removeLastElement(SortedMap<Long, UsageStats> map) {
        map.remove(map.lastKey());
    }
    //Checks if user stays on Home Screen at the moment
    @SuppressLint("NewApi")
    public static boolean isOnHomeScreen() {
        if (android.os.Build.VERSION.SDK_INT <= 23) { //Works only on older versions
            Context context;
            if((context = MainActivity.getContextOfApplication()) == null) {
                context = DialogDisplayService.getContext();
            }
            ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName currentTask = null;

            currentTask = taskInfo.get(0).topActivity;
            final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
            mainIntent.addCategory(Intent.CATEGORY_HOME);
            PackageManager mPackageManager = context.getPackageManager();

            List<ResolveInfo> appList = mPackageManager.queryIntentActivities(mainIntent, 0);

            for (int i = 0; i < appList.size(); i++) {
                ResolveInfo apl = appList.get(i);
                if (currentTask.getPackageName().equals(apl.activityInfo.packageName)) {
                    return true;
                }
            }
        }
        return false;
    }
    //Adds earlier installed applications to set
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void putNewAddedApps() {
        Context context;
        if((context = MainActivity.getContextOfApplication()) == null) {
            context = DialogDisplayService.getContext();
        }

        ChangedPackages changedPackages = context.getPackageManager().getChangedPackages(sequence_number);
        if(changedPackages != null) {
            sequence_number = changedPackages.getSequenceNumber();
            for (String packageName : changedPackages.getPackageNames()) {
                BlockedAppsListManager.addApp(packageName);
            }
        }
    }
}
