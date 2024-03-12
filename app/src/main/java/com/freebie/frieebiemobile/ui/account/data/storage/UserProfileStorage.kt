package com.freebie.frieebiemobile.ui.account.data.storage

import com.freebie.frieebiemobile.ui.account.data.db.UserProfileDaoProvider
import com.freebie.frieebiemobile.ui.account.data.mapper.ProfileDataMapper
import com.freebie.frieebiemobile.ui.account.domain.ProfileModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface UserProfileStorage {
    fun getOwnProfileFlow(): Flow<ProfileModel>
    suspend fun updateOwnProfile(profile: ProfileModel)
    suspend fun getOwnProfile(): ProfileModel?
    suspend fun clear()
}

class UserProfileStorageImpl @Inject constructor(
    private val userProfileDaoProvider: UserProfileDaoProvider,
    private val mapper: ProfileDataMapper
) : UserProfileStorage {

    override fun getOwnProfileFlow(): Flow<ProfileModel> {
        return userProfileDaoProvider
            .getUserProfileDao()
            .observeProfileById()
            .filterNotNull()
            .map(mapper::mapToDomain)
            .filterNotNull()
    }

    override suspend fun updateOwnProfile(profile: ProfileModel) {
        withContext(Dispatchers.IO) {
            userProfileDaoProvider.getUserProfileDao().insert(mapper.mapToData(profile))
        }
    }

    override suspend fun getOwnProfile() =
        withContext(Dispatchers.IO) {
            return@withContext mapper.mapToDomain(userProfileDaoProvider.getUserProfileDao().getProfile())
        }

    override suspend fun clear() = withContext(Dispatchers.IO) {
        userProfileDaoProvider.getUserProfileDao().clearTable()
    }

}