package com.freebie.frieebiemobile.ui.rate.data.mapper

import com.freebie.frieebiemobile.ui.rate.domain.RateModel
import com.freebie.frieebiemobile.ui.rate.domain.ReplyCompanyModel
import com.freebie.protos.ReviewModelProtos
import javax.inject.Inject

class ReviewModelMapper @Inject constructor() {
    fun mapReview(reviewModel: ReviewModelProtos.Review): RateModel {
        return RateModel(
            id = reviewModel.id,
            reviewerName = reviewModel.reviewerName,
            comment = reviewModel.message,
            reviewerRating = reviewModel.score.toFloat(),
            avatar = reviewModel.reviewerPicture,
            date = reviewModel.createdAt,
            reply = mapReply(reviewModel.comment)
        )
    }

    private fun mapReply(comment: ReviewModelProtos.Reply?): ReplyCompanyModel? {
        if (comment == null || comment.id.isNullOrEmpty()) return null
        return ReplyCompanyModel(
            id = comment.id,
            message = comment.message,
            date = comment.createdAt
        )
    }
}