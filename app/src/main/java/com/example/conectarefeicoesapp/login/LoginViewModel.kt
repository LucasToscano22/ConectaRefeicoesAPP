package com.example.conectarefeicoesapp.login

import androidx.lifecycle.ViewModel
import com.example.conectarefeicoesapp.Model.Usuario
import com.example.conectarefeicoesapp.Model.UsuarioHolder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<LoginState>(LoginState.Idle)
    val uiState: StateFlow<LoginState> = _uiState.asStateFlow()

    fun validarLogin(email: String, pass: String) {
        if (email == "carlos.p@dpp.com" && pass == "senha123") {
            UsuarioHolder.currentUser = Usuario(id = "1", login = email)
            _uiState.value = LoginState.Success
        } else {
            _uiState.value = LoginState.Error("Login ou senha inválidos")
        }
    }

    fun resetState() {
        _uiState.value = LoginState.Idle
    }
}

sealed class LoginState {
    object Idle : LoginState()
    object Success : LoginState()
    data class Error(val message: String) : LoginState()
}