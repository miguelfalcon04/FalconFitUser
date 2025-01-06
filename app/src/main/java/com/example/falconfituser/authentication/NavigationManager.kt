package com.example.falconfituser.authentication

import javax.inject.Inject
import javax.inject.Singleton
import com.example.falconfituser.R

@Singleton
class NavigationManager @Inject constructor(
    private val authenticationService: AuthenticationService
) {
    // Lista de los ids que necesitan autentificación
    private val authenticatedDestinations: Set<Int> = setOf(
        R.id.supersetListFragment,
        R.id.exerciseListFragment,
        R.id.createExerciseFragment,
        R.id.createUpdateSupersetFragment,
        R.id.settingsFragment
    )

    // Mira si el fragmento neceita autentificación y si el usuario no está aitenticado
    // Un ejemplo es
    // Este destino está en mi lista de pantallas protegidas? Si
    // El usuario no está autenticado? Si
    // Entonces lo redirige al login en el MainActivity
    fun shouldNavigateToLogin(destinationId: Int): Boolean {
        return authenticatedDestinations.contains(destinationId) && !authenticationService.isAuthenticated()
    }
}