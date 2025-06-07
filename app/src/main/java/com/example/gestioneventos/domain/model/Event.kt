package com.example.gestioneventos.domain.model

data class Event(
    val id: String = "",
    val title: String = "",
    val startTime: Long = 0L,
    val endTime: Long = 0L,
    val category: String = "",
    val priority: Int = 0,
    val isCompleted: Boolean = false
)
