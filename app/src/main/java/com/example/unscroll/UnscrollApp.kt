package com.example.unscroll

import android.app.Application
import com.example.unscroll.ui.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin


class UnscrollApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@UnscrollApp)
            modules(appModule)
        }
    }
}