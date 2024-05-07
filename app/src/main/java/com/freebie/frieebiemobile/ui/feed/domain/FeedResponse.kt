package com.freebie.frieebiemobile.ui.feed.domain

data class FeedResponse(
    val companyList: List<CompanyModel>
)

data class CompanyModel(
    val id: String,
    val name: String,
    val avatar: String,
    val coupons: List<CouponModel>,
    val booklet: List<BookletModel>
)

data class CouponModel(
    val id: String,
    val avatar: String,
    val name: String?,
    val description: String?,
    val discount: String?,
    val priceWithDiscount: Double,
    val priceWithoutDiscount: Double
)

data class BookletModel(
    val id: String,
    val avatar: String,
    val name: String
)

class BookletByStatus(
    val status: BookletStatus,
    val booklets: List<BookletModel>
)

enum class BookletStatus {
    IN_REVIEW, ACTIVE, EXPIRED, UNRECOGNIZED, HIDDEN
}