package com.freebie.frieebiemobile.ui.account.domain

import com.freebie.frieebiemobile.ui.account.domain.model.AccountInfoModel

interface GetAccountInfoUseCase {
    suspend fun getAccountInfo(): Result<AccountInfoModel>
}