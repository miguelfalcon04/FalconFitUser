package com.example.falconfituser.ui.loginAndRegister

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
                loginRegisterRepository.login(login)
                _loginState.value = LoginState.Success()
            } catch (e: Exception){
                _loginState.value = LoginState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun register(register: RegisterRaw){
        viewModelScope.launch{
            try{
                loginRegisterRepository.register(register)
                _registerState.value = RegisterState.Success()
            } catch (e: Exception){
                _registerState.value = RegisterState.Error(e.message ?: "Error desconocido")
            }
        }
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