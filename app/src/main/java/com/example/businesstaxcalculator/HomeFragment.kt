package com.example.businesstaxcalculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.businesstaxcalculator.databinding.HomeFragmentLayoutBinding
import com.example.businesstaxcalculator.validatot.IncomeValidator
import com.example.businesstaxcalculator.viewmodel.IncomeViewModel

class HomeFragment : Fragment() {

    private lateinit var binding: HomeFragmentLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeFragmentLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val incomeCounter = IncomeViewModel()

        binding.calculateBttn.setOnClickListener {
            val inputTxt = binding.txtField.text.toString()
            val validRes = IncomeValidator(inputTxt).validate()

            binding.txtInputLt.error =
                if (!validRes.isSuccess) "Invalid income input"
                else {
                    val array = incomeCounter.SetIncomeTax(binding.txtField.text.toString())
                    binding.cardVw1.setValues(array[0].toString(), "income * 2")
                    binding.cardVw2.setValues(array[1].toString(), "income * 3")
                    binding.cardVw3.setValues(array[2].toString(), "income * 4")
                    binding.cardVw4.setValues(array[3].toString(), "income * 5")
                    ""
                }
        }
    }
}
