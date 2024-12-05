package com.example.businesstaxcalculator.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.businesstaxcalculator.R
import com.example.businesstaxcalculator.databinding.FragmentSettingsBinding
import com.example.businesstaxcalculator.ui.SharedIncomeViewModel
import com.example.businesstaxcalculator.ui.base.BaseTabFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class SettingsFragment : BaseTabFragment() {

    private lateinit var binding: FragmentSettingsBinding
    override val viewModel: SharedIncomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentSettingsBinding.inflate(inflater, container, false)

        val currencies = listOf(getString(R.string.usd), getString(R.string.eur),
            getString(R.string.uah))

        val arrayAdapter = createAdapter(requireContext(),currencies)

        binding.currencyDropdown.adapter = arrayAdapter

        binding.currencyDropdown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                Toast.makeText(requireContext(), selectedItem.toString(), Toast.LENGTH_SHORT ).show()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                Toast.makeText(requireContext(), "Nothing", Toast.LENGTH_SHORT ).show()
            }
        }

        val inputTxtDollar = binding.editDollar.text.toString()
        val validResDollar = viewModel.incomeValidation(inputTxtDollar)

        val inputTxtEuro = binding.editEuro.text.toString()
        val validResEuro = viewModel.incomeValidation(inputTxtEuro)

        binding.getRateBtn.setOnClickListener {
            if(validResDollar.isSuccess){
                TODO()
            } else{
                TODO()
            }
            if(validResEuro.isSuccess){
                TODO()
            }else{
                TODO()
            }
        }

        return binding.root
    }

    fun createAdapter(context: Context, stringList: List<String>): ArrayAdapter<String> {
        return ArrayAdapter(
            context,
            android.R.layout.simple_spinner_item,
            stringList
        )
    }
}
