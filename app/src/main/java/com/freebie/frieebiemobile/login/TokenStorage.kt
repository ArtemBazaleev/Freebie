package com.freebie.frieebiemobile.login

import com.freebie.frieebiemobile.storage.SecuredKeyValueStorage
import javax.inject.Inject

interface TokenStorage {
    suspend fun getAccessToken(): String?
    suspend fun setToken(token: String)
}

class TokenStorageImpl @Inject constructor(
    private val securedKeyValueStorage: SecuredKeyValueStorage
): TokenStorage {

    override suspend fun getAccessToken(): String? {
        return securedKeyValueStorage.getString(KEY, null)
    }

    override suspend fun setToken(token: String) {
        securedKeyValueStorage.putString(KEY, token)
    }

    companion object {
        private const val KEY = "accessTokenKey"
    }
}