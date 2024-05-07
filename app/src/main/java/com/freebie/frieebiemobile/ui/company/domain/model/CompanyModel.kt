package com.freebie.frieebiemobile.ui.company.domain.model

import com.freebie.frieebiemobile.ui.account.domain.model.CouponsByGroupModel
import com.freebie.frieebiemobile.ui.feed.domain.BookletByStatus
import com.freebie.frieebiemobile.ui.rate.domain.RateModel

class CompanyModel(
    val id: String,
    val name: String,
    val avatar: String,
    val description: String,
    val rating: RatingInfoModel,
    val creatorId: String,
    val coupons: List<CouponsByGroupModel>,
    val booklets: List<BookletByStatus>,
    val linksList: List<ExternalCompanyLink>,
    val rateList: List<RateModel>
)

data class CompanyEditModel(
    val companyId: String,
    val categoryId: String,
    val categoryName: String = "",
    val avatar: String?,
    val locale: List<Locale>,
    val links: List<ExternalCompanyLink>,
    val cityId: String,
    val cityName: String = ""
)

class RatingInfoModel(
    val ratingScore: Double,
    val canRate: Boolean,
    val reviewCount: Int
)

class ExternalCompanyLink(
    val url: String,
    val type: ExternalLinkType
)

enum class ExternalLinkType {
    WHATSAPP,
    INSTAGRAM,
    TELEGRAM,
}