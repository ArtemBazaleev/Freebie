package com.freebie.frieebiemobile.ui.account.presentation

import android.content.Context
import com.freebie.frieebiemobile.R
import com.freebie.frieebiemobile.ui.account.domain.model.AccountInfoModel
import com.freebie.frieebiemobile.ui.account.domain.model.StatusCoupon
import com.freebie.frieebiemobile.ui.account.presentation.mappers.CouponUiMapper
import com.freebie.frieebiemobile.ui.account.presentation.model.AccountActionButtonUIModel
import com.freebie.frieebiemobile.ui.account.presentation.model.AccountCompanyUIModel
import com.freebie.frieebiemobile.ui.account.presentation.model.AccountCouponsUIModel
import com.freebie.frieebiemobile.ui.account.presentation.model.AccountDescUIModel
import com.freebie.frieebiemobile.ui.account.presentation.model.AccountHeaderUIModel
import com.freebie.frieebiemobile.ui.account.presentation.model.AccountUIModel
import com.freebie.frieebiemobile.ui.account.presentation.model.ButtonAction
import com.freebie.frieebiemobile.ui.account.presentation.model.CouponGroupUiModel
import com.freebie.frieebiemobile.ui.account.presentation.model.CouponGroupsUIModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AccountUIMapper @Inject constructor(
    @ApplicationContext private val context: Context,
    private val couponUiMapper: CouponUiMapper
) {

    fun mapAccountInfo(
        accountInfo: AccountInfoModel,
        isAuthorized: Boolean,
        selectedGroupId: Int? = null
    ): List<AccountUIModel> {
        val result = mutableListOf<AccountUIModel>()
        result.add(AccountHeaderUIModel("My coupons"))
        if (accountInfo.coupons.isEmpty()) {
            result.addAll(getEmptyCouponsSection())
        } else {
            val couponGroups = accountInfo.coupons.mapIndexed { index, group ->
                CouponGroupUiModel(
                    groupId = group.statusCoupon.intValue, groupTitle = when (group.statusCoupon) {
                        StatusCoupon.ACTIVE -> R.string.active
                        StatusCoupon.USED -> R.string.used
                        StatusCoupon.EXPIRED -> R.string.expired
                        StatusCoupon.UNRECOGNIZED -> R.string.unrecognized
                        StatusCoupon.RESERVED -> R.string.reserved
                        StatusCoupon.IN_REVIEW -> R.string.in_review
                        StatusCoupon.CANCELED -> R.string.canceled
                        StatusCoupon.HIDDEN -> R.string.hidden
                    }, isActive = if (selectedGroupId == null) {
                        index == 0
                    } else {
                        selectedGroupId == group.statusCoupon.intValue
                    }
                )
            }
            result.add(CouponGroupsUIModel(couponGroups))
            val coupons = if (selectedGroupId == null) {
                accountInfo.coupons.first()
            } else {
                accountInfo.coupons.firstOrNull { it.statusCoupon.intValue == selectedGroupId }
            }?.coupons ?: emptyList()

            result.add(AccountCouponsUIModel(coupons = coupons.map { couponModel ->
                couponUiMapper.mapCoupon(couponModel)
            }))
        }

        result.add(AccountHeaderUIModel("My companies"))//todo
        val companies = mutableListOf<AccountCompanyUIModel>()
        accountInfo.companies.forEach { companiesByGroupModel ->
            companies.addAll(companiesByGroupModel.companies.map { companyExtended ->
                AccountCompanyUIModel(
                    companyId = companyExtended.encryptedId,
                    companyAvatar = companyExtended.avatarUrl,
                    companyName = companyExtended.name
                )
            })
        }
        result.addAll(companies)
        if (accountInfo.companies.size <= 3) {
            result.addAll(getEmptyCompaniesSection(accountInfo.companies.isNotEmpty()))
        }

        if (!isAuthorized) {
            result.addAll(getAuthSection())
        }
        return result
    }

    private fun getEmptyCouponsSection(): List<AccountUIModel> {
        return mutableListOf<AccountUIModel>().apply {
            add(AccountDescUIModel("Join freebie community! Get all features"))//todo
            add(AccountActionButtonUIModel("Get coupons", null))//todo
        }
    }

    private fun getEmptyCompaniesSection(haveCompanies: Boolean): List<AccountUIModel> {
        return mutableListOf<AccountUIModel>().apply {
            if (!haveCompanies) {
                add(AccountDescUIModel("Register company to add own coupons"))//todo
            }
            add(
                AccountActionButtonUIModel(
                    "Register new company",
                    null,
                    ButtonAction.RegisterCompany
                )
            )//todo
        }
    }

    private fun getLogoutSection(): List<AccountUIModel> {
        return mutableListOf<AccountUIModel>().apply {
            add(
                AccountActionButtonUIModel(
                    text = "Logout",//todo
                    drawableStart = null,
                    buttonAction = ButtonAction.Logout
                )
            )
        }
    }

    private fun getAuthSection(): List<AccountUIModel> {
        return mutableListOf<AccountUIModel>().apply {
            add(AccountHeaderUIModel("Log in or Sign up"))//todo
            add(AccountDescUIModel("Join freebie community! Get all features"))//todo
            add(
                AccountActionButtonUIModel(
                    "Continue with google",//todo
                    R.drawable.ic_google, ButtonAction.GoogleSignIn
                )
            )
        }
    }

}