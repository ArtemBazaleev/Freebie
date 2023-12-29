package com.freebie.frieebiemobile.ui.account.data.mapper

import com.freebie.frieebiemobile.ui.account.domain.model.CouponsByGroupModel
import com.freebie.frieebiemobile.ui.account.domain.model.StatusCoupon
import com.freebie.frieebiemobile.ui.feed.domain.CouponModel
import com.freebie.protos.CouponDescriptionModelProtos
import com.freebie.protos.CouponModelProtos
import javax.inject.Inject

class CouponsDataMapperImpl @Inject constructor() {

    fun mapCoupons(
        couponsListProto: List<CouponModelProtos.CouponsByStatus>
    ): List<CouponsByGroupModel> {
        val result = mutableListOf<CouponsByGroupModel>()
        couponsListProto.forEach { couponsByStatusProto ->
            val coupons = couponsByStatusProto.couponsList.map { couponProto ->
                CouponModel(
                    id = couponProto.encryptedId,
                    avatar = couponProto.imageUrl,
                    name = couponProto.name,
                    description = couponProto.description,
                    discount = couponProto.discount,
                    priceWithDiscount = couponProto.priceWithDiscount,
                    priceWithoutDiscount = couponProto.priceWithoutDiscount
                )
            }
            result.add(
                CouponsByGroupModel(
                    statusCoupon = mapStatusCoupon(couponsByStatusProto.status),
                    coupons = coupons
                )
            )
        }
        return result
    }

    fun mapCouponsDescription(
        couponsListProto: List<CouponDescriptionModelProtos.CouponDescriptionByStatus>
    ) : List<CouponsByGroupModel> {
        val result = mutableListOf<CouponsByGroupModel>()
        couponsListProto.forEach { couponDescriptionByStatus ->
            val coupons = couponDescriptionByStatus.couponDescriptionList.map { couponProto ->
                CouponModel(
                    id = couponProto.encryptedId,
                    avatar = couponProto.imageUrl,
                    name = couponProto.name,
                    description = couponProto.description,
                    discount = couponProto.discount,
                    priceWithDiscount = couponProto.priceWithDiscount,
                    priceWithoutDiscount = couponProto.priceWithoutDiscount
                )
            }
            result.add(
                CouponsByGroupModel(
                    statusCoupon = mapStatusCoupon(couponDescriptionByStatus.status),
                    coupons = coupons
                )
            )
        }
        return result
    }

    private fun mapStatusCoupon(status: CouponDescriptionModelProtos.Status): StatusCoupon {
        return when(status) {
            CouponDescriptionModelProtos.Status.ACTIVE -> StatusCoupon.ACTIVE
            CouponDescriptionModelProtos.Status.EXPIRED -> StatusCoupon.EXPIRED
            CouponDescriptionModelProtos.Status.UNRECOGNIZED -> StatusCoupon.UNRECOGNIZED
            CouponDescriptionModelProtos.Status.IN_REVIEW -> StatusCoupon.IN_REVIEW
            CouponDescriptionModelProtos.Status.USED -> StatusCoupon.USED
            CouponDescriptionModelProtos.Status.RESERVED -> StatusCoupon.RESERVED
            CouponDescriptionModelProtos.Status.CANCELED -> StatusCoupon.CANCELED
        }
    }

    private fun mapStatusCoupon(status: CouponModelProtos.Status): StatusCoupon {
        return when(status) {
            CouponModelProtos.Status.ACTIVE -> StatusCoupon.ACTIVE
            CouponModelProtos.Status.USED -> StatusCoupon.USED
            CouponModelProtos.Status.EXPIRED -> StatusCoupon.EXPIRED
            CouponModelProtos.Status.UNRECOGNIZED -> StatusCoupon.UNRECOGNIZED
            CouponModelProtos.Status.RESERVED -> StatusCoupon.RESERVED
        }
    }
}