package com.example.gestioneventos.data.remote

import com.example.gestioneventos.data.remote.model.Category
import com.example.gestioneventos.data.remote.model.Priority
import com.example.gestioneventos.data.repository.CategoryRespository
import javax.inject.Inject

class CategoryService @Inject constructor(
    private val api: CategoryApi
) : CategoryRespository {
    override suspend fun fetchCategories(): List<Category> {
        return api.getCategories().categories
    }

    override suspend fun fetchPriorities(): List<Priority> {
        return api.getPriorities().priorities
    }
}