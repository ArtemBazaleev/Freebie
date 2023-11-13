package com.freebie.frieebiemobile.ui.account.data.repository

import com.freebie.frieebiemobile.ui.account.data.api.AccountApi
import com.freebie.frieebiemobile.ui.account.data.mapper.AccountDataMapperImpl
import com.freebie.frieebiemobile.ui.account.domain.model.AccountInfoModel
import java.lang.Exception
import javax.inject.Inject

interface AccountRepository {
    suspend fun requestAccountInfo(): Result<AccountInfoModel>
}

class AccountRepositoryImpl @Inject constructor(
    private val accountApi: AccountApi,
    private val mapper: AccountDataMapperImpl
): AccountRepository {

    override suspend fun requestAccountInfo(): Result<AccountInfoModel> {
        return try {
            val account = accountApi.getAccountInfo()
            Result.success(mapper.map(account))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}