package com.freebie.frieebiemobile.ui.coupon.data.api

import com.freebie.frieebiemobile.network.HttpAccess
import com.freebie.frieebiemobile.network.Method
import com.freebie.protos.CouponDescriptionApiProtos
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface CouponApi {
    suspend fun getCouponInfo(couponId: String): CouponDescriptionApiProtos.CouponDescriptionResponse
}

class CouponApiImpl @Inject constructor(
    private val httpAccess: HttpAccess
) : CouponApi {

    override suspend fun getCouponInfo(
        couponId: String
    ): CouponDescriptionApiProtos.CouponDescriptionResponse = withContext(Dispatchers.IO) {
        val response = httpAccess.httpRequest(
            requestUrlSegment = COUPON_INFO + couponId,
            method = Method.GET
        )
        return@withContext CouponDescriptionApiProtos
            .CouponDescriptionResponse.parseFrom(response.bodyAsArray)
    }

    companion object {
        private val COUPON_INFO = "v1/coupon-descriptions/"
    }
}

