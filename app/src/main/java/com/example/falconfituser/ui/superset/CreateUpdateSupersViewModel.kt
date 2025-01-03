package com.example.falconfituser.ui.superset

import android.content.SharedPreferences
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
    private val exerciseRepository: IExerciseRepository,
    private val sharedPreferences: SharedPreferences
): ViewModel(){
    private val _uiState = MutableStateFlow<CreateUpdateSupersUiState>(CreateUpdateSupersUiState.Loading)
    val uiState: StateFlow<CreateUpdateSupersUiState>
        get() = _uiState.asStateFlow()


    //TODO() RELACIONAR CON USARIO
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
                    val userId = sharedPreferences.getString("USER_ID", null)?.toIntOrNull() ?: 0
                    val exercises = exerciseRepository.readAll(userId)
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