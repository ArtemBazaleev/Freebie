package com.freebie.frieebiemobile

import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.freebie.frieebiemobile.databinding.ActivityMainBinding
import com.freebie.frieebiemobile.fcm.NotificationController
import com.freebie.frieebiemobile.fcm.NotificationData
import com.freebie.frieebiemobile.login.GoogleAuth
import com.freebie.frieebiemobile.ui.utils.NavHolder
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import java.lang.ref.WeakReference
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavHolder {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController

    private val viewModel by viewModels<MainActivityViewModel>()

    @Inject
    lateinit var googleAuth: GoogleAuth

    // Declare the launcher at the top of your Activity/Fragment:
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {

            NotificationController().apply {
                showNotification(this@MainActivity, NotificationData("123", "header", "footer"))
            }
            // FCM SDK (and your app) can post notifications.
        } else {
            // TODO: Inform user that that your app will not show notifications.
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        navController = navHostFragment.navController
        val weakReference = WeakReference(navView)
        navController.addOnDestinationChangedListener { _, destination, _ ->

            val view = weakReference.get()
            if (view == null) {
                //navController.removeOnDestinationChangedListener(this)
                return@addOnDestinationChangedListener
            }
            view.menu.forEach { item ->
                if (destination.hierarchy.any { it.id == item.itemId }) {
                    item.isChecked = true
                }
            }
            if (destination.id == R.id.feed_creen
                || destination.id == R.id.category_screen
                || destination.id == R.id.account_screen
            ) {
                showNavBar()
            } else {
                hideNavBar()
            }
        }
        navView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.feed_navigation -> {
                    navController.navigate(
                        R.id.feed_navigation,
                        null,
                        NavOptions.Builder().setLaunchSingleTop(true).build()
                    )
                    Toast.makeText(this, "123", Toast.LENGTH_SHORT).show()
                }

                R.id.category_navigation -> {
                    navController.navigate(
                        R.id.category_navigation,
                        null,
                        NavOptions.Builder().setLaunchSingleTop(true).build()
                    )
                    Toast.makeText(this, "321", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }
        navView.setupWithNavController(navController)
        viewModel
        showFakeNotification()
    }

    private fun showFakeNotification() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(POST_NOTIFICATIONS)
            }
        }
    }

    override fun getNavController(): NavController {
        return navController
    }

    private fun showNavBar() {
        binding.navView.visibility = View.VISIBLE
    }

    private fun hideNavBar() {
        binding.navView.visibility = View.GONE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        runCatching {
            val cred = googleAuth.onResult(requestCode, resultCode, data)
            cred?.let(viewModel::credentialsReceived)
        }
    }

}