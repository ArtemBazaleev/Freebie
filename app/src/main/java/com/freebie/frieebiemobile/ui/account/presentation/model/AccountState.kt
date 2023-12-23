package com.freebie.frieebiemobile.ui.account.presentation.model

import com.freebie.frieebiemobile.ui.feed.models.PlaceHolderInfo

data class AccountState (
    val ownProfile: UserUiModel?,
    val accountUI: List<AccountUIModel>,
    val isRefreshing: Boolean = false,
    val placeholder: PlaceHolderInfo? = null,
    val isAuthed: Boolean = false
)