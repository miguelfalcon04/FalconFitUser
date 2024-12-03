package com.example.falconfituser.ui.exercise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.falconfituser.data.exercise.Exercise
import com.example.falconfituser.data.exercise.IExerciseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ExerciseListViewModel @Inject constructor(
    private val exerciseRepository: IExerciseRepository
): ViewModel(){
    private val _uiState = MutableStateFlow<ExercListUiState>(ExercListUiState.Loading)
    val uiState: StateFlow<ExercListUiState>
        get() = _uiState.asStateFlow()

    init {
        viewModelScope.launch{
            withContext(Dispatchers.Main){
                exerciseRepository.setStream.collect{
                        exercList ->
                    if(exercList.isEmpty()){
                        _uiState.value = ExercListUiState.Loading
                    }else{
                        _uiState.value = ExercListUiState.Success(exercList)
                    }
                }
            }
        }
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                exerciseRepository.readAll()
            }
        }
    }
}

sealed class ExercListUiState(){
    data object Loading: ExercListUiState()
    class Success(val exrcList: List<Exercise>): ExercListUiState()
    class Error(val message: String): ExercListUiState()
}