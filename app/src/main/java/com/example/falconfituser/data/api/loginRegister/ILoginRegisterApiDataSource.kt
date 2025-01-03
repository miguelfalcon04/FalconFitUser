package com.example.falconfituser.data.api.loginRegister

import retrofit2.Response

interface ILoginRegisterApiDataSource {
    suspend fun loginUser(userToLogin: LoginRaw): Response<LoginResponse>
    suspend fun registerUser(userToRegister: RegisterRaw): Response<LoginResponse>
}