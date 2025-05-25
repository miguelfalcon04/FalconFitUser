package com.example.falconfituser.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.falconfituser.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import com.example.falconfituser.R
import com.example.falconfituser.data.Constants.Companion.USERFB
import com.example.falconfituser.di.FirestoreSigleton
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.example.falconfituser.data.loginRegister.User
import android.content.Context
import android.content.res.Configuration
import java.util.Locale

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var sharedPreferences: SharedPreferences

    val userId = Firebase.auth.currentUser!!.uid
    private val firestore = FirestoreSigleton.getInstance()
    private val userCollection = firestore.collection(USERFB)

    // Variables para controlar los cambios en los datos del usuario
    private var originalName = ""
    private var originalSurname = ""
    private var originalPhoneNumber = ""
    private var hasUnsavedChanges = false

    // Lista de idiomas y sus códigos
    private val languages = arrayOf("Español", "English", "Français", "Deutsch")
    private val languageCodes = arrayOf("es", "en", "fr", "de")

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

        // Configurar listeners para detectar cambios en los campos editables
        setupTextChangeListeners()

        // Configurar el botón de guardar cambios
        binding.btnSaveChanges.setOnClickListener {
            saveUserChanges()
        }

        // Configuración del botón de logout (funcionalidad original)
        val toLogin = binding.btnLogout
        toLogin.setOnClickListener{
            logout()
        }

        val switch = binding.switchDarkMode

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
        setupLanguageDropdown()

        // Cargar datos del usuario
        getUser()
    }


    /**
     * Configura los TextWatcher para detectar cambios en tiempo real
     * en los campos nombre, apellidos y teléfono
     */
    private fun setupTextChangeListeners() {
        // TextWatcher común para todos los campos editables
        val textWatcher = object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No necesitamos acción antes del cambio
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // No necesitamos acción durante el cambio
            }

            override fun afterTextChanged(s: android.text.Editable?) {
                // Después de cada cambio, verificamos si hay diferencias con los valores originales
                checkForChanges()
            }
        }

        // Aplicamos el listener a cada campo editable
        binding.nameSettings.addTextChangedListener(textWatcher)
        binding.surnameSettings.addTextChangedListener(textWatcher)
        binding.phoneNumberSettings.addTextChangedListener(textWatcher)
    }

    /**
     * Verifica si hay cambios pendientes comparando valores actuales con originales
     * Muestra u oculta el botón de guardar con animación suave
     */
    private fun checkForChanges() {
        val currentName = binding.nameSettings.text.toString()
        val currentSurname = binding.surnameSettings.text.toString()
        val currentPhone = binding.phoneNumberSettings.text.toString()

        // Comparamos cada campo con su valor original
        val nameChanged = currentName != originalName
        val surnameChanged = currentSurname != originalSurname
        val phoneChanged = currentPhone != originalPhoneNumber

        // Si cualquier campo cambió, hay cambios pendientes
        hasUnsavedChanges = nameChanged || surnameChanged || phoneChanged

        // Animación suave para mostrar/ocultar el botón
        if (hasUnsavedChanges) {
            // Mostrar botón si hay cambios y no está habilitado
            if (!binding.btnSaveChanges.isEnabled) {
                binding.btnSaveChanges.isEnabled = true
                binding.btnSaveChanges.alpha = 0f
                binding.btnSaveChanges.animate()
                    .alpha(1f)
                    .setDuration(300)
                    .start()
            }
        } else {
            // Ocultar botón si no hay cambios y está habilitado
            if (binding.btnSaveChanges.isEnabled) {
                binding.btnSaveChanges.animate()
                    .alpha(0f)
                    .setDuration(300)
                    .withEndAction {
                        binding.btnSaveChanges.isEnabled = false
                    }
                    .start()
            }
        }
    }

    /**
     * Guarda los cambios del usuario en Firebase Firestore
     */
    private fun saveUserChanges() {
        val newName = binding.nameSettings.text.toString().trim()
        val newSurname = binding.surnameSettings.text.toString().trim()
        val newPhoneNumber = binding.phoneNumberSettings.text.toString().trim()

        // Validación básica de campos requeridos
        if (newName.isEmpty() || newSurname.isEmpty()) {
            Toast.makeText(
                requireContext(),
                "Nombre y apellidos no pueden estar vacíos",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Preparamos los datos para actualizar en Firestore
        val updates = hashMapOf<String, Any>(
            "name" to newName,
            "surname" to newSurname,
            "phoneNumber" to newPhoneNumber
        )

        // Buscamos el documento del usuario actual
        userCollection
            .whereEqualTo("uuid", userId)
            .get()
            .addOnSuccessListener { snapshot ->
                val doc = snapshot.documents.firstOrNull()
                if (doc != null && doc.exists()) {
                    // Actualizamos el documento en Firestore
                    doc.reference.update(updates)
                        .addOnSuccessListener {
                            // Éxito: actualizamos los valores de referencia
                            originalName = newName
                            originalSurname = newSurname
                            originalPhoneNumber = newPhoneNumber
                            hasUnsavedChanges = false

                            // Esto ocultará el botón automáticamente
                            checkForChanges()

                            Toast.makeText(
                                requireContext(),
                                "Cambios guardados correctamente",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(
                                requireContext(),
                                "Error guardando cambios: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Usuario no encontrado para actualizar",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    requireContext(),
                    "Error buscando usuario: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    /**
     * Carga los datos del usuario desde Firestore
     * Esta función ahora también guarda los valores originales para comparación
     */
    private fun getUser() {
        userCollection
            .whereEqualTo("uuid", userId)
            .get()
            .addOnSuccessListener { snapshot ->
                val doc = snapshot.documents.firstOrNull()
                if (doc != null && doc.exists()) {
                    val user = doc.toObject(User::class.java)
                    if (user != null) {
                        // Guardamos los valores originales para posterior comparación
                        originalName = user.name ?: ""
                        originalSurname = user.surname ?: ""
                        originalPhoneNumber = user.phoneNumber ?: ""

                        // Poblamos los campos de la interfaz
                        binding.apply {
                            nameSettings.setText(user.name)
                            surnameSettings.setText(user.surname)
                            phoneNumberSettings.setText(user.phoneNumber)
                            emailSettings.setText(user.email)
                            registrationDateSettings.setText(user.registerDate)

                            // Cargamos la imagen de perfil
                            if(user.picture != "" && user.picture != null){
                                avatarImageView.load(user.picture)
                            } else {
                                avatarImageView.load(R.drawable.help)
                            }
                        }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "No se pudo obtener valores del usuario",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Usuario no encontrado",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    requireContext(),
                    "Error cargando usuario: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    /**
     * Configure user interface (Light/Dark)
     */
    private suspend fun setUIMode(isChecked: Boolean) {
        if (isChecked) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        UiModePreferences.saveToDataStore(requireContext(), isChecked)
    }

    /**
     * Close user session and navigate to login
     */
    private fun logout() {
        // Cerrar sesión Firebase
        FirebaseAuth.getInstance().signOut()

        // Guardamos el estado del tema y idioma para preservar preferencias
        val currentThemeMode = sharedPreferences.getBoolean("ui_mode", false)
        val currentLanguage = sharedPreferences.getBoolean("language_preference", false)

        // Limpiamos todas las preferencias
        sharedPreferences.edit().clear().apply()

        // Restauramos las preferencias de UI
        sharedPreferences.edit()
            .putBoolean("ui_mode", currentThemeMode)
            .putBoolean("language_preference", currentLanguage)
            .apply()

        // Navegamos al login y limpiamos el back stack
        findNavController().navigate(
            R.id.loginFragment,
            null,
            NavOptions.Builder()
                .setPopUpTo(R.id.main, inclusive = true)
                .build()
        )
    }

    private fun setupLanguageDropdown() {
        val adapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_dropdown_item_1line, languages)

        binding.languageDropdown.setAdapter(adapter)

        // Establecer idioma actual
        val currentLanguage = getCurrentLanguage()
        val currentIndex = languageCodes.indexOf(currentLanguage)
        if (currentIndex != -1) {
            binding.languageDropdown.setText(languages[currentIndex], false)
        }

        binding.languageDropdown.setOnItemClickListener { _, _, position, _ ->
            changeLanguage(languageCodes[position])
        }
    }

    private fun changeLanguage(languageCode: String) {
        // Guardar preferencia
        val sharedPrefs = requireContext().getSharedPreferences("falcon_fit_prefs", Context.MODE_PRIVATE)
        sharedPrefs.edit().putString("selected_language", languageCode).apply()

        // Cambiar idioma
        setLocale(languageCode)

        // Reiniciar actividad para aplicar cambios
        requireActivity().recreate()
    }

    private fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration()
        config.setLocale(locale)

        requireContext().resources.updateConfiguration(config,
            requireContext().resources.displayMetrics)
    }

    private fun getCurrentLanguage(): String {
        val sharedPrefs = requireContext().getSharedPreferences("falcon_fit_prefs", Context.MODE_PRIVATE)
        return sharedPrefs.getString("selected_language", "es") ?: "es"
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}