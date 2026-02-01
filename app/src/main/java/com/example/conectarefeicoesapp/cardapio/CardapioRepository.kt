package com.example.conectarefeicoesapp.cardapio

import android.util.Log
import com.example.conectarefeicoesapp.Model.Cardapio
import com.example.conectarefeicoesapp.Model.Categoria
import com.example.conectarefeicoesapp.Model.Item
import com.example.conectarefeicoesapp.Model.Secao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CardapioRepository {

    fun getCardapio(): Flow<Cardapio?> = flow {
        try {
            Log.d("CardapioRepository", "Iniciando busca na API...")

            // 1. Chama a API
            val menuDto = RetrofitClient.service.getCardapioPadrao()

            Log.d("CardapioRepository", "API retornou cardápio: ${menuDto.nome}")

            // 2. Converte os dados
            val secoesConvertidas = menuDto.categorias.map { categoriaDto ->
                Secao(
                    id = categoriaDto.id,
                    categoria = Categoria(
                        id = categoriaDto.id,
                        descricao = categoriaDto.nome,
                        restricaoNumerica = categoriaDto.limiteMaximoEscolhas
                    ),
                    itens = categoriaDto.itens.map { itemDto ->
                        Item(
                            id = itemDto.id,
                            descricao = itemDto.nome
                            // Removi a linha 'selecionado = false' pois ela não existe no seu Modelo
                        )
                    }
                )
            }

            // 3. Monta o objeto final
            val cardapioFinal = Cardapio(
                id = menuDto.id.toString(),
                secoes = secoesConvertidas
            )

            emit(cardapioFinal)

        } catch (e: Exception) {
            Log.e("CardapioRepository", "ERRO DE API: ${e.message}")
            e.printStackTrace()
            emit(null)
        }
    }
}