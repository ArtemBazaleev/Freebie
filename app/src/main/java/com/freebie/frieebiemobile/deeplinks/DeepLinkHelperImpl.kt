package com.freebie.frieebiemobile.deeplinks

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeepLinkHelperImpl @Inject constructor(
    @ApplicationContext val applicationContext: Context
){
    fun openDeepLink(url: String) {
        Uri.parse(url).let { uri ->
            applicationContext.startActivity(Intent(Intent.ACTION_VIEW, uri).apply {
                addFlags(FLAG_ACTIVITY_NEW_TASK)
            })
        }
    }
}