package com.example.businesstaxcalculator.ui.main.settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.businesstaxcalculator.R
import com.example.businesstaxcalculator.data.UserSelection
import com.example.businesstaxcalculator.databinding.FragmentSettingsBinding
import com.example.businesstaxcalculator.ui.main.SharedIncomeViewModel
import com.example.businesstaxcalculator.ui.base.BaseTabFragment
import kotlinx.coroutines.launch
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.getValue

@AndroidEntryPoint
class SettingsFragment : BaseTabFragment() {

    private lateinit var binding: FragmentSettingsBinding
    override val viewModel: SharedIncomeViewModel by viewModels()

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        val biometricAvailable = isBiometricSupported(requireContext())

        binding.switchFingerprintUnlock.visibility =
            if (biometricAvailable) View.VISIBLE else View.GONE

        val isAppLockEnabled = sharedPreferences.getBoolean("switch_app_lock", false)
        val isFingerprintEnabled = sharedPreferences.getBoolean("switch_fingerprint_unlock", false)

        binding.switchAppLock.isChecked = isAppLockEnabled
        binding.switchFingerprintUnlock.isChecked = isFingerprintEnabled

        binding.switchFingerprintUnlock.isEnabled = isAppLockEnabled

        binding.switchAppLock.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("switch_app_lock", isChecked).apply()
            binding.switchFingerprintUnlock.isEnabled = isChecked
        }

        binding.switchFingerprintUnlock.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("switch_fingerprint_unlock", isChecked).apply()
        }

        val currencies = listOf(
            getString(R.string.usd),
            getString(R.string.eur),
            getString(R.string.uah)
        )

        val arrayAdapter = createAdapter(requireContext(), currencies)

        binding.materialSpinner.setAdapter(arrayAdapter)

        val userSelection = UserSelection()

        binding.materialSpinner.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = currencies[position]
            userSelection.spinnerSelection = selectedItem
            viewModel.dataStorageSave(userSelection)
        }

        binding.getRateBtn.setOnClickListener {
            val validResDollar = viewModel.incomeValidation(binding.editDollar.text.toString())
            val validResEuro = viewModel.incomeValidation(binding.editEuro.text.toString())

            if (validResDollar != null) userSelection.dollarInput = validResDollar
            else Toast.makeText(
                requireContext(),
                getString(R.string.enter_value_again),
                Toast.LENGTH_SHORT
            ).show()

            if (validResEuro != null) userSelection.euroInput = validResEuro
            else Toast.makeText(
                requireContext(),
                getString(R.string.enter_value_again),
                Toast.LENGTH_SHORT
            ).show()

            viewModel.dataStorageSave(userSelection)
        }

        observeRates()
        viewModel.fetchRates()
        binding.btnSavePassword.setOnClickListener {
            val newPassword = binding.etNewPassword.text.toString()
            val validPassword = viewModel.validatePassword(newPassword)

            val confirmPassword = binding.etConfirmPassword.text.toString()
            val validConfirmPassword = viewModel.validatePassword(confirmPassword)

            if (validPassword.isSuccess && validConfirmPassword.isSuccess && newPassword == confirmPassword) {
                viewModel.savePasswords(newPassword, sharedPreferences)
                Toast.makeText(
                    requireContext(),
                    getString(R.string.password_changed_successfully),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.error_in_entering_password),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        return binding.root
    }


    private fun isBiometricSupported(context: Context): Boolean {
        val biometricManager = BiometricManager.from(context)
        return biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS
    }

    private fun createAdapter(context: Context, stringList: List<String>): ArrayAdapter<String> {
        return ArrayAdapter(
            context,
            android.R.layout.simple_dropdown_item_1line,
            stringList
        )
    }

    private fun observeRates() {
        lifecycleScope.launch {
            viewModel.usdRate.collect { rate ->
                binding.currencyRateUsd.text =
                    if (rate != null) context?.getString(R.string.usd_template, rate.saleRate)
                    else getString(R.string.usd_n_a)
            }
        }
        lifecycleScope.launch {
            viewModel.eurRate.collect { rate ->
                binding.currencyRateEur.text =
                    if (rate != null) context?.getString(R.string.eur_template, rate.saleRate)
                    else getString(R.string.eur_n_a)
            }
        }
    }
}
