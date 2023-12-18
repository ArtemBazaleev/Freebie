package com.freebie.frieebiemobile.ui.account.presentation.adapter

import com.freebie.frieebiemobile.ui.account.presentation.model.AccountActionButtonUIModel
import com.freebie.frieebiemobile.ui.account.presentation.model.CouponGroupUiModel
import com.freebie.frieebiemobile.ui.feed.models.CouponUI

interface AccountClickListener {
    fun actionButtonClick(model: AccountActionButtonUIModel)
    fun couponGroupClick(group: CouponGroupUiModel)
    fun onCouponClicked(coupon: CouponUI)
}