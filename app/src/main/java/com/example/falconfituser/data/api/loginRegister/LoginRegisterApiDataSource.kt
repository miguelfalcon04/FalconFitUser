package com.example.falconfituser.data.api.loginRegister

import com.example.falconfituser.data.api.IFalconFitApi
import javax.inject.Inject

class LoginRegisterApiDataSource @Inject constructor(
    private val ffApi: IFalconFitApi
): ILoginRegisterApiDataSource {
    override suspend fun loginUser(userToLogin: LoginRaw) {
        ffApi.login(userToLogin)
    }

    override suspend fun registerUser(userToRegister: RegisterRaw) {
        ffApi.register(userToRegister)
    }
}