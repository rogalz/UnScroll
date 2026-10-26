package com.example.unscroll.ui.navigation

import android.net.Uri

object Routes {
    const val MONITORED_APPS = "monitored_apps"
    const val APP_PICKER = "app_picker"
    const val LIMIT_SETUP = "limit_setup/{packageName}/{appName}"

    fun limitSetup(packageName: String, appName: String): String {
        return "limit_setup/${Uri.encode(packageName)}/${Uri.encode(appName)}"
    }
}