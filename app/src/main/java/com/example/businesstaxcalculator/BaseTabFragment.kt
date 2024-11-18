package com.example.businesstaxcalculator

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel

abstract class BaseTabFragment: Fragment() {
    protected open val viewModel: ViewModel
        get() {
            throw Exception("ViewModel is not initialized.")
        }

    override fun onStart() {
        super.onStart()
    }

    fun getMainActivity(): MainActivity? {
        return activity as? MainActivity
    }

    open fun updateData() {}
}