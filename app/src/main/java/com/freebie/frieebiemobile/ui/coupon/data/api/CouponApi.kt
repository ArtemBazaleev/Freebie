package com.freebie.frieebiemobile.ui.coupon.data.api

import com.freebie.frieebiemobile.network.HttpAccess
import com.freebie.frieebiemobile.network.Method
import com.freebie.protos.CouponDescriptionApiProtos
import com.freebie.protos.CouponDescriptionModelProtos
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface CouponApi {
    suspend fun getCouponInfo(
        couponId: String
    ): CouponDescriptionApiProtos.CouponDescriptionResponse
    suspend fun getCouponsByCompanyId(
        companyId: String,
        page: Int,
        pageSize: Int
    ): CouponDescriptionApiProtos.CouponDescriptionListResponse
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

    override suspend fun getCouponsByCompanyId(
        companyId: String,
        page: Int,
        pageSize: Int
    ): CouponDescriptionApiProtos.CouponDescriptionListResponse = withContext(Dispatchers.IO) {
        val request = CouponDescriptionApiProtos
            .CouponDescriptionListRequest
            .newBuilder()
            .setCompanyId(companyId)
            .setSize(pageSize)
            .setPage(page)
            .addStatus(CouponDescriptionModelProtos.Status.ACTIVE) // TODO add dynamic status
            .build()
        val response = httpAccess.httpRequest(
            requestUrlSegment = COUPONS_BY_COMPANY,
            method = Method.POST,
            body = request.toByteArray()
        )
        return@withContext CouponDescriptionApiProtos
            .CouponDescriptionListResponse
            .parseFrom(response.bodyAsArray)

    }

    companion object {
        private val COUPON_INFO = "v1/coupon-descriptions/"
        private val COUPONS_BY_COMPANY = "v1/coupon-descriptions/list"
    }
}

