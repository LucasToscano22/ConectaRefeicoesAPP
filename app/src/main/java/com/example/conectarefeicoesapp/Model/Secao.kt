package com.example.conectarefeicoesapp.Model

data class Secao(
    val id:Long = 0L,
    val categoria: Categoria? = null,
    val itens: List<Item> = emptyList<Item>()
)
