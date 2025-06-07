package com.example.gestioneventos.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatEpochTime(epochMillis: Long): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(epochMillis))
}

