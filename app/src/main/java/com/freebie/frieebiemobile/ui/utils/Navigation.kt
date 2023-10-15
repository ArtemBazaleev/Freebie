package com.freebie.frieebiemobile.ui.utils

import android.app.Activity
import androidx.navigation.NavController

fun Activity.getNavComponent(): NavController? {
    return if (this is NavHolder) getNavController()
    else null
}

interface NavHolder {
    fun getNavController(): NavController
}