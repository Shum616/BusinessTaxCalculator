package com.example.businesstaxcalculator.presentation.home

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.businesstaxcalculator.domain.calculator.IncomeTaxBreakdown
import com.example.businesstaxcalculator.domain.calculator.calculateFopTaxes
import com.example.businesstaxcalculator.domain.history.IncomeHistoryRecord
import com.example.businesstaxcalculator.domain.history.IncomeHistoryRepository
import com.example.businesstaxcalculator.domain.money.Money
import com.example.businesstaxcalculator.utils.FOP_GROUP_3_RATE_PREFERENCE
import com.example.businesstaxcalculator.utils.FOP_GROUP_PREFERENCE
import com.example.businesstaxcalculator.utils.FopGroup
import com.example.businesstaxcalculator.utils.Group3TaxRate
import com.example.businesstaxcalculator.utils.validator.IValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class HomeUiState(
    val income: String = "",
    val hasIncomeError: Boolean = false,
    val selectedDateEpochDay: Long = LocalDate.now().toEpochDay(),
    val result: IncomeTaxBreakdown? = null
)

sealed interface HomeUiEvent {
    data class HistorySaved(val fopGroupNumber: Int) : HomeUiEvent
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val validator: IValidator,
    private val preferences: SharedPreferences,
    private val historyRepository: IncomeHistoryRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()
    private val _events = MutableSharedFlow<HomeUiEvent>()
    val events = _events.asSharedFlow()

    fun updateIncome(income: String) {
        _uiState.update { it.copy(income = income, hasIncomeError = false) }
    }

    fun selectDate(epochDay: Long) {
        _uiState.update { it.copy(selectedDateEpochDay = epochDay) }
    }

    fun calculate() {
        val income = _uiState.value.income
        if (!validator.validateInput(income).isSuccess) {
            _uiState.update { it.copy(hasIncomeError = true) }
            return
        }
        val group = FopGroup.from(
            preferences.getInt(FOP_GROUP_PREFERENCE, FopGroup.FIRST.number)
        )
        val rate = Group3TaxRate.from(
            preferences.getInt(
                FOP_GROUP_3_RATE_PREFERENCE,
                Group3TaxRate.WITHOUT_VAT.percent
            )
        )
        val grossIncome = Money.parse(income) ?: return
        val result = calculateFopTaxes(grossIncome, group, rate)
        val date = LocalDate.ofEpochDay(_uiState.value.selectedDateEpochDay)
        _uiState.update { it.copy(result = result) }
        viewModelScope.launch {
            historyRepository.save(
                IncomeHistoryRecord(
                    date = date,
                    fopGroup = group,
                    grossIncome = grossIncome,
                    netProfit = result.netProfit,
                    esv = result.esv,
                    militaryTax = result.militaryTax,
                    singleTax = result.singleTax
                )
            )
            _events.emit(HomeUiEvent.HistorySaved(group.number))
        }
    }
}
