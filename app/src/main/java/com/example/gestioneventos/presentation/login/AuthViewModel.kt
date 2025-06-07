package com.example.gestioneventos.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestioneventos.domain.model.AuthResult
import com.example.gestioneventos.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository):ViewModel() {
    private val _authState = MutableLiveData<AuthResult>()
    val authState:LiveData<AuthResult> = _authState
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthResult.Loading
            _authState.value = authRepository.login(email, password)
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthResult.Loading
            _authState.value = authRepository.register(email, password)
        }
    }
    fun isUserLoggedIn(): Boolean {
        return authRepository.isUserLoggedln()
    }
}