package com.freebie.frieebiemobile.ui.coupon.presentation.model

data class CouponDetailsUiModel(
    val title: String,
    val imageUrl: String,
    val description: String,
    val priceWithoutDiscount: String,
    val priceWithDiscount: String,
    val couponPriceText: String
)