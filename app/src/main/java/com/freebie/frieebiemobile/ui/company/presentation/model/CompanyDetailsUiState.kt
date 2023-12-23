package com.freebie.frieebiemobile.ui.company.presentation.model

import com.freebie.frieebiemobile.ui.feed.models.CouponUI
import com.freebie.frieebiemobile.ui.feed.models.OfferUI

data class CompanyDetailsUiState(
    val companyId: String,
    val avatar: String,
    val description: String,
    val name: String,
    val coupons: List<CouponUI> = emptyList(),
    val booklets: List<OfferUI> = emptyList()
)

val EMPTY_COMPANY_UI_STATE = CompanyDetailsUiState(
    companyId = "",
    avatar = "",
    description = "",
    name = ""
)