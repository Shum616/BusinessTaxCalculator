package com.example.businesstaxcalculator.viewmodel

import android.view.View
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import com.example.businesstaxcalculator.databinding.ActivityMainBinding

class IncomeViewModel : BaseObservable() {

    fun SetIncomeTax(income:String): Array<Double> {
        var icomeNum = income.toDouble()

        var taxResult1 = icomeNum*2
        var taxResult2 = icomeNum*3
        var taxResult3 = icomeNum*4
        var taxResult4 = icomeNum*5

        return arrayOf(taxResult1,taxResult2, taxResult3, taxResult4)
    }
}