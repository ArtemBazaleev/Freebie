package com.freebie.frieebiemobile.ui.booklet.domain

import com.freebie.frieebiemobile.ui.feed.domain.BookletModel

interface GetBookletsByAccountUseCase {
    suspend fun getBookletsByCompany(
        companyId: String,
        page: Int,
        pageSize: Int
    ): Result<List<BookletModel>>
}