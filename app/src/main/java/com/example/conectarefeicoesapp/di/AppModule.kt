package com.example.conectarefeicoesapp.di

import androidx.room.Room
import com.example.conectarefeicoesapp.cardapio.CardapioRepository
import com.example.conectarefeicoesapp.data.local.AppDatabase
import com.example.conectarefeicoesapp.data.repository.PedidoRepository
import com.example.conectarefeicoesapp.login.LoginViewModel
import com.example.conectarefeicoesapp.meuspedidos.MeusPedidosViewModel
import com.example.conectarefeicoesapp.pedido.PedidoViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "conecta_refeicoes_db"
        )
            .build()
    }

    single { Firebase.firestore }

    single { get<AppDatabase>().cardapioDao() }
    single { get<AppDatabase>().pedidoDao() }

    single { CardapioRepository(get()) }
    single { PedidoRepository(get(), get()) }

    viewModel { PedidoViewModel(get(), get()) }
    viewModel { MeusPedidosViewModel(get()) }
    viewModel { LoginViewModel() }


}