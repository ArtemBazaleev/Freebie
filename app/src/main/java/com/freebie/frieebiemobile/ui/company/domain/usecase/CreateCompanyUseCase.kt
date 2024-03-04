package com.freebie.frieebiemobile.ui.company.domain.usecase

import com.freebie.frieebiemobile.ui.company.domain.model.CompanyCreationParams

interface CreateCompanyUseCase {
    suspend fun createCompany(params: CompanyCreationParams) : Result<String>
}