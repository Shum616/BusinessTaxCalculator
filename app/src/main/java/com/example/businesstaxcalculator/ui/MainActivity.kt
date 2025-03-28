package com.example.businesstaxcalculator.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.businesstaxcalculator.R
import com.example.businesstaxcalculator.databinding.ActivityMainBinding
import com.example.businesstaxcalculator.ui.home.HomeFragment
import com.example.businesstaxcalculator.ui.profile.ProfileFragment
import com.example.businesstaxcalculator.ui.settings.SettingsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> navigate(R.id.action_to_home)
                R.id.settings -> navigate(R.id.action_to_settings)
                R.id.profile -> navigate(R.id.action_to_profile)
            }
            true
        }
    }

    private fun navigate(action: Int) = findNavController(R.id.container).navigate(action)
}