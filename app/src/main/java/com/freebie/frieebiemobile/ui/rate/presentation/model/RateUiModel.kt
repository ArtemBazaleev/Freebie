package com.freebie.frieebiemobile.ui.rate.presentation.model

sealed interface RateUIModel {
    fun getItemViewType(): RateItemViewType
}

enum class RateItemViewType(val intValue: Int) {
    COMPANY_RESPONSE(1), USER_REVIEW(0)
}