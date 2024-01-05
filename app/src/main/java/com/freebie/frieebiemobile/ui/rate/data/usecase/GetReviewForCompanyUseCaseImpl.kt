package com.freebie.frieebiemobile.ui.rate.data.usecase

import com.freebie.frieebiemobile.ui.rate.data.repository.ReviewRepository
import com.freebie.frieebiemobile.ui.rate.domain.RateModel
import com.freebie.frieebiemobile.ui.rate.domain.usecase.GetReviewForCompanyUseCase
import javax.inject.Inject

class GetReviewForCompanyUseCaseImpl @Inject constructor(
    private val repository: ReviewRepository
): GetReviewForCompanyUseCase {
    override suspend fun getReviewForCompany(
        companyId: String,
        page: Int,
        size: Int
    ) = repository.companyReviewList(companyId, page, size)
}