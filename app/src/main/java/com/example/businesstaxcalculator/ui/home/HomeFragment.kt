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

        binding.unitedTax.setValues(getString(R.string.flat_rate_tax), "0")
        binding.unitedContr.setValues(getString(R.string.unified_social_contribution), "0")
        binding.remaining.setValues(getString(R.string.net_gross), "0")
        binding.taxes.setValues(getString(R.string.taxes), "0")

        binding.calculateBttn.setOnClickListener {
            val inputTxt = binding.txtField.text.toString()
            val validRes = viewModel.incomeValidation(binding.txtField.text.toString())

            binding.txtInputLt.error =
                if (!validRes.isSuccess) "Invalid income input"
                else {
                    binding.unitedTax.setValues(getString(R.string.flat_rate_tax), viewModel.calculateUnitedTaxUan(inputTxt, 1.0))
                    binding.unitedContr.setValues(getString(R.string.unified_social_contribution), viewModel.calculateUnidedSocialСontributionUan(inputTxt))
                    binding.remaining.setValues(getString(R.string.net_gross), viewModel.calculateRemaining(inputTxt))
                    binding.taxes.setValues(getString(R.string.taxes), viewModel.calculateTaxes(inputTxt))
                    ""
                }
        }
        return binding.root
    }
}
