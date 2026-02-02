package com.example.conectarefeicoesapp.meuspedidos

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.conectarefeicoesapp.Model.Pedido
import com.example.conectarefeicoesapp.data.repository.PedidoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// Corrected constructor to receive the repository via injection
class MeusPedidosViewModel(private val repository: PedidoRepository) : ViewModel() {

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _allPedidos: StateFlow<List<Pedido>> = repository.getPedidosStream()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

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
        viewModelScope.launch {
            repository.syncPedidosFromFirestore()
        }
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun cancelarPedido(pedido: Pedido) {
        Log.d("MeusPedidosViewModel", "cancelarPedido called for: ${pedido.id}")
    }
}