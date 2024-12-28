package com.example.popisosnovnihsredstava

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.example.popisosnovnihsredstava.databinding.ActivityMainBinding
import com.example.popisosnovnihsredstava.helpers.SQLiteSifarnikHelper
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var izabraniPopisID : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val sharedPreferences = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
        binding.textviewUsername.text = sharedPreferences.getString("username", "")

        binding.buttonSkeniranje.setOnClickListener {
            if (izabraniPopisID == 0){
                Toast.makeText(this, "Nije izabrana nijedna popisna lista!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, SkeniranjeActivity::class.java)
            intent.putExtra("id_popis", izabraniPopisID)
            startActivity(intent)
        }

        binding.buttonPregledStavki.setOnClickListener {
            if (izabraniPopisID == 0){
                Toast.makeText(this, "Nije izabrana nijedna popisna lista!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, PregledStavkiActivity::class.java)
            intent.putExtra("id_popis", izabraniPopisID)
            startActivity(intent)
        }

        binding.buttonPreuzmisifarnik.setOnClickListener {
            deleteDatabase("sifarnik.db")
            SQLiteSifarnikHelper(this).popuniTestnimPodacima()
            Toast.makeText(this, "Šifarnik uspešno preuzet!", Toast.LENGTH_SHORT).show()

        }
        binding.buttonSinhronizuj.setOnClickListener {
            deleteDatabase("bazaPopis.db")
            SQLitePopisHelper(this).unesiTestnePopise()
            FillPopisSpinner()
            Toast.makeText(this, "Baza uspešno sinhronizovana!", Toast.LENGTH_SHORT).show()
        }
        FillPopisSpinner()
    }

    private fun FillPopisSpinner() {
        val dbHelper = SQLitePopisHelper(this)

        val popisi = dbHelper.getAllPopis()
        val popisNames = popisi.map { "${it.id} - ${it.datum} - ${it.napomena}" }
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            popisNames
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinner.adapter = adapter

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                izabraniPopisID = popisi[position].id
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //nista
            }

        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
    }


//top level funkcija - može da se pozove sa bilo kog mesta...bolje nego static
fun formatirajDateTime(valueFromDBRow: String): LocalDateTime {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    val vremePopisivanjaStr = valueFromDBRow
    val normalizedStr = vremePopisivanjaStr.split(".")[0]
    val vremePopisivanja = LocalDateTime.parse(normalizedStr, formatter)!!
    return vremePopisivanja
}

