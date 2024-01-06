package com.freebie.frieebiemobile.ui.company.presentation.mapper

import com.freebie.frieebiemobile.ui.account.domain.model.StatusCoupon
import com.freebie.frieebiemobile.ui.account.presentation.mappers.CouponUiMapper
import com.freebie.frieebiemobile.ui.company.domain.model.CompanyModel
import com.freebie.frieebiemobile.ui.company.domain.model.ExternalCompanyLink
import com.freebie.frieebiemobile.ui.company.domain.model.ExternalLinkType
import com.freebie.frieebiemobile.ui.company.presentation.model.CompanyDetailsUiState
import com.freebie.frieebiemobile.ui.company.presentation.model.ExternalLinkUiModel
import com.freebie.frieebiemobile.ui.company.presentation.model.LinkUiType
import com.freebie.frieebiemobile.ui.feed.domain.BookletStatus
import com.freebie.frieebiemobile.ui.feed.models.OfferUI
import com.freebie.frieebiemobile.ui.rate.presentation.mapper.RateUiMapper
import javax.inject.Inject

class CompanyUiMapper @Inject constructor(
    private val couponUiMapper: CouponUiMapper,
    private val rateUiMapper: RateUiMapper
) {

    fun map(companyModel: CompanyModel): CompanyDetailsUiState {
        val couponGroup = companyModel.coupons.findLast { it.statusCoupon == StatusCoupon.ACTIVE }
        val bookletGroup = companyModel.booklets.findLast { it.status == BookletStatus.ACTIVE }
        return CompanyDetailsUiState(
            name = companyModel.name,
            companyId = companyModel.id,
            avatar = companyModel.avatar,
            description = companyModel.description,
            rating = companyModel.rating.ratingScore,
            coupons = couponGroup?.coupons?.map { couponModel ->
                couponUiMapper.mapCoupon(couponModel)
            } ?: emptyList(),
            booklets = bookletGroup?.booklets?.map {
                OfferUI(it.id, it.avatar)
            } ?: emptyList(),
            externalLinks = mapExternalLinks(companyModel.linksList),
            rateList = companyModel.rateList.map(rateUiMapper::mapToUserRate),
            showMoreComment = companyModel.rating.reviewCount > companyModel.rateList.size,
            canRate = companyModel.rating.canRate
        )
    }

    private fun mapExternalLinks(linksList: List<ExternalCompanyLink>): List<ExternalLinkUiModel> {
        return mutableListOf<ExternalLinkUiModel>().apply {
            linksList.forEach { link ->
                add(
                    ExternalLinkUiModel(
                        url = link.url,
                        linkType = when (link.type) {
                            ExternalLinkType.WHATSAPP -> LinkUiType.WhatsApp
                            ExternalLinkType.INSTAGRAM -> LinkUiType.Instagram
                            ExternalLinkType.TELEGRAM -> LinkUiType.Telegram
                        }
                    )
                )
            }
        }
    }


}