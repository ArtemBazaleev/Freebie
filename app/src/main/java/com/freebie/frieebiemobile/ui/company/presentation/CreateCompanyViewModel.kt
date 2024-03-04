package com.freebie.frieebiemobile.ui.company.presentation

import androidx.lifecycle.ViewModel
import com.freebie.frieebiemobile.ui.category.domain.usecase.GetCategoryRepositoryUseCase
import com.freebie.frieebiemobile.ui.company.domain.usecase.CreateCompanyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateCompanyViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoryRepositoryUseCase,
    private val createCompany: CreateCompanyUseCase
): ViewModel() {
}