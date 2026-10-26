package com.example.unscroll.ui.di

import androidx.room.Room
import com.example.unscroll.data.repo.MonitoringRepository
import com.example.unscroll.data.repo.UsageStatsRepository
import com.example.unscroll.data.room.AppDatabase
import com.example.unscroll.ui.appPicker.AppPickerViewModel
import com.example.unscroll.ui.monitoring.MonitoringViewModel
import com.example.unscroll.ui.usage.UsageViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel

val appModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "app_database"
        ).build()
    }
    single { UsageStatsRepository(get()) }
    viewModel<UsageViewModel>()
    viewModel<MonitoringViewModel>()
    viewModel<AppPickerViewModel>()
    single { get<AppDatabase>().monitoredAppDao() }
    single { MonitoringRepository(get()) }
}