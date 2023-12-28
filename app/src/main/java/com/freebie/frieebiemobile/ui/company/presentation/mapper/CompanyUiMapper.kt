package com.freebie.frieebiemobile.ui.company.presentation.mapper

import com.freebie.frieebiemobile.ui.account.domain.model.StatusCoupon
import com.freebie.frieebiemobile.ui.account.presentation.mappers.CouponUiMapper
import com.freebie.frieebiemobile.ui.company.domain.model.CompanyModel
import com.freebie.frieebiemobile.ui.company.presentation.model.CompanyDetailsUiState
import com.freebie.frieebiemobile.ui.feed.domain.BookletStatus
import com.freebie.frieebiemobile.ui.feed.models.OfferUI
import javax.inject.Inject

class CompanyUiMapper @Inject constructor(
    private val couponUiMapper: CouponUiMapper
) {

    fun map(companyModel: CompanyModel) : CompanyDetailsUiState {
        val couponGroup = companyModel.coupons.findLast { it.statusCoupon == StatusCoupon.ACTIVE }
        val bookletGroup = companyModel.booklets.findLast { it.status == BookletStatus.ACTIVE }
        return CompanyDetailsUiState(
            name = companyModel.name,
            companyId = companyModel.id,
            avatar = companyModel.avatar,
            description = companyModel.description,
            coupons = couponGroup?.coupons?.map { couponModel ->
                couponUiMapper.mapCoupon(couponModel)
            } ?: emptyList(),
            booklets = bookletGroup?.booklets?.map {
                OfferUI(it.id, it.avatar)
            } ?: emptyList()
        )
    }



}