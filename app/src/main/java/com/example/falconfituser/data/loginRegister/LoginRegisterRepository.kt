package com.example.falconfituser.data.loginRegister

import com.example.falconfituser.data.api.loginRegister.ILoginRegisterApiDataSource
import com.example.falconfituser.data.api.loginRegister.LoginRaw
import com.example.falconfituser.data.api.loginRegister.RegisterRaw
import javax.inject.Inject

class LoginRegisterRepository @Inject constructor(
    private val apiData: ILoginRegisterApiDataSource
): ILoginRegisterRepository {
    override suspend fun login(userToLogin: LoginRaw) {
        apiData.loginUser(userToLogin)
    }

    override suspend fun register(userToRegister: RegisterRaw) {
        apiData.registerUser(userToRegister)
    }
}