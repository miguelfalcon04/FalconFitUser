package com.example.falconfituser.ui.exercise

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.falconfituser.data.exercise.Exercise
import com.example.falconfituser.data.exercise.IExerciseRepository
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
    private val sharedPreferences: SharedPreferences
): ViewModel() {
    private val _uiState = MutableStateFlow<ExercListUiState>(ExercListUiState.Loading)
    val uiState: StateFlow<ExercListUiState>
        get() = _uiState.asStateFlow()

    // Añado Job para manejar la subscripción y poder eliminarla cuando quiera
    // Así al cerrar sesión e inicar de nuevocon otro usuario, no se mostrará los datos del otro
    // Debido a que volverá subscribirse. Si no, la subscripción permanecería y no haría el init
    private var collectionJob: Job? = null

    fun initialize() {
        // Elimano cualquier job existente
        collectionJob?.cancel()

        // Iniciamo una nueva suscripción
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

    fun loadExercises() {
        viewModelScope.launch {
            val userId = sharedPreferences.getString("USER_ID", null)?.toIntOrNull() ?: 0
            withContext(Dispatchers.IO) {
                exerciseRepository.readAll(userId)
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