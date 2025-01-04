package com.example.mynewglobusproject.manager

object AuthManager {
    fun isSignedIn(): Boolean {
        // Foydalanuvchi tizimga kirgan yoki kirmaganligini tekshirish
        // Masalan, foydalanuvchi tokenini tekshirish yoki `SharedPreferences` orqali foydalanuvchi ma'lumotlarini olish
        return true // Yoki false, agar foydalanuvchi tizimga kirmagan bo'lsa
    }
}
