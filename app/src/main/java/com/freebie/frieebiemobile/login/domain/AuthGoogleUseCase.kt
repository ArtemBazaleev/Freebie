package com.freebie.frieebiemobile.login.domain

import com.freebie.frieebiemobile.login.data.repository.AuthRepository
import com.freebie.frieebiemobile.ui.account.domain.ProfileModel
import com.google.android.gms.auth.api.identity.SignInCredential
import java.lang.IllegalStateException
import javax.inject.Inject

interface AuthGoogleUseCase {
    suspend fun auth(credential: SignInCredential) : Result<ProfileModel>
}

class AuthGoogleUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : AuthGoogleUseCase {

    override suspend fun auth(credential: SignInCredential): Result<ProfileModel> {
        val token = credential.googleIdToken ?: return Result.failure(IllegalStateException("No googleId token"))
        return authRepository.auth(token)
    }
}