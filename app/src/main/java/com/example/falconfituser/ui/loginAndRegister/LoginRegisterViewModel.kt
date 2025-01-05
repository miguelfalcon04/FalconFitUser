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
                        clearCredentials() //Limpiamos las antiguas creedenciales antes de añadir las nuevas

                        saveId(body.user.id.toString())
                        saveToken(body.jwt) // Guardamos el token en SharedPreferences
                        _loginState.value = LoginState.Success(body.user.id.toString()) // Damos el visto bueno para navegar
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
                val response = loginRegisterRepository.register(register)

                if(response.isSuccessful){
                    clearCredentials()
                    _registerState.value = RegisterState.Success()
                } else {
                    val errorBody = response.errorBody()?.string()
                    _registerState.value = RegisterState.Error(
                        "Error en el registro: ${response.code()} - $errorBody"
                    )
                }
            } catch (e: Exception){
                _registerState.value = RegisterState.Error("Error desconocido")
            }
        }
    }

    // Limpio las credenciales por si hago login, logout y login de nuevo con otro usuario
    private fun clearCredentials() {
        sharedPreferences.edit()
            .remove("JWT_TOKEN")
            .remove("USER_ID")
            .apply()
    }

    private fun saveToken(token: String){
        if (token.isNotBlank()) {
            sharedPreferences.edit()
                .putString("JWT_TOKEN", token)
                .apply()
        }
    }

    fun getToken(): String? {
        return sharedPreferences.getString("JWT_TOKEN", null)
    }

    fun saveId(id: String){
        if (id.isNotBlank()) {
            sharedPreferences.edit()
                .putString("USER_ID", id)
                .apply()
        }
    }

    fun getId(): String? {
        return sharedPreferences.getString("USER_ID", null)
    }
}

sealed class LoginState {
    data object Loading: LoginState()
    class Success(val message: String) : LoginState()
    class Error(val message: String) : LoginState()
}

sealed class RegisterState {
    data object Loading: RegisterState()
    class Success() : RegisterState()
    class Error(val message: String) : RegisterState()
}