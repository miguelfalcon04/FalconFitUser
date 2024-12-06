package com.example.falconfituser.data.loginRegister

import com.example.falconfituser.data.api.loginRegister.LoginRaw
import com.example.falconfituser.data.api.loginRegister.RegisterRaw

interface ILoginRegisterRepository {
    suspend fun login(userToLogin: LoginRaw)
    suspend fun register(userToRegister: RegisterRaw)
}