package com.example.conectarefeicoesapp.login

import androidx.lifecycle.ViewModel
import com.example.conectarefeicoesapp.Model.Usuario
import com.example.conectarefeicoesapp.Model.UsuarioHolder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel : ViewModel() {

    // Estado para gerenciar o que a UI deve fazer (Navegar, Mostrar Erro ou Ficar Parada)
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    fun validarLogin(email: String, pass: String) {
        // Lógica de validação (Fake Login) movida da View para a ViewModel
        if (email == "carlos.p@dpp.com" && pass == "senha123") {
            UsuarioHolder.currentUser = Usuario(id = "1", login = email)
            _loginState.value = LoginState.Success
        } else {
            _loginState.value = LoginState.Error("Login ou senha inválidos")
        }
    }

    // Reseta o estado para evitar navegação duplicada se a tela for recomposta
    fun resetState() {
        _loginState.value = LoginState.Idle
    }
}

// Classes utilitárias para representar os estados da tela de Login
sealed class LoginState {
    object Idle : LoginState()   // Estado inicial/parado
    object Success : LoginState() // Login deu certo
    data class Error(val message: String) : LoginState() // Login falhou
}