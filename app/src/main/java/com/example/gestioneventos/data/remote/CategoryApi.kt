package com.example.gestioneventos.data.remote

import com.example.gestioneventos.data.remote.model.CategoryResponse
import com.example.gestioneventos.data.remote.model.PriorityResponse
import retrofit2.http.GET

interface CategoryApi {
    @GET("categories")
    suspend fun getCategories(): CategoryResponse

    @GET("priorities")
    suspend fun getPriorities(): PriorityResponse


}