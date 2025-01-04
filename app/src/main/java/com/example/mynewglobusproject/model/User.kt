package com.example.mynewglobusproject.model

data class User(
    val id: Int,
    val first_name: String,
    val last_name: String,
    val password: String,
    val phone: String,
    val date_of_birth: String, // YYYY-MM-DD formatida
    val gender: String // "male" yoki "female"
)


