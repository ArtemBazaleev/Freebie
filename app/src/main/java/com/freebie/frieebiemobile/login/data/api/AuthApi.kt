package com.freebie.frieebiemobile.login.data.api

import com.freebie.frieebiemobile.network.HttpAccess
import com.freebie.frieebiemobile.network.Method
import com.freebie.protos.AuthApiProtos
import com.freebie.protos.UserProfileModelProtos
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface AuthApi {
    suspend fun auth(token: String): Result<Pair<String, UserProfileModelProtos.UserProfile>>
    suspend fun logout(): Result<Boolean>
}

class AuthApiImpl @Inject constructor(
    private val httpAccess: HttpAccess
) : AuthApi {

    override suspend fun auth(
        token: String
    ): Result<Pair<String, UserProfileModelProtos.UserProfile>> = withContext(Dispatchers.IO) {
        val body = AuthApiProtos.AuthRequest
            .newBuilder()
            .setAuthToken(token)
            .setAuthMethod(AuthApiProtos.AuthMethod.GOOGLE)
            .build()

        val result = httpAccess.httpRequest(
            requestUrlSegment = AUTH_ENDPOINT,
            headers = mapOf(),
            body = body.toByteArray(),
            method = Method.POST
        )
        return@withContext if (result.isSuccess) {
            val response = AuthApiProtos.AuthResponse.parseFrom(result.bodyAsArray)
            val profile = response.profile
            Result.success(Pair(response.token, profile))
        } else Result.failure(IllegalStateException("Result is failed"))
    }

    override suspend fun logout(): Result<Boolean> = withContext(Dispatchers.IO) {
        return@withContext try {
            val result = httpAccess.httpRequest(
                requestUrlSegment = LOGOUT_ENDPOINT,
                method = Method.GET
            )
            if (result.isSuccess) Result.success(true)
            else Result.success(false)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    companion object {
        private const val AUTH_ENDPOINT = "v1/auth"
        private const val LOGOUT_ENDPOINT = "v1/auth/logout"
    }
}