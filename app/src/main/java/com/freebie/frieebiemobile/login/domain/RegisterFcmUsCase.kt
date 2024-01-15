package com.freebie.frieebiemobile.login.domain

import com.freebie.frieebiemobile.login.data.repository.AuthRepository
import javax.inject.Inject

interface RegisterFcmUseCase {
    suspend fun registerFcm()
}

class RegisterFcmUseCaseImpl @Inject constructor(
    private val repository: AuthRepository
): RegisterFcmUseCase {
    override suspend fun registerFcm() {
        repository.registerFcmToken()
    }
}