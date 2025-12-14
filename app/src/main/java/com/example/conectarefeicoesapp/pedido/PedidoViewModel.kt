package com.example.conectarefeicoesapp.pedido

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.conectarefeicoesapp.Model.Item
import com.example.conectarefeicoesapp.Model.Pedido
import com.example.conectarefeicoesapp.Model.Secao
import com.example.conectarefeicoesapp.Model.mockCardapio
import com.example.conectarefeicoesapp.Model.mockPedido
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class PedidoUiState(
    val pedidoId: Long? = null,
    val selectedItems: List<Item> = emptyList(),
    val observacao: String = "",
    val isNewPedido: Boolean = true
)

class PedidoViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(PedidoUiState())
    val uiState: StateFlow<PedidoUiState> = _uiState.asStateFlow()

    fun loadPedido(pedidoId: String?) {
        if (pedidoId != null) {
            // In a real app, you would fetch this from a repository/database
            if (pedidoId == mockPedido.id.toString()) {
                _uiState.update {
                    it.copy(
                        pedidoId = mockPedido.id,
                        selectedItems = mockPedido.itens,
                        observacao = mockPedido.observacao,
                        isNewPedido = false
                    )
                }
            }
        } else {
            _uiState.value = PedidoUiState()
        }
    }

    fun onItemClick(item: Item, secao: Secao) {
        val currentSelected = _uiState.value.selectedItems.toMutableList()
        val itemsInCategory = currentSelected.filter { selected ->
            mockCardapio.secoes.any { s -> s.categoria == secao.categoria && s.itens.contains(selected) }
        }

        if (currentSelected.contains(item)) {
            currentSelected.remove(item)
        } else {
            if (itemsInCategory.size < secao.categoria.restricaoNumerica) {
                currentSelected.add(item)
            }
        }
        _uiState.update { it.copy(selectedItems = currentSelected) }
    }

    fun onObservacaoChange(newObservacao: String) {
        _uiState.update { it.copy(observacao = newObservacao) }
    }

    fun savePedido() {
        val currentPedidoState = _uiState.value
        val pedidoToSave = Pedido(
            id = currentPedidoState.pedidoId ?: 0L, // Use existing ID or 0 for new
            id_requester = 123L, // Placeholder
            itens = currentPedidoState.selectedItems,
            observacao = currentPedidoState.observacao
        )

        if (currentPedidoState.isNewPedido) {
            Log.d("PedidoViewModel", "Salvando NOVO pedido: $pedidoToSave")
        } else {
            Log.d("PedidoViewModel", "Atualizando pedido EXISTENTE: $pedidoToSave")
        }
    }
}