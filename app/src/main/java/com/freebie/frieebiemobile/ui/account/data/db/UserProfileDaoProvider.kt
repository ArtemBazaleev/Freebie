package com.freebie.frieebiemobile.ui.account.data.db

import android.content.Context
import androidx.room.Room
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface UserProfileDaoProvider {
    fun getUserProfileDao(): ProfileDao
}


class UserProfileDaoProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context
): UserProfileDaoProvider {
    private val roomProfileDb by lazy { createProfileDb() }

    private fun createProfileDb() =
        Room.databaseBuilder(context, ProfileRoomDao::class.java, PROFILE_DB_NAME).build()

    override fun getUserProfileDao(): ProfileDao {
        return roomProfileDb.profileDao()
    }

    companion object {
        private const val PROFILE_DB_NAME = "profile-db"
    }
}