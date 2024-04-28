package com.freebie.frieebiemobile.permissions

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.freebie.frieebiemobile.MainActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import java.lang.ref.WeakReference
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PermissionManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private var permissionResultListener: WeakReference<RequestPermissionResult>? = null
    private var activity: WeakReference<Activity>? = null

    private var requestPermissionLauncher: ActivityResultLauncher<String>? = null

    private fun getComponentActivity(): ComponentActivity? {
        return activity?.get() as? ComponentActivity
    }

    fun requestPermissions(
        permission: String,
        requestPermissionResult: RequestPermissionResult
    ) {
        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
            requestPermissionResult.onPermissionResult(PermissionResult.Granted)
        } else {
            this.permissionResultListener = WeakReference(requestPermissionResult)
            requestPermissionLauncher?.launch(permission)
        }
    }

    fun init(activity: Activity) {
        this.activity = WeakReference(activity)
        requestPermissionLauncher = getComponentActivity()?.registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ) { isGranted: Boolean ->
            if (isGranted) {
                permissionResultListener?.get()?.onPermissionResult(PermissionResult.Granted)
            } else {
                permissionResultListener?.get()?.onPermissionResult(PermissionResult.NotGranted)
            }
            permissionResultListener = null
        }
    }
}

fun interface RequestPermissionResult {
    fun onPermissionResult(result: PermissionResult)
}

sealed interface PermissionResult {
    object Granted : PermissionResult
    object NotGranted : PermissionResult
}