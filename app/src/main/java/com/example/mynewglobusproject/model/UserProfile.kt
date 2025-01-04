package com.example.mynewglobusproject.model

data class UserProfile(
    val id: Int,
    val first_name: String,
    val last_name: String,
    val phone: String,
    val date_of_birth: String,
    val gender: String
)