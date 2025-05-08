package com.example.falconfituser.ui

import android.app.ComponentCaller
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.falconfituser.R
import com.example.falconfituser.authentication.NavigationManager
import com.example.falconfituser.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.zxing.integration.android.IntentIntegrator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var bottomNav: BottomNavigationView
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var navigationManager: NavigationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val navHostFragment=supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController

        bottomNav = findViewById(R.id.bottom_navigation)
        bottomNav.setupWithNavController(navController)


        val btnScanner = findViewById<FloatingActionButton>(R.id.btnScanner)

        btnScanner.setOnClickListener{
            initScanner()
        }

        navController.addOnDestinationChangedListener { controller, destination, _ ->
            if (navigationManager.shouldNavigateToLogin(destination.id)) {
                // Redirigir al login
                controller.navigate(R.id.loginFragment)
            }

            // Ocultar menu segun su ubicaión
            when(destination.id) {
                R.id.loginFragment, R.id.registerFragment,
                R.id.createExerciseFragment, R.id.createUpdateSupersetFragment,
                R.id.cameraPreviewFragment -> {
                    bottomNav.visibility = View.GONE
                }
                else -> {
                    bottomNav.visibility = View.VISIBLE
                }
            }

            // Ocultar botón de navegación siempre excepto en machineListFragment
            when(destination.id) {
                R.id.machineListFragment -> {
                    btnScanner.visibility = View.VISIBLE
                }
                else -> {
                    btnScanner.visibility = View.GONE
                }
            }
        }

    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        caller: ComponentCaller
    ) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Cancelado", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "El valor escaneado es: " + result.contents, Toast.LENGTH_LONG).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun initScanner() {
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Centra el QR dentro del recuadro")
        integrator.setBeepEnabled(true)
        integrator.initiateScan()
    }
}