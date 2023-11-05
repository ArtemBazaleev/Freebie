package com.freebie.frieebiemobile.ui.account.data.storage

import com.freebie.frieebiemobile.ui.account.data.db.UserProfileDaoProvider
import com.freebie.frieebiemobile.ui.account.data.mapper.ProfileDataMapper
import com.freebie.frieebiemobile.ui.account.domain.ProfileModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

interface UserProfileStorage {
    fun getOwnProfileFlow(): Flow<ProfileModel>
    suspend fun updateOwnProfile(profile: ProfileModel)
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
    }

    override suspend fun updateOwnProfile(profile: ProfileModel) {
        userProfileDaoProvider.getUserProfileDao().insertAll(mapper.mapToData(profile))
    }

}