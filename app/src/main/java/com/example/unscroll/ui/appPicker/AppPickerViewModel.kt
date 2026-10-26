package com.example.unscroll.ui.appPicker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unscroll.data.model.UsageStatItem
import com.example.unscroll.data.repo.MonitoringRepository
import com.example.unscroll.data.repo.UsageStatsRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class AppPickerUiState(
    val apps: List<UsageStatItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val needsUsageAccess: Boolean = false,
)

class AppPickerViewModel(
    private val usageStatsRepository: UsageStatsRepository,
    private val monitoringRepository: MonitoringRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppPickerUiState())
    val uiState: StateFlow<AppPickerUiState> = _uiState.asStateFlow()

    init {
        loadApps()
    }

    fun loadApps() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                val (hasAccess, apps) = withContext(Dispatchers.IO) {
                    val hasAccess = usageStatsRepository.hasUsageAccess()
                    if (!hasAccess) {
                        return@withContext false to emptyList()
                    }

                    val monitoredPackages = monitoringRepository
                        .observeAll()
                        .first()
                        .mapTo(mutableSetOf()) { it.packageName }

                    true to usageStatsRepository.getTodayUsage()
                        .filterNot { it.packageName in monitoredPackages }
                }

                if (!hasAccess) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Włącz dostęp do statystyk użycia w ustawieniach Androida.",
                        needsUsageAccess = true,
                    )
                    return@launch
                }

                _uiState.value = AppPickerUiState(
                    apps = apps,
                    isLoading = false,
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Nie udało się pobrać listy aplikacji.",
                    needsUsageAccess = false,
                )
            }
        }
    }
}
