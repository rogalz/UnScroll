package com.example.unscroll.ui.usage

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import java.text.DateFormat
import java.util.Date

@Composable
fun UsageScreen(
    modifier: Modifier= Modifier,
    viewModel: UsageViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .then(modifier),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "Unscroll — Usage Inspector",
            style = MaterialTheme.typography.headlineSmall,
        )

        Button(
            onClick = {
                context.startActivity(
                    Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                )
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Otwórz ustawienia dostępu")
        }

        Button(
            onClick = viewModel::refresh,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Odśwież statystyki")
        }

        when {
            state.isLoading -> {
                CircularProgressIndicator()
            }

            !state.hasPermission -> {
                Text(
                    "Brak dostępu do statystyk użycia. " +
                            "Włącz dostęp dla Unscroll w ustawieniach " +
                            "i wróć do aplikacji.",
                    color = MaterialTheme.colorScheme.error,
                )
            }

            state.error != null -> {
                Text(
                    text = state.error.orEmpty(),
                    color = MaterialTheme.colorScheme.error,
                )
            }

            state.items.isEmpty() -> {
                Text("Brak statystyk użycia dla dzisiejszego dnia.")
            }

            else -> {
                Text(
                    text = "Aplikacje: ${state.items.size}",
                    style = MaterialTheme.typography.titleMedium,
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(
                        items = state.items,
                        key = { it.packageName },
                    ) { item ->
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.medium,
                            color = MaterialTheme.colorScheme.surfaceVariant,
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                verticalArrangement =
                                    Arrangement.spacedBy(4.dp),
                            ) {
                                Text(
                                    text = item.appName,
                                    style = MaterialTheme.typography.titleMedium,
                                )

                                Text(
                                    text = item.packageName,
                                    style = MaterialTheme.typography.bodySmall,
                                )

                                Text(
                                    text = "Czas na pierwszym planie: " +
                                            formatDuration(
                                                item.totalTimeInForeground
                                            ),
                                )

                                Text(
                                    text = "Ostatnie użycie: " +
                                            formatTimestamp(item.lastTimeUsed),
                                    style = MaterialTheme.typography.bodySmall,
                                )

                                HorizontalDivider()

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                ) {
                                    Text(
                                        text = "Początek rekordu: " +
                                                formatTimestamp(item.firstTimeStamp),
                                        modifier = Modifier.weight(1f),
                                        style = MaterialTheme.typography.labelSmall,
                                    )
                                }

                                Text(
                                    text = "Koniec rekordu: " +
                                            formatTimestamp(item.lastTimeStamp),
                                    style = MaterialTheme.typography.labelSmall,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun formatDuration(milliseconds: Long): String {
    val totalSeconds = milliseconds / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    return if (hours > 0) {
        "${hours}h ${minutes}min"
    } else {
        "${minutes}min ${seconds}s"
    }
}

private fun formatTimestamp(timestamp: Long): String {
    if (timestamp <= 0L) return "Brak danych"

    return DateFormat.getDateTimeInstance(
        DateFormat.SHORT,
        DateFormat.SHORT,
    ).format(Date(timestamp))
}
