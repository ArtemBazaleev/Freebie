package com.freebie.frieebiemobile.ui.company.presentation.model

import com.freebie.frieebiemobile.ui.feed.models.CouponAdapterUiModel
import com.freebie.frieebiemobile.ui.feed.models.CouponUI
import com.freebie.frieebiemobile.ui.feed.models.OfferUI
import com.freebie.frieebiemobile.ui.rate.presentation.model.UserRateUiModel

data class CompanyDetailsUiState(
    val companyId: String,
    val avatar: String,
    val description: String,
    val name: String,
    val rating: Double,
    val showMoreComment: Boolean,
    val canRate: Boolean,
    val canModerate: Boolean,
    val coupons: List<CouponAdapterUiModel> = emptyList(),
    val booklets: List<OfferUI> = emptyList(),
    val externalLinks: List<ExternalLinkUiModel> = emptyList(),
    val rateList: List<UserRateUiModel> = emptyList()
)

val EMPTY_COMPANY_UI_STATE = CompanyDetailsUiState(
    companyId = "",
    avatar = "",
    description = "",
    name = "",
    rating = 0.0,
    showMoreComment = false,
    canRate = false,
    canModerate = false
)