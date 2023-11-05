package com.freebie.frieebiemobile.ui.account.data.usecase

import com.freebie.frieebiemobile.ui.account.data.repository.UserProfileRepository
import com.freebie.frieebiemobile.ui.account.domain.OwnUserProfileUseCase
import javax.inject.Inject

class OwnUserProfileUseCaseImpl @Inject constructor(
    private val repository: UserProfileRepository
): OwnUserProfileUseCase {

    override fun getOwnUserProfileFlow() =
        repository.getOwnProfileFlow()

}