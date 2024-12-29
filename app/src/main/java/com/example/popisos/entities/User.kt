package com.example.popisosnovnihsredstava.entities

data class User(
    val id: Int,
    val ime: String = "",
    val prezime: String = "",
    val username: String = "",
    val email: String = "",
    val password: String
)
