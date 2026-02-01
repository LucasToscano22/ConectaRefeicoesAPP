package com.example.conectarefeicoesapp.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categorias")
data class Categoria(
    @PrimaryKey val id: Long = 0L,
    val descricao: String = "",
    val restricaoNumerica: Int = 0
)
