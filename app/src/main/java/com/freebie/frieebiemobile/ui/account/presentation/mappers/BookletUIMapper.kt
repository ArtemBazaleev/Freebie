package com.freebie.frieebiemobile.ui.account.presentation.mappers

import com.freebie.frieebiemobile.ui.feed.domain.BookletModel
import com.freebie.frieebiemobile.ui.feed.models.OfferUI
import javax.inject.Inject

class BookletUIMapper @Inject constructor() {
    fun map(model: BookletModel): OfferUI {
        return OfferUI(
            id = model.id,
            avatar = model.avatar
        )
    }
}