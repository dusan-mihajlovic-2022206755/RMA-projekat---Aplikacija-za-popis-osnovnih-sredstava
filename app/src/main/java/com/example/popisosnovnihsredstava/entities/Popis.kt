package com.example.popisosnovnihsredstava.entities

import java.time.LocalDate

data class Popis(
    val id: Int,                 // Maps to `id` (INT UNSIGNED)
    val datum: LocalDate?,       // Maps to `datum` (DATE)
    val napomena: String?,       // Maps to `napomena` (VARCHAR)
    val active: Boolean?,        // Maps to `active` (BIT)
)
