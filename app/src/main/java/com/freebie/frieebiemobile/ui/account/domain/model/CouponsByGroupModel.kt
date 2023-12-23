package com.freebie.frieebiemobile.ui.account.domain.model

import com.freebie.frieebiemobile.ui.feed.domain.CouponModel

class CouponsByGroupModel (
    val statusCoupon: StatusCoupon,
    val coupons: List<CouponModel>
)

enum class StatusCoupon(val intValue: Int){
    ACTIVE(0),
    USED(1),
    EXPIRED(2),
    UNRECOGNIZED(3),
    RESERVED(4),
    IN_REVIEW(5)
}