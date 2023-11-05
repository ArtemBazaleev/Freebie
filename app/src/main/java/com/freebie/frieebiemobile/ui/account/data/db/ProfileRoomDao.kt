package com.freebie.frieebiemobile.ui.account.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ProfileEntity::class], version = 1)
abstract class ProfileRoomDao : RoomDatabase() {
    abstract fun profileDao(): ProfileDao
}