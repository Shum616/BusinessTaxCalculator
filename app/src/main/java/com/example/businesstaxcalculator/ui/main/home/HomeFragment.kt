package com.example.businesstaxcalculator.ui.main.home

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.example.businesstaxcalculator.R
import com.example.businesstaxcalculator.databinding.HomeFragmentLayoutBinding
import com.example.businesstaxcalculator.ui.main.SharedIncomeViewModel
import com.example.businesstaxcalculator.ui.base.BaseTabFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class HomeFragment : BaseTabFragment() {

    private lateinit var binding: HomeFragmentLayoutBinding
    override val viewModel: SharedIncomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeFragmentLayoutBinding.inflate(inflater, container, false)


        val calendar = Calendar.getInstance()

        binding.datePicker.setOnClickListener {
            showDatePicker(binding.datePicker)
        }



        binding.unitedTax.setValues(getString(R.string.flat_rate_tax), "0")
        binding.unitedContr.setValues(getString(R.string.unified_social_contribution), "0")
        binding.remaining.setValues(getString(R.string.net_gross), "0")
        binding.taxes.setValues(getString(R.string.taxes), "0")

        binding.calculateBttn.setOnClickListener {
            val validatedIncome = viewModel.incomeValidation(binding.txtField.text.toString())

            if (validatedIncome == null) binding.txtInputLt.error = "Invalid income input"
            else {
                binding.unitedTax.setValues(
                    getString(R.string.flat_rate_tax),
                    viewModel.getUnitedTaxUan(validatedIncome, 1.0).toString()
                )
                binding.unitedContr.setValues(
                    getString(R.string.unified_social_contribution),
                    viewModel.getUnidedSocialСontributionUan(validatedIncome).toString()
                )
                binding.remaining.setValues(
                    getString(R.string.net_gross),
                    viewModel.getRemaining(validatedIncome).toString()
                )
                binding.taxes.setValues(
                    getString(R.string.taxes),
                    viewModel.getTaxes(validatedIncome).toString()
                )
            }
        }
        return binding.root
    }

    private fun showDatePicker(editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(this.requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                editText.setText(formattedDate)
            }, year, month, day)
        datePickerDialog.show()
    }
}
