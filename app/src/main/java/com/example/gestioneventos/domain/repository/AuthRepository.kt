package com.example.gestioneventos.domain.repository

import com.example.gestioneventos.domain.model.AuthResult

interface AuthRepository {
suspend fun login (email: String,password:String): AuthResult
suspend fun register (email: String,password: String): AuthResult

fun logout()
fun isUserLoggedln():Boolean

}