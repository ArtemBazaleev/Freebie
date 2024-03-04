package com.freebie.frieebiemobile.ui.company.domain.model

class CompanyCreationParams(
    val categoryId: String,
    val avatar: String?,
    val locale: List<Locale>,
    val links: List<ExternalCompanyLink>,
    val city: String
)

class Locale(
    val langCode: String,
    val description: String,
    val name: String
)