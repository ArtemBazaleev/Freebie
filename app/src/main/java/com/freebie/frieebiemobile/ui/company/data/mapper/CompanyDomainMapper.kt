package com.freebie.frieebiemobile.ui.company.data.mapper

import com.freebie.frieebiemobile.ui.account.data.mapper.CouponsDataMapperImpl
import com.freebie.frieebiemobile.ui.company.domain.model.CompanyModel
import com.freebie.protos.CompanyApiProtos
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
            booklets = bookletMapper.mapBooklet(companyResponse.data.bookletList)
        )
    }
}