package com.freebie.frieebiemobile.ui.account.presentation.model

import com.freebie.frieebiemobile.ui.feed.models.CouponUI

data class AccountCouponsUIModel(
    val coupons: List<CouponUI>
): AccountUIModel {
    override fun getItemType() = AccountType.COUPONS
}