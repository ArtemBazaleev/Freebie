package com.freebie.frieebiemobile.ui.rate.presentation.model

data class UserRateUiModel(
    val id: String,
    val reviewerName: String,
    val comment: String,
    val avatar: String,
    val reviewerRating: Float?,
    val date: String,
    val needToShowFullText: Boolean = false,
    val needToShowRating: Boolean = false
): RateUIModel {
    override fun getItemViewType(): RateItemViewType {
        return RateItemViewType.USER_REVIEW
    }
}