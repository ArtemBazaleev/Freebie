package com.freebie.frieebiemobile.ui.account.presentation.model

data class AccountHeaderUIModel(
    val text: String
): AccountUIModel {
    override fun getItemType() = AccountType.HEADER
}