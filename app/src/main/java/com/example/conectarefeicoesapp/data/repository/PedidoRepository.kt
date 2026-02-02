package com.example.conectarefeicoesapp.data.repository

import android.util.Log
import com.example.conectarefeicoesapp.Model.Pedido
import com.example.conectarefeicoesapp.data.local.PedidoDao
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class PedidoRepository(
    private val pedidoDao: PedidoDao,
    private val firestore: FirebaseFirestore
) {

    // A UI vai consumir este Flow. Ele vem direto do Room (rápido e offline)
    fun getPedidosStream(): Flow<List<Pedido>> {
        return pedidoDao.getPedidos()
    }

    // Função para sincronizar dados do Firestore para o Room
    suspend fun syncPedidosFromFirestore() {
        try {
            Log.d("PedidoRepository", "Sincronizando pedidos do Firestore...")
            val snapshot = firestore.collection("pedidos").get().await()
            val pedidosDaNuvem = snapshot.toObjects(Pedido::class.java)
            pedidoDao.insertAll(pedidosDaNuvem) // Salva os dados novos no Room
            Log.d("PedidoRepository", "Sincronização de pedidos concluída com sucesso.")
        } catch (e: Exception) {
            Log.e("PedidoRepository", "Erro ao sincronizar pedidos do Firestore: ${e.message}")
        }
    }

    suspend fun addPedido(pedido: Pedido) {
        try {
            Log.d("PedidoRepository", "Adicionando novo pedido ao Firestore...")
            val documentReference = firestore.collection("pedidos").add(pedido).await()
            val pedidoComId = pedido.copy(id = documentReference.id)
            pedidoDao.insert(pedidoComId) // Salva no Room com o ID correto
            Log.d("PedidoRepository", "Pedido adicionado com sucesso. ID: ${documentReference.id}")
        } catch (e: Exception) {
            Log.e("PedidoRepository", "Erro ao adicionar pedido: ${e.message}")
        }
    }
}