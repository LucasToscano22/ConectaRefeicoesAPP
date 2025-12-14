package com.example.conectarefeicoesapp.pedido

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.conectarefeicoesapp.Model.Pedido

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaMeusPedidos(navController: NavController, viewModel: MeusPedidosViewModel = viewModel()) {
    val pedidos by viewModel.pedidos.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Meus Pedidos") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(pedidos) { pedido ->
                    PedidoCard(
                        pedido = pedido,
                        onEdit = { navController.navigate("cadastrar?pedidoId=${pedido.id}") },
                        onCancel = { viewModel.cancelarPedido(pedido) }
                    )
                }
            }
        }
    }
}

@Composable
fun PedidoCard(pedido: Pedido, onEdit: () -> Unit, onCancel: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp)
                )
                Column(modifier = Modifier.padding(start = 16.dp).weight(1f)) {
                    Text(text = "Pedido #${pedido.id}", fontWeight = FontWeight.Bold)
                    Text(text = pedido.itens.joinToString { it.descricao }, fontSize = 14.sp, color = Color.Gray)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(imageVector = Icons.Default.Info, contentDescription = "Status", tint = Color.Red)
                    Text(text = "Reportar Erro", fontSize = 12.sp, color = Color.Red)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedButton(onClick = onEdit, modifier = Modifier.weight(1f)) {
                    Text("Editar Pedido")
                }
                Button(onClick = onCancel, modifier = Modifier.weight(1f)) {
                    Text("Cancelar Pedido")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TelaMeusPedidosPreview() {
    TelaMeusPedidos(navController = rememberNavController())
}
