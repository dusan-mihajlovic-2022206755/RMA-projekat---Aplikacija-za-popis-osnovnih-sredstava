package com.example.popisosnovnihsredstava.entities

import java.time.LocalDate
import java.time.LocalDateTime

data class PopisStavka(
    val id: Int,                  // Maps to `id` (INT UNSIGNED)
    val idPopis: Int,             // Maps to `id_popis` (INT UNSIGNED)
    val idArtikal: Int,           // Maps to `id_artikal` (INT UNSIGNED)
    val idLokacija: Int,          // Maps to `id_lokacija` (INT UNSIGNED)
    val kolicina: Int,            // Maps to `kolicina` (INT)
    val idUser: Int,              // Maps to `id_user` (INT UNSIGNED)
    val idRacunopolagac: Int,              // Maps to `id_user` (INT UNSIGNED)
    val vremePopisivanja: LocalDateTime, // Maps to `vreme_popisivanja` (DATETIME)
)