package com.freebie.frieebiemobile.login.data.repository

import com.freebie.frieebiemobile.login.TokenStorage
import com.freebie.frieebiemobile.login.data.api.AuthApi
import com.freebie.frieebiemobile.login.domain.AuthStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface AuthRepository {
    suspend fun auth(token: String): Result<Boolean>
    suspend fun isAuthorized(): AuthStatus
}

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val tokenStorage: TokenStorage
) : AuthRepository {


    override suspend fun auth(token: String): Result<Boolean> = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = api.auth(token)
            saveTokenToPreferences(token)
            return@withContext Result.success(response.isSuccess)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun isAuthorized(): AuthStatus {
        return if (!tokenStorage.getAccessToken().isNullOrEmpty()) {
            AuthStatus.AUTHED
        } else {
            AuthStatus.NOT_AUTHED
        }
    }

    private suspend fun saveTokenToPreferences(token: String) {
        tokenStorage.setToken(token)
    }

}