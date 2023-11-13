package com.freebie.frieebiemobile.ui.account.presentation.model

class AccountCompanyUIModel(
    val companyId: String,
    val companyName: String,
    val companyAvatar: String
): AccountUIModel {
    override fun getItemType() = AccountType.COMPANY
}