package com.example.businesstaxcalculator.domain.calculator

import com.example.businesstaxcalculator.utils.FopGroup
import com.example.businesstaxcalculator.utils.GROUP_1_MILITARY_TAX_MONTHLY_UAH
import com.example.businesstaxcalculator.utils.GROUP_1_SINGLE_TAX_MONTHLY_UAH
import com.example.businesstaxcalculator.utils.GROUP_2_MILITARY_TAX_MONTHLY_UAH
import com.example.businesstaxcalculator.utils.GROUP_2_SINGLE_TAX_MONTHLY_UAH
import com.example.businesstaxcalculator.utils.GROUP_3_MILITARY_TAX_PERCENT
import com.example.businesstaxcalculator.utils.Group3TaxRate
import com.example.businesstaxcalculator.utils.MONTHLY_ESV_UAH
import java.math.BigDecimal
import java.math.RoundingMode

data class IncomeTaxBreakdown(
    val netProfit: Double,
    val esv: Double,
    val militaryTax: Double,
    val singleTax: Double
)

fun calculateFopTaxes(income: Double, group: FopGroup, group3Rate: Group3TaxRate): IncomeTaxBreakdown {
    val gross = income.money()
    val esv = MONTHLY_ESV_UAH.money()
    val militaryTax: BigDecimal
    val singleTax: BigDecimal

    when (group) {
        FopGroup.FIRST -> {
            militaryTax = GROUP_1_MILITARY_TAX_MONTHLY_UAH.money()
            singleTax = GROUP_1_SINGLE_TAX_MONTHLY_UAH.money()
        }
        FopGroup.SECOND -> {
            militaryTax = GROUP_2_MILITARY_TAX_MONTHLY_UAH.money()
            singleTax = GROUP_2_SINGLE_TAX_MONTHLY_UAH.money()
        }
        FopGroup.THIRD -> {
            militaryTax = gross.percent(GROUP_3_MILITARY_TAX_PERCENT)
            singleTax = gross.percent(group3Rate.percent)
        }
    }

    return IncomeTaxBreakdown(
        netProfit = gross.subtract(esv).subtract(militaryTax).subtract(singleTax).moneyValue(),
        esv = esv.moneyValue(),
        militaryTax = militaryTax.moneyValue(),
        singleTax = singleTax.moneyValue()
    )
}

private fun Number.money(): BigDecimal = BigDecimal(toString()).setScale(2, RoundingMode.HALF_UP)
private fun BigDecimal.percent(percent: Int): BigDecimal =
    multiply(BigDecimal(percent)).divide(BigDecimal(100)).setScale(2, RoundingMode.HALF_UP)
private fun BigDecimal.moneyValue(): Double = setScale(2, RoundingMode.HALF_UP).toDouble()
