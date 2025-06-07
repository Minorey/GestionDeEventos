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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.gestioneventos.domain.model.Event
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun EventForm(
    viewModel: EventsViewModel,
    editingEvent: Event? = null,
    onSave: () -> Unit
) {
    val context = LocalContext.current
    val scroll = rememberScrollState()

    var title by remember { mutableStateOf(editingEvent?.title ?: "") }
    var category by remember { mutableStateOf(editingEvent?.category ?: "") }

    var startTimeMillis by remember { mutableStateOf(editingEvent?.startTime ?: 0L) }
    var endTimeMillis by remember { mutableStateOf(editingEvent?.endTime ?: 0L) }

    var priority by remember { mutableStateOf(editingEvent?.priority ?: 0) }

    val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

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
                            Toast.makeText(context, "La hora de fin debe ser mayor a la de inicio", Toast.LENGTH_SHORT).show()
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
        Modifier
            .verticalScroll(scroll)
            .padding(8.dp)
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Título") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = category,
            onValueChange = { category = it },
            label = { Text("Categoría") },
            modifier = Modifier.fillMaxWidth()
        )

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
                .clickable { if (startTimeMillis == 0L) {
                    Toast.makeText(context, "Primero selecciona la hora de inicio", Toast.LENGTH_SHORT).show()
                } else {
                    pickEndDateTime(startTimeMillis) { endTimeMillis = it }
                } },
            enabled = false,
            readOnly = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Prioridad", style = MaterialTheme.typography.titleMedium)
        val priorities = listOf("Baja" to 0, "Media" to 1, "Alta" to 2)
        Column {
            priorities.forEach { (label, value) ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = priority == value,
                            onClick = { priority = value },
                            role = Role.RadioButton
                        )
                        .padding(vertical = 4.dp)
                ) {
                    RadioButton(
                        selected = priority == value,
                        onClick = { priority = value }
                    )
                    Text(label)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                if (title.isBlank() || category.isBlank() || startTimeMillis == 0L || endTimeMillis == 0L) {
                    Toast.makeText(context, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                val newEvent = Event(
                    id = editingEvent?.id ?: "",
                    title = title,
                    category = category,
                    startTime = startTimeMillis,
                    endTime = endTimeMillis,
                    priority = priority,
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
