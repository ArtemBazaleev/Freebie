package com.freebie.frieebiemobile.ui.account.domain.model

class CompanyExtendedModel (
    val encryptedId: String,
    val creatorId: String,
    val categoryId: String,
    val avatarUrl: String,
    val name: String,
    val description: String,
    val createdAt: Long,
    val updatedAt: Long,
    val statusCompany: StatusCompany
)

enum class StatusCompany {
    IN_REVIEW,
    ACTIVE,
    DISABLED,
    UNRECOGNIZED
}