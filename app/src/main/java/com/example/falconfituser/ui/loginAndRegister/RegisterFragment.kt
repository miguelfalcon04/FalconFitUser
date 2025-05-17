package com.example.falconfituser.ui.loginAndRegister

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.falconfituser.R
import com.example.falconfituser.data.loginRegister.User
import com.example.falconfituser.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
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

        val btnRegister = binding.registerUserButton
        val toLogin = binding.goLoginButton

        btnRegister.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val surname = binding.surnameEditText.text.toString()
            val email = binding.emailTextView.text.toString()
            val password = binding.passwordEditTextRegister.text.toString()

            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = dateFormat.format(calendar.time)

            if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || password.isEmpty()){
                Toast.makeText(context, getString(R.string.error_fill_all), Toast.LENGTH_SHORT).show()
            }else{
                val userToRegister = User(
                    name = name,
                    surname = surname,
                    email = email,
                    registerDate = date
                )
                viewModel.registerFirebase(email, password, userToRegister  )
                // viewModel.register(userToRegister)
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