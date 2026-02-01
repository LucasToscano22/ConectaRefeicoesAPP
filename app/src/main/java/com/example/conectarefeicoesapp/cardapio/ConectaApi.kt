package com.example.conectarefeicoesapp.cardapio

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

// 1. Modelos que representam EXATAMENTE o JSON da sua API
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

// 2. Interface que define a chamada (o endpoint que você escolheu)
interface ConectaApiService {
    @GET("conecta-restaurante/menu/active") // Pegando o cardápio fixo ID 67
    suspend fun getCardapioPadrao(): MenuDto
}

// 3. Objeto Singleton para criar a conexão
object RetrofitClient {
    private const val BASE_URL = "https://conecta-restaurante-api-e8ef9ee76c95.herokuapp.com/"

    val service: ConectaApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ConectaApiService::class.java)
    }
}