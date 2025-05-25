package com.example.falconfituser.authentication

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Singleton class that manages authentication tokens and user data storage.
 * Uses dependency injection for SharedPreferences access.
 */
@Singleton
class AuthenticationService @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    companion object {
        private const val JWT_KEY = "JWT_TOKEN"
    }

    /**
     * Retrieves stored JWT token.
     * @return JWT token string or null if not found
     */
    fun getJwtToken(): String? {
        return sharedPreferences.getString(JWT_KEY, null)
    }

    /**
     * Saves JWT token to persistent storage.
     * @param token JWT token to store
     */
    fun saveJwtToken(token: String) {
        sharedPreferences.edit().putString(JWT_KEY, token).apply()
    }

    /**
     * Clears token and user ID.
     * Call this during logout.
     */
    fun clearCredentials() {
        sharedPreferences.edit()
            .remove(JWT_KEY)
            .remove("USER_ID")
            .apply()
    }

    /**
     * Saves user ID with validation.
     * @param id User ID to store
     */
    fun saveId(id: String) {
        if (id.isNotBlank()) {
            sharedPreferences.edit()
                .putString("USER_ID", id)
                .apply()
        }
    }

    /**
     * Retrieves stored user ID.
     * @return User ID string, defaults to "0" if not found
     */
    fun getId(): String {
        return sharedPreferences.getString("USER_ID", "0").toString()
    }
}