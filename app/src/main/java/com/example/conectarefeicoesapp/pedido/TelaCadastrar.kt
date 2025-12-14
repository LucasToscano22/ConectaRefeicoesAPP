package com.example.conectarefeicoesapp.pedido

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.conectarefeicoesapp.Model.Cardapio
import com.example.conectarefeicoesapp.Model.Item
import com.example.conectarefeicoesapp.Model.Secao
import com.example.conectarefeicoesapp.Model.mockCardapio

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaCadastrar(
    navController: NavController,
    pedidoId: String?,
    viewModel: PedidoViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(pedidoId) {
        viewModel.loadPedido(pedidoId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (uiState.isNewPedido) "Realizar Pedido" else "Atualizar Pedido") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Cardápio",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
            HorizontalDivider(
                modifier = Modifier.padding(bottom = 32.dp),
                thickness = 1.dp,
                color = Color.Black
            )
            MountMenu(
                menu = mockCardapio,
                selectedItems = uiState.selectedItems,
                onItemClick = viewModel::onItemClick
            )

            Text(
                text = "Observação",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp)
            )
            OutlinedTextField(
                value = uiState.observacao,
                onValueChange = { viewModel.onObservacaoChange(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                placeholder = { Text("Ex: Tirar a cebola") }
            )
            Button(
                onClick = { 
                    viewModel.savePedido()
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text(if (uiState.isNewPedido) "Fazer Pedido" else "Salvar Alterações")
            }
        }
    }
}

@Composable
fun MenuItemCard(
    item: Item,
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .padding(4.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            elevation = CardDefaults.cardElevation(4.dp),
            colors = if (isSelected) {
                CardDefaults.cardColors(containerColor = Color(0xFFFDD738))
            } else {
                CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0))
            },
            border = if (isSelected) BorderStroke(2.dp, Color.Black) else null
        ) {
            Box(
                modifier = Modifier.size(100.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isSelected) Icons.Default.Check else Icons.Default.Close,
                    contentDescription = item.descricao,
                    modifier = Modifier.size(60.dp),
                    tint = Color.Black
                )
            }
        }
        Text(
            text = item.descricao,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 4.dp)
                .width(100.dp)
        )
    }
}

@Composable
fun MountMenu(
    menu: Cardapio,
    selectedItems: List<Item>,
    onItemClick: (Item, Secao) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        for (secao in menu.secoes) {
            val selectedCount = selectedItems.count { item -> secao.itens.contains(item) }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
            ) {
                Text(
                    text = secao.categoria.descricao,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "Selecionados: $selectedCount de ${secao.categoria.restricaoNumerica}",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                secao.itens.chunked(3).forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowItems.forEach { item ->
                            MenuItemCard(
                                item = item,
                                modifier = Modifier.weight(1f),
                                isSelected = selectedItems.contains(item),
                                onClick = { onItemClick(item, secao) }
                            )
                        }
                        if (rowItems.size < 3) {
                            for (i in 0 until (3 - rowItems.size)) {
                                Spacer(modifier = Modifier.weight(1f).padding(4.dp))
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TelaCadastrarPreview() {
    TelaCadastrar(navController = rememberNavController(), pedidoId = null)
}

@Preview(showBackground = true)
@Composable
fun TelaCadastrarEditPreview() {
    TelaCadastrar(navController = rememberNavController(), pedidoId = "1")
}