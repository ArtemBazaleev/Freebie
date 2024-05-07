package com.freebie.frieebiemobile.ui.coupon.domain

class CouponDescriptionModel(
    val id: String,
    val description: String,
    val avatar: String,
    val createdAt: Long,
    val expiredAt: Long,
    val availableAmount: Int,
    val companyId: String,
    val name: String,
    val priceOfCoupon: Double,
    val priceWithDiscount: Double,
    val priceWithoutDiscount: Double,
    val discount: String,
    val status: CouponStatus
)

enum class CouponStatus(val statusInt: Int){
    IN_REVIEW(0),
    ACTIVE(1),
    EXPIRED(2),
    USED(3),
    CANCELED(4),
    RESERVED(5),
    UNRECOGNIZED(-1),
    HIDDEN(6);
}