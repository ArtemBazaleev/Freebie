package com.freebie.frieebiemobile.login.domain

import com.freebie.frieebiemobile.login.data.repository.AuthRepository
import javax.inject.Inject

interface LogoutUseCase {
    suspend fun logout(): Result<Boolean>
}

class LogoutUseCaseImpl @Inject constructor(
    private val repository: AuthRepository
): LogoutUseCase {

    override suspend fun logout() = repository.logout()
}