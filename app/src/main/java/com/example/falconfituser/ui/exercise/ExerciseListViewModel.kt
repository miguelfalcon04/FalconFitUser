package com.example.falconfituser.ui.exercise

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.falconfituser.data.exercise.Exercise
import com.example.falconfituser.data.exercise.IExerciseRepository
import com.example.falconfituser.data.exercise.toExternal
import com.example.falconfituser.data.local.LocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ExerciseListViewModel @Inject constructor(
    private val exerciseRepository: IExerciseRepository,
    private val localRepository: LocalRepository,
    private val sharedPreferences: SharedPreferences
): ViewModel() {
    private val _uiState = MutableStateFlow<ExercListUiState>(ExercListUiState.Loading)
    val uiState: StateFlow<ExercListUiState>
        get() = _uiState.asStateFlow()

    // Añado Job para manejar la subscripción y poder eliminarla cuando quiera
    // Así al cerrar sesión e inicar de nuevo con otro usuario, no se mostrará los datos del otro
    // Debido a que volverá subscribirse. Si no, la subscripción permanecería y no haría el init
    private var collectionJob: Job? = null

    fun initialize() {
        // Elimino cualquier job existente
        collectionJob?.cancel()

        // Inicio una nueva suscripción
        collectionJob = viewModelScope.launch {
            launch(Dispatchers.Main) {
                exerciseRepository.setStream.collect { exercList ->
                    if (exercList.isEmpty()) {
                        _uiState.value = ExercListUiState.Loading
                    } else {
                        _uiState.value = ExercListUiState.Success(exercList)
                    }
                }
            }

            // Cargo los ejercicios
            loadExercises()
        }
    }

    private fun loadExercises() {
        viewModelScope.launch {
            val userId = sharedPreferences.getString("USER_ID", null)?.toIntOrNull() ?: 0

            // Tomo los valores del remoto. Si da error devuelvo una emptyList()
            val remoteExercises: List<Exercise> = try {
                withContext(Dispatchers.IO) {
                    exerciseRepository.readAll(userId)
                }
            } catch (e: Exception) {
                emptyList()
            }

            if (remoteExercises.isNotEmpty()) {
                // Si el remoto devolve datos los mostramos
                _uiState.value = ExercListUiState.Success(remoteExercises)
            } else {
                // Si no hay datos remotos cargo los datos locales
                withContext(Dispatchers.IO) {
                    localRepository.getExercisesByUser(userId).collect { localExercisesEntity ->
                        _uiState.value = ExercListUiState.Success(
                            localExercisesEntity.map { it.toExternal() }
                        )
                    }
                }
            }
        }
    }

    fun deleteExercise(exerciseId: Int) {
        viewModelScope.launch {
            exerciseRepository.deleteExercise(exerciseId)
            withContext(Dispatchers.IO) {
                loadExercises()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        collectionJob?.cancel() // Limpiamo al destruir el ViewModel
    }

}

sealed class ExercListUiState(){
    data object Loading: ExercListUiState()
    class Success(val exrcList: List<Exercise>): ExercListUiState()
    class Error(val message: String): ExercListUiState()
}