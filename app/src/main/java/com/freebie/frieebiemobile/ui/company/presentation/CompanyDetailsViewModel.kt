package com.freebie.frieebiemobile.ui.company.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.freebie.frieebiemobile.ui.company.domain.usecase.GetCompanyDetailsUseCase
import com.freebie.frieebiemobile.ui.company.presentation.mapper.CompanyUiMapper
import com.freebie.frieebiemobile.ui.company.presentation.model.CompanyDetailsUiState
import com.freebie.frieebiemobile.ui.company.presentation.model.EMPTY_COMPANY_UI_STATE
import dagger.hilt.android.lifecycle.HiltViewModel
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

    val state: Flow<CompanyDetailsUiState>
        get() = _state

    fun requestCompanyDetails(companyId: String) {
        viewModelScope.launch {
            val response = getCompanyDetailsUseCase.getCompanyInfo(companyId)
            if (response.isSuccess) {
                _state.emit(mapper.map(response.getOrThrow()))
            }
        }
    }

}