package com.example.businesstaxcalculator.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.businesstaxcalculator.R
import com.example.businesstaxcalculator.data.UserSelection
import com.example.businesstaxcalculator.data.database.DataStorage
import com.example.businesstaxcalculator.data.database.IDataStorage
import com.example.businesstaxcalculator.databinding.FragmentSettingsBinding
import com.example.businesstaxcalculator.ui.SharedIncomeViewModel
import com.example.businesstaxcalculator.ui.base.BaseTabFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.getValue

@AndroidEntryPoint
class SettingsFragment : BaseTabFragment() {

    private lateinit var binding: FragmentSettingsBinding
    override val viewModel: SharedIncomeViewModel by viewModels()
    @Inject
    lateinit var dataStorage: IDataStorage<UserSelection>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentSettingsBinding.inflate(inflater, container, false)

        val currencies = listOf(getString(R.string.usd), getString(R.string.eur),
            getString(R.string.uah))

        val arrayAdapter = createAdapter(requireContext(),currencies)

        binding.materialSpinner.setAdapter(arrayAdapter)

        /////not sure about this part
        val userSelection = UserSelection()

        binding.materialSpinner.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = currencies[position]
            userSelection.spinnerSelection = selectedItem
        }



        binding.getRateBtn.setOnClickListener {
            val inputTxtDollar = binding.editDollar.text.toString()
            val validResDollar = viewModel.incomeValidation(inputTxtDollar)

            val inputTxtEuro = binding.editEuro.text.toString()
            val validResEuro = viewModel.incomeValidation(inputTxtEuro)

            if(validResDollar.isSuccess){
                userSelection.dollarInput = inputTxtDollar.toDouble()
            } else{
                Toast.makeText(requireContext(), "Enter value again", Toast.LENGTH_SHORT).show()
            }
            if(validResEuro.isSuccess){
                userSelection.euroInput = inputTxtEuro.toDouble()
            }else{
                Toast.makeText(requireContext(), "Enter value again", Toast.LENGTH_SHORT).show()
            }

            lifecycleScope.launch { dataStorage.save(userSelection) }

        }

//        lifecycleScope.launch {    //this function works!
//            val loadedData = dataStorage.load()
//            loadedData?.let {
//                println("Spinner: ${it.spinnerSelection}, First: ${it.dollarInput}, Second: ${it.euroInput}")
//            }
//        }

        return binding.root
    }

    fun createAdapter(context: Context, stringList: List<String>): ArrayAdapter<String> {
        return ArrayAdapter(
            context,
            android.R.layout.simple_dropdown_item_1line,
            stringList
        )
    }
}
