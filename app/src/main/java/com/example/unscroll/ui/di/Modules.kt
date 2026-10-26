package com.example.unscroll.ui.di

import com.example.unscroll.data.repo.UsageStatsRepository
import com.example.unscroll.ui.usage.UsageViewModel
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel

val appModule = module {
    single { UsageStatsRepository(get()) }
    viewModel<UsageViewModel>()
}