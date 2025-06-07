package com.example.gestioneventos.data.repository

import com.example.gestioneventos.data.remote.model.Category
import com.example.gestioneventos.data.remote.model.Priority

interface CategoryRespository {
    suspend fun fetchCategories(): List<Category>
    suspend fun fetchPriorities(): List<Priority>
}