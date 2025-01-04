package com.example.mynewglobusproject.model

data class CategoryResponse(
    val success: Boolean,
    val errMessage: String?,
    val errorCode: String?,
    val data: DataWrapper
)

data class DataWrapper(
    val categories: List<Category>
)

data class Category(
    val id: Int,
    val name: String,
    val min_price: Int,
    val max_price: Int,
    val parent_category: String?
)
