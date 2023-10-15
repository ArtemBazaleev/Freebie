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
    val discount: String?
)

data class BookletModel(
    val avatar: String
)