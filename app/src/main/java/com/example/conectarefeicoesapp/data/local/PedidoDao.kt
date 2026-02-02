package com.example.conectarefeicoesapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.conectarefeicoesapp.Model.Pedido
import kotlinx.coroutines.flow.Flow

@Dao
interface PedidoDao {

    @Query("SELECT * FROM pedidos")
    fun getPedidos(): Flow<List<Pedido>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pedidos: List<Pedido>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pedido: Pedido)

}
