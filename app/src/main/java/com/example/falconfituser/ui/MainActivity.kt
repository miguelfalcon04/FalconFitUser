package com.example.falconfituser.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.falconfituser.R
import com.example.falconfituser.authentication.NavigationManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var bottomNav: BottomNavigationView

    @Inject
    lateinit var navigationManager: NavigationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment=supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController

        bottomNav = findViewById(R.id.bottom_navigation)
        bottomNav.setupWithNavController(navController)


        navController.addOnDestinationChangedListener { controller, destination, _ ->
            if (navigationManager.shouldNavigateToLogin(destination.id)) {
                // Redirigir al login
                controller.navigate(R.id.loginFragment)
            }

            // Ocultar menu segun su ubicaión
            when(destination.id) {
                R.id.loginFragment, R.id.registerFragment,
                R.id.createExerciseFragment, R.id.createUpdateSupersetFragment -> {
                    bottomNav.visibility = View.GONE
                }
                else -> bottomNav.visibility = View.VISIBLE
            }
        }

    }
}