package com.freebie.frieebiemobile.ui.rate.domain.usecase

interface ReplyCommentUseCase {
    suspend fun replyComment(message: String, commentId: String) : Result<Boolean>
}