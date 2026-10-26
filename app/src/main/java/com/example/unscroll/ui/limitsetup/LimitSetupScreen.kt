
package com.example.unscroll.ui.limitsetup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.unscroll.ui.monitoring.MonitoringViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun LimitSetupScreen(
    packageName: String,
    appName: String,
    onSaved: () -> Unit,
    onCancel: () -> Unit,
    viewModel: MonitoringViewModel = koinViewModel(),
) {
    val saveState by viewModel.saveUiState.collectAsStateWithLifecycle()
    var limitMinutes by remember {
        mutableFloatStateOf(30f)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "Monitoruj aplikację",
            style = MaterialTheme.typography.headlineSmall,
        )

        Text(
            text = appName,
            style = MaterialTheme.typography.titleLarge,
        )

        Text(
            text = "Ile czasu dziennie chcesz jej poświęcać?",
            style = MaterialTheme.typography.bodyLarge,
        )

        Text(
            text = "${limitMinutes.toInt()} min",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
        )

        Slider(
            value = limitMinutes,
            onValueChange = { limitMinutes = it },
            valueRange = 5f..240f,
            steps = 46,
            modifier = Modifier.fillMaxWidth(),
            enabled = !saveState.isSaving,
        )

        Text(
            text = "Limit odnawia się każdego dnia o północy.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        saveState.errorMessage?.let { message ->
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        Button(
            onClick = {
                viewModel.saveRule(
                    packageName = packageName,
                    dailyLimitMinutes = limitMinutes.toInt(),
                    onSaved = onSaved,
                )
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !saveState.isSaving,
        ) {
            Text(if (saveState.isSaving) "Zapisywanie…" else "Zapisz limit")
        }

        OutlinedButton(
            onClick = onCancel,
            modifier = Modifier.fillMaxWidth(),
            enabled = !saveState.isSaving,
        ) {
            Text("Anuluj")
        }
    }
}
