package com.freebie.frieebiemobile.login.data.api

import com.freebie.frieebiemobile.network.HttpAccess
import com.freebie.frieebiemobile.network.Method
import com.freebie.protos.AuthApiProtos
import com.freebie.protos.UserProfileModelProtos
import java.lang.IllegalStateException
import javax.inject.Inject

interface AuthApi {
    suspend fun auth(token: String): Result<Pair<String, UserProfileModelProtos.UserProfile>>
}

class AuthApiImpl @Inject constructor(
    private val httpAccess: HttpAccess
): AuthApi {

    override suspend fun auth(token: String): Result<Pair<String, UserProfileModelProtos.UserProfile>> {
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
        return if (result.isSuccess) {
            val response = AuthApiProtos.AuthResponse.parseFrom(result.bodyAsArray)
            val profile = response.profile
            Result.success(Pair(response.token, profile))
        } else Result.failure(IllegalStateException("Result is failed"))
    }

    companion object {
        private const val AUTH_ENDPOINT = "v1/auth"
    }
}