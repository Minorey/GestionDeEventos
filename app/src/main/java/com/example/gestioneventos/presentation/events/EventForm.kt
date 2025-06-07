package com.example.gestioneventos.presentation.events

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.gestioneventos.domain.model.Event
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventForm(
    viewModel: EventsViewModel,
    editingEvent: Event? = null,
    onSave: () -> Unit
) {
    val context = LocalContext.current
    val categories by viewModel.categories.observeAsState(emptyList())
    val priorities by viewModel.priorities.observeAsState(emptyList())

    var title by remember { mutableStateOf(editingEvent?.title ?: "") }
    var category by remember { mutableStateOf(editingEvent?.category ?: "") }
    var priorityId by remember { mutableStateOf(editingEvent?.priority ?: 0) }

    var expandedCategory by remember { mutableStateOf(false) }
    var expandedPriority by remember { mutableStateOf(false) }

    var startTimeMillis by remember { mutableStateOf(editingEvent?.startTime ?: 0L) }
    var endTimeMillis by remember { mutableStateOf(editingEvent?.endTime ?: 0L) }

    val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    val scroll = rememberScrollState()

    fun pickStartDateTime(onResult: (Long) -> Unit) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            context,
            { _, year, month, day ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, day)

                TimePickerDialog(
                    context,
                    { _, hour, minute ->
                        calendar.set(Calendar.HOUR_OF_DAY, hour)
                        calendar.set(Calendar.MINUTE, minute)
                        onResult(calendar.timeInMillis)
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                ).show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    fun pickEndDateTime(minStartTime: Long, onResult: (Long) -> Unit) {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = minStartTime + 1 * 60 * 1000 // como mínimo 1 min después
        DatePickerDialog(
            context,
            { _, year, month, day ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, day)

                TimePickerDialog(
                    context,
                    { _, hour, minute ->
                        calendar.set(Calendar.HOUR_OF_DAY, hour)
                        calendar.set(Calendar.MINUTE, minute)
                        val selected = calendar.timeInMillis
                        if (selected > minStartTime) {
                            onResult(selected)
                        } else {
                            Toast.makeText(
                                context,
                                "La hora de fin debe ser mayor a la de inicio",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                ).show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    Column(
        modifier = Modifier
            .verticalScroll(scroll)
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Título") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = expandedCategory,
            onExpandedChange = { expandedCategory = !expandedCategory }
        ) {
            OutlinedTextField(
                value = category,
                onValueChange = {},
                readOnly = true,
                label = { Text("Categoría") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategory) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expandedCategory,
                onDismissRequest = { expandedCategory = false }
            ) {
                categories.forEach {
                    DropdownMenuItem(
                        text = { Text(it.name) },
                        onClick = {
                            category = it.name
                            expandedCategory = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = expandedPriority,
            onExpandedChange = { expandedPriority = !expandedPriority }
        ) {
            val selectedLabel =
                priorities.find { it.id == priorityId }?.name ?: "Selecciona prioridad"
            OutlinedTextField(
                value = selectedLabel,
                onValueChange = {},
                readOnly = true,
                label = { Text("Prioridad") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPriority) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expandedPriority,
                onDismissRequest = { expandedPriority = false }
            ) {
                priorities.forEach {
                    DropdownMenuItem(
                        text = { Text(it.name) },
                        onClick = {
                            priorityId = it.id
                            expandedPriority = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = if (startTimeMillis > 0) formatter.format(Date(startTimeMillis)) else "",
            onValueChange = {},
            label = { Text("Inicio") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { pickStartDateTime { startTimeMillis = it } },
            enabled = false,
            readOnly = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = if (endTimeMillis > 0) formatter.format(Date(endTimeMillis)) else "",
            onValueChange = {},
            label = { Text("Fin") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    if (startTimeMillis == 0L) {
                        Toast
                            .makeText(
                                context,
                                "Primero selecciona la hora de inicio",
                                Toast.LENGTH_SHORT
                            )
                            .show()
                    } else {
                        pickEndDateTime(startTimeMillis) { endTimeMillis = it }
                    }
                },
            enabled = false,
            readOnly = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                if (title.isBlank() || category.isBlank() || startTimeMillis == 0L || endTimeMillis == 0L) {
                    Toast.makeText(context, "Todos los campos son obligatorios", Toast.LENGTH_SHORT)
                        .show()
                    return@Button
                }

                val newEvent = Event(
                    id = editingEvent?.id ?: "",
                    title = title,
                    category = category,
                    startTime = startTimeMillis,
                    endTime = endTimeMillis,
                    priority = priorityId,
                    completed = editingEvent?.completed ?: false,
                    attending = editingEvent?.attending ?: false
                )

                if (editingEvent == null) viewModel.addEvent(newEvent)
                else viewModel.updateEvent(newEvent)

                onSave()
            }
        ) {
            Text(if (editingEvent == null) "Crear Evento" else "Actualizar Evento")
        }
    }
}

