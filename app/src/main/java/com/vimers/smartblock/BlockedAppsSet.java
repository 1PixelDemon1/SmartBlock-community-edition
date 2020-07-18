package com.vimers.smartblock;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * {@code BlockedAppsListManager} provides the access to the blocked applications list.
 * This list is persistent. Persistence is implemented by utilizing the Shared Preferences.
 */
public class BlockedAppsSet {
    private static final String BLOCKED_APPS_PREF = "PACKAGE_NAMES";
    private static final String BLOCKED_APPS_KEY = "BLOCKED_APPS";
    private static final String[] SPECIAL_BLOCKLIST = {
            "com.android.chrome",
            "com.google.android.googlequicksearchbox",
            "com.google.android.youtube"
    };
    private final Context appContext;
    private final SharedPreferences appsSetSharedPrefs;
    private Set<String> apps;

    public BlockedAppsSet(Context appContext) {
        this.appContext = appContext;
        appsSetSharedPrefs = appContext.getSharedPreferences(
                BLOCKED_APPS_PREF,
                Context.MODE_PRIVATE
        );
        load();
    }

    /**
     * Loads the {@code packageNames} set from the Shared Preferences.
     */
    private void load() {
        Set<String> prefsSet = appsSetSharedPrefs.getStringSet(BLOCKED_APPS_KEY, null);

        // According to SharedPreferences.getStringSet() documentation,
        // it's not recommended to modify the set which it returns.
        // So we convert its result to our own set.
        apps = (prefsSet != null) ? new HashSet<>(prefsSet) : new HashSet<>();
    }

    /**
     * Saves the {@code packageNames} set to the Shared Preferences.
     */
    private void save() {
        appsSetSharedPrefs.edit()
                .putStringSet(BLOCKED_APPS_KEY, apps)
                .apply();
    }

    /**
     * Adds an app into the blocked apps set.
     */
    public void add(@NonNull String packageName) {
        // We never want to block ourselves
        if (packageName.equals(appContext.getPackageName()))
            return;
        apps.add(packageName);
        save();
    }

    /**
     * Removes an app from the blocked apps set.
     */
    public void remove(@NonNull String packageName) {
        apps.remove(packageName);
        save();
    }

    public Set<String> getAll() {
        return apps;
    }

    public void clear() {
        apps.clear();
        save();
    }

    /**
     * Retrieves the list of all applications installed on this device and adds them to the set.
     */
    public void setAllInstalled() {
        List<ApplicationInfo> allApps = appContext.getPackageManager()
                .getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo appInfo : allApps) {
            apps.add(appInfo.packageName);
        }
        save();
    }

    private boolean isUserApp(ApplicationInfo appInfo) {
        return ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) ||
                ((appInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 1);
    }

    /**
     * Adds the apps which were installed by the user, to the set.
     */
    private void addUserInstalled() {
        List<ApplicationInfo> appList = appContext.getPackageManager()
                .getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo appInfo : appList) {
            if (isUserApp(appInfo))
                add(appInfo.packageName);
        }
    }

    /**
     * Adds the apps which are listed in {@code SPECIAL_BLOCKLIST}, to the set.
     */
    private void addFromSpecialBlocklist() {
        apps.addAll(Arrays.asList(SPECIAL_BLOCKLIST));
    }

    /**
     * Adds the apps which match the set of criteria described below, to the set.
     * Removes all apps which do not match, from the set.
     * <p>
     * Criteria:
     * <ol>
     *     <li>The application was installed by the user.</li>
     *     <li>
     *         It is in the list of special system apps such as YouTube
     *         (see {@code SPECIAL_BLOCKLIST} for the full list).
     *     </li>
     * </ol>
     */
    public void setAuto() {
        apps.clear();
        addUserInstalled();
        addFromSpecialBlocklist();
        save();
    }
}
