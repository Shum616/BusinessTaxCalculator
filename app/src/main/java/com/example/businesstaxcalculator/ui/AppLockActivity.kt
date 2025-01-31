package com.example.businesstaxcalculator.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.businesstaxcalculator.R
import com.example.businesstaxcalculator.databinding.ActivityLockBinding

class AppLockActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLockBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLockBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("AppLockPrefs", MODE_PRIVATE)

        binding.btnUnlock.setOnClickListener {
            val enteredPassword = binding.etPassword.text.toString()
            val savedPassword =
                sharedPreferences.getString("password", "1234")

            if (enteredPassword == savedPassword) {
                finish()
            } else {
                Toast.makeText(this,
                    getString(R.string.invalid_password),
                    Toast.LENGTH_SHORT).show()
            }
        }
    }
}