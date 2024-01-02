package com.freebie.frieebiemobile.ui.coupon.data.mapper

import com.freebie.frieebiemobile.ui.coupon.domain.CouponDescriptionModel
import com.freebie.frieebiemobile.ui.coupon.domain.CouponStatus
import com.freebie.protos.CouponDescriptionApiProtos
import com.freebie.protos.CouponDescriptionModelProtos
import javax.inject.Inject

class CouponDescriptionMapperImpl @Inject constructor() {
    fun map(
        proto: CouponDescriptionModelProtos.CouponDescription
    ): CouponDescriptionModel {
        return CouponDescriptionModel(
            id = proto.encryptedId,
            description = proto.description,
            avatar = proto.imageUrl,
            createdAt = proto.createdAt,
            expiredAt = proto.expiredAt,
            availableAmount = proto.availableAmount.toInt(),
            companyId = proto.companyId,
            name = proto.name,
            priceOfCoupon = proto.couponPrice,
            priceWithDiscount = proto.priceWithDiscount,
            priceWithoutDiscount = proto.priceWithoutDiscount,
            discount = proto.discount,
            status = proto.status.toCouponStatus()
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


