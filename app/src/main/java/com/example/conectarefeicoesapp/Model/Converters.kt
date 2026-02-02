package com.example.conectarefeicoesapp.Model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromItemList(items: List<Item>): String {
        return gson.toJson(items)
    }

    @TypeConverter
    fun toItemList(itemsJson: String): List<Item> {
        val itemType = object : TypeToken<List<Item>>() {}.type
        return gson.fromJson(itemsJson, itemType)
    }
}