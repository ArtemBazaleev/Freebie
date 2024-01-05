package com.freebie.frieebiemobile.ui.rate.data.api

import android.net.Uri
import com.freebie.frieebiemobile.network.HttpAccess
import com.freebie.frieebiemobile.network.Method
import com.freebie.protos.ReviewApiProtos
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface ReviewApi {
    suspend fun rateCompany(
        rating: Int,
        companyId: String,
        comment: String
    ): Result<ReviewApiProtos.CreateReviewResponse>

    suspend fun companyRates(
        companyId: String,
        page: Int,
        size: Int
    ): Result<ReviewApiProtos.ReviewListResponse>
}

class ReviewApiImpl @Inject constructor(
    private val httpAccess: HttpAccess
): ReviewApi {

    override suspend fun rateCompany(
        rating: Int,
        companyId: String,
        comment: String
    ): Result<ReviewApiProtos.CreateReviewResponse> = withContext(Dispatchers.IO) {
        runCatching {
            val response = httpAccess.httpRequest(
                requestUrlSegment = RATE_COMPANY,
                method = Method.POST,
                body = ReviewApiProtos.CreateReviewRequest.newBuilder()
                    .setCompanyId(companyId).setMessage(comment).setScore(rating)
                    .build()
                    .toByteArray()
            )
            ReviewApiProtos.CreateReviewResponse.parseFrom(response.bodyAsArray)
        }
    }

    override suspend fun companyRates(
        companyId: String,
        page: Int,
        size: Int
    ): Result<ReviewApiProtos.ReviewListResponse> = withContext(Dispatchers.IO) {
        runCatching {
            val path = Uri.parse(COMPANY_REVIEWS)
                .buildUpon()
                .appendEncodedPath(companyId)
                .appendQueryParameter("page", page.toString())
                .appendQueryParameter("size", size.toString())
                .toString()
            val response = httpAccess.httpRequest(
                requestUrlSegment = path,
                method = Method.GET
            )
            ReviewApiProtos.ReviewListResponse.parseFrom(response.bodyAsArray)
        }
    }

    companion object {
        private const val RATE_COMPANY = "v1/reviews/company"
        private const val COMPANY_REVIEWS = "v1/reviews/company"
    }

}