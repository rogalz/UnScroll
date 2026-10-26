
package com.example.unscroll.ui.monitoring

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unscroll.data.model.UsageStatItem
import com.example.unscroll.data.repo.MonitoringRepository
import com.example.unscroll.data.repo.UsageStatsRepository
import com.example.unscroll.data.room.MonitoredAppEntity
import com.example.unscroll.domain.monitoring.UsageProgress
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class MonitoredAppUi(
    val rule: MonitoredAppEntity,
    val appName: String,
    val usedMillis: Long,
) {
    private val progress: UsageProgress
        get() = UsageProgress.from(usedMillis, rule.dailyLimitMinutes)

    val usedMinutes: Int
        get() = progress.usedMinutes

    val limitMinutes: Int
        get() = progress.limitMinutes

    val remainingMinutes: Int
        get() = progress.remainingMinutes

    val fraction: Float
        get() = progress.fraction

    val exceeded: Boolean
        get() = progress.exceeded

    val percentage: Int
        get() = progress.percentage
}

data class MonitoringUiState(
    val monitoredApps: List<MonitoredAppUi> = emptyList(),
    val isRefreshing: Boolean = false,
    val error: String? = null,
)

data class RuleSaveUiState(
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
)

class MonitoringViewModel(
    private val monitoringRepository: MonitoringRepository,
    private val usageStatsRepository: UsageStatsRepository,
) : ViewModel() {

    private val usageItems =
        MutableStateFlow<List<UsageStatItem>>(emptyList())

    private val isRefreshing = MutableStateFlow(false)
    private val errorMessage = MutableStateFlow<String?>(null)
    private val _saveUiState = MutableStateFlow(RuleSaveUiState())
    val saveUiState = _saveUiState.asStateFlow()

    val uiState = combine(
        monitoringRepository.observeAll(),
        usageItems,
        isRefreshing,
        errorMessage,
    ) { rules, usage, refreshing, message ->
        val usageByPackage = usage.associateBy { it.packageName }

        val monitored = rules.map { rule ->
            val stats = usageByPackage[rule.packageName]

            MonitoredAppUi(
                rule = rule,
                appName = stats?.appName
                    ?: usageStatsRepository.getAppName(rule.packageName),
                usedMillis = stats?.totalTimeInForeground ?: 0L,
            )
        }

        MonitoringUiState(
            monitoredApps = monitored,
            isRefreshing = refreshing,
            error = message,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = MonitoringUiState(),
    )

    fun refresh() {
        viewModelScope.launch {
            isRefreshing.value = true
            errorMessage.value = null

            try {
                val result = withContext(Dispatchers.IO) {
                    if (!usageStatsRepository.hasUsageAccess()) {
                        throw IllegalStateException(
                            "Brak dostępu do statystyk użycia."
                        )
                    }

                    usageStatsRepository.getTodayUsage()
                }

                usageItems.value = result
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                errorMessage.value = e.message
                    ?: "Nie udało się odczytać statystyk."
            } finally {
                isRefreshing.value = false
            }
        }
    }

    fun saveRule(
        packageName: String,
        dailyLimitMinutes: Int,
        onSaved: () -> Unit,
    ) {
        viewModelScope.launch {
            _saveUiState.value = RuleSaveUiState(isSaving = true)
            try {
                withContext(Dispatchers.IO) {
                    monitoringRepository.save(
                        packageName = packageName,
                        dailyLimitMinutes = dailyLimitMinutes,
                    )
                }
                _saveUiState.value = RuleSaveUiState()
                onSaved()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _saveUiState.value = RuleSaveUiState(
                    errorMessage = e.message
                    ?: "Nie udało się zapisać limitu."
                )
            }
        }
    }

    fun removeRule(packageName: String) {
        viewModelScope.launch {
            errorMessage.value = null
            try {
                withContext(Dispatchers.IO) {
                    monitoringRepository.remove(packageName)
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                errorMessage.value = e.message
                    ?: "Nie udało się usunąć reguły."
            }
        }
    }
}
