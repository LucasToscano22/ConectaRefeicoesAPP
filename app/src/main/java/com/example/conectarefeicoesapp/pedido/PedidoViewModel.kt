package com.example.conectarefeicoesapp.pedido

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.conectarefeicoesapp.Model.Cardapio
import com.example.conectarefeicoesapp.Model.Item
import com.example.conectarefeicoesapp.Model.Pedido
import com.example.conectarefeicoesapp.Model.Secao
import com.example.conectarefeicoesapp.Model.UsuarioHolder
import com.example.conectarefeicoesapp.cardapio.CardapioRepository
import com.example.conectarefeicoesapp.data.repository.PedidoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PedidoUiState(
    val pedidoId: String? = null,
    val selectedItems: List<Item> = emptyList(),
    val observacao: String = "",
    val isNewPedido: Boolean = true,
    val cardapio: Cardapio? = null,
    val isLoading: Boolean = true
)

class PedidoViewModel(
    private val pedidoRepository: PedidoRepository,
    private val cardapioRepository: CardapioRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PedidoUiState())
    val uiState: StateFlow<PedidoUiState> = _uiState.asStateFlow()

    init {
        loadCardapio()
    }

    private fun loadCardapio() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            cardapioRepository.getCardapio().collect { cardapioFromRepo ->
                _uiState.update {
                    it.copy(
                        cardapio = cardapioFromRepo,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun loadPedido(pedidoId: String?) {
        if (pedidoId != null) {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true) }
                // Assuming getPedido is implemented in PedidoRepository to fetch a single pedido by id
                val pedido = pedidoRepository.getPedidosStream().firstOrNull()?.find { it.id == pedidoId }

                if (pedido != null) {
                    _uiState.update {
                        it.copy(
                            pedidoId = pedido.id,
                            selectedItems = pedido.itens,
                            observacao = pedido.observacao,
                            isNewPedido = false,
                            isLoading = false
                        )
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false, isNewPedido = true, pedidoId = null, selectedItems = emptyList(), observacao = "") }
                }
            }
        } else {
            _uiState.update { it.copy(isNewPedido = true, pedidoId = null, selectedItems = emptyList(), observacao = "") }
        }
    }

    fun onItemClick(item: Item, secao: Secao) {
        val cardapio = _uiState.value.cardapio ?: return
        val currentSelected = _uiState.value.selectedItems.toMutableList()
        val itemsInCategory = currentSelected.filter { selected ->
            cardapio.secoes.any { s -> s.categoria == secao.categoria && s.itens.contains(selected) }
        }

        if (currentSelected.contains(item)) {
            currentSelected.remove(item)
        } else {
            if (secao.categoria != null && itemsInCategory.size < secao.categoria.restricaoNumerica) {
                currentSelected.add(item)
            }
        }
        _uiState.update { it.copy(selectedItems = currentSelected) }
    }

    fun onObservacaoChange(newObservacao: String) {
        _uiState.update { it.copy(observacao = newObservacao) }
    }

    fun savePedido() {
        viewModelScope.launch {
            val currentPedidoState = _uiState.value
            val userId = UsuarioHolder.currentUser?.id?.toLongOrNull() ?: 0L

            val pedidoToSave = Pedido(
                id = currentPedidoState.pedidoId ?: "",
                id_requester = userId,
                itens = currentPedidoState.selectedItems,
                observacao = currentPedidoState.observacao
            )

            try {
                pedidoRepository.addPedido(pedidoToSave)
                Log.d("PedidoViewModel", "Pedido salvo com sucesso: $pedidoToSave")
            } catch (e: Exception) {
                Log.e("PedidoViewModel", "Erro ao salvar pedido", e)
            }
        }
    }
}
