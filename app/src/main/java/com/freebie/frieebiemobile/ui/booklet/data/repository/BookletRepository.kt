package com.freebie.frieebiemobile.ui.booklet.data.repository

import com.freebie.frieebiemobile.ui.booklet.data.api.BookletApi
import com.freebie.frieebiemobile.ui.booklet.data.mapper.BookletDataMapper
import com.freebie.frieebiemobile.ui.feed.domain.BookletModel
import javax.inject.Inject

interface BookletRepository {
    suspend fun getBookletsByCompany(
        companyId: String,
        page: Int,
        pageSize: Int
    ): Result<List<BookletModel>>
}

class BookletRepositoryImpl @Inject constructor(
    private val api: BookletApi,
    private val mapper: BookletDataMapper
): BookletRepository {
    override suspend fun getBookletsByCompany(
        companyId: String,
        page: Int,
        pageSize: Int
    ): Result<List<BookletModel>> =
        try {
            val apiResponse = api.getBookletsByCompany(companyId, page, pageSize).getOrThrow()
            Result.success(apiResponse.bookletsList.map(mapper::map))
        } catch (e: Exception) {
            Result.failure(e)
        }
}