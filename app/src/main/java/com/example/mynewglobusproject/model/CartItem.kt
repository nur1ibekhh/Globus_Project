package com.example.mynewglobusproject.model

data class CartItem(
    val id: Int,
    val product: Product,
    var quantity: Int,
    val cart_items: Int
)
