package com.example.popisosnovnihsredstava

import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
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
        sharedViewModel.popisID.value = intent.getIntExtra("id_popis", 0)

        val navController = findNavController(R.id.nav_host_fragment_content_skeniranje)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_skeniranje)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}