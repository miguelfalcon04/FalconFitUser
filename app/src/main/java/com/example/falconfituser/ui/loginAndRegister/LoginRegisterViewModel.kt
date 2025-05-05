package com.example.falconfituser.ui.loginAndRegister

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.falconfituser.authentication.AuthenticationService
import com.example.falconfituser.data.api.loginRegister.LoginRaw
import com.example.falconfituser.data.api.loginRegister.RegisterRaw
import com.example.falconfituser.data.loginRegister.LoginRegisterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@HiltViewModel
class LoginRegisterViewModel @Inject constructor(
    private val loginRegisterRepository: LoginRegisterRepository,
    private val authenticationService: AuthenticationService,
): ViewModel() {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Loading)
    val loginState: StateFlow<LoginState>
        get() = _loginState.asStateFlow()

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Loading)
    val registerState: StateFlow<RegisterState>
        get() = _registerState.asStateFlow()


    fun loginFirebase(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            val auth = Firebase.auth

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { signInTask ->
                if (signInTask.isSuccessful) {
                    val user = auth.currentUser

                    user?.getIdToken(true)?.addOnCompleteListener { tokenTask ->
                        if (tokenTask.isSuccessful) {
                            // Obtengo el token como string
                            val idToken = tokenTask.result.token

                            // Guardo el token y el uid del usuario
                            if (idToken != null) {
                                authenticationService.clearCredentials()

                                authenticationService.saveId(user.uid)
                                authenticationService.saveJwtToken(idToken)

                                _loginState.value = LoginState.Success("Usuario autenticado en Firebase")
                            } else {
                                _loginState.value = LoginState.Error("No se pudo obtener el token")
                            }
                        } else {
                            _loginState.value = LoginState.Error("Error al obtener el token: ${tokenTask.exception?.message}")
                        }
                    }
                } else {
                    // Error al iniciar sesión
                    _loginState.value = LoginState.Error("Error de autenticación: ${signInTask.exception?.message}")
                }
            }
        } else {
            _loginState.value = LoginState.Error("Faltan credenciales para completar el login")
        }
    }

    fun registerFirebase(email:String, password:String){
        if (email.isNotEmpty() && password.isNotEmpty()){
            val auth = Firebase.auth

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
                if (it.isSuccessful){
                    authenticationService.clearCredentials()
                    _registerState.value = RegisterState.Success()
                }else{
                    _registerState.value = RegisterState.Error("Usuario mal creado")
                }
            }
        }else{
            _registerState.value = RegisterState.Error("Error falta creedenciales")
        }
    }

    fun login(login: LoginRaw){
        viewModelScope.launch{
            try {
                val response = loginRegisterRepository.login(login)

                if (response.isSuccessful) { // Si la respuesta es válida
                    val body = response.body() // Cogemos la respuesta
                    if (body != null) {
                        //Limpiamos las antiguas creedenciales antes de añadir las nuevas
                        authenticationService.clearCredentials()

                        // Guardamos el Token y el Id en SharedPreferences
                        authenticationService.saveId(body.user.id.toString())
                        authenticationService.saveJwtToken(body.jwt)

                        // Damos el visto bueno para navegar
                        _loginState.value = LoginState.Success(body.user.id.toString())
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
                    authenticationService.clearCredentials()
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