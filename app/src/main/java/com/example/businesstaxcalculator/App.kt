package com.example.businesstaxcalculator

import android.app.Application
import com.example.businesstaxcalculator.di.createAppContainer

class App : Application() {
    val container by lazy { createAppContainer(this) }
}
