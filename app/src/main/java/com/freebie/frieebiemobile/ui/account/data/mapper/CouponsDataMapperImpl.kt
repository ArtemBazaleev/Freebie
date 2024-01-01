package com.freebie.frieebiemobile.ui.account.data.mapper

import com.freebie.frieebiemobile.ui.account.domain.model.CouponsByGroupModel
import com.freebie.frieebiemobile.ui.account.domain.model.StatusCoupon
import com.freebie.frieebiemobile.ui.feed.domain.CouponModel
import com.freebie.protos.CouponDescriptionModelProtos
import com.freebie.protos.PurchaseEnumsProtos
import com.freebie.protos.PurchaseModelProtos
import javax.inject.Inject

class CouponsDataMapperImpl @Inject constructor() {

    fun mapPurchasesToCoupons(
        purchasesListProto: List<PurchaseModelProtos.PurchaseByStatus>
    ): List<CouponsByGroupModel> {
        val result = mutableListOf<CouponsByGroupModel>()
        purchasesListProto.forEach { purchaseByStatusProto ->
            val coupons = purchaseByStatusProto.purchaseList.map { purchase ->
                CouponModel(
                    id = purchase.purchaseItem.encryptedId,
                    avatar = purchase.purchaseItem.imageUrl,
                    name = purchase.purchaseItem.name,
                    description = purchase.purchaseItem.description,
                    discount = purchase.purchaseItem.discount,
                    priceWithDiscount = purchase.purchaseItem.priceWithDiscount,
                    priceWithoutDiscount = purchase.purchaseItem.priceWithoutDiscount
                )
            }
            result.add(
                CouponsByGroupModel(
                    statusCoupon = mapPurchaseStatus(purchaseByStatusProto.status),
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
        }
    }

    private fun mapPurchaseStatus(status: PurchaseEnumsProtos.PurchaseStatus): StatusCoupon {
        return when(status) {
            PurchaseEnumsProtos.PurchaseStatus.ACTIVE -> StatusCoupon.ACTIVE
            PurchaseEnumsProtos.PurchaseStatus.USED -> StatusCoupon.USED
            PurchaseEnumsProtos.PurchaseStatus.EXPIRED -> StatusCoupon.EXPIRED
            PurchaseEnumsProtos.PurchaseStatus.UNRECOGNIZED -> StatusCoupon.UNRECOGNIZED
            PurchaseEnumsProtos.PurchaseStatus.RESERVED -> StatusCoupon.RESERVED
            PurchaseEnumsProtos.PurchaseStatus.CANCELED -> StatusCoupon.CANCELED
        }
    }
}