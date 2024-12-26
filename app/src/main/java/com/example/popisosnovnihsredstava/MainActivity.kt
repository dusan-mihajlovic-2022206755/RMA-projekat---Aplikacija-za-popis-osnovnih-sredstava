package com.example.popisosnovnihsredstava

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.example.popisosnovnihsredstava.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var chosenPopisID : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val sharedPreferences = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
        binding.textviewUsername.text = sharedPreferences.getString("username", "")

        val scanButton: Button = findViewById(R.id.button_skeniranje)
        val pregledStavkiPopisa : Button = findViewById(R.id.button_pregled_stavki)

        scanButton.setOnClickListener {
            val intent = Intent(this, SkeniranjeActivity::class.java)
            intent.putExtra("id_popis", chosenPopisID)
            startActivity(intent)
        }

        pregledStavkiPopisa.setOnClickListener {
            val intent = Intent(this, PregledStavkiActivity::class.java)
            intent.putExtra("id_popis", chosenPopisID)
            startActivity(intent)
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
                chosenPopisID = popisi[position].id
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