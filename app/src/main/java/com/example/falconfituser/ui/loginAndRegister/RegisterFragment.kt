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
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.falconfituser.R
import com.example.falconfituser.data.api.loginRegister.RegisterRaw
import com.example.falconfituser.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {
    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: LoginRegisterViewModel by viewModels()

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
                Toast.makeText(context, getString(R.string.error_fill_all), Toast.LENGTH_SHORT).show()
            }else{
                val userToRegister = RegisterRaw(
                    username = username,
                    email = email,
                    password = password
                )
                viewModel.register(userToRegister)
            }
        }

        toLogin.setOnClickListener{
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        lifecycleScope.launch{
            viewModel.registerState.collect{ registerState ->
                when(registerState){
                    RegisterState.Loading -> {}
                    is RegisterState.Success -> {
                        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                    }
                    is RegisterState.Error -> {
                        Toast.makeText(context, getString(R.string.error_average_error), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}