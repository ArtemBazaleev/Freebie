package com.freebie.frieebiemobile

import android.app.Application
import com.bumptech.glide.Glide
import com.freebie.frieebiemobile.permissions.ActivityProvider
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class FreebieApp : Application() {

    @Inject
    lateinit var activityProvider: ActivityProvider

    override fun onCreate() {
        super.onCreate()
        activityProvider.register()
    }
}