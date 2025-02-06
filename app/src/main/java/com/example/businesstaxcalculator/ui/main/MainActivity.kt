package com.example.businesstaxcalculator.ui.main

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import com.example.businesstaxcalculator.R
import com.example.businesstaxcalculator.databinding.ActivityMainBinding
import com.example.businesstaxcalculator.ui.applock.AppLockActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isAppLockEnabled = sharedPreferences.getBoolean("switch_app_lock", true) // За замовчуванням true

        if (isAppLockEnabled) {
            val intent = Intent(this, AppLockActivity::class.java)
            startActivity(intent)
        }

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