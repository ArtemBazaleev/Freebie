package com.freebie.frieebiemobile.ui.account.data.mapper

import com.freebie.frieebiemobile.ui.account.data.db.ProfileEntity
import com.freebie.frieebiemobile.ui.account.domain.ProfileModel
import javax.inject.Inject

class ProfileDataMapper @Inject constructor() {
    fun mapToDomain(profile: ProfileEntity?): ProfileModel? {
        if (profile == null) return null
        return ProfileModel(
            uid = profile.uid,
            firstName = profile.firstName,
            lastName = profile.lastName,
            uniqueName = profile.uniqueName,
            avatar = profile.avatar
        )
    }

    fun mapToData(profile: ProfileModel): ProfileEntity {
        return ProfileEntity(
            uid = profile.uid,
            firstName = profile.firstName,
            lastName = profile.lastName,
            uniqueName = profile.uniqueName,
            avatar = profile.avatar
        )
    }
}