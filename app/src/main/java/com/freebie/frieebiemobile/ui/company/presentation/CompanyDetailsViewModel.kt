package com.freebie.frieebiemobile.ui.company.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.freebie.frieebiemobile.network.NoInternetException
import com.freebie.frieebiemobile.ui.company.domain.usecase.GetCompanyDetailsUseCase
import com.freebie.frieebiemobile.ui.company.presentation.mapper.CompanyUiMapper
import com.freebie.frieebiemobile.ui.company.presentation.model.CompanyDetailsUiState
import com.freebie.frieebiemobile.ui.company.presentation.model.EMPTY_COMPANY_UI_STATE
import com.freebie.frieebiemobile.ui.utils.PlaceHolderState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyDetailsViewModel @Inject constructor(
    private val getCompanyDetailsUseCase: GetCompanyDetailsUseCase,
    private val mapper: CompanyUiMapper
): ViewModel() {

    private val _state = MutableStateFlow(EMPTY_COMPANY_UI_STATE)
    private val _placeholderState = MutableStateFlow<PlaceHolderState?>(null)
    private var requestCompanyInfoJob: Job? = null

    val state: Flow<CompanyDetailsUiState>
        get() = _state

    val placeholderState: Flow<PlaceHolderState?>
        get() = _placeholderState

    fun requestCompanyDetails(companyId: String) {
        if (requestCompanyInfoJob?.isActive == true) return
        requestCompanyInfoJob = viewModelScope.launch {
            _placeholderState.emit(null)
            getCompanyDetailsUseCase.getCompanyInfo(companyId).onSuccess { companyModel ->
                _state.emit(mapper.map(companyModel))
                _placeholderState.emit(null)
            }.onFailure {
                delay(100)
                if (it is NoInternetException) _placeholderState.emit(PlaceHolderState.NoInternet)
                else _placeholderState.emit(PlaceHolderState.SomethingWentWrong)
            }
        }
    }

}