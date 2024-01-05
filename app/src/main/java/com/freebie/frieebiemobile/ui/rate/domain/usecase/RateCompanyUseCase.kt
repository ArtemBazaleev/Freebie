package com.freebie.frieebiemobile.ui.rate.domain.usecase

interface RateCompanyUseCase {
    suspend fun rate(rating: Int, comment: String, companyId: String): Result<Boolean>
}