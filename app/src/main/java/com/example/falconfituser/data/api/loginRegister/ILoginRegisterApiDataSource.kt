package com.example.falconfituser.data.api.loginRegister

interface ILoginRegisterApiDataSource {
    suspend fun loginUser(userToLogin: LoginRaw)
    suspend fun registerUser(userToRegister: RegisterRaw)
}