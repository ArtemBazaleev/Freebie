package com.freebie.frieebiemobile.fcm

import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FcmTokenProvider @Inject constructor() {
    private val firebaseMessaging by lazy { FirebaseMessaging.getInstance() }

    suspend fun provideFcmToken(): String = withContext(Dispatchers.IO) {
        return@withContext firebaseMessaging.token.await()
    }
}