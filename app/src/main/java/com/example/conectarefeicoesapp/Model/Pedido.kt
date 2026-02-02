package com.example.conectarefeicoesapp.Model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId

@Entity(tableName = "pedidos")
data class Pedido(
    @PrimaryKey @DocumentId var id: String = "",
    val id_requester: Long = 0L,
    val itens: List<Item> = emptyList<Item>(),
    val observacao: String = ""
)
