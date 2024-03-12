package com.freebie.frieebiemobile.ui.account.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {

    @Query("Select * from profileentity")
    fun observeProfileById(): Flow<ProfileEntity>

    @Query("Select * from profileentity")
    fun getProfile(): ProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg users: ProfileEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: ProfileEntity) : Long

    @Query("DELETE FROM profileentity")
    fun clearTable()

    @Delete
    suspend fun delete(user: ProfileEntity)
}