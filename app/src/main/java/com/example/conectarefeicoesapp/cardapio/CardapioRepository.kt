package com.example.conectarefeicoesapp.cardapio

import android.util.Log
import com.example.conectarefeicoesapp.Model.Cardapio
import com.example.conectarefeicoesapp.Model.Categoria
import com.example.conectarefeicoesapp.Model.Item
import com.example.conectarefeicoesapp.Model.Secao
import com.example.conectarefeicoesapp.data.local.CardapioDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class CardapioRepository(
    private val cardapioDao: CardapioDao,
    private val apiService: ConectaApiService
) {

    fun getCardapio(): Flow<Cardapio?> = flow {
        val categoriasLocais = cardapioDao.getCategorias()

        if (categoriasLocais.isNotEmpty()) {
            Log.d("CardapioRepository", "Carregando do ROOM (Cache)")

            val secoesLocais = categoriasLocais.map { cat ->
                Secao(
                    id = cat.id,
                    categoria = cat,
                    itens = cardapioDao.getItensPorCategoria(cat.id)
                )
            }
            emit(Cardapio(id = "cache_local", secoes = secoesLocais))
        }

        try {
            Log.d("CardapioRepository", "Buscando da API (Internet)...")

            val menuDto = apiService.getCardapioPadrao()

            val listaCategorias = mutableListOf<Categoria>()
            val listaItens = mutableListOf<Item>()

            val secoesConvertidas = menuDto.categorias.map { categoriaDto ->
                val novaCategoria = Categoria(
                    id = categoriaDto.id,
                    descricao = categoriaDto.nome,
                    restricaoNumerica = categoriaDto.limiteMaximoEscolhas
                )
                listaCategorias.add(novaCategoria)

                val itensDestaCategoria = categoriaDto.itens.map { itemDto ->
                    val novoItem = Item(
                        id = itemDto.id,
                        descricao = itemDto.nome,
                        categoriaOwnerId = novaCategoria.id
                    )
                    listaItens.add(novoItem)
                    novoItem
                }

                Secao(
                    id = categoriaDto.id,
                    categoria = novaCategoria,
                    itens = itensDestaCategoria
                )
            }

            cardapioDao.atualizarCardapio(listaCategorias, listaItens)
            Log.d("CardapioRepository", "Cache atualizado com sucesso!")

            val cardapioFinal = Cardapio(
                id = menuDto.id.toString(),
                secoes = secoesConvertidas
            )
            emit(cardapioFinal)

        } catch (e: Exception) {
            Log.e("CardapioRepository", "ERRO DE API: ${e.message}. Exibindo apenas cache.")
            if (categoriasLocais.isEmpty()) emit(null)
        }
    }.flowOn(Dispatchers.IO)
}