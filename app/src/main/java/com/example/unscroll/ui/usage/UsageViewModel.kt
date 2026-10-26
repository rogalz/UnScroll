package com.example.unscroll.ui.usage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unscroll.data.model.UsageStatItem
import com.example.unscroll.data.repo.UsageStatsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class UsageUiState(
    val hasPermission: Boolean = false,
    val isLoading: Boolean = false,
    val items: List<UsageStatItem> = emptyList(),
    val error: String? = null,
)

class UsageViewModel(
    private val repository: UsageStatsRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UsageUiState())
    val uiState = _uiState.asStateFlow()

    fun refresh() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, error = null)
            }

            try {
                val hasPermission = withContext(Dispatchers.IO) {
                    repository.hasUsageAccess()
                }

                val items = if (hasPermission) {
                    withContext(Dispatchers.IO) {
                        repository.getTodayUsage()
                    }
                } else {
                    emptyList()
                }

                _uiState.value = UsageUiState(
                    hasPermission = hasPermission,
                    isLoading = false,
                    items = items,
                )
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Nie udało się odczytać statystyk.",
                    )
                }
            }
        }
    }
}