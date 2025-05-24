package com.example.falconfituser.ui.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// No Chatgpt
// No tenia ningun ejemplo de clase. He seguido este video para hacerlo https://www.youtube.com/watch?v=uRXmvvKEQVU&ab_channel=BoltUIX
object UiModePreferences {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "ui_mode_preferences")

    suspend fun saveToDataStore(context: Context, isNightMode: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[UI_MODE_KEY] = isNightMode
        }
    }

    fun uiMode(context: Context): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[UI_MODE_KEY] ?: false
        }
    }


    fun languagePreference(context: Context): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[LANGUAGE_KEY] ?: false
        }
    }

    private val LANGUAGE_KEY = booleanPreferencesKey("language")
    private val UI_MODE_KEY = booleanPreferencesKey("ui_mode")
}