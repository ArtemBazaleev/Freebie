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
import com.freebie.frieebiemobile.ui.feed.models.AddOfferUiModel
import com.freebie.frieebiemobile.ui.feed.models.CouponAdapterUiModel
import com.freebie.frieebiemobile.ui.feed.models.CreateCoupon
import com.freebie.frieebiemobile.ui.feed.models.OfferAdapterUiModel
import com.freebie.frieebiemobile.ui.feed.models.OfferUI
import com.freebie.frieebiemobile.ui.rate.presentation.mapper.RateUiMapper
import javax.inject.Inject

class CompanyUiMapper @Inject constructor(
    private val couponUiMapper: CouponUiMapper,
    private val rateUiMapper: RateUiMapper
) {

    fun map(companyModel: CompanyModel, myId: String?): CompanyDetailsUiState {
        val isAdmin = myId == companyModel.creatorId
        return CompanyDetailsUiState(
            name = companyModel.name,
            companyId = companyModel.id,
            avatar = companyModel.avatar,
            description = companyModel.description,
            rating = companyModel.rating.ratingScore,
            coupons = getCoupons(companyModel, isAdmin),
            booklets = getBooklets(companyModel, isAdmin),
            externalLinks = mapExternalLinks(companyModel.linksList),
            rateList = companyModel.rateList.map(rateUiMapper::mapToUserRate),
            showMoreComment = companyModel.rating.reviewCount > companyModel.rateList.size,
            canRate = companyModel.rating.canRate,
            canModerate = isAdmin
        )
    }

    private fun getBooklets(companyModel: CompanyModel, isAdmin: Boolean): List<OfferAdapterUiModel> {
        val result = mutableListOf<OfferAdapterUiModel>()
        if (isAdmin) {
            result.add(AddOfferUiModel())
        } //TODO add group paging
        val bookletGroup = companyModel.booklets.findLast { it.status == BookletStatus.ACTIVE }
        val booklets = bookletGroup?.booklets?.map { OfferUI(it.id, it.avatar) } ?: emptyList()
        result.addAll(booklets)
        return result
    }

    private fun getCoupons(companyModel: CompanyModel, isAdmin: Boolean): List<CouponAdapterUiModel> {
        val result = mutableListOf<CouponAdapterUiModel>()

//        val couponGroups: List<CouponsByGroupModel> = if (isAdmin) { //TODO как пагинировать разные купоны ?
//            companyModel.coupons
//        } else {
//            val couponsByGroupActive = companyModel.coupons.findLast {
//                it.statusCoupon == StatusCoupon.ACTIVE
//            }
//            mutableListOf<CouponsByGroupModel>().apply { couponsByGroupActive?.let(::add) }
//        }
        if (isAdmin) {
            result.add(CreateCoupon())
        }
        val activeCoupons = companyModel.coupons.findLast {
            it.statusCoupon == StatusCoupon.ACTIVE
        }?.coupons?.map { couponUiMapper.mapCoupon(it) } ?: emptyList()
        result.addAll(activeCoupons)
        return result
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