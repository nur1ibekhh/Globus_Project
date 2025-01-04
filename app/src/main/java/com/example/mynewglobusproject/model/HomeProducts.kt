package com.example.mynewglobusproject.model

data class HomeProducts(
    val `data`: Data,
    val errMessage: Any,
    val errorCode: Any,
    val success: Boolean
)

data class Data(
    val items: List<Product>,
    val next: String,
    val previous: Any,
    val total_records: Int
)

