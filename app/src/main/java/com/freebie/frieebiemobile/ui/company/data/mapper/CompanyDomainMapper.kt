package com.freebie.frieebiemobile.ui.company.data.mapper

import com.freebie.frieebiemobile.ui.account.data.mapper.CouponsDataMapperImpl
import com.freebie.frieebiemobile.ui.company.domain.model.CompanyModel
import com.freebie.frieebiemobile.ui.company.domain.model.ExternalCompanyLink
import com.freebie.frieebiemobile.ui.company.domain.model.ExternalLinkType
import com.freebie.protos.CompanyApiProtos
import com.freebie.protos.CompanyModelProtos.Link
import com.freebie.protos.CompanyModelProtos.LinkType
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
            coupons = couponMapper.mapCouponsDescription(companyResponse.data.couponDescriptionList),
            booklets = bookletMapper.mapBooklet(companyResponse.data.bookletList),
            linksList = mapLinksList(companyResponse.data.company.linksList)
        )
    }

    private fun mapLinksList(links: List<Link>): List<ExternalCompanyLink> {
        val result = mutableListOf<ExternalCompanyLink>()
        links.forEach { link ->
            when(link.type) {
                LinkType.WHATSUP -> ExternalLinkType.WHATSAPP
                LinkType.INSTAGRAM -> ExternalLinkType.INSTAGRAM
                LinkType.TELEGRAM -> ExternalLinkType.TELEGRAM
                else -> null
            } ?.let { nonNullType ->
                result.add(ExternalCompanyLink(link.url, nonNullType))
            }
        }
        return result
    }
}