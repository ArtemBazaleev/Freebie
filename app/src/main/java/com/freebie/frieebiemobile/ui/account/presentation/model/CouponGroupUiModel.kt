package com.freebie.frieebiemobile.ui.account.presentation.model

import androidx.annotation.StringRes

class CouponGroupsUIModel(
    val groups: List<CouponGroupUiModel>
): AccountUIModel {
    override fun getItemType() = AccountType.COUPON_GROUPS
}


data class CouponGroupUiModel(
    val groupId: Int,
    @StringRes val groupTitle: Int,
    val isActive: Boolean
)