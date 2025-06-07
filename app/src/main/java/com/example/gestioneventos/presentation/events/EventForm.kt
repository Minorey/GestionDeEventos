package com.example.gestioneventos.presentation.events

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gestioneventos.domain.model.Event
@Composable
fun EventForm(
    viewModel: EventsViewModel,
    editingEvent: Event? = null,
    onSave: () -> Unit
) {
    var title by remember { mutableStateOf(editingEvent?.title ?: "") }
    var category by remember { mutableStateOf(editingEvent?.category ?: "") }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf(editingEvent?.priority ?: 0) }

    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        TextField(value = title, onValueChange = { title = it }, label = { Text("Título") })
        TextField(value = category, onValueChange = { category = it }, label = { Text("Categoría") })
        TextField(value = startTime, onValueChange = { startTime = it }, label = { Text("Inicio (epoch)") })
        TextField(value = endTime, onValueChange = { endTime = it }, label = { Text("Fin (epoch)") })

        Text("Prioridad:")
        Row {
            listOf("Baja" to 0, "Media" to 1, "Alta" to 2).forEach { (label, value) ->
                Row(
                    Modifier
                        .clickable { priority = value }
                        .padding(8.dp)
                ) {
                    RadioButton(selected = priority == value, onClick = { priority = value })
                    Text(label)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val event = Event(
                id = editingEvent?.id ?: "",
                title = title,
                category = category,
                startTime = startTime.toLongOrNull() ?: 0L,
                endTime = endTime.toLongOrNull() ?: 0L,
                priority = priority
            )
            if (editingEvent == null) {
                viewModel.addEvent(event)
            } else {
                viewModel.updateEvent(event)
            }
            onSave()
        }) {
            Text(if (editingEvent == null) "Guardar" else "Actualizar")
        }
    }
}

