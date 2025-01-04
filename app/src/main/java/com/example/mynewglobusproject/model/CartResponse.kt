package com.example.mynewglobusproject.model

data class CartResponse(
    val data: CartData
)
data class CartData(
    val cart: List<CartItem>
)


