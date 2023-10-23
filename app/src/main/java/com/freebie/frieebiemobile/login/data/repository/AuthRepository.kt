package com.freebie.frieebiemobile.login.data.repository

import com.freebie.frieebiemobile.login.TokenStorage
import com.freebie.frieebiemobile.login.data.api.AuthApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

interface AuthRepository {
    suspend fun auth(token: String): Result<Boolean>
}

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val tokenStorage: TokenStorage
): AuthRepository {


    override suspend fun auth(token: String): Result<Boolean> = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = api.auth(token)
            saveTokenToPreferences(token)
            return@withContext Result.success(response.isSuccess)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun saveTokenToPreferences(token: String) {
        tokenStorage.setToken(token)
    }

}