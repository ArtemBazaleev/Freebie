package com.freebie.frieebiemobile.ui.rate.presentation.mapper

import com.freebie.frieebiemobile.ui.rate.domain.RateModel
import com.freebie.frieebiemobile.ui.rate.presentation.model.RateCompanyResponseUIModel
import com.freebie.frieebiemobile.ui.rate.presentation.model.RateUIModel
import com.freebie.frieebiemobile.ui.rate.presentation.model.UserRateUiModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class RateUiMapper @Inject constructor() {

    private val sdf by lazy { SimpleDateFormat("dd/MM/yy", Locale.getDefault()) }

    fun mapToUserRate(
        model: RateModel,
        showFullText: Boolean = false,
        showRating: Boolean = true
    ): UserRateUiModel {
        return UserRateUiModel(
            id = model.id,
            avatar = model.avatar,
            comment = model.comment,
            reviewerRating = model.reviewerRating,
            reviewerName = model.reviewerName,
            needToShowRating = showRating,
            date = sdf.format(Date(model.date)),
            needToShowFullText = showFullText
        )
    }

    fun mapToUserAndCompanyReply(
        model: RateModel,
        showFullText: Boolean = true,
        showRating: Boolean = true,
        canModerate: Boolean
    ): List<RateUIModel> {
        return mutableListOf<RateUIModel>().apply {
            add(UserRateUiModel(
                id = model.id,
                comment = model.comment,
                reviewerRating = model.reviewerRating,
                reviewerName = model.reviewerName,
                needToShowRating = showRating,
                avatar = model.avatar,
                date = sdf.format(Date(model.date)),
                needToShowFullText = showFullText,
                canReply = model.reply == null && canModerate
            ))
            if (model.reply != null) {
                add(RateCompanyResponseUIModel(
                    id = model.reply.id,
                    comment = model.reply.message,
                    responseToCommentId = model.id,
                    date = sdf.format(Date(model.reply.date)),
                ))
            }
        }
    }
}