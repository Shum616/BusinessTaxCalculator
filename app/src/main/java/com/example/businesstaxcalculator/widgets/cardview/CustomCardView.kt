package com.example.businesstaxcalculator.widgets.cardview

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.businesstaxcalculator.databinding.CardviewLayoutBinding
import com.example.businesstaxcalculator.R


class CardViewMainVw : ConstraintLayout {
    private val binding: CardviewLayoutBinding

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = CardviewLayoutBinding.inflate(inflater, this, true)
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs){
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CardViewMainVw)

        val titleSize = typedArray.getDimension(R.styleable.CardViewMainVw_titleTextSize, 18f)
        val subheadSize = typedArray.getDimension(R.styleable.CardViewMainVw_subheadTextSize, 16f)

        binding.title.gravity = Gravity.CENTER

        binding.title.textSize = titleSize
        binding.subhead.textSize = subheadSize

        typedArray.recycle()
    }

    constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0)
            : super(context, attrs, defStyle)

    fun setValues(title: String, value: String) {
        binding.title.text = title
        binding.subhead.text = value
    }

}