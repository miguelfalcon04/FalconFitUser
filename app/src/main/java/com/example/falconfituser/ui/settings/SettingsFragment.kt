package com.example.falconfituser.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.falconfituser.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.falconfituser.R
import com.example.falconfituser.data.Constants.Companion.USERFB
import com.example.falconfituser.data.superset.Superset
import com.example.falconfituser.di.FirestoreSigleton
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.example.falconfituser.data.loginRegister.User
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var sharedPreferences: SharedPreferences

    val userId = Firebase.auth.currentUser!!.uid
    private val firestore = FirestoreSigleton.getInstance()
    private val userCollection = firestore.collection(USERFB)

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

        // Inicializa sharedPreferences
        sharedPreferences = requireContext().getSharedPreferences("falcon_fit_prefs", 0)

        val toLogin = binding.logoutButton
        toLogin.setOnClickListener{
            logout()
        }

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

    private fun getUser(){
        userCollection.whereEqualTo("userId", userId).get().addOnSuccessListener { querySnapshot ->
            val userList = mutableListOf<User>()
            for (document in querySnapshot.documents){
                val user = document.toObject(User::class.java)

                // Lee la referencia del documento y la guarda localmente en cada Superset.
                // Por eso al verlo en Firebase la variable document es null pero realmente la
                // tomo aquí

                /*
                supersetWithDocId.let {
                    userList.add(it)
                }
                */
            }
            // _state.value = userList.toList()
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
        withContext(Dispatchers.Main) { // Para que se ejecute en Application y no de error de 2 DataStore
            val languageTag = if (isChecked) "en" else "es"
            val localeList = LocaleListCompat.forLanguageTags(languageTag)
            AppCompatDelegate.setApplicationLocales(localeList)
        }
        UiModePreferences.saveLanguageToDataStore(requireContext(), isChecked)
    }

    private fun logout() {
        // Cerrar sesión Firebase
        FirebaseAuth.getInstance().signOut()

        // Guardamo el estado del tema y idioma
        val currentThemeMode = sharedPreferences.getBoolean("ui_mode", false)
        val currentLanguage = sharedPreferences.getBoolean("language_preference", false)

        // Lo elimino todoo
        sharedPreferences.edit().clear().apply()

        // Aplico las preferencias de UI
        sharedPreferences.edit()
            .putBoolean("ui_mode", currentThemeMode)
            .putBoolean("language_preference", currentLanguage)
            .apply()

        // Navegamo al login y elimino los demas fragmentos del back stack con el setPopUpTo
        findNavController().navigate(
            R.id.loginFragment,
            null,
            NavOptions.Builder()
                .setPopUpTo(R.id.main, inclusive = true)
                .build()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

}
