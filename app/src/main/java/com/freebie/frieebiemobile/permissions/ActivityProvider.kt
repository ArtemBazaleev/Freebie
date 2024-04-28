package com.freebie.frieebiemobile.permissions

import android.app.Activity
import android.content.Context
import com.freebie.frieebiemobile.FreebieApp
import dagger.hilt.android.qualifiers.ApplicationContext
import java.lang.ref.WeakReference
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActivityProvider @Inject constructor(
    @ApplicationContext private val app: Context
) {

    private var resumedActivity: WeakReference<Activity>? = null

    private fun registerActivityCallbacks() {
        (app as? FreebieApp)?.registerActivityLifecycleCallbacks(
            object: DefaultActivityLifecycleCallback {
                override fun onActivityResumed(activity: Activity) {
                    resumedActivity = WeakReference(activity)
                }

                override fun onActivityStopped(activity: Activity) {
                    resumedActivity = null
                }
            }
        )
    }

    fun getActivity(): Activity? = resumedActivity?.get()

    fun register() {
        registerActivityCallbacks()
    }
}