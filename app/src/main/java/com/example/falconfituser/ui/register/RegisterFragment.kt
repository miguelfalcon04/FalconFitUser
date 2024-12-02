package com.example.falconfituser.ui.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.falconfituser.R
import com.example.falconfituser.databinding.FragmentRegisterBinding
import com.example.falconfituser.databinding.ItemMachineBinding
import com.example.falconfituser.ui.MainActivity

class RegisterFragment : Fragment(R.layout.fragment_register) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnRegister = view.findViewById<Button>(R.id.registerButton)
        val getUsername = view.findViewById<EditText>(R.id.nameEditText)
        val getEmail = view.findViewById<EditText>(R.id.emailEditText)
        val getPassword = view.findViewById<EditText>(R.id.passwordEditText)
        val toLogin = view.findViewById<TextView>(R.id.loginButton)

        btnRegister.setOnClickListener {
            val username = getUsername.text.toString()
            val email = getEmail.text.toString()
            val password = getPassword.text.toString()
            if (username.isEmpty() && email.isEmpty() && password.isEmpty()){
                Toast.makeText(context, "Alguna de las credenciales es incorrecta", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
        }

        toLogin.setOnClickListener{
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}