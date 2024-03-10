package com.freebie.frieebiemobile.ui.feed.models

import com.freebie.frieebiemobile.R

sealed interface CouponAdapterUiModel {
    val layoutId: Int
}

data class CouponUI(
    val id: String,
    val avatar: String,
    val name: String,
    val description: String,
    val discount: String,
    val price: Double,
    val priceWithDiscount: Double,
    override val layoutId: Int = R.layout.item_coupon
) : CouponAdapterUiModel

data class CreateCoupon(
    val image: String = "https://freebie-bucket-stage.s3.il-central-1.amazonaws.com/other/Online_Shoping_27-min.png"
) : CouponAdapterUiModel {
    override val layoutId: Int
        get() = R.layout.item_add_coupon
}
