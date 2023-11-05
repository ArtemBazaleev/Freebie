package com.freebie.frieebiemobile.login.data.repository

import com.freebie.frieebiemobile.login.TokenStorage
import com.freebie.frieebiemobile.login.data.api.AuthApi
import com.freebie.frieebiemobile.login.domain.AuthStatus
import com.freebie.frieebiemobile.ui.account.data.storage.UserProfileStorage
import com.freebie.frieebiemobile.ui.account.domain.ProfileModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface AuthRepository {
    suspend fun auth(token: String): Result<ProfileModel>
    suspend fun isAuthorized(): AuthStatus
}

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val tokenStorage: TokenStorage,
    private val profileStorage: UserProfileStorage
) : AuthRepository {

    override suspend fun auth(token: String): Result<ProfileModel> = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = api.auth(token).getOrThrow()
            saveTokenToPreferences(response.first)
            val profile = ProfileModel(
                uid = response.second.userId,
                firstName = response.second.name,
                lastName = response.second.familyName,
                avatar = response.second.pictureUrl,
                uniqueName = response.second.userId
            )
            profileStorage.updateOwnProfile(profile)
            return@withContext Result.success(profile)
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