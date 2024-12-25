package com.example.popisosnovnihsredstava

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.example.popisosnovnihsredstava.databinding.ActivityPregledStavkiBinding

class PregledStavkiActivity : AppCompatActivity(){
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityPregledStavkiBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPregledStavkiBinding.inflate(layoutInflater)
        setContentView(binding.root)
/*
        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_skeniranje)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        val items = listOf("Apple", "Banana", "Cherry", "Date", "Grape", "Lemon", "Mango")
        val myAdapter = PregledStavkiAdapter(items)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = myAdapter

        searchButton.setOnClickListener {
            val query = searchEditText.text.toString()
            myAdapter.filter(query)
        }*/
    }
}
