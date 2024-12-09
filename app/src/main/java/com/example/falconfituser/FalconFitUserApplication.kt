package com.example.falconfituser

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.example.falconfituser.ui.settings.UiModePreferences
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltAndroidApp
class FalconFitUserApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Para cuando inicie obtener el tema directamente sin tener que navegar al fragmento
        CoroutineScope(Dispatchers.IO).launch {
            UiModePreferences.uiMode(applicationContext).collectLatest { isNightMode ->
                withContext(Dispatchers.Main) {
                    val mode = if (isNightMode) {
                        AppCompatDelegate.MODE_NIGHT_YES
                    } else {
                        AppCompatDelegate.MODE_NIGHT_NO
                    }
                    AppCompatDelegate.setDefaultNightMode(mode)
                }
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            UiModePreferences.languagePreference(applicationContext).collectLatest { isEnglish ->
                withContext(Dispatchers.Main) {
                    val languageTag = if (isEnglish) "en" else "es"
                    val localeList = LocaleListCompat.forLanguageTags(languageTag)
                    AppCompatDelegate.setApplicationLocales(localeList)
                }
            }
        }
    }
}



