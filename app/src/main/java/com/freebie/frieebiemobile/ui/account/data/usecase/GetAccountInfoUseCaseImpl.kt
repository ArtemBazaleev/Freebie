package com.freebie.frieebiemobile.ui.account.data.usecase

import com.freebie.frieebiemobile.ui.account.data.repository.AccountRepository
import com.freebie.frieebiemobile.ui.account.domain.GetAccountInfoUseCase
import com.freebie.frieebiemobile.ui.account.domain.model.AccountInfoModel
import javax.inject.Inject

class GetAccountInfoUseCaseImpl @Inject constructor(
    private val repository: AccountRepository
): GetAccountInfoUseCase {

    override suspend fun getAccountInfo(): Result<AccountInfoModel> {
        return repository.requestAccountInfo()
    }
}