package com.example.gestioneventos.domain.repository

import com.example.gestioneventos.domain.model.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirebaseAuthRepository @Inject constructor():AuthRepository{
    //auth
    private val auth = FirebaseAuth.getInstance()
    override suspend fun login(email: String, password: String): AuthResult = withContext(Dispatchers.IO) {
        return@withContext try {
            auth.signInWithEmailAndPassword(email,password).await()
            AuthResult.Success
        }catch (e: Exception){//el error segun local
            AuthResult.Error(e.localizedMessage ?: "Error desconocido")
        }
    }

    override suspend fun register(email: String, password: String): AuthResult = withContext(Dispatchers.IO) {
        return@withContext try {
            auth.createUserWithEmailAndPassword(email,password).await()
            AuthResult.Success
        }catch (e:Exception){
            AuthResult.Error(e.localizedMessage ?: "Error desconocido")
        }
    }
    //cierre de sesion
    override fun logout() {
        auth.signOut()
    }
    //Hey el usuario esta logeado?
    override fun isUserLoggedln(): Boolean = auth.currentUser != null

}