package com.freebie.frieebiemobile.ui.company.presentation.model

import com.freebie.frieebiemobile.ui.feed.models.CouponUI
import com.freebie.frieebiemobile.ui.feed.models.OfferUI
import com.freebie.frieebiemobile.ui.rate.presentation.model.RateUiModel

data class CompanyDetailsUiState(
    val companyId: String,
    val avatar: String,
    val description: String,
    val name: String,
    val rating: Double,
    val showMoreComment: Boolean,
    val canRate: Boolean,
    val coupons: List<CouponUI> = emptyList(),
    val booklets: List<OfferUI> = emptyList(),
    val externalLinks: List<ExternalLinkUiModel> = emptyList(),
    val rateList: List<RateUiModel> = emptyList()
)

val EMPTY_COMPANY_UI_STATE = CompanyDetailsUiState(
    companyId = "",
    avatar = "",
    description = "",
    name = "",
    rating = 0.0,
    showMoreComment = false,
    canRate = false
)