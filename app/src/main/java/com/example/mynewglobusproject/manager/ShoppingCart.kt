package com.example.mynewglobusproject.manager

import com.example.mynewglobusproject.model.Product

object ShoppingCart {
    private val cartItems = mutableListOf<Product>()

    fun addToCart(product: Product) {
        cartItems.add(product)
    }

    fun getCartItems(): List<Product> {
        return cartItems
    }
}
