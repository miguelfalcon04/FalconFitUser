package com.example.falconfituser.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.falconfituser.R

class LoginFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController() // Obtén el NavController

        // Configura el listener para el botón de registro
        view.findViewById<Button>(R.id.registerButton).setOnClickListener {
            // Navega al RegisterFragment utilizando la acción definida en el nav_graph
            navController.navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }
}