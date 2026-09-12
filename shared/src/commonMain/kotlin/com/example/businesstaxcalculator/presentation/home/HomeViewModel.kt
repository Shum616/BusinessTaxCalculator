package com.example.businesstaxcalculator.presentation.home

import com.example.businesstaxcalculator.domain.settings.AppSettings
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.businesstaxcalculator.domain.calculator.IncomeTaxBreakdown
import com.example.businesstaxcalculator.domain.calculator.calculateFopTaxes
import com.example.businesstaxcalculator.domain.history.IncomeHistoryRecord
import com.example.businesstaxcalculator.domain.history.IncomeHistoryRepository
import com.example.businesstaxcalculator.domain.money.Money
import com.example.businesstaxcalculator.utils.validator.IValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

data class HomeUiState(
    val isSaving: Boolean = false,
    val income: String = "",
    val hasIncomeError: Boolean = false,
    val selectedDateEpochDay: Long = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date.toEpochDays(),
    val result: IncomeTaxBreakdown? = null
)

sealed interface HomeUiEvent {
    data object SaveFailed : HomeUiEvent
    data class HistorySaved(val fopGroupNumber: Int) : HomeUiEvent
}


class HomeViewModel(
    private val validator: IValidator,
    private val settings: AppSettings,
    private val historyRepository: IncomeHistoryRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()
    private val _events = Channel<HomeUiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun updateIncome(income: String) {
        _uiState.update { it.copy(income = income, hasIncomeError = false) }
    }

    fun selectDate(epochDay: Long) {
        _uiState.update { it.copy(selectedDateEpochDay = epochDay) }
    }

    fun calculate() {
        if (_uiState.value.isSaving) return
        val income = _uiState.value.income
        if (!validator.validateInput(income).isSuccess) {
            _uiState.update { it.copy(hasIncomeError = true) }
            return
        }
        val group = settings.fopGroup
        val rate = settings.group3TaxRate
        val grossIncome = Money.parse(income) ?: return
        val result = calculateFopTaxes(grossIncome, group, rate)
        val date = LocalDate.fromEpochDays(_uiState.value.selectedDateEpochDay)
        _uiState.update { it.copy(result = result, isSaving = true) }
        viewModelScope.launch {
            try {
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
                _events.send(HomeUiEvent.HistorySaved(group.number))
            } catch (cancelled: kotlinx.coroutines.CancellationException) {
                throw cancelled
            } catch (_: Exception) {
                _events.send(HomeUiEvent.SaveFailed)
            } finally {
                _uiState.update { it.copy(isSaving = false) }
            }
        }
    }
}
