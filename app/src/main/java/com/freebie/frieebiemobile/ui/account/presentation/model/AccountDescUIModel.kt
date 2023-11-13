package com.freebie.frieebiemobile.ui.account.presentation.model

data class AccountDescUIModel (
    val text: String
): AccountUIModel {
    override fun getItemType() = AccountType.DESCRIPTION
}