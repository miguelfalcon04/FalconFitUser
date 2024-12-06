package com.example.falconfituser.ui.loginAndRegister

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.falconfituser.R
import com.example.falconfituser.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment(R.layout.fragment_register) {
    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

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
                Toast.makeText(context, "Rellene todos los campos", Toast.LENGTH_SHORT).show()
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