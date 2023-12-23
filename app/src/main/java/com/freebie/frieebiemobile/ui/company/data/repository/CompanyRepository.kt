package com.freebie.frieebiemobile.ui.company.data.repository

import com.freebie.frieebiemobile.ui.company.data.api.CompanyApi
import com.freebie.frieebiemobile.ui.company.data.mapper.CompanyDomainMapper
import com.freebie.frieebiemobile.ui.company.domain.model.CompanyModel
import javax.inject.Inject

interface CompanyRepository {
    suspend fun companyById(companyId: String): Result<CompanyModel>
}

class CompanyRepositoryImpl @Inject constructor(
    private val api: CompanyApi,
    private val mapper: CompanyDomainMapper
) : CompanyRepository {

    override suspend fun companyById(companyId: String): Result<CompanyModel> {
        val companyResponse = api.getCompanyInfo(companyId)
        return if (companyResponse.isSuccess) {
            Result.success(mapper.map(companyResponse.getOrThrow()))
        } else {
            val exception = companyResponse.exceptionOrNull()
                ?: IllegalStateException("error while requesting details")
            Result.failure(exception)
        }
    }

}