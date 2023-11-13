package com.freebie.frieebiemobile.ui.account.data.mapper

import com.freebie.frieebiemobile.ui.account.domain.model.AccountInfoModel
import com.freebie.frieebiemobile.ui.account.domain.model.CompaniesByGroupModel
import com.freebie.frieebiemobile.ui.account.domain.model.CompanyExtendedModel
import com.freebie.frieebiemobile.ui.account.domain.model.CouponsByGroupModel
import com.freebie.frieebiemobile.ui.account.domain.model.StatusCompany
import com.freebie.frieebiemobile.ui.account.domain.model.StatusCoupon
import com.freebie.frieebiemobile.ui.feed.domain.CouponModel
import com.freebie.protos.CompanyModelProtos
import com.freebie.protos.CompanyModelProtos.CompanyStatus
import com.freebie.protos.CouponModelProtos
import com.freebie.protos.UserProfileApiProtos.AccountDataResponse
import javax.inject.Inject

class AccountDataMapperImpl @Inject constructor() {
    fun map(proto: AccountDataResponse): AccountInfoModel {
        return AccountInfoModel(
            balance = proto.data.balance,
            companies = mapCompany(proto.data.companiesList),
            coupons = mapCoupons(proto.data.couponsList)
        )
    }

    private fun mapCoupons(
        couponsListProto: MutableList<CouponModelProtos.CouponsByStatus>
    ): List<CouponsByGroupModel> {
        val result = mutableListOf<CouponsByGroupModel>()
        couponsListProto.forEach { couponsByStatusProto ->
            val coupons = couponsByStatusProto.couponsList.map { couponProto ->
                CouponModel(
                    id = couponProto.encryptedId,
                    avatar = couponProto.imageUrl,
                    name = couponProto.name,
                    description = couponProto.description,
                    discount = couponProto.discount,
                    priceWithDiscount = couponProto.priceWithDiscount,
                    priceWithoutDiscount = couponProto.priceWithoutDiscount
                )
            }
            result.add(
                CouponsByGroupModel(
                    statusCoupon = mapStatusCoupon(couponsByStatusProto.status),
                    coupons = coupons
                )
            )
        }
        return result
    }

    private fun mapCompany(
        companiesList: MutableList<CompanyModelProtos.CompanyByStatus>
    ): List<CompaniesByGroupModel> {
        val result = mutableListOf<CompaniesByGroupModel>()
        companiesList.forEach { companyByGroupProto ->
            val companies = companyByGroupProto.companyList.map { companyProto ->
                CompanyExtendedModel(
                    encryptedId = companyProto.encryptedId,
                    creatorId = companyProto.creatorId,
                    categoryId = companyProto.categoryId,
                    avatarUrl = companyProto.avatarUrl,
                    name = companyProto.name,
                    description = companyProto.description,
                    createdAt = companyProto.createdAt,
                    updatedAt = companyProto.updatedAt,
                    statusCompany = mapStatus(companyProto.status)
                )
            }
            result.add(
                CompaniesByGroupModel(
                    statusCompany = mapStatus(companyByGroupProto.status),
                    companies = companies
                )
            )
        }
        return result
    }

    private fun mapStatus(status: CompanyStatus): StatusCompany {
        return when(status) {
            CompanyStatus.IN_REVIEW -> StatusCompany.IN_REVIEW
            CompanyStatus.ACTIVE -> StatusCompany.ACTIVE
            CompanyStatus.DISABLED -> StatusCompany.DISABLED
            CompanyStatus.UNRECOGNIZED -> StatusCompany.UNRECOGNIZED
        }
    }

    private fun mapStatusCoupon(status: CouponModelProtos.Status): StatusCoupon {
        return when(status) {
            CouponModelProtos.Status.ACTIVE -> StatusCoupon.ACTIVE
            CouponModelProtos.Status.USED -> StatusCoupon.USED
            CouponModelProtos.Status.EXPIRED -> StatusCoupon.EXPIRED
            CouponModelProtos.Status.UNRECOGNIZED -> StatusCoupon.UNRECOGNIZED
        }
    }
}