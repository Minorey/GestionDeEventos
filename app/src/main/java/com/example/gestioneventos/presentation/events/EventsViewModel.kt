package com.example.gestioneventos.presentation.events

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestioneventos.data.remote.model.Category
import com.example.gestioneventos.data.remote.model.Priority
import com.example.gestioneventos.data.repository.CategoryRespository
import com.example.gestioneventos.domain.model.Event
import com.example.gestioneventos.data.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(
    private val repository: EventRepository,
    private val categoryRepository: CategoryRespository) : ViewModel() {

    private val _events = MutableLiveData<List<Event>>()
    val events: LiveData<List<Event>> = _events
    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> = _categories

    private val _priorities = MutableLiveData<List<Priority>>()
    val priorities: LiveData<List<Priority>> = _priorities
    init {
        fetchEvents()
        fetchDropdownData()
    }

    fun fetchEvents() {
        repository.getEvents {
            _events.value = it
        }
    }
    fun fetchDropdownData() {
        viewModelScope.launch {
            _categories.value = categoryRepository.fetchCategories()
            _priorities.value = categoryRepository.fetchPriorities()
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
