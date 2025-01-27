package com.example.falconfituser.ui.loginAndRegister

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.falconfituser.R
import com.example.falconfituser.data.api.loginRegister.LoginRaw
import com.example.falconfituser.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginRegisterViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnLogin = view.findViewById<Button>(R.id.loginButton)
        val getEmail = view.findViewById<EditText>(R.id.emailEditText)
        val getPassword = view.findViewById<EditText>(R.id.passwordEditText)
        val toRegister = view.findViewById<Button>(R.id.registerButton)

        btnLogin.setOnClickListener {
            val email = getEmail.text.toString()
            val password = getPassword.text.toString()
            if (email.isEmpty() && password.isEmpty()){
                Toast.makeText(context, getString(R.string.error_invalid_credentials), Toast.LENGTH_SHORT).show()
            }else{
                val userToLogin = LoginRaw(
                    identifier = email,
                    password = password
                )
                viewModel.login(userToLogin)
            }
        }

        toRegister.setOnClickListener{
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        lifecycleScope.launch{
            viewModel.loginState.collect{ loginState ->
                when(loginState){
                    LoginState.Loading -> {}
                    is LoginState.Success -> {
                        // Toast.makeText(context, loginState.message.toString(), Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_loginFragment_to_machine)
                    }
                    is LoginState.Error -> {
                        Toast.makeText(context, loginState.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}