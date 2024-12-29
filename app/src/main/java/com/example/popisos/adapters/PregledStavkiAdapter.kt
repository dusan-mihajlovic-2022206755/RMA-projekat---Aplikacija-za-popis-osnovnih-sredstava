package com.example.popisosnovnihsredstava.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.popisos.models.PopisStavkaModelPregledStavki
import com.example.popisosnovnihsredstava.R
import com.example.popisosnovnihsredstava.formatDateTimeToString
import sqlite.SQLiteSifarnikHelper

class PregledStavkiAdapter(private val context: Context, private val originalStavke: List<PopisStavkaModelPregledStavki>) :
    RecyclerView.Adapter<PregledStavkiAdapter.PopisStavkaViewHolder>() {

    private var filteredStavke: List<PopisStavkaModelPregledStavki> = originalStavke

    inner class PopisStavkaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val idArtikalTextView: TextView = itemView.findViewById(R.id.artikal_naziv)
        val kolicinaTextView: TextView = itemView.findViewById(R.id.kolicina)
        val vremeTextView: TextView = itemView.findViewById(R.id.vreme_popisivanja)
        val lokacijaTextView: TextView = itemView.findViewById(R.id.lokacijaTextView)
        val racunopolagacTextView: TextView = itemView.findViewById(R.id.racunopolagacTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopisStavkaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_popis_stavka_pregled, parent, false)
        return PopisStavkaViewHolder(view)
    }

    override fun onBindViewHolder(holder: PopisStavkaViewHolder, position: Int) {
        val stavka = filteredStavke[position]

        holder.idArtikalTextView.text = stavka.artikal
        holder.kolicinaTextView.text = "Količina: ${stavka.popisStavka.kolicina}"
        holder.vremeTextView.text = formatDateTimeToString(stavka.popisStavka.vremePopisivanja)
        holder.lokacijaTextView.text = stavka.lokacija
        holder.racunopolagacTextView.text = stavka.racunopolagac
    }

    override fun getItemCount(): Int = filteredStavke.size

    fun filter(query: String) {
        val sifarnikHelper = SQLiteSifarnikHelper(context)

        filteredStavke = if (query.isEmpty()) {
            originalStavke
        } else {
            originalStavke.filter { stavka ->
                val artikal = sifarnikHelper.getArtikalById(stavka.popisStavka.idArtikal)
                val artikalName = artikal?.naziv
                val artikalSifra = artikal?.sifra
                val racunopolagac = sifarnikHelper.getRacunopolagacById(stavka.popisStavka.idRacunopolagac)
                val racunopolagacIme = racunopolagac?.ime
                val racunopolagacPrezime = racunopolagac?.prezime
                val racunopolagacSifra = racunopolagac?.sifra

                val lokacija = sifarnikHelper.getLokacijaById(stavka.popisStavka.idLokacija)
                val lokacijaIme= lokacija?.naziv
                val lokacijaSifra = lokacija?.sifra

                artikalName?.contains(query, ignoreCase = true) == true ||
                        artikalSifra?.contains(query, ignoreCase = true) == true ||
                        racunopolagacIme?.contains(query, ignoreCase = true) == true ||
                        racunopolagacPrezime?.contains(query, ignoreCase = true) == true ||
                        racunopolagacSifra?.contains(query, ignoreCase = true) == true ||
                        lokacijaIme?.contains(query, ignoreCase = true) == true ||
                        lokacijaSifra?.contains(query, ignoreCase = true) == true

            }
        }
        notifyDataSetChanged()
    }
}
