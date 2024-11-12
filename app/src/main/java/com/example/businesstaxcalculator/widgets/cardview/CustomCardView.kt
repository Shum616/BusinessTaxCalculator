package com.example.businesstaxcalculator.widgets.cardview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.businesstaxcalculator.databinding.CardviewLayoutBinding

class CustomCardView : ConstraintLayout{
    private val binding: CardviewLayoutBinding

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = CardviewLayoutBinding.inflate(inflater, this, true)
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0)
            : super(context, attrs, defStyle)

    fun setValues(title: String, value: String){
        //binding.title.setText(title) //setting text for int and resources
        binding.title.text = title
        binding.subhead.text = value
    }

}