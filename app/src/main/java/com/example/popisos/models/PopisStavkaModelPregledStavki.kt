package com.example.popisos.models

import com.example.popisosnovnihsredstava.entities.PopisStavka

class PopisStavkaModelPregledStavki(
    val popisStavka: PopisStavka,
    val racunopolagac: String = "",
    val lokacija: String = "",
    val artikal: String = ""
)