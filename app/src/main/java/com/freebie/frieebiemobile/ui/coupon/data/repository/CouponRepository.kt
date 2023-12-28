package com.freebie.frieebiemobile.ui.coupon.data.repository

import com.freebie.frieebiemobile.ui.coupon.data.api.CouponApi
import com.freebie.frieebiemobile.ui.coupon.data.mapper.CouponDescriptionMapperImpl
import com.freebie.frieebiemobile.ui.coupon.domain.CouponDescriptionModel
import javax.inject.Inject

interface CouponRepository {
    suspend fun getCouponDescription(couponId: String): Result<CouponDescriptionModel>

    suspend fun getCouponsByCompany(
        companyId: String,
        page: Int,
        pageSize: Int
    ): Result<List<CouponDescriptionModel>>
}

class CouponRepositoryImpl @Inject constructor(
    private val api: CouponApi,
    private val mapper: CouponDescriptionMapperImpl
) : CouponRepository {

    override suspend fun getCouponDescription(couponId: String): Result<CouponDescriptionModel> =
        try {
            Result.success(mapper.map(api.getCouponInfo(couponId).coupon))
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun getCouponsByCompany(
        companyId: String,
        page: Int,
        pageSize: Int
    ): Result<List<CouponDescriptionModel>> =
        try {
            val apiResponse = api.getCouponsByCompanyId(companyId, page, pageSize)
            Result.success(apiResponse.couponsList.map(mapper::map))
        } catch (e: Exception) {
            Result.failure(e)
        }
}