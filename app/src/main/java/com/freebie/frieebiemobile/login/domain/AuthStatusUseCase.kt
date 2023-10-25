package com.freebie.frieebiemobile.login.domain

import com.freebie.frieebiemobile.login.data.repository.AuthRepository
import javax.inject.Inject

interface AuthStatusUseCase {
    suspend fun getAuthStatus(): AuthStatus
}

class AuthStatusUseCaseImpl @Inject constructor(
    private val repository: AuthRepository
): AuthStatusUseCase {

    override suspend fun getAuthStatus(): AuthStatus {
        return repository.isAuthorized()
    }

}