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

    // Corrected to use getPedidosStream from the repository
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
        // Automatically sync with Firestore when the ViewModel is created
        viewModelScope.launch {
            repository.syncPedidosFromFirestore()
        }
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun cancelarPedido(pedido: Pedido) {
        // TODO: Implement cancellation logic in PedidoRepository
        // The original code called repository.deletePedido(pedido.id)
        // This method needs to be created in PedidoRepository and PedidoDao
        Log.d("MeusPedidosViewModel", "cancelarPedido called for: ${pedido.id}")
    }
}