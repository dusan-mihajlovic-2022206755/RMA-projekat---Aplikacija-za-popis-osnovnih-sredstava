package com.example.popisos.skeniranje

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.popisosnovnihsredstava.R
import com.example.popisosnovnihsredstava.databinding.ActivitySkeniranjeBinding

class SkeniranjeActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivitySkeniranjeBinding
    val sharedViewModel: SkeniranjeSharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySkeniranjeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        sharedViewModel.popisID.value = intent.getIntExtra("id_popis", 0)

        val navController = findNavController(R.id.nav_host_fragment_content_skeniranje)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)


    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_skeniranje)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}