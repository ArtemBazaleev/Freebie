package com.freebie.frieebiemobile.ui.coupon.domain

interface GetCouponDetailsUseCase {
    suspend fun getCouponDetails(id: String) : Result<CouponDescriptionModel>
}