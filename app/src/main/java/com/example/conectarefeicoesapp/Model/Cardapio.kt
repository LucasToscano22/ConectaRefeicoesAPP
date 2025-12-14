package com.example.conectarefeicoesapp.Model

data class Cardapio(val id: Long, var secoes: ArrayList<Secao>) {


    fun addSecao(secao: Secao) {
        secoes.add(secao)
    }

    fun rmSecao(secao: Secao) {
        secoes.remove(secao)
    }



}