package com.example.falconfituser.ui.superset

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.falconfituser.authentication.AuthenticationService
import com.example.falconfituser.data.exercise.Exercise
import com.example.falconfituser.data.exercise.IExerciseRepository
import com.example.falconfituser.data.superset.ISupersetRepository
import com.example.falconfituser.data.superset.Superset
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateUpdateSupersViewModel @Inject constructor(
    private val supersetRepository: ISupersetRepository,
    private val exerciseRepository: IExerciseRepository,
    private val authenticationService: AuthenticationService,
): ViewModel(){
    private val _uiState = MutableStateFlow<CreateUpdateSupersUiState>(CreateUpdateSupersUiState.Loading)
    val uiState: StateFlow<CreateUpdateSupersUiState>
        get() = _uiState.asStateFlow()

    val userId = authenticationService.getId()

    fun createSuperset(superset: Superset) {
        viewModelScope.launch{
            supersetRepository.createSuperset(superset)
        }
    }

    fun updateSuperset(supersetId: Int, superset: Superset){
        viewModelScope.launch{
            supersetRepository.updateSuperset(supersetId, superset)
        }
    }

    private fun loadExercises() {
        viewModelScope.launch {
            val exercises = exerciseRepository.readAll(userId)

            _uiState.value = if (exercises.isNotEmpty()) {
                CreateUpdateSupersUiState.Success(exercises)
            } else {
                CreateUpdateSupersUiState.Error("Error al obtener los ejercicios")
            }
        }
    }

    init {
        loadExercises()
    }
}

sealed class CreateUpdateSupersUiState(){
    data object Loading: CreateUpdateSupersUiState()
    class Success(val exrcList: List<Exercise>): CreateUpdateSupersUiState()
    class Error(val message: String): CreateUpdateSupersUiState()
}