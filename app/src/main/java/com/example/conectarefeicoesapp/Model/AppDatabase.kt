package com.example.conectarefeicoesapp.Model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.conectarefeicoesapp.cardapio.CardapioDao

@Database(entities = [Categoria::class, Item::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun cardapioDao(): CardapioDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "conecta_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}