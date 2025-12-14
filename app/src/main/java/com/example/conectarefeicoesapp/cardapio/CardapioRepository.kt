package com.example.conectarefeicoesapp.cardapio

import android.util.Log
import com.example.conectarefeicoesapp.Model.Cardapio
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CardapioRepository {

    private val db = Firebase.firestore
    private val cardapiosCollection = db.collection("cardapios")

    fun getCardapio(): Flow<Cardapio?> {
        return cardapiosCollection.limit(1).snapshots().map { snapshot ->
            Log.w(snapshot.toString(), "Menu document found: ${snapshot.documents.first().id}")
            if (snapshot.isEmpty) {
                Log.w("CardapioRepository", "No menu document found in 'cardapios' collection.")
                null
            } else {
                val document = snapshot.documents.first()
                Log.d("CardapioRepository", "Menu document found: ${document.id}")
                try {
                    document.toObject<Cardapio>()
                } catch (e: Exception) {
                    Log.e("CardapioRepository", "Error converting menu document to Cardapio object.", e)
                    null
                }
            }
        }
    }
}
