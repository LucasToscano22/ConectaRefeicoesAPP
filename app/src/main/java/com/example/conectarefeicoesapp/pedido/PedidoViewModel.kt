package com.example.conectarefeicoesapp.pedido

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.conectarefeicoesapp.Model.Item
import com.example.conectarefeicoesapp.Model.Pedido
import com.example.conectarefeicoesapp.Model.Secao
import com.example.conectarefeicoesapp.Model.mockCardapio
import com.example.conectarefeicoesapp.Model.mockPedidos
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
        val id = pedidoId?.toLongOrNull()
        if (id != null) {
            val pedidoToLoad = mockPedidos.find { it.id == id }
            if (pedidoToLoad != null) {
                _uiState.update {
                    it.copy(
                        pedidoId = pedidoToLoad.id,
                        selectedItems = pedidoToLoad.itens,
                        observacao = pedidoToLoad.observacao,
                        isNewPedido = false
                    )
                }
            } else {
                 _uiState.value = PedidoUiState()
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

        if (currentPedidoState.isNewPedido) {
            val newId = (mockPedidos.maxOfOrNull { it.id } ?: 0L) + 1
            val newPedido = Pedido(
                id = newId,
                id_requester = 123L,
                itens = currentPedidoState.selectedItems,
                observacao = currentPedidoState.observacao
            )
            mockPedidos.add(newPedido)
            Log.d("PedidoViewModel", "Salvando NOVO pedido: $newPedido")
        } else {
            val index = mockPedidos.indexOfFirst { it.id == currentPedidoState.pedidoId }
            if (index != -1) {
                val updatedPedido = mockPedidos[index].copy(
                    itens = currentPedidoState.selectedItems,
                    observacao = currentPedidoState.observacao
                )
                mockPedidos[index] = updatedPedido
                Log.d("PedidoViewModel", "Atualizando pedido EXISTENTE: $updatedPedido")
            } else {
                Log.e("PedidoViewModel", "Erro: Pedido para atualização não encontrado com id: ${currentPedidoState.pedidoId}")
            }
        }
    }
}
