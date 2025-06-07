package com.example.gestioneventos.di

import com.example.gestioneventos.data.remote.CategoryApi
import com.example.gestioneventos.data.remote.CategoryService
import com.example.gestioneventos.data.repository.CategoryRespository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://b4906993-1160-4bca-9ccf-4fd11d6c57db.mock.pstmn.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideCategoryApi(retrofit: Retrofit): CategoryApi {
        return retrofit.create(CategoryApi::class.java)
    }

    @Provides
    fun provideCategoryRepository(api: CategoryApi): CategoryRespository {
        return CategoryService(api)
    }
}