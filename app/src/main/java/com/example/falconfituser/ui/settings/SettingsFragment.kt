package com.example.falconfituser.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.falconfituser.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val switch = binding.darkThemeSwitch
        val languageSwitch = binding.languageSwitch

        // TEMA OSCURO O CLARO
        lifecycleScope.launch {
            UiModePreferences.uiMode(requireContext()).collect { isNightMode ->
                switch.isChecked = isNightMode
            }
        }
        switch.setOnCheckedChangeListener { _, isChecked ->
            lifecycleScope.launch {
                setUIMode(isChecked)
            }
        }

        // IDIOMA
        lifecycleScope.launch {
            UiModePreferences.languagePreference(requireContext()).collect { isEnglish ->
                languageSwitch.isChecked = isEnglish
            }
        }
        languageSwitch.setOnCheckedChangeListener { _, isChecked ->
            lifecycleScope.launch {
                setLanguage(isChecked)
            }
        }
    }

    private suspend fun setUIMode(isChecked: Boolean) {
        if (isChecked) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        UiModePreferences.saveToDataStore(requireContext(), isChecked)
    }

    private suspend fun setLanguage(isChecked: Boolean) {
        withContext(Dispatchers.Main) { // Asegura que se ejecuta en el hilo principal
            val languageTag = if (isChecked) "en" else "es"
            val localeList = LocaleListCompat.forLanguageTags(languageTag)
            AppCompatDelegate.setApplicationLocales(localeList)
        }
        UiModePreferences.saveLanguageToDataStore(requireContext(), isChecked)
    }

}
