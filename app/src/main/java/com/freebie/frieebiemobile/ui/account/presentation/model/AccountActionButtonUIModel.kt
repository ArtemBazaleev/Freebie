package com.freebie.frieebiemobile.ui.account.presentation.model

import androidx.annotation.DrawableRes

data class AccountActionButtonUIModel(
    val text: String,
    @DrawableRes val drawableStart: Int?,
    val buttonAction: ButtonAction? = null
): AccountUIModel {
    override fun getItemType() = AccountType.BUTTON
}

enum class ButtonAction {
    GoogleSignIn,
    Logout
}