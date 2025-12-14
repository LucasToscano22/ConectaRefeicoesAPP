package com.example.conectarefeicoesapp.Model

import com.google.firebase.firestore.DocumentId


data class Pedido(
    @DocumentId var id: String = "",
    val id_requester: Long = 0L,
    val itens: List<Item> = emptyList<Item>(),
    val observacao: String = ""
)
