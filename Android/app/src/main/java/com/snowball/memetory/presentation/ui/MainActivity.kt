package com.snowball.memetory.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.snowball.memetory.R
import com.snowball.memetory.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigationView = binding.bottomNavigation
        val navController = findNavController(R.id.mainNavHost)

        bottomNavigationView.setupWithNavController(navController)



    }
}