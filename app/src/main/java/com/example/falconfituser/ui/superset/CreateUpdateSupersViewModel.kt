package com.example.falconfituser.ui.superset

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.falconfituser.data.api.superset.SupersetPost
import com.example.falconfituser.data.exercise.Exercise
import com.example.falconfituser.data.exercise.IExerciseRepository
import com.example.falconfituser.data.superset.ISupersetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CreateUpdateSupersViewModel @Inject constructor(
    private val supersetRepository: ISupersetRepository,
    private val exerciseRepository: IExerciseRepository

): ViewModel(){
    private val _uiState = MutableStateFlow<CreateUpdateSupersUiState>(CreateUpdateSupersUiState.Loading)
    val uiState: StateFlow<CreateUpdateSupersUiState>
        get() = _uiState.asStateFlow()


    fun createSuperset(superset: SupersetPost){
        viewModelScope.launch{
            supersetRepository.createSuperset(superset)
            supersetRepository.readAll()
        }
    }

    fun updateSuperset(supersetId: Int, superset: SupersetPost){
        viewModelScope.launch{
            supersetRepository.updateSuperset(supersetId, superset)
            supersetRepository.readAll()
        }
    }

    private fun loadExercises() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val exercises = exerciseRepository.readAll()
                    _uiState.value = CreateUpdateSupersUiState.Success(exercises)
                } catch (e: Exception) {
                    _uiState.value = CreateUpdateSupersUiState.Error(e.message ?: "Error desconocido")
                }
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