package com.example.gestioneventos.presentation.events

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.gestioneventos.domain.model.Event
import com.example.gestioneventos.utils.formatEpochTime

@Composable
fun EventsScreen(modifier: Modifier, viewModel: EventsViewModel) {
    val allEvents by viewModel.events.observeAsState(emptyList())
    var showDialog by remember { mutableStateOf(false) }
    var selectedEvent by remember { mutableStateOf<Event?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredEvents = allEvents.filter {
        it.title.contains(searchQuery, ignoreCase = true) ||
                it.category.contains(searchQuery, ignoreCase = true)
    }



    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                selectedEvent = null // crear nuevo
                showDialog = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar evento")
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier
            .padding(paddingValues)
            .padding(16.dp)) {

            // Búsqueda
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Buscar evento...") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn {
                items(filteredEvents) { event ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable {
                                selectedEvent = event
                                showDialog = true
                            }.alpha(if (event.completed) 0.5f else 1f),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            val textDecoration = if (event.completed) TextDecoration.LineThrough else TextDecoration.None
                            Text("Título: ${event.title}", style = MaterialTheme.typography.titleMedium.copy(
                                textDecoration = textDecoration
                            ))
                            Text("Categoría: ${event.category}")
                            Text("Inicio: ${formatEpochTime(event.startTime)}")
                            Text("Fin: ${formatEpochTime(event.endTime)}")

                            val priorityColor = when (event.priority) {
                                0 -> Color.Gray
                                1 -> Color(0xFFFFA000)
                                else -> Color.Red
                            }
                            Text("Prioridad: ${when (event.priority) {
                                0 -> "Baja"
                                1 -> "Media"
                                else -> "Alta"
                            }}", color = priorityColor, fontWeight = FontWeight.Bold)



                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = event.attending,
                                    onCheckedChange = { viewModel.toggleAttendance(event) }
                                )
                                Text("¿Asistiré?")
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = event.completed,
                                    onCheckedChange = {
                                        viewModel.toggleCompletion(event)
                                    }
                                )
                                Text("¿Completado?")
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                IconButton(onClick = {
                                    viewModel.deleteEvent(event.id)
                                }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                                }
                            }
                        }
                    }

                }
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {},
                dismissButton = {},
                text = {
                    EventForm(
                        viewModel = viewModel,
                        editingEvent = selectedEvent,
                        onSave = {
                            showDialog = false
                            selectedEvent = null
                        }
                    )
                }
            )
        }
    }
}

