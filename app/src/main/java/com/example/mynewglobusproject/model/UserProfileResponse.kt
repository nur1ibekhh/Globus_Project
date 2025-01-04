package com.example.mynewglobusproject.model

data class UserProfileResponse(
    val data: Data
) {
    data class Data(
        val user: User
    ) {
        data class User(
            val id: Int,
            val first_name: String,
            val last_name: String,
            val phone: String,
            val date_of_birth: String,
            val gender: String,
            val is_active: Boolean
        )
    }
}
