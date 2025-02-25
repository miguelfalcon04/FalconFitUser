package com.example.falconfituser.ui.superset

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.falconfituser.data.superset.ISupersetRepository
import com.example.falconfituser.data.superset.Superset
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
class SupersetListViewModel @Inject constructor(
    private val supersetRepository: ISupersetRepository,
    private val sharedPreferences: SharedPreferences
    ): ViewModel() {
    private val _uiState = MutableStateFlow<SupersListUiState>(SupersListUiState.Loading)
    val uiState: StateFlow<SupersListUiState>
        get() = _uiState.asStateFlow()

    // Explicación en ExerciseListViewModel
    private var collectionJob: Job? = null

    init{
        loadSupersets()

        collectionJob?.cancel()

        collectionJob = viewModelScope.launch {
            withContext(Dispatchers.Main){
                supersetRepository.setStream.collect{ supersList ->
                    if(supersList.isEmpty()){
                        _uiState.value = SupersListUiState.Loading
                    }else{
                        _uiState.value = SupersListUiState.Success(supersList)
                    }
                }
            }
        }
    }

    fun loadSupersets(){
        viewModelScope.launch {
            val userId = sharedPreferences.getString("USER_ID", null)?.toIntOrNull() ?: 0

            val supersets = supersetRepository.readAll(userId)

            if(supersets.isNotEmpty()){
                SupersListUiState.Success(supersets)
            }else{
                SupersListUiState.Error("Error al obtener los supersets")
            }
        }
    }

    fun deleteSuperset(supersetId: Int){
        viewModelScope.launch {
            supersetRepository.deleteSuperset(supersetId)
            loadSupersets()
        }
    }

    override fun onCleared() {
        super.onCleared()
        collectionJob?.cancel() // Limpio al destruir el ViewModel
    }

}

sealed class SupersListUiState(){
    data object Loading: SupersListUiState()
    class Success(val supersList: List<Superset>): SupersListUiState()
    class Error(val message: String): SupersListUiState()
}