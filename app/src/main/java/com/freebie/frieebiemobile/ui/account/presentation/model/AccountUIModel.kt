package com.freebie.frieebiemobile.ui.account.presentation.model

sealed interface AccountUIModel{
    fun getItemType(): AccountType
}

enum class AccountType(val intValue: Int) {
    HEADER(0),
    DESCRIPTION(1),
    BUTTON(2),
    COUPON_GROUPS(3),
    COUPONS(4),
    COMPANY(5)
}
