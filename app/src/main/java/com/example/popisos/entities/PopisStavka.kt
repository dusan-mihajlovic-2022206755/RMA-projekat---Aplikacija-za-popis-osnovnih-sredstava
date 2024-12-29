package com.example.popisosnovnihsredstava.entities

import java.time.LocalDateTime

data class PopisStavka(
    val id: Int,
    val idPopis: Int,
    val idArtikal: Int,
    val idLokacija: Int,
    var kolicina: Int,
    val idUser: Int,
    val idRacunopolagac: Int,
    val vremePopisivanja: LocalDateTime,
)