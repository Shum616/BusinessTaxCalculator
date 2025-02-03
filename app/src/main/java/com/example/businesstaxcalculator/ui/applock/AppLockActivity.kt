package com.example.businesstaxcalculator.ui.applock

import android.content.SharedPreferences
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
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

        var isPasswordVisible = false

        binding.togglePasswordButton.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                binding.etPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                binding.togglePasswordButton.setImageResource(R.drawable.ic_eye)
            } else {
                binding.etPassword.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                binding.togglePasswordButton.setImageResource(R.drawable.ic_closed_eye)
            }
            binding.etPassword.setSelection(binding.etPassword.text.length)
        }


        binding.btnUnlock.setOnClickListener {
            val enteredPassword = binding.etPassword.text.toString()
            val savedPassword =
                sharedPreferences.getString("password", "1234")

            if (enteredPassword == savedPassword) {
                finish()
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.invalid_password),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
