package com.freebie.frieebiemobile.ui.rate.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.freebie.frieebiemobile.ui.rate.domain.usecase.RateCompanyUseCase
import com.freebie.frieebiemobile.ui.rate.presentation.model.RateCompanyEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RateCompanyViewModel @Inject constructor(
    private val rateCompanyUseCase: RateCompanyUseCase
) : ViewModel() {

    private var rateJob: Job? = null

    private val _events = MutableSharedFlow<RateCompanyEvent>(replay = 1)

    val events: Flow<RateCompanyEvent>
        get() = _events


    fun rateCompany(
        message: String,
        rating: Int,
        companyId: String
    ) {
        if (rateJob?.isActive == true) return
        rateJob = viewModelScope.launch {
            rateCompanyUseCase
                .rate(rating, message, companyId)
                .onSuccess { _events.emit(RateCompanyEvent.RateSuccess) }
                .onFailure { _events.emit(RateCompanyEvent.RateFailed) }
        }
    }
}