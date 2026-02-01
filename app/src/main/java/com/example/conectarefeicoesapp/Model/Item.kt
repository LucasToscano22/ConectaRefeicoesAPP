package com.example.conectarefeicoesapp.Model

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "itens")
data class Item(
    @PrimaryKey val id: Long = 0L,
    var descricao: String = "",
    var categoriaOwnerId: Long = 0L)
