package com.freebie.frieebiemobile.ui.feed.models

sealed interface FeedItem {
    fun getItemType(): FeedItemType
}

data class CompanyHeaderItem(
    val companyId: String,
    val avatar: String?,
    val companyName: String
) : FeedItem {
    override fun getItemType() = FeedItemType.COMPANY_HEADER
}

data class CouponsItem(
    val companyId: String,
    val coupons: List<CouponUI>
) : FeedItem {
    override fun getItemType() = FeedItemType.COUPON
}

data class OffersItem(
    val companyId: String,
    val offers: List<OfferUI>
) : FeedItem {
    override fun getItemType() = FeedItemType.OFFER
}

object FeedShimmer : FeedItem {
    override fun getItemType() = FeedItemType.FEED_SHIMMER
}

enum class FeedItemType(val intValue: Int) {
    COMPANY_HEADER(0), COUPON(1), OFFER(2), FEED_SHIMMER(3)
}