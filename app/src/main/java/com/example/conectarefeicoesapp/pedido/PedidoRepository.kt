package com.example.conectarefeicoesapp.pedido

import android.util.Log
import com.example.conectarefeicoesapp.Model.Pedido
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class PedidoRepository {
    private val db: FirebaseFirestore = Firebase.firestore
    private val pedidosCollection = db.collection("pedidos")
    fun getPedidos(): Flow<List<Pedido>> {
        return pedidosCollection.snapshots().map { snapshot ->
            snapshot.documents.mapNotNull { document ->
                try {
                    val pedido = document.toObject<Pedido>()
                    pedido?.id = document.id
                    pedido
                } catch (e: Exception) {
                    Log.e("PedidoRepository", "Erro ao converter documento para Pedido", e)
                    null
                }
            }
        }
    }

    suspend fun getPedido(pedidoId: String): Pedido? {
        return try {
            val document = pedidosCollection.document(pedidoId).get().await()
            val pedido = document.toObject<Pedido>()
            pedido?.id = document.id
            pedido
        } catch (e: Exception) {
            Log.e("PedidoRepository", "Erro ao buscar pedido $pedidoId", e)
            null
        }
    }

    suspend fun savePedido(pedido: Pedido) {
        try {
            if (pedido.id.isEmpty()) {
                val newDocRef = pedidosCollection.document()
                pedido.id = newDocRef.id
                newDocRef.set(pedido).await()
            } else {
                pedidosCollection.document(pedido.id).set(pedido).await()
            }
        } catch (e: Exception) {
            Log.e("PedidoRepository", "Erro ao salvar pedido", e)
        }
    }
    suspend fun deletePedido(pedidoId: String) {
        try {
            pedidosCollection.document(pedidoId).delete().await()
        } catch (e: Exception) {
            Log.e("PedidoRepository", "Erro ao deletar pedido $pedidoId", e)
        }
    }
}