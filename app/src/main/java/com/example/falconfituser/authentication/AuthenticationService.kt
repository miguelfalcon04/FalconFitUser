package com.example.falconfituser.authentication

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticationService @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    companion object {
        private const val JWT_KEY = "JWT_TOKEN"
    }

    fun getJwtToken(): String? {
        return sharedPreferences.getString(JWT_KEY, null)
    }

    fun isAuthenticated(): Boolean {
        return getJwtToken() != null
    }

    fun saveJwtToken(token: String) {
        sharedPreferences.edit().putString(JWT_KEY, token).apply()
    }

    fun clearJwtToken() {
        sharedPreferences.edit().remove(JWT_KEY).apply()
    }
}