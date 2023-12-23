package com.freebie.frieebiemobile.ui.account.presentation

import com.freebie.frieebiemobile.ui.feed.domain.CouponModel
import com.freebie.frieebiemobile.ui.feed.models.CouponUI
import javax.inject.Inject

class CouponUiMapper @Inject constructor() {
    fun mapCoupon(model: CouponModel): CouponUI = CouponUI(
        id = model.id,
        avatar = model.avatar,
        name = model.name ?: "",
        description = model.description ?: "",
        discount = model.discount ?: "",
        priceWithDiscount = model.priceWithDiscount,
        price = model.priceWithoutDiscount
    )
}