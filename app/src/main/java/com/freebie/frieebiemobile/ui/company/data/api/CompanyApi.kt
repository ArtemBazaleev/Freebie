package com.freebie.frieebiemobile.ui.company.data.api

import android.net.Uri
import android.util.Log
import com.freebie.frieebiemobile.network.HttpAccess
import com.freebie.frieebiemobile.network.Method
import com.freebie.frieebiemobile.ui.company.domain.model.CompanyCreationParams
import com.freebie.frieebiemobile.ui.company.domain.model.ExternalLinkType
import com.freebie.protos.CommonModelProtos.Locale
import com.freebie.protos.CompanyApiProtos
import com.freebie.protos.CompanyModelProtos
import com.freebie.protos.CompanyModelProtos.Link
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.IllegalArgumentException

interface CompanyApi {
    suspend fun getCompanyInfo(companyId: String): Result<CompanyApiProtos.CompanyDataResponse>
    suspend fun createCompany(companyCreationParams: CompanyCreationParams): Result<CompanyApiProtos.CreateCompanyResponse>
    suspend fun updateCompany(companyCreationParams: CompanyCreationParams,
                              companyId: String): Result<CompanyApiProtos.CreateCompanyResponse>
    suspend fun getCompanyEditInfo(companyId: String): Result<CompanyApiProtos.CompanyEditResponse>
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

    private fun createRequestProto(companyCreationParams: CompanyCreationParams) : CompanyApiProtos.CreateCompanyRequest {
        return CompanyApiProtos
            .CreateCompanyRequest
            .newBuilder()
            .apply {
                companyCreationParams.links.forEach { companyLink ->
                    addLinks(
                        Link.newBuilder()
                            .setType(mapLink(companyLink.type))
                            .setUrl(companyLink.url)
                            .build()
                    )
                }
                companyCreationParams.avatar?.let { avatarUrl = it }
                city = companyCreationParams.city
                categoryId = companyCreationParams.categoryId
                companyCreationParams.locale.forEach { loc ->
                    addLocales(
                        Locale.newBuilder()
                            .setLocale(loc.langCode)
                            .setDescription(loc.description)
                            .setName(loc.name)
                            .build()
                    )
                }
            }
            .build()
    }

    override suspend fun createCompany(companyCreationParams: CompanyCreationParams) = runCatching {
        withContext(Dispatchers.IO) {
            val companyRequest = createRequestProto(companyCreationParams)
            val response = httpAccess.httpRequest(
                requestUrlSegment = CREATE_COMPANY,
                method = Method.POST,
                body = companyRequest.toByteArray(),
            )
            if (response.isSuccess) return@withContext CompanyApiProtos
                .CreateCompanyResponse
                .parseFrom(response.bodyAsArray)
            else if (response.code == 400) throw IllegalArgumentException("company already exist")
            else error("error while creating company")
        }
    }

    override suspend fun updateCompany(
        companyCreationParams: CompanyCreationParams,
        companyId: String
    ): Result<CompanyApiProtos.CreateCompanyResponse> = runCatching {
        withContext(Dispatchers.IO) {
            val request = createRequestProto(companyCreationParams)
            val response = httpAccess.httpRequest(
                requestUrlSegment = "$CREATE_COMPANY/$companyId",
                method = Method.PATCH,
                body = request.toByteArray()
            )
            if (response.isSuccess) return@withContext CompanyApiProtos
                .CreateCompanyResponse
                .parseFrom(response.bodyAsArray)
            else if (response.code == 400) throw IllegalArgumentException("company already exist can not patch")
            else error("error while updating company code = ${response.code}")
        }
    }

    override suspend fun getCompanyEditInfo(
        companyId: String
    ): Result<CompanyApiProtos.CompanyEditResponse> = runCatching {
        withContext(Dispatchers.IO) {
            val response = httpAccess.httpRequest(
                requestUrlSegment = "$GET_COMPANY_EDIT_INFO/$companyId/edit",
                method = Method.GET
            )
            if (response.isSuccess) return@withContext CompanyApiProtos
                .CompanyEditResponse
                .parseFrom(response.bodyAsArray)
            else error("error while getting company edit info code = ${response.code}, \"$GET_COMPANY_EDIT_INFO/$companyId/edit\"")
        }
    }

    private fun mapLink(externalLinkType: ExternalLinkType): CompanyModelProtos.LinkType {
        return when (externalLinkType) {
            ExternalLinkType.WHATSAPP -> CompanyModelProtos.LinkType.WHATSAPP
            ExternalLinkType.INSTAGRAM -> CompanyModelProtos.LinkType.INSTAGRAM
            ExternalLinkType.TELEGRAM -> CompanyModelProtos.LinkType.TELEGRAM
        }
    }

    companion object {
        private const val GET_COMPANY_INFO = "v1/companies/company-data"
        private const val GET_COMPANY_EDIT_INFO = "v1/companies"
        private const val CREATE_COMPANY = "v1/companies"
    }
}