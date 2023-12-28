package com.freebie.frieebiemobile.ui.coupon.data.usecase

import com.freebie.frieebiemobile.ui.coupon.data.repository.CouponRepository
import com.freebie.frieebiemobile.ui.coupon.domain.CouponDescriptionModel
import com.freebie.frieebiemobile.ui.coupon.domain.GetCouponsByCompanyUseCase
import javax.inject.Inject

class GetCouponsByCompanyUseCaseImpl @Inject constructor(
    private val repository: CouponRepository
) : GetCouponsByCompanyUseCase {
    override suspend fun getCouponsByCompany(
        companyId: String,
        page: Int,
        pageSize: Int
    ): Result<List<CouponDescriptionModel>> =
        repository.getCouponsByCompany(companyId, page, pageSize)
}