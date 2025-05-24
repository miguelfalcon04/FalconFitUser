package com.example.falconfituser.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.falconfituser.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.zxing.integration.android.IntentIntegrator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val host = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = host.navController
        val navInflater = navController.navInflater

        val graph = navInflater.inflate(R.navigation.nav_graph).apply {
            setStartDestination(
                if (FirebaseAuth.getInstance().currentUser != null) {
                    R.id.machine
                } else {
                    R.id.loginFragment
                }
            )
        }

        navController.graph = graph

        bottomNav = findViewById(R.id.bottom_navigation)
        bottomNav.setupWithNavController(navController)

        val btnScanner = findViewById<FloatingActionButton>(R.id.btnScanner)

        btnScanner.setOnClickListener{
            initScanner()
        }

        navController.addOnDestinationChangedListener { controller, destination, _ ->
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
        data: Intent?
    ) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, R.string.cancel, Toast.LENGTH_LONG).show()
            } else if (result.contents.contains("https://falconfit.netlify.app/")) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setData(Uri.parse(result.contents))
                intent.setPackage("com.android.chrome")
                startActivity(intent)
            } else {
                Toast.makeText(this, R.string.invalidQr, Toast.LENGTH_LONG).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun initScanner() {
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Scan in box")
        integrator.setBeepEnabled(true)
        integrator.initiateScan()
    }
}