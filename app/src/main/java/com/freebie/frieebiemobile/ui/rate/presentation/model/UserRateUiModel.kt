package com.freebie.frieebiemobile.ui.rate.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class UserRateUiModel(
    val id: String,
    val reviewerName: String,
    val comment: String,
    val avatar: String,
    val reviewerRating: Float?,
    val date: String,
    val needToShowFullText: Boolean = false,
    val needToShowRating: Boolean = false,
    val canReply: Boolean = false
): RateUIModel, Parcelable {
    override fun getItemViewType(): RateItemViewType {
        return RateItemViewType.USER_REVIEW
    }
}