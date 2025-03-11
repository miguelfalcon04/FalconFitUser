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

    fun clearCredentials() {
        sharedPreferences.edit().
        remove(JWT_KEY)
        .remove("USER_ID")
        .apply()
    }

    fun saveId(id: String){
        if (id.isNotBlank()) {
            sharedPreferences.edit()
                .putString("USER_ID", id)
                .apply()
        }
    }

    fun getId(): String {
        return sharedPreferences.getString("USER_ID", "0").toString()
    }
}