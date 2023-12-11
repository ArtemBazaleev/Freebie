package com.freebie.frieebiemobile.ui.coupon.data.mapper

import com.freebie.frieebiemobile.ui.coupon.domain.CouponDescriptionModel
import com.freebie.frieebiemobile.ui.coupon.domain.CouponStatus
import com.freebie.protos.CouponDescriptionApiProtos
import com.freebie.protos.CouponDescriptionModelProtos
import javax.inject.Inject

class CouponDescriptionMapperImpl @Inject constructor() {
    fun map(
        proto: CouponDescriptionApiProtos.CouponDescriptionResponse
    ): CouponDescriptionModel {
        proto.coupon.status
        return CouponDescriptionModel(
            id = proto.coupon.encryptedId,
            description = proto.coupon.description,
            avatar = proto.coupon.imageUrl,
            createdAt = proto.coupon.createdAt,
            expiredAt = proto.coupon.expiredAt,
            availableAmount = proto.coupon.availableAmount.toInt(),
            companyId = proto.coupon.companyId,
            name = proto.coupon.name,
            priceOfCoupon = proto.coupon.couponPrice,
            priceWithDiscount = proto.coupon.priceWithDiscount,
            priceWithoutDiscount = proto.coupon.priceWithoutDiscount,
            status = proto.coupon.status.toCouponStatus()
        )
    }

    private fun CouponDescriptionModelProtos.Status.toCouponStatus(): CouponStatus {
        return when(this) {
            CouponDescriptionModelProtos.Status.IN_REVIEW -> CouponStatus.IN_REVIEW
            CouponDescriptionModelProtos.Status.ACTIVE -> CouponStatus.ACTIVE
            CouponDescriptionModelProtos.Status.EXPIRED -> CouponStatus.EXPIRED
            CouponDescriptionModelProtos.Status.UNRECOGNIZED -> CouponStatus.UNRECOGNIZED
        }
    }

}


