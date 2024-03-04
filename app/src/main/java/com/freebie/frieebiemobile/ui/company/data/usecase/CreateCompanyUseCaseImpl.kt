package com.freebie.frieebiemobile.ui.company.data.usecase

import com.freebie.frieebiemobile.ui.company.data.repository.CompanyRepository
import com.freebie.frieebiemobile.ui.company.domain.model.CompanyCreationParams
import com.freebie.frieebiemobile.ui.company.domain.usecase.CreateCompanyUseCase
import javax.inject.Inject

class CreateCompanyUseCaseImpl @Inject constructor(
    private val repository: CompanyRepository
): CreateCompanyUseCase {

    override suspend fun createCompany(params: CompanyCreationParams) : Result<String> {
        return repository.createCompany(params)
    }
}