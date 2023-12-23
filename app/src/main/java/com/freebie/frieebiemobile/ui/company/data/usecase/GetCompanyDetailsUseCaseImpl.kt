package com.freebie.frieebiemobile.ui.company.data.usecase

import com.freebie.frieebiemobile.ui.company.data.repository.CompanyRepository
import com.freebie.frieebiemobile.ui.company.domain.usecase.GetCompanyDetailsUseCase
import javax.inject.Inject

class GetCompanyDetailsUseCaseImpl @Inject constructor(
    private val repository: CompanyRepository
): GetCompanyDetailsUseCase {
    override suspend fun getCompanyInfo(companyId: String) = repository.companyById(companyId)
}