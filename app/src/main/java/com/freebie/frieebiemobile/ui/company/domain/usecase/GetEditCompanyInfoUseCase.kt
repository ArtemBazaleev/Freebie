package com.freebie.frieebiemobile.ui.company.domain.usecase

import com.freebie.frieebiemobile.ui.company.domain.model.CompanyEditModel

interface GetEditCompanyInfoUseCase {
    suspend fun getEditCompanyInfo(companyId: String): Result<CompanyEditModel>
}