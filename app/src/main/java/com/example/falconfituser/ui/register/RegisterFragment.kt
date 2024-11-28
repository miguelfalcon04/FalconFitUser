package com.example.falconfituser.ui.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.falconfituser.R
import com.example.falconfituser.databinding.FragmentRegisterBinding
import com.example.falconfituser.databinding.ItemMachineBinding

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = FragmentRegisterBinding.inflate(inflater,container,false)
        return binding.root

        val navController = findNavController()

        // Configuramos el listener para el botón de "Login"
        binding.loginButton.setOnClickListener {
            // Navegar al LoginFragment utilizando la acción definida en el nav_graph
            navController.navigate(R.id.action_registerFragment_to_loginFragment)
        }

    }
}