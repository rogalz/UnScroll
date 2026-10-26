package com.example.unscroll.data.repo

import android.app.AppOpsManager
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Process
import com.example.unscroll.data.model.UsageStatItem
import java.util.Calendar

class UsageStatsRepository(
    private val context: Context,
) {
    private val usageStatsManager =
        context.getSystemService(Context.USAGE_STATS_SERVICE)
                as UsageStatsManager

    fun hasUsageAccess(): Boolean {
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE)
                as AppOpsManager

        return appOps.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            Process.myUid(),
            context.packageName,
        ) == AppOpsManager.MODE_ALLOWED
    }

    fun getTodayUsage(): List<UsageStatItem> {
        if (!hasUsageAccess()) return emptyList()

        val now = System.currentTimeMillis()

        val startOfDay = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        val stats = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY,
            startOfDay,
            now,
        )

        return stats
            .filter { it.totalTimeInForeground > 0L }
            .groupBy { it.packageName }
            .mapNotNull { (packageName, entries) ->
                val totalTime = entries.sumOf {
                    it.totalTimeInForeground
                }

                if (totalTime <= 0L) return@mapNotNull null

                UsageStatItem(
                    packageName = packageName,
                    appName = getAppName(packageName),
                    totalTimeInForeground = totalTime,
                    lastTimeUsed = entries.maxOf {
                        it.lastTimeUsed
                    },
                    firstTimeStamp = entries.minOf {
                        it.firstTimeStamp
                    },
                    lastTimeStamp = entries.maxOf {
                        it.lastTimeStamp
                    },
                )
            }
            .sortedByDescending { it.totalTimeInForeground }
    }

    private fun getAppName(packageName: String): String {
        return try {
            val appInfo = context.packageManager
                .getApplicationInfo(packageName, 0)

            context.packageManager
                .getApplicationLabel(appInfo)
                .toString()
        } catch (_: PackageManager.NameNotFoundException) {
            packageName
        } catch (_: SecurityException) {
            packageName
        }
    }
}