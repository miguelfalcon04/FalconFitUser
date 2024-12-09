package com.example.falconfituser.ui.superset

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.falconfituser.data.api.superset.SupersetRaw
import com.example.falconfituser.data.exercise.Exercise
import com.example.falconfituser.data.superset.ISupersetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateUpdateSupersViewModel @Inject constructor(
    private val supersetRepository: ISupersetRepository
): ViewModel(){
    private val _uiState = MutableStateFlow<CreateUpdateSupersUiState>(CreateUpdateSupersUiState.Loading)
    val uiState: StateFlow<CreateUpdateSupersUiState>
        get() = _uiState.asStateFlow()

    fun createSuperset(superset: SupersetRaw){
        viewModelScope.launch{
            supersetRepository.createSuperset(superset)
        }
    }

    fun updateSuperset(supersetId: Int, superset: SupersetRaw){
        viewModelScope.launch{
            supersetRepository.updateSuperset(supersetId, superset)
        }
    }
}

sealed class CreateUpdateSupersUiState(){
    data object Loading: CreateUpdateSupersUiState()
    class Success(val supersList: List<Exercise>): CreateUpdateSupersUiState()
    class Error(val message: String): CreateUpdateSupersUiState()
}