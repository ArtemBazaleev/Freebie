package com.freebie.frieebiemobile.ui.rate.domain

class RateModel(
    val id: String,
    val reviewerName: String,
    val comment: String,
    val reviewerRating: Float?,
    val reply: ReplyCompanyModel? = null
)

class ReplyCompanyModel(
    val message: String
)