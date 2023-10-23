package com.freebie.frieebiemobile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.freebie.frieebiemobile.databinding.ActivityMainBinding
import com.freebie.frieebiemobile.login.GoogleAuth
import com.freebie.frieebiemobile.ui.account.AccountViewModel
import com.freebie.frieebiemobile.ui.utils.NavHolder
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView


        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
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
            if (destination.id == R.id.navigation_category_feed) { hideNavBar() }
            else { showNavBar() }
        }
        navView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.feed_navigation -> {
                    navController.navigate(R.id.feed_navigation, null, NavOptions.Builder().setLaunchSingleTop(true).build())
                    Toast.makeText(this, "123", Toast.LENGTH_SHORT).show()
                }
                R.id.category_navigation -> {
                    navController.navigate(R.id.category_navigation, null, NavOptions.Builder().setLaunchSingleTop(true).build())
                    Toast.makeText(this, "321", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }
        navView.setupWithNavController(navController)
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
        val cred = googleAuth.onResult(requestCode, resultCode, data)
        cred?.let(viewModel::credentialsReceived)
    }

}