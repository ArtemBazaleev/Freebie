package com.freebie.frieebiemobile.ui.booklet.data.api

import com.freebie.frieebiemobile.network.HttpAccess
import com.freebie.frieebiemobile.network.Method
import com.freebie.protos.BookletApiProtos
import com.freebie.protos.BookletModelProtos
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface BookletApi {
    suspend fun getBookletsByCompany(
        companyId: String,
        page: Int,
        pageSize: Int
    ): Result<BookletApiProtos.BookletListResponse>
}

class BookletApiImpl @Inject constructor(
    private val httpAccess: HttpAccess
) : BookletApi {

    override suspend fun getBookletsByCompany(
        companyId: String,
        page: Int,
        pageSize: Int
    ): Result<BookletApiProtos.BookletListResponse> = withContext(Dispatchers.IO) {

        val request = BookletApiProtos
            .BookletListRequest
            .newBuilder()
            .setCompanyId(companyId)
            .setSize(pageSize)
            .setPage(page)
            .addStatus(BookletModelProtos.Status.ACTIVE) // TODO add dynamic status
            .build()

        val response = httpAccess.httpRequest(
            requestUrlSegment = BOOKLETS_BY_COMPANY,
            method = Method.POST,
            body = request.toByteArray()
        )

        return@withContext Result.success(BookletApiProtos
            .BookletListResponse
            .parseFrom(response.bodyAsArray))
    }

    companion object {
        private const val BOOKLETS_BY_COMPANY = "v1/booklets/list"
    }
}