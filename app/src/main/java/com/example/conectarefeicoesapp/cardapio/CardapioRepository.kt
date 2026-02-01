package com.example.conectarefeicoesapp.cardapio

import android.content.Context
import android.util.Log
import com.example.conectarefeicoesapp.Model.AppDatabase
import com.example.conectarefeicoesapp.Model.Cardapio
import com.example.conectarefeicoesapp.Model.Categoria
import com.example.conectarefeicoesapp.Model.Item
import com.example.conectarefeicoesapp.Model.Secao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

// Adicionei o Context no construtor para pegar o banco
class CardapioRepository(context: Context) {

    private val cardapioDao = AppDatabase.getDatabase(context).cardapioDao()

    fun getCardapio(): Flow<Cardapio?> = flow {
        // 1. EMITIR DADOS DO CACHE (ROOM) IMEDIATAMENTE
        // Tenta buscar do banco local
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

        // 2. BUSCAR DA API E ATUALIZAR O CACHE
        try {
            Log.d("CardapioRepository", "Buscando da API (Internet)...")
            val menuDto = RetrofitClient.service.getCardapioPadrao()

            // Prepara as listas para salvar no Room
            val listaCategorias = mutableListOf<Categoria>()
            val listaItens = mutableListOf<Item>()

            val secoesConvertidas = menuDto.categorias.map { categoriaDto ->
                // Cria objeto Categoria
                val novaCategoria = Categoria(
                    id = categoriaDto.id,
                    descricao = categoriaDto.nome,
                    restricaoNumerica = categoriaDto.limiteMaximoEscolhas
                )
                listaCategorias.add(novaCategoria)

                // Cria objetos Item e vincula à categoria (Flattening)
                val itensDestaCategoria = categoriaDto.itens.map { itemDto ->
                    val novoItem = Item(
                        id = itemDto.id,
                        descricao = itemDto.nome,
                        categoriaOwnerId = novaCategoria.id // VINCULO IMPORTANTE
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

            // SALVA NO ROOM (Atualiza o cache)
            cardapioDao.atualizarCardapio(listaCategorias, listaItens)
            Log.d("CardapioRepository", "Cache atualizado com sucesso!")

            // Emite o dado novo que veio da API
            val cardapioFinal = Cardapio(
                id = menuDto.id.toString(),
                secoes = secoesConvertidas
            )
            emit(cardapioFinal)

        } catch (e: Exception) {
            Log.e("CardapioRepository", "ERRO DE API: ${e.message}. Exibindo apenas cache (se houver).")
            // Se der erro na API, o usuário continua vendo o cache emitido no passo 1
            if (categoriasLocais.isEmpty()) emit(null)
        }
    }
}