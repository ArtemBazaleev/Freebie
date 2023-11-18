package com.freebie.frieebiemobile.ui.account.presentation

import android.content.Context
import com.freebie.frieebiemobile.R
import com.freebie.frieebiemobile.ui.account.domain.model.AccountInfoModel
import com.freebie.frieebiemobile.ui.account.domain.model.StatusCoupon
import com.freebie.frieebiemobile.ui.account.presentation.model.AccountActionButtonUIModel
import com.freebie.frieebiemobile.ui.account.presentation.model.AccountCompanyUIModel
import com.freebie.frieebiemobile.ui.account.presentation.model.AccountCouponsUIModel
import com.freebie.frieebiemobile.ui.account.presentation.model.AccountDescUIModel
import com.freebie.frieebiemobile.ui.account.presentation.model.AccountHeaderUIModel
import com.freebie.frieebiemobile.ui.account.presentation.model.AccountUIModel
import com.freebie.frieebiemobile.ui.account.presentation.model.ButtonAction
import com.freebie.frieebiemobile.ui.account.presentation.model.CouponGroupUiModel
import com.freebie.frieebiemobile.ui.account.presentation.model.CouponGroupsUIModel
import com.freebie.frieebiemobile.ui.feed.models.CouponUI
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AccountUIMapper @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun mapAccountInfo(accountInfo: AccountInfoModel, isAuthorized: Boolean): List<AccountUIModel> {
        val result = mutableListOf<AccountUIModel>()

        result.add(AccountHeaderUIModel("My coupons")) //todo
        if (accountInfo.coupons.isEmpty()) {
            result.addAll(getEmptyCouponsSection())
        } else {
            val couponGroups = accountInfo.coupons.map { group ->
                CouponGroupUiModel(
                    groupId = group.statusCoupon.intValue,
                    groupTitle = when (group.statusCoupon) {
                        StatusCoupon.ACTIVE -> R.string.active
                        StatusCoupon.USED -> R.string.used
                        StatusCoupon.EXPIRED -> R.string.expired
                        StatusCoupon.UNRECOGNIZED -> R.string.unrecognized
                    },
                    isActive = false
                )
            }
            result.add(CouponGroupsUIModel(couponGroups))
            result.add(
                AccountCouponsUIModel(
                    coupons = accountInfo.coupons.first().coupons.map { couponModel ->  //todo
                        CouponUI(
                            id = couponModel.id,
                            avatar = couponModel.avatar,
                            name = couponModel.name ?: "",
                            description = couponModel.description ?: "",
                            discount = couponModel.discount ?: ""
                        )
                    }
                )
            )
        }

        result.add(AccountHeaderUIModel("My companies"))//todo
        if (accountInfo.companies.isEmpty()) {
            result.addAll(getEmptyCompaniesSection())
        } else {
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

    private fun getEmptyCompaniesSection(): List<AccountUIModel> {
        return mutableListOf<AccountUIModel>().apply {
            add(AccountDescUIModel("Register company to add own coupons"))//todo
            add(AccountActionButtonUIModel("Register new company", null))//todo
        }
    }

    private fun getAuthSection(): List<AccountUIModel> {
        return mutableListOf<AccountUIModel>().apply {
            add(AccountHeaderUIModel("Log in or Sign up"))//todo
            add(AccountDescUIModel("Join freebie community! Get all features"))//todo
            add(
                AccountActionButtonUIModel(
                    "Continue with google",//todo
                    R.drawable.ic_google,
                    ButtonAction.GoogleSignIn
                )
            )
        }
    }

}