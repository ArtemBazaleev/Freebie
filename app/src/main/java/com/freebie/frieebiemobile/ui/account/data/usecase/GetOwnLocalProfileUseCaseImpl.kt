package com.freebie.frieebiemobile.ui.account.data.usecase

import com.freebie.frieebiemobile.ui.account.data.repository.UserProfileRepository
import com.freebie.frieebiemobile.ui.account.domain.GetOwnLocalProfileUseCase
import com.freebie.frieebiemobile.ui.account.domain.ProfileModel
import javax.inject.Inject

class GetOwnLocalProfileUseCaseImpl @Inject constructor(
    private val repository: UserProfileRepository
) : GetOwnLocalProfileUseCase {
    override suspend fun getOwnLocalProfile(): ProfileModel? {
        return repository.getOwnLocalProfile()
    }
}