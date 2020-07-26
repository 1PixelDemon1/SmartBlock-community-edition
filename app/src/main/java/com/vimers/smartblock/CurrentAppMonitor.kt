package com.vimers.smartblock

import android.app.ActivityManager
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Build

class CurrentAppMonitor(private val context: Context) {
    fun getCurrentRunningApp(): String? {
        val usm = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager?
                ?: return null
        val appList = getRecentActiveApps(usm)
        return getLastRunningApp(appList)
    }

    /**
     * Checks if the home screen is active now.
     */
    fun isOnHomeScreen(): Boolean {
        // Works only on older versions
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M)
            return false

        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val taskInfo = am.getRunningTasks(1)
        val currentTask = taskInfo[0].topActivity ?: return false
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_HOME)
        val appList = context.packageManager.queryIntentActivities(mainIntent, 0)
        for (i in appList.indices) {
            val apl = appList[i]
            if (currentTask.packageName == apl.activityInfo.packageName) {
                return true
            }
        }
        return false
    }

    private fun getLastRunningApp(appList: List<UsageStats>) =
            appList.filterNot { it.packageName == "android" }.maxBy { it.lastTimeUsed }?.packageName

    private fun getRecentActiveApps(usm: UsageStatsManager): List<UsageStats> {
        // Get current time to set time period in which some applications are being active
        val time = System.currentTimeMillis()
        // Get a list of active applications during given period of time
        return usm.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY,
                time - 1000 * 1000,
                time
        )
    }
}