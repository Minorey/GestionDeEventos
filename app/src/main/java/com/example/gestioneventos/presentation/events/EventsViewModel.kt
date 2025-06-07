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

    fun getOptimizedEventSchedule(): List<Event> {
        val eventList = _events.value ?: return emptyList()


        val sortedEvents = eventList.sortedBy { it.endTime }


        val n = sortedEvents.size
        val p = IntArray(n) { -1 }

        for (i in 1 until n) {
            for (j in i - 1 downTo 0) {
                if (sortedEvents[j].endTime <= sortedEvents[i].startTime) {
                    p[i] = j
                    break
                }
            }
        }


        val dp = IntArray(n)
        val selected = mutableListOf<Int>()

        for (i in 0 until n) {
            val include = sortedEvents[i].priority + if (p[i] != -1) dp[p[i]] else 0
            val exclude = if (i > 0) dp[i - 1] else 0

            dp[i] = maxOf(include, exclude)
        }


        fun findSolution(i: Int): List<Int> {
            if (i < 0) return emptyList()
            val include = sortedEvents[i].priority + if (p[i] != -1) dp[p[i]] else 0
            val exclude = if (i > 0) dp[i - 1] else 0

            return if (include > exclude) {
                listOf(i) + if (p[i] != -1) findSolution(p[i]) else emptyList()
            } else {
                findSolution(i - 1)
            }
        }

        val selectedIndices = findSolution(n - 1)
        return selectedIndices.map { sortedEvents[it] }
    }

    fun getGreedyOptimizedEvents(): List<Event> {
        val events = _events.value ?: return emptyList()

        // Ordenar por hora de finalización
        val sorted = events.sortedWith(compareBy<Event> { it.endTime }.thenByDescending { it.priority })

        val selected = mutableListOf<Event>()
        var lastEndTime = 0L

        for (event in sorted) {
            if (event.startTime >= lastEndTime) {
                selected.add(event)
                lastEndTime = event.endTime
            }
        }

        return selected
    }


}
