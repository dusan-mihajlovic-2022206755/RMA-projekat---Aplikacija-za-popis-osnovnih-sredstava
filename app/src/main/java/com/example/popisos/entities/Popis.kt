package com.example.popisosnovnihsredstava.entities

import java.time.LocalDate

data class Popis(
    val id: Int,
    val datum: LocalDate?,
    val napomena: String?,
    val active: Boolean?,
)
