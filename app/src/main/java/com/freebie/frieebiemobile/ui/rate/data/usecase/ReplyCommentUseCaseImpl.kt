package com.freebie.frieebiemobile.ui.rate.data.usecase

import com.freebie.frieebiemobile.ui.rate.data.repository.ReviewRepository
import com.freebie.frieebiemobile.ui.rate.domain.usecase.ReplyCommentUseCase
import javax.inject.Inject

class ReplyCommentUseCaseImpl @Inject constructor(
    private val repository: ReviewRepository
) : ReplyCommentUseCase {
    override suspend fun replyComment(message: String, commentId: String) =
        repository.replyOnComment(commentId, message)
}