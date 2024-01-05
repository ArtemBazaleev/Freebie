package com.freebie.frieebiemobile.ui.rate.data.repository

import com.freebie.frieebiemobile.ui.rate.data.api.ReviewApi
import com.freebie.frieebiemobile.ui.rate.data.mapper.ReviewModelMapper
import com.freebie.frieebiemobile.ui.rate.domain.RateModel
import javax.inject.Inject

interface ReviewRepository {
    suspend fun reviewCompany(
        companyId: String,
        rating: Int,
        comment: String
    ): Result<Boolean>

    suspend fun companyReviewList(
        companyId: String,
        page: Int,
        size: Int
    ): Result<List<RateModel>>
}

class ReviewRepositoryImpl @Inject constructor(
    private val reviewApi: ReviewApi,
    private val reviewMapper: ReviewModelMapper
) : ReviewRepository {

    override suspend fun reviewCompany(
        companyId: String,
        rating: Int,
        comment: String
    ): Result<Boolean> {
        val response = reviewApi.rateCompany(rating, companyId, comment)
        return if (response.isSuccess) Result.success(true)
        else Result.failure(response.exceptionOrNull() ?: IllegalStateException(""))
    }

    override suspend fun companyReviewList(
        companyId: String,
        page: Int,
        size: Int
    ): Result<List<RateModel>> {
        val response = reviewApi.companyRates(companyId, page, size)
        return if (response.isSuccess) Result.success(
            response.getOrThrow().reviewsList.map(
                reviewMapper::mapReview
            )
        )
        else Result.failure(response.exceptionOrNull() ?: IllegalStateException(""))
    }

}

