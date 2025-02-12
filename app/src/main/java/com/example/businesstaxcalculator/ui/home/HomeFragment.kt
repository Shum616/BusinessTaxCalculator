package com.example.businesstaxcalculator.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.businesstaxcalculator.R
import com.example.businesstaxcalculator.databinding.HomeFragmentLayoutBinding
import com.example.businesstaxcalculator.ui.SharedIncomeViewModel
import com.example.businesstaxcalculator.ui.base.BaseTabFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseTabFragment() {

    private lateinit var binding: HomeFragmentLayoutBinding
    override val viewModel: SharedIncomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeFragmentLayoutBinding.inflate(inflater, container, false)

        binding.cardVw1.setValues(getString(R.string.flat_rate_tax), "0")
        binding.cardVw2.setValues(getString(R.string.unified_social_contribution), "0")
        binding.cardVw3.setValues(getString(R.string.net_gross), "0")
        binding.cardVw4.setValues(getString(R.string.taxes), "0")

        binding.calculateBttn.setOnClickListener {
            val inputTxt = binding.txtField.text.toString()
            val numToCalculate = inputTxt.toDouble()
            val validRes = viewModel.incomeValidation(inputTxt)

            binding.txtInputLt.error =
                if (!validRes.isSuccess) "Invalid income input"
                else {
                    binding.cardVw1.setValues(getString(R.string.flat_rate_tax), viewModel.calculateUnitedTaxUan(numToCalculate, 1.0).toString())
                    binding.cardVw2.setValues(getString(R.string.unified_social_contribution), viewModel.calculateUnidedSocialСontributionUan(numToCalculate).toString())
                    binding.cardVw3.setValues(getString(R.string.net_gross), viewModel.calculateRemaining(numToCalculate).toString())
                    binding.cardVw4.setValues(getString(R.string.taxes), viewModel.calculateTaxes(numToCalculate).toString())
                    ""
                }
        }
        return binding.root
    }
}
