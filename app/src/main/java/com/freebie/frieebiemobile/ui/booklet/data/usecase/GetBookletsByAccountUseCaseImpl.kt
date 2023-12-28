package com.freebie.frieebiemobile.ui.booklet.data.usecase

import com.freebie.frieebiemobile.ui.booklet.data.repository.BookletRepository
import com.freebie.frieebiemobile.ui.booklet.domain.GetBookletsByAccountUseCase
import com.freebie.frieebiemobile.ui.feed.domain.BookletModel
import javax.inject.Inject

class GetBookletsByAccountUseCaseImpl @Inject constructor(
    private val repository: BookletRepository
) : GetBookletsByAccountUseCase {
    override suspend fun getBookletsByCompany(
        companyId: String,
        page: Int,
        pageSize: Int
    ): Result<List<BookletModel>> =
        repository.getBookletsByCompany(companyId, page, pageSize)
}