package com.example.gestioneventos.presentation.events

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gestioneventos.utils.formatEpochTime

@Composable
fun OptimizedEventsScreen(viewModel: EventsViewModel) {
    val optimizedEvents = remember { viewModel.getGreedyOptimizedEvents() }

    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        if (optimizedEvents.isEmpty()) {
            Text("No hay eventos optimizados disponibles.")
        } else {
            Text(
                text = "Eventos sin solapamientos y con mayor prioridad:",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn {
                items(optimizedEvents) { event ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = event.title,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text("Categoría: ${event.category}")
                            Text("Inicio: ${formatEpochTime(event.startTime)}")
                            Text("Fin: ${formatEpochTime(event.endTime)}")

                            val prioridadLabel = when (event.priority) {
                                0 -> "Baja"
                                1 -> "Media"
                                else -> "Alta"
                            }
                            Text("Prioridad: $prioridadLabel")
                        }
                    }
                }
            }
        }
    }
}


