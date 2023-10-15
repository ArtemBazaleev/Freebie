package com.freebie.frieebiemobile.ui.feed.data

import android.util.Log
import com.freebie.frieebiemobile.ContinuationCallback
import com.freebie.frieebiemobile.network.HttpAccess
import com.freebie.frieebiemobile.network.Method
import com.freebie.protos.DiscountsApiProtos.DiscountsListRequest
import com.freebie.protos.DiscountsApiProtos.DiscountsListResponse

import javax.inject.Inject


class FeedApiImpl @Inject constructor(
    private val httpAccess: HttpAccess
): FeedApi {

    override suspend fun requestFeedList(
        page: Int,
        size: Int,
        category: String
    ): DiscountsListResponse {
        val body = DiscountsListRequest.newBuilder()
            .setPage(page)
            .setSize(size)
            .setEncryptedCategoryId(category)
            .build()
        val response = httpAccess.httpRequest(
            requestUrlSegment = "v1/discounts",
            headers = mapOf(),
            body = body.toByteArray(),
            method = Method.POST
        )
        return DiscountsListResponse.parseFrom(response.bodyAsArray)
    }

}

interface FeedApi {
    suspend fun requestFeedList(
        page: Int,
        size: Int,
        category: String
    ) : DiscountsListResponse
}