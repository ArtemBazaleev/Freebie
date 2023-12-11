package com.freebie.frieebiemobile.ui.coupon.presentation.mapper

import android.content.Context
import com.freebie.frieebiemobile.R
import com.freebie.frieebiemobile.ui.coupon.domain.CouponDescriptionModel
import com.freebie.frieebiemobile.ui.coupon.presentation.model.CouponDetailsUiModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class CouponDetailsUIMapper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun map(model: CouponDescriptionModel): CouponDetailsUiModel {
        val currencySign = "€"
        return CouponDetailsUiModel(
            title = model.name,
            imageUrl = model.avatar,
            description = model.description,
            priceWithoutDiscount = "$currencySign ${model.priceWithoutDiscount}",
            priceWithDiscount = "$currencySign ${model.priceWithDiscount}",
            couponPriceText = context.getString(
                R.string.get_coupon_action_btn_text,
                "$currencySign ${model.priceOfCoupon}"
            )
        )
    }
}