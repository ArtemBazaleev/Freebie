package com.freebie.frieebiemobile.storage

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import java.lang.Exception
import javax.inject.Inject

interface KeyValueStorage {
    suspend fun putString(key: String, value: String)
    suspend fun getString(key: String, defValue: String): String

    suspend fun putLong(key: String, value: Long)
    suspend fun getLong(key: String, defValue: Long): Long
    suspend fun clear()
}

class KeyValueStorageImpl @Inject constructor(
    @ApplicationContext private val context: Context
): KeyValueStorage {

    private val Context.dataStore by preferencesDataStore(name = "settings")

    override suspend fun putString(key: String, value: String) {
        context.dataStore.edit {
            it[stringPreferencesKey(key)] = value
        }
    }

    override suspend fun getString(key: String, defValue: String): String {
        return try {
            context.dataStore.data.first()[stringPreferencesKey(key)] ?: defValue
        } catch (e: Exception) {
            defValue
        }
    }

    override suspend fun putLong(key: String, value: Long) {
        context.dataStore.edit {
            it[longPreferencesKey(key)] = value
        }
    }

    override suspend fun getLong(key: String, defValue: Long): Long {
        return try {
            context.dataStore.data.first()[longPreferencesKey(key)] ?: defValue
        } catch (e: Exception) {
            defValue
        }
    }

    override suspend fun clear() {
        context.dataStore.edit {
            it.clear()
        }
    }

}