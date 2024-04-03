package com.freebie.frieebiemobile.ui.company.data.usecase

import com.freebie.frieebiemobile.ui.company.data.repository.CompanyRepository
import com.freebie.frieebiemobile.ui.company.domain.model.CompanyEditModel
import com.freebie.frieebiemobile.ui.company.domain.usecase.GetEditCompanyInfoUseCase
import javax.inject.Inject

class GetEditCompanyInfoUseCaseImpl @Inject constructor(
    private val repository: CompanyRepository
): GetEditCompanyInfoUseCase {
    override suspend fun getEditCompanyInfo(companyId: String): Result<CompanyEditModel> {
        return repository.getEditCompanyInfo(companyId)
    }
}