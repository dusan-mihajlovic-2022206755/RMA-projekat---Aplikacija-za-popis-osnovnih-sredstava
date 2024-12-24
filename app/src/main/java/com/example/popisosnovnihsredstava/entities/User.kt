package com.example.popisosnovnihsredstava.entities

data class User(
    val id: Int,              // Corresponds to `id` in the table
    val ime: String = "",     // Corresponds to `ime`
    val prezime: String = "", // Corresponds to `prezime`
    val username: String = "",// Corresponds to `username`
    val email: String = "",   // Corresponds to `email`
)
