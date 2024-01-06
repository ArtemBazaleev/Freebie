package com.freebie.frieebiemobile.ui.rate.presentation.model

data class RateCompanyResponseUIModel(
    val id: String,
    val responseToCommentId: String,
    val comment: String,
    val date: String
): RateUIModel {
    override fun getItemViewType(): RateItemViewType {
        return RateItemViewType.COMPANY_RESPONSE
    }
}
