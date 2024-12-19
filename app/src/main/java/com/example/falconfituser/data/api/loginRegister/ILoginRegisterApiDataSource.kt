package com.example.falconfituser.data.api.loginRegister

import retrofit2.Response

interface ILoginRegisterApiDataSource {
    suspend fun loginUser(userToLogin: LoginRaw): Result<LoginResponse>
    suspend fun registerUser(userToRegister: RegisterRaw): Result<LoginResponse>
}