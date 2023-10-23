package com.freebie.frieebiemobile.login.domain

import com.freebie.frieebiemobile.login.data.repository.AuthRepository
import com.google.android.gms.auth.api.identity.SignInCredential
import javax.inject.Inject

interface AuthGoogleUseCase {
    suspend fun auth(credential: SignInCredential) : Result<Boolean>
}

class AuthGoogleUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : AuthGoogleUseCase {

    override suspend fun auth(credential: SignInCredential): Result<Boolean> {
        val token = credential.googleIdToken ?: return Result.success(false)
        return authRepository.auth(token)
    }
}