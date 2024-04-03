package com.freebie.frieebiemobile.ui.company.data.usecase

import com.freebie.frieebiemobile.ui.company.data.repository.CompanyRepository
import com.freebie.frieebiemobile.ui.company.domain.model.CompanyCreationParams
import com.freebie.frieebiemobile.ui.company.domain.usecase.UpdateCompanyUseCase
import javax.inject.Inject

class UpdateCompanyUseCaseImpl @Inject constructor(
    private val repository: CompanyRepository
): UpdateCompanyUseCase {
    override suspend fun updateCompany(params: CompanyCreationParams,
                                       companyId: String): Result<String> {
        return repository.updateCompany(params, companyId)
    }
}