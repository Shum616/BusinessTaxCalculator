package com.example.businesstaxcalculator.main.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.businesstaxcalculator.R
import com.example.businesstaxcalculator.main.base.BaseTabFragment

class ProfileFragment : BaseTabFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        return view
    }
}
