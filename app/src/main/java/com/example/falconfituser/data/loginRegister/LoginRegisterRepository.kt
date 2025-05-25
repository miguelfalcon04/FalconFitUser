package com.example.falconfituser.data.loginRegister

import com.example.falconfituser.data.api.loginRegister.ILoginRegisterApiDataSource
import com.example.falconfituser.data.api.loginRegister.LoginRaw
import com.example.falconfituser.data.api.loginRegister.LoginResponse
import com.example.falconfituser.data.api.loginRegister.RegisterRaw
import retrofit2.Response
import javax.inject.Inject

/**
 * Repository class that handles user login and registration operations.
 * Delegates the work to the injected API data source.
 */
class LoginRegisterRepository @Inject constructor(
    private val apiData: ILoginRegisterApiDataSource
): ILoginRegisterRepository {

    /**
     * Logs in a user by sending credentials to the API.
     * @param userToLogin Object containing user identifier and password
     * @return Response containing JWT and user information if successful
     */
    override suspend fun login(userToLogin: LoginRaw): Response<LoginResponse> {
        return apiData.loginUser(userToLogin)
    }

    /**
     * Registers a new user via the API.
     * @param userToRegister Object containing email, username, and password
     * @return Response containing JWT and new user data if successful
     */
    override suspend fun register(userToRegister: RegisterRaw): Response<LoginResponse> {
        return apiData.registerUser(userToRegister)
    }
}