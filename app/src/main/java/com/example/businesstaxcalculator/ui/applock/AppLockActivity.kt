package com.example.businesstaxcalculator.ui.applock

import android.content.SharedPreferences
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.example.businesstaxcalculator.R
import com.example.businesstaxcalculator.databinding.ActivityLockBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.Executor
import javax.inject.Inject

@AndroidEntryPoint
class AppLockActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLockBinding
    @Inject
    lateinit var sharedPreferences: SharedPreferences
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLockBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            val savedPassword = sharedPreferences.getString("password", "1234")

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

        val isFingerprintUnlockEnabled = sharedPreferences.getBoolean("switch_fingerprint_unlock", false)
        binding.fingerprintButton.visibility = if (isFingerprintUnlockEnabled) View.VISIBLE else View.GONE

        val executor: Executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    finish()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    showPasswordOption()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    showPasswordOption()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Аутентифікація відбитком пальця")
            .setSubtitle("Підтвердьте свою особу")
            .setNegativeButtonText("Ввести пароль")
            .build()

        binding.fingerprintButton.setOnClickListener {
            if (isBiometricAvailable()) {
                biometricPrompt.authenticate(promptInfo)
            } else {
                showPasswordOption()
            }
        }
    }
    private fun isBiometricAvailable(): Boolean {
        val biometricManager = BiometricManager.from(this)
        return biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS
    }

    private fun showPasswordOption() {
        binding.etPassword.visibility = View.VISIBLE
        binding.btnUnlock.visibility = View.VISIBLE
    }
}
