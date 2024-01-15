package com.freebie.frieebiemobile

import android.annotation.SuppressLint
import android.util.Log
import com.freebie.frieebiemobile.fcm.NotificationController
import com.freebie.frieebiemobile.fcm.NotificationData
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

@SuppressLint("MissingFirebaseInstanceTokenRefresh") //TODO
class FreebiePushNotificationService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        Log.d("FreebiePushNotificationService", "received data = ${message.data}")
        val params = NotificationData(
            header = message.data["title"] ?: "netu title",
            footer = message.data["body"] ?: "netu body",
            id = message.data["id"] ?: "netu id"
        )
        NotificationController().apply {
            showNotification(baseContext, params)
        }
    }


}
