package com.freebie.frieebiemobile.ui.account.data.api

import com.freebie.frieebiemobile.network.HttpAccess
import com.freebie.frieebiemobile.network.Method
import com.freebie.protos.UserProfileApiProtos
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface AccountApi {
    suspend fun getAccountInfo(): UserProfileApiProtos.AccountDataResponse
}

class AccountApiImpl @Inject constructor(
    private val httpAccess: HttpAccess
): AccountApi {

    override suspend fun getAccountInfo(): UserProfileApiProtos.AccountDataResponse = withContext(Dispatchers.IO) {
        val request = httpAccess.httpRequest(
            requestUrlSegment = ACCOUNT_INFO_API,
            headers = emptyMap(),
            method = Method.GET,
            body = null
        )
        return@withContext UserProfileApiProtos
            .AccountDataResponse
            .parseFrom(request.bodyAsArray)
    }

    companion object {
        private val ACCOUNT_INFO_API = "v1/users/account-data"
    }
}