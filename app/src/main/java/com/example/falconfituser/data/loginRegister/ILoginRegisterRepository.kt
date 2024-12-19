package com.example.falconfituser.data.loginRegister

import com.example.falconfituser.data.api.loginRegister.LoginRaw
import com.example.falconfituser.data.api.loginRegister.LoginResponse
import com.example.falconfituser.data.api.loginRegister.RegisterRaw
import retrofit2.Response

interface ILoginRegisterRepository {
    suspend fun login(userToLogin: LoginRaw): Result<LoginResponse>
    suspend fun register(userToRegister: RegisterRaw): Result<LoginResponse>
}