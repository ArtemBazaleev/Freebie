package com.freebie.frieebiemobile.ui.feed.data

import com.freebie.frieebiemobile.ui.feed.domain.FeedResponse
import com.freebie.frieebiemobile.ui.feed.models.CompanyHeaderItem
import com.freebie.frieebiemobile.ui.feed.models.CouponUI
import com.freebie.frieebiemobile.ui.feed.models.CouponsItem
import com.freebie.frieebiemobile.ui.feed.models.FeedItem
import com.freebie.frieebiemobile.ui.feed.models.OfferUI
import com.freebie.frieebiemobile.ui.feed.models.OffersItem
import javax.inject.Inject

class CompanyListMapper @Inject constructor() {
    fun map(feed: FeedResponse): List<FeedItem> {
        val feedItems = mutableListOf<FeedItem>()
        feed.companyList.forEach {
            feedItems.add(CompanyHeaderItem(
                it.id,
                it.avatar,
                it.name
            ))
            val coupons = it.coupons.map { coupon ->
                CouponUI(
                    id = coupon.id,
                    avatar = coupon.avatar,
                    name = coupon.name ?: "",
                    description = coupon.description ?: "",
                    discount = coupon.discount ?: "",
                    price = coupon.priceWithoutDiscount,
                    priceWithDiscount = coupon.priceWithoutDiscount
                )
            }
            if (coupons.isNotEmpty()) feedItems.add(CouponsItem(it.id, coupons))
            val booklet = it.booklet.map { booklet ->
                OfferUI(booklet.avatar)
            }
            if (booklet.isNotEmpty()) feedItems.add(OffersItem(it.id, booklet))
        }
        return feedItems
    }
}