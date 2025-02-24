package com.example.falconfituser.ui.superset

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.falconfituser.data.api.superset.SupersetListRaw
import com.example.falconfituser.data.api.superset.SupersetPost
import com.example.falconfituser.data.api.superset.SupersetRaw
import com.example.falconfituser.data.exercise.Exercise
import com.example.falconfituser.data.exercise.IExerciseRepository
import com.example.falconfituser.data.local.LocalRepository
import com.example.falconfituser.data.superset.ISupersetRepository
import com.example.falconfituser.data.superset.toLocal
import com.example.falconfituser.ui.exercise.ExercListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
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

    fun createSuperset(superset: SupersetPost) {
        viewModelScope.launch{
            supersetRepository.createSuperset(superset)
        }
    }

    fun updateSuperset(supersetId: Int, superset: SupersetPost){
        viewModelScope.launch{
            supersetRepository.updateSuperset(supersetId, superset)
        }
    }

    private fun loadExercises() {
        viewModelScope.launch {
            val userId = sharedPreferences.getString("USER_ID", null)?.toIntOrNull() ?: 0

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