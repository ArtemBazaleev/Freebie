package com.freebie.frieebiemobile.ui.coupon.domain

interface GetCouponsByCompanyUseCase {
    suspend fun getCouponsByCompany(
        companyId: String,
        page: Int,
        pageSize: Int
    ): Result<List<CouponDescriptionModel>>
}