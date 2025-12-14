package com.example.conectarefeicoesapp.Model

data class Pedido(
    val id: Long,
    val id_requester: Long,
    val itens: List<Item>,
    val observacao: String
)
