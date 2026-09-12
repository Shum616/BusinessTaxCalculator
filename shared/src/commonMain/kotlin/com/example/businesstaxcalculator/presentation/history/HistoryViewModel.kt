package com.example.businesstaxcalculator.presentation.history

import com.example.businesstaxcalculator.domain.settings.AppSettings
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.businesstaxcalculator.domain.history.HistoryPeriod
import com.example.businesstaxcalculator.domain.history.IncomeHistoryRecord
import com.example.businesstaxcalculator.domain.history.IncomeHistoryRepository
import com.example.businesstaxcalculator.domain.history.IncomeHistorySummary
import com.example.businesstaxcalculator.domain.history.summarizeIncomeHistory
import com.example.businesstaxcalculator.domain.fop.FopGroup
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HistoryUiState(
    val fopGroup: FopGroup = FopGroup.FIRST,
    val period: HistoryPeriod = HistoryPeriod.MONTH,
    val summaries: List<IncomeHistorySummary> = emptyList(),
    val isLoading: Boolean = true
)


class HistoryViewModel(
    private val repository: IncomeHistoryRepository,
    settings: AppSettings
) : ViewModel() {
    private var records: List<IncomeHistoryRecord> = emptyList()
    private val _uiState = MutableStateFlow(
        HistoryUiState(
            fopGroup = settings.fopGroup
        )
    )
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observeAll().collectLatest { records ->
                this@HistoryViewModel.records = records
                _uiState.update {
                    it.copy(
                        summaries = summaries(records, it.fopGroup, it.period),
                        isLoading = false
                    )
                }
            }
        }
    }

    fun selectPeriod(period: HistoryPeriod) {
        _uiState.update {
            it.copy(period = period, summaries = summaries(records, it.fopGroup, period))
        }
    }

    fun selectFopGroup(group: FopGroup) {
        _uiState.update {
            it.copy(fopGroup = group, summaries = summaries(records, group, it.period))
        }
    }

    private fun summaries(
        records: List<IncomeHistoryRecord>,
        group: FopGroup,
        period: HistoryPeriod
    ) = summarizeIncomeHistory(records.filter { it.fopGroup == group }, period)
}
