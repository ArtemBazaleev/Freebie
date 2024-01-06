package com.freebie.frieebiemobile.ui.rate.domain

class RateModel(
    val id: String,
    val reviewerName: String,
    val comment: String,
    val reviewerRating: Float?,
    val avatar: String,
    val date: Long,
    val reply: ReplyCompanyModel? = null
)

class ReplyCompanyModel(
    val id: String,
    val message: String,
    val date: Long
)