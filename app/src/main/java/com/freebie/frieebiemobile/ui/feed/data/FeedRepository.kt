package com.freebie.frieebiemobile.ui.feed.data

import com.freebie.frieebiemobile.ui.feed.domain.BookletModel
import com.freebie.frieebiemobile.ui.feed.domain.CompanyModel
import com.freebie.frieebiemobile.ui.feed.domain.CouponModel
import com.freebie.frieebiemobile.ui.feed.domain.FeedResponse
import javax.inject.Inject

class FeedRepositoryImpl @Inject constructor(
    private val feedApi: FeedApi
) : FeedRepository {

    override suspend fun requestFeed(
        page: Int,
        size: Int,
        category: String
    ): FeedResponse {
        return FeedResponse(
            feedApi.requestFeedList(
                page, size, category
            ).discountsList.map { discount ->
                CompanyModel(
                    id = discount.company.encryptedId,
                    name = discount.company.name,
                    avatar = discount.company.avatarUrl,
                    coupons = discount.couponsList.map { coupon ->
                        CouponModel(
                            id = coupon.encryptedId,
                            avatar = coupon.imageUrl,
                            name = coupon.name ?: "",
                            description = coupon.description ?: "",
                            discount = coupon.discount
                        )
                    },
                    booklet = discount.bookletsList.map { booklet ->
                        BookletModel(booklet.imageUrl)
                    }
                )
            }
        )
    }

}

interface FeedRepository {
    suspend fun requestFeed(
        page: Int,
        size: Int,
        category: String
    ): FeedResponse
}
