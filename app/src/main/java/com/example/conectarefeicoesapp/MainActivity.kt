package com.example.conectarefeicoesapp

import TelaHome
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.conectarefeicoesapp.pedido.TelaCadastrar
import com.example.conectarefeicoesapp.pedido.TelaMeusPedidos
import com.example.conectarefeicoesapp.ui.theme.ConectaRefeicoesAPPTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ConectaRefeicoesAPPTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        val navController = rememberNavController()
                        NavHost(navController = navController, startDestination = "login") {
                            composable("login") { TelaLogin(navController) }
                            composable("home") { TelaHome(navController) }
                            composable("meus_pedidos") { TelaMeusPedidos(navController) }
                            composable(
                                route = "cadastrar?pedidoId={pedidoId}",
                                arguments = listOf(navArgument("pedidoId") {
                                    type = NavType.StringType
                                    nullable = true
                                })
                            ) { backStackEntry ->
                                TelaCadastrar(
                                    navController = navController,
                                    pedidoId = backStackEntry.arguments?.getString("pedidoId")
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ConectaRefeicoesAPPTheme {
        val navController = rememberNavController()
        TelaLogin(navController)
    }
}
