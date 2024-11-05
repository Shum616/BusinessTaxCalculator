package com.example.businesstaxcalculator

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.businesstaxcalculator.databinding.ActivityMainBinding
import com.example.businesstaxcalculator.validatot.IncomeValidator
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.calculateBttn.setOnClickListener {
            val inputTxt = binding.txtField.text.toString()
            val validRes = IncomeValidator(inputTxt).validate()

            binding.txtInputLt.error =
                if (!validRes.isSuccess) "Invalid income input" else null
                //TODO
        }

        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)!!
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.settings -> {
                    loadFragment(SettingsFragment())
                    true
                }
                R.id.profile -> {
                    loadFragment(ProfileFragment())
                    true
                }

                else -> false ///wtf have i done???
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {

        if (fragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment)
            transaction.commit()
        }
    }
}