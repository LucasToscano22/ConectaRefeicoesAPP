package com.example.conectarefeicoesapp.Model

import com.google.firebase.firestore.DocumentId

data class Cardapio(
    @DocumentId val id: String = "",
    val secoes: List<Secao> = emptyList<Secao>()
)
