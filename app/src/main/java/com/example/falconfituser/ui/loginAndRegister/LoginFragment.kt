package com.example.falconfituser.ui.loginAndRegister

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.falconfituser.R
import com.example.falconfituser.data.api.loginRegister.LoginRaw
import com.example.falconfituser.data.worker.WorkManagerHelper
import com.example.falconfituser.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private var PERMISSIONS_REQUIRED = arrayOf(Manifest.permission.POST_NOTIFICATIONS)

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginRegisterViewModel by viewModels()

    val contract = ActivityResultContracts.RequestMultiplePermissions()
    val launcher = registerForActivityResult(contract){
        permissions ->
        var granted = true
        permissions.entries.forEach{
            permission ->
                if(permission.key in PERMISSIONS_REQUIRED && !permission.value){
                    granted = false
                }
        }

        if(!granted){
            Toast.makeText(requireContext(), "No se enviaran notificaciones", Toast.LENGTH_LONG).show()
        }
    }

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

        if (!hasNotisPermissions(requireContext())){
            launcher.launch(PERMISSIONS_REQUIRED)
        }

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
            WorkManagerHelper.testNotificationNow(requireContext())
            Toast.makeText(
                requireContext(),
                "Notificación programada para 5 segundos",
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        lifecycleScope.launch{
            viewModel.loginState.collect{ loginState ->
                when(loginState){
                    LoginState.Loading -> {}
                    is LoginState.Success -> {
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

    // Comprobar si tenemos permiso
    private fun hasNotisPermissions(context: Context):Boolean{
        return PERMISSIONS_REQUIRED.all{
                permission ->
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        }
    }
}