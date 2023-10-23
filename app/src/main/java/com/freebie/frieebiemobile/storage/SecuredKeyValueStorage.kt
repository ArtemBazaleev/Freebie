package com.freebie.frieebiemobile.storage

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


interface SecuredKeyValueStorage {
    suspend fun putString(key: String, value: String)
    suspend fun getString(key: String, defValue: String?): String?
}

class SecuredKeyValueStorageImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : SecuredKeyValueStorage {

    private val masterKey by lazy {
        MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
    }

    private val sharedPreferences by lazy {
        EncryptedSharedPreferences.create(
            context,
            "freebie-pref",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    private val editor by lazy { sharedPreferences.edit() }

    override suspend fun putString(key: String, value: String) {
        editor
            .putString(key, value)
            .apply()
    }

    override suspend fun getString(key: String, defValue: String?): String? {
        return sharedPreferences.getString(key, defValue)
    }

    companion object {
        private const val PREF = "freebie-pref"
    }
}