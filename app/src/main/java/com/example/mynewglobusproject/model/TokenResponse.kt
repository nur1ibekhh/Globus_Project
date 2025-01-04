package com.example.mynewglobusproject.model

data class TokenResponse(
    val data: TokenData?
)

data class TokenData(
    val token: Token?
)

data class Token(
    val access: String?
)