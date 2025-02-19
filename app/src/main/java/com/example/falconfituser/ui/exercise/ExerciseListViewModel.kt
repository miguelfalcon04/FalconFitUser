package com.example.falconfituser.ui.exercise

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.falconfituser.data.exercise.Exercise
import com.example.falconfituser.data.exercise.IExerciseRepository
import com.example.falconfituser.data.exercise.toExternal
import com.example.falconfituser.data.exercise.toLocal
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

    init {
        loadExercises()
        // Elimino cualquier job existente
        collectionJob?.cancel()

        // Inicio una nueva suscripción
        collectionJob = viewModelScope.launch {
            withContext(Dispatchers.Main) {
                exerciseRepository.setStream.collect { exercList ->
                    if (exercList.isEmpty()) {
                        _uiState.value = ExercListUiState.Loading
                    } else {
                        _uiState.value = ExercListUiState.Success(exercList)
                    }
                }
            }
        }
    }

    private fun loadExercises() {
        viewModelScope.launch {
            val userId = sharedPreferences.getString("USER_ID", null)?.toIntOrNull() ?: 0

            val res = exerciseRepository.readAll(userId)
            if(res.isNotEmpty()){
                _uiState.value = ExercListUiState.Success(res)

                for (exercise in res){
                    localRepository.createExercise(exercise.toLocal(userId))
                }
            }else{
                localRepository.getExercisesByUser(userId).collect{
                    localExercise ->
                    _uiState.value = ExercListUiState.Success(
                        localExercise.map { it.toExternal() }
                    )
                }
            }

        }
    }

    fun deleteExercise(exerciseId: Int) {
        viewModelScope.launch {
            exerciseRepository.deleteExercise(exerciseId)
            localRepository.deleteExercise(exerciseId)
            loadExercises()
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