package com.example.falconfituser.ui.loginAndRegister

import android.content.SharedPreferences
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.falconfituser.R
import com.example.falconfituser.data.api.loginRegister.LoginRaw
import com.example.falconfituser.data.api.loginRegister.RegisterRaw
import com.example.falconfituser.data.loginRegister.LoginRegisterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginRegisterViewModel @Inject constructor(
    private val loginRegisterRepository: LoginRegisterRepository,
    private val sharedPreferences: SharedPreferences
): ViewModel() {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Loading)
    val loginState: StateFlow<LoginState>
        get() = _loginState.asStateFlow()

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Loading)
    val registerState: StateFlow<RegisterState>
        get() = _registerState.asStateFlow()

    fun login(login: LoginRaw){
        viewModelScope.launch{
            try {
                val response = loginRegisterRepository.login(login)

                if (response.isSuccessful) { // Si la respuesta es válida
                    val body = response.body() // Cogemos la respuesta
                    if (body != null) {
                        val jwt = body.jwt // Extraemos el JWT
                        saveToken(jwt) // Guardamos el token en SharedPreferences
                        _loginState.value = LoginState.Success() // Navegamos
                    } else {
                        _loginState.value = LoginState.Error("La respuesta del servidor está vacía")
                    }
                } else { // Si viene una respuesta inválida
                    val errorMessage = "Error del servidor: ${response.code()} - ${response.message()}"
                    _loginState.value = LoginState.Error(errorMessage)
                }

            } catch (e: Exception){
                _loginState.value = LoginState.Error("Error desconocido")
            }
        }
    }

    fun register(register: RegisterRaw){
        viewModelScope.launch{
            try{
                loginRegisterRepository.register(register)
                _registerState.value = RegisterState.Success()
            } catch (e: Exception){
                _registerState.value = RegisterState.Error("Error desconocido")
            }
        }
    }

    private fun saveToken(token: String){
        sharedPreferences.edit()
            .putString("JWT_TOKEN",token)
            .apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString("JWT_TOKEN", null)
    }
}

sealed class LoginState {
    data object Loading: LoginState()
    class Success() : LoginState()
    class Error(val message: String) : LoginState()
}

sealed class RegisterState {
    data object Loading: RegisterState()
    class Success() : RegisterState()
    class Error(val message: String) : RegisterState()
}