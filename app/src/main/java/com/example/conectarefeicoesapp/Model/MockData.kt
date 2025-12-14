package com.example.conectarefeicoesapp.Model

val mockItems = listOf(
    Item(1, "Arroz"),
    Item(2, "Feijão"),
    Item(3, "Macarrão"),
    Item(4, "Bife"),
    Item(5, "Frango Grelhado"),
    Item(6, "Salada de Alface e Tomate"),
    Item(7, "Purê de Batata"),
    Item(8, "Farofa")
)

val mockCategorias = listOf(
    Categoria(1, "Grãos", 2),
    Categoria(2, "Proteínas", 1),
    Categoria(3, "Acompanhamentos", 3)
)

val mockSecoes = arrayListOf(
    Secao(1, mockCategorias[0], arrayListOf(mockItems[0], mockItems[1], mockItems[2])),
    Secao(2, mockCategorias[1], arrayListOf(mockItems[3], mockItems[4])),
    Secao(3, mockCategorias[2], arrayListOf(mockItems[5], mockItems[6], mockItems[7]))
)

val mockCardapio = Cardapio(1, mockSecoes)

val mockPedidos = arrayListOf(
    Pedido(
        id = 1L,
        id_requester = 123L,
        itens = listOf(mockItems[3], mockItems[0], mockItems[6]), 
        observacao = "Bife bem passado, por favor."
    ),
    Pedido(
        id = 2L,
        id_requester = 123L,
        itens = listOf(mockItems[1], mockItems[2], mockItems[4]),
        observacao = "Macarrão bem passado, por favor."
    )
)
