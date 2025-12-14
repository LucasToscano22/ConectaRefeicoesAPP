package com.example.conectarefeicoesapp.pedido

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.conectarefeicoesapp.Model.Pedido
import com.example.conectarefeicoesapp.Model.mockPedidos
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MeusPedidosViewModel : ViewModel() {

    private val _pedidos = MutableStateFlow<List<Pedido>>(emptyList())
    val pedidos: StateFlow<List<Pedido>> = _pedidos.asStateFlow()

    init {
        carregarPedidos()
    }

    private fun carregarPedidos() {
        _pedidos.value = mockPedidos
    }

    fun cancelarPedido(pedido: Pedido) {
        mockPedidos.remove(pedido)
        carregarPedidos()
        Log.d("MeusPedidosViewModel", "Pedido cancelado: ${pedido.id}")
    }
}