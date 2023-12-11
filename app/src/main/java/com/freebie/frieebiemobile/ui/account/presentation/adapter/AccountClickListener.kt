package com.freebie.frieebiemobile.ui.account.presentation.adapter

import com.freebie.frieebiemobile.ui.account.presentation.model.AccountActionButtonUIModel
import com.freebie.frieebiemobile.ui.account.presentation.model.CouponGroupUiModel

interface AccountClickListener {
    fun actionButtonClick(model: AccountActionButtonUIModel)
    fun couponGroupClick(group: CouponGroupUiModel)
}