package com.freebie.frieebiemobile.ui.coupon.data.usecase

import com.freebie.frieebiemobile.ui.coupon.data.repository.CouponRepository
import com.freebie.frieebiemobile.ui.coupon.domain.CouponDescriptionModel
import com.freebie.frieebiemobile.ui.coupon.domain.GetCouponDetailsUseCase
import javax.inject.Inject

class GetCouponDetailsUseCaseImpl @Inject constructor(
    private val repository: CouponRepository
): GetCouponDetailsUseCase {

    override suspend fun getCouponDetails(id: String): Result<CouponDescriptionModel> {
        return repository.getCouponDescription(id)
    }

}