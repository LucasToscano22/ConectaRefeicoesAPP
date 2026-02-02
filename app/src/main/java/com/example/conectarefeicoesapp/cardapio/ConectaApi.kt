package com.example.conectarefeicoesapp.cardapio

import retrofit2.http.GET

data class MenuDto(
    val id: Long,
    val nome: String,
    val categorias: List<CategoriaDto>
)

data class CategoriaDto(
    val id: Long,
    val nome: String,
    val limiteMaximoEscolhas: Int,
    val itens: List<ItemDto>
)

data class ItemDto(
    val id: Long,
    val nome: String,
    val categoria: String
)

interface ConectaApiService {
    @GET("conecta-restaurante/menu/active") 
    suspend fun getCardapioPadrao(): MenuDto
}

