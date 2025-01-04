package com.example.mynewglobusproject.manager

import android.content.Context

import org.json.JSONArray
import org.json.JSONObject

object CartManager {
    private const val CART_PREFS = "cart_prefs"
    private const val CART_ITEMS = "cart_items"

    fun addToCart(context: Context, productName: String, productPrice: Int, productImageUrl: String) {
        val sharedPreferences = context.getSharedPreferences(CART_PREFS, Context.MODE_PRIVATE)
        val cartItemsJson = sharedPreferences.getString(CART_ITEMS, "[]")
        val cartItemsArray = JSONArray(cartItemsJson)

        // Create a new cart item JSON object
        val cartItem = JSONObject().apply {
            put("name", productName)
            put("price", productPrice)
            put("imageUrl", productImageUrl)
        }

        // Add the new item to the cart array
        cartItemsArray.put(cartItem)

        // Save the updated array back to SharedPreferences
        sharedPreferences.edit().putString(CART_ITEMS, cartItemsArray.toString()).apply()
    }

    fun getCartItems(context: Context): List<Map<String, Any>> {
        val sharedPreferences = context.getSharedPreferences(CART_PREFS, Context.MODE_PRIVATE)
        val cartItemsJson = sharedPreferences.getString(CART_ITEMS, "[]") ?: "[]"
        val cartItemsArray = JSONArray(cartItemsJson)

        val cartItemsList = mutableListOf<Map<String, Any>>()
        for (i in 0 until cartItemsArray.length()) {
            val item = cartItemsArray.getJSONObject(i)
            cartItemsList.add(
                mapOf(
                    "name" to item.getString("name"),
                    "price" to item.getInt("price"),
                    "imageUrl" to item.getString("imageUrl")
                )
            )
        }
        return cartItemsList
    }
}

