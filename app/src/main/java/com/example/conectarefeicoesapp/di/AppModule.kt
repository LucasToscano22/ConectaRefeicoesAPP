package com.example.conectarefeicoesapp.di

import androidx.room.Room
import com.example.conectarefeicoesapp.cardapio.CardapioRepository
import com.example.conectarefeicoesapp.cardapio.ConectaApiService // Importe sua interface
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
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {

    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "conecta_refeicoes_db"
        )
            .build()
    }
    single { get<AppDatabase>().cardapioDao() }
    single { get<AppDatabase>().pedidoDao() }

    single { Firebase.firestore }

    single {
        Retrofit.Builder()
            .baseUrl("https://conecta-restaurante-api-e8ef9ee76c95.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single {
        get<Retrofit>().create(ConectaApiService::class.java)
    }


    single { CardapioRepository(get(), get()) }

    single { PedidoRepository(get(), get()) }

    viewModel { PedidoViewModel(get(), get()) }
    viewModel { MeusPedidosViewModel(get()) }
    viewModel { LoginViewModel() }
}