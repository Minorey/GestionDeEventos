package com.example.gestioneventos.presentation.events

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gestioneventos.domain.model.Event
import com.example.gestioneventos.data.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(
    private val repository: EventRepository
) : ViewModel() {

    private val _events = MutableLiveData<List<Event>>()
    val events: LiveData<List<Event>> = _events

    init {
        fetchEvents()
    }

    fun fetchEvents() {
        repository.getEvents {
            _events.value = it
        }
    }

    fun addEvent(event: Event) {
        repository.addEvent(event) { success ->
            if (success) fetchEvents()
        }
    }

    fun updateEvent(event: Event) {
        repository.updateEvent(event) { success ->
            if (success) fetchEvents()
        }
    }

    fun deleteEvent(id: String) {
        repository.deleteEvent(id) { success ->
            if (success) fetchEvents()
        }
    }

    fun toggleCompletion(event: Event) {
        val updated = event.copy(completed = !event.completed)
        updateEvent(updated)
    }

    fun toggleAttendance(event: Event) {
        val updated = event.copy(attending = !event.attending)
        updateEvent(updated)
    }

}
