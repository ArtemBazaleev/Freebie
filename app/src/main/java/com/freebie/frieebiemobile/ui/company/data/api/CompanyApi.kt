package com.freebie.frieebiemobile.ui.company.data.api

import com.freebie.frieebiemobile.network.HttpAccess
import com.freebie.frieebiemobile.network.Method
import com.freebie.protos.CompanyApiProtos
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface CompanyApi {
    suspend fun getCompanyInfo(companyId: String): Result<CompanyApiProtos.CompanyDataResponse>
}

class CompanyApiImpl @Inject constructor(
    private val httpAccess: HttpAccess
) : CompanyApi {

    override suspend fun getCompanyInfo(companyId: String): Result<CompanyApiProtos.CompanyDataResponse> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                val response = httpAccess.httpRequest(
                    requestUrlSegment = "$GET_COMPANY_INFO/$companyId",
                    method = Method.GET
                )
                if (response.isSuccess) {
                    val proto = CompanyApiProtos
                        .CompanyDataResponse
                        .parseFrom(response.bodyAsArray)
                    Result.success(proto)
                } else {
                    Result.failure(IllegalStateException("response is not success for $GET_COMPANY_INFO $companyId"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    companion object {
        private const val GET_COMPANY_INFO = "v1/companies/company-data"
    }
}