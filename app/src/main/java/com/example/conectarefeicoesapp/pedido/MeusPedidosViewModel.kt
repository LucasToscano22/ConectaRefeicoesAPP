package com.example.conectarefeicoesapp.pedido

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.conectarefeicoesapp.Model.Pedido
import com.example.conectarefeicoesapp.Model.mockPedidos
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class MeusPedidosViewModel : ViewModel() {

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _allPedidos = MutableStateFlow<List<Pedido>>(emptyList())

    val pedidos: StateFlow<List<Pedido>> = searchText
        .combine(_allPedidos) { text, pedidos ->
            if (text.isBlank()) {
                pedidos
            } else {
                pedidos.filter { pedido ->
                    pedido.itens.any { item ->
                        item.descricao.contains(text, ignoreCase = true)
                    }
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = _allPedidos.value
        )

    init {
        carregarPedidos()
    }

    private fun carregarPedidos() {
        _allPedidos.value = mockPedidos
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun cancelarPedido(pedido: Pedido) {
        mockPedidos.remove(pedido)
        carregarPedidos()
        Log.d("MeusPedidosViewModel", "Pedido cancelado: ${pedido.id}")
    }
}
