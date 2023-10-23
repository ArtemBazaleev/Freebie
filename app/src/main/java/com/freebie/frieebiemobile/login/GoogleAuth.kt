package com.freebie.frieebiemobile.login

import android.R.attr.label
import android.R.attr.text
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat.startIntentSenderForResult
import androidx.core.content.ContextCompat.getSystemService
import com.freebie.frieebiemobile.R
import com.freebie.frieebiemobile.network.HttpAccess
import com.freebie.frieebiemobile.network.Method
import com.freebie.protos.AuthApiProtos
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject


interface GoogleAuth {
    fun requestAuth(activity: Activity)
    fun onResult(requestCode: Int, resultCode: Int, data: Intent?): SignInCredential?
}

class GoogleAuthImpl @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
    private val httpAccess: HttpAccess
) : GoogleAuth {

    private val oneTapClient by lazy { Identity.getSignInClient(applicationContext) }
    private val signInRequest by lazy { buildSignInRequest() }
    private val TAG = "GoogleAuthImpl"
    private val REQ_ONE_TAP = 3

    override fun requestAuth(activity: Activity) {
        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener { result ->
                try {
                    startIntentSenderForResult(
                        activity,
                        result.pendingIntent.intentSender, REQ_ONE_TAP,
                        null, 0, 0, 0, Bundle.EMPTY
                    )
                } catch (e: IntentSender.SendIntentException) {
                    Log.e(TAG, "Couldn't start One Tap UI: ${e.localizedMessage}")
                }
            }.addOnFailureListener { error ->
                Log.d("GoogleAuthImpl", "requestAuth fail error = $error")
            }
    }

    override fun onResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ): SignInCredential? {
        return when (requestCode) {
            REQ_ONE_TAP -> {
                oneTapClient.getSignInCredentialFromIntent(data)
            }
            else -> null
        }
    }

    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.builder()
            .setPasswordRequestOptions(
                BeginSignInRequest.PasswordRequestOptions.builder()
                    .setSupported(true)
                    .build()
            )
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(
                        applicationContext.getString(
                            R.string.auth_client_id
                        )
                    )
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            // Automatically sign in when exactly one credential is retrieved.
            .setAutoSelectEnabled(true)
            .build()
    }

}