package com.freebie.frieebiemobile.ui.account.presentation.model

import com.freebie.frieebiemobile.ui.feed.models.CouponUI

data class AccountState (
    val ownProfile: UserUiModel?,
    val accountUI: List<AccountUIModel>,
    val isRefreshing: Boolean = false
//    val couponTypes: List<CouponGroupUiModel>,
//    val coupons: List<CouponUI>
)