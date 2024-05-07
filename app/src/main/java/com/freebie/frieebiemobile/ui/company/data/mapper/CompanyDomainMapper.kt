package com.freebie.frieebiemobile.ui.company.data.mapper

import com.freebie.frieebiemobile.ui.account.data.mapper.CouponsDataMapperImpl
import com.freebie.frieebiemobile.ui.company.domain.model.CompanyEditModel
import com.freebie.frieebiemobile.ui.company.domain.model.CompanyModel
import com.freebie.frieebiemobile.ui.company.domain.model.ExternalCompanyLink
import com.freebie.frieebiemobile.ui.company.domain.model.ExternalLinkType
import com.freebie.frieebiemobile.ui.company.domain.model.Locale
import com.freebie.frieebiemobile.ui.company.domain.model.RatingInfoModel
import com.freebie.frieebiemobile.ui.rate.domain.RateModel
import com.freebie.protos.CompanyApiProtos
import com.freebie.protos.CompanyModelProtos.Link
import com.freebie.protos.CompanyModelProtos.LinkType
import com.freebie.protos.RatingModelProtos
import javax.inject.Inject

class CompanyDomainMapper @Inject constructor(
    private val couponMapper: CouponsDataMapperImpl,
    private val bookletMapper: BookletMapperImpl
) {
    fun map(companyResponse: CompanyApiProtos.CompanyDataResponse): CompanyModel {
        return CompanyModel(
            id = companyResponse.data.company.encryptedId,
            name = companyResponse.data.company.name,
            avatar = companyResponse.data.company.avatarUrl,
            description = companyResponse.data.company.description,
            rating = mapRating(companyResponse.data.company.rating),
            creatorId = companyResponse.data.company.creatorId,
            coupons = couponMapper.mapCouponsDescription(companyResponse.data.couponDescriptionList),
            booklets = bookletMapper.mapBooklet(companyResponse.data.bookletList),
            linksList = mapLinksList(companyResponse.data.company.linksList),
            rateList = mapRateComments(companyResponse.data.company.rating)
        )
    }

    private fun mapRateComments(rating: RatingModelProtos.Rating): List<RateModel> {
        return rating.reviewsList.map { rate ->
            RateModel(
                id = rate.id,
                reviewerName = rate.reviewerName,
                comment = rate.message,
                avatar = rate.reviewerPicture,
                date = rate.createdAt,
                reviewerRating = rate.score.toFloat()
            )
        }
    }

    private fun mapRating(rating: RatingModelProtos.Rating): RatingInfoModel {
        return RatingInfoModel(
            ratingScore = rating.score,
            canRate = rating.isAllowedToReview,
            reviewCount = rating.totalReviews.toInt()
        )
    }

    private fun mapLinksList(links: List<Link>): List<ExternalCompanyLink> {
        val result = mutableListOf<ExternalCompanyLink>()
        links.forEach { link ->
            when (link.type) {
                LinkType.WHATSAPP -> ExternalLinkType.WHATSAPP
                LinkType.INSTAGRAM -> ExternalLinkType.INSTAGRAM
                LinkType.TELEGRAM -> ExternalLinkType.TELEGRAM
                else -> null
            }?.let { nonNullType ->
                result.add(ExternalCompanyLink(link.url, nonNullType))
            }
        }
        return result
    }

    fun mapCompanyEditInfo(
        proto: CompanyApiProtos.CompanyEditResponse,
        companyId: String
    ): CompanyEditModel {
        return CompanyEditModel(
            categoryId = proto.categoryId,
            companyId = companyId,
            avatar = proto.avatarUrl,
            cityId = proto.city,
            locale = proto.localesList.map { locale ->
                Locale(
                    langCode = locale.locale,
                    description = locale.description,
                    name = locale.name
                )
            },
            links = mapLinksList(proto.linksList),
        )
    }
}