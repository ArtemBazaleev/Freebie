package com.freebie.frieebiemobile.ui.rate.domain.usecase

import com.freebie.frieebiemobile.ui.rate.domain.RateModel

interface GetReviewForCompanyUseCase {
    suspend fun getReviewForCompany(
        companyId: String,
        page: Int,
        size: Int
    ) : Result<List<RateModel>>
}