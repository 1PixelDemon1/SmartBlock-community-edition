package com.vimers.smartblock;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BlockedAppsListManager {
    static private SharedPreferences blockedAppsListPreference;
    static private Set<String> packageNames;

    //Adds an app into Set and re-set SharedPreferences
    static boolean addApp(String packageName) {
        if(packageNames == null || blockedAppsListPreference == null) {
            reset();
        }
        if(packageNames.contains(packageName)) {
            return false;
        }
        packageNames.add(packageName);
        resetPreferences();
        return true;
    }
    //Removes an app into Set and re-set SharedPreferences
    static boolean removeApp(String packageName) {
        if(packageNames == null || blockedAppsListPreference == null) {
            reset();
        }
        if(!packageNames.contains(packageName)) {
            return false;
        }
        packageNames.remove(packageName);
        resetPreferences();
        return true;
    }
    //Re-sets SharedPreferences
    static private void resetPreferences() {
        Context applicationContext;
        if((applicationContext = MainActivity.getContextOfApplication()) == null) {
            applicationContext = DialogDisplayService.getContext();
        }
        blockedAppsListPreference = applicationContext.getSharedPreferences("BLOCKED_APPS", Context.MODE_PRIVATE);
        SharedPreferences.Editor preferenceEditor = blockedAppsListPreference.edit();
        preferenceEditor.clear();
        preferenceEditor.putStringSet("PACKAGE_NAMES", packageNames);
        preferenceEditor.apply();
    }
    //Re-sets SharedPreferences and defines Set
    static private void reset() {
        Context applicationContext;
        if((applicationContext = MainActivity.getContextOfApplication()) == null) {
            applicationContext = DialogDisplayService.getContext();
        }
        blockedAppsListPreference = applicationContext.getSharedPreferences("BLOCKED_APPS", Context.MODE_PRIVATE);
        packageNames = blockedAppsListPreference.getStringSet("PACKAGE_NAMES", new HashSet<String>());
    }
    //returns Set
    static public Set<String> getPackageNames() {
        if(packageNames == null) {
            reset();
        }
        return packageNames;
    }
    //Writes set to console
    static public void printPackageNames() {
        for(String packageName: packageNames) {
            Log.d("PackageNames", packageName);
        }
    }
    //Clears set
    static public void clearSet() {
        if(packageNames == null) {
            reset();
        }
        packageNames.clear();
        resetPreferences();
    }
    //Fills set with all of packages installed on device
    static public void setAllPossible() {
        if(packageNames == null) {
            reset();
        }
        packageNames.clear();
        Context applicationContext;
        if((applicationContext = MainActivity.getContextOfApplication()) == null) {
            applicationContext = DialogDisplayService.getContext();
        }
        List<ApplicationInfo> appList = applicationContext.getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA);
        for(ApplicationInfo app: appList) {
            packageNames.add(app.packageName);
        }
        resetPreferences();
    }
    //Sets all of the "Have to block" apps
    static public void setAllAuto() {
        Context applicationContext;
        if((applicationContext = MainActivity.getContextOfApplication()) == null) {
            applicationContext = DialogDisplayService.getContext();
        }
        List<ApplicationInfo> appList = applicationContext.getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA);

        packageNames.clear();
        //Adds all packages, which were installed after android installation and apps, which were installed by manufacturer/ Except for Google apps(Pre-installed by android)
        for (ApplicationInfo aInfo: appList) {
            if (((aInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) || ((aInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 1)) {
                addApp(aInfo.packageName);
            }
        }
        addApp("com.android.chrome");
        addApp("com.google.android.googlequicksearchbox");
        addApp("com.google.android.youtube");
        removeApp("com.vimers.smartblock");

        resetPreferences();
    }
}
