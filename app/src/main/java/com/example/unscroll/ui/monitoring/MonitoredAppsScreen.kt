
package com.example.unscroll.ui.monitoring

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.unscroll.ui.theme.unscrollStatusColors
import org.koin.androidx.compose.koinViewModel

@Composable
fun MonitoredAppsScreen(
    onAddApp: () -> Unit,
    viewModel: MonitoringViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "Monitorowane aplikacje",
            style = MaterialTheme.typography.headlineSmall,
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = onAddApp) {
                Text("Dodaj aplikację")
            }

            OutlinedButton(
                onClick = viewModel::refresh,
                enabled = !state.isRefreshing,
            ) {
                Text("Odśwież")
            }
        }

        state.error?.let { message ->
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
            )
        }

        if (state.monitoredApps.isEmpty()) {
            Text(
                text = "Nie monitorujesz jeszcze żadnej aplikacji.",
                modifier = Modifier.weight(1f),
            )
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(
                    items = state.monitoredApps,
                    key = { it.rule.packageName },
                ) { app ->
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.large,
                        color = MaterialTheme.colorScheme.surfaceVariant,
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement =
                                Arrangement.spacedBy(8.dp),
                        ) {
                            Text(
                                text = app.appName,
                                style = MaterialTheme.typography.titleMedium,
                            )

                            Text(
                                text = "${app.usedMinutes} min z " +
                                    "${app.limitMinutes} min",
                                style = MaterialTheme.typography.bodyLarge,
                            )

                            LinearProgressIndicator(
                                progress = { app.fraction },
                                modifier = Modifier.fillMaxWidth(),
                                color = if (app.exceeded) {
                                    MaterialTheme.unscrollStatusColors.blocked
                                } else {
                                    MaterialTheme.colorScheme.primary
                                },
                                trackColor =
                                    MaterialTheme.colorScheme.surfaceContainerHighest,
                            )

                            Text(
                                text = when {
                                    app.exceeded ->
                                        "Limit przekroczony o " +
                                            "${app.usedMinutes - app.limitMinutes} min " +
                                            "(${app.percentage}%)"

                                    else ->
                                        "Pozostało ${app.remainingMinutes} min"
                                },
                                color = if (app.exceeded) {
                                    MaterialTheme.unscrollStatusColors.blocked
                                } else {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                },
                                style = MaterialTheme.typography.bodyMedium,
                            )

                            OutlinedButton(
                                onClick = {
                                    viewModel.removeRule(app.rule.packageName)
                                },
                            ) {
                                Text("Przestań monitorować")
                            }
                        }
                    }
                }
            }
        }
    }
}
