package com.freebie.frieebiemobile.ui.feed.models

import com.freebie.frieebiemobile.R

sealed interface OfferAdapterUiModel {
    val layoutId: Int
}

data class OfferUI(
    val id: String,
    val avatar: String,
    override val layoutId: Int = R.layout.item_offer
): OfferAdapterUiModel

data class AddOfferUiModel(
    override val layoutId: Int = R.layout.item_add_offer
): OfferAdapterUiModel