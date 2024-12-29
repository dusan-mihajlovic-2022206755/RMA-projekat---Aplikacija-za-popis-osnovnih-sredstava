package com.example.popisosnovnihsredstava.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.popisosnovnihsredstava.R
import com.example.popisosnovnihsredstava.entities.PopisStavka
import com.example.popisosnovnihsredstava.formatDateTimeToString
import sqlite.SQLitePopisHelper
import sqlite.SQLiteSifarnikHelper

class PopisStavkaAdapter(private val context: Context, private val stavke: MutableList<PopisStavka>) :
    RecyclerView.Adapter<PopisStavkaAdapter.PopisStavkaViewHolder>() {

    inner class PopisStavkaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val idArtikalTextView: TextView = itemView.findViewById(R.id.artikal_naziv)
        val kolicinaTextView: TextView = itemView.findViewById(R.id.kolicina)
        val vremeTextView: TextView = itemView.findViewById(R.id.vreme_popisivanja)
        val reduceKolicinaButton: Button = itemView.findViewById(R.id.reduce_kolicina_button)
        val increaseKolicinaButton: Button = itemView.findViewById(R.id.increase_kolicina_button)
        val deleteButton: Button = itemView.findViewById(R.id.delete_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopisStavkaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_popis_stavka, parent, false)
        return PopisStavkaViewHolder(view)
    }

    override fun onBindViewHolder(holder: PopisStavkaViewHolder, position: Int) {
        val stavka = stavke[position]
        val nazivArtikla = SQLiteSifarnikHelper(context).getArtikalById(stavka.idArtikal)?.naziv

        holder.idArtikalTextView.text = nazivArtikla
        holder.kolicinaTextView.text = "Količina: ${stavka.kolicina}"
        holder.vremeTextView.text = formatDateTimeToString(stavka.vremePopisivanja)

        holder.increaseKolicinaButton.setOnClickListener {
            val dbHelper = SQLitePopisHelper(context)
            dbHelper.incrementKolicinaById(stavka.id)
            stavka.kolicina += 1
            holder.kolicinaTextView.text = "Količina: ${stavka.kolicina}"
            notifyItemChanged(position)
        }

        holder.reduceKolicinaButton.setOnClickListener {
            if (stavka.kolicina > 1) {
                val dbHelper = SQLitePopisHelper(context)
                dbHelper.decrementKolicinaById(stavka.id)
                stavka.kolicina -= 1
                holder.kolicinaTextView.text = "Količina: ${stavka.kolicina}"
                notifyItemChanged(position)
            }
        }

        holder.deleteButton.setOnClickListener {
            stavke.removeAt(position)
            SQLitePopisHelper(context).deletePopisStavkaById(stavka.id)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, stavke.size)
            Toast.makeText(context, "Stavka izbrisana!", Toast.LENGTH_SHORT).show()
        }
    }


    override fun getItemCount(): Int = stavke.size
}

