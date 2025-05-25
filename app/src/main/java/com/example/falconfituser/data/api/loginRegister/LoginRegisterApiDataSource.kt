package com.example.falconfituser.data.api.loginRegister

import com.example.falconfituser.data.api.IFalconFitApi
import retrofit2.Response
import javax.inject.Inject

/**
 * Data source class that handles login and register API calls.
 * Implements the repository pattern by delegating API calls to Retrofit interface.
 */
class LoginRegisterApiDataSource @Inject constructor(
    private val ffApi: IFalconFitApi
): ILoginRegisterApiDataSource {

    /**
     * Make the login for an user.
     * @param userToLogin User identifier and password
     * @return Response containing the jwt and user logged
     */
    override suspend fun loginUser(userToLogin: LoginRaw): Response<LoginResponse> {
        return ffApi.login(userToLogin)
    }

    /**
     * Register a new user.
     * @param userToRegister User identifier, password and username
     * @return Response containing the jwt and user logged
     */
    override suspend fun registerUser(userToRegister: RegisterRaw): Response<LoginResponse> {
        return ffApi.register(userToRegister)
    }
}