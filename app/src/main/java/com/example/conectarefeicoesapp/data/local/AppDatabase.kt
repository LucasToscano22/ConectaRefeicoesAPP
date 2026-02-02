package com.example.conectarefeicoesapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.conectarefeicoesapp.Model.Categoria
import com.example.conectarefeicoesapp.Model.Converters
import com.example.conectarefeicoesapp.Model.Item
import com.example.conectarefeicoesapp.Model.Pedido

@Database(
    entities = [Categoria::class, Item::class, Pedido::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun cardapioDao(): CardapioDao
    abstract fun pedidoDao(): PedidoDao
}