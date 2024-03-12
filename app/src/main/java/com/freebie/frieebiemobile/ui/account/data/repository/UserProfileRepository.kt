package com.freebie.frieebiemobile.ui.account.data.repository

import com.freebie.frieebiemobile.ui.account.data.storage.UserProfileStorage
import com.freebie.frieebiemobile.ui.account.domain.ProfileModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface UserProfileRepository {
    fun getOwnProfileFlow(): Flow<ProfileModel> //TODO add getOwnProfile model


    suspend fun getOwnLocalProfile(): ProfileModel?
}

class UserProfileRepositoryImpl @Inject constructor(
    private val storage: UserProfileStorage
): UserProfileRepository {
    override fun getOwnProfileFlow() = storage.getOwnProfileFlow()

    override suspend fun getOwnLocalProfile(): ProfileModel? {
        return storage.getOwnProfile()
    }
}
