package com.freebie.frieebiemobile.ui.rate.data.usecase

import com.freebie.frieebiemobile.ui.rate.data.repository.ReviewRepository
import com.freebie.frieebiemobile.ui.rate.domain.usecase.RateCompanyUseCase
import javax.inject.Inject

class RateCompanyUseCaseImpl @Inject constructor(
    private val repository: ReviewRepository
): RateCompanyUseCase {
    override suspend fun rate(rating: Int, comment: String, companyId: String): Result<Boolean> {
        return repository.reviewCompany(companyId, rating, comment)
    }
}