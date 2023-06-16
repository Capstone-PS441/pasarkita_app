package com.rayhan.pasarkitarevision

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.rayhan.pasarkitarevision.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        navController = navHostFragment.findNavController()

        binding.navView.apply {
            setupWithNavController(navController)
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_beranda -> {
                    showBottomNavigation()
                }
                R.id.navigation_produk -> {
                    showBottomNavigation()
                }
                R.id.navigation_keranjang -> {
                    showBottomNavigation()
                }
                R.id.navigation_profil -> {
                    showBottomNavigation()
                }
                else -> {
                    hideBottomNavigation()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        navController.currentDestination?.let { destination ->
            if (destination.id == R.id.navigation_beranda ||
                destination.id == R.id.navigation_produk ||
                destination.id == R.id.navigation_profil
            ) {
                showBottomNavigation()
            } else {
                hideBottomNavigation()
            }
        }
    }

    private fun showBottomNavigation() {
        binding.navView.visibility = View.VISIBLE
    }

    private fun hideBottomNavigation() {
        binding.navView.visibility = View.GONE
    }
}