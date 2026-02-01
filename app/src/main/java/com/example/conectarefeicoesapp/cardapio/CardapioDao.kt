package com.example.conectarefeicoesapp.cardapio


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.conectarefeicoesapp.Model.Categoria
import com.example.conectarefeicoesapp.Model.Item

@Dao
interface CardapioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategorias(categorias: List<Categoria>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItens(itens: List<Item>)

    @Query("DELETE FROM categorias")
    suspend fun clearCategorias()

    @Query("DELETE FROM itens")
    suspend fun clearItens()

    @Query("SELECT * FROM categorias")
    suspend fun getCategorias(): List<Categoria>

    @Query("SELECT * FROM itens WHERE categoriaOwnerId = :categoriaId")
    suspend fun getItensPorCategoria(categoriaId: Long): List<Item>

    @Transaction
    suspend fun atualizarCardapio(categorias: List<Categoria>, itens: List<Item>) {
        clearCategorias()
        clearItens()
        insertCategorias(categorias)
        insertItens(itens)
    }
}