package com.example.popisosnovnihsredstava

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.popisos.models.PopisStavkaModelPregledStavki
import com.example.popisosnovnihsredstava.adapters.PregledStavkiAdapter
import com.example.popisosnovnihsredstava.databinding.ActivityPregledStavkiBinding
import sqlite.SQLitePopisHelper
import sqlite.SQLiteSifarnikHelper

class PregledStavkiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPregledStavkiBinding
    private lateinit var adapter: PregledStavkiAdapter
    private var stavke: List<PopisStavkaModelPregledStavki> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPregledStavkiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val dbHelper = SQLitePopisHelper(this)
        val sifarnik = SQLiteSifarnikHelper(this)

        stavke = dbHelper.getPopisStavkeByIdPopis(intent.getIntExtra("id_popis", 0))
            .map { x ->
                val (racunopolagac, lokacija, artikal) = sifarnik.getSifarnikData(x.idArtikal, x.idLokacija, x.idRacunopolagac)
                PopisStavkaModelPregledStavki(x, racunopolagac.toString(), lokacija.toString(), artikal.toString())
            }

        adapter = PregledStavkiAdapter(this, stavke)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        binding.searchButton.setOnClickListener {
            val query = binding.searchEditText.text.toString()
            adapter.filter(query)
        }
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
