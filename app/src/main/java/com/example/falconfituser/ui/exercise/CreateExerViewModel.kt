package com.example.falconfituser.ui.exercise

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.falconfituser.data.exercise.Exercise
import com.example.falconfituser.data.exercise.IExerciseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateExerViewModel @Inject constructor(
    private val exerciseRepository: IExerciseRepository,
): ViewModel(){
    private val _uiState = MutableStateFlow<CreateExerUiState>(CreateExerUiState.Loading)
    val uiState: StateFlow<CreateExerUiState>
        get() = _uiState.asStateFlow()

    fun createExercise(exercise: Exercise, photo: Uri?){
        viewModelScope.launch{
            exerciseRepository.createExercise(exercise, photo)
        }
    }

    fun updateExercise(exerciseId: Int, exercise: Exercise, photo: Uri?){
        viewModelScope.launch{
            exerciseRepository.updateExercise(exerciseId, exercise, photo)
        }
    }
}

sealed class CreateExerUiState(){
    data object Loading: CreateExerUiState()
    class Success(val exrcList: List<Exercise>): CreateExerUiState()
    class Error(val message: String): CreateExerUiState()
}