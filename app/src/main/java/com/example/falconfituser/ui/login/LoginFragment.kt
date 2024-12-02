package com.example.falconfituser.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.falconfituser.R
import com.example.falconfituser.ui.MainActivity

class LoginFragment : Fragment(R.layout.fragment_login) {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnLogin = view.findViewById<Button>(R.id.loginButton)
        val getUsername = view.findViewById<EditText>(R.id.emailEditText)
        val getPassword = view.findViewById<EditText>(R.id.passwordEditText)
        val toRegister = view.findViewById<Button>(R.id.registerButton)

        btnLogin.setOnClickListener {
            val username = getUsername.text.toString()
            val password = getPassword.text.toString()
            if (username.isEmpty() && password.isEmpty()){
                Toast.makeText(context, "Alguna de las credenciales es incorrecta", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

        }

        toRegister.setOnClickListener{
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        btnLogin.setOnClickListener{
            findNavController().navigate(R.id.machineListFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}