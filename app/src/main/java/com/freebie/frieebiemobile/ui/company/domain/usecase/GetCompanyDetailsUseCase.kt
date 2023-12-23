package com.freebie.frieebiemobile.ui.company.domain.usecase

import com.freebie.frieebiemobile.ui.company.domain.model.CompanyModel


interface GetCompanyDetailsUseCase {
    suspend fun getCompanyInfo(companyId: String): Result<CompanyModel>
}