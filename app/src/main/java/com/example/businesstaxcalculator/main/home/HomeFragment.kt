package com.example.businesstaxcalculator.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.businesstaxcalculator.databinding.HomeFragmentLayoutBinding
import com.example.businesstaxcalculator.main.SharedIncomeViewModel
import com.example.businesstaxcalculator.main.base.BaseTabFragment

class HomeFragment : BaseTabFragment() {

    private lateinit var binding: HomeFragmentLayoutBinding
    override val viewModel: SharedIncomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeFragmentLayoutBinding.inflate(inflater, container, false)

        binding.calculateBttn.setOnClickListener {
            val inputTxt = binding.txtField.text.toString()

            val validRes = viewModel.incomeValidation(inputTxt)

            binding.txtInputLt.error =
                if (!validRes.isSuccess) "Invalid income input"
                else {
                    val array = viewModel.setIncomeTax(binding.txtField.text.toString())
                    binding.cardVw1.setValues(array[0].toString(), "income * 2")
                    binding.cardVw2.setValues(array[1].toString(), "income * 3")
                    binding.cardVw3.setValues(array[2].toString(), "income * 4")
                    binding.cardVw4.setValues(array[3].toString(), "income * 5")
                    ""
                }
        }
        return binding.root
    }
}
