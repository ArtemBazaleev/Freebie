package com.freebie.frieebiemobile.ui.company.domain.usecase

import com.freebie.frieebiemobile.ui.company.domain.model.CompanyCreationParams

interface UpdateCompanyUseCase {
    suspend fun updateCompany(params: CompanyCreationParams) : Result<String>
}