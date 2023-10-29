package com.freebie.frieebiemobile.login.data.storage

import com.freebie.frieebiemobile.storage.KeyValueStorage
import javax.inject.Inject

interface GoogleAuthStorage {
    suspend fun authCanceled()
    suspend fun isEligibleForAuth(): Boolean
}

class GoogleAuthStorageImpl @Inject constructor(
    private val keyValueStorage: KeyValueStorage
): GoogleAuthStorage {

    override suspend fun authCanceled() {
        val currentCount = keyValueStorage.getLong(GOOGLE_ERROR_AUTH_COUNT, 0)
        if (currentCount >= 2) return
    }

    override suspend fun isEligibleForAuth(): Boolean {
        //TODO("Not yet implemented")
        return false
    }

    private fun lastErrorTime(): Long {
        //TODO()
        return 0
    }

    companion object {
        private const val GOOGLE_ERROR_AUTH_COUNT = "GOOGLE_ERROR_AUTH_COUNT"
        private const val LAST_GOOGLE_ERROR_COUNT = "LAST_GOOGLE_ERROR_COUNT"
    }

}