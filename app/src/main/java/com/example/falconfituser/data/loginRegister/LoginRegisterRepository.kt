package com.example.falconfituser.data.loginRegister

import com.example.falconfituser.data.api.loginRegister.ILoginRegisterApiDataSource
import com.example.falconfituser.data.api.loginRegister.LoginRaw
import com.example.falconfituser.data.api.loginRegister.LoginResponse
import com.example.falconfituser.data.api.loginRegister.RegisterRaw
import retrofit2.Response
import javax.inject.Inject

class LoginRegisterRepository @Inject constructor(
    private val apiData: ILoginRegisterApiDataSource
): ILoginRegisterRepository {
    override suspend fun login(userToLogin: LoginRaw):Response<LoginResponse> {
        return apiData.loginUser(userToLogin)
    }

    override suspend fun register(userToRegister: RegisterRaw): Response<LoginResponse> {
        return apiData.registerUser(userToRegister)
    }
}